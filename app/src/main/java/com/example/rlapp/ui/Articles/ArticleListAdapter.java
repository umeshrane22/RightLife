package com.example.rlapp.ui.Articles;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.rlapp.R;
import com.example.rlapp.RetrofitData.ApiClient;
import com.example.rlapp.databinding.ArticleItemRowBinding;
import com.example.rlapp.ui.Articles.models.Article;
import com.example.rlapp.ui.utility.svgloader.GlideApp;

import java.util.List;

public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.ArticleViewHolder> {
    private List<Article> articleList;
    private Context context;

    public ArticleListAdapter(ArticlesDetailActivity articlesDetailActivity, List<Article> articleList) {
        this.articleList = articleList;
        context = articlesDetailActivity;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ArticleItemRowBinding binding = ArticleItemRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ArticleViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        Article article = articleList.get(position);

        //holder.binding.txtArticleContent.setText(article.getHtmlContent());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.binding.txtArticleContent.setText(Html.fromHtml(article.getHtmlContent(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.binding.txtArticleContent.setText(Html.fromHtml(article.getHtmlContent()));
        }
        Glide.with(context).load(ApiClient.CDN_URL_QA + article.getThumbnail())
                .transform(new RoundedCorners(1))
                .into(holder.binding.imageView);


        //holder.binding.imageView.setImageResource(article.getImageResId());

        // Hide Product cards initially
        if (article.getRecommendedProduct() != null && article.getRecommendedProduct().getSectionTitle() != null) {
            holder.binding.card1.setVisibility(View.VISIBLE);
            holder.binding.tvProductTitle.setText(article.getRecommendedProduct().getSectionTitle());
            holder.binding.txtDescCardProduct.setText(article.getRecommendedProduct().getDescription());
            holder.binding.txtPriceProduct.setText(String.format("₹ %s", article.getRecommendedProduct().getDiscountedPrice()));
            holder.binding.txtSavedPriceProduct.setText(String.format("₹%s", article.getRecommendedProduct().getListPrice()));
            //holder.binding.txtBtnBuyNow.setText(article.getRecommendedProduct().getButtonText());
            GlideApp.with(context).load(ApiClient.CDN_URL_QA + article.getThumbnail())
                    .transform(new RoundedCorners(15))
                    .into(holder.binding.imgThumbnailProduct);

        } else {
            holder.binding.card1.setVisibility(View.GONE);
        }

        // Service Card
        if (article.getRecommendedService() != null && article.getRecommendedService().getTitle() != null) {
            holder.binding.card2.setVisibility(View.VISIBLE);
            holder.binding.tvServiceCardTitle.setText(article.getRecommendedService().getTitle());
            holder.binding.txtDescCardService.setText(article.getRecommendedService().getDescription());
            holder.binding.txtTagService.setText(article.getRecommendedService().getModuleId());

            GlideApp.with(context).load(ApiClient.CDN_URL_QA + article.getRecommendedService().getImageUrl())
                    .transform(new RoundedCorners(15))
                    .into(holder.binding.imgThumbnailService);
            GlideApp.with(context).load(ApiClient.CDN_URL_QA + article.getRecommendedService().getModuleImageUrl())
                    .placeholder(R.drawable.circular_logo)
                    .into(holder.binding.imgTagService);
        } else {
            holder.binding.card2.setVisibility(View.GONE);
        }

        // Article Card
        if (article.getRecommendedArticle() != null && article.getRecommendedArticle().getTitle() != null) {
            if (article.getRecommendedArticle().getContentType().equalsIgnoreCase("video")){
                holder.binding.card3.setVisibility(View.VISIBLE);

                holder.binding.tvArticlecard3TitleVideo.setText(article.getRecommendedArticle().getTitle());
                holder.binding.txtDescCard3Video.setText(article.getRecommendedArticle().getTitle());

                GlideApp.with(context).load(ApiClient.CDN_URL_QA + article.getRecommendedService().getImageUrl())
                        .transform(new RoundedCorners(15))
                        .into(holder.binding.imgThumbnailCard3Video);



                holder.binding.card3Series.setVisibility(View.GONE);
            }else if (article.getRecommendedArticle().getContentType().equalsIgnoreCase("series")){
                holder.binding.card3.setVisibility(View.GONE);
                holder.binding.card3Series.setVisibility(View.VISIBLE);
            }else if (article.getRecommendedArticle().getContentType().equalsIgnoreCase("text")){
                holder.binding.card3.setVisibility(View.GONE);
                holder.binding.card3Series.setVisibility(View.VISIBLE);
            }
        }

/*
        if (article.getRecommendedLive() != null && article.getRecommendedLive().getTitle() != null) {
            //if (article.getRecommendedArticle().getContentType().equalsIgnoreCase("video"))
            holder.binding.card1.setVisibility(View.VISIBLE);
            holder.binding.tvProductTitle.setText(article.getRecommendedLive().getSectionTitle());
            holder.binding.txtDescCardProduct.setText(article.getRecommendedLive().getSectionSubtitle());
            holder.binding.txtPriceProduct.setText(String.format("₹ %s", article.getRecommendedLive().getPriceInINR()));
            holder.binding.txtSavedPriceProduct.setText(String.format("₹%s", article.getRecommendedLive().getOriginalPriceInINR()));
            //holder.binding.txtBtnBuyNow.setText(article.getRecommendedProduct().getButtonText());
            GlideApp.with(context).load(ApiClient.CDN_URL_QA + article.getRecommendedLive().getThumbnail().getUrl())
                    .transform(new RoundedCorners(15))
                    .into(holder.binding.imgThumbnailProduct);

        } else {
            holder.binding.card1.setVisibility(View.GONE);
        }*/

        // funfact Card
        if (article.getFunFacts() != null && article.getFunFacts().getDescription() != null) {
            holder.binding.card4.setVisibility(View.VISIBLE);
        } else {
            holder.binding.card4.setVisibility(View.GONE);
        }


        // Show the correct recommended  card based on the condition
      /*  if (article.getVisibleCardIndex() == 1) {
            holder.binding.card1.setVisibility(View.VISIBLE);
        } else if (article.getVisibleCardIndex() == 2) {
            holder.binding.card2.setVisibility(View.VISIBLE);
        } else if (article.getVisibleCardIndex() == 3) {
            holder.binding.card3.setVisibility(View.VISIBLE);
        } else if (article.getVisibleCardIndex() == 4) {
            holder.binding.card4.setVisibility(View.VISIBLE);
        }*/
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    static class ArticleViewHolder extends RecyclerView.ViewHolder {
        ArticleItemRowBinding binding;

        public ArticleViewHolder(ArticleItemRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
