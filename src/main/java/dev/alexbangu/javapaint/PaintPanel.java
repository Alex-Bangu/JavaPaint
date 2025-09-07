package dev.alexbangu.javapaint;

import javafx.scene.canvas.Canvas;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

public class PaintPanel extends Canvas implements EventHandler<MouseEvent>, Observer {
    private String mode="Circle";
    private PaintModel model;
    private ShapeFactory factory;
    private DrawSquigCmd currentSquiggle;
    private DrawPolylineCmd currentPolyline;
    private DrawLineCmd currentLine;
    private Color currentColor = Color.GREEN;
    private Boolean isOutlined = false;

    public PaintPanel(PaintModel model) {
        super(300, 300);
        this.model=model;
        this.model.addObserver(this);
        this.factory = new ShapeFactory();

        this.addEventHandler(MouseEvent.MOUSE_PRESSED, this);
        this.addEventHandler(MouseEvent.MOUSE_RELEASED, this);
        this.addEventHandler(MouseEvent.MOUSE_MOVED, this);
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, this);
        this.addEventHandler(MouseEvent.MOUSE_DRAGGED, this);
    }
    /**
     *  Controller aspect of this
     */
    public void setMode(String mode){
        this.mode=mode;
        System.out.println(this.mode);
    }

    public void ExportToPNG(){
        WritableImage a = new WritableImage((int)getWidth(), (int)getHeight());
        WritableImage snapshot = this.snapshot(null, a);

        BufferedImage bi = new BufferedImage((int)getWidth(), (int)getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int x=0;x<snapshot.getWidth();x++){
            for (int y=0;y<snapshot.getHeight();y++){
                bi.setRGB(x, y,snapshot.getPixelReader().getArgb(x, y));
            }
        }
        try {
            String directory = "exported_drawings";
            createDirectory(directory);
            String filename = generateUniqueFileName(directory);
            System.out.println(filename);

            File file = new File(directory, filename + ".png");
            ImageIO.write(bi, "png", file);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String generateUniqueFileName(String directory) {

        String timestamp = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
        String fileName = "Exported " + timestamp;
        return fileName;
    }

    private static void createDirectory(String directory) {

        Path path = Paths.get(directory);

        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void handle(MouseEvent mouseEvent) {


        Point centre = new Point(mouseEvent.getX(), mouseEvent.getY());
        // Later when we learn about inner classes...
        // https://docs.oracle.com/javafx/2/events/DraggablePanelsExample.java.htm

        EventType<MouseEvent> mouseEventType = (EventType<MouseEvent>) mouseEvent.getEventType();

        if (!Objects.equals(this.mode, "Polyline") && currentPolyline != null){
            currentPolyline.selfDestruct();
            currentLine.SelfDestruct();
            currentPolyline = null;
            currentLine = null;
            this.model.manualSetChanged();
        }

        if(mouseEventType.equals(MouseEvent.MOUSE_RELEASED) || mouseEventType.equals(MouseEvent.MOUSE_PRESSED)
                || mouseEventType.equals(MouseEvent.MOUSE_DRAGGED)) {

        // "Circle", "Rectangle", "Square", "Squiggle", "Polyline", "Oval", "Triangle, Line"
        switch(this.mode) {

            case "Circle":
                Shape circle = this.factory.createCircle(centre, mouseEvent.getX(), mouseEvent.getY());
                DrawCircleCmd c = new DrawCircleCmd(circle,currentColor);
                if (isOutlined){ c.toggleOutlined(); }
                this.model.addCommand(c);
                break;

            case "Rectangle":
                Shape rect = this.factory.createRectangle(centre, mouseEvent.getX(), mouseEvent.getY());
                DrawRectCmd r = new DrawRectCmd(rect,currentColor);
                if (isOutlined){ r.toggleOutlined(); }
                this.model.addCommand(r);
                break;

            case "Square":
                Shape square = this.factory.createSquare(centre, mouseEvent.getX(), mouseEvent.getY());
                DrawSquareCmd s = new DrawSquareCmd(square,currentColor);
                if (isOutlined){ s.toggleOutlined(); }
                this.model.addCommand(s);
                break;

            case "Oval":
                Shape oval = this.factory.createOval(centre, mouseEvent.getX(), mouseEvent.getY());
                DrawOvalCmd o = new DrawOvalCmd(oval,currentColor);
                if (isOutlined){ o.toggleOutlined(); }
                this.model.addCommand(o);
                break;

            case "Triangle":
                Shape triangle = this.factory.createTriangle(centre, mouseEvent.getX(), mouseEvent.getY());
                DrawTriCmd t = new DrawTriCmd(triangle,currentColor);
                if (isOutlined){ t.toggleOutlined(); }
                this.model.addCommand(t);
                break;

            case "Squiggle":
                if(mouseEventType.equals(MouseEvent.MOUSE_PRESSED)){

                    currentSquiggle = new DrawSquigCmd(currentColor);
                    this.model.addCommand(currentSquiggle);
                    currentSquiggle.addPoint(new Point(mouseEvent.getX(), mouseEvent.getY()));

                } else if (mouseEventType.equals(MouseEvent.MOUSE_DRAGGED) && currentSquiggle != null) {

                    currentSquiggle.addPoint(new Point(mouseEvent.getX(), mouseEvent.getY()));
                    this.model.manualSetChanged();

                } else if (mouseEventType.equals(MouseEvent.MOUSE_RELEASED)) {
                    currentSquiggle = null;
                }
                break;

            case "Polyline":

                if(mouseEventType.equals(MouseEvent.MOUSE_PRESSED)){

                    if (currentPolyline == null){

                       currentPolyline = new DrawPolylineCmd(currentColor);
                       this.model.addCommand(currentPolyline);
                        if (isOutlined){ currentPolyline.toggleOutlined(); }
                       currentPolyline.addPoint(new Point(mouseEvent.getX(), mouseEvent.getY()));

                        DrawLineCmd d = new DrawLineCmd(currentColor, new Point(mouseEvent.getX(), mouseEvent.getY()));
                        this.model.addCommand(d);
                        currentLine = d;

                    }else{

                        currentPolyline.addPoint(new Point(mouseEvent.getX(), mouseEvent.getY()));
                        currentLine.UpdateStartPoint(new Point(mouseEvent.getX(), mouseEvent.getY()));
                        this.model.manualSetChanged();


                        if (currentPolyline.isComplete()){
                            currentPolyline = null;
                        }

                    }
                }


                break;

            case "Line":
                if(mouseEventType.equals(MouseEvent.MOUSE_PRESSED)){

                    DrawLineCmd d = new DrawLineCmd(currentColor, new Point(mouseEvent.getX(), mouseEvent.getY()));
                    this.model.addCommand(d);
                    currentLine = d;

                } else if (mouseEventType.equals(MouseEvent.MOUSE_DRAGGED)) {

                    currentLine.UpdateEndPoint(new Point(mouseEvent.getX(), mouseEvent.getY()));
                    this.model.manualSetChanged();

                } else if (mouseEventType.equals(MouseEvent.MOUSE_RELEASED)) {

                    currentLine.UpdateEndPoint(new Point(mouseEvent.getX(), mouseEvent.getY()));
                    currentLine = null;
                }
                break;

            case "Eraser":
                Shape eraser = this.factory.createEraser(centre, 10.0);
                this.model.addCommand(new DrawCircleCmd(eraser, Color.WHITE));
                System.out.println("Eraser");
                break;

            default:
                break;
        }

        } else {

            if (Objects.equals(this.mode, "Polyline") && mouseEventType.equals(MouseEvent.MOUSE_MOVED)
                    && currentPolyline != null){

                currentLine.UpdateEndPoint(new Point(mouseEvent.getX(), mouseEvent.getY()));

            }


            if(Objects.equals(this.mode, "Squiggle") || Objects.equals(this.mode, "Polyline")
                    || Objects.equals(this.mode, "Line")){
                this.model.manualSetChanged();
            }
            this.factory.reset();
        }
    }
    @Override
    public void update(Observable o, Object arg) {

                this.isOutlined = this.model.getIsOutlined();
                this.currentColor = this.model.getCurrentColour();

                GraphicsContext g2d = this.getGraphicsContext2D();
                g2d.setFill(Color.WHITE);
                g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
                ArrayList<DrawCommand> drawCommands = this.model.getCommands();

                // Draw Shapes and lines
                for(DrawCommand c : drawCommands){
                    c.execute(g2d);
                }


    }


}
