package dev.alexbangu.javapaint;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Square extends Rectangle {
    private double sideLen;

    public Square(Point topLeft, Point bottomRight) {
        super(topLeft, bottomRight);
        this.setType("Square");
    }

    private void setSideLen() {
        this.sideLen = min(Math.abs(this.getSecondPoint().x - this.getCurrentCentre().x),
                Math.abs(this.getCurrentCentre().y - this.getSecondPoint().y));
    }

    public Point getStartPoint() {
        return getTrueTopLeft();
    }

    public double getSideLen() {
        return sideLen;
    }


    public Point getTrueTopLeft(){
        double x, y;
        if ((this.getSecondPoint().x <= this.getCurrentCentre().x)){
            x = max(this.getSecondPoint().x, this.getCurrentCentre().x - getSideLen());
        } else {
            x = this.getCurrentCentre().x;
        }
        if ((this.getSecondPoint().y <= this.getCurrentCentre().y)){
            y = max(this.getSecondPoint().y, this.getCurrentCentre().y - getSideLen());

        } else {
            y = this.getCurrentCentre().y;
        }
        return new Point(x,y);
    }

    public void setSecondPoint(Point secondPoint) {
        super.setSecondPoint(secondPoint);
        this.setSideLen();
    }
}
