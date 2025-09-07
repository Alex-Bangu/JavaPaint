package dev.alexbangu.javapaint;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DrawSquareCmd implements DrawCommand {
    private final Shape s;
    private final Color z;
    private Boolean outlined = false;

    public DrawSquareCmd(Shape square, Color z){
        this.s = square;
        this.z = z;
    }

    @Override
    public String toString(){

        Square c = (Square) s;
        Point p1 = c.getCurrentCentre();
        Point p2 = c.getSecondPoint();

        return "sqre|" + outlined + "|" + z + "|" + p1.x + "|" + p1.y + "|" + p2.x + "|" + p2.y + "|";
    }

    public void toggleOutlined(){
        this.outlined = !this.outlined;
    }

    @Override
    public void execute(GraphicsContext g2d) {
        g2d.setFill(z);
        Square square = (Square) s;
        Point one = square.getStartPoint();
        double x = one.x;
        double y = one.y;
        double w = square.getSideLen();
        if (this.outlined){
            g2d.setStroke(this.z);
            g2d.strokeRect(x, y, w, w);
        }else {
            g2d.fillRect(x, y, w, w);
        }
    }
}