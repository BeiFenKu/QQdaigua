package com.king.Login_Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.king.qqdaigua.R;

/**
 * Created by KingLee on 2018/4/11.
 */

public class dialog1_admin extends DialogFragment {

    private View view1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view1 = inflater.inflate(R.layout.dialog1_admin, null);
        getDialog().setTitle("登录失败原因");


        return view1;
    }
}
