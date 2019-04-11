package com.example.adarsh.receive_recommendations;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.ads.NativeAd;

public class AppCardFragment extends Fragment {

    private AbstractUnit mAppUnit; //to have customised view of ad
    private NativeAd mNativeAd;         // for ad unit
    private AbstractAppUnitFactory mAbstractAppUnitFactory;
    private int mPosition = -1;

    public interface AbstractAppUnitFactory { //factory design pattern to create factory of class
        AbstractUnit createUnit(Context context, int position);  //here we are creating the  factory of abstract unit class
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        if (mAbstractAppUnitFactory == null || mPosition < 0) {
            return new AppUnitLoading(getActivity());
        }

        mAppUnit = mAbstractAppUnitFactory.createUnit(getActivity(), mPosition);  //creating abstract display unit for a ad
        mAppUnit.configure(mNativeAd);          //configuring the obtained ad in a abstract unit
        return mAppUnit;
    }

    public void setNativeAd(NativeAd ad) {
        mNativeAd = ad;
    }           //setting the fetched ad in a unit

    public void setAbstractAppUnitFactory(AbstractAppUnitFactory abstractAppUnitFactory) {
        mAbstractAppUnitFactory = abstractAppUnitFactory;
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    /**
     * @return the {@link AbstractUnit} associated with this fragment
     */
    public AbstractUnit getAppUnit() {
        return mAppUnit;
    }
}
