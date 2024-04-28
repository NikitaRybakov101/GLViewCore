package com.example.glviewcore.glcore.utils;

import com.example.glviewcore.glcore.math.Matrix;
import com.example.glviewcore.glcore.math.Vec3;

import javax.microedition.khronos.opengles.GL10;

public class GLU {
    private static final double[] sScratch = new double[32];

    public static int gluProject(double objX, double objY, double objZ,
                                 double[] model, int modelOffset, double[] project, int projectOffset,
                                 int[] view, int viewOffset, double[] win, int winOffset) {
        double[] scratch = sScratch;
        synchronized(scratch)
        {
            final int M_OFFSET = 0;
            final int V_OFFSET = 16;
            final int V2_OFFSET = 20;

            Matrix.multiplyMM(scratch, M_OFFSET, project, projectOffset, model, modelOffset);

            scratch[V_OFFSET] = objX;
            scratch[V_OFFSET + 1] = objY;
            scratch[V_OFFSET + 2] = objZ;
            scratch[V_OFFSET + 3] = 1.0;

            Matrix.multiplyMV(scratch, V2_OFFSET, scratch, M_OFFSET, scratch, V_OFFSET);
            double w = scratch[V2_OFFSET + 3];

            if (w == 0.0) {
                return GL10.GL_FALSE;
            }
            double rw = 1.0 / w;

            win[winOffset] = view[viewOffset] + view[viewOffset + 2]
                    * (scratch[V2_OFFSET] * rw + 1.0)
                    * 0.5;

            win[winOffset + 1] = view[viewOffset + 1] + view[viewOffset + 3]
                    * (scratch[V2_OFFSET + 1] * rw + 1.0)
                    * 0.5;

            win[winOffset + 2] = (scratch[V2_OFFSET + 2] * rw + 1.0) * 0.5;
        }

        return GL10.GL_TRUE;
    }

    public static Vec3 projectPoint(Vec3 point3d, int[] viewPort, float[] viewMatrix, float[] projectMatrix) {
        double[] resultArray = new double[3];

        gluProject(
                point3d.x, point3d.y, point3d.z,
                convertFloatsToDoubles(viewMatrix), 0,
                convertFloatsToDoubles(projectMatrix), 0,
                viewPort, 0,
                resultArray, 0
        );

        return new Vec3((float) resultArray[0], (float) (viewPort[3] - resultArray[1]), (float) resultArray[2]);
    }

    public static double[] convertFloatsToDoubles(float[] input) {
        if (input == null) {
            return null;
        }
        double[] output = new double[input.length];
        for (int i = 0; i < input.length; i++) {
            output[i] = input[i];
        }
        return output;
    }
}
