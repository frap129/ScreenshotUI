package org.carbonrom.screenshotui;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
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

    private View mScreenshotUILayout;
    private WindowManager wm;
    private Context mContext;

    private Drawable mScreenshot;

    public ScreenshotUILayout(Context context, Drawable screenshot) {
        super(context);
        mContext = context;
        mScreenshot = screenshot;
        mScreenshotUILayout = inflate(getContext(), R.layout.screenshot, null);

        // Add screenshot to view
        FrameLayout container = mScreenshotUILayout.findViewById(R.id.container);
        ImageView screenshotView = getScreenshotView();
        container.addView(screenshotView);

        // Populate variables for later user
        mCropButton = mScreenshotUILayout.findViewById(R.id.crop);
        mScrollButton = mScreenshotUILayout.findViewById(R.id.scroll);
        mEditButton = mScreenshotUILayout.findViewById(R.id.edit);
        mCancelButton = mScreenshotUILayout.findViewById(R.id.cancel);
        mShareButton = mScreenshotUILayout.findViewById(R.id.share);
        mSaveButton = mScreenshotUILayout.findViewById(R.id.save);

        wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);

        setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        addView(mScreenshotUILayout, getWindowLayoutParams());

        ScaleAnimation animation = new ScaleAnimation(1/0.86f, 1f, 1/0.86f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.0f);
        animation.setDuration(200);
        animation.setStartOffset(300);
        screenshotView.setAnimation(animation);
        animation.startNow();
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
        float scale = 0.8f;
        int viewHeight = (int) (displayMetrics.heightPixels * scale);
        int viewWidth = (int) (displayMetrics.widthPixels * scale);

        // Set view height and width
        imageView.setScaleX((float) (viewWidth) / imgWidth);
        imageView.setScaleY((float) (viewHeight) / imgHeight);

        return imageView;
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

    public void showUI(){
        wm.addView(this, getWindowLayoutParams());
    }

    public void dismissUI() {
        wm.removeView(this);
    }

    private WindowManager.LayoutParams getWindowLayoutParams() {
        WindowManager.LayoutParams param = new WindowManager.LayoutParams();

        param.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        param.format = PixelFormat.RGBA_8888;
        param.dimAmount= 0.8f;
        param.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED;
        param.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        param.flags = param.flags | WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
        param.x = 0;
        param.y = 0;
        param.width = WindowManager.LayoutParams.MATCH_PARENT;
        param.height = WindowManager.LayoutParams.MATCH_PARENT;

        return param;
    }

}