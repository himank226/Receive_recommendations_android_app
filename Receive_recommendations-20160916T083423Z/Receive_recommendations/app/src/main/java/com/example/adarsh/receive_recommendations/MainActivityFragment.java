package com.example.adarsh.receive_recommendations;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.ads.AdError;
import com.facebook.ads.NativeAdsManager;

/**
 * The main GetApps fragment
 */
public class MainActivityFragment extends Fragment implements NativeAdsManager.Listener {

    private static final String TAG = MainActivityFragment.class.getName();
    private static final int ADNW_APP_DISABLED = 1005;
    private static final int NUM_ADS = 10;

    private NativeAdsManager mAdsManager;
    private AppCardPager mAppPager;
    private AppPagerAdapter mAppPagerAdapter;
    private OnLogListener mLogCallback;

    /**
     * A logging interface for app events
     */
    public interface OnLogListener {

        /**
         * Logs a network error
         */
        public void onNetworkError();

        /**
         * Logs a no fill error
         */
        public void onNoFillError();

        /**
         * Logs an app scrolled event with the given parameters
         * @param parameters accompanying parameters for the event
         */
        public void onAppsScrolled(Bundle parameters);
    }

    private final ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                Bundle parameters = new Bundle();
                parameters.putInt("position", mAppPager.getCurrentItem());

                if (mLogCallback != null) {
                    mLogCallback.onAppsScrolled(parameters);
                }
            }
        }
    };

    private final View.OnClickListener mRetryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mAppPagerAdapter.setLoadingState();

            configureAdsManager();
        }
    };

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // If we have been backgrounded for long enough to have been gc'd, its worth
        // fetching a fresh set of ads
        configureAppPager();
        configureAdsManager();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof OnLogListener) {
            mLogCallback = (OnLogListener) activity;
        }
    }

    private void configureAdsManager() {
        mAdsManager = new NativeAdsManager(getActivity(), Utils.PLACEMENT_ID, NUM_ADS);
        mAdsManager.setListener(this);   //setting listeners...we have 2 listeners onadloaded and on Aderror
        mAdsManager.loadAds();

        Log.i(TAG, String.format("Attempting load of %d ads in placement %s", NUM_ADS, Utils.PLACEMENT_ID));
    }

    private void configureAppPager() {
        mAppPagerAdapter = new AppPagerAdapter(getChildFragmentManager());
        mAppPagerAdapter.setLoadingState();
        mAppPagerAdapter.setRetryListener(mRetryListener);

        mAppPager = (AppCardPager) getView().findViewById(R.id.app_pager);
        mAppPager.setOnPageChangeListener(mOnPageChangeListener);
        mAppPager.setAdapter(mAppPagerAdapter);
        mAppPager.setPageMargin((int) getResources().getDimension(R.dimen.getrecs_blue_padding));
        mAppPager.setOffscreenPageLimit(AppCardPager.OFF_SCREEN_PAGE_LIMIT);
        mAppPager.setAppUnitState(AppUnitState.Loading);
    }

    @Override
    public void onAdsLoaded() {
        if (mAdsManager.getUniqueNativeAdCount() != 0) {
            Log.i(TAG, String.format("Loaded %d ads", mAdsManager.getUniqueNativeAdCount()));
            mAppPagerAdapter.setNativeAdsManager(mAdsManager);
            mAppPager.setAppUnitState(AppUnitState.Apps);
        } else {
            Log.w(TAG, "Successful ad request with 0 fill");
            if (mLogCallback != null) {
                mLogCallback.onNoFillError();
            }
            mAppPagerAdapter.setErrorState(true);
            mAppPager.setAppUnitState(AppUnitState.Error);
        }
    }

    @Override
    public void onAdError(AdError adError) {
        Log.w(TAG, "Encountered ad loading error '" + adError.getErrorMessage() + "'");
        if (mLogCallback != null) {
            mLogCallback.onNetworkError();
        }
        mAppPager.setAppUnitState(AppUnitState.Error);
        if (adError.getErrorCode() == ADNW_APP_DISABLED) {
            mAppPagerAdapter.setErrorState(false);
        } else {
            mAppPagerAdapter.setErrorState(true);
        }
    }

    @Override
    public void onDestroy() {
        mAdsManager.setListener(null);
        mAdsManager = null;
        mAppPager = null;
        mAppPagerAdapter = null;
        mLogCallback = null;
        super.onDestroy();
    }
}
