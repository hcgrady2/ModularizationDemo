package demo.ss.com.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.callback.NavCallback;
import com.alibaba.android.arouter.launcher.ARouter;

import demo.ss.com.baselibrary.ToastManager;


@Route(path = "/read/ActivityMain")
public class ReadMainActivity extends AppCompatActivity {
    private static final String TAG="ReadMainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_activity_main);
    }

    public void ReadToast(View view){
        ToastManager.show(this,"ReadToast");
    }

    public void ReadToWrite(View view){

        ARouter.getInstance().build("/write/ActivityWrite").navigation(this, new NavCallback() {

            @Override
            public void onFound(Postcard postcard) {
                Log.i(TAG, "onFound: ");
            }

            @Override
            public void onLost(Postcard postcard) {
                Log.i(TAG, "onLost: ");
            }

            @Override
            public void onArrival(Postcard postcard) {
                Log.i(TAG, "onArrival: ");
            }

            @Override
            public void onInterrupt(Postcard postcard) {
                Log.i(TAG, "onInterrupt: ");
            }
        });




    }
}
