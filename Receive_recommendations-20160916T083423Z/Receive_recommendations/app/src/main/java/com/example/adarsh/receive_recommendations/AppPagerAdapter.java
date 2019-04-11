/**
To fetch data from Native Ad manager and hooking the page adapter to app card pager to display ads..
 */package com.example.adarsh.receive_recommendations;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdsManager;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AppPagerAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = AppPagerAdapter.class.getName();

    private final HashMap<Integer, WeakReference<AppCardFragment>> mLoadedFragments = new HashMap<>();
    private AppUnitState mAppUnitState;

    private final AppCardFragment.AbstractAppUnitFactory mLoadingOrErrorUnitFactory = new AppCardFragment.AbstractAppUnitFactory() {
        @Override
        public AbstractUnit createUnit(Context context, int position) {
            AppUnitLoading unitLoading = new AppUnitLoading(context);
            configureLoadingOrErrorCard(unitLoading, false, position);
            return unitLoading;
        }
    };

    private final AppCardFragment.AbstractAppUnitFactory mAppUnitFactory = new AppCardFragment.AbstractAppUnitFactory() {
        @Override
        public AbstractUnit createUnit(Context context, int position) {
            return new AppUnit(context);
        }
    };

    private List<NativeAd> mAds;
    private int mAdCount;

    private boolean mRetry;
    private View.OnClickListener mRetryListener;

    public AppPagerAdapter(FragmentManager fm) {
        super(fm);
        mAds = new LinkedList<>();   //linked list to store ads
        mAdCount = 0;
        mAppUnitState = AppUnitState.Loading;
        mRetry = false;
    }

    /**
     * Setup the ads for the application
     *
     * @param adsManager the {@link NativeAdsManager} to use for initialization
     */
    public void setNativeAdsManager(NativeAdsManager adsManager) {
        mAppUnitState = AppUnitState.Apps;

        mAds.clear();
        mAdCount = adsManager.getUniqueNativeAdCount();             //getting total count
        for (int i = 0; i < mAdCount; i++) {
            mAds.add(adsManager.nextNativeAd());       //assigning thhe data to Linked list
        }
        notifyDataSetChanged();
    }

    /**
     * Initialze the error state of the app
     *
     *  retry whether or not to retry an ads fetch
     */
    public void setErrorState(boolean retry) {
        AppUnitState previousState = mAppUnitState;
        mAppUnitState = AppUnitState.Error;
        mRetry = retry;
        if (previousState == AppUnitState.Loading) {
            rebindFragments();
        } else {
            mAdCount = 2;
            notifyDataSetChanged();
        }
    }

    /**
     * Initializes the loading state of the app
     */
    public void setLoadingState() {
        AppUnitState previousState = mAppUnitState;
        mAppUnitState = AppUnitState.Loading;

        if (previousState == AppUnitState.Error) {
            rebindFragments();
        } else {
            mAdCount = 2;
            notifyDataSetChanged();
        }
    }

    public void setRetryListener(View.OnClickListener retryListener) {
        mRetryListener = retryListener;
    }

    @Override
    public float getPageWidth(int position) {
        return AppUnit.SCREEN_TO_WIDTH_RATIO;
    }

    @Override
    public int getCount() {
        return mAdCount;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        AppCardFragment appCardFragment = new AppCardFragment();
        appCardFragment.setPosition(position);

        if (mAppUnitState != AppUnitState.Apps) {
            appCardFragment.setAbstractAppUnitFactory(mLoadingOrErrorUnitFactory);
        } else {
            appCardFragment.setAbstractAppUnitFactory(mAppUnitFactory);
            appCardFragment.setNativeAd(mAds.get(position));
        }

        return appCardFragment;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        AppCardFragment cardFragment = (AppCardFragment) super.instantiateItem(container, position);
        mLoadedFragments.put(position, new WeakReference<AppCardFragment>(cardFragment));
        return cardFragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Log.e(TAG, position + "\t" + mAdCount);
        if (position < mLoadedFragments.size() && position < mAds.size()) {
            mLoadedFragments.remove(position);
            if (mAppUnitState == AppUnitState.Apps) {
                mAds.get(position).unregisterView();
            }
        }

        super.destroyItem(container, position, object);
    }

    @Override
    public int getItemPosition(Object object) {
        // This is a hack to force a reload of all cards when notifyDataSetChanged() is changed.
        return POSITION_NONE;
    }

    private void rebindFragments() {
        for (Map.Entry<Integer, WeakReference<AppCardFragment>> entry : mLoadedFragments.entrySet()) {
            AppCardFragment fragment = entry.getValue().get();
            if (fragment != null) {
                if (fragment.getAppUnit() instanceof AppUnitLoading) {
                    configureLoadingOrErrorCard((AppUnitLoading) fragment.getAppUnit(), true, entry.getKey());
                }
            }
        }
    }

    private void configureLoadingOrErrorCard(AppUnitLoading unit, boolean animated, int position) {
        if (mAppUnitState == AppUnitState.Loading) {
            unit.setState(AppUnitState.Loading, animated, mRetry);
        } else if (mAppUnitState == AppUnitState.Error) {
            if (position == 0) {
                unit.setState(AppUnitState.Error, animated, mRetry);
                unit.setRetryClickListener(mRetryListener);
            } else {
                unit.setState(AppUnitState.Loading, animated, mRetry);
                unit.setShowLoading(false);
            }
        }
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
