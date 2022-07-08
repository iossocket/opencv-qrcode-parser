package org.opencv;

import android.content.Context;
import android.util.Log;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

public final class OpenCV {
    private static final String TAG = "OpenCV";

    private OpenCV() {
        throw new AssertionError();
    }

    public static void initAsync(Context context) {
        LoaderCallbackInterface loaderCallback = new BaseLoaderCallback(context.getApplicationContext()) {
            public void onManagerConnected(int status) {
                super.onManagerConnected(status);
                Log.i("OpenCV", "onManagerConnected:" + status);
                if (status == 0) {
                    Log.i("OpenCV", "OpenCV loaded successfully");
                }

            }
        };
        try {
            if (!OpenCVLoader.initDebug()) {
                Log.d("OpenCV", "Internal OpenCV library not found. Using OpenCV Manager for initialization");
                OpenCVLoader.initAsync("4.5.2", context.getApplicationContext(), loaderCallback);
            } else {
                Log.d("OpenCV", "OpenCV library found inside package. Using it!");
                loaderCallback.onManagerConnected(0);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }
}

