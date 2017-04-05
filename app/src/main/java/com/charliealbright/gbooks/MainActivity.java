package com.charliealbright.gbooks;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.charliealbright.gbooks.adapter.BookSearchAdapter;
import com.charliealbright.gbooks.adapter.EndlessRecyclerViewScrollListener;
import com.charliealbright.gbooks.dialog.SearchDialogFragment;
import com.charliealbright.gbooks.model.GBooksResponse;
import com.charliealbright.gbooks.model.Volume;
import com.charliealbright.gbooks.service.GoogleBooksService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements SearchDialogFragment.SearchDialogFragmentListener {

    private GoogleBooksService mGoogleBooksService;

    private View mRootView;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private BookSearchAdapter mBookSearchAdapter;
    private EndlessRecyclerViewScrollListener mScrollListener;

    private RelativeLayout mWelcomeMessage;
    private RelativeLayout mSearchOverlay;

    private String mCurrentQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRootView = findViewById(R.id.content_main);
        mWelcomeMessage = (RelativeLayout)findViewById(R.id.welcome_message);
        mSearchOverlay = (RelativeLayout)findViewById(R.id.search_overlay);

        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        List<Volume> bookList = new ArrayList<>();
        if (savedInstanceState != null) {
            bookList = savedInstanceState.getParcelableArrayList("bookList");
            mCurrentQuery = savedInstanceState.getString("query");
        }

        if (!bookList.isEmpty()) {
            mWelcomeMessage.setVisibility(View.GONE);
        }

        mBookSearchAdapter = new BookSearchAdapter(getApplicationContext(), bookList, Glide.with(this));
        mRecyclerView.setAdapter(mBookSearchAdapter);
        mScrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Call<GBooksResponse> getMoreCall = mGoogleBooksService.search(mCurrentQuery, getString(R.string.api_key), totalItemsCount);
                getMoreCall.enqueue(mGetMoreCallback);
            }
        };
        mRecyclerView.addOnScrollListener(mScrollListener);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/books/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mGoogleBooksService = retrofit.create(GoogleBooksService.class);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchDialogFragment searchDialogFragment = new SearchDialogFragment();
                searchDialogFragment.show(getSupportFragmentManager(), searchDialogFragment.getTag());
            }
        });
    }

    Callback<GBooksResponse> mSearchCallback = new Callback<GBooksResponse>() {
        @Override
        public void onResponse(Call<GBooksResponse> call, Response<GBooksResponse> response) {
            mSearchOverlay.setVisibility(View.GONE);

            GBooksResponse gBooksResponse = response.body();
            Timber.d("[SEARCH RESPONSE] kind = %s, itemCount = %d", gBooksResponse.getKind(), gBooksResponse.getItemCount());
            Snackbar.make(mRootView, "Search returned " + response.body().getItemCount() + " items.", Snackbar.LENGTH_LONG)
                    .setAction("OK", null).show();

            mBookSearchAdapter.addItemsAndNotify(gBooksResponse.getVolumes());
            mBookSearchAdapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(Call<GBooksResponse> call, Throwable t) {
            Timber.w(t);
        }
    };

    Callback<GBooksResponse> mGetMoreCallback = new Callback<GBooksResponse>() {
        @Override
        public void onResponse(Call<GBooksResponse> call, Response<GBooksResponse> response) {
            GBooksResponse gBooksResponse = response.body();
            mBookSearchAdapter.addItemsAndNotify(gBooksResponse.getVolumes());
        }

        @Override
        public void onFailure(Call<GBooksResponse> call, Throwable t) {
            Timber.w(t);
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        ArrayList<Volume> arrayList = new ArrayList<>(mBookSearchAdapter.getBookList());
        outState.putParcelableArrayList("bookList", arrayList);
        outState.putString("query", mCurrentQuery);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onSearchInitiated(boolean valid, String query) {
        if (valid) {
            mCurrentQuery = query;
            mWelcomeMessage.setVisibility(View.GONE);
            mSearchOverlay.setVisibility(View.VISIBLE);

            mBookSearchAdapter.clearSafelyAndNotify();
            mScrollListener.resetState();

            Call<GBooksResponse> apiCall = mGoogleBooksService.search(query, getString(R.string.api_key), 0);
            apiCall.enqueue(mSearchCallback);
        } else {
            // Snackbar with error msg.
        }
    }
}
