package com.shahareinisim.tzachiapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class TfilonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tfilon);

        initCardView("שחרית", R.drawable.banner_tfhilot, view -> tfilahFragment(TfilahFragment.Tfilah.SHACHRIT));
        initCardView("מנחה", R.drawable.banner_tfhilot, view -> tfilahFragment(TfilahFragment.Tfilah.MINCHA));
        initCardView("ערבית", R.drawable.banner_tfhilot, view -> tfilahFragment(TfilahFragment.Tfilah.HARVIT));
        initCardView("ברכת המזון", R.drawable.banner_tfhilot, view -> tfilahFragment(TfilahFragment.Tfilah.BIRCAT_HAMAZON));
        initCardView("ברכת הלבנה", R.drawable.banner_tfhilot, view -> tfilahFragment(TfilahFragment.Tfilah.BIRCAT_HALEVANA));
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

    public void tfilahFragment(TfilahFragment.Tfilah tfilah) {

        FragmentManager fm = getSupportFragmentManager();
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        // replace the FrameLayout with new Fragment
        fragmentTransaction.add(R.id.fragment_container, new TfilahFragment(tfilah), "tag");
//        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.addToBackStack("");

        fragmentTransaction.commit();
    }
}