package com.pm.cameracore;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.util.Size;

/**
 * @author feng
 * @date 2022/7/4
 * @email 820275844@qq.com
 */
public interface DelegateCallback {
    void onChangeViewSize(Size size);
    void onTransformView(Matrix matrix);
    SurfaceTexture getSurfaceTexture();
    void onCaptureResult(Bitmap bitmap);
    void onRecordResult(Bitmap coverBitmap,String videoAbsolutePath);
}
