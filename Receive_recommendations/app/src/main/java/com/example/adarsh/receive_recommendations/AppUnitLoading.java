package com.example.adarsh.receive_recommendations;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;

/**
 * A placeholder for an {@link AppUnit} while data is loading
 */
public class AppUnitLoading extends AbstractUnit {

    private ShimmerFrameLayout mShimmerContainer;
    private View mLoadingSectionView;
    private View mErrorSectionView;
    private TextView mErrorMessageView;
    private View mRetryView;

    /**
     * Constructs an AppUnitLoading
     *
     * @param context the application context
     */
    public AppUnitLoading(Context context) {
        super(context);
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        LinearLayout root = (LinearLayout) layoutInflater.inflate(R.layout.app_unit_loading, this);
        setBackground(new ColorDrawable(getResources().getColor(R.color.getrecommendations_loading_background)));
        setOrientation(LinearLayout.VERTICAL);

        View cover = root.findViewById(R.id.cover);
        configureLayoutToAspectRatio(cover);

        mShimmerContainer = (ShimmerFrameLayout) root.findViewById(R.id.shimmer);
        mLoadingSectionView = root.findViewById(R.id.loading_section);
        mErrorSectionView = root.findViewById(R.id.error_section);
        mErrorMessageView = (TextView) root.findViewById(R.id.error_message);
        mRetryView = root.findViewById(R.id.retry);

        mShimmerContainer.setTilt(0);
        mErrorSectionView.setVisibility(View.GONE);
    }

    /**
     * Sets the loading state
     *
     * @param showLoading true to start the loading state, false to stop
     */
    public void setShowLoading(boolean showLoading) {
        if (showLoading) {
            mShimmerContainer.startShimmerAnimation();
        } else {
            mShimmerContainer.stopShimmerAnimation();
        }
    }

    /**
     * Sets the error state
     *
     * @param showError true to go to the error state, false otherwise
     * @param animated  true to animate to the loading state, false otherwise
     * @param retry     true to prompt the user to retry, false otherwise
     */
    public void setShowError(boolean showError, boolean animated, boolean retry) {
        setShowLoading(!showError);

        if (retry) {
            mErrorMessageView.setText(R.string.retry_error_message);
            mErrorMessageView.setVisibility(View.VISIBLE);
            mRetryView.setVisibility(View.VISIBLE);
        } else {
            mErrorMessageView.setText(R.string.unsupported_error_message);
            mErrorMessageView.setVisibility(View.VISIBLE);
            mRetryView.setVisibility(View.GONE);
        }

        if (animated && showError) {
            AnimationUtils.crossFade(mLoadingSectionView, mErrorSectionView, View.INVISIBLE);
        } else {
            if (showError) {
                mLoadingSectionView.setVisibility(View.INVISIBLE);
                mErrorSectionView.setVisibility(View.VISIBLE);
            } else {
                AnimationUtils.crossFadeReset(mLoadingSectionView);
                mErrorSectionView.setVisibility(View.GONE);
            }
        }
        this.requestLayout();
    }

    /**
     * Set the click listener to retry
     *
     * @param listener the click listener to set
     */
    public void setRetryClickListener(OnClickListener listener) {
        mRetryView.setOnClickListener(listener);
    }

    @Override
    public void setState(AppUnitState state, boolean animated, boolean retry) {
        setShowError(state == AppUnitState.Error, animated, retry);
    }
}
