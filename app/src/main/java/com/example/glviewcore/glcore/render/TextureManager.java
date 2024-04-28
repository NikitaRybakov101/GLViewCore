package com.example.glviewcore.glcore.render;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.glviewcore.glcore.utils.TextureUtils;

import java.util.ArrayList;
import java.util.HashMap;


public class TextureManager {
    private final HashMap<String,Integer> bitmapHashMap = new HashMap<>();
    private final RendererGLThread rendererGLThread;

    public TextureManager(RendererGLThread rendererGLThread) {
        this.rendererGLThread = rendererGLThread;
    }

    ///------Async------///
    private final ArrayList<Texture> listTextureForCreate = new ArrayList<>();
    int tempSizeBitmap = 0;
    public void addTextureAsync(Bitmap bitmap, String key) {
        listTextureForCreate.add(new Texture(bitmap, key));
    }
    public void createTexture() {
        int size = listTextureForCreate.size();
        if(size > 0) {
            for (int i = tempSizeBitmap; i < size; i++) {
                Texture texture = listTextureForCreate.get(i);

                int textureTargetId = TextureUtils.loadTexture(texture.bitmap);
                bitmapHashMap.put(texture.key,textureTargetId);
            }
            tempSizeBitmap = size;
        }
    }
    ////////////////////////

    public void addTexture(Bitmap bitmap, String key) {
        int textureTargetId = TextureUtils.loadTexture(bitmap);
        bitmapHashMap.put(key,textureTargetId);
    }

    public Integer getTextureTargetId(String key) {
        return bitmapHashMap.get(key);
    }

    public void clearTexturesBuffers() {
        for (String key : bitmapHashMap.keySet()) {
            Integer value = bitmapHashMap.get(key);
            rendererGLThread.deleteTexturesBuffer(value);
        }
    }

    public void clearTexturesBuffersOfKey(String key) {
        Integer value = bitmapHashMap.get(key);
        rendererGLThread.deleteTexturesBuffer(value);
    }

    public void loadTextureIdRes(Context context,int idRes, String key, float compress) {
        Bitmap bitmap = getBitmapOfIdRes(context,idRes,compress);
        addTexture(bitmap, key);
    }

    public Bitmap getBitmapOfIdRes(Context context,int idRes, float compress) {
        Bitmap bitmapSource = BitmapFactory.decodeResource(context.getResources(), idRes);
        Bitmap bitmapToScale = Bitmap.createBitmap(bitmapSource, 0, 0, bitmapSource.getWidth(), bitmapSource.getHeight());
        return Bitmap.createScaledBitmap(bitmapToScale, (int) (bitmapSource.getWidth() * compress), (int) (bitmapSource.getHeight() * compress), true);
    }

    private static class Texture {
        public Bitmap bitmap;
        public String key;

        public Texture(Bitmap bitmap, String key) {
            this.bitmap = bitmap;
            this.key = key;
        }
    }

}
