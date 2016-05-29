package com.example.dotua.goldendictbridge;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import static com.example.dotua.goldendictbridge.SharedFunction.executeFragmentWordIntent;

public class OneFragment extends Fragment {



    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        View rootView = inflater.inflate(R.layout.fragment_one, container, false);
        executeFragmentWordIntent(getActivity(),intent, rootView, 1);
        return rootView;
    }





}
