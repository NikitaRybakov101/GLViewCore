package com.example.glviewcore.glcore.camera;

import android.opengl.Matrix;

import com.example.glviewcore.glcore.math.Matrix4;
import com.example.glviewcore.glcore.math.Vec3;
import com.example.glviewcore.glcore.math.Vector3;


public class Camera {
    float ratio = 1f;

    ///

    float left = -1f;
    float right = 1f;
    float bottom = -1f;
    float top = 1f;

    float near = 0.5f;
    float far = 20f;

    ///

    private Vec3 position = new Vec3(0,0,1);
    private Vec3 lookAt = new Vec3(0,0,0);

    ///

    private float[] mProjectionMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private final Matrix4 viewMatrix = new Matrix4();

    public void createProjectionMatrix(int width ,int height, float nearPlane, float farPlane) {
        near = nearPlane;
        far = farPlane;

        float scale = 1/6f;

        if (width > height) {
            ratio = width / (float) height;
            left *= ratio;
            right *= ratio;
        } else {
            ratio = height / (float)  width;
            bottom *= ratio;
            top *= ratio;
        }
        Matrix.frustumM(mProjectionMatrix, 0, left * scale, right * scale, bottom * scale, top * scale, near, far);
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
    }

    public Vec3 getPositionFromViewMatrix() {
        viewMatrix.setAll(mViewMatrix).inverse();
        Vector3 pos = viewMatrix.getTranslation();
        return new Vec3((float) pos.x, (float) pos.y, (float) pos.z);
    }

    public void setLookAt(Vec3 lookAt) {
        this.lookAt = lookAt;
    }

    public Vec3 getPosition() {
        return position;
    }

    public Vec3 getLookAt() {
        return lookAt;
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
