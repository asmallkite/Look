package lizheng.www.look.presenter.impl_presenter;

import lizheng.www.look.presenter.IBasePresenter;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by 10648 on 2016/9/16 0016.
 * 接口实现类
 */
public class BasePresenterImpl implements IBasePresenter {

    private CompositeSubscription mCompositeSubscription;

    @Override
    public void addSubscription(Subscription s) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(s);
    }

    @Override
    public void unsubcrible() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }

    }
}
