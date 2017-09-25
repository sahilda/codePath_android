package com.sahilda.nytimessearch.RecyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sahilda.nytimessearch.R;
import com.sahilda.nytimessearch.models.Article;

import java.util.List;

public class ArticleArrayAdapter extends RecyclerView.Adapter<ArticleArrayAdapter.ViewHolder> {

    private List<Article> mArtciles;
    private Context mContext;

    public ArticleArrayAdapter(Context context, List<Article> articles) {
        mArtciles = articles;
        mContext = context;
    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public ArticleArrayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_article_result, parent, false);
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ArticleArrayAdapter.ViewHolder viewHolder, int position) {
        Article article = mArtciles.get(position);
        TextView textView = viewHolder.tvTitle;
        textView.setText(article.getHeadline());

        ImageView imageView = viewHolder.imImage;
        imageView.setImageResource(0);

        if (!TextUtils.isEmpty(article.getThumbNail())) {
            Glide.with(getContext())
                    .load(article.getThumbNail())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(imageView);
        } else {
            Glide.with(getContext())
                    .load(R.drawable.placeholder)
                    .into(imageView);
        }
    }

    @Override
    public int getItemCount() {
        return mArtciles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle;
        public ImageView imImage;

        public ViewHolder(View itemView) {
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            imImage = (ImageView) itemView.findViewById(R.id.ivImage);
        }
    }

}
