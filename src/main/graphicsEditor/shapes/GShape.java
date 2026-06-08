package main.graphicsEditor.shapes;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RectangularShape;
import javax.swing.ImageIcon;

abstract public class GShape implements Cloneable {
    public enum EAnchor {
        eNW,
        eNN,
        eNE,
        eEE,
        eSE,
        eSS,
        eSW,
        eWW,
        eRotate,
        eMove,
    }

    private boolean isSelected;

    protected Shape shape;
    private final Anchors anchors;

    private double rotationAngle = 0;



    public GShape() {
        this.isSelected = false;
        this.anchors = new Anchors();
    }

    public GShape clone() {
        try {
            GShape cloned = (GShape) super.clone();
            cloned.shape = (Shape) (((RectangularShape) this.shape).clone());
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    // getter and setter
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
        if (isSelected) {
            this.anchors.setPosition(this.shape.getBounds());
        }
    }

    private Point inverseRotatePoint(int x, int y) {
        Rectangle bounds = this.shape.getBounds();

        double centerX = bounds.getCenterX();
        double centerY = bounds.getCenterY();

        double dx = x - centerX;
        double dy = y - centerY;

        double cos = Math.cos(-this.rotationAngle);
        double sin = Math.sin(-this.rotationAngle);

        double rotatedX = centerX + dx * cos - dy * sin;
        double rotatedY = centerY + dx * sin + dy * cos;

        return new Point((int) rotatedX, (int) rotatedY);
    }



    public EAnchor onShape(int x, int y) {
        EAnchor eAnchor = null;

        if (isSelected) {
            eAnchor = this.anchors.onShape(x, y);
        }

        if (eAnchor == null) {
            Point p = inverseRotatePoint(x, y);

            if (this.shape.contains(p.x, p.y)) {
                eAnchor = EAnchor.eMove;
            }
        }

        return eAnchor;
    }

    public void draw(Graphics2D g) {
        Graphics2D g2 = (Graphics2D) g.create();

        Rectangle bounds = this.shape.getBounds();
        double centerX = bounds.getCenterX();
        double centerY = bounds.getCenterY();

        g2.rotate(this.rotationAngle, centerX, centerY);
        g2.draw(shape);
        g2.dispose();

        if (isSelected) {
            this.anchors.draw(g);
        }
    }

    public void setLocation0(int x, int y) {
    }

    public void setLocation1(int x, int y) {
    }

    public void translate(int dx, int dy) {
    }

    //polygon 전용
    public void addPoint(int x, int y) {
    }

    public void finish(int x, int y) {
    }

    private static class Anchors {
        private Image rotateImage;

        public int w = 15;
        public int h = 15;

        private final Ellipse2D[] anchors;

        public Anchors() {
            anchors = new Ellipse2D[EAnchor.values().length - 1];

            for (int i = 0; i < anchors.length; i++) {
                this.anchors[i] = new Ellipse2D.Float();
            }

            this.rotateImage = new ImageIcon("images/rotate.png").getImage();
        }

        public EAnchor onShape(int x, int y) {
            for (int i = 0; i < anchors.length; i++) {
                if (this.anchors[i].contains(x, y)) {
                    return EAnchor.values()[i];
                }
            }
            return null;
        }

        public void setPosition(Rectangle br) {
            int x = br.x;
            int y = br.y;
            int width = br.width;
            int height = br.height;

            int left = x - w / 2;
            int centerX = x + width / 2 - w / 2;
            int right = x + width - w / 2;

            int top = y - h / 2;
            int centerY = y + height / 2 - h / 2;
            int bottom = y + height - h / 2;

            this.anchors[EAnchor.eNW.ordinal()].setFrame(left, top, w, h);
            this.anchors[EAnchor.eNN.ordinal()].setFrame(centerX, top, w, h);
            this.anchors[EAnchor.eNE.ordinal()].setFrame(right, top, w, h);

            this.anchors[EAnchor.eWW.ordinal()].setFrame(left, centerY, w, h);
            this.anchors[EAnchor.eEE.ordinal()].setFrame(right, centerY, w, h);

            this.anchors[EAnchor.eSW.ordinal()].setFrame(left, bottom, w, h);
            this.anchors[EAnchor.eSS.ordinal()].setFrame(centerX, bottom, w, h);
            this.anchors[EAnchor.eSE.ordinal()].setFrame(right, bottom, w, h);
            this.anchors[EAnchor.eRotate.ordinal()].setFrame(centerX, top - 30, w, h);
        }

        public void draw(Graphics2D g) {
            for (int i = 0; i < anchors.length; i++) {
                if (i == EAnchor.eRotate.ordinal()) {
                    Rectangle bounds = anchors[i].getBounds();
                    g.drawImage(rotateImage, bounds.x, bounds.y, bounds.width, bounds.height, null);
                } else {
                    g.draw(anchors[i]);
                }
            }
        }
    }


    public Rectangle getBounds() {
        return this.shape.getBounds();
    }

    public void setFrame(int x, int y, int w, int h) {
        ((RectangularShape) this.shape).setFrame(x, y, w, h);
        if (this.isSelected) {
            this.anchors.setPosition(this.shape.getBounds());
        }
    }

    //getter and setter
    public double getRotationAngle() {
        return this.rotationAngle;
    }

    public void setRotationAngle(double rotationAngle) {
        this.rotationAngle = rotationAngle;
    }

}
