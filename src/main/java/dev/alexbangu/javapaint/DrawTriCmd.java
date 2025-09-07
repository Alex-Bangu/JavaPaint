package dev.alexbangu.javapaint;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DrawTriCmd implements DrawCommand {
    private final Shape s;
    private final Color z;
    private Boolean outlined = false;

    public DrawTriCmd(Shape triangle, Color z){
        this.s = triangle;
        this.z = z;
    }

    @Override
    public String toString(){

        Triangle c = (Triangle) s;
        Point p1 = c.getCurrentCentre();
        Point p2 = c.getSecondPoint();

        return "tria|" + outlined + "|" + z + "|" + p1.x + "|" + p1.y + "|" + p2.x + "|" + p2.y + "|";
    }

    public void toggleOutlined(){
        this.outlined = !this.outlined;
    }

    @Override
    public void execute(GraphicsContext g2d) {
        g2d.setFill(z);
        Triangle t = (Triangle) s;
        double[] xpts = t.getVerticesX();
        double[] ypts = t.getVerticesY();
        if (this.outlined){
            g2d.setStroke(this.z);
            g2d.strokePolygon(xpts,ypts,3);
        }else {
            g2d.fillPolygon(xpts, ypts, 3);
        }
    }
}