package dev.alexbangu.javapaint;

import javafx.scene.canvas.GraphicsContext;


public interface DrawCommand {

    /**
     * All draw commands must draw whatever they represent
     * to the "screen" that is g2d.
     *
     * @param g2d the GraphicsContext which the command is drawing to
     */
    void execute(GraphicsContext g2d);

    String toString();
}
