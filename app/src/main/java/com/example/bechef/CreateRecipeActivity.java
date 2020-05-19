package com.example.bechef;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ListView;
import android.widget.TableLayout;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class CreateRecipeActivity extends AppCompatActivity {

    private TabLayout tabs;
    private ViewPager viewPager;

    public interface FragmentTouchListener {
        void onTouchEvent(MotionEvent event);
    }

    private List<FragmentTouchListener> myTouchListeners = new ArrayList<>();

    public void registerFragmentTouchListener(FragmentTouchListener listener) {
        myTouchListeners.add(listener);
    }

    public void unRegisterMyTouchListener(FragmentTouchListener listener) {
        myTouchListeners.remove(listener);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for(FragmentTouchListener listener:myTouchListeners) {
            listener.onTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);

        tabs = (TabLayout) findViewById(R.id.create_recipe_layout);
        viewPager = (ViewPager) findViewById(R.id.create_recipe_viewPager);

        setupViewPager(viewPager);

        tabs.setupWithViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager){
        PageAdapter viewPagerAdapter = new PageAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new CreateRecipeTab1(), "Step 1");
        viewPagerAdapter.addFragment(new CreateRecipeTab2(), "Step 2");
        viewPagerAdapter.addFragment(new CreateRecipeTab3(), "Step 3");
        viewPager.setAdapter(viewPagerAdapter);
    }

}
