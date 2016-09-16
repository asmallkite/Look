package lizheng.www.look;

import android.app.Application;

/**
 * Created by 10648 on 2016/9/16 0016.
 */
public class MyApplication extends Application {

    public static MyApplication myApplication;

    public static Application getContext() {
        return myApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
    }
}
