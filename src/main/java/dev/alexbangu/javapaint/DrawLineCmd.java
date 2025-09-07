package dev.alexbangu.javapaint;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DrawLineCmd implements DrawCommand{
    private final Color z;
    private Point s;
    private Point f;


    public DrawLineCmd(Color z, Point s){
        this.z = z;
        this.s = s;
    }

    @Override
    public String toString(){

        return "line|" + z + "|" + s.x + "|" + s.y + "|" + f.x + "|" + f.y + "|";
    }

    public void UpdateEndPoint(Point p){
            this.f = p;
    }

    public void UpdateStartPoint(Point p){
        this.s = p;
    }

    public void SelfDestruct(){
        this.s = null;
        this.f = null;
    }

    @Override
    public void execute(GraphicsContext g2d) {

        if (f != null && s != null){

            g2d.setStroke(z);

            g2d.strokeLine(s.x,s.y,f.x,f.y);

        }

    }
}
