package main.graphicsEditor.transformer;

import java.awt.Rectangle;

import main.graphicsEditor.shapes.GShape;
import main.graphicsEditor.shapes.GShape.EAnchor;

public class GResizer extends GTransformer{
    private EAnchor anchor;
    private Rectangle originalBounds;


    public GResizer(GShape shape, EAnchor anchor) {
        super(shape);
        this.anchor = anchor;
    }
    @Override
    public void start(int x, int y) {
        this.originalBounds = this.shape.getBounds();
    }

    @Override
    public void keep(int x, int y) {
        int left = originalBounds.x;
        int top = originalBounds.y;
        int right = originalBounds.x + originalBounds.width;
        int bottom = originalBounds.y + originalBounds.height;

        switch (anchor) {
            case eNW:
                left = x;
                top = y;
                break;
            case eNN:
                top = y;
                break;
            case eNE:
                right = x;
                top = y;
                break;
            case eEE:
                right = x;
                break;
            case eSE:
                right = x;
                bottom = y;
                break;
            case eSS:
                bottom = y;
                break;
            case eSW:
                left = x;
                bottom = y;
                break;
            case eWW:
                left = x;
                break;
            default:
                break;
        }

        int newX = Math.min(left, right);
        int newY = Math.min(top, bottom);
        int newW = Math.abs(right - left);
        int newH = Math.abs(bottom - top);

        this.shape.setFrame(newX, newY, newW, newH);
    }

    @Override
    public void finish(int x, int y) {
        keep(x, y);
    }
}

