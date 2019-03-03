package org.carbonrom.screenshotui;

import android.app.Service;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.view.View;

public class ScreenshotUIService extends Service {

    private ScreenshotUILayout mScreenshotUI;
    @Override
    public void onCreate() {
        super.onCreate();
        Drawable screenshot = getDrawable(R.drawable.dummy); // Replace with screenshot capture
        mScreenshotUI = new ScreenshotUILayout(this, screenshot);
        mScreenshotUI.showUI();

        setOnClickReceivers();
    }

    private void setOnClickReceivers() {
        mScreenshotUI.mCropButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPartialScreenshot();
            }
        });

        mScreenshotUI.mScrollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScrollingScreenshot();
            }
        });

        mScreenshotUI.mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEditIntent();
            }
        });

        mScreenshotUI.mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteScreenshot();
                mScreenshotUI.dismissUI();
                stopSelf();
            }
        });

        mScreenshotUI.mShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendShareIntent();
            }
        });

        mScreenshotUI.mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveScreenshot();
                mScreenshotUI.dismissUI();
                stopSelf();
            }
        });
    }

    private void startPartialScreenshot() {
        // Requires platform APIs
    }

    private void startScrollingScreenshot() {
        // Requires platform APIs
    }

    private void sendEditIntent() {
        // Requires us to actually take a screenshot
    }

    private void sendShareIntent() {
        // Requires us to actually take a screenshot
    }

    private void deleteScreenshot() {
        // Requires us to actually take a screenshot
    }

    private void saveScreenshot() {
        // should be no-op, but makes better UX to oppose cancel button
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /* Requires platform APIs
    public Bitmap getSnapshot() {
        final ActivityManager am = (android.app.ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        int taskId = -1;
        java.util.List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list.size() != 0) {
            ActivityManager.RunningTaskInfo topRunningTask = list.get(0);
            taskId = topRunningTask.id;
        }
        try {
            ActivityManager.TaskSnapshot snapshot = ActivityManager.getService()
                    .getTaskSnapshot(taskId, true);
            if (snapshot != null)
                return Bitmap.createHardwareBitmap(snapshot.getSnapshot());
            else
                return null;
        } catch (RemoteException e) {
            // nah
        }
    }*/

}
