package org.graphicsEditor.shapes;

import java.awt.*;

public class GRectangle extends GShape{

    public GRectangle() {
        this.shape = new Rectangle();
    }

    @Override
    public GShape clone() {
        GRectangle clonedShape = (GRectangle) super.clone();
        clonedShape.shape = new Rectangle();
        return clonedShape;
    }

    public void setLocation0(int x, int y) {
        this.x0 = x;
        this.y0 = y;
        Rectangle r = (Rectangle) shape;
        r.setFrame(x, y, 0, 0);
    }
    public void setLocation1(int x, int y) {
        Rectangle r = (Rectangle) shape;
        r.setFrame(
                Math.min(this.x0, x),
                Math.min(this.y0, y),
                Math.abs(x - this.x0),
                Math.abs(y - this.y0)
        );
    }
}
