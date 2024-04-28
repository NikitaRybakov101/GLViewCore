package com.example.glviewcore.glcore.model.object3d;

import android.opengl.GLES20;


import com.example.glviewcore.glcore.math.Vector3;
import com.example.glviewcore.glcore.model.utils.ColorARGB;
import com.example.glviewcore.glcore.model.utils.CurvedLine;

import java.util.ArrayList;

public class Line3D extends Object3D {

    private final ColorARGB color;
    private final int countTriangleRoundedLine = 20;
    private final ArrayList<Vector3> roundedRouteLine;


    public Line3D(ArrayList<Vector3> listPointRoute, float width, ColorARGB color) {
        CurvedLine curvedLine = new CurvedLine(0.1f, 0.001f, 1f);
        this.color = color;

        roundedRouteLine = (ArrayList<Vector3>) curvedLine.getRoundedVertices(listPointRoute);
        createRouteLine(width);
    }

    private void createRouteLine(float width) {
        Vector3 firstPoint = roundedRouteLine.get(0);
        Vector3 tempPointSR = null;
        Vector3 tempPointSL = null;

        float[] vertex = new float[2 * (roundedRouteLine.size() - 1) * 3 * 3 + 2 * countTriangleRoundedLine * 3 * 3];

        for (int i = 1; i < roundedRouteLine.size(); i++) {
            Vector3 secondPoint = roundedRouteLine.get(i);
            double dx = secondPoint.x - firstPoint.x;
            double dy = secondPoint.z - firstPoint.z;

            double radian = Math.atan2(dy,dx);
            double dxR = width/2f * Math.cos(radian + Math.PI/2f);
            double dyR = width/2f * Math.sin(radian + Math.PI/2f);

            double dxL = width/2f * Math.cos(radian - Math.PI/2f);
            double dyL = width/2f * Math.sin(radian - Math.PI/2f);

            Vector3 pointSR = new Vector3(secondPoint.x + dxR,secondPoint.y,secondPoint.z + dyR);
            Vector3 pointSL = new Vector3(secondPoint.x + dxL,secondPoint.y,secondPoint.z + dyL);
            Vector3 pointFR; Vector3 pointFL;

            if(i == 1) {
                pointFR = new Vector3(pointSR.x - dx, pointSR.y, pointSR.z - dy);
                pointFL = new Vector3(pointSL.x - dx, pointSL.y, pointSL.z - dy);
            } else {
                pointFR = tempPointSR;
                pointFL = tempPointSL;
            }

            tempPointSR = pointSR;
            tempPointSL = pointSL;
            firstPoint = secondPoint;

            ////////////////

            int index = (i - 1) * 6 * 3;

            vertex[index] = (float) pointSR.x;
            vertex[index + 1] = (float) pointSR.y;
            vertex[index + 2] = (float) pointSR.z;

            vertex[index + 3] = (float) pointSL.x;
            vertex[index + 4] = (float) pointSL.y;
            vertex[index + 5] = (float) pointSL.z;

            vertex[index + 6] = (float) pointFL.x;
            vertex[index + 7] = (float) pointFL.y;
            vertex[index + 8] = (float) pointFL.z;

            ///

            vertex[index + 9] = (float) pointSR.x;
            vertex[index + 10] = (float) pointSR.y;
            vertex[index + 11] = (float) pointSR.z;

            vertex[index + 12] = (float) pointFR.x;
            vertex[index + 13] = (float) pointFR.y;
            vertex[index + 14] = (float) pointFR.z;

            vertex[index + 15] = (float) pointFL.x;
            vertex[index + 16] = (float) pointFL.y;
            vertex[index + 17] = (float) pointFL.z;
        }

        addCircleStartEndLine(roundedRouteLine.get(0),
                width,vertex, 2 * (roundedRouteLine.size() - 1) * 3 * 3);

        addCircleStartEndLine(roundedRouteLine.get(roundedRouteLine.size() - 1),
                width,vertex, 2 * (roundedRouteLine.size() - 1) * 3 * 3 + countTriangleRoundedLine * 3 * 3);

        float[] colors = new float[2 * (roundedRouteLine.size() - 1) * 3 * 4 + 2 * countTriangleRoundedLine * 3 * 4];

        ColorARGB colorARGB = color;

        for (int i = 0; i < colors.length/4; i++) {
            int index = i * 4;
            if(colorARGB != null) {
                colors[index] = colorARGB.r;
                colors[index + 1] = colorARGB.g;
                colors[index + 2] = colorARGB.b;
                colors[index + 3] = colorARGB.a;
            }
        }
        setData(vertex,colors);
    }

    private void addCircleStartEndLine(Vector3 startEndPoint,float width,float[] vertex,int startIndex) {
        double dr = (Math.PI * 2.0) / countTriangleRoundedLine;
        double radian = 0;
        Vector3 tempPoint = new Vector3(startEndPoint.x + width/2f,startEndPoint.y,startEndPoint.z);

        for (int i = 0; i < countTriangleRoundedLine; i++) {
            radian += dr;
            double dx = width/2f * Math.cos(radian);
            double dy = - width/2f * Math.sin(radian);
            Vector3 secondPoint = new Vector3(startEndPoint.x + dx,startEndPoint.y,startEndPoint.z + dy);

            ////////////////

            int index = i * 9 + startIndex;

            vertex[index] = (float) startEndPoint.x;
            vertex[index + 1] = (float) startEndPoint.y;
            vertex[index + 2] = (float) startEndPoint.z;

            vertex[index + 3] = (float) tempPoint.x;
            vertex[index + 4] = (float) tempPoint.y;
            vertex[index + 5] = (float) tempPoint.z;

            vertex[index + 6] = (float) secondPoint.x;
            vertex[index + 7] = (float) secondPoint.y;
            vertex[index + 8] = (float) secondPoint.z;

            tempPoint = secondPoint;
        }
    }

    private void setData(float[] vertex, float[] colors) {
        setData(vertex, colors, null, GLES20.GL_TRIANGLES,null,null);
    }
}
