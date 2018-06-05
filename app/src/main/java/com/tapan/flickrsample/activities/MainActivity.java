package com.tapan.flickrsample.activities;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tapan.flickrsample.FlickrApplication;
import com.tapan.flickrsample.R;
import com.tapan.flickrsample.listeners.OnClickListener;
import com.tapan.flickrsample.utils.URLs;
import com.tapan.flickrsample.utils.Utils;
import com.tapan.flickrsample.adapters.FeedAdapter;
import com.tapan.flickrsample.objects.FeedObject;
import com.tapan.flickrsample.views.ItemDecoration;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private RelativeLayout mProgressbarLayout;
    private RecyclerView mRecyclerView;
    private TextView mEmptyView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private GridLayoutManager mGridLayoutManager;
    private FeedAdapter mAdapter;
    private List<FeedObject.Items> mFeedList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        clickViews();
        setRecyclerView();
        getFeed();
    }

    private void initViews() {

        mRecyclerView = findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        mEmptyView = findViewById(android.R.id.empty);
        mProgressbarLayout = findViewById(R.id.layoutProgressbar);

        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
    }

    private void clickViews() {

        mProgressbarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getFeed();
            }
        });
    }

    private void setRecyclerView() {

        mGridLayoutManager = new GridLayoutManager(this,2);

        mAdapter = new FeedAdapter(this,mFeedList);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        mRecyclerView.addItemDecoration(new ItemDecoration(spacingInPixels));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void getFeed() {

        mSwipeRefreshLayout.setRefreshing(false);
        mProgressbarLayout.setVisibility(View.VISIBLE);

        Call<FeedObject> call = FlickrApplication.mApiService
                .getFeed(URLs.FORMAT_TYPE, URLs.NO_JSON_CALLBACK);

        call.enqueue(new retrofit2.Callback<FeedObject>() {
            @Override
            public void onResponse(Call<FeedObject> call, Response<FeedObject> response) {

                FeedObject feedObject = response.body();

                if (response.isSuccessful() && feedObject != null) {

                    mFeedList = feedObject.getItems();
                    setData();

                } else {

                    setEmpty(getResources().getString(R.string.err_server_down));
                }
            }

            @Override
            public void onFailure(Call<FeedObject> call, Throwable t) {

                String msg;
                if(Utils.isNetworkAvailable(MainActivity.this)) {
                    msg = getResources().getString(R.string.err_server_down);
                } else {
                    msg = getResources().getString(R.string.err_internet_connection);
                }

                setEmpty(msg);
            }
        });
    }

    private void setData() {

        mProgressbarLayout.setVisibility(View.GONE);

        if(mFeedList!=null && mFeedList.size()>0) {

            mAdapter.setData(mFeedList);
            mRecyclerView.setVisibility(View.VISIBLE);

        } else {

            setEmpty(getResources().getString(R.string.no_feed));
        }
    }

    private void setEmpty(String msg) {

        mProgressbarLayout.setVisibility(View.GONE);

        mEmptyView.setText(msg);
        mRecyclerView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(int position) {

        if(position==mAdapter.getSelectedPos()) {

            mAdapter.setSelectedPos(-1);
            mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {

                    return 1;
                }
            });

        } else {

            mAdapter.setSelectedPos(position);
            final int infoPos = Utils.getInfoPosition(position);

            mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {

                    if(position==infoPos) {
                        return 2;
                    } else {
                        return 1;
                    }
                }
            });
        }

        mAdapter.notifyDataSetChanged();
    }
}
