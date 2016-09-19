package lizheng.www.look.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import lizheng.www.look.R;
import lizheng.www.look.adapter.MeiziAdapter;
import lizheng.www.look.bean.meizi.Meizi;
import lizheng.www.look.presenter.impl_presenter.MeiziPresenterImpl;
import lizheng.www.look.presenter.impl_view.IMeiziFragment;
import lizheng.www.look.util.Once;
import lizheng.www.look.widget.WrapContentLinearLayoutManager;

public class MeiziFragment extends BaseFragment implements IMeiziFragment {


    private Unbinder unbinder;
    @BindView(R.id.recycle_meizi)
    RecyclerView mRecycleMeizi;
    @BindView(R.id.prograss)
    ProgressBar mPrograss;

    WrapContentLinearLayoutManager linearLayoutManager;
    MeiziPresenterImpl mMeiziPresenter;
    MeiziAdapter meiziAdapter;
    RecyclerView.OnScrollListener loadmoreListener;

    private boolean loading;

    private int index = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meizi, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mMeiziPresenter = new MeiziPresenterImpl(getContext(), this);
        
        initView();
        
        super.onViewCreated(view, savedInstanceState);
    }

    private void initView() {
        meiziAdapter = new MeiziAdapter(getContext());
        linearLayoutManager = new WrapContentLinearLayoutManager(getContext());
        intialListener();
        mRecycleMeizi.setLayoutManager(linearLayoutManager);
        mRecycleMeizi.setAdapter(meiziAdapter);
        mRecycleMeizi.addOnScrollListener(loadmoreListener);
        new Once(getContext()).show("tag_T_F", new Once.OnceCallback() {
            @Override
            public void onOnce() {
                Snackbar.make(mRecycleMeizi, getString(R.string.meizitips),
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.meiziaction, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getContext(), "你懂真好", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .show();
            }
        });
        mRecycleMeizi.setItemAnimator(new DefaultItemAnimator());
        loadData();
    }

    private void loadData() {
        if (meiziAdapter.getItemCount() > 0) {
            meiziAdapter.clearData();
        }
        mMeiziPresenter.getMeiziData(index);
    }

    private void intialListener() {
        loadmoreListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    int visibleItemCount = linearLayoutManager.getChildCount();//显示的items
                    int totalItemCount = linearLayoutManager.getItemCount();
                    int pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();

                    if (!loading && (visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        loading = true;
                        index += 1;
                        loadMoreData();
                    }
                }
            }
        };
    }

    private void loadMoreData() {
        meiziAdapter.loadingStart();
        mMeiziPresenter.getMeiziData(index);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mMeiziPresenter.unsubcrible();
        unbinder.unbind();
    }

    @Override
    public void updateMeiziData(ArrayList<Meizi> list) {
        meiziAdapter.loadingFinish();
        loading =false;
        meiziAdapter.addItems(list);
    }

    @Override
    public void showProgressDialog() {
        mPrograss.setVisibility(View.VISIBLE);
    }

    @Override
    public void hidProgressDialog() {
        mPrograss.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showError(String error) {
        mPrograss.setVisibility(View.INVISIBLE);
        if (mRecycleMeizi != null) {
            Snackbar.make(mRecycleMeizi,  getString(R.string.snack_infor),
                    Snackbar.LENGTH_SHORT)
                    .setAction("重试", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mMeiziPresenter.getMeiziData(index);
                        }
                    }).show();
        }
    }
}
