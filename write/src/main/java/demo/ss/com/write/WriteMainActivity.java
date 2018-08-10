package demo.ss.com.write;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import demo.ss.com.baselibrary.ToastManager;


public class WriteMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_activity_main);
    }


    public void WriteToast(View view){
        ToastManager.show(this,"WrietToast");
    }
}
