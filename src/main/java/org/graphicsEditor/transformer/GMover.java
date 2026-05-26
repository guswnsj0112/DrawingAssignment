package org.graphicsEditor.transformer;

import org.graphicsEditor.shapes.GShape;

public class GMover extends GTransformer {
    private int px;
    private int py;

    public GMover(GShape shape) {
        super(shape);
    }

    @Override
    public void start(int x, int y) {
        this.px = x;
        this.py = y;
    }

    @Override
    public void keep(int x, int y) {
        this.shape.move(x - this.px, y - this.py);
        this.px = x;
        this.py = y;
    }

    @Override
    public void finish(int x, int y) {
        this.keep(x, y);
    }
}
