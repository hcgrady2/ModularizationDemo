package demo.ss.com.baselibrary;

import android.content.Context;
import android.widget.Toast;


public class ToastManager {

    private ToastManager() {
    }

    public static void show(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }



}
