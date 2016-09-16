package lizheng.www.look.presenter;

import rx.Subscription;

/**
 * Created by 10648 on 2016/9/15 0015.
 * 与model交互的接口
 */
public interface IBasePresenter {
    void addSubscription(Subscription s);
    void unsubcrible();
}
