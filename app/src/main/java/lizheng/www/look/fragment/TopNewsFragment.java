package lizheng.www.look.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import lizheng.www.look.R;
import lizheng.www.look.adapter.TopNewsAdapter;
import lizheng.www.look.bean.news.NewsList;
import lizheng.www.look.presenter.impl_presenter.TopNewsPresenterImpl;
import lizheng.www.look.presenter.impl_view.ITopNewsFragment;
import lizheng.www.look.widget.WrapContentLinearLayoutManager;

/**
 * Created by 10648 on 2016/9/19 0019.
 */
public class TopNewsFragment extends BaseFragment
        implements ITopNewsFragment {

    boolean loading;
    int currentIndex = 0;

    TopNewsPresenterImpl mTopNewsPresenter;
    TopNewsAdapter mAdapter;

    LinearLayoutManager mLinearLayoutManager;
    RecyclerView.OnScrollListener loadingMoreListener;

    @BindView(R.id.recycle_topnews)
    RecyclerView mRecycleTopnews;
    @BindView(R.id.progress)
    ProgressBar mProgress;

    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.topnews_fragment_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView();
    }


    private void initData() {
        mTopNewsPresenter = new TopNewsPresenterImpl(this);
        mAdapter = new TopNewsAdapter(getContext());
    }

    private void initView() {
        initListener();
        mLinearLayoutManager = new WrapContentLinearLayoutManager(getContext());
        mRecycleTopnews.setItemAnimator(new DefaultItemAnimator());
        mRecycleTopnews.setLayoutManager(mLinearLayoutManager);
        mRecycleTopnews.setHasFixedSize(true);
        mRecycleTopnews.setAdapter(mAdapter);
        mRecycleTopnews.addOnScrollListener(loadingMoreListener);
        loadData();
    }

    private void loadData() {
        if (mAdapter.getItemCount() > 0) {
            mAdapter.clearData();
        }mTopNewsPresenter.getNewsList(currentIndex);

    }

    private void initListener() {
        loadingMoreListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    int visibleItemCount = mLinearLayoutManager.getChildCount();
                    int totalItemCount = mLinearLayoutManager.getItemCount();
                    int pastVisiblesItems = mLinearLayoutManager.findFirstVisibleItemPosition();

                    if (!loading && (visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        loading = true;
                        loadMoreDate();
                    }
                }
            }
        };
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void upListItem(NewsList newsList) {
        loading = false;
        hidProgressDialog();
        mAdapter.addItems(newsList.getNewsList());
    }

    @Override
    public void showProgressDialog() {
        mProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hidProgressDialog() {
        mProgress.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showError(String error) {
        if (mRecycleTopnews != null) {
            Snackbar.make(mRecycleTopnews, getString(R.string.snack_infor),
                    Snackbar.LENGTH_SHORT)
                    .setAction("重试", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mTopNewsPresenter.getNewsList(currentIndex);
                        }
                    }).show();
        }
    }


    private void loadMoreDate() {
        currentIndex += 20;
        mTopNewsPresenter.getNewsList(currentIndex);
    }
}