package dev.alexbangu.javapaint;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DrawOvalCmd implements DrawCommand {
    private final Shape s;
    private final Color z;
    private Boolean outlined = false;

    public DrawOvalCmd(Shape oval, Color z){
        this.s = oval;
        this.z = z;
    }

    @Override
    public String toString(){

        Oval c = (Oval) s;
        Point p1 = c.getCurrentCentre();
        Point p2 = c.getSecondPoint();

        return "oval|" + outlined + "|" + z + "|" + p1.x + "|" + p1.y + "|" + p2.x + "|" + p2.y + "|";
    }

    public void toggleOutlined(){
        this.outlined = !this.outlined;
    }

    @Override
    public void execute(GraphicsContext g2d) {
        g2d.setFill(z);
        Oval oval = (Oval) s;
        Point one = oval.getCentre();
        double x = one.x;
        double y = one.y;
        double w = oval.getWidth();
        double h = oval.getHeight();
        if (this.outlined){
            g2d.setStroke(this.z);
            g2d.strokeOval(x, y, w, h);
        }else {
            g2d.fillOval(x, y, w, h);
        }
    }
}