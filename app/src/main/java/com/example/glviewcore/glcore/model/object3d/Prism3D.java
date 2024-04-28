package com.example.glviewcore.glcore.model.object3d;

import static java.lang.Math.PI;
import static java.lang.Math.abs;

import android.opengl.GLES20;

import java.util.List;

import com.example.glviewcore.glcore.math.Earcut;
import com.example.glviewcore.glcore.model.utils.ColorARGB;


public class Prism3D extends Object3D {
    private ColorARGB colorARGB;
    private boolean enableTransparency;
    private float[] colors;
    private float[] vertex;

    public void drawObject3DPrism(ColorARGB colorARGB, double[] points, float base, float height, boolean enableTransparency) {
        this.colorARGB = colorARGB;
        this.enableTransparency = enableTransparency;

        int pathPointsSize = points.length / 2;

        List<Integer> numVerticesToTriangle = Earcut.earcut(points, null, 2);

        vertex = new float[numVerticesToTriangle.size() * 3 + pathPointsSize * 2 * 3 * 3];
        colors = new float[numVerticesToTriangle.size() * 4 + pathPointsSize * 2 * 3 * 4];


        for (int i = 0; i < pathPointsSize; i++) {
            int indexPoints = i * 2;

            float point1X = (float) points[indexPoints];
            float point1Y = (float) points[indexPoints + 1];

            float point2X;
            float point2Y;

            if (i + 1 < pathPointsSize) {
                point2X = (float) points[indexPoints + 2];
                point2Y = (float) points[indexPoints + 3];
            } else {
                point2X = (float) points[0];
                point2Y = (float) points[1];
            }

            int index = i * 6 * 3;
            int colorIndex = i * 6;

            vertex[index] = point1X;
            vertex[index + 1] = base;
            vertex[index + 2] = point1Y;
            createWallsColors(colorIndex, point1X,point1Y, point2X,point2Y);

            vertex[index + 3] = point2X;
            vertex[index + 4] = base;
            vertex[index + 5] = point2Y;
            createWallsColors(colorIndex + 1, point1X,point1Y, point2X,point2Y);

            vertex[index + 6] = point1X;
            vertex[index + 7] = base + height;
            vertex[index + 8] = point1Y;
            createWallsColors(colorIndex + 2, point1X,point1Y, point2X,point2Y);

            vertex[index + 9] = point1X;
            vertex[index + 10] = base + height;
            vertex[index + 11] = point1Y;
            createWallsColors(colorIndex + 3, point1X,point1Y, point2X,point2Y);

            vertex[index + 12] = point2X;
            vertex[index + 13] = base + height;
            vertex[index + 14] = point2Y;
            createWallsColors(colorIndex + 4, point1X,point1Y, point2X,point2Y);

            vertex[index + 15] = point2X;
            vertex[index + 16] = base;
            vertex[index + 17] = point2Y;
            createWallsColors(colorIndex + 5, point1X,point1Y, point2X,point2Y);
        }

        for (int i = 0; i < numVerticesToTriangle.size(); i++) {
            int num = numVerticesToTriangle.get(i);

            int index = i * 3 + pathPointsSize * 6 * 3;
            int number = num * 2;

            vertex[index] = (float) points[number];
            vertex[index + 1] = base + height;
            vertex[index + 2] = (float) points[number + 1];

            createBaseColors(i + pathPointsSize * 6, true);
        }

        setData();
    }

    private void setData() {
        setData(vertex, colors,null,GLES20.GL_TRIANGLES,null,null);
    }

    private void createBaseColors(int i, boolean isUp) {
        int index = i * 4;

        float r1, g1, b1;

        float alpha = colorARGB.a;

        if (isUp) {
            r1 = colorARGB.r + 0.1f;
            g1 = colorARGB.g + 0.1f;
            b1 = colorARGB.b + 0.1f;
        } else {
            r1 = colorARGB.r - 0.3f;
            g1 = colorARGB.g - 0.3f;
            b1 = colorARGB.b - 0.3f;
        }

        colors[index] = r1;
        colors[index + 1] = g1;
        colors[index + 2] = b1;
        colors[index + 3] = alpha;
    }

    private void createWallsColors(int i, float point1X, float point1Y,float point2X, float point2Y) {
        int index = i * 4;

        float deltaX = abs(point2X - point1X);
        float deltaY = abs(point2Y - point1Y);

        float lightK = 0.26f;
        float min = -0.26f;

        float lightPower;

        if (deltaX != 0) {
            float tg = deltaY / deltaX;
            float radian = (float) Math.atan(tg);

            lightPower = (float) ((radian / (PI / 2f)) * lightK + min);
        } else {
            lightPower = lightK * 1 + min;
        }

        colors[index] = colorARGB.r + lightPower;
        colors[index + 1] = colorARGB.g + lightPower;
        colors[index + 2] = colorARGB.b + lightPower;
        colors[index + 3] = colorARGB.a;
    }
}
