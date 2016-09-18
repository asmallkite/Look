package lizheng.www.look.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lizheng.www.look.MainActivity;
import lizheng.www.look.R;
import lizheng.www.look.bean.Meizi;

/**
 * Created by 10648 on 2016/9/16 0016.
 */
public class MeiziAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements MainActivity.LoadingMore {


    @BindView(R.id.item_image_id)
    ImageView mItemImageId;
    private ArrayList<Meizi> mMeizis = new ArrayList<>();

    private static final int TYPE_LOADING_MORE = -1;
    private static final int NORMAL_ITEM = 1;
    boolean showLoadingMore;

    private Context mContext;

    public MeiziAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);
        if (position < getItemCount() && getItemCount() > 0) {
            return NORMAL_ITEM;
        }
        return TYPE_LOADING_MORE;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case NORMAL_ITEM:
                return new MeiziViewHolder(LayoutInflater.from(mContext).inflate(R.layout.meizi_layout_item, parent, false));
            case TYPE_LOADING_MORE:
                return new LoadingMoreHolder(LayoutInflater.from(mContext).inflate(R.layout.infinite_loading, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case NORMAL_ITEM:
                bindViewHolderNormal((MeiziViewHolder) holder, position);
                break;
            case TYPE_LOADING_MORE:
                bindLoadingViewHold((LoadingMoreHolder) holder, position);
                break;
        }

    }

    private void bindLoadingViewHold(LoadingMoreHolder holder, int position) {
        holder.mProgressBar.setVisibility(showLoadingMore ? View.VISIBLE : View.INVISIBLE);
    }

    private void bindViewHolderNormal(final MeiziViewHolder holder, int position) {
        final Meizi meizi = mMeizis.get(holder.getAdapterPosition());

        /**
         * 是否在此添加 点击事件 进入详情页
         */
        //*********************************************
        Glide.with(mContext)
                .load(meizi.getUrl())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .fitCenter()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mMeizis.size();
    }

    @Override
    public void loadingStart() {
        if (showLoadingMore)
            return;
        showLoadingMore = true;
        notifyItemInserted(getLoadingMoreItemPosition());
    }

    private int getLoadingMoreItemPosition() {
        return showLoadingMore ? getItemCount() - 1 : RecyclerView.NO_POSITION;
    }

    @Override
    public void loadingFinish() {
        if (!showLoadingMore)
            return;
        final int loadingPos = getLoadingMoreItemPosition();
        showLoadingMore = false;
        notifyItemRemoved(loadingPos);

    }

    public void addItems(ArrayList<Meizi> list) {
        mMeizis.addAll(list);
        notifyDataSetChanged();
    }

    public void clearData() {
        mMeizis.clear();
        notifyDataSetChanged();
    }





    public class LoadingMoreHolder extends RecyclerView.ViewHolder {
        ProgressBar mProgressBar;

        public LoadingMoreHolder(View itemView) {
            super(itemView);
            mProgressBar = (ProgressBar) itemView;
        }
    }

    /**
     * 待测试
     */


    public class MeiziViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_image_id)
        ImageView imageView;

        public MeiziViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
        @OnClick(R.id.item_image_id)
        public void onClick() {
            Toast.makeText(mContext, "out cladd  in again", Toast.LENGTH_SHORT).show();
        }
    }


}
