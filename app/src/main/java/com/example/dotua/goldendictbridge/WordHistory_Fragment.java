package com.example.dotua.goldendictbridge;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class WordHistory_Fragment extends Fragment {

    public WordHistory_Fragment (){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_history__fragment, container, false);
        RecyclerView recyclerView = (RecyclerView)rootView.findViewById(R.id.word_history_recycler_view);

        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)

        List<String> daset = new ArrayList<>();
        for (int i =1; i<=5; i++){
            daset.add(String.valueOf(i));
        }

        WordHistory_Adapter mAdapter = new WordHistory_Adapter(daset);
        recyclerView.setAdapter(mAdapter);
        Database_QueryTask queryTask = new Database_QueryTask(this.getContext());
        queryTask.execute(mAdapter);

        return rootView;
    }
}

