package lizheng.www.look.api;

import lizheng.www.look.bean.news.NewsList;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by 10648 on 2016/9/19 0019.
 */
public interface TopNews {

    @GET("http://c.m.163.com/nc/article/headline/T1348647909107/{id}-20.html")
    Observable<NewsList> getNews(@Path("id") int id);
}
