package dev.alexbangu.javapaint;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DrawCircleCmd implements DrawCommand {
    private final Shape s;
    private final Color z;
    private Boolean outlined = false;

    public DrawCircleCmd(Shape circle, Color z){
        this.s = circle;
        this.z = z;
    }

    @Override
    public String toString(){

        Circle c = (Circle)s;
        Point p = c.getCentre();

        return "crlc|" + outlined + "|" + z + "|" + c.getRadius() + "|" + p.x + "|" + p.y + "|";
    }

    public void toggleOutlined(){
        this.outlined = !this.outlined;
    }

    @Override
    public void execute(GraphicsContext g2d) {
        g2d.setFill(z);
        Circle c = (Circle) s;
        double x = c.getCentre().x;
        double y = c.getCentre().y;
        double radius = c.getRadius();
        if (this.outlined){
            g2d.setStroke(this.z);
            g2d.strokeOval(x, y, radius, radius);
        }else{
            g2d.fillOval(x, y, radius, radius);
        }
    }
}
