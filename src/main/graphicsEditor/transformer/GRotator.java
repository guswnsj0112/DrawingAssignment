package main.graphicsEditor.transformer;

import main.graphicsEditor.shapes.GShape;

import java.awt.Rectangle;

public class GRotator extends GTransformer {
    private double centerX;
    private double centerY;
    private double startAngle;
    private double originalAngle;

    public GRotator(GShape shape) {
        super(shape);
    }

    @Override
    public void start(int x, int y) {
        Rectangle bounds = this.shape.getBounds();

        this.centerX = bounds.getCenterX();
        this.centerY = bounds.getCenterY();

        this.startAngle = Math.atan2(y - centerY, x - centerX);
        this.originalAngle = this.shape.getRotationAngle();
    }

    @Override
    public void keep(int x, int y) {
        double currentAngle = Math.atan2(y - centerY, x - centerX);
        double deltaAngle = currentAngle - startAngle;

        this.shape.setRotationAngle(originalAngle + deltaAngle);
    }

    @Override
    public void finish(int x, int y) {
        keep(x, y);
    }
}