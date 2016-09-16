package lizheng.www.look.api;

import java.io.File;
import java.io.IOException;

import lizheng.www.look.MyApplication;
import lizheng.www.look.util.NetWorkUtil;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 10648 on 2016/9/16 0016.
 */
public class ApiManage {

    public static ApiManage apiManage;
    private Object monitor = new Object();

    private static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            if (NetWorkUtil.isNetWorkAvailable(MyApplication.getContext())) {
                int maxAge = 60; // 在线缓存在1分钟内可读取
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            } else {
                int maxStale = 60 * 60 * 24 * 28; // 离线时缓存保存4周
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
        }
    };

    private static File httpCacheDirectory = new File(MyApplication.getContext().getCacheDir(), "zhihuCache");
    private static int cacheSize = 10 * 1024 * 1024; // 10 MiB
    private static Cache cache = new Cache(httpCacheDirectory, cacheSize);

    private static OkHttpClient client = new OkHttpClient.Builder()
            .addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
            .addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
            .cache(cache)
            .build();

    public static ApiManage getInstance() {
        /*以下用到了 双重检验锁
        * 双重检验锁模式（double checked locking pattern），是一种使用同步块加锁的方法。
        * 程序员称其为双重检查锁，因为会有两次检查 instance == null，
        * 一次是在同步块外，一次是在同步块内。为什么在同步块内还要再检验一次？
        * 因为可能会有多个线程一起进入同步块外的 if，如果在同步块内不进行二次检验的话就会生成多个实例了。*/
        if (apiManage == null) {
            synchronized (ApiManage.class) {
                if (apiManage == null) {
                    apiManage = new ApiManage();
                }
            }
        }
        return apiManage;
    }

    public GankApi gank;

    public GankApi getGankService() {
        if (gank == null) {
            synchronized (monitor) {
                if (gank == null) {
                    gank = new Retrofit.Builder()
                            .baseUrl("http://gank.io")
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .client(client)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()
                            .create(GankApi.class);
                }
            }
        }
        return gank;
    }
}
