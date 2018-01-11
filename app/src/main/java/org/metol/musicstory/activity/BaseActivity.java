package org.metol.musicstory.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.getkeepsafe.taptargetview.TapTarget;
import com.lapism.searchview.SearchAdapter;
import com.lapism.searchview.SearchHistoryTable;
import com.lapism.searchview.SearchItem;
import com.lapism.searchview.SearchView;
import com.melnykov.fab.FloatingActionButton;

import org.metol.musicstory.Common;
import org.metol.musicstory.R;
import org.metol.musicstory.adapter.ViewPagerAdapter;
import org.metol.musicstory.fragment.BaseFragment;
import org.metol.musicstory.fragment.CardBottomSheetFragment;
import org.metol.musicstory.model.MusicStory;
import org.metol.musicstory.model.Constants;
import org.metol.musicstory.util.GlideManager;
import org.metol.musicstory.util.ImeUtils;

import java.net.URISyntaxException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Broccoli.Huang on 2018/1/3.
 */
public abstract class BaseActivity extends AppCompatActivity{
    @BindView(R.id.cl_main)             CoordinatorLayout       mCL_main;
    @BindView(R.id.toolbar)             Toolbar                 mToolbar;
    @BindView(R.id.tv_toolbar_title)    TextView                mToolbarTitle;
    @BindView(R.id.fab)                 FloatingActionButton    mFAB;
    @BindView(R.id.tab_layout)          TabLayout               mTabLayout;
    @BindView(R.id.view_tab_shadow)     View                    mViewTabShadow;
    @BindView(R.id.view_pager)          ViewPager               mViewPager;
    @BindView(R.id.fl_custom)           FrameLayout             mFL_custom;
    @BindView(R.id.searchView)          SearchView              mSearchView;
    @BindView(R.id.wv_urlscheme)        WebView                 mWebViewUrlScheme;

    protected ViewStub mVS_custom;
    private MenuItem mi_search;
    private MenuItem mi_profile;

    private SearchHistoryTable mHistoryDatabase;
    private ViewPagerAdapter mViewPagerAdapter;
    private int nowPage = 0;
    private String search_voice_keyword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        ButterKnife.bind(this);

        //Hide without an animation
        mFAB.hide(false);

        setSupportActionBar(mToolbar);
        mToolbar.inflateMenu(R.menu.menu_main);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.toolbar_background));
        if (canToolBarBack()) {
            mToolbar.setNavigationIcon(R.drawable.ic_navigate_before_black_24dp);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

        if(isSearchVisible()){
            setSearchView();
        }else{
            findViewById(R.id.searchView).setVisibility(View.GONE);
        }

        if((getTabTitle() == null || getTabTitle().length == 0) && (getTabIcon() == null || getTabIcon().length == 0)) {
            mViewPager.setVisibility(View.GONE);
            mTabLayout.setVisibility(View.GONE);
            mViewTabShadow.setVisibility(View.GONE);

            mVS_custom = new ViewStub(this);
            mFL_custom.addView(mVS_custom);
            mFL_custom.setVisibility(View.VISIBLE);
            if(getCustomView() != 0) mVS_custom.setLayoutResource(getCustomView());
//            mVS_custom.inflate();
        }else{
            mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
            mViewPagerAdapter.onRestoreInstanceState(savedInstanceState);
            for (int index = 0; index < getTabFragment().size(); index++) {
                mViewPagerAdapter.addFragment("", getTabFragment().get(index));
            }

            mViewPager.setAdapter(mViewPagerAdapter);
            mViewPager.setOffscreenPageLimit(mViewPager.getAdapter().getCount());
            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    onViewPagerPageSelected(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });

//		    mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
//		    mTabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
            mTabLayout.setupWithViewPager(mViewPager);
            mTabLayout.setTabTextColors(getResources().getColor(R.color.tab_text_not_focus), getResources().getColor(R.color.app_theme));
            mTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.app_theme));

            onViewPagerPageSelected(nowPage);
        }
        initFAB(nowPage);
    }

    private void onViewPagerPageSelected(int position){
        if(isShowTab()){
            if (getTabIcon() != null && getTabIcon().length > 0) {
                for (int index = 0; index < getTabIcon().length; index++) {
                    Drawable d = getResources().getDrawable(getTabIcon()[index]);
                    DrawableCompat.setTint(d, getResources().getColor((index == position ? R.color.app_theme : R.color.tab_icon_not_focus)));
                    mTabLayout.getTabAt(index).setIcon(d);
                }
                mToolbar.setTitle(getTabTitle()[position]);
            } else {
                for (int index = 0; index < getTabTitle().length; index++) {
                    mTabLayout.getTabAt(index).setText(getTabTitle()[index]);
                }
            }
        }else{
            mTabLayout.setVisibility(View.GONE);
        }
        initFAB(position);
        nowPage = position;
    }

    protected void initFAB(int position){
        Drawable d = getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp);
        DrawableCompat.setTint(d, getResources().getColor(R.color.app_theme));
        mFAB.setImageDrawable(d);
        mFAB.setColorNormal(getResources().getColor(R.color.fab_focus));
        mFAB.setColorPressed(getResources().getColor(R.color.fab_pressed));
        mFAB.setColorRipple(getResources().getColor(R.color.fab_ripple));

        mFAB.hide();
        if(mViewPagerAdapter != null && position<=mViewPagerAdapter.getCount()-1 && mViewPagerAdapter.getItem(position) != null && mViewPagerAdapter.getItem(position) instanceof BaseFragment) {
            ((BaseFragment) mViewPagerAdapter.getItem(position)).setFAB(mFAB);
        }
    }

    protected void setSearchView() {
        mHistoryDatabase = new SearchHistoryTable(this);

        mSearchView = (SearchView) findViewById(R.id.searchView);
        if (mSearchView != null) {
            mSearchView.setArrowOnly(false);
            mSearchView.setHint(getString(R.string.search_hint));
            mSearchView.setVersion(SearchView.VERSION_MENU_ITEM);
            mSearchView.setVersionMargins(SearchView.VERSION_MARGINS_MENU_ITEM);
            mSearchView.setTheme(SearchView.THEME_LIGHT, true);
//            mSearchView.setQuery(extras.getString(EXTRA_KEY_TEXT), false);
//            mSearchView.setTextOnly();
            mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    if(TextUtils.isEmpty(search_voice_keyword)){
                    }else{
                        search_voice_keyword = "";
                    }
                    search(query);
                    mSearchView.close(false);
                    mSearchView.setTextOnly("");
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if(TextUtils.isEmpty(newText)) search_voice_keyword = "";
                    return false;
                }
            });
            mSearchView.setOnOpenCloseListener(new SearchView.OnOpenCloseListener() {
                @Override
                public boolean onOpen() {
                    if (mFAB != null) {
                        mFAB.hide();
                    }
                    return true;
                }

                @Override
                public boolean onClose() {
                    return true;
                }
            });
            mSearchView.setVoiceText(getString(R.string.search_voice_hint));
            mSearchView.setOnVoiceClickListener(new SearchView.OnVoiceClickListener() {
                @Override
                public void onVoiceClick() {
                    // permission
                }
            });

//            ArrayList<SearchItem> suggestionsList = new ArrayList<>();
//            suggestionsList.add(new SearchItem("search1"));
//            suggestionsList.add(new SearchItem("search2"));
//            suggestionsList.add(new SearchItem("search3"));
//            SearchAdapter searchAdapter = new SearchAdapter(this, suggestionsList);
            SearchAdapter searchAdapter = new SearchAdapter(this);
            searchAdapter.addOnItemClickListener(new SearchAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int i) {
                    TextView textView = (TextView) view.findViewById(R.id.textView_item_text);
                    String query = textView.getText().toString();
                    search(query);
                    mSearchView.close(false);
                }
            });
            mSearchView.setAdapter(searchAdapter);
        }
    }

    protected void search(String text) {
        search_voice_keyword = "";
        text = text.trim();
        mHistoryDatabase.addItem(new SearchItem(text));
        intentSearchActivity(text);
    }

    public void showBottomSheet(MusicStory musicStory){
        mFAB.hide();

        final CardBottomSheetFragment baseCardBottomSheetFragment = getCardBottomSheetFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.ARGUMENTS_MUSICSTORY, musicStory);
        baseCardBottomSheetFragment.setArguments(bundle);
        baseCardBottomSheetFragment.setCallback(new CardBottomSheetFragment.Callback_BottomSheet() {
            @Override
            public void onDismiss() {
                initFAB(nowPage);
            }
        });
        baseCardBottomSheetFragment.show(getSupportFragmentManager(), R.id.bottomsheet);
    }

    public void requestShowTarget(TapTarget... tapTargets){
        shownTapTarget(tapTargets);
    }

    private static Snackbar mSnackbar = null;
    /**
     * 會自動將該頁面之前產生的Toast清除後再顯示新的Toast
     */
    public void showSnack(int res) {
        showSnack(res, Snackbar.LENGTH_SHORT);
    }
    public void showSnack(CharSequence text) {
        showSnack(text, Snackbar.LENGTH_SHORT);
    }
    public void showSnack(int res, int duration) {
        if(mSnackbar != null) mSnackbar.dismiss();
        mSnackbar = Snackbar.make(mCL_main, res, duration);
        mSnackbar.show();
    }
    public void showSnack(CharSequence text, int duration) {
        if(mSnackbar != null) mSnackbar.dismiss();
        mSnackbar = Snackbar.make(mCL_main, text, duration);
        mSnackbar.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        searchView.activityResumed();
        if(mViewPager.getCurrentItem()==0){
            if(TextUtils.isEmpty(getStaticCenterTitle())) {
                mToolbar.setTitle(getTabTitle()[0]);
            }else{
                mToolbar.setTitle("");
                mToolbarTitle.setVisibility(View.VISIBLE);
                mToolbarTitle.setText(getStaticCenterTitle());
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if(mViewPagerAdapter != null) mViewPagerAdapter.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SearchView.SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (results != null && results.size() > 0) {
                String searchWrd = results.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    search_voice_keyword = searchWrd;
                    if (mSearchView != null) {
                        mSearchView.setTextOnly(searchWrd);
                        ImeUtils.showIme(mSearchView);
                        //直接搜尋
//                        mSearchView.setQuery(searchWrd, true);
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        mi_search = menu.findItem(R.id.action_search);
        //TODO mi_profile頭像太小
        mi_profile = menu.findItem(R.id.action_profile);

        mi_search.setVisible(isSearchVisible());
        mi_profile.setVisible(isInfoVisible());

        if(isInfoVisible()) {
            GlideManager.getDrawableFbAvatarFromUrl(BaseActivity.this, Common.getFbID(), GlideManager.FbAvatarType.TYPE_SMALL, new GlideManager.Callback() {
                @Override
                public void onDrawable(Drawable drawable) {
                    if (drawable != null) mi_profile.setIcon(drawable);
                }

                @Override
                public void onBitmap(Bitmap bitmap) {
                }
            });
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == mi_search.getItemId()) {
            mSearchView.open(true, item);
            return true;
        }else if(item.getItemId() == mi_profile.getItemId()) {
            intentInfoActivity();
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }

    public void openKKBOXUrlScheme(String requestUrl){
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo("com.skysoft.kkbox.android", PackageManager.GET_ACTIVITIES);

            mWebViewUrlScheme.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (url.startsWith("intent://")) {
                        try {
                            startActivity(Intent.parseUri(url, Intent.URI_INTENT_SCHEME));
                        } catch (URISyntaxException urise) {
                        } catch (ActivityNotFoundException anfe) {
                            return false;
                        }
                        return true;
                    }

                    return false;
                }
            });

            mWebViewUrlScheme.getSettings().setJavaScriptEnabled(true);
            mWebViewUrlScheme.loadUrl(requestUrl);
        } catch (PackageManager.NameNotFoundException e) {
            showSnack("您沒有安裝KKBOX哦");
        }
    }

    public FloatingActionButton getFAB(){
        return mFAB;
    }

    protected abstract String getStaticCenterTitle();
    protected abstract String[] getTabTitle();
    protected abstract int[] getTabIcon();
    protected abstract boolean isShowTab();
    protected abstract ArrayList<Fragment> getTabFragment();
    protected abstract boolean canToolBarBack();
    protected abstract boolean isSearchVisible();
    protected abstract boolean isInfoVisible();
    protected abstract int getCustomView();
    protected abstract CardBottomSheetFragment getCardBottomSheetFragment();
    protected abstract void intentSearchActivity(String keyword);
    protected abstract void intentInfoActivity();
    protected abstract void shownTapTarget(TapTarget... tapTargets);
}
