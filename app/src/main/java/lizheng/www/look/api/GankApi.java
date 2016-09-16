package lizheng.www.look.api;

import lizheng.www.look.bean.meizi.MeiziData;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by 10648 on 2016/9/16 0016.
 * 待扩展
 */
public interface GankApi {
    @GET("/api/data/福利/10/{page}")
    Observable<MeiziData> getMeiziData(@Path("page") int page);


}
