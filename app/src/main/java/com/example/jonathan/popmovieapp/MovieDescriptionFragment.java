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
            // String[] array = b.getStringArray("movie");

            //Intent intent = new Intent();

            //Movie movie = intent.getExtras().getParcelable("b");
            //Movie movie = intent.getParcelableExtra("b");
            Log.v("THIS MOVIE: ", movie.getDescription());

            // String descStr = array[1];
            // String rating = array[2];

            ((TextView) rootView.findViewById(R.id.textView))
                    .setText(movie.getDescription());

            ((TextView) rootView.findViewById(R.id.textView2))
                    .setText(movie.getTitle());

            ImageView image = (ImageView) rootView.findViewById(R.id.descImageView);
            Picasso.with(getActivity()).load(movie.getPoster_path()).into(image);

            // if (intent != null) {
            //    String url = movie.getTitle();

            //     Picasso.with(getActivity()).load(url).into(image);
            // }
        }

        return rootView;
    }
}
