package com.example.brenda.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.brenda.popularmovies.activities.MovieDetailActivity;
import com.example.brenda.popularmovies.adapters.MovieAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener {
    private static final int IMG_LIST_ITEMS = 100;
    private MovieAdapter mAdapter;
    private RecyclerView mImageList;
    private List <com.example.brenda.popularmovies.models.Movie> movies;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageList = (RecyclerView) findViewById(R.id.rv_images);

        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        mImageList.setLayoutManager(layoutManager);
        mImageList.setHasFixedSize(true);
        mAdapter = new MovieAdapter(IMG_LIST_ITEMS, this);
        mImageList.setAdapter(mAdapter);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        if (mToast != null) {
            mToast.cancel();
        }
        String toastMessage = "Item #" + clickedItemIndex + " clicked.";
        mToast = Toast.makeText(this, toastMessage, Toast.LENGTH_LONG);
        mToast.show();
        //Movie selectedMovie = movies.get(clickedItemIndex);

        Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
        //intent.putExtra(getString(R.string.movie_intent_key, selectedMovie));
        startActivity(intent);

    }
}
