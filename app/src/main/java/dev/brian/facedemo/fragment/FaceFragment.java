package dev.brian.facedemo.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.brian.facedemo.R;
import dev.brian.facedemo.util.Constant;
import dev.brian.facedemo.util.DrawUtil;
import dev.brian.facedemo.util.GifView;
import dev.brian.facedemo.util.HttpUtils;
import dev.brian.facedemo.util.ImageUtil;

/**
 * 作者: jiayi.zhang
 * 时间: 2017/11/6
 * 描述:
 */

public class FaceFragment extends Fragment implements View.OnClickListener {
    private ImageView iv_face;
    private Button ib_face_enter;
    private Button ib_face_choice;

    private TextView tv_face_gender;
    private TextView tv_face_age;
    private TextView tv_face_beauty;

    private Bitmap scalingPhoto;

    private String gender;
    private int age;
    private int beauty;

    private Paint paint;
    private View view;
    private GifView gif;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_face, container, false);
        iv_face = (ImageView) view.findViewById(R.id.iv_face);
        ib_face_enter = (Button) view.findViewById(R.id.ib_face_enter);
        ib_face_choice = (Button) view.findViewById(R.id.ib_face_choice);

        tv_face_gender = (TextView) view.findViewById(R.id.tv_face_gender);
        tv_face_age = (TextView) view.findViewById(R.id.tv_face_age);
        tv_face_beauty = (TextView) view.findViewById(R.id.tv_face_beauty);

        gif = (GifView) view.findViewById(R.id.gif);

        ib_face_enter.setOnClickListener(this);
        ib_face_choice.setOnClickListener(this);

        paint = new Paint();
        scalingPhoto = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.defualt);
        return view;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String str = (String) msg.obj;
            System.out.println("*******face:" + str);
            if (str.equals("403") || str.equals("400") || str.equals("413") || str.equals("500")) {
                Toast.makeText(getActivity(), "请再试一次", Toast.LENGTH_SHORT).show();

            } else {
                try {
                    JSONObject resultJSON = new JSONObject(str);
                    System.out.println(resultJSON.getString("faces") + "=====");
                    if (resultJSON.getString("faces").equals("[]")) {
                        Toast.makeText(getActivity(), "这是一个张没有排到脸的照片", Toast.LENGTH_SHORT).show();

                    } else {
                        List res = DrawUtil.FacePrepareBitmap(resultJSON,
                                scalingPhoto, paint, iv_face);
                        gender = (String) res.get(0);
                        age = (Integer) res.get(1);
                        beauty = (Integer) res.get(2);

                        System.out.println("------------" + gender + " " + age
                                + " " + " " + beauty);
                        tv_face_gender.setText("性别:" + ImageUtil.getFaceGender(gender));
                        tv_face_age.setText("年龄:" + age);
                        tv_face_beauty.setText("颜值:" + beauty);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            gif.setVisibility(View.GONE);
        };
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {

            if (data != null) {

                String photoPath = ImageUtil.getPhotoPath(getActivity(), data);

                scalingPhoto = ImageUtil.getScalingPhoto(photoPath);

                iv_face.setImageBitmap(scalingPhoto);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.ib_face_choice:

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
                break;
            case R.id.ib_face_enter:

                gif.setVisibility(View.VISIBLE);
                gif.setMovieResource(R.raw.red);
                String base64ImageEncode = ImageUtil
                        .getBase64ImageEncode(scalingPhoto);
                System.out.println(base64ImageEncode);

                final Map<String, Object> map = new HashMap<String, Object>();
                map.put("api_key", Constant.Key);
                map.put("api_secret", Constant.Secret);
                map.put("return_attributes", "gender,age,beauty");
                map.put("image_base64", base64ImageEncode);


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String result = HttpUtils
                                    .post(Constant.URL_DETECT, map);
                            Message message = new Message();
                            message.obj = result;
                            handler.sendMessage(message);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;

        }
    }
}
