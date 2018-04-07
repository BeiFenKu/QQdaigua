package com.king.Fragment1;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.king.qqdaigua.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment1 extends Fragment {
    private View view;
    private ImageView image_touxiang;

    public BlankFragment1() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.blank_fragment1, container, false);
        init();

//        Bitmap bmSrc = BitmapFactory.decodeFile()

        return view;
    }

    private void init() {
        image_touxiang = (ImageView) view.findViewById(R.id.image_touxiang);
//        rotation(旋转角度).setDuration(动画时间)
        image_touxiang.animate().rotation(360).setDuration(1000);
        image_touxiang.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new dialog_admin().show(getFragmentManager(), "123");
                return true;
            }
        });
    }

}
