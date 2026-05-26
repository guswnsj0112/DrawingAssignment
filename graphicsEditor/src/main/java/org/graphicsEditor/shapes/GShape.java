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
        if (this.shape.contains(x, y)) {
            return EAnchor.eMove;
        } else {
            return null;
        }
    }

    public void draw(Graphics2D g) {
        g.draw(shape);
    }

    public void resize(int x, int y) {

    }
    public void rotate(int x, int y) {
    }
    public void setLocation0(int x, int y) {}
    public void setLocation1(int x, int y) {}
    public void translate(int dx, int dy) {}


}
