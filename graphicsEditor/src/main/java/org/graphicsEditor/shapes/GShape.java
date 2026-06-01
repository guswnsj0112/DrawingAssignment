package org.graphicsEditor.shapes;

import java.awt.*;
import java.awt.geom.AffineTransform;

abstract public class GShape implements Cloneable {
    public enum EAnchor {
        eRotate,
        eMove,
        eResize
    }
    protected int x0, y0, x1, y1;

    protected Shape shape;
    private static final int ANCHOR_SIZE = 8;

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
        Rectangle bounds = this.shape.getBounds();

        for (Rectangle anchor : this.getResizeAnchors(bounds)) {
            if (anchor.contains(x, y)) {
                return EAnchor.eResize;
            }
        }
        Shape strokedShape = new BasicStroke(4).createStrokedShape(this.shape);
        if (this.shape.contains(x, y) || strokedShape.contains(x, y)) {
            return EAnchor.eMove;
        }
        return null;
    }

    public void draw(Graphics2D g) {
        g.draw(shape);
    }

    public void drawAnchors(Graphics2D g) {
        Rectangle bounds = this.shape.getBounds();
        Color color = g.getColor();

        g.draw(bounds);
        g.setColor(Color.WHITE);
        for (Rectangle anchor : this.getResizeAnchors(bounds)) {
            g.fill(anchor);
        }
        g.setColor(Color.BLACK);
        for (Rectangle anchor : this.getResizeAnchors(bounds)) {
            g.draw(anchor);
        }
        g.setColor(color);
    }

    public Rectangle getBounds() {
        return this.shape.getBounds();
    }

    public void resize(int x, int y) {

    }
    public void rotate(int x, int y) {
    }
    public void setLocation0(int x, int y) {}
    public void setLocation1(int x, int y) {}
    public void addPoint(int x, int y) {}
    public void finish(int x, int y) {}
    public void translate(int dx, int dy) {
        AffineTransform affineTransform = AffineTransform.getTranslateInstance(dx, dy);
        this.transform(affineTransform);
    }

    public void scale(double sx, double sy, double anchorX, double anchorY) {
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.translate(anchorX, anchorY);
        affineTransform.scale(sx, sy);
        affineTransform.translate(-anchorX, -anchorY);
        this.transform(affineTransform);
    }

    protected void transform(AffineTransform affineTransform) {
        this.shape = affineTransform.createTransformedShape(this.shape);
    }

    private Rectangle[] getResizeAnchors(Rectangle bounds) {
        int half = ANCHOR_SIZE / 2;
        int x0 = bounds.x;
        int y0 = bounds.y;
        int x1 = bounds.x + bounds.width / 2;
        int y1 = bounds.y + bounds.height / 2;
        int x2 = bounds.x + bounds.width;
        int y2 = bounds.y + bounds.height;

        return new Rectangle[] {
                new Rectangle(x0 - half, y0 - half, ANCHOR_SIZE, ANCHOR_SIZE),
                new Rectangle(x1 - half, y0 - half, ANCHOR_SIZE, ANCHOR_SIZE),
                new Rectangle(x2 - half, y0 - half, ANCHOR_SIZE, ANCHOR_SIZE),
                new Rectangle(x0 - half, y1 - half, ANCHOR_SIZE, ANCHOR_SIZE),
                new Rectangle(x2 - half, y1 - half, ANCHOR_SIZE, ANCHOR_SIZE),
                new Rectangle(x0 - half, y2 - half, ANCHOR_SIZE, ANCHOR_SIZE),
                new Rectangle(x1 - half, y2 - half, ANCHOR_SIZE, ANCHOR_SIZE),
                new Rectangle(x2 - half, y2 - half, ANCHOR_SIZE, ANCHOR_SIZE)
        };
    }

}
