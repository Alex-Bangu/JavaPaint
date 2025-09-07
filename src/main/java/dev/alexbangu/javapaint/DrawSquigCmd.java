package dev.alexbangu.javapaint;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class DrawSquigCmd implements DrawCommand {
    private final ArrayList<Point> points = new ArrayList<>();
    private final Color z;

    public DrawSquigCmd(Color z){
        this.z = z;

    }

    @Override
    public String toString(){

        StringBuilder val = new StringBuilder("sqig|" + z + "|" + points.size() + "|");

        for (Point p : points){
            val.append(p.x).append("|").append(p.y).append("|");
        }
        return val.toString();
    }

    public void addPoint(Point p){
        points.add(p);
    }

    @Override
    public void execute(GraphicsContext g2d) {
        g2d.setStroke(z);

        for(int i=0;i<points.size()-1; i++){
            Point p1=points.get(i);
            Point p2=points.get(i+1);
            if (p2.x == -1 || p1.x == -1) {
                i++;
            }
            else {
                g2d.strokeLine(p1.x,p1.y,p2.x,p2.y);
            }
        }

    }
}