package lizheng.www.look.presenter.impl_presenter;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import lizheng.www.look.api.ApiManage;
import lizheng.www.look.bean.meizi.MeiziData;
import lizheng.www.look.config.Config;
import lizheng.www.look.presenter.IMeiziPresenter;
import lizheng.www.look.presenter.impl_view.IMeiziFragment;
import lizheng.www.look.util.CacheUtil;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
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
        Log.d("MeiziPresenterImpl", "MeiziPresenterImpl is there");
    }

    @Override
    public void getMeiziData(int t) {
        Log.d("MeiziPresenterImpl", "MeiziPresenterImpl getMeiziData tongfa");
        mIMeiziFragment.showProgressDialog();
        Subscription subscription = ApiManage.getInstance()
                .getGankService().getMeiziData(t)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MeiziData>(){

                    @Override
                    public void onCompleted() {
                        Log.d("MeiziPresenterImpl", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        mIMeiziFragment.hidProgressDialog();
                        mIMeiziFragment.showError(e.getMessage());
                        Log.d("MeiziPresenterImpl", "onError->" + e.toString());
                    }

                    @Override
                    public void onNext(MeiziData data) {
                        Log.d("MeiziPresenterImpl", "onNext");
                        mIMeiziFragment.hidProgressDialog();
                        mCacheUtil.put(Config.MEIZI, gson.toJson(data));
                        mIMeiziFragment.updateMeiziData(data.getResults());

                    }
                });
        addSubscription(subscription);

    }
}
