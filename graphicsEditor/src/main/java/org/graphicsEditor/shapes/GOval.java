package org.graphicsEditor.shapes;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class GOval extends GShape{

    public GOval() {
        this.shape = new Ellipse2D.Double();
    }

    @Override
    public GShape clone() {
        GOval clonedShape = (GOval) super.clone();
        clonedShape.shape = new Ellipse2D.Double();
        return clonedShape;
    }

    public void setLocation0(int x, int y) {
        this.x0 = x;
        this.y0 = y;
        Ellipse2D r = (Ellipse2D) shape;
        r.setFrame(x, y, 0, 0);
    }
    public void setLocation1(int x, int y) {
        Ellipse2D r = (Ellipse2D) shape;
        r.setFrame(
                Math.min(this.x0, x),
                Math.min(this.y0, y),
                Math.abs(x - this.x0),
                Math.abs(y - this.y0)
        );
    }
}
