
package com.example.adarsh.receive_recommendations;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;

import java.util.Arrays;

/**
 * A class to represent a single ad unit in the application
 */
public class AppUnit extends AbstractUnit {

    private LinearLayout mRoot;
    private MediaView mCover;
    private ImageView mIcon;
    private TextView mTitle;
    private TextView mSubtitle;
    private TextView mContextSentence;
    private TextView mDescription;
    private TextView mCtaButton;

    /**
     * Constructs an AppUnit
     *
     * @param context the application context
     */
    public AppUnit(Context context) {
        super(context);
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        mRoot = (LinearLayout) layoutInflater.inflate(R.layout.app_unit, this);
        setBackground(new ColorDrawable(getResources().getColor(R.color.getrecommendations_background)));
        setOrientation(LinearLayout.VERTICAL);
        mCover = (MediaView) mRoot.findViewById(R.id.media);
        mIcon = (ImageView) mRoot.findViewById(R.id.icon);
        mTitle = (TextView) mRoot.findViewById(R.id.title);
        mSubtitle = (TextView) mRoot.findViewById(R.id.subtitle);
        mContextSentence = (TextView) mRoot.findViewById(R.id.context_sentence);
        mDescription = (TextView) mRoot.findViewById(R.id.description);
        mCtaButton = (TextView) mRoot.findViewById(R.id.cta_button);
    }

    @Override
    public void configure(NativeAd ad) {      // configuring ads Assigning  values to title,CTA   (class of Abstractunit)
        mCover.setNativeAd(ad);
        configureLayoutToAspectRatio(mCover);
        NativeAd.Image adIcon = ad.getAdIcon();             //image
        NativeAd.downloadAndDisplayImage(adIcon, mIcon);
        mTitle.setText(ad.getAdTitle());                    //title
        String subtitle = ad.getAdSubtitle();
        mSubtitle.setVisibility(View.GONE);
        if (subtitle != null && !subtitle.isEmpty()) {
            mSubtitle.setText(subtitle);
            mSubtitle.setVisibility(View.VISIBLE);
        }
        mContextSentence.setText(ad.getAdSocialContext());      //title of ad
        mDescription.setText(ad.getAdBody());       // setting body
        mCtaButton.setText(ad.getAdCallToAction());     //cta


        View[] clickEnabledViews = {mRoot};
        ad.registerViewForInteraction(mRoot, Arrays.asList(clickEnabledViews));
    }
}
