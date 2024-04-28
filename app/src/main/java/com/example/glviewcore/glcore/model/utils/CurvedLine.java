package com.example.glviewcore.glcore.model.utils;

import static java.lang.Math.PI;

import com.example.glviewcore.glcore.math.Quaternion;
import com.example.glviewcore.glcore.math.Vector3;

import java.util.ArrayList;
import java.util.List;

public class CurvedLine {
    private final float _width;
    private final float _cornerRadius;
    private final float _roundedAngle;
    private static final float epsilon = 0.0001f;

    public CurvedLine(float width, float cornerRadius, float roundedAngle) {
        _width = width;
        _cornerRadius = cornerRadius;
        _roundedAngle = roundedAngle;
    }

    public List<Vector3> getRoundedVertices(List<Vector3> vertices) {
        Vector3 n = Vector3.Z;

        Vector3 center;
        float Radius = (_cornerRadius < _width / 2) ? _width / 2 : _cornerRadius;

        List<Vector3> roundedVertices = new ArrayList<>();
        Vector3 dir = Vector3.subtractAndCreate(vertices.get(1), vertices.get(0));
        dir.normalize();

        Vector3 qdir = Vector3.crossAndCreate(dir, n);
        qdir.normalize();

        n = Vector3.crossAndCreate(qdir, dir);
        n.normalize();
        roundedVertices.add(vertices.get(0));
        for (int i = 1; i < vertices.size() - 1; i++) {
            Vector3 nextDir = Vector3.subtractAndCreate(vertices.get(i + 1), vertices.get(i));
            Vector3 nextQdir = Vector3.crossAndCreate(nextDir, n);
            nextDir.normalize();
            nextQdir.normalize();

            if (nextDir.angle(dir) < 1.0f) {
                roundedVertices.add(vertices.get(i));
            } else {
                float maxR = (float) (Math.min(
                        Vector3.subtractAndCreate(vertices.get(i), vertices.get(i - 1)).length(),
                        Vector3.subtractAndCreate(vertices.get(i + 1), vertices.get(i)).length()) * 0.5f);

                float r = Radius;
                Vector3 v = dir.invertAndCreate();
                v.normalize();
                Vector3 nextDirNormalized = new Vector3(nextDir);
                nextDirNormalized.normalize();
                Vector3 pdir = Vector3.addAndCreate(v, nextDirNormalized);
                pdir.normalize();
                if (pdir.length() < epsilon || Vector3.subtractAndCreate(qdir, nextQdir).length() < epsilon) {
                    Vector3 normal1 = Vector3.crossAndCreate(dir, qdir).inverse();
                    Vector3 normal2 = Vector3.crossAndCreate(nextDir, nextQdir).inverse();
                    pdir = Vector3.addAndCreate(normal1, normal2);
                    pdir.normalize();
                }

                float angle = (float) dir.angle(pdir);
                float rwidth = (r / (float) Math.sin(angle * Math.PI / 180.0f));
                float rlength = (float) Math.sqrt(rwidth * rwidth - r * r);

                if (rlength > maxR) {
                    rlength = maxR;
                    rwidth = rlength / (float) Math.cos(angle * Math.PI / 180.0f);
                }

                Vector3 vertex1 = Vector3.addAndCreate(vertices.get(i), Vector3.multiplyAndCreate(pdir, rwidth));
                Vector3 vertex2 = Vector3.subtractAndCreate(vertices.get(i), Vector3.multiplyAndCreate(pdir, rwidth));

                Vector3 rightPoint = Vector3.addAndCreate(vertices.get(i), Vector3.multiplyAndCreate(nextDir, rlength));
                Vector3 leftPoint = Vector3.addAndCreate(vertices.get(i), Vector3.multiplyAndCreate(dir, -rlength));

                center = (Math.abs(Vector3.dot(Vector3.subtractAndCreate(leftPoint, vertex1), dir)) < epsilon) ?
                        vertex1 : vertex2;

                Vector3 rotateAxis = Vector3.crossAndCreate(dir, nextDir);
                Vector3 rotateVector = Vector3.subtractAndCreate(leftPoint, center);

                angle = (float) Vector3.subtractAndCreate(leftPoint, center).angle(Vector3.subtractAndCreate(rightPoint, center));
                int segmentCount = (int) (angle / _roundedAngle);
                float angleDelta = angle / (float) segmentCount;

                Quaternion q = new Quaternion(rotateAxis, angleDelta);

                roundedVertices.add(leftPoint);
                for (int j = 0; j < segmentCount - 1; j++) {
                    rotateVector = q.multiply(rotateVector).clone();
                    roundedVertices.add(Vector3.addAndCreate(center, rotateVector));
                }
                roundedVertices.add(rightPoint);
            }
            dir = nextDir;
            qdir = nextQdir;
        }
        roundedVertices.add(vertices.get(vertices.size() - 1));

        return roundedVertices;
    }
}
