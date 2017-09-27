package com.example.brenda.popularmovies.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.brenda.popularmovies.R;

/**
 * Created by brenda on 9/21/17.
 */

public class OverviewFragment extends Fragment{
    public OverviewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_overview, container, false);

        TextView overview = (TextView) view.findViewById(R.id.overview);

        String movieOverview = getArguments().getString(getString(R.string.overview_data_key));
        overview.setText(movieOverview);

        return view;
    }

}
