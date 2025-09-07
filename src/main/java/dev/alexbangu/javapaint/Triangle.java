package dev.alexbangu.javapaint;

public class Triangle extends Rectangle {
    private boolean flipped = false;

    public Triangle(Point topLeft, Point bottomRight) {
        super(topLeft, bottomRight);
        this.setType("Triangle");
    }

    public void setFlipped(double x, double y) {
        if (this.getTrueTopLeft().y < y) {
            flipped = true;
            // System.out.println("Flipped triangle");
        }
        else if (this.getTrueTopLeft().y >= y) {
            flipped = false;
            // System.out.println("UNFlipped triangle");
        }
    }

    private Point getTopVertex() {
        return new Point(this.getTrueTopLeft().x + this.getWidth() / 2, this.getTrueTopLeft().y);
    }

    public double [] getVerticesX() {
        // return the x points of each vertex
        double first = (int) this.getTrueTopLeft().x;
        double second = (int) this.getTrueTopLeft().x + (int) this.getWidth();
        double third = (int) this.getTopVertex().x;

        return new double[] { first, second, third };
    }

    public double [] getVerticesY() {
        // return the y points of each vertex

        double first;
        double second;
        double third;

        if (!flipped) {
            first = (int) this.getTrueTopLeft().y + (int) this.getHeight();
            second = (int) this.getTrueTopLeft().y + (int) this.getHeight();
            third = (int) this.getTopVertex().y;
        }
        else {
            first = (int) this.getTrueTopLeft().y;
            second = (int) this.getTrueTopLeft().y;
            third = (int) this.getTrueTopLeft().y + this.getHeight();
        }

        return new double[] { first, second, third };
    }

}
