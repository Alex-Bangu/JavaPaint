package dev.alexbangu.javapaint;


public class Circle extends Shape {
        private Point centre;
        private double radius;

        public Circle(Point centre, double radius){
                super(centre, "Circle");
                this.radius = radius;
        }

        public double getRadius() {
                return radius;
        }

        public void setRadius(double radius) {
                this.radius = radius;
        }

}
