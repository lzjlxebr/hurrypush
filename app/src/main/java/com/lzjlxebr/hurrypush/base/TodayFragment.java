package com.lzjlxebr.hurrypush.base;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.lzjlxebr.hurrypush.R;
import com.lzjlxebr.hurrypush.adapter.TodayFragmentAdapter;
import com.lzjlxebr.hurrypush.db.HurryPushContract;

public class TodayFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String LOG_TAG = TodayFragment.class.getSimpleName();

    private TodayFragmentAdapter todayFragmentAdapter;
    private RecyclerView mRecyclerView;
    private int mPosition = RecyclerView.NO_POSITION;


    private ProgressBar mLoadingIndicator;

    public static String[] TODAY_PROJECTION = {
            HurryPushContract.ClientInfoEntry.COLUMN_APP_ID,
            HurryPushContract.ClientInfoEntry.COLUMN_GENDER,
            HurryPushContract.ClientInfoEntry.COLUMN_CURRENT_LEVEL_ID,
            HurryPushContract.ClientInfoEntry.COLUMN_CURRENT_EXP,
            HurryPushContract.ClientInfoEntry.COLUMN_UPGRADE_EXP,
    };

    public static final int INDEX_COLUMN_APP_ID = 0;
    public static final int INDEX_COLUMN_GENDER = 1;
    public static final int INDEX_COLUMN_CURRENT_LEVEL_ID = 2;
    public static final int INDEX_COLUMN_CURRENT_EXP = 3;
    public static final int INDEX_COLUMN_UPGRADE_EXP = 4;

    private static final int TODAY_LOADER_ID = 1000;




    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {


        //HurryPushSyncUtils.startImmediateSync((MainActivity)getActivity());
        Log.d(LOG_TAG, "MainActivity was created.");

        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_today, container, false);
        todayFragmentAdapter = new TodayFragmentAdapter((MainActivity)getActivity());

        mLoadingIndicator = view.findViewById(R.id.today_load_indicator);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.today_recycler_view);
        showLoading();

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setAdapter(todayFragmentAdapter);

        getLoaderManager().initLoader(TODAY_LOADER_ID, null, this);

        Log.d(LOG_TAG, "Todat fragment on creating.");
        return view;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        switch (id) {
            case TODAY_LOADER_ID: {
                Uri todayQueryUri = HurryPushContract.ClientInfoEntry.CLIENT_INFO_URI;

                return new CursorLoader(
                        (MainActivity) getActivity(),
                        todayQueryUri,
                        TODAY_PROJECTION,
                        null,
                        new String[]{"b0fb3e80-82ed-44c5-aaaa-824bb129efe7"},
                        null
                );
            }
            default:
                throw new RuntimeException("Loader Not Implemented" + id);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        todayFragmentAdapter.swapCursor(data);
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        mRecyclerView.smoothScrollToPosition(mPosition);
        if (data.getCount() != 0) showTodayDataView();

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        todayFragmentAdapter.swapCursor(null);
    }

    public void showTodayDataView() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    public void showLoading() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }
}
