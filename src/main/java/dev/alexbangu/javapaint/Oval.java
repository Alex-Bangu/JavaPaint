package dev.alexbangu.javapaint;

public class Oval extends Rectangle {

    public Oval(Point topLeft, Point bottomRight) {
        super(topLeft, bottomRight);
        this.setType("Oval");
    }
}
