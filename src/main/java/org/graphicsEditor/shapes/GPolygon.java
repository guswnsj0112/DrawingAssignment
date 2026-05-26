package org.graphicsEditor.shapes;

import java.awt.*;

public class GPolygon extends GShape {
    private Polygon polygon;
    private boolean hasPreviewPoint;

    public GPolygon() {
        this.polygon = new Polygon();
        this.shape = this.polygon;
        this.hasPreviewPoint = false;
    }

    @Override
    public GPolygon clone() {
        GPolygon cloned = (GPolygon) super.clone();
        cloned.polygon = new Polygon(this.polygon.xpoints, this.polygon.ypoints, this.polygon.npoints);
        cloned.shape = cloned.polygon;
        return cloned;
    }

    @Override
    public void setLocation0(int x, int y) {
        this.polygon.reset();
        this.polygon.addPoint(x, y);
        this.hasPreviewPoint = false;
        this.updateBounds();
    }

    @Override
    public void setLocation1(int x, int y) {
        if (this.polygon.npoints == 0) {
            this.setLocation0(x, y);
        }
        if (!this.hasPreviewPoint) {
            this.polygon.addPoint(x, y);
            this.hasPreviewPoint = true;
        } else {
            this.polygon.xpoints[this.polygon.npoints - 1] = x;
            this.polygon.ypoints[this.polygon.npoints - 1] = y;
            this.polygon.invalidate();
        }
        this.updateBounds();
    }

    @Override
    public void addPoint(int x, int y) {
        this.setLocation1(x, y);
        this.polygon.addPoint(x, y);
        this.hasPreviewPoint = true;
        this.updateBounds();
    }

    @Override
    public void move(int dx, int dy) {
        super.move(dx, dy);
        this.polygon.translate(dx, dy);
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.drawPolygon(this.polygon);
    }

    private void updateBounds() {
        Rectangle bounds = this.polygon.getBounds();
        this.x0 = bounds.x;
        this.y0 = bounds.y;
        this.x1 = bounds.x + bounds.width;
        this.y1 = bounds.y + bounds.height;
    }
}
