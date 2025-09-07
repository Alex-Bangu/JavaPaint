package dev.alexbangu.javapaint;

public class Rectangle extends Shape {
    private Point secondPoint;
    private double width;
    private double height;

    public Rectangle(Point topLeft, Point bottomRight) {
        super(topLeft, "Rectangle");
        this.secondPoint = bottomRight;
    }

    private void setWidth(){
        this.width = Math.abs(secondPoint.x - super.getCentre().x);
    }

    private void setHeight(){
        this.height = Math.abs(super.getCentre().y - secondPoint.y);
    }

    @Override
    public Point getCentre() {
        return getTrueTopLeft();
    }

    public Point getCurrentCentre() {
        return super.getCentre();
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public Point getTrueTopLeft(){
        double x, y;
        if ((this.secondPoint.x <= super.getCentre().x)){
            x = this.secondPoint.x;
        } else {
            x = super.getCentre().x;
        }
        if ((this.secondPoint.y <= super.getCentre().y)){
            y = this.secondPoint.y;
        } else {
            y = super.getCentre().y;
        }
        return new Point(x,y);
    }

    public void setSecondPoint(Point secondPoint) {
        this.secondPoint = secondPoint;
        this.setHeight();
        this.setWidth();
    }

    public Point getSecondPoint() {
        return this.secondPoint;
    }
}
