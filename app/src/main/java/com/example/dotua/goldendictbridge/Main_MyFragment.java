package com.example.dotua.goldendictbridge;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static com.example.dotua.goldendictbridge.Main_SharedFunction.getWordList;
import static com.example.dotua.goldendictbridge.Main_SharedFunction.sendMessage;
import static com.example.dotua.goldendictbridge.Main_SharedFunction.showPopupMenu;
import static com.example.dotua.goldendictbridge.Main_SharedFunction.getReceivedWord;

public class Main_MyFragment extends Fragment {

    public Main_MyFragment() {
        // Required empty public constructor
    }

    public static Main_MyFragment newMyFragmentInstance(Context context, int numberOfCharacter) {
        Main_MyFragment myFragment = new Main_MyFragment();

        Bundle args = new Bundle();
        args.putInt(context.getString(R.string.bundle_key_number_of_character), numberOfCharacter);
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int numberOfCharacter = getArguments().getInt(getString(R.string.bundle_key_number_of_character), 1);

        View rootView = inflater.inflate(R.layout.recycler_view__activity_auto_fit, container, false);

        executeFragmentWordIntent(getActivity(), rootView, numberOfCharacter);
        return rootView;
    }

    public void executeFragmentWordIntent (final Context context,
                                                  View rootView,
                                                  int numberOfCharacter){
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.addItemDecoration(new RecyclerView_MarginDecoration(context));
        recyclerView.setHasFixedSize(true);

        TextView header = (TextView)LayoutInflater.from(context).inflate(
                R.layout.recycler_view__auto_fit_header,
                recyclerView,
                false);
        header.setText(getReceivedWord());
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(context, ((TextView)v).getText().toString());
            }
        });
        header.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                showPopupMenu(v, ((TextView)v).getText().toString());
                return true;
            }
        });

        final RecyclerView_WordListAdapter adapter = new RecyclerView_WordListAdapter(
                header,
                getWordList(),
                numberOfCharacter);

        final GridLayoutManager manager = (GridLayoutManager) recyclerView.getLayoutManager();
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter.isHeader(position) ? manager.getSpanCount() : 1;
            }
        });

        recyclerView.setAdapter(adapter);
    }
}
