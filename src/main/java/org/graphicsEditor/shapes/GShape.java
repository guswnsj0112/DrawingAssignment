package org.graphicsEditor.shapes;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;

abstract public class GShape implements Cloneable {
    public enum EAnchor {
        eRotate,
        eMove,
        eResize
    }
    protected int x0, y0, x1, y1;

    protected Shape shape;

    public GShape() {
    }
    public GShape clone() {
        try {
            return (GShape) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
    public EAnchor onShape(int x, int y) {
        int left = Math.min(this.x0, this.x1);
        int right = Math.max(this.x0, this.x1);
        int top = Math.min(this.y0, this.y1);
        int bottom = Math.max(this.y0, this.y1);

        if (left <= x && x <= right && top <= y && y <= bottom) {
            return EAnchor.eMove;
        }
        return null;
    }
    public void move(int dx, int dy) {
        this.x0 += dx;
        this.y0 += dy;
        this.x1 += dx;
        this.y1 += dy;
    }
    public void resize(int x, int y) {

    }
    public void rotate(int x, int y) {
    }
    public void setLocation0(int x, int y) {
        this.x0 = x;
        this.y0 = y;
    }
    public void setLocation1(int x, int y) {
        this.x1 = x;
        this.y1 = y;
    }

    public void addPoint(int x, int y) {
        this.setLocation1(x, y);
    }

    public void setSize(int width, int height) {
        this.x1 = x0 + width;
        this.y1 = y0 + height;
    }
    abstract public void draw(Graphics2D g);

}
