package com.example.jonathan.popmovieapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

//import android.app.Fragment;


/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDescriptionFragment extends Fragment {

    public MovieDescriptionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       // inflater.inflate(R.layout.fragment_movie_description, container, false);

        View rootView = inflater.inflate(R.layout.fragment_movie_description, container, false);

        Bundle b = getActivity().getIntent().getExtras();

        Log.v("THIS BUNDLE: ", b.toString());
        Movie movie = b.getParcelable("MOVIE");


        if(movie == null){
            Log.v("THIS MOVIE: ", "IS EMPTY!!!!!!!!!!!!!!!!!!!!!!!!!!");
        } else {
            Log.v("THIS MOVIE ELSE: ", movie.getVote_average());

            ImageView image = (ImageView) rootView.findViewById(R.id.descImageView);
            Picasso.with(getActivity()).load(movie.getPoster_path()).into(image);

            ((TextView) rootView.findViewById(R.id.textView))
                    .setText(movie.getDescription());

            ((TextView) rootView.findViewById(R.id.textView2))
                    .setText(movie.getTitle());

            ((TextView) rootView.findViewById(R.id.textView4)).setText("Rating: " + movie.getPopularity());
        }

        return rootView;
    }

}
