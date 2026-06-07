package main.graphicsEditor.shapes;


import java.awt.Polygon;
import java.awt.Rectangle;

public class GPolygon extends GShape {

    public GPolygon() {
        this.shape = new Polygon();
    }

    @Override
    public GShape clone() {
        GPolygon cloned = new GPolygon();
        Polygon polygon = (Polygon) this.shape;

        for (int i = 0; i < polygon.npoints; i++) {
            ((Polygon) cloned.shape).addPoint(polygon.xpoints[i], polygon.ypoints[i]);
        }

        cloned.setRotationAngle(this.getRotationAngle());
        return cloned;
    }

    @Override
    public void setLocation0(int x, int y) {
        Polygon polygon = (Polygon) this.shape;
        polygon.reset();
        polygon.addPoint(x, y);
        polygon.addPoint(x, y);
    }

    @Override
    public void setLocation1(int x, int y) {
        Polygon polygon = (Polygon) this.shape;

        if (polygon.npoints < 2) {
            polygon.addPoint(x, y);
        } else {
            polygon.xpoints[polygon.npoints - 1] = x;
            polygon.ypoints[polygon.npoints - 1] = y;
            polygon.invalidate();
        }
    }

    @Override
    public void addPoint(int x, int y) {
        Polygon polygon = (Polygon) this.shape;

        setLocation1(x, y);
        polygon.addPoint(x, y);
    }

    @Override
    public void finish(int x, int y) {
        setLocation1(x, y);
    }

    @Override
    public void translate(int dx, int dy) {
        Polygon polygon = (Polygon) this.shape;
        polygon.translate(dx, dy);
    }

    @Override
    public void setFrame(int x, int y, int w, int h) {
        Polygon polygon = (Polygon) this.shape;
        Rectangle old = polygon.getBounds();

        if (old.width == 0 || old.height == 0) {
            return;
        }

        double sx = w / (double) old.width;
        double sy = h / (double) old.height;

        for (int i = 0; i < polygon.npoints; i++) {
            polygon.xpoints[i] = x + (int) Math.round((polygon.xpoints[i] - old.x) * sx);
            polygon.ypoints[i] = y + (int) Math.round((polygon.ypoints[i] - old.y) * sy);
        }

        polygon.invalidate();

        if (isSelected()) {
            setSelected(true);
        }
    }
}