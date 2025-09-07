package dev.alexbangu.javapaint;

public class ShapeFactory {
    private Shape shape;

    public ShapeFactory() {}

    public Shape createCircle(Point centre, double x, double y) {
        if(this.shape == null) {
            this.shape = new Circle(centre, 0);
        }
        Circle circle = (Circle) this.shape;
        double true_radius = circle.getRadius()/2;
        Point true_centre = new Point(circle.getCentre().x + true_radius, circle.getCentre().y + true_radius);
        double dx = x - true_centre.x;
        double dy = y - true_centre.y;
        double dr = Math.sqrt(dx*dx + dy * dy) - true_radius;
        Point new_centre = new Point(circle.getCentre().x - dr, circle.getCentre().y - dr);
        circle.setCentre(new_centre);
        circle.setRadius(2*dr + circle.getRadius());
        this.shape = circle;
        return circle;
    }

    public Shape createRectangle(Point centre, double x, double y) {
        if(this.shape == null) {
            this.shape = new Rectangle(centre, centre);
        }
        Rectangle rect = (Rectangle) this.shape;
        rect.setSecondPoint(new Point(x, y));
        this.shape = rect;
        return rect;
    }

    public Shape createSquare(Point centre, double x, double y) {
        if(this.shape == null) {
            this.shape = new Square(centre, centre);
        }
        Square square = (Square) this.shape;
        square.setSecondPoint(new Point(x, y));
        this.shape = square;
        return square;
    }

    public Shape createOval(Point centre, double x, double y) {
        if(this.shape == null) {
            this.shape = new Oval(centre, centre);
        }
        Oval oval = (Oval) this.shape;
        oval.setSecondPoint(new Point(x, y));
        this.shape = oval;
        return oval;
    }

    public Shape createTriangle(Point centre, double x, double y) {
        if(this.shape == null) {
            this.shape = new Triangle(centre, centre);
        }
        Triangle triangle = (Triangle) this.shape;
        triangle.setSecondPoint(new Point(x, y));
        triangle.setFlipped(x, y);
        this.shape = triangle;
        return triangle;
    }

    public Shape createEraser(Point centre, double radius) {
        Eraser eraser = new Eraser(centre, radius);
        Point new_centre = new Point(centre.x - radius/2, centre.y - radius/2);
        eraser.setCentre(new_centre);
        return eraser;
    }

    public void reset() {
        this.shape = null;
    }
}
