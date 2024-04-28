package com.example.glviewcore.glcore.model.object3d;

import android.opengl.GLES20;

import com.example.glviewcore.R;
import com.example.glviewcore.glcore.model.utils.ColorARGB;

import java.util.Random;

public class Water3D extends Object3D {
    private final float[] vertex;
    private final float[] vertex2;
    private final float[] vertex3;
    private final float[] colors;

    private final Random random = new Random();
    private int segments = 10;
    public Water3D(float x, float y, float z, float w, float h) {
        vertex = new float[segments * segments * 3 * 3  * 2];
        vertex2 = new float[segments * segments * 3 * 3  * 2];
        vertex3 = new float[segments * segments * 3 * 3  * 2];

        colors = new float[segments * segments * 3 * 4  * 2];


        float a = w / segments;
        float startX = - w / 2f;
        float startZ = h / 2f;

        for (int j = 0; j < segments; j++) {
            for (int i = 0; i < segments; i++) {

                int index = i * 9 + j * segments * 9;

                float shiftX = i * a;
                float shiftZ = j * a;

                if((j + 1) % 2 == 0) {
                    shiftX += a/2f;
                }

                vertex[index + 0] = startX + shiftX;
                vertex[index + 1] = y;
                vertex[index + 2] = startZ - shiftZ;

                ///
                vertex2[index + 0] = startX + a + shiftX;
                vertex2[index + 1] = y;
                vertex2[index + 2] = startZ - shiftZ;

                vertex3[index + 0] = startX + a / 2f + shiftX;
                vertex3[index + 1] = y;
                vertex3[index + 2] = startZ - a - shiftZ;
                ///




                vertex[index + 3] = startX + a + shiftX;
                vertex[index + 4] = y;
                vertex[index + 5] = startZ - shiftZ;

                ///
                vertex3[index + 3] = startX + shiftX;
                vertex3[index + 4] = y;
                vertex3[index + 5] = startZ - shiftZ;

                vertex2[index + 3] = startX + a / 2f + shiftX;
                vertex2[index + 4] = y;
                vertex2[index + 5] = startZ - a - shiftZ;
                ///





                vertex[index + 6] = startX + a / 2f + shiftX;
                vertex[index + 7] = y;
                vertex[index + 8] = startZ - a - shiftZ;

                ///
                vertex2[index + 6] = startX + shiftX;
                vertex2[index + 7] = y;
                vertex2[index + 8] = startZ - shiftZ;

                vertex3[index + 6] = startX + a + shiftX;
                vertex3[index + 7] = y;
                vertex3[index + 8] = startZ - shiftZ;
                ///
            }
        }


        for (int j = 0; j < segments; j++) {
            for (int i = 0; i < segments; i++) {

                int index = i * 9 + j * segments * 9  + segments * segments * 3 * 3 ;

                float shiftX = i * a;
                float shiftZ = j * a;

                if((j + 1) % 2 == 0) {
                    shiftX -= a/2f;
                }

                vertex[index + 0] = startX + a/2f + shiftX;
                vertex[index + 1] = y;
                vertex[index + 2] = startZ - a - shiftZ;

                ///
                vertex3[index + 0] = startX + a/2f + a + shiftX;
                vertex3[index + 1] = y;
                vertex3[index + 2] = startZ - a - shiftZ;

                vertex2[index + 0] = startX + a + shiftX;
                vertex2[index + 1] = y;
                vertex2[index + 2] = startZ - shiftZ;
                ///



                vertex[index + 3] = startX + a/2f + a + shiftX;
                vertex[index + 4] = y;
                vertex[index + 5] = startZ - a - shiftZ;

                ///
                vertex2[index + 3] = startX + a/2f + shiftX;
                vertex2[index + 4] = y;
                vertex2[index + 5] = startZ - a - shiftZ;

                vertex3[index + 3] = startX + a + shiftX;
                vertex3[index + 4] = y;
                vertex3[index + 5] = startZ - shiftZ;
                ///




                vertex[index + 6] = startX + a + shiftX;
                vertex[index + 7] = y;
                vertex[index + 8] = startZ - shiftZ;

                ///
                vertex3[index + 6] = startX + a/2f + shiftX;
                vertex3[index + 7] = y;
                vertex3[index + 8] = startZ - a - shiftZ;

                vertex2[index + 6] = startX + a/2f + a + shiftX;
                vertex2[index + 7] = y;
                vertex2[index + 8] = startZ - a - shiftZ;
                ///

            }
        }

        for (int i = 0; i < colors.length/(4 * 3); i++) {
            int index = i * 4 * 3;

            colors[index] = 0.5f;
            colors[index + 1] = 0.5f;
            colors[index + 2] = 1f;
            colors[index + 3] = 0.5f;

            colors[index + 4] = 0.5f;
            colors[index + 5] = 0.5f;
            colors[index + 6] = 1f;
            colors[index + 7] = 0.5f;

            colors[index + 8] = 0.5f;
            colors[index + 9] = 0.5f;
            colors[index + 10] = 1f;
            colors[index + 11] = 0.5f;
        }

        setData();
    }

    private void setData() {
        setData(vertex, colors, null, GLES20.GL_TRIANGLES,vertex2,vertex3);
    }
}
