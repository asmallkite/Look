package lizheng.www.look.presenter.impl_presenter;

import lizheng.www.look.api.ApiManage;
import lizheng.www.look.bean.news.NewsList;
import lizheng.www.look.presenter.ITopNewsPresenter;
import lizheng.www.look.presenter.impl_view.ITopNewsFragment;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 10648 on 2016/9/19 0019.
 */
public class TopNewsPresenterImpl extends BasePresenterImpl
        implements ITopNewsPresenter{

    ITopNewsFragment mITopNewsFragment;

    public TopNewsPresenterImpl(ITopNewsFragment ITopNewsFragment) {
        mITopNewsFragment = ITopNewsFragment;
    }

    @Override
    public void getNewsList(int t) {
        mITopNewsFragment.showProgressDialog();
        Subscription subscription = ApiManage.getInstance().getTopNewsService()
                .getNews(t)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NewsList>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mITopNewsFragment.hidProgressDialog();
                        mITopNewsFragment.showError(e.toString());
                    }

                    @Override
                    public void onNext(NewsList newsList) {
                        mITopNewsFragment.hidProgressDialog();
                        mITopNewsFragment.upListItem(newsList);
                    }
                });
        addSubscription(subscription);

    }
}
