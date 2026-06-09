package main.graphicsEditor.shapes;

import java.awt.Rectangle;
import java.awt.geom.Line2D;

public class GLine extends GShape {

    public GLine() {
        this.shape = new Line2D.Double();
    }

    @Override
    public GShape clone() {
        GLine cloned = new GLine();
        Line2D line = (Line2D) this.shape;
        cloned.shape = new Line2D.Double(
                line.getX1(), line.getY1(),
                line.getX2(), line.getY2()
        );
        cloned.setRotationAngle(this.getRotationAngle());
        return cloned;
    }

    @Override
    public void setLocation0(int x, int y) {
        Line2D line = (Line2D) this.shape;
        line.setLine(x, y, x, y);
    }

    @Override
    public void setLocation1(int x, int y) {
        Line2D line = (Line2D) this.shape;
        line.setLine(line.getX1(), line.getY1(), x, y);
    }

    @Override
    public void translate(int dx, int dy) {
        Line2D line = (Line2D) this.shape;
        line.setLine(
                line.getX1() + dx,
                line.getY1() + dy,
                line.getX2() + dx,
                line.getY2() + dy
        );
    }

    @Override
    public EAnchor onShape(int x, int y) {
        EAnchor eAnchor = super.onShape(x, y);
        if (eAnchor != null) {
            return eAnchor;
        }

        Line2D line = (Line2D) this.shape;
        if (line.ptSegDist(x, y) <= 5) {
            return EAnchor.eMove;
        }
        return null;
    }

    @Override
    public void setFrame(int x, int y, int w, int h) {
        Line2D line = (Line2D) this.shape;
        Rectangle old = line.getBounds();

        double sx = old.width == 0 ? 1 : w / (double) old.width;
        double sy = old.height == 0 ? 1 : h / (double) old.height;

        double x1 = old.width == 0 ? x + w / 2.0 : x + (line.getX1() - old.x) * sx;
        double y1 = old.height == 0 ? y + h / 2.0 : y + (line.getY1() - old.y) * sy;
        double x2 = old.width == 0 ? x + w / 2.0 : x + (line.getX2() - old.x) * sx;
        double y2 = old.height == 0 ? y + h / 2.0 : y + (line.getY2() - old.y) * sy;

        line.setLine(x1, y1, x2, y2);

        if (isSelected()) {
            setSelected(true);
        }
    }
}