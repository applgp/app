package com.pm.cameracore;


import android.util.Size;

import java.util.Comparator;

/**
 * Compares two {@code Size}s based on their areas.
 *
 * @author feng
 * @date 2022/7/4
 * @email 820275844@qq.com
 */
public class CompareSizesByArea implements Comparator<Size> {

    @Override
    public int compare(Size lhs, Size rhs) {
        // We cast here to ensure the multiplications won't overflow
        long lhsArea = (long) lhs.getWidth() * lhs.getHeight();
        long rhsArea = (long) rhs.getWidth() * rhs.getHeight();
        return Long.signum(lhsArea - rhsArea);
    }

}
