package org.carbonrom.screenshotui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;


// REMOVE THIS CLASS LATER
// USED FOR DEBUGGING
public class ScreenshotUILauncher extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 0);
        }
        Intent svc = new Intent(this, ScreenshotUIService.class);
        startService(svc);
        finish();
    }
}
