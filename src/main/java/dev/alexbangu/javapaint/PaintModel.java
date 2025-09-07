package dev.alexbangu.javapaint;

import javafx.scene.paint.Color;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Observable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PaintModel extends Observable {
        private final ArrayList<DrawCommand> drawCommands = new ArrayList<>();
        private final ArrayList<DrawCommand> toRedo = new ArrayList<>();
        private Color currentColor = Color.GREEN;
        private Boolean isOutlined = false;

        public static String generateUniqueFileName(String directory) {

                String timestamp = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
                String fileName = "Canvas " + timestamp + ".txt";
                return Paths.get(directory, fileName).toString();
        }

        @SuppressWarnings("CallToPrintStackTrace")
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

        private boolean validFileCheck(Matcher matcher){
            if (!matcher.find()){
                System.out.println("ERROR - Non-command found, file may not load correctly");
                return false;
            }
            return true;
        }

        @SuppressWarnings({"ResultOfMethodCallIgnored", "CallToPrintStackTrace"})
        public void readFromFile(String filePathIn) {

                this.clearCanvas();

                ShapeFactory factory = new ShapeFactory();

                Path filePath = Path.of(filePathIn);

                try (BufferedReader reader = Files.newBufferedReader(filePath)) {

                        Pattern pattern = Pattern.compile("([^|]*)\\|");

                        String line;
                        while ((line = reader.readLine()) != null) {
                                factory.reset();


                                Matcher matcher = pattern.matcher(line);

                                if (!validFileCheck(matcher)){break;}
                                String command = matcher.group(1);


                            switch (command) {
                                case "crlc" -> {

                                    if (!validFileCheck(matcher)){break;}
                                    boolean outLined = Boolean.parseBoolean(matcher.group(1));

                                    if (!validFileCheck(matcher)){break;}
                                    Color z = Color.web(matcher.group(1).substring(2));

                                    if (!validFileCheck(matcher)){break;}
                                    double r = Double.parseDouble(matcher.group(1));
                                    if (!validFileCheck(matcher)){break;}
                                    double x = Double.parseDouble(matcher.group(1));
                                    if (!validFileCheck(matcher)){break;}
                                    double y = Double.parseDouble(matcher.group(1));


                                    Point p = new Point(x, y);
                                    Shape circle = new Circle(p, r);
                                    DrawCircleCmd c = new DrawCircleCmd(circle, z);
                                    if (outLined) {
                                        c.toggleOutlined();
                                    }
                                    this.addCommand(c);


                                }
                                case "line" -> {

                                    if (!validFileCheck(matcher)){break;};
                                    Color z = Color.web(matcher.group(1).substring(2));
                                    if (!validFileCheck(matcher)){break;};
                                    double x1 = Double.parseDouble(matcher.group(1));
                                    if (!validFileCheck(matcher)){break;};
                                    double y1 = Double.parseDouble(matcher.group(1));
                                    if (!validFileCheck(matcher)){break;};
                                    double x2 = Double.parseDouble(matcher.group(1));
                                    if (!validFileCheck(matcher)){break;};
                                    double y2 = Double.parseDouble(matcher.group(1));

                                    Point p1 = new Point(x1, y1);
                                    Point p2 = new Point(x2, y2);

                                    DrawLineCmd d = new DrawLineCmd(z, p1);
                                    d.UpdateEndPoint(p2);
                                    this.addCommand(d);


                                }
                                case "sqig" -> {

                                    if (!validFileCheck(matcher)){break;};
                                    Color z = Color.web(matcher.group(1).substring(2));

                                    DrawSquigCmd q = new DrawSquigCmd(z);

                                    if (!validFileCheck(matcher)){break;};
                                    double s = Double.parseDouble(matcher.group(1));

                                    for (int i = 0; i < s; i++) {
                                        if (!validFileCheck(matcher)){break;};
                                        double x = Double.parseDouble(matcher.group(1));
                                        if (!validFileCheck(matcher)){break;};
                                        double y = Double.parseDouble(matcher.group(1));
                                        q.addPoint(new Point(x,y));

                                    }

                                    this.addCommand(q);


                                }
                                case "poly" -> {

                                    if (!validFileCheck(matcher)){break;};
                                    Color z = Color.web(matcher.group(1).substring(2));

                                    DrawPolylineCmd p = new DrawPolylineCmd(z);

                                    if (!validFileCheck(matcher)){break;};
                                    if (Boolean.parseBoolean(matcher.group(1))){p.toggleOutlined();}

                                    if (!validFileCheck(matcher)){break;};
                                    double s = Double.parseDouble(matcher.group(1));

                                    for (int i = 0; i < s; i++) {
                                        if (!validFileCheck(matcher)){break;};
                                        double x = Double.parseDouble(matcher.group(1));
                                        if (!validFileCheck(matcher)){break;};
                                        double y = Double.parseDouble(matcher.group(1));
                                        p.addPoint(new Point(x,y));

                                    }

                                    this.addCommand(p);


                                }
                                case "oval" -> {

                                    if (!validFileCheck(matcher)){break;};
                                    boolean outLined = Boolean.parseBoolean(matcher.group(1));
                                    if (!validFileCheck(matcher)){break;};
                                    Color z = Color.web(matcher.group(1).substring(2));
                                    if (!validFileCheck(matcher)){break;};
                                    double x1 = Double.parseDouble(matcher.group(1));
                                    if (!validFileCheck(matcher)){break;};
                                    double y1 = Double.parseDouble(matcher.group(1));
                                    if (!validFileCheck(matcher)){break;};
                                    double x2 = Double.parseDouble(matcher.group(1));
                                    if (!validFileCheck(matcher)){break;};
                                    double y2 = Double.parseDouble(matcher.group(1));


                                    Point p1 = new Point(x1, y1);
                                    Point p2 = new Point(x2, y2);

                                    Shape oval = factory.createOval(p1, p2.x, p2.y);
                                    DrawOvalCmd o = new DrawOvalCmd(oval, z);
                                    if (outLined) {
                                        o.toggleOutlined();
                                    }
                                    this.addCommand(o);


                                }
                                case "rect" -> {

                                    if (!validFileCheck(matcher)){break;};
                                    boolean outLined = Boolean.parseBoolean(matcher.group(1));

                                    if (!validFileCheck(matcher)){break;};
                                    Color z= Color.web(matcher.group(1).substring(2));

                                    if (!validFileCheck(matcher)){break;};
                                    double x1 = Double.parseDouble(matcher.group(1));
                                    if (!validFileCheck(matcher)){break;};
                                    double y1 = Double.parseDouble(matcher.group(1));
                                    if (!validFileCheck(matcher)){break;};
                                    double x2 = Double.parseDouble(matcher.group(1));
                                    if (!validFileCheck(matcher)){break;};
                                    double y2 = Double.parseDouble(matcher.group(1));


                                    Point p1 = new Point(x1, y1);

                                    Shape rect = factory.createRectangle(p1, x2, y2);
                                    DrawRectCmd r = new DrawRectCmd(rect, z);
                                    if (outLined) {
                                        r.toggleOutlined();
                                    }
                                    this.addCommand(r);


                                }
                                case "sqre" -> {

                                    if (!validFileCheck(matcher)){break;};
                                    boolean outLined = Boolean.parseBoolean(matcher.group(1));

                                    if (!validFileCheck(matcher)){break;};
                                    Color z= Color.web(matcher.group(1).substring(2));

                                    if (!validFileCheck(matcher)){break;};
                                    double x1 = Double.parseDouble(matcher.group(1));
                                    if (!validFileCheck(matcher)){break;};
                                    double y1 = Double.parseDouble(matcher.group(1));
                                    if (!validFileCheck(matcher)){break;};
                                    double x2 = Double.parseDouble(matcher.group(1));
                                    if (!validFileCheck(matcher)){break;};
                                    double y2 = Double.parseDouble(matcher.group(1));


                                    Point p1 = new Point(x1, y1);

                                    Shape square = factory.createSquare(p1, x2, y2);
                                    DrawSquareCmd r = new DrawSquareCmd(square, z);
                                    if (outLined) {
                                        r.toggleOutlined();
                                    }
                                    this.addCommand(r);


                                }
                                case "tria" -> {

                                    if (!validFileCheck(matcher)){break;};
                                    boolean outLined = Boolean.parseBoolean(matcher.group(1));

                                    if (!validFileCheck(matcher)){break;};
                                    Color z= Color.web(matcher.group(1).substring(2));

                                    if (!validFileCheck(matcher)){break;};
                                    double x1 = Double.parseDouble(matcher.group(1));
                                    if (!validFileCheck(matcher)){break;};
                                    double y1 = Double.parseDouble(matcher.group(1));
                                    if (!validFileCheck(matcher)){break;};
                                    double x2 = Double.parseDouble(matcher.group(1));
                                    if (!validFileCheck(matcher)){break;};
                                    double y2 = Double.parseDouble(matcher.group(1));


                                    Point p1 = new Point(x1, y1);

                                    Shape tri = factory.createTriangle(p1, x2, y2);
                                    DrawTriCmd r = new DrawTriCmd(tri, z);
                                    if (outLined) {
                                        r.toggleOutlined();
                                    }
                                    this.addCommand(r);


                                }
                            }
                        }

                    reader.close();

                } catch (IOException e) {
                        e.printStackTrace();
                }
        }


        @SuppressWarnings("CallToPrintStackTrace")
        public void SaveCurrentCanvas(){
                String directory = "output_files";

                createDirectory(directory);

                String fileName = generateUniqueFileName(directory);

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {

                        for (DrawCommand d: drawCommands){
                                writer.append(d.toString()).append("\n");
                        }

                        writer.close();

                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

        /**
         * Clears all shapes and line from the canvas.
         */
        public void clearCanvas(){
                drawCommands.clear();
                manualSetChanged();
        }

        /**
         * Manual updates the observers without having
         * to add clutter to the canvas.
         * (This was previous done by adding a dot to the corner).
         */
        public void manualSetChanged(){
                this.setChanged();
                this.notifyObservers();
        }

        /**
         * Adds the given command to the commands list.
         * Used for adding shapes and lines to the canvas.
         *
         * @param c, the command to be added
         */
        public void addCommand(DrawCommand c){
                this.drawCommands.add(c);
                manualSetChanged();
        }

        /**
         * Removes the latest command added to the commands list.
         * Used when the redo button is used
         *
         * @return cmd, the draw command being removed
         */
        public DrawCommand removeCommand() {
            if (this.drawCommands.size() > 0){
                DrawCommand cmd = this.drawCommands.remove(drawCommands.size() - 1);
                manualSetChanged();
                System.out.println(cmd.toString());
                return cmd;
            }
            else {
                return null;
            }
        }

        public void addToRedo(DrawCommand cmd){
            this.toRedo.add(cmd);
            System.out.println(this.toRedo);
            System.out.println(this.drawCommands);
            manualSetChanged();
        }

        public DrawCommand removeFromRedo(){
            if (this.toRedo.isEmpty()){
                return null;
            }
            DrawCommand cmd = this.toRedo.remove(this.toRedo.size() - 1);
            manualSetChanged();
            return cmd;
        }

        /**
         * Sets the current color to the given one.
         *
         * @param z the colour which the current one will be set to
         */
        public void setCurrentColor(Color z){
                this.currentColor = z;
                manualSetChanged();
        }

        public void toggleIsOutlined(){
                this.isOutlined = !this.isOutlined;
                manualSetChanged();
        }

        /**
         * Returns the list of all commands currently in the model.
         *
         * @return the list of all commands
         */
        public ArrayList<DrawCommand> getCommands(){
                return this.drawCommands;
        }

        /**
         * Returns the current colour of shapes to be drawn.
         *
         * @return the current colour
         */
        public Color getCurrentColour(){
                return this.currentColor;
        }

        /**
         * @return whether the shapes are being drawn as outlines
         */
        public Boolean getIsOutlined(){
                return this.isOutlined;
        }
}
