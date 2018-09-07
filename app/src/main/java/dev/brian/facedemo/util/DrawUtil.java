package dev.brian.facedemo.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 作者: jiayi.zhang
 * 时间: 2017/11/8
 * 描述:
 */

public class DrawUtil {
    private static float y1;
    private static float x1;
    private static float w1;
    private static float h1;
    private static float y2;
    private static float x2;
    private static float w2;
    private static float h2;

    private static final String TAG = "DrawUtil";

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static List FacePrepareBitmap(JSONObject jsObject,
                                         Bitmap scalingPhoto, Paint paint, ImageView imageView)
            throws JSONException {
        List attributesList = new ArrayList();
        Bitmap bitmapPre = Bitmap.createBitmap(scalingPhoto.getWidth(),
                scalingPhoto.getHeight(), scalingPhoto.getConfig());
        Canvas canvas = new Canvas(bitmapPre);
        canvas.drawBitmap(scalingPhoto, 0, 0, null);

        final JSONArray jsonArray = jsObject.getJSONArray("faces");
        Integer num = jsonArray.length();
        System.out.println(num);
        for (int i = 0; i < num; i++) {
            JSONObject face = jsonArray.getJSONObject(i);
            JSONObject postion = face.getJSONObject("face_rectangle");
            float y = (float) postion.getDouble("top");
            float x = (float) postion.getDouble("left");

            float w = (float) postion.getDouble("width");
            float h = (float) postion.getDouble("height");
            System.out.println("x:" + x + " y:" + y + " w:" + w + " h" + h);

            System.out.println(bitmapPre.getWidth() + "   "
                    + bitmapPre.getHeight());
            System.out.println("x:" + x + " y:" + y + " w:" + w + " h" + h);
            // 设置画布颜色
            paint.setColor(Color.RED);
            paint.setStrokeWidth(5);
            // 画线
            canvas.drawLine(x, y, x + w, y, paint);
            canvas.drawLine(x, y + h, x + w, y + h, paint);
            canvas.drawLine(x, y, x, y + h, paint);
            canvas.drawLine(x + w, y, x + w, y + h, paint);

            JSONObject attributes = face.getJSONObject("attributes");
            int age = attributes.getJSONObject("age").getInt("value");
            String gender = attributes.getJSONObject("gender").getString(
                    "value");
            int beauty;
            if (gender.equals("Male")) {
                beauty = attributes.getJSONObject("beauty")
                        .getInt("male_score");
            } else {
                beauty = attributes.getJSONObject("beauty").getInt(
                        "female_score");
            }
            System.out.println("age:" + age + "  gender:" + gender + "beauty:"
                    + beauty);

            scalingPhoto = bitmapPre;
            imageView.setImageBitmap(scalingPhoto);

            attributesList.add(gender);
            attributesList.add(age);
            attributesList.add(beauty);
        }
        return attributesList;
    }

    public static String GesturePrepareBitmap(JSONObject jsObject,
                                              Bitmap scalingPhoto, Paint paint, ImageView imageView)
            throws JSONException {
        String gestureEN = "";
        Bitmap bitmapPre = Bitmap.createBitmap(scalingPhoto.getWidth(),
                scalingPhoto.getHeight(), scalingPhoto.getConfig());
        Canvas canvas = new Canvas(bitmapPre);
        canvas.drawBitmap(scalingPhoto, 0, 0, null);

        final JSONArray jsonArray = jsObject.getJSONArray("hands");
        Integer num = jsonArray.length();
        for (int i = 0; i < num; i++) {
            JSONObject hands = jsonArray.getJSONObject(i);
            JSONObject postion = hands.getJSONObject("hand_rectangle");
            JSONObject gesture = hands.getJSONObject("gesture");

            Map<String, Double> mapGesture = ImageUtil.GetMapGesture(gesture);

            String gestureUS = ImageUtil.getGestureUS(mapGesture);
            gestureEN = ImageUtil.getGestureENS(gestureUS);
            Log.i(TAG, "GesturePrepareBitmap: "+ gestureEN);;

            float y = (float) postion.getDouble("top");
            float x = (float) postion.getDouble("left");

            float w = (float) postion.getDouble("width");
            float h = (float) postion.getDouble("height");
            System.out.println("x:" + x + " y:" + y + " w:" + w + " h" + h);

            System.out.println(bitmapPre.getWidth() + "   "
                    + bitmapPre.getHeight());
            System.out.println("x:" + x + " y:" + y + " w:" + w + " h" + h);

            paint.setColor(Color.RED);
            paint.setStrokeWidth(5);
            //
            canvas.drawLine(x, y, x + w, y, paint);//
            canvas.drawLine(x, y + h, x + w, y + h, paint);//
            canvas.drawLine(x, y, x, y + h, paint);//
            canvas.drawLine(x + w, y, x + w, y + h, paint);//

            scalingPhoto = bitmapPre;
            imageView.setImageBitmap(scalingPhoto);
        }
        return gestureEN;
    }

    public static int SearchPrepareBitmap(JSONObject jsObject,
                                          Bitmap scalingPhoto1, Bitmap scalingPhoto2, Paint paint,
                                          ImageView imageView1, ImageView imageView2) throws JSONException {
        int search = jsObject.getInt("confidence");//
        Bitmap bitmapPre1 = Bitmap.createBitmap(scalingPhoto1.getWidth(),
                scalingPhoto1.getHeight(), scalingPhoto1.getConfig());
        Bitmap bitmapPre2 = Bitmap.createBitmap(scalingPhoto2.getWidth(),
                scalingPhoto2.getHeight(), scalingPhoto2.getConfig());
        Canvas canvas1 = new Canvas(bitmapPre1);
        Canvas canvas2 = new Canvas(bitmapPre2);
        canvas1.drawBitmap(scalingPhoto1, 0, 0, null);
        canvas2.drawBitmap(scalingPhoto2, 0, 0, null);

        final JSONArray jsonArray1 = jsObject.getJSONArray("faces1");
        for (int i = 0; i < jsonArray1.length(); i++) {
            JSONObject hands = jsonArray1.getJSONObject(i);
            JSONObject postion = hands.getJSONObject("face_rectangle");

            y1 = (float) postion.getDouble("top");
            x1 = (float) postion.getDouble("left");
            w1 = (float) postion.getDouble("width");
            h1 = (float) postion.getDouble("height");

            System.out.println("x1:" + x1 + " y1:" + y1 + " w1:" + w1 + " h1"
                    + h1);
        }

        final JSONArray jsonArray2 = jsObject.getJSONArray("faces2");
        for (int i = 0; i < jsonArray2.length(); i++) {
            JSONObject hands = jsonArray2.getJSONObject(i);
            JSONObject postion = hands.getJSONObject("face_rectangle");
            y2 = (float) postion.getDouble("top");
            x2 = (float) postion.getDouble("left");
            w2 = (float) postion.getDouble("width");
            h2 = (float) postion.getDouble("height");

            System.out.println("x2:" + x2 + " y2:" + y2 + " w2:" + w2 + " h2"
                    + h2);
        }

        paint.setColor(Color.RED);
        paint.setStrokeWidth(5);

        //
        canvas1.drawLine(x1, y1, x1 + w1, y1, paint);//
        canvas1.drawLine(x1, y1 + h1, x1 + w1, y1 + h1, paint);//
        canvas1.drawLine(x1, y1, x1, y1 + h1, paint);//
        canvas1.drawLine(x1 + w1, y1, x1 + w1, y1 + h1, paint);//

        canvas2.drawLine(x2, y2, x2 + w2, y2, paint);//
        canvas2.drawLine(x2, y2 + h2, x2 + w2, y2 + h2, paint);//
        canvas2.drawLine(x2, y2, x2, y2 + h2, paint);//
        canvas2.drawLine(x2 + w2, y2, x2 + w2, y2 + h2, paint);//

        scalingPhoto1 = bitmapPre1;
        scalingPhoto2 = bitmapPre2;
        imageView1.setImageBitmap(scalingPhoto1);
        imageView2.setImageBitmap(scalingPhoto2);

        return search;
    }

    //
    public static String ocridcardPrepareBitmap(JSONObject jsObject,
                                                Bitmap scalingPhoto, Paint paint, ImageView imageView)
            throws JSONException {
        String res = "";
        final JSONArray jsonArray1 = jsObject.getJSONArray("cards");
        for (int i = 0; i < jsonArray1.length(); i++) {
            JSONObject card = jsonArray1.getJSONObject(i);
            res += card.getString("name");
            res += card.getString("gender");
            res += card.getString("id_card_number");
            res += card.getString("birthday");
            res += card.getString("address");
        }
        return res;
    }

    //
    public static String ocrdriverlicensePrepareBitmap(JSONObject jsObject,
                                                       Bitmap scalingPhoto, Paint paint, ImageView imageView)
            throws JSONException {
        String res = "";
        final JSONArray jsonArray1 = jsObject.getJSONArray("cards");
        for (int i = 0; i < jsonArray1.length(); i++) {
            JSONObject card = jsonArray1.getJSONObject(i);
            res += card.getString("gender");
            res += card.getString("issued_by");
            res += card.getString("license_number");
            res += card.getString("address");
        }
        return res;
    }

    //
    public static String ocrvehiclelicensePrepareBitmap(JSONObject jsObject,
                                                        Bitmap scalingPhoto, Paint paint, ImageView imageView)
            throws JSONException {
        String res = "";
        final JSONArray jsonArray1 = jsObject.getJSONArray("cards");
        for (int i = 0; i < jsonArray1.length(); i++) {
            JSONObject card = jsonArray1.getJSONObject(i);
            res += card.getString("vehicle_type");
            res += card.getString("issued_by");
            res += card.getString("plate_no");
            res += card.getString("address");
        }
        return res;
    }
}
