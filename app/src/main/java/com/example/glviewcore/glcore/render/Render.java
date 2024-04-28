package com.example.glviewcore.glcore.render;

import android.content.Context;
import android.view.TextureView;

import com.example.glviewcore.glcore.camera.Camera;
import com.example.glviewcore.glcore.camera.OrthographicCamera;
import com.example.glviewcore.glcore.math.Vec3;

public abstract class Render {
    private final Scene scene;
    private final Camera camera;
    private final OrthographicCamera cameraOrthographic;
    private final RendererGLThread rendererGLThread;
    private final TextureManager textureManager;



    public Render(Context context, TextureView texture_view) {
        camera = new Camera();
        cameraOrthographic = new OrthographicCamera();

        Vec3 position = new Vec3(0,0,100);
        Vec3 lookAt = new Vec3(0,0, 103);

        cameraOrthographic.setPosition(position);
        cameraOrthographic.setLookAt(lookAt);

        rendererGLThread = new RendererGLThread(context, texture_view,camera,cameraOrthographic,this);

        scene = new Scene(rendererGLThread);
        rendererGLThread.setScene(scene);

        textureManager = new TextureManager(rendererGLThread);
        rendererGLThread.setTextureManager(textureManager);
    }



    protected abstract void initScene();
    protected abstract void render(long deltaTime);
    protected abstract void onStartRender();
    protected abstract void onPostFrame();
    protected abstract void onDestroyRender();



    public void renderStop()
    {
        rendererGLThread.renderStop();
    }

    public void renderStart()
    {
        rendererGLThread.renderStart();
    }

    public void renderFinish()
    {
        rendererGLThread.renderFinish();
    }

    public void setFrameLimit(int fps)
    {
        rendererGLThread.setFrameLimit(fps);
    }



    public Scene getScene()
    {
        return scene;
    }

    public Camera getCamera()
    {
        return camera;
    }

    public OrthographicCamera getOrthographicCamera()
    {
        return cameraOrthographic;
    }

    public RendererGLThread getRenderGLThread()
    {
        return rendererGLThread;
    }

    public TextureManager getTextureManager()
    {
        return textureManager;
    }
}
