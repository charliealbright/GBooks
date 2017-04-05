package com.charliealbright.gbooks.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.charliealbright.gbooks.BookDetailActivity;
import com.charliealbright.gbooks.R;
import com.charliealbright.gbooks.model.PaletteBitmap;
import com.charliealbright.gbooks.model.PaletteBitmapTranscoder;
import com.charliealbright.gbooks.model.Volume;
import com.charliealbright.gbooks.model.VolumeImages;

import java.util.List;

import timber.log.Timber;

/**
 * Created by Charlie on 4/4/17.
 */

public class BookSearchAdapter extends RecyclerView.Adapter<BookSearchAdapter.ViewHolder> {

    private List<Volume> mBookList;
    private @ColorInt int mDefaultColor;

    private final BitmapRequestBuilder<String, PaletteBitmap> mGlideRequest;

    public BookSearchAdapter(Context context, List<Volume> bookList, RequestManager glide) {
        mBookList = bookList;
        mGlideRequest = glide
                .fromString()
                .asBitmap()
                .transcode(new PaletteBitmapTranscoder(context), PaletteBitmap.class)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        mDefaultColor = ContextCompat.getColor(context, R.color.colorWetAsphalt);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        VolumeImages images = mBookList.get(position).getVolumeInfo().getImageSet();
        if (images != null) {
            String imageUrl = mBookList.get(position).getVolumeInfo().getImageSet().getThumbnailImage();
            mGlideRequest.load(imageUrl)
                    .into(new ImageViewTarget<PaletteBitmap>(holder.mBookImageView) {
                        @Override
                        protected void setResource(PaletteBitmap resource) {
                            super.view.setImageBitmap(resource.bitmap);
                            int color = resource.palette.getMutedColor(mDefaultColor);
                            holder.mCardView.setCardBackgroundColor(color);
                        }
                    });
        }

        holder.mBookTitle.setText(mBookList.get(position).getVolumeInfo().getTitle());
        holder.mBookDescription.setText(mBookList.get(position).getVolumeInfo().getDescription());

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Timber.d("[CLICK] item = %d", holder.getAdapterPosition());
                Intent intent = new Intent(view.getContext(), BookDetailActivity.class);
                intent.putExtra("id", mBookList.get(holder.getAdapterPosition()).getId());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBookList.size();
    }

    public List<Volume> getBookList() {
        return mBookList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CardView mCardView;
        ImageView mBookImageView;
        TextView mBookTitle;
        TextView mBookDescription;

        public ViewHolder(View itemView) {
            super(itemView);

            mCardView = (CardView)itemView.findViewById(R.id.card_view);
            mBookImageView = (ImageView)itemView.findViewById(R.id.book_image);
            mBookTitle = (TextView)itemView.findViewById(R.id.book_title);
            mBookDescription = (TextView)itemView.findViewById(R.id.book_description);
        }
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
        Glide.clear(holder.mBookImageView);
    }

    public void addItemsAndNotify(List<Volume> booksToAdd) {
        int itemCount = booksToAdd.size();
        mBookList.addAll(booksToAdd);
        notifyItemRangeInserted(getItemCount() - 1, itemCount);
    }

    public void clearSafelyAndNotify() {
        if (!mBookList.isEmpty()) {
            mBookList.clear();
            notifyDataSetChanged();
        }
    }
}
