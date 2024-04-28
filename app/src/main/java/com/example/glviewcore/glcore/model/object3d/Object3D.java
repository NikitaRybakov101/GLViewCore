package com.example.glviewcore.glcore.model.object3d;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.example.glviewcore.glcore.math.Vec3;

public class Object3D {
    public int drawingMode;

    ///

    public int textureTargetId = 0;
    public String textureKey = "";

    ///

    public FloatBuffer vertexBuffer;
    public FloatBuffer vertexBuffer2;
    public FloatBuffer vertexBuffer3;
    public FloatBuffer colorBuffer;
    public FloatBuffer textureCoordinatesBuffer;

    ///

    public int vertexBufferId = 0;
    public int vertexBufferId2 = 0;
    public int vertexBufferId3 = 0;
    public int colorBufferId = 0;
    public int textureCoordinatesBufferId = 0;

    public int lengthVertex;
    public int lengthVertex2;
    public int lengthVertex3;
    public int lengthColor;
    public int lengthTextureCoordinates;

    ///

    public Vec3 translate = new Vec3(0,0,0);
    public float rotY = 0f;
    public float scale = 1f;

    ///

    public boolean isVisible = true;
    public boolean isDisableDepthTest = false;

    public void setData(float[] vertex,float[] colors,float[] textureCoordinates,int drawingMode, float[] vertex2, float[] vertex3) {
        this.drawingMode = drawingMode;

        if(vertex != null) {
            lengthVertex = vertex.length;
        }

        if(vertex2 != null) {
            lengthVertex2 = vertex2.length;
        }

        if(vertex3 != null) {
            lengthVertex3 = vertex3.length;
        }

        if(colors != null) {
            lengthColor = colors.length;
        }

        if(textureCoordinates != null) {
            lengthTextureCoordinates = textureCoordinates.length;
        }

        if(vertex != null) {
            vertexBuffer = ByteBuffer.allocateDirect(vertex.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
            vertexBuffer.put(vertex);
        }

        if(vertex2 != null) {
            vertexBuffer2 = ByteBuffer.allocateDirect(vertex2.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
            vertexBuffer2.put(vertex2);
        }

        if(vertex3 != null) {
            vertexBuffer3 = ByteBuffer.allocateDirect(vertex3.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
            vertexBuffer3.put(vertex3);
        }

        if(colors != null) {
            colorBuffer = ByteBuffer.allocateDirect(colors.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
            colorBuffer.put(colors);
        }

        if(textureCoordinates != null) {
            textureCoordinatesBuffer = ByteBuffer.allocateDirect(textureCoordinates.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
            textureCoordinatesBuffer.put(textureCoordinates);
        }
    }

    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    ///

    public void setTranslate(Vec3 translate) {
        this.translate = translate;
    }

    public void setRotY(float rotY) {
        this.rotY = rotY;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    ///

    public void clear() {
        if(vertexBuffer != null) {
            vertexBuffer.clear();
            vertexBuffer = null;
        }

        if(colorBuffer != null) {
            colorBuffer.clear();
            colorBuffer = null;
        }

        if(textureCoordinatesBuffer != null) {
            textureCoordinatesBuffer.clear();
            textureCoordinatesBuffer = null;
        }
    }

}
