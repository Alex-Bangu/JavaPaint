package dev.alexbangu.javapaint;

public class Shape {
    private Point centre;
    private String type;

    public Shape(Point centre, String type) {
        this.centre = centre;
        this.type = type;
    }

    public Point getCentre() {
        return centre;
    }

    public void setCentre(Point centre) {
        this.centre = centre;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
