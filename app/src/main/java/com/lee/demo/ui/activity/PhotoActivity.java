package com.lee.demo.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 拍照和相册选择图片的Demo
 */
import com.lee.demo.R;

import java.io.File;

import cn.lee.cplibrary.util.LogUtil;
import cn.lee.cplibrary.util.SharedPreferencesUtils;
import cn.lee.cplibrary.util.takephotos.PhotoUtil;
import cn.lee.cplibrary.util.timer.TimeUtils;

public class PhotoActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        PhotoUtil.init(this, savedInstanceState);
        imageView = (ImageView) findViewById(R.id.imageView);
        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoUtil.showPicChooseDialog(PhotoActivity.this);
            }
        });
        textView = (TextView) findViewById(R.id.textView);
        SharedPreferencesUtils.putShareValue(this,"user","李翠侠");
        textView.setText( SharedPreferencesUtils.getShareString(this,"user"));

        LogUtil.i("",this,"1111111111111111111111111");
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        File file = PhotoUtil.getImageFile(this, requestCode, resultCode, data, String.valueOf(TimeUtils.getCurTimeMillis()));
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        imageView.setImageBitmap(bitmap);
    }

}
