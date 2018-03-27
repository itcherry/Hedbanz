package com.transcendensoft.hedbanz.presentation.mainscreen;

import android.os.Bundle;
import android.view.View;

import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.presentation.base.BaseActivity;
import com.transcendensoft.hedbanz.presentation.custom.widget.VerticalViewPager;
import com.transcendensoft.hedbanz.presentation.menu.MenuFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.Lazy;

public class MainActivity extends BaseActivity {
    @BindView(R.id.verticalViewPager) VerticalViewPager mViewPager;

    @Inject Lazy<MainFragment> mainFragmentLazy;
    @Inject Lazy<MenuFragment> menuFragmentLazy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this, this);

        initViewPager();
    }

    private void initViewPager(){
        MainScreenFragmentAdapter adapter = new MainScreenFragmentAdapter.Holder(getSupportFragmentManager())
                .add(mainFragmentLazy.get())
                .add(menuFragmentLazy.get())
                .set();
        mViewPager.setAdapter(adapter);
        mViewPager.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
    }

    public void openMenu(){
        mViewPager.setCurrentItem(1, true);
    }

    @Override
    public void onBackPressed() {
        if(mViewPager.getCurrentItem() != 0) {
            mViewPager.setCurrentItem(0, true);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void showServerError() {

    }

    @Override
    public void showNetworkError() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showContent() {

    }

    @Override
    public void hideAll() {

    }
}