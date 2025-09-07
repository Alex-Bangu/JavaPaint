package dev.alexbangu.javapaint;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DrawRectCmd implements DrawCommand {
    private final Shape s;
    private final Color z;
    private Boolean outlined = false;

    public DrawRectCmd(Shape rect, Color z){
        this.s = rect;
        this.z = z;
    }

    @Override
    public String toString(){

        Rectangle c = (Rectangle) s;
        Point p1 = c.getCurrentCentre();
        Point p2 = c.getSecondPoint();

        return "rect|" + outlined + "|" + z + "|" + p1.x + "|" + p1.y + "|" + p2.x + "|" + p2.y + "|";
    }

    public void toggleOutlined(){
        this.outlined = !this.outlined;
    }

    @Override
    public void execute(GraphicsContext g2d) {
        g2d.setFill(z);
        Rectangle r = (Rectangle) s;
        Point one = r.getCentre();
        double x = one.x;
        double y = one.y;
        double w = r.getWidth();
        double h = r.getHeight();
        if (this.outlined){
            g2d.setStroke(this.z);
            g2d.strokeRect(x, y, w, h);
        }else {
            g2d.fillRect(x, y, w, h);
        }
    }
}