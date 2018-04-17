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

public class NotificationsFragment extends Fragment {
    public WebView webNotificationsView;
    private BottomNavigationView navigation;
    private static boolean activityStarted;
    private View mCustomView;
    private RelativeLayout mContentView;
    private ViewGroup mContentViewContainer;
    private FrameLayout mCustomViewContainer;
    private WebChromeClient.CustomViewCallback mCustomViewCallback;
    private MyWebChromeClient mWebChromeClient = null;
    private ProgressDialog progressBar;
    View decorView;
    AudioManager audioManager;

    public static NotificationsFragment newInstance() {
        NotificationsFragment fragment = new NotificationsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View notificationsView=inflater.inflate(R.layout.notifications_fragment, container, false);
        webNotificationsView = (WebView) notificationsView.findViewById(R.id.web1);
        webNotificationsView.loadUrl("http://www.firstcrush.co/notifications");

        // Enable Javascript
        WebSettings webSettings = webNotificationsView.getSettings();

        // Force links and redirects to open in the WebView instead of in a browser
        webNotificationsView.setWebViewClient(new WebViewClient());
        // Enable Javascript

        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSaveFormData(true);
        //webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.supportMultipleWindows();
        webSettings.setJavaScriptEnabled(true);
        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= 20) {
            String ua = "Chrome";
            webNotificationsView.getSettings().setUserAgentString(ua);
        }
        webSettings.setBuiltInZoomControls(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webNotificationsView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            webNotificationsView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        // Force links and redirects to open in the WebView instead of in a browser
        mWebChromeClient = new MyWebChromeClient();
        webNotificationsView.setWebChromeClient(mWebChromeClient);
        webNotificationsView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                if (progressBar != null)
                    progressBar.dismiss();
            }
        });
        progressBar = ProgressDialog.show(getActivity(), "", "Loading...");
        webNotificationsView.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && event.getAction() == MotionEvent.ACTION_UP) {
                    if(webNotificationsView.canGoBack()&& mCustomView == null) {
                        webNotificationsView.goBack();
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
                if ((keyCode == KeyEvent.KEYCODE_HOME)) {
                    return true;
                }
                if ((keyCode == KeyEvent.KEYCODE_APP_SWITCH)) {
                    return true;
                }
                return false;
            }
        });
        return notificationsView;
    }

    @Override
    public void onPause() {
        super.onPause();    //To change body of overridden methods use File | Settings | File Templates.
        webNotificationsView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();    //To change body of overridden methods use File | Settings | File Templates.
        webNotificationsView.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();    //To change body of overridden methods use File | Settings | File Templates
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        webNotificationsView = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        webNotificationsView.saveState(outState);
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