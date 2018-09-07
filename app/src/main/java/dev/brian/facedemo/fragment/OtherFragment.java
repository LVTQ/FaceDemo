package dev.brian.facedemo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import dev.brian.facedemo.R;

/**
 * 作者: jiayi.zhang
 * 时间: 2017/11/6
 * 描述:
 */

public class OtherFragment extends Fragment implements View.OnClickListener {

    private View view;
    private ImageView tv_other1;
    private ImageView tv_other2;
    private ImageView tv_other3;
    private ImageView tv_other4;

    private FragmentManager fm;
    private FragmentTransaction transaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_other,container,false);
        tv_other1 = (ImageView) view.findViewById(R.id.tv_other1);
        tv_other2 = (ImageView) view.findViewById(R.id.tv_other2);
        tv_other3 = (ImageView) view.findViewById(R.id.tv_other3);
        tv_other4 = (ImageView) view.findViewById(R.id.tv_other4);

        tv_other1.setOnClickListener(this);
        tv_other2.setOnClickListener(this);
        tv_other3.setOnClickListener(this);
        tv_other4.setOnClickListener(this);

        fm = getFragmentManager();

        return view;
    }

    @Override
    public void onClick(View v) {
        transaction = fm.beginTransaction();
        switch (v.getId()) {
            case R.id.tv_other1:
                transaction.replace(R.id.ll_fregment, new FaceFragment());
                break;
            case R.id.tv_other2:
                transaction.replace(R.id.ll_fregment, new OcridcardFragment());
                break;
            case R.id.tv_other3:
                transaction.replace(R.id.ll_fregment, new OcrdriverlicenseFragment());
                break;
            case R.id.tv_other4:
                transaction.replace(R.id.ll_fregment, new OcrvehiclelicenseFragment());
                break;
        }
        transaction.commit();
    }
}
