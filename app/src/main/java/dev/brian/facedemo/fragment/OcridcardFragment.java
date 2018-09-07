package dev.brian.facedemo.fragment;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import dev.brian.facedemo.R;
import dev.brian.facedemo.util.Constant;
import dev.brian.facedemo.util.DrawUtil;
import dev.brian.facedemo.util.GifView;
import dev.brian.facedemo.util.HttpUtils;
import dev.brian.facedemo.util.ImageUtil;

/**
 * 证件识别
 *
 * @author Administrator
 */
public class OcridcardFragment extends Fragment implements OnClickListener {
    private ImageView iv_ocridcard;//
    private Button ib_ocridcard_enter;
    private Button ib_ocridcard_choice;

    private TextView tv_ocridcard;

    private Bitmap scalingPhoto;// 位图

    private String ocridcard;

    private Paint paint;// 画笔工具
    private View view;
    private GifView gif;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ocridcard, container, false);

        iv_ocridcard = (ImageView) view.findViewById(R.id.iv_ocridcard);
        ib_ocridcard_enter = (Button) view.findViewById(R.id.ib_ocridcard_enter);
        ib_ocridcard_choice = (Button) view.findViewById(R.id.ib_ocridcard_choice);

        tv_ocridcard = (TextView) view.findViewById(R.id.tv_ocridcard);
        gif = (GifView) view.findViewById(R.id.gif);
        ib_ocridcard_enter.setOnClickListener(this);
        ib_ocridcard_choice.setOnClickListener(this);

        paint = new Paint();// 创建画笔

        scalingPhoto = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.idcard);
        return view;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String str = (String) msg.obj;
            System.out.println("********ocridcard:" + str);
            if (str.equals("403") || str.equals("400") || str.equals("413")
                    || str.equals("500")) {
                Toast.makeText(getActivity(), "Please Try Again",
                        Toast.LENGTH_SHORT).show();
            } else {
                try {
                    JSONObject resultJSON = new JSONObject(str);
                    System.out.println(resultJSON.getString("cards") + "=====");
                    if (resultJSON.getString("cards").equals("[]")) {
                        Toast.makeText(getActivity(),
                                "Try Again", Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        ocridcard = DrawUtil.ocridcardPrepareBitmap(resultJSON,
                                scalingPhoto, paint, iv_ocridcard);

                        System.out.println("------------ocridcard" + ocridcard);

                        tv_ocridcard.setText("证件信息:" + ocridcard);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            gif.setVisibility(View.GONE);// 停止gif
        }

        ;
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            //
            if (data != null) {
                //
                String photoPath = ImageUtil.getPhotoPath(getActivity(), data);
                //
                scalingPhoto = ImageUtil.getScalingPhoto(photoPath);
                //
                iv_ocridcard.setImageBitmap(scalingPhoto);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_ocridcard_choice:
                //
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);//
                break;
            case R.id.ib_ocridcard_enter:
                // 显示加载动画
                gif.setVisibility(View.VISIBLE);
                gif.setMovieResource(R.raw.red); // 设置背景gif图片资源
                // 确定按钮不允许再次点击

                //
                String base64ImageEncode = ImageUtil
                        .getBase64ImageEncode(scalingPhoto);
                System.out.println(base64ImageEncode);
                //
                final Map<String, Object> map = new HashMap<String, Object>();
                map.put("api_key", Constant.Key);
                map.put("api_secret", Constant.Secret);
                map.put("image_base64", base64ImageEncode);

                //
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String result = HttpUtils.post(Constant.URL_OCRIDCARD,
                                    map);

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
