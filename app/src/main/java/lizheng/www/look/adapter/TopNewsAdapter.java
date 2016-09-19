package lizheng.www.look.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import lizheng.www.look.R;
import lizheng.www.look.bean.news.NewsBean;
import lizheng.www.look.util.DensityUtil;

/**
 * Created by 10648 on 2016/9/19 0019.
 */
public class TopNewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_LOADING_MORE = -1;
    private static final int NOMAL_ITEM = 1;
    boolean showLoadingMore;
    float width;
    int widthPx;
    int heighPx;
    private ArrayList<NewsBean> topNewitems = new ArrayList<>();
    private Context mContext;

    public TopNewsAdapter(Context context) {
        mContext = context;
        width = mContext.getResources().getDimension(R.dimen.image_width);
        widthPx = DensityUtil.dip2px(mContext, width);
        heighPx = widthPx * 3 / 4;
        
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case NOMAL_ITEM:
                View view = LayoutInflater.from(mContext).inflate(R.layout.topnews_item_layout, parent, false);
                return new TopViewViewHolder(view);
            case TYPE_LOADING_MORE:
                return new LoadingMoreHolder(LayoutInflater.from(mContext).inflate(R.layout.infinite_loading, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        
        if (NOMAL_ITEM == getItemViewType(position)) {
            bindViewHolderNormal((TopViewViewHolder) holder, position);
        } else if (TYPE_LOADING_MORE == getItemViewType(position)) {
            bindLoadingViewHold((LoadingMoreHolder) holder, position);
        }
    }

    private void bindViewHolderNormal(TopViewViewHolder holder, int position) {
        NewsBean newsBeanItem = topNewitems.get(position);
        holder.textView.setText(newsBeanItem.getTitle());
        holder.sourceTextview.setText(newsBeanItem.getSource());
        Glide.with(mContext)
                .load(newsBeanItem.getImgsrc())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .centerCrop()
                .override(widthPx, heighPx)
                .into(holder.imageView);

    }
    private void bindLoadingViewHold(LoadingMoreHolder holder, int position) {
        holder.mProgressBar.setVisibility(showLoadingMore? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return topNewitems.size();
    }

    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);
        if (getItemCount() > 0 && position < getItemCount()) {
            return NOMAL_ITEM;
        }
        return TYPE_LOADING_MORE;
    }

    public void addItems(ArrayList<NewsBean> list) {
        list.remove(0);
        topNewitems.addAll(list);
        notifyDataSetChanged();
    }

    public void clearData() {
        topNewitems.clear();
        notifyDataSetChanged();
    }

    static class TopViewViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_image_id)
        ImageView imageView;
        @BindView(R.id.item_text_id)
        TextView textView;
        @BindView(R.id.item_text_source_id)
        TextView sourceTextview;
        @BindView(R.id.zhihu_item_layout)
        LinearLayout linearLayout;

        TopViewViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public class LoadingMoreHolder extends RecyclerView.ViewHolder {
        ProgressBar mProgressBar;

        public LoadingMoreHolder(View itemView) {
            super(itemView);
            mProgressBar = (ProgressBar) itemView;
        }
    }
}
