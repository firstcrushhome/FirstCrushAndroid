package co.firstcrush.firstcrush;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class TravelFragment extends Fragment {
    public WebView webTravelView;
    private BottomNavigationView navigation;
    private static boolean activityStarted;
    private View mCustomView;
    private RelativeLayout mContentView;
    private FrameLayout mCustomViewContainer;
    private ViewGroup mContentViewContainer;
    private WebChromeClient.CustomViewCallback mCustomViewCallback;
    private MyWebChromeClient mWebChromeClient = null;
    private ProgressDialog progressBar;
    View decorView;
    AudioManager audioManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View travelView=inflater.inflate(R.layout.travel_fragment, container, false);
        webTravelView = (WebView) travelView.findViewById(R.id.web1);
        // Enable Javascript
        WebSettings webSettings = webTravelView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        // Enable Javascript
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSaveFormData(true);
        //webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.supportMultipleWindows();
        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= 20) {
            String ua = "Chrome";
            webTravelView.getSettings().setUserAgentString(ua);
        }
        webSettings.setBuiltInZoomControls(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webTravelView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            webTravelView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        // Force links and redirects to open in the WebView instead of in a browser
        mWebChromeClient = new MyWebChromeClient();
        webTravelView.setWebChromeClient(mWebChromeClient);
        webTravelView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                if (progressBar != null)
                    progressBar.dismiss();
            }
        });
        progressBar = ProgressDialog.show(getActivity(), "", "Loading...");
        webTravelView.loadUrl("http://www.firstcrush.co/travel/");
        webTravelView.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && event.getAction() == MotionEvent.ACTION_UP) {
                    if(webTravelView.canGoBack()&& mCustomView == null) {
                        webTravelView.goBack();
                        return true;
                    }
                    else
                    {
                        decorView = getActivity().getWindow().getDecorView();
                        decorView.setSystemUiVisibility(
                                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                    }
                }

                if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)) {
                    //handler.sendEmptyMessage(2);
                    audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND + AudioManager.FLAG_SHOW_UI);
                    return true;
                }
                if
                        ((keyCode == KeyEvent.KEYCODE_VOLUME_UP)) {
                    //handler.sendEmptyMessage(3);
                    audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND + AudioManager.FLAG_SHOW_UI);
                    return true;
                }
                if ((keyCode == KeyEvent.KEYCODE_APP_SWITCH)) {
                    return true;
                }
                return false;
            }
        });
        return travelView;
    }

    @Override
    public void onPause() {
        super.onPause();    //To change body of overridden methods use File | Settings | File Templates.
        webTravelView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();    //To change body of overridden methods use File | Settings | File Templates.
        webTravelView.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();    //To change body of overridden methods use File | Settings | File Templates
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        webTravelView = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        webTravelView.saveState(outState);
    }





    public class MyWebChromeClient extends WebChromeClient {
        private int mOriginalOrientation;
        private Context mContext;
        FrameLayout.LayoutParams LayoutParameters = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            super.onShowCustomView(view, callback);
            decorView = getActivity().getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            // if a view already exists then immediately terminate the new one
            if (mCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }
            mOriginalOrientation = getActivity().getRequestedOrientation();
            //mContentView = (RelativeLayout) getView();
            mContentView = getActivity().findViewById(R.id.activity_main);
            if (mContentView != null)
            {
                mContentView.setVisibility(View.GONE);
                mContentViewContainer=(ViewGroup) mContentView.getParent();
                mContentViewContainer.removeView(mContentView);
            }
            mCustomViewContainer = new FrameLayout(getActivity());
            mCustomViewContainer.setLayoutParams(LayoutParameters);
            mCustomViewContainer.setBackgroundResource(android.R.color.black);
            view.setLayoutParams(LayoutParameters);
            mCustomViewContainer.addView(view);
            mCustomView = view;
            mCustomViewCallback = callback;
            mCustomViewContainer.setVisibility(View.VISIBLE);
            getActivity().setContentView(mCustomViewContainer);
        }

        @Override
        public void onHideCustomView() {
            super.onHideCustomView();
            decorView = getActivity().getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            if (mCustomView != null) {
                // Hide the custom view.
                mCustomView.setVisibility(View.GONE);
                // Remove the custom view from its container.
                mCustomViewContainer = (FrameLayout) mCustomView.getParent();
                // Remove the custom view from its container.
                mCustomViewContainer.removeView(mCustomView);
                mCustomView = null;
                mCustomViewContainer.setVisibility(View.GONE);
                mCustomViewCallback.onCustomViewHidden();

                // Show the content view.
                mContentView.setVisibility(View.VISIBLE);
                getActivity().setContentView(mContentView);

            }
        }
    }
}