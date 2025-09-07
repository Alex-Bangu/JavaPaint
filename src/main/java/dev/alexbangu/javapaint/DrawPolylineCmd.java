package dev.alexbangu.javapaint;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

import static java.lang.Math.abs;

public class DrawPolylineCmd implements DrawCommand{
    private final ArrayList<Point> points = new ArrayList<>();
    private final Color z;
    private boolean isComplete = false;
    private Boolean outlined = false;

    public DrawPolylineCmd(Color z){
        this.z = z;
    }

    @Override
    public String toString(){

        StringBuilder val = new StringBuilder("poly|" + z + "|" + outlined + "|");

        if (isComplete){
            val.append(points.size()+1).append("|");
        }else{
            val.append(points.size()).append("|");
        }

        for (Point p : points){
            val.append(p.x).append("|").append(p.y).append("|");
        }

        if (isComplete){val.append(points.getFirst().x).append("|").append(points.getFirst().y).append("|");}

        return val.toString();
    }

    public void toggleOutlined(){
        this.outlined = !this.outlined;
    }

    public Boolean isComplete(){
        return this.isComplete;
    }

    public void selfDestruct(){
        points.clear();
    }

    public void addPoint(Point p){

        if (points.size() < 3){
            points.add(p);
        }else{
            Point f = points.getFirst();
            if (abs(p.x - f.x) <= 10 && abs(p.y - f.y) <= 10){
                this.isComplete = true;
                if (outlined){points.add(points.getFirst());}
            }else{
                points.add(p);
            }

        }


    }

    @Override
    public void execute(GraphicsContext g2d) {
        g2d.setStroke(z);
        g2d.setFill(z);

        if (isComplete && ! outlined){
            double[] xPoints = new double[points.size()];
            double[] yPoints = new double[points.size()];

            for (int i = 0; i < points.size(); i++) {
                Point current = points.get(i);
                xPoints[i] = current.x;
                yPoints[i] = current.y;
            }

            g2d.fillPolygon(xPoints,yPoints, points.size());

        }else{

            for(int i=0;i<points.size()-1; i++){
                Point p1=points.get(i);
                Point p2=points.get(i+1);
                g2d.strokeLine(p1.x,p1.y,p2.x,p2.y);
            }

        }

    }
}
