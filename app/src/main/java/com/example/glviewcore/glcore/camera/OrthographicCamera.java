package com.example.glviewcore.glcore.camera;

import android.opengl.Matrix;

import com.example.glviewcore.glcore.math.Vec3;


public class OrthographicCamera {
    public float ratio = 1f;

    public float left = -1f;
    public float right = 1f;
    public float bottom = -1f;
    public float top = 1f;

    public float near = 1f;
    public float far = 3f;

    private Vec3 position = new Vec3(0,0,100);
    private Vec3 lookAt = new Vec3(0,0, 103);

    private float[] mProjectionMatrix = new float[16];
    private float[] mViewMatrix = new float[16];

    public void createProjectionMatrix(int width ,int height, float nearPlane, float farPlane) {
        near = nearPlane;
        far = farPlane;

        if (width > height)
        {
            ratio = width / (float) height;
            left *= ratio;
            right *= ratio;
        } else {
            ratio = height / (float)  width;
            bottom *= ratio;
            top *= ratio;
        }
        Matrix.orthoM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }

    public void updateViewMatrix() {
        float eyeX = position.x;
        float eyeY = position.y;
        float eyeZ = position.z;

        float centerX = lookAt.x;
        float centerY = lookAt.y;
        float centerZ = lookAt.z;

        float upX = 0f;
        float upY = 1f;
        float upZ = 0f;

        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
    }

    public void setPosition(Vec3 position) {
        this.position = position;
        updateViewMatrix();
    }

    public void setLookAt(Vec3 lookAt) {
        this.lookAt = lookAt;
        updateViewMatrix();
    }

    public float[] getViewMatrix() {
        return mViewMatrix;
    }

    public float[] getProjectionMatrix() {
        return mProjectionMatrix;
    }

    public void setViewMatrix(float[] viewMatrix) {
        mViewMatrix = viewMatrix;
    }

    public void setProjectionMatrix(float[] projectionMatrix) {
        mProjectionMatrix = projectionMatrix;
    }
}
