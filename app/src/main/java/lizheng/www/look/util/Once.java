package lizheng.www.look.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 10648 on 2016/9/16 0016.
 *
 */
public class Once {

    SharedPreferences mSharedPreferences;
    Context mContext;

    public Once(Context context) {
        mSharedPreferences = context.getSharedPreferences("key", Context.MODE_PRIVATE);
        mContext = context;
    }

    public void show(String tagKey, OnceCallback callback) {
        boolean isSecondTime = mSharedPreferences.getBoolean(tagKey, false);
        if (! isSecondTime) {
            callback.onOnce();
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean(tagKey, true);
            editor.apply();
        }
    }

    public interface OnceCallback {
        void onOnce();
    }
}
