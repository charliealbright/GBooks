package com.charliealbright.gbooks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.charliealbright.gbooks.model.Volume;
import com.charliealbright.gbooks.service.GoogleBooksService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BookDetailActivity extends AppCompatActivity {

    private GoogleBooksService mGoogleBooksService;

    private ImageView mBookImage;
    private TextView mTitle;
    private TextView mAuthors;
    private TextView mPublisher;
    private TextView mPublishedDate;
    private RatingBar mRatingBar;
    private TextView mRatingCount;
    private TextView mPageCount;
    private TextView mType;
    private TextView mDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        mBookImage = (ImageView)findViewById(R.id.activity_book_image);
        mTitle = (TextView)findViewById(R.id.activity_book_title);
        mAuthors = (TextView)findViewById(R.id.activity_book_authors);
        mPublisher = (TextView)findViewById(R.id.activity_book_publisher);
        mPublishedDate = (TextView)findViewById(R.id.activity_book_published_date);
        mRatingBar = (RatingBar)findViewById(R.id.activity_book_rating_bar);
        mRatingCount = (TextView)findViewById(R.id.activity_book_rating_count);
        mPageCount = (TextView)findViewById(R.id.activity_book_page_count);
        mType = (TextView)findViewById(R.id.activity_book_type);
        mDescription = (TextView)findViewById(R.id.activity_book_description);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/books/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mGoogleBooksService = retrofit.create(GoogleBooksService.class);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        Call<Volume> bookCall = mGoogleBooksService.getVolume(id);
        bookCall.enqueue(mBookCallback);
    }

    Callback<Volume> mBookCallback = new Callback<Volume>() {
        @Override
        public void onResponse(Call<Volume> call, Response<Volume> response) {
            Volume volume = response.body();

            if (volume.getVolumeInfo().getImageSet() != null) {
                Glide.with(getApplicationContext())
                        .load(volume.getVolumeInfo().getImageSet().getMediumImage())
                        .crossFade()
                        .into(mBookImage);
            }

            mTitle.setText(volume.getVolumeInfo().getTitle());
            mAuthors.setText(volume.getVolumeInfo().getAuthors());
            mPublisher.setText(volume.getVolumeInfo().getPublisher());

            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            try {
                if (volume.getVolumeInfo().getPublishedDate() != null) {
                    Date date = inputFormat.parse(volume.getVolumeInfo().getPublishedDate());
                    SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault());
                    String dateString = outputFormat.format(date);
                    mPublishedDate.setText(dateString);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            mRatingBar.setRating(volume.getVolumeInfo().getAverageRating());
            int ratingCount = volume.getVolumeInfo().getRatingsCount();
            if (ratingCount == 0) {
                mRatingCount.setText("No Ratings");
            } else if (ratingCount == 1) {
                mRatingCount.setText("1 Rating");
            } else {
                mRatingCount.setText("" + ratingCount + " Ratings");
            }

            mPageCount.setText("" + volume.getVolumeInfo().getPageCount() + " pages");
            mType.setText(volume.getVolumeInfo().getPrintType());

            if (volume.getVolumeInfo().getDescription() != null) {
                mDescription.setText(Html.fromHtml(volume.getVolumeInfo().getDescription()));
            }
        }

        @Override
        public void onFailure(Call<Volume> call, Throwable t) {

        }
    };
}
