package org.graphicsEditor.transformer;

import org.graphicsEditor.shapes.GShape;

import java.awt.*;


//shape이 없다면 Transformer의 의미가 없다
abstract public class GTransformer {
    protected GShape shape; //그래서 안에 보면 shape이라는 얘를 가지고 있다.
    //앞으로 모든 Shape 얘가 작업을 한다

    public GTransformer(GShape shape) {
        this.shape = shape;
    }

    //4단계를 만들어둠
    abstract public void start(int x, int y);
    abstract public void keep(int x, int y);
    abstract public void finish(int x, int y);
    public void cont(int x, int y) {}
}


