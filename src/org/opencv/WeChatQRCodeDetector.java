package org.opencv;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.wechat_qrcode.WeChatQRCode;

public final class WeChatQRCodeDetector {
    private static final String TAG = "WeChatQRCodeDetector";
    private static WeChatQRCode sWeChatQRCode;

    private WeChatQRCodeDetector() {
        throw new AssertionError();
    }

    public static void init(Context context) {
        initWeChatQRCode(context.getApplicationContext());
    }

    private static void initWeChatQRCode(Context context) {
        try {
            String modelDir = "models";
            String[] models = context.getAssets().list(modelDir);
            String saveDirPath = getExternalFilesDir(context, modelDir);
            File saveDir = new File(saveDirPath);
            if (!saveDir.exists()) {
                saveDir.mkdirs();
            }

            File detect = new File(saveDirPath, "detect.prototxt");
            File detectModel = new File(saveDirPath, "detect.caffemodel");
            File resolution = new File(saveDirPath, "sr.prototxt");
            File resolutionModel = new File(saveDirPath, "sr.caffemodel");
            if (!detect.exists() || !detectModel.exists() || !resolution.exists() || !resolutionModel.exists()) {
                String[] var9 = models;
                int var10 = models.length;

                for(int var11 = 0; var11 < var10; ++var11) {
                    String model = var9[var11];
                    Log.d(TAG, "model: " + model);
                    InputStream inputStream = context.getAssets().open(modelDir + "/" + model);
                    File saveFile = new File(saveDir, model);
                    FileOutputStream outputStream = new FileOutputStream(saveFile);
                    byte[] buffer = new byte[1024];

                    int len;
                    while((len = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, len);
                    }

                    outputStream.flush();
                    inputStream.close();
                    outputStream.close();
                    Log.d(TAG, "file:" + saveFile.getAbsolutePath());
                }
            }

            Log.d(TAG, "Initial WeChatQRCode");
            sWeChatQRCode = new WeChatQRCode(detect.getAbsolutePath(), detectModel.getAbsolutePath(), resolution.getAbsolutePath(), resolutionModel.getAbsolutePath());
        } catch (Throwable exception) {
            exception.printStackTrace();
        }

    }

    private static String getExternalFilesDir(Context context, String path) {
        File[] files = context.getExternalFilesDirs(path);
        return files != null && files.length > 0 ? files[0].getAbsolutePath() : context.getExternalFilesDir(path).getAbsolutePath();
    }

    public static List<String> detectAndDecode(Bitmap bitmap) {
        try {
            Mat mat = new Mat();
            Utils.bitmapToMat(bitmap, mat);
            if (sWeChatQRCode == null) {
                return new ArrayList<>();
            }
            return sWeChatQRCode.detectAndDecode(mat);
        } catch (Throwable e) {
            return new ArrayList<>();
        }
    }

    private static List<String> detectAndDecode(Mat img) {
        return sWeChatQRCode.detectAndDecode(img);
    }

    private static List<String> detectAndDecode(Mat img, List<Mat> points) {
        return sWeChatQRCode.detectAndDecode(img, points);
    }
}

