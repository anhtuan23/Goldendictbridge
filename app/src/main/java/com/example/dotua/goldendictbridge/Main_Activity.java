package com.example.dotua.goldendictbridge;

import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import me.grantland.widget.AutofitTextView;

import static com.example.dotua.goldendictbridge.Main_MyFragment.newMyFragmentInstance;
import static com.example.dotua.goldendictbridge.Main_SharedFunction.getReceivedWord;

public class Main_Activity extends NavigationDrawerActivity {

    private static Menu mOptionsMenu = null;
    public Menu getMenu(){return mOptionsMenu;}
    private static AutofitTextView directTranslateTextView;
    private static CardView cardView;
    private  static ImageView imageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Main_CustomViewPager viewPager = (Main_CustomViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.setPagingEnabled(false);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        cardView = (CardView)findViewById(R.id.card_view) ;
        directTranslateTextView = (AutofitTextView) findViewById(R.id.direct_translate);
        imageView = (ImageView) findViewById(R.id.thumbnail);

        Main_SharedFunction.generateWordList(this,this.getIntent());
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new WordHistory_Fragment(),"HISTORY");
        adapter.addFragment(newMyFragmentInstance(this, 1), "ONE");
        adapter.addFragment(newMyFragmentInstance(this, 2), "TWO");
        adapter.addFragment(newMyFragmentInstance(this, 3), "THREE");
//        adapter.addFragment(newMyFragmentInstance(this, 4), "FOUR");
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mOptionsMenu = menu;
        getMenuInflater().inflate(R.menu.main__menu, menu);

        MenuItem item = menu.findItem(R.id.my_toggle);
        ToggleButton switchModeToggle= (ToggleButton)item.getActionView().
                findViewById(R.id.toggle_share_mode);

        final SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        switchModeToggle.setChecked(sharedPref.getBoolean(getString(R.string.pref_share_mode_key),false));

        final SharedPreferences.Editor editor = sharedPref.edit();
        //set false as default
//        editor.putBoolean(getString(R.string.pref_share_mode_key), false);
//        editor.commit();

        switchModeToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((ToggleButton) v).isChecked()) {
                    editor.putBoolean(getString(R.string.pref_share_mode_key), true);
                } else {
                    editor.putBoolean(getString(R.string.pref_share_mode_key), false);
                }
                editor.apply();
            }
        });

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        String receivedWord = getReceivedWord();
        executeDirectTranslateTask(receivedWord);
        executeDirectTranslateImageView(receivedWord);
    }

    public static void updateSearchViewQuery(String query) {
        SearchView searchView =
                (SearchView) mOptionsMenu.findItem(R.id.search).getActionView();
        searchView.setQuery(query, false);
        searchView.setIconified(false);
    }

    public static void changeDirectTranslateAutoFitTextView(String translatedText){
        directTranslateTextView.setText(translatedText);
    }

    public static void executeDirectTranslateTask(String string){
        DirectTranslate_Task directTranslate_Task = new DirectTranslate_Task();
        directTranslate_Task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, string);
    }

    public static void executeDirectTranslateImageView(String query){
        DirectTranslate_GetImageTask directTranslate_getImageTask = new DirectTranslate_GetImageTask(
                new DirectTranslate_GetImageTask.AsyncResponse(){
                    @Override
                    public void processFinish(String imageUrl){
//                        Toast.makeText(imageView.getContext(), imageUrl, Toast.LENGTH_SHORT).show();
                        Picasso.with(imageView.getContext())
                                .load(imageUrl)
                                .placeholder(R.drawable.placeholder)
                                .error(R.drawable.error)
                                .into(imageView);
                    }
                }
        );
//        directTranslate_getImageTask.execute(query);
        directTranslate_getImageTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,query);
    }

    public static void resetCardViewPosition(){
        cardView.setTranslationY(0);
    }
}
