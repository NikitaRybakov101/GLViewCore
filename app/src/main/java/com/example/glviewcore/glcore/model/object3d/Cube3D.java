package com.example.glviewcore.glcore.model.object3d;

import android.graphics.PointF;

import com.example.glviewcore.glcore.model.utils.ColorARGB;

import java.util.ArrayList;

public class Cube3D extends Prism3D {
    private final ArrayList<PointF> pointsPolygonArrow = new ArrayList<>();

    public Cube3D(ColorARGB colorARGB, float size) {
        initModelArrow(size);
        float base = -size / 2f;
        float height = (size / 2f) * 2f;

        drawObject3DPrism(colorARGB,getDoubleNotScale(pointsPolygonArrow) , base, height, true);
    }

    private void initModelArrow(float size) {
        float edge = size / 2f;

        pointsPolygonArrow.add(new PointF(-edge,-edge));
        pointsPolygonArrow.add(new PointF(edge,-edge));
        pointsPolygonArrow.add(new PointF(edge,edge));
        pointsPolygonArrow.add(new PointF(-edge,edge));

        pointsPolygonArrow.add(new PointF(-edge,-edge));
    }

    protected double[] getDoubleNotScale(ArrayList<PointF> pointsList)
    {
        pointsList.remove(pointsList.size() - 1);

        double[] points = new double[pointsList.size() * 2];
        for (int i = 0; i < pointsList.size(); i++)
        {
            PointF pointF = pointsList.get(i);

            int index = i * 2;

            points[index] = pointF.x;
            points[index + 1] = pointF.y;
        }
        return points;
    }
}
