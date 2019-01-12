package com.example.android.playmusic;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



public class DetailFragment extends Fragment {


    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();

    if (bundle != null) {

        TextView textView = (TextView) getView().findViewById(R.id.songdetail);
        textView.setText(bundle.getString("songname"));
        TextView textView1 = (TextView) getView().findViewById(R.id.singerdetail);
        textView1.setText(bundle.getString("singername"));


}
    }


}
