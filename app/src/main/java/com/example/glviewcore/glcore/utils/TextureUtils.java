package com.example.glviewcore.glcore.utils;

import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glDeleteTextures;
import static android.opengl.GLES20.glGenTextures;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

public class TextureUtils {
    public static int loadTexture(Bitmap bitmap) {

        final int[] textureIds = new int[1];
        glGenTextures(1, textureIds, 0);

        if (textureIds[0] == 0) {
            return 0;
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        if (bitmap == null) {
            glDeleteTextures(1, textureIds, 0);
            return 0;
        }

        glBindTexture(GL_TEXTURE_2D, textureIds[0]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        glBindTexture(GL_TEXTURE_2D, 0);

        return textureIds[0];
    }
}
