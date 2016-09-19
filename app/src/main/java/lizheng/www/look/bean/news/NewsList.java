package lizheng.www.look.bean.news;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by 10648 on 2016/9/19 0019.
 */
public class NewsList {
    @SerializedName("T1348647909107")
    ArrayList<NewsBean> newsList;
    public ArrayList<NewsBean> getNewsList() {
        return newsList;
    }
    public void setNewsList(ArrayList<NewsBean> newsList) {
        this.newsList = newsList;
    }
}
