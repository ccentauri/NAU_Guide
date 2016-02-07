package ua.nau.edu.RecyclerViews.NewsActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gc.materialdesign.views.ProgressBarIndeterminate;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ua.nau.edu.NAU_Guide.R;
import ua.nau.edu.Systems.CircleTransform;

public class NewsAdapterTest extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<NewsDataModel> dataSet;
    private Context context;

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    // The minimum amount of items to have below your current scroll position before loading more.
    private int visibleThreshold = 2;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    public NewsAdapterTest(ArrayList<NewsDataModel> data, Context context, RecyclerView recyclerView) {
        this.dataSet = data;
        this.context = context;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                    Log.i("NewsAdapterTest", "totalItemCount = "
                                    + Integer.toString(totalItemCount)
                                    + "    lastVisibleItem= " + Integer.toString(lastVisibleItem)
                                    + "  loading: " + loading
                    );

                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        // End has been reached
                        Log.i("NewsAdapterTest", "onScrolled: End reached");
                        if (onLoadMoreListener != null) {
                            loading = true;
                            onLoadMoreListener.onLoadMore();
                        }
                    }
                }
            });
        }
    }

    public void setData(ArrayList<NewsDataModel> data) {
        this.dataSet = data;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ExpandableTextView postMessage;
        TextView postTitle;
        TextView postSubTitle;
        ImageView authorImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.authorImage = (ImageView) itemView.findViewById(R.id.header_image);
            this.postTitle = (TextView) itemView.findViewById(R.id.header_title);
            this.postSubTitle = (TextView) itemView.findViewById(R.id.header_subtitle);
            this.postMessage = (ExpandableTextView) itemView.findViewById(R.id.post_text_expand);
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    @Override
    public int getItemViewType(int position) {
        if (dataSet.get(position) != null)
            return VIEW_ITEM;
        else
            return VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;

        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_news, parent, false);

            vh = new MyViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_progress, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int listPosition) {
        if (holder instanceof MyViewHolder) {

            ExpandableTextView postMessage = ((MyViewHolder) holder).postMessage;
            TextView postTitle = ((MyViewHolder) holder).postTitle;
            TextView postSubTitle = ((MyViewHolder) holder).postSubTitle;
            ImageView authorImage = ((MyViewHolder) holder).authorImage;

            postTitle.setText(dataSet.get(listPosition).getAuthor());
            postSubTitle.setText(dataSet.get(listPosition).getCreateTime());
            postMessage.setText(dataSet.get(listPosition).getMessage());
            Picasso.with(context).load(Uri.parse(dataSet.get(listPosition).getAuthorPhotoUrl())).transform(new CircleTransform()).into(authorImage);
        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    public void setLoaded() {
        this.loading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        }
    }

}