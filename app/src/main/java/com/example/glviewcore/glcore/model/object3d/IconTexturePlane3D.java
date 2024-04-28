package com.example.glviewcore.glcore.model.object3d;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.opengl.GLES20;

import com.example.glviewcore.glcore.model.utils.RectangleF;
import com.example.glviewcore.glcore.math.Vec3;

public class IconTexturePlane3D extends Object3D {
    private final float[] vertex;
    private final float[] textureCoordinates;


    public Vec3 projectionPosition;
    public RectangleF viewRect;

    public Bitmap bitmap;
    public boolean bitmapIsRecycled = false;


    public int floor;
    private final float w;
    private final float h;


    public boolean idDisableClustering = false;
    public PointF offset = new PointF(0,0);


    public IconTexturePlane3D(float x, float y, float z, float w, float h) {
        this.w = w;
        this.h = h;

        vertex = new float[6 * 3];
        textureCoordinates = new float[6 * 2];

        vertex[0] = x - w/2f;
        vertex[1] = y - h/2f;
        vertex[2] = z;

        vertex[3] = x + w/2f;
        vertex[4] = y - h/2f;
        vertex[5] = z;

        vertex[6] = x - w/2f;
        vertex[7] = y + h/2f;
        vertex[8] = z;

        //////////////////////////

        vertex[9] = x - w/2f;
        vertex[10] = y + h/2f;
        vertex[11] = z;

        vertex[12] = x + w/2f;
        vertex[13] = y + h/2f;
        vertex[14] = z;

        vertex[15] = x + w/2f;
        vertex[16] = y - h/2f;
        vertex[17] = z;

        ///////////////////////////

        textureCoordinates[0] = 1;
        textureCoordinates[1] = 1;

        textureCoordinates[2] = 0;
        textureCoordinates[3] = 1;

        textureCoordinates[4] = 1;
        textureCoordinates[5] = 0;

        //////////////////////////

        textureCoordinates[6] = 1;
        textureCoordinates[7] = 0;

        textureCoordinates[8] = 0;
        textureCoordinates[9] = 0;

        textureCoordinates[10] = 0;
        textureCoordinates[11] = 1;

        setData();
    }

    private void setData() {
        setData(vertex, null, textureCoordinates,GLES20.GL_TRIANGLES,null,null);
    }

    public void setProjectionPosition(Vec3 position) {
        projectionPosition = position;
    }

    public void createViewRect() {
        float scaleH = 0.8f;
        float scaleW = 0.6f;

        viewRect = new RectangleF(
                new PointF(translate.x + (w/2f) * scaleW,translate.y + (h/2f) * scaleH),
                new PointF(translate.x - (w/2f) * scaleW,translate.y - (h/2f) * scaleH)
        );
    }
}
