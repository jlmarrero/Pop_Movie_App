package com.example.jonathan.popmovieapp;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Objects;

//import android.app.Fragment;


/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDescriptionFragment extends Fragment {

    public MovieDescriptionFragment() {
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_description, container, false);
        // Pull data from the intent passed from MainAcitivy
        Bundle b = getActivity().getIntent().getExtras();

        // Get the Movie object from the Bundle
        Movie movie = b.getParcelable("MOVIE");

        /* Test (and log results) to see if the movie object was successfully
        passed to the movie description fragment before loading content into
        the rootview.
         */

        if (movie == null) {
            Log.v("THIS MOVIE: ", "IS <----- EMPTY ------");
        } else {
            Log.v("THIS MOVIE BACK: ", movie.getBackdrop_path());

            Log.v("BACKDROP PATH: ", movie.getBackdrop_path());


            // Loading the backdrop image
            String checker = "empty";
            ImageView image2 = (ImageView) rootView.findViewById(R.id.imageView);
            if (Objects.equals(movie.getBackdrop_path(), checker)){
                Picasso.with(getActivity()).load(R.drawable.ic_insert_photo_black_24dp).into(image2);
            } else {
                Picasso.with(getActivity()).load(movie.getBackdrop_path()).into(image2);
            }

            // Loading the movie poster image
            ImageView image = (ImageView) rootView.findViewById(R.id.descImageView);
            if (Objects.equals(movie.getPoster_path(), checker)){
                Picasso.with(getActivity()).load(R.drawable.ic_insert_photo_black_24dp).into(image);
            } else {
                Picasso.with(getActivity()).load(movie.getPoster_path()).into(image);
            }


            // Loading movie description
            ((TextView) rootView.findViewById(R.id.textView))
                    .setText(movie.getDescription());

            // Loading movie title
            ((TextView) rootView.findViewById(R.id.textView2))
                    .setText(movie.getTitle());

            // Loading movie rating
            ((TextView) rootView.findViewById(R.id.textView4))
                    .setText("Rating: " + movie.getVote_average());

            // Loading movie release date
            ((TextView) rootView.findViewById(R.id.textView3))
                    .setText("Release date: " + movie.getRelease_date().toString());
        }

        return rootView;
    }

}
