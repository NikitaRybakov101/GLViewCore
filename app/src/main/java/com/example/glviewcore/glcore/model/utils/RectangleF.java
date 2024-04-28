package com.example.glviewcore.glcore.model.utils;


import android.graphics.PointF;

public class RectangleF {
    private final PointF bottomLeft;
    private final PointF topRight;

    public RectangleF(PointF topRight, PointF bottomLeft) {
        this.topRight = topRight;
        this.bottomLeft = bottomLeft;
    }

    public boolean isOverlapping(RectangleF other) {
        if (this.topRight.y < other.bottomLeft.y || this.bottomLeft.y > other.topRight.y) {
            return false;
        }
        return !(this.topRight.x < other.bottomLeft.x) && !(this.bottomLeft.x > other.topRight.x);
    }
}

