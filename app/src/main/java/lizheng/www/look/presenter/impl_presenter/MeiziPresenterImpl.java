package lizheng.www.look.presenter.impl_presenter;

import android.content.Context;

import com.google.gson.Gson;

import lizheng.www.look.api.ApiManage;
import lizheng.www.look.bean.meizi.MeiziData;
import lizheng.www.look.config.Config;
import lizheng.www.look.presenter.IMeiziPresenter;
import lizheng.www.look.presenter.impl_view.IMeiziFragment;
import lizheng.www.look.util.CacheUtil;
import rx.Observer;
import rx.Subscription;
import rx.schedulers.Schedulers;

/**
 * Created by 10648 on 2016/9/16 0016.
 */
public class MeiziPresenterImpl extends BasePresenterImpl implements IMeiziPresenter {

    private IMeiziFragment mIMeiziFragment;
    private CacheUtil mCacheUtil;
    private Gson gson = new Gson();

    public MeiziPresenterImpl(Context context, IMeiziFragment iMeiziFragment) {
        this.mIMeiziFragment = iMeiziFragment;
        mCacheUtil = CacheUtil.get(context);
    }

    @Override
    public void getMeiziData(int t) {
        mIMeiziFragment.showProgressDialog();
        Subscription subscription = ApiManage.getInstance()
                .getGankService().getMeiziData(t)
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<MeiziData>(){

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mIMeiziFragment.hidProgressDialog();
                        mIMeiziFragment.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(MeiziData data) {
                        mIMeiziFragment.hidProgressDialog();
                        mCacheUtil.put(Config.MEIZI, gson.toJson(data));
                        mIMeiziFragment.updateMeiziData(data.getResults());

                    }
                });
        addSubscription(subscription);

    }
}
