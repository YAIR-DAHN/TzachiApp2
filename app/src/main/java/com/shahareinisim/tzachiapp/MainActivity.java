package com.shahareinisim.tzachiapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    static final String MAIN_WEB_LINK = "https://63863ecdd7031.site123.me/";
    static final String DONATION_LINK = "https://www.matara.pro/nedarimplus/online/?mosad=7001292";
    static final String ZMANEI_TFILOT_LINK = "https://63863ecdd7031.site123.me/#section-64a56d581a3bc";


    @SuppressLint({"SetJavaScriptEnabled", "UseCompatLoadingForDrawables"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWVCardView("אתר ראשי", R.drawable.banner_main_website, MAIN_WEB_LINK);
        initCardView("תפילון", R.drawable.banner_tfhilot, view -> {
            Intent intent = new Intent(MainActivity.this, TfilonActivity.class);
            startActivity(intent);
        });
        initWVCardView("זמני התפילות", R.drawable.banner_tfhilot, ZMANEI_TFILOT_LINK);
        initWVCardView("לתרומות", R.drawable.banner_donations, DONATION_LINK);
    }

    public void initWVCardView(String title, int bannerImg, String url) {

        MainItem mainItem = new MainItem(0, title, bannerImg, this);
        mainItem.setOnClickListener(view -> webFragment(url));
        ((LinearLayout) findViewById(R.id.dashboard)).addView(mainItem);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mainItem.getLayoutParams();
        params.setMargins(convertToPX(20), convertToPX(5), convertToPX(20), convertToPX(5));
    }

    public void initCardView(String title, int bannerImg, View.OnClickListener onClickListener) {

        MainItem mainItem = new MainItem(0, title, bannerImg, this);
        mainItem.setOnClickListener(onClickListener);
        ((LinearLayout) findViewById(R.id.dashboard)).addView(mainItem);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mainItem.getLayoutParams();
        params.setMargins(convertToPX(20), convertToPX(5), convertToPX(20), convertToPX(5));
    }

    public int convertToPX(int dp) {
        Resources r = getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, r.getDisplayMetrics());
    }

    public void webFragment(String url) {

        FragmentManager fm = getSupportFragmentManager();
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        // replace the FrameLayout with new Fragment
        fragmentTransaction.add(R.id.fragment_container, WebViewFragment.newInstance(url, ""), "tag");
//        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.addToBackStack("");

        fragmentTransaction.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        int stackCount = getSupportFragmentManager().getBackStackEntryCount();
        if (stackCount > 0) {
            try {
                WebViewFragment fragment  = (WebViewFragment) getSupportFragmentManager().getFragments().get(stackCount-1);
                if ((keyCode == KeyEvent.KEYCODE_BACK) && fragment.canGoBack()) {
                    fragment.goBack();
                    return true;
                }
            } catch (IndexOutOfBoundsException exception) {
                Log.d("IndexOutOfBoundsException", exception.toString());
            }
        }

        Log.d("", "stackCount="+stackCount);
        return super.onKeyDown(keyCode, event);
    }
}