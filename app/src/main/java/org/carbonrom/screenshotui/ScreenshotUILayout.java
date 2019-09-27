package org.carbonrom.screenshotui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class ScreenshotUILayout extends LinearLayout {

    public ImageButton mCropButton;
    public ImageButton mScrollButton;
    public ImageButton mEditButton;
    public ImageButton mCancelButton;
    public ImageButton mShareButton;
    public ImageButton mSaveButton;

    public LinearLayout mTopMenu;
    public LinearLayout mBottomMenu;

    private View mScreenshotUILayout;
    private WindowManager wm;
    private WindowManager.LayoutParams lp;
    private Context mContext;

    private Drawable mScreenshot;
    private int mViewHeight;
    private int mViewWidth;

    public ScreenshotUILayout(Context context, Drawable screenshot) {
        super(context);
        mContext = context;
        mScreenshot = screenshot;
        mScreenshotUILayout = inflate(getContext(), R.layout.screenshot, null);

        // Add screenshot to view
        FrameLayout container = mScreenshotUILayout.findViewById(R.id.container);
        ImageView screenshotView = getScreenshotView();
        handleImageClick(screenshotView);
        container.addView(screenshotView);

        // Populate variables for later use
        mCropButton = mScreenshotUILayout.findViewById(R.id.crop);
        mScrollButton = mScreenshotUILayout.findViewById(R.id.scroll);
        mEditButton = mScreenshotUILayout.findViewById(R.id.edit);
        mCancelButton = mScreenshotUILayout.findViewById(R.id.cancel);
        mShareButton = mScreenshotUILayout.findViewById(R.id.share);
        mSaveButton = mScreenshotUILayout.findViewById(R.id.save);
        mTopMenu = mScreenshotUILayout.findViewById(R.id.top_menu);
        mBottomMenu = mScreenshotUILayout.findViewById(R.id.bottom_menu);


        wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mScreenshotUILayout.setScaleX(0.4f);
        mScreenshotUILayout.setScaleY(0.4f);
        addView(mScreenshotUILayout, getWindowLayoutParams());

        ScaleAnimation screenshotAnimation = getAnimation();
        screenshotView.setAnimation(screenshotAnimation);
        screenshotAnimation.startNow();
    }

    public ImageView getScreenshotView() {
        ImageView imageView = new ImageView(mContext);

        // Set ImageView drawable to screenshot
        imageView.setImageDrawable(mScreenshot);

        // Get original height and width
        int imgWidth = mScreenshot.getIntrinsicWidth();
        int imgHeight = mScreenshot.getIntrinsicHeight();

        // Calculate view height and width
        Display display = ((WindowManager) this.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getRealMetrics(displayMetrics);
        final float startScale = 0.8f;
        mViewHeight = (int) (displayMetrics.heightPixels * startScale);
        mViewWidth = (int) (displayMetrics.widthPixels * startScale);

        // Set view height and width
        imageView.setScaleX((float) (mViewWidth) / imgWidth);
        imageView.setScaleY((float) (mViewHeight) / imgHeight);

        return imageView;
    }

    public ScaleAnimation getAnimation() {
        ScaleAnimation animation = new ScaleAnimation(1/0.75f, 1f, 1/0.75f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        animation.setDuration(200);
        animation.setStartOffset(200);
        return animation;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() != KeyEvent.ACTION_UP) {
            Intent stopIntent = new Intent(mContext, ScreenshotUIService.class);
            mContext.stopService(stopIntent);
            dismissUI();
        }
        return super.dispatchKeyEvent(event);
    }

    public void showUI() {
        lp = getWindowLayoutParams();
        wm.addView(this, lp);
    }

    public void dismissUI() {
        wm.removeView(this);
    }

    public void dimBackground() {
        lp.dimAmount= 0.8f;
        lp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        wm.updateViewLayout(this, lp);
    }

    public void setFullscreen() {
        setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        // Configure UI Flags
        lp.flags = lp.flags | WindowManager.LayoutParams.FLAG_FULLSCREEN;
        lp.flags = lp.flags | WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        lp.flags = lp.flags | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;

        // Use entire screen
        //lp.x = 0;
        //lp.y = 0;
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        // Center everything
        lp.gravity = Gravity.CENTER;
        wm.updateViewLayout(this, lp);
    }

    public void showMenus() {
        mTopMenu.setVisibility(LinearLayout.VISIBLE);
        mBottomMenu.setVisibility(LinearLayout.VISIBLE);
    }

    public void rescaleImage(ImageView iv) {
        // Get original height and width
        int imgWidth = mScreenshot.getIntrinsicWidth();
        int imgHeight = mScreenshot.getIntrinsicHeight();

        // Calculate view height and width
        Display display = ((WindowManager) this.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getRealMetrics(displayMetrics);
        final float newScale = 0.8f;
        mViewHeight = (int) (displayMetrics.heightPixels * newScale);
        mViewWidth = (int) (displayMetrics.widthPixels * newScale);

        // Set view height and width
        iv.setScaleX((float) (mViewWidth) / imgWidth);
        iv.setScaleY((float) (mViewHeight) / imgHeight);

    }

    private void handleImageClick(ImageView iv) {
        iv.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //rescaleImage((ImageView) v);
                mScreenshotUILayout.setScaleX(1);
                mScreenshotUILayout.setScaleY(1);
                setFullscreen();
                dimBackground();
                showMenus();
                return true;
            }
        });
    }

    private WindowManager.LayoutParams getWindowLayoutParams() {
        WindowManager.LayoutParams param = new WindowManager.LayoutParams();

        // Change to TYPE_SYSTEM_OVERLAY later
        param.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;

        param.format = PixelFormat.TRANSLUCENT;
        param.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED;

        // Initialize screenshot in bottom right
        param.gravity = Gravity.END | Gravity.BOTTOM;

        // Wrap screenshot
        param.width = mViewWidth;
        param.height = mViewHeight;

        return param;
    }

}