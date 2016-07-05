package com.example.dotua.goldendictbridge;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static com.example.dotua.goldendictbridge.Main_Activity.changeDirectTranslateImageView;
import static com.example.dotua.goldendictbridge.Main_Activity.resetCardViewPosition;
import static com.example.dotua.goldendictbridge.Main_SharedFunction.getDesiredString;
import static com.example.dotua.goldendictbridge.Main_SharedFunction.getReceivedWord;
import static com.example.dotua.goldendictbridge.Main_SharedFunction.getWordList;
import static com.example.dotua.goldendictbridge.Main_SharedFunction.sendMessage;
import static com.example.dotua.goldendictbridge.Main_SharedFunction.showPopupMenu;

public class Main_MyFragment extends Fragment {

    public Main_MyFragment() {
        // Required empty public constructor
    }
    private int numberOfCharacter;
    private View rootView;

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
        numberOfCharacter = getArguments().getInt(getString(R.string.bundle_key_number_of_character), 1);

        rootView = inflater.inflate(R.layout.recycler_view__activity_auto_fit, container, false);

        executeFragmentWordIntent(getActivity());
        return rootView;
    }

    public void executeFragmentWordIntent (final Context context){
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
                String sendString = getReceivedWord();
                DirectTranslate_Task directTranslate_task = new DirectTranslate_Task();
                directTranslate_task.execute(sendString);
                changeDirectTranslateImageView(sendString);
                resetCardViewPosition();
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
        initSwipe(recyclerView, adapter);

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void initSwipe(RecyclerView recyclerView,final RecyclerView_WordListAdapter adapter){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition() - 1; //compensate for header postion
                String sendString;
                if (position >= 0) {
                    sendString = getDesiredString(getWordList(), numberOfCharacter, position);
                }
                else {
                    sendString = getReceivedWord();
                }

//                RecyclerView_TextViewHolder rTVH = (RecyclerView_TextViewHolder) viewHolder;
                if (direction == ItemTouchHelper.LEFT){
//                    adapter.removeItem(position);
//                    Toast.makeText(getContext(), "Up", Toast.LENGTH_SHORT).show();
                    sendMessage(getContext(), sendString,R.integer.action_send);
                } else {
//                    removeView();
//                    edit_position = position;
//                    alertDialog.setTitle("Edit Country");
//                    et_country.setText(countries.get(position));
//                    alertDialog.show();
//                    Toast.makeText(getContext(), "Down", Toast.LENGTH_SHORT).show();
                    sendMessage(getContext(), sendString,R.integer.action_dictionary);
                }
            }

//            @Override
//            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//
//                Bitmap icon;
//                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
//
//                    View itemView = viewHolder.itemView;
//                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
//                    float width = height / 3;
//
//                    Paint p = new Paint();
//
//                    if(dY > 0){
////                        p.setColor(Color.parseColor("#388E3C"));
////                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
////                        c.drawRect(background,p);
////                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_brightness_3_black_48px);
////                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
////                        c.drawBitmap(icon,null,icon_dest,p);
//                    } else {
////                        p.setColor(Color.parseColor("#D32F2F"));
////                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
////                        c.drawRect(background,p);
////                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_g_translate_chosen);
////                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
////                        c.drawBitmap(icon,null,icon_dest,p);
//                    }
//                }
//                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }


}
