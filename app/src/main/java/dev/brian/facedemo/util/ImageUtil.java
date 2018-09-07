package dev.brian.facedemo.util;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 作者: jiayi.zhang
 * 时间: 2017/11/8
 * 描述:
 */

public class ImageUtil {

    public static String getBase64ImageEncode(Bitmap myImage) {
        Bitmap bmSmall = Bitmap.createBitmap(myImage, 0, 0, myImage.getWidth(), myImage.getHeight());
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmSmall.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] arrays = stream.toByteArray();
        // base64 encode
        byte[] encode = Base64.encode(arrays, Base64.DEFAULT);
        String base64Encode = new String(encode);
        return base64Encode;
    }

    public static Bitmap getScalingPhoto(String path) {

        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(path, options);

        double ratio = Math.max(options.outWidth * 1.0d / 1024f, options.outHeight * 1.0d / 1024f);
        options.inSampleSize = (int) Math.ceil(ratio);

        options.inJustDecodeBounds = false;

        Bitmap photoImage = BitmapFactory.decodeFile(path, options);

        return photoImage;
    }


    public static String getPhotoPath(Context context, Intent data) {
        if (data != null) {

            Uri uri = data.getData();

            Cursor query = context.getContentResolver().query(uri, null, null, null, null);
            query.moveToFirst();

            int idx = query.getColumnIndex(MediaStore.Images.ImageColumns.DATA);

            String photoStr = query.getString(idx);
            query.close();
            return photoStr;
        } else {
            Toast.makeText(context, "Try Again", Toast.LENGTH_SHORT).show();
            return null;
        }
    }


    public static Map<String, Double> GetMapGesture(JSONObject gesture) {
        Map<String, Double> mapGesture = new HashMap<String, Double>();
        try {
            mapGesture.put("unknown", gesture.getDouble("unknown"));
            mapGesture.put("heart_a", gesture.getDouble("heart_a"));
            mapGesture.put("heart_b", gesture.getDouble("heart_b"));
            mapGesture.put("heart_c", gesture.getDouble("heart_c"));
            mapGesture.put("heart_d", gesture.getDouble("heart_d"));
            mapGesture.put("ok", gesture.getDouble("ok"));
            mapGesture.put("hand_open", gesture.getDouble("hand_open"));
            mapGesture.put("thumb_up", gesture.getDouble("thumb_up"));
            mapGesture.put("thumb_down", gesture.getDouble("thumb_down"));
            mapGesture.put("rock", gesture.getDouble("rock"));
            mapGesture.put("namaste", gesture.getDouble("namaste"));
            mapGesture.put("palm_up", gesture.getDouble("palm_up"));
            mapGesture.put("fist", gesture.getDouble("fist"));
            mapGesture.put("index_finger_up", gesture.getDouble("index_finger_up"));
            mapGesture.put("double_finger_up", gesture.getDouble("double_finger_up"));
            mapGesture.put("victory", gesture.getDouble("victory"));
            mapGesture.put("big_v", gesture.getDouble("big_v"));
            mapGesture.put("phonecall", gesture.getDouble("phonecall"));
            mapGesture.put("beg", gesture.getDouble("beg"));
            mapGesture.put("thanks", gesture.getDouble("thanks"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mapGesture;
    }

    public static String getGestureUS(Map<String, Double> map) {
        Double max = 0.0;
        String result = null;
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            if (entry.getValue() > max) {
                result = entry.getKey();
                if (result != null)
                    max = entry.getValue();
            }
        }
        return result;
    }


    public static String getGestureENS(String str) {
        String gesture = null;
        if (str.equals("unknown")) {
            gesture = "UNKNOWN";
        } else if (str.equals("heart_a")) {
            gesture = "A";
        } else if (str.equals("heart_b")) {
            gesture = "B";
        } else if (str.equals("heart_c")) {
            gesture = "C";
        } else if (str.equals("heart_d")) {
            gesture = "D";
        } else if (str.equals("ok")) {
            gesture = "OK";
        } else if (str.equals("hand_open")) {
            gesture = "open";
        } else if (str.equals("thumb_up")) {
            gesture = "up";
        } else if (str.equals("thumb_down")) {
            gesture = "down";
        } else if (str.equals("rock")) {
            gesture = "ROCK";
        } else if (str.equals("namaste")) {
            gesture = "namaste";
        } else if (str.equals("palm_up")) {
            gesture = "palm_up";
        } else if (str.equals("fist")) {
            gesture = "fist";
        } else if (str.equals("index_finger_up")) {
            gesture = "index_finger_up";
        } else if (str.equals("double_finger_up")) {
            gesture = "double_finger_up";
        } else if (str.equals("victory")) {
            gesture = "victory";
        } else if (str.equals("big_v")) {
            gesture = "big_v";
        } else if (str.equals("phonecall")) {
            gesture = "phonecall";
        } else if (str.equals("beg")) {
            gesture = "beg";
        } else if (str.equals("thanks")) {
            gesture = "thanks";
        }
        return gesture;
    }

    public static String getFaceGender(String str){
        String gender="";
        if(str.equals("Male")){
            gender="男";
        }else if(str.equals("Female")){
            gender="女";
        }else{
            gender="";
        }
        return gender;
    }

}
