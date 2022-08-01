package com.pm.cameracore;

import android.util.Log;
import android.util.Size;
import android.view.Surface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author feng
 * @date 2022/7/4
 * @email 820275844@qq.com
 */
public class SizeUtils {
    /**
     * 给定相机支持的 {@code Choice} {@code Size}，选择最小的一个
     * 至少与相应的纹理视图大小一样大，并且最多与
     * 各自的最大尺寸，其纵横比与指定值匹配。 如果这样的大小
     * 不存在，选择最大的一个，最多与各自的最大尺寸一样大，
     * 并且其纵横比与指定值匹配。
     *
     * @param choices        The list of sizes that the camera supports for the intended output
     *                       class
     * @param surfaceSize    The surfaceSize of the texture view relative to sensor coordinate
     * @param maxPreviewSize The maximum maxPreviewSize that can be chosen
     * @param aspectRatio    The aspect ratio
     * @return The optimal {@code Size}, or an arbitrary one if none were big enough
     */
    public static Size chooseOptimalSize(Size[] choices, Size surfaceSize, Size maxPreviewSize, Size aspectRatio) {

        // 收集至少与预览 Surface 一样大的支持分辨率
        List<Size> bigEnough = new ArrayList<>();
        // 收集小于预览Surface的支持分辨率
        List<Size> notBigEnough = new ArrayList<>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices) {
            boolean isRange = option.getWidth() <= maxPreviewSize.getWidth() && option.getHeight() <= maxPreviewSize.getHeight();
            if (isRange && option.getHeight() == option.getWidth() * h / w) {
                if (option.getWidth() >= surfaceSize.getWidth() &&
                        option.getHeight() >= surfaceSize.getHeight()) {
                    bigEnough.add(option);
                } else {
                    notBigEnough.add(option);
                }
            }
        }

        // Pick the smallest of those big enough. If there is no one big enough, pick the
        // largest of those not big enough.
        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, new CompareSizesByArea());
        } else if (notBigEnough.size() > 0) {
            return Collections.max(notBigEnough, new CompareSizesByArea());
        } else {
            Log.e("error", "Couldn't find any suitable preview size");
            return choices[0];
        }
    }

    public static Size chooseThumbSize(Size[] sizes) {
        Size size = sizes[sizes.length - 1];
        for (int i = 0; i < sizes.length; i++) {
            Size s = sizes[i];
            int width = s.getWidth();
            int height = s.getHeight();
            if (height != 0 && width / height == 16 / 9) {
                size = s;
            }
        }
        return size;
    }

    /**
     * @param sizes            摄像头硬件支持的尺寸
     * @param customOutputSize 自定义尺寸
     * @param aspectRatio      纵横比尺寸
     * @param matchLargest     true 匹配最大尺寸,false 匹配自定义尺寸
     * @return
     */
    public static Size chooseOutputSize(Size[] sizes, Size customOutputSize, Size aspectRatio, boolean matchLargest) {
        List<Size> largests = new ArrayList<>(6);
        List<Size> leasts = new ArrayList<>(6);
        int h = aspectRatio.getHeight();
        int w = aspectRatio.getWidth();
        for (int i = 0; i < sizes.length; i++) {
            Size choices = sizes[i];
            if (choices.getWidth() >= customOutputSize.getWidth() && choices.getHeight() >= customOutputSize.getHeight()) {
                leasts.add(choices);
            }
            if (choices.getHeight() == choices.getWidth() * h / w) {
                largests.add(choices);
            }
        }

        if (!matchLargest) {
            return Collections.min(leasts, new CompareSizesByArea());
        }

        if (!largests.isEmpty()) {
            return Collections.max(largests, new CompareSizesByArea());
        }

        return customOutputSize;
    }

}
