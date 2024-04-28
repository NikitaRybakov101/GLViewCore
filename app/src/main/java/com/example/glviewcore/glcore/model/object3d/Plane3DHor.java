package com.example.glviewcore.glcore.model.object3d;

import android.opengl.GLES20;

public class Plane3DHor extends Object3D {
    private final float[] vertex;
    private final float[] textureCoordinates;

    public Plane3DHor(float x, float y, float z, float w, float h) {
        vertex = new float[6 * 3];
        textureCoordinates = new float[6 * 2];

        vertex[0] = x - w/2f;
        vertex[1] = y;
        vertex[2] = z - h/2f;

        vertex[3] = x + w/2f;
        vertex[4] = y;
        vertex[5] = z - h/2f;

        vertex[6] = x - w/2f;
        vertex[7] = y;
        vertex[8] = z + h/2f;

        //////////////////////////

        vertex[9] = x - w/2f;
        vertex[10] = y;
        vertex[11] = z + h/2f;

        vertex[12] = x + w/2f;
        vertex[13] = y;
        vertex[14] = z + h/2f;

        vertex[15] = x + w/2f;
        vertex[16] = y;
        vertex[17] = z - h/2f;

        ///////////////////////////

        textureCoordinates[0] = 0;
        textureCoordinates[1] = 0;

        textureCoordinates[2] = 1f;
        textureCoordinates[3] = 0;

        textureCoordinates[4] = 0;
        textureCoordinates[5] = 1;

        //////////////////////////

        textureCoordinates[6] = 0;
        textureCoordinates[7] = 1;

        textureCoordinates[8] = 1f;
        textureCoordinates[9] = 1;

        textureCoordinates[10] = 1f;
        textureCoordinates[11] = 0;

        setData();
    }

    private void setData() {
        setData(vertex, null, textureCoordinates, GLES20.GL_TRIANGLES,null,null);
    }
}
