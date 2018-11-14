package cn.lee.cplibrary.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cn.lee.cplibrary.R;
import cn.lee.cplibrary.util.permissionutil.PermissionDemoActivity;
import cn.lee.cplibrary.widget.sign.SignDemoActivity;
import cn.lee.cplibrary.widget.statelayout.SampleActivity;


public class CPMainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cp_activity_cpmain);
        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button1) {
            startActivity(new Intent(getseSelfActivity(), SampleActivity.class));
        }else if (v.getId() == R.id.button2) {
            startActivity(new Intent(getseSelfActivity(), PermissionDemoActivity.class));
        }else if (v.getId() == R.id.button3) {
            startActivity(new Intent(getseSelfActivity(), SignDemoActivity.class));
        }
    }

    public Activity getseSelfActivity() {
        return this;
    }
}
