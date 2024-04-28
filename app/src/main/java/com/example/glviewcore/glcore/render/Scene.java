package com.example.glviewcore.glcore.render;

import com.example.glviewcore.glcore.model.object3d.IconTexturePlane3D;
import com.example.glviewcore.glcore.model.object3d.Object3D;

import java.util.ArrayList;

public class Scene {
    public ArrayList<Object3D> polygonObject3Ds = new ArrayList<>();
    public ArrayList<Object3D> waterObject3Ds = new ArrayList<>();
    public ArrayList<Object3D> textureObject3Ds = new ArrayList<>();
    public ArrayList<IconTexturePlane3D> orthographicObject3Ds = new ArrayList<>();

    private final RendererGLThread rendererGLThread;

    public Scene(RendererGLThread rendererGLThread) {
        this.rendererGLThread = rendererGLThread;
    }

    public void addPolygonObject(Object3D object3D) { polygonObject3Ds.add(object3D); }
    public void addWaterObject(Object3D object3D) { waterObject3Ds.add(object3D); }
    public void addTextureObject(Object3D object3D) { textureObject3Ds.add(object3D); }
    public void addOrthographicObject(IconTexturePlane3D object3D) { orthographicObject3Ds.add(object3D);}



    ///---------AsyncCreate---------///
    private final ArrayList<IconTexturePlane3D> listTexturePlaneForCreate = new ArrayList<>();
    private final ArrayList<IconTexturePlane3D> listTexturePlaneForDelete = new ArrayList<>();
    int tempSizeTexturePlane = 0;
    int tempSizeTexturePlaneDelete = 0;
    public void addOrthographicObjectAsync(IconTexturePlane3D object3D) {
        listTexturePlaneForCreate.add(object3D);
    }

    public void removeOrthographicObjectAsync(IconTexturePlane3D object3D) {
        listTexturePlaneForDelete.add(object3D);
    }

    public void createTexturePlane() {
        int size = listTexturePlaneForCreate.size();
        if(size > 0) {
            for (int i = tempSizeTexturePlane; i < size; i++) {
                IconTexturePlane3D texturePlane3D = listTexturePlaneForCreate.get(i);

                ///
                rendererGLThread.textureManager.addTexture(texturePlane3D.bitmap,texturePlane3D.textureKey);

                if(texturePlane3D.bitmapIsRecycled) {
                    texturePlane3D.bitmap.recycle();
                    texturePlane3D.bitmap = null;
                }
                ///

                rendererGLThread.createOrthographicObject(texturePlane3D);
                orthographicObject3Ds.add(texturePlane3D);
            }
            tempSizeTexturePlane = size;
        }


        int sizeDelete = listTexturePlaneForDelete.size();
        if(sizeDelete > 0) {
            for (int i = tempSizeTexturePlaneDelete; i < sizeDelete; i++) {
                IconTexturePlane3D object3D = listTexturePlaneForDelete.get(i);

                rendererGLThread.clearBuffersObject(object3D);
                rendererGLThread.textureManager.clearTexturesBuffersOfKey(object3D.textureKey);

                orthographicObject3Ds.remove(object3D);
            }
            tempSizeTexturePlaneDelete = sizeDelete;
        }
    }

    ///

    private final ArrayList<Object3D> polygonsObjectForCreate = new ArrayList<>();
    private final ArrayList<Object3D> polygonsObjectForDelete = new ArrayList<>();
    int tempSizePolygons = 0;
    int tempSizePolygonsDelete = 0;
    public void addPolygonsObjectAsync(Object3D object3D) {
        polygonsObjectForCreate.add(object3D);
    }

    public void removePolygonsObjectAsync(Object3D object3D) {
        polygonsObjectForDelete.add(object3D);
    }

    public void createPolygonsObject() {
        int size = polygonsObjectForCreate.size();
        if(size > 0) {
            for (int i = tempSizePolygons; i < size; i++) {
                Object3D object3D = polygonsObjectForCreate.get(i);

                rendererGLThread.createPolygonsObject(object3D);
                polygonObject3Ds.add(object3D);
            }
            tempSizePolygons = size;
        }

        int sizeDelete = polygonsObjectForDelete.size();
        if(sizeDelete > 0) {
            for (int i = tempSizePolygonsDelete; i < sizeDelete; i++) {
                Object3D object3D = polygonsObjectForDelete.get(i);

                rendererGLThread.clearBuffersObject(object3D);
                polygonObject3Ds.remove(object3D);
            }
            tempSizePolygonsDelete = sizeDelete;
        }
    }

    ///

    private final ArrayList<Object3D> textureObjectForCreate = new ArrayList<>();
    private final ArrayList<Object3D> textureObjectForDelete = new ArrayList<>();
    int tempSizeTextures = 0;
    int tempSizeTexturesDelete = 0;
    public void addTextureObjectAsync(Object3D object3D) {
        textureObjectForCreate.add(object3D);
    }

    public void removeTextureObjectAsync(Object3D object3D) {
        textureObjectForDelete.add(object3D);
    }

    public void createTextureObject() {
        int size = textureObjectForCreate.size();
        if(size > 0) {
            for (int i = tempSizeTextures; i < size; i++) {
                Object3D object3D = textureObjectForCreate.get(i);

                rendererGLThread.createTextureObject(object3D);
                textureObject3Ds.add(object3D);
            }
            tempSizeTextures = size;
        }

        int sizeDelete = textureObjectForDelete.size();
        if(sizeDelete > 0) {
            for (int i = tempSizeTexturesDelete; i < sizeDelete; i++) {
                Object3D object3D = textureObjectForDelete.get(i);

                rendererGLThread.clearBuffersObject(object3D);
                textureObject3Ds.remove(object3D);
            }
            tempSizeTexturesDelete = sizeDelete;
        }
    }

    public void clearScene() {
        textureObjectForCreate.clear();
        textureObjectForDelete.clear();

        polygonsObjectForCreate.clear();
        polygonsObjectForDelete.clear();

        listTexturePlaneForCreate.clear();
        listTexturePlaneForDelete.clear();

        polygonObject3Ds.clear();
        waterObject3Ds.clear();
        textureObject3Ds.clear();
        orthographicObject3Ds.clear();
    }
}
