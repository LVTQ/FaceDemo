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


public class SearchFragment extends Fragment implements OnClickListener {
	private ImageView iv_search1;
	private ImageView iv_search2;
	private Button ib_search_enter;
	private Button ib_search_choice1;
	private Button ib_search_choice2;

	private TextView tv_search;
	private int search;

	private Bitmap scalingPhoto1;
	private Bitmap scalingPhoto2;

	private Paint paint;// 画笔
	private View view;
	private GifView gif;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_search, container, false);

		iv_search1 = (ImageView) view.findViewById(R.id.iv_search1);
		iv_search2 = (ImageView) view.findViewById(R.id.iv_search2);
		ib_search_enter = (Button) view.findViewById(R.id.ib_search_enter);
		ib_search_choice1 = (Button) view.findViewById(R.id.ib_search_choice1);
		ib_search_choice2 = (Button) view.findViewById(R.id.ib_search_choice2);

		tv_search = (TextView) view.findViewById(R.id.tv_search);

		gif = (GifView) view.findViewById(R.id.gif);

		ib_search_enter.setOnClickListener(this);
		ib_search_choice1.setOnClickListener(this);
		ib_search_choice2.setOnClickListener(this);

		paint = new Paint();

		scalingPhoto1 = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.pic1);
		scalingPhoto2 = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.pic2);
		return view;
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String str = (String) msg.obj;
			System.out.println("*******search:" + str);
			if (str.equals("403") || str.equals("400") || str.equals("413")
					|| str.equals("500")) {// 试用 API Key 无法使用 <param>
											// 对应的参数。请勿传入此参数。或者使用正式 API Key
											// 调用。但是，第二次可以使用，这是一个bug
				Toast.makeText(getActivity(), "Please Try Again",
						Toast.LENGTH_SHORT).show();
			} else {
				try {
					JSONObject resultJSON = new JSONObject(str);
					if (resultJSON.getString("faces1").equals("[]")) {
						Toast.makeText(getActivity(),
								"The first picture is not a face image",
								Toast.LENGTH_SHORT).show();
					} else if (resultJSON.getString("faces2").equals("[]")) {
						Toast.makeText(getActivity(),
								"The second picture is not a face image",
								Toast.LENGTH_SHORT).show();
					} else {
						search = DrawUtil.SearchPrepareBitmap(resultJSON,
								scalingPhoto1, scalingPhoto2, paint,
								iv_search1, iv_search2);

						
						tv_search.setText("置信度:有" + search + "%的可能性是同一个人");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			gif.setVisibility(View.GONE);// 停止gif
		};
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			//
			if (data != null) {
				//
				String photoPath1 = ImageUtil.getPhotoPath(getActivity(), data);
				//
				scalingPhoto1 = ImageUtil.getScalingPhoto(photoPath1);
				//
				iv_search1.setImageBitmap(scalingPhoto1);
			}
		}
		if (requestCode == 2) {
			//
			if (data != null) {
				//
				String photoPath2 = ImageUtil.getPhotoPath(getActivity(), data);
				//
				scalingPhoto2 = ImageUtil.getScalingPhoto(photoPath2);
				//
				iv_search2.setImageBitmap(scalingPhoto2);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_search_choice1:
			//
			Intent intent1 = new Intent();
			intent1.setAction(Intent.ACTION_PICK);
			intent1.setType("image/*");
			startActivityForResult(intent1, 1);//
			break;
		case R.id.ib_search_choice2:
			//
			Intent intent2 = new Intent();
			intent2.setAction(Intent.ACTION_PICK);
			intent2.setType("image/*");
			startActivityForResult(intent2, 2);//
			break;
		case R.id.ib_search_enter:
			// 显示加载动画
			gif.setVisibility(View.VISIBLE);
			gif.setMovieResource(R.raw.red); // 设置背景gif图片资源
			// 确定按钮不允许再次点击

			//
			String base64ImageEncode1 = ImageUtil
					.getBase64ImageEncode(scalingPhoto1);
			String base64ImageEncode2 = ImageUtil
					.getBase64ImageEncode(scalingPhoto2);

			//
			final Map<String, Object> map = new HashMap<String, Object>();
			map.put("api_key", Constant.Key);
			map.put("api_secret", Constant.Secret);
			map.put("image_base64_1", base64ImageEncode1);
			map.put("image_base64_2", base64ImageEncode2);

			//
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						String result = HttpUtils.post(Constant.URL_COMPARE,
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
