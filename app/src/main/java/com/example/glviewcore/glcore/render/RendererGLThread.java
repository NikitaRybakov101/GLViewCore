package com.example.glviewcore.glcore.render;

import static android.opengl.EGL14.EGL_CONTEXT_CLIENT_VERSION;
import static android.opengl.EGL14.EGL_OPENGL_ES2_BIT;
import static android.opengl.GLES20.GL_ALPHA;
import static android.opengl.GLES20.GL_ARRAY_BUFFER;
import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.GL_ELEMENT_ARRAY_BUFFER;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_SRC_ALPHA;
import static android.opengl.GLES20.GL_STATIC_DRAW;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glBindBuffer;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glBufferData;
import static android.opengl.GLES20.glDisableVertexAttribArray;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGenBuffers;
import static android.opengl.GLES20.glVertexAttribPointer;
import static javax.microedition.khronos.egl.EGL10.EGL_ALPHA_SIZE;
import static javax.microedition.khronos.egl.EGL10.EGL_BLUE_SIZE;
import static javax.microedition.khronos.egl.EGL10.EGL_DEFAULT_DISPLAY;
import static javax.microedition.khronos.egl.EGL10.EGL_DEPTH_SIZE;
import static javax.microedition.khronos.egl.EGL10.EGL_GREEN_SIZE;
import static javax.microedition.khronos.egl.EGL10.EGL_NONE;
import static javax.microedition.khronos.egl.EGL10.EGL_NO_CONTEXT;
import static javax.microedition.khronos.egl.EGL10.EGL_RED_SIZE;
import static javax.microedition.khronos.egl.EGL10.EGL_RENDERABLE_TYPE;
import static javax.microedition.khronos.egl.EGL10.EGL_STENCIL_SIZE;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;
import android.view.TextureView;

import androidx.annotation.NonNull;

import com.example.glviewcore.R;
import com.example.glviewcore.glcore.camera.Camera;
import com.example.glviewcore.glcore.camera.OrthographicCamera;
import com.example.glviewcore.glcore.math.Vec3;
import com.example.glviewcore.glcore.model.object3d.Object3D;
import com.example.glviewcore.glcore.utils.ShaderUtils;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;


public class RendererGLThread extends Thread implements TextureView.SurfaceTextureListener {

    private final TextureView texture_view;
    private Scene scene;
    private final Render render;
    private final Camera camera;
    private final OrthographicCamera orthographicCamera;
    private final Context context;
    public TextureManager textureManager;

    //------//

    private int aColorLocation = 0;
    private int aPositionLocation = 0;
    private int uMatrixLocation = 0;


    private int aColorWaterLocation = 0;
    private int aPositionWaterLocation1 = 0;
    private int aPositionWaterLocation2 = 0;
    private int aPositionWaterLocation3 = 0;
    private int uMatrixWaterLocation = 0;
    private int uTime = 0;


    private int aPositionTextureLocation = 0;
    private int aTextureLocation = 0;
    private int uTextureUnitLocation = 0;
    private int uMatrixTextureLocation = 0;

    //------//

    private int programPolygonsId = 0;
    private int programTextureId = 0;
    private int programWaterId = 0;

    private final float[] mMatrix = new float[16];
    private final float[] mModelMatrixOrt = new float[16];
    private final float[] mModelMatrix = new float[16];


    public RendererGLThread(Context context, TextureView texture_view, Camera camera, OrthographicCamera orthographicCamera, Render render) {
        this.context = context;
        this.texture_view = texture_view;
        this.render = render;
        this.camera = camera;
        this.orthographicCamera = orthographicCamera;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void setTextureManager(TextureManager textureManager) {
        this.textureManager = textureManager;
    }

    ///-----------Init-----------///
    private void initGL() {
        GLES20.glClearColor(0.94f, 0.94f, 1f, 1f);

        GLES20.glEnable(GL_DEPTH_TEST);
        GLES20.glEnable(GL_BLEND);
        GLES20.glEnable(GL_ALPHA);

        GLES20.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glActiveTexture(GL_TEXTURE0);

        /////////////////////
        int vertexShaderId = ShaderUtils.createShader(context, GL_VERTEX_SHADER, R.raw.vertex_shader);
        int fragmentShaderId = ShaderUtils.createShader(context, GL_FRAGMENT_SHADER, R.raw.fragment_shader);
        programPolygonsId = ShaderUtils.createProgram(vertexShaderId, fragmentShaderId);

        int vertexShaderIdTexture = ShaderUtils.createShader(context, GL_VERTEX_SHADER, R.raw.vertex_shader_texture);
        int fragmentShaderIdTexture = ShaderUtils.createShader(context, GL_FRAGMENT_SHADER, R.raw.fragment_shader_texture);
        programTextureId = ShaderUtils.createProgram(vertexShaderIdTexture, fragmentShaderIdTexture);

        int vertexShaderIdWater = ShaderUtils.createShader(context, GL_VERTEX_SHADER, R.raw.vertex_water_shader);
        int fragmentShaderIdWater = ShaderUtils.createShader(context, GL_FRAGMENT_SHADER, R.raw.fragment_shader);
        programWaterId = ShaderUtils.createProgram(vertexShaderIdWater, fragmentShaderIdWater);

        aPositionLocation = GLES20.glGetAttribLocation(programPolygonsId, "a_Position");
        aColorLocation = GLES20.glGetAttribLocation(programPolygonsId, "a_Color");
        uMatrixLocation = GLES20.glGetUniformLocation(programPolygonsId, "u_Matrix");

        aPositionTextureLocation = GLES20.glGetAttribLocation(programTextureId, "a_Position");
        aTextureLocation = GLES20.glGetAttribLocation(programTextureId, "a_Texture");
        uTextureUnitLocation = GLES20.glGetUniformLocation(programTextureId, "u_TextureUnit");
        uMatrixTextureLocation = GLES20. glGetUniformLocation(programTextureId, "u_Matrix");

        aPositionWaterLocation1 = GLES20.glGetAttribLocation(programWaterId, "a_Position1");
        aPositionWaterLocation2 = GLES20.glGetAttribLocation(programWaterId, "a_Position2");
        aPositionWaterLocation3 = GLES20.glGetAttribLocation(programWaterId, "a_Position3");
        aColorWaterLocation = GLES20.glGetAttribLocation(programWaterId, "a_Color");
        uMatrixWaterLocation = GLES20.glGetUniformLocation(programWaterId, "u_Matrix");
        uTime = GLES20.glGetUniformLocation(programWaterId, "u_Time");

        //////////////////

        for (int i = 0; i < scene.polygonObject3Ds.size(); i++) {
            createPolygonsObject(scene.polygonObject3Ds.get(i));
        }
        for (int i = 0; i < scene.waterObject3Ds.size(); i++) {
            createWaterObject(scene.waterObject3Ds.get(i));
        }
        for (int i = 0; i < scene.textureObject3Ds.size(); i++) {
            createTextureObject(scene.textureObject3Ds.get(i));
        }
        for (int i = 0; i < scene.orthographicObject3Ds.size(); i++) {
            createOrthographicObject(scene.orthographicObject3Ds.get(i));
        }

        int width = texture_view.getWidth();
        int height = texture_view.getHeight();

        GLES20.glViewport(0, 0, width,height);

        camera.createProjectionMatrix(width, height,0.5f,20);
        orthographicCamera.createProjectionMatrix(width, height,1,3);
    }

    ///---------CreateBuffers---------///

    public void createPolygonsObject(Object3D object3D) {
        int[] mVBOIds = new int[2];
        glGenBuffers(2, mVBOIds, 0);
        int index = 0;

        object3D.vertexBuffer.position(0);
        glBindBuffer(GL_ARRAY_BUFFER, mVBOIds[index]);
        glBufferData(GL_ARRAY_BUFFER, 4 * object3D.lengthVertex, object3D.vertexBuffer, GL_STATIC_DRAW);

        object3D.colorBuffer.position(0);
        glBindBuffer(GL_ARRAY_BUFFER, mVBOIds[index + 1]);
        glBufferData(GL_ARRAY_BUFFER, 4 * object3D.lengthColor, object3D.colorBuffer, GL_STATIC_DRAW);

        object3D.vertexBufferId = mVBOIds[index];
        object3D.colorBufferId = mVBOIds[index + 1];

        object3D.clear();
    }

    public void createWaterObject(Object3D object3D) {
        int[] mVBOIds = new int[4];
        glGenBuffers(4, mVBOIds, 0);
        int index = 0;

        object3D.vertexBuffer.position(0);
        glBindBuffer(GL_ARRAY_BUFFER, mVBOIds[index]);
        glBufferData(GL_ARRAY_BUFFER, 4 * object3D.lengthVertex, object3D.vertexBuffer, GL_STATIC_DRAW);

        object3D.vertexBuffer2.position(0);
        glBindBuffer(GL_ARRAY_BUFFER, mVBOIds[index + 1]);
        glBufferData(GL_ARRAY_BUFFER, 4 * object3D.lengthVertex2, object3D.vertexBuffer2, GL_STATIC_DRAW);

        object3D.vertexBuffer3.position(0);
        glBindBuffer(GL_ARRAY_BUFFER, mVBOIds[index + 2]);
        glBufferData(GL_ARRAY_BUFFER, 4 * object3D.lengthVertex3, object3D.vertexBuffer3, GL_STATIC_DRAW);

        ///

        object3D.colorBuffer.position(0);
        glBindBuffer(GL_ARRAY_BUFFER, mVBOIds[index + 3]);
        glBufferData(GL_ARRAY_BUFFER, 4 * object3D.lengthColor, object3D.colorBuffer, GL_STATIC_DRAW);

        object3D.vertexBufferId = mVBOIds[index];
        object3D.vertexBufferId2 = mVBOIds[index + 1];
        object3D.vertexBufferId3 = mVBOIds[index + 2];
        object3D.colorBufferId = mVBOIds[index + 3];

        object3D.clear();
    }

    public void createTextureObject(Object3D object3D) {
        int[] mVBOIds = new int[2];
        glGenBuffers(2, mVBOIds, 0);
        int index = 0;

        object3D.vertexBuffer.position(0);
        glBindBuffer(GL_ARRAY_BUFFER, mVBOIds[index]);
        glBufferData(GL_ARRAY_BUFFER, 4 * object3D.lengthVertex, object3D.vertexBuffer, GL_STATIC_DRAW);

        object3D.textureCoordinatesBuffer.position(0);
        glBindBuffer(GL_ARRAY_BUFFER, mVBOIds[index + 1]);
        glBufferData(GL_ARRAY_BUFFER, 4 * object3D.lengthTextureCoordinates, object3D.textureCoordinatesBuffer, GL_STATIC_DRAW);

        Integer textureId = textureManager.getTextureTargetId(object3D.textureKey);
        if(textureId != null) {
            object3D.textureTargetId = textureId;
        }

        object3D.vertexBufferId = mVBOIds[index];
        object3D.textureCoordinatesBufferId = mVBOIds[index + 1];

        object3D.clear();
    }

    public void createOrthographicObject(Object3D object3D) {
        int[] mVBOIds = new int[2];
        glGenBuffers(2, mVBOIds, 0);
        int index = 0;

        object3D.vertexBuffer.position(0);
        glBindBuffer(GL_ARRAY_BUFFER, mVBOIds[index]);
        glBufferData(GL_ARRAY_BUFFER, 4 * object3D.lengthVertex, object3D.vertexBuffer, GL_STATIC_DRAW);

        object3D.textureCoordinatesBuffer.position(0);
        glBindBuffer(GL_ARRAY_BUFFER, mVBOIds[index + 1]);
        glBufferData(GL_ARRAY_BUFFER, 4 * object3D.lengthTextureCoordinates, object3D.textureCoordinatesBuffer, GL_STATIC_DRAW);

        Integer textureId = textureManager.getTextureTargetId(object3D.textureKey);
        if(textureId != null) {
            object3D.textureTargetId = textureId;
        }

        object3D.vertexBufferId = mVBOIds[index];
        object3D.textureCoordinatesBufferId = mVBOIds[index + 1];

        object3D.clear();
    }

    ///-----------ModelMatrix-----------///

    private void bindMatrixOrthographic() {
        Matrix.multiplyMM(mMatrix, 0, orthographicCamera.getViewMatrix(), 0, mModelMatrixOrt, 0);
        Matrix.multiplyMM(mMatrix, 0, orthographicCamera.getProjectionMatrix(), 0, mMatrix, 0);
        GLES20.glUniformMatrix4fv(uMatrixTextureLocation, 1, false, mMatrix, 0);
    }

    private void translateModelOrthographic(Vec3 translate) {
        Matrix.translateM(mModelMatrixOrt,0,-translate.x,translate.y,translate.z);
    }

    private void bindMatrix() {
        Matrix.multiplyMM(mMatrix, 0, camera.getViewMatrix(), 0, mModelMatrix, 0);
        Matrix.multiplyMM(mMatrix, 0, camera.getProjectionMatrix(), 0, mMatrix, 0);
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, mMatrix, 0);
    }

    private void bindMatrixTextureShader() {
        Matrix.multiplyMM(mMatrix, 0, camera.getViewMatrix(), 0, mModelMatrix, 0);
        Matrix.multiplyMM(mMatrix, 0, camera.getProjectionMatrix(), 0, mMatrix, 0);
        GLES20.glUniformMatrix4fv(uMatrixTextureLocation, 1, false, mMatrix, 0);
    }

    private void bindMatrixWaterShader() {
        Matrix.multiplyMM(mMatrix, 0, camera.getViewMatrix(), 0, mModelMatrix, 0);
        Matrix.multiplyMM(mMatrix, 0, camera.getProjectionMatrix(), 0, mMatrix, 0);
        GLES20.glUniformMatrix4fv(uMatrixWaterLocation, 1, false, mMatrix, 0);
    }


    private void scaleModel(float scale) {
        Matrix.scaleM(mModelMatrix,0,scale,scale,scale);
    }

    private void rotateModel(float rotY) {
        Matrix.rotateM(mModelMatrix,0,rotY,0,1,0);
    }

    private void translateModel(Vec3 translate) {
        Matrix.translateM(mModelMatrix,0,translate.x,translate.y,translate.z);
    }

    private void bindShaderTime() {
        float time = (SystemClock.uptimeMillis()) / 1000f;
        GLES20.glUniform1f(uTime, time);
    }

    ///-----------Render-----------///
    private EGL10 egl;
    private EGLDisplay eglDisplay;
    private EGLSurface eglSurface;

    private boolean renderIsStop = false;
    private boolean renderActive = true;

    private int limitFPS = 120;

    @Override
    public void run() {
        super.run();

        createSurface();

        render.initScene();
        initGL();
        render.onStartRender();

        long saveTime = 0;
        while(renderActive) {
            if(!renderIsStop) {
                if(saveTime == 0) {
                    saveTime = System.currentTimeMillis();
                }
                long currentTime = System.currentTimeMillis();
                long deltaTime = currentTime - saveTime;
                saveTime = currentTime;

                textureManager.createTexture();
                scene.createTexturePlane();
                scene.createPolygonsObject();
                scene.createTextureObject();

                renderScene(deltaTime,currentTime);

                render.onPostFrame();
                frameLimit(deltaTime);
            } else {
                saveTime = 0;
                frameLimit(0);
            }
        }

        render.onDestroyRender();
        glClear();
    }

    private void frameLimit(long deltaTime) {
        long time = (long) (1000f / limitFPS);

        if(deltaTime <= time) {
            try {
                Thread.sleep(time - deltaTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void setFrameLimit(int fps) {
        limitFPS = fps;
    }

    public void renderScene(long deltaTime, long currentTime) {
        GLES20.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        render.render(deltaTime);

        GLES20.glUseProgram(programPolygonsId);
        polygonRender();

        GLES20.glUseProgram(programTextureId);
        textureRender();

        GLES20.glUseProgram(programWaterId);
        waterRender();

        ///
        GLES20.glClear(GL_DEPTH_BUFFER_BIT);
        ///

        orthographicRender();

        egl.eglSwapBuffers(eglDisplay, eglSurface);
    }

    private void polygonRender() {
        int triangles;

        for (int i = 0; i < scene.polygonObject3Ds.size(); i++) {
            Object3D object3D = scene.polygonObject3Ds.get(i);
            triangles = object3D.lengthVertex / 9;

            glBindBuffer(GL_ARRAY_BUFFER, object3D.vertexBufferId);
            glVertexAttribPointer(aPositionLocation, 3, GL_FLOAT, false, 0, 0);
            glEnableVertexAttribArray(aPositionLocation);

            glBindBuffer(GL_ARRAY_BUFFER, object3D.colorBufferId);
            glVertexAttribPointer(aColorLocation, 4, GL_FLOAT, false, 0, 0);
            glEnableVertexAttribArray(aColorLocation);

            if (object3D.isVisible) {
                //////
                Matrix.setIdentityM(mModelMatrix,0);
                translateModel(object3D.translate);
                scaleModel(object3D.scale);
                rotateModel(object3D.rotY);
                bindMatrix();
                //////

                GLES20.glDrawArrays(GL_TRIANGLES, 0, triangles * 3);
            }

            glDisableVertexAttribArray(aPositionLocation);
            glDisableVertexAttribArray(aColorLocation);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        }
    }

    private void waterRender() {
        int triangles;

        for (int i = 0; i < scene.waterObject3Ds.size(); i++) {
            Object3D object3D = scene.waterObject3Ds.get(i);
            triangles = object3D.lengthVertex / 9;

            ///

            glBindBuffer(GL_ARRAY_BUFFER, object3D.vertexBufferId);
            glVertexAttribPointer(aPositionWaterLocation1, 3, GL_FLOAT, false, 0, 0);
            glEnableVertexAttribArray(aPositionWaterLocation1);

            glBindBuffer(GL_ARRAY_BUFFER, object3D.vertexBufferId2);
            glVertexAttribPointer(aPositionWaterLocation2, 3, GL_FLOAT, false,  0, 0);
            glEnableVertexAttribArray(aPositionWaterLocation2);

            glBindBuffer(GL_ARRAY_BUFFER, object3D.vertexBufferId3);
            glVertexAttribPointer(aPositionWaterLocation3, 3, GL_FLOAT, false, 0, 0);
            glEnableVertexAttribArray(aPositionWaterLocation3);

            ///

            glBindBuffer(GL_ARRAY_BUFFER, object3D.colorBufferId);
            glVertexAttribPointer(aColorWaterLocation, 4, GL_FLOAT, false, 0, 0);
            glEnableVertexAttribArray(aColorWaterLocation);

            if (object3D.isVisible) {
                //////
                Matrix.setIdentityM(mModelMatrix,0);
                translateModel(object3D.translate);
                scaleModel(object3D.scale);
                rotateModel(object3D.rotY);

                bindMatrixWaterShader();
                bindShaderTime();
                //////

                GLES20.glDrawArrays(GL_TRIANGLES, 0, triangles * 3);
            }

            glDisableVertexAttribArray(aPositionWaterLocation1);
            glDisableVertexAttribArray(aPositionWaterLocation2);
            glDisableVertexAttribArray(aPositionWaterLocation3);

            glDisableVertexAttribArray(aColorWaterLocation);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        }
    }

    private void textureRender() {
        int triangles;

        for (int i = 0; i < scene.textureObject3Ds.size(); i++) {
            Object3D object3D = scene.textureObject3Ds.get(i);
            triangles = object3D.lengthVertex / 9;

            glBindBuffer(GL_ARRAY_BUFFER, object3D.vertexBufferId);
            glVertexAttribPointer(aPositionTextureLocation, 3, GL_FLOAT, false, 0, 0);
            glEnableVertexAttribArray(aPositionTextureLocation);

            glBindBuffer(GL_ARRAY_BUFFER, object3D.textureCoordinatesBufferId);
            glVertexAttribPointer(aTextureLocation, 2, GL_FLOAT, false, 0, 0);
            glEnableVertexAttribArray(aTextureLocation);

            GLES20.glBindTexture(GL_TEXTURE_2D, object3D.textureTargetId);

            if (object3D.isVisible) {
                //////
                Matrix.setIdentityM(mModelMatrix,0);
                translateModel(object3D.translate);
                scaleModel(object3D.scale);
                rotateModel(object3D.rotY);
                bindMatrixTextureShader();
                //////

                if(object3D.isDisableDepthTest) { GLES20.glDisable(GL_DEPTH_TEST); }

                GLES20.glDrawArrays(GL_TRIANGLES, 0, triangles * 3);

                if(object3D.isDisableDepthTest) { GLES20.glEnable(GL_DEPTH_TEST);}
            }

            glDisableVertexAttribArray(aPositionTextureLocation);
            glDisableVertexAttribArray(aTextureLocation);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
            glBindTexture(GL_TEXTURE_2D, 0);
        }
    }

    private void orthographicRender() {
        int triangles;

        for (int i = 0; i < scene.orthographicObject3Ds.size(); i++) {
            Object3D object3D = scene.orthographicObject3Ds.get(i);
            triangles = object3D.lengthVertex / 9;

            glBindBuffer(GL_ARRAY_BUFFER, object3D.vertexBufferId);
            glVertexAttribPointer(aPositionTextureLocation, 3, GL_FLOAT, false, 0, 0);
            glEnableVertexAttribArray(aPositionTextureLocation);

            glBindBuffer(GL_ARRAY_BUFFER, object3D.textureCoordinatesBufferId);
            glVertexAttribPointer(aTextureLocation, 2, GL_FLOAT, false, 0, 0);
            glEnableVertexAttribArray(aTextureLocation);

            GLES20.glBindTexture(GL_TEXTURE_2D, object3D.textureTargetId);

            if (object3D.isVisible) {
                GLES20.glClear(GL_DEPTH_BUFFER_BIT);

                //////
                Matrix.setIdentityM(mModelMatrixOrt,0);
                translateModelOrthographic(object3D.translate);
                bindMatrixOrthographic();
                //////

                GLES20.glDrawArrays(GL_TRIANGLES, 0, triangles * 3);
            }

            glDisableVertexAttribArray(aPositionTextureLocation);
            glDisableVertexAttribArray(aTextureLocation);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
            glBindTexture(GL_TEXTURE_2D, 0);
        }
    }

    public void renderStart() {
        renderIsStop = false;
    }

    public void renderStop() {
        renderIsStop = true;
    }

    public void renderFinish() {
        renderActive = false;
    }

    ///---------Clear---------///

    private void glClear() {
        for (int i = 0; i < scene.polygonObject3Ds.size(); i++) {
            clearBuffersObject(scene.polygonObject3Ds.get(i));
        }
        for (int i = 0; i < scene.orthographicObject3Ds.size(); i++) {
            clearBuffersObject(scene.orthographicObject3Ds.get(i));
        }
        for (int i = 0; i < scene.textureObject3Ds.size(); i++) {
            clearBuffersObject(scene.textureObject3Ds.get(i));
        }
        for (int i = 0; i < scene.waterObject3Ds.size(); i++) {
            clearBuffersObject(scene.waterObject3Ds.get(i));
        }
        scene.clearScene();
        textureManager.clearTexturesBuffers();

        GLES20.glFlush();
        GLES20.glFinish();
    }

    public void clearBuffersObject(Object3D object3D) {
        final int[] ids = new int[1];

        if(object3D.vertexBufferId != 0) {
            ids[0] = object3D.vertexBufferId;
            GLES20.glDeleteBuffers(1,ids,0);
        }

        if(object3D.colorBufferId != 0) {
            ids[0] = object3D.colorBufferId;
            GLES20.glDeleteBuffers(1,ids,0);
        }

        if(object3D.textureCoordinatesBufferId != 0) {
            ids[0] = object3D.textureCoordinatesBufferId;
            GLES20.glDeleteBuffers(1,ids,0);
        }
    }

    public void deleteTexturesBuffer(int textureTargetId) {
        final int[] ids = new int[1];
        ids[0] = textureTargetId;
        GLES20.glDeleteTextures(1,ids,0);
    }


    ///-----------Surface-----------///
    private void createSurface() {
        egl = (EGL10) EGLContext.getEGL();
        eglDisplay = egl.eglGetDisplay(EGL_DEFAULT_DISPLAY);
        egl.eglInitialize(eglDisplay, new int[] {0,0});
        EGLConfig eglConfig = chooseEglConfig(egl, eglDisplay);
        EGLContext eglContext = egl.eglCreateContext(eglDisplay, eglConfig, EGL_NO_CONTEXT, new int[] {EGL_CONTEXT_CLIENT_VERSION, 2, EGL_NONE});
        eglSurface = egl.eglCreateWindowSurface(eglDisplay, eglConfig, texture_view.getSurfaceTexture(), null);
        egl.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext);
    }

    private final int[] config = new int[] {
            EGL_RENDERABLE_TYPE,
            EGL_OPENGL_ES2_BIT,
            EGL_RED_SIZE, 8,
            EGL_GREEN_SIZE, 8,
            EGL_BLUE_SIZE, 8,
            EGL_ALPHA_SIZE, 8,
            EGL_DEPTH_SIZE, 16,
            EGL_STENCIL_SIZE, 8,
            EGL_NONE
    };

    private EGLConfig chooseEglConfig(EGL10 egl, EGLDisplay eglDisplay) {
        int[] configsCount = new int[] {0};
        EGLConfig[] configs = new EGLConfig[1];
        egl.eglChooseConfig(eglDisplay, config, configs, 1, configsCount);
        return configs[0];
    }

    @Override
    public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surfaceTexture, int i, int i1) {
        start();
    }

    @Override
    public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surfaceTexture, int i, int i1) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surfaceTexture) {
        renderFinish();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surfaceTexture) {

    }
}
