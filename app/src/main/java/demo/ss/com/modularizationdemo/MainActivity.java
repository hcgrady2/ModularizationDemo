package demo.ss.com.modularizationdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;

import demo.ss.com.baselibrary.ToastManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void Toast(View view) {
        ToastManager.show(this,"MainToast");

    }

    public void Read(View view) {
        //发起路由跳转
        ARouter.getInstance().build("/read/ActivityMain").navigation();
    }

}
