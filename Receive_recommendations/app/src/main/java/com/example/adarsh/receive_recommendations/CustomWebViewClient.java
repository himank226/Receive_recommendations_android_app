package com.example.adarsh.receive_recommendations;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CustomWebViewClient extends WebViewClient {
    private String mInitialUrl;
    private ProgressBar mProgressBar;
    private Context mContext;
    private TextView mErrorView;
    private boolean mError;

    public CustomWebViewClient(
            String initialUrl,
            ProgressBar progressBar,
            Context context,
            TextView errorView) {
        super();
        mInitialUrl = initialUrl;
        mProgressBar = progressBar;
        mContext = context;
        mErrorView = errorView;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        if (!mError) {
            mProgressBar.setVisibility(View.GONE);
            view.setVisibility(View.VISIBLE);
            super.onPageFinished(view, url);
        }
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.equals(mInitialUrl)) {
            return false;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));    //redirecting to given url
        mContext.startActivity(intent);
        return true;
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        if (errorCode == WebViewClient.ERROR_HOST_LOOKUP) {
            showError(view);
        }
    }

    private void showError(final WebView view) {
        mError = true;
        AnimationUtils.crossFade(mProgressBar, mErrorView, View.INVISIBLE);
    }
}