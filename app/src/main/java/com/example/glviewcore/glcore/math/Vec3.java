package com.example.glviewcore.glcore.math;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Vec3 {
    public float x;
    public float y;
    public float z;

    public Vec3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void scaleVec(float scale) {
        this.x *= scale;
        this.y *= scale;
        this.z *= scale;
    }

    public void sumVector(Vec3 translate) {
        this.x += translate.x;
        this.y += translate.y;
        this.z += translate.z;
    }

    public void rotateAxisY(float rotY) {
        double radian = degreeToRadian(rotY);
        float rx = (float) (x * cos(radian) - z * sin(radian));
        float rz = (float) (z * cos(radian) + x * sin(radian));

        x = rx;
        z = rz;
    }


    public void rotateAxisZ(float rotZ) {
        double radian = degreeToRadian(rotZ);
        float rx = (float) (x * cos(radian) - y * sin(radian));
        float ry = (float) (y * cos(radian) + x * sin(radian));

        x = rx;
    //    y = ry;
    }


    public void rotateAxisX(float rotX) {
        double radian = degreeToRadian(rotX);
        float rz = (float) (z * cos(radian) - y * sin(radian));
        float ry = (float) (y * cos(radian) + z * sin(radian));

        z = rz;
      //  y = ry;
    }

    private double degreeToRadian(double degree) {
        return (degree * 2.0 * PI) / (360.0);
    }

    public Vec3 copyVec() {
        return new Vec3(x,y,z);
    }
}
