package org.carbonrom.screenshotui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


// REMOVE THIS CLASS LATER
// USED FOR DEBUGGING
public class ScreenshotUILauncher extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent svc = new Intent(this, ScreenshotUIService.class);
        startService(svc);
        finish();
    }
}
