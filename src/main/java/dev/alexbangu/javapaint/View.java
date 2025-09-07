package dev.alexbangu.javapaint;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Paths;

public class View implements EventHandler<ActionEvent> {

        private PaintModel paintModel;
        private PaintPanel paintPanel;
        private ShapeChooserPanel shapeChooserPanel;
        private boolean sideBarShowing = true;
        private Stage mainStage;

        public View(PaintModel model, Stage stage) {
            this.paintModel = model;
            this.mainStage = stage;

            this.paintPanel = new PaintPanel(this.paintModel);
            this.shapeChooserPanel = new ShapeChooserPanel(this);

            BorderPane root = new BorderPane();
            root.setTop(createMenuBar());
            root.setCenter(this.paintPanel);
            root.setLeft(this.shapeChooserPanel);

            //bind the canvas's width to the pane's width (minus the shapeChooserPanel width)
            paintPanel.widthProperty().bind(root.widthProperty().subtract(shapeChooserPanel.widthProperty()));
            //bind the canvas's height to the pane's height, ensuring it resizes automatically.
            paintPanel.heightProperty().bind(root.heightProperty());

            Scene scene = new Scene(root,500,500);
            stage.setScene(scene);
            stage.setTitle("Paint");
            stage.show();
        }

        public PaintModel getPaintModel() {
                return this.paintModel;
        }

        // ugly way to do this?
        public void setMode(String mode){
            this.paintPanel.setMode(mode);
        }

        public void setCurrentColor(Color z){
                this.paintModel.setCurrentColor(z);
        }

        public void toggleOutline(){ this.paintModel.toggleIsOutlined(); }


        private MenuBar createMenuBar() {

                MenuBar menuBar = new MenuBar();
                Menu menu;
                MenuItem menuItem;

                // A menu for File

                menu = new Menu("File");

                menuItem = new MenuItem("New");
                menuItem.setOnAction(this);
                menu.getItems().add(menuItem);

                menuItem = new MenuItem("Open");
                menuItem.setOnAction(this);
                menu.getItems().add(menuItem);

                menuItem = new MenuItem("Save");
                menuItem.setOnAction(this);
                menu.getItems().add(menuItem);

                menuItem = new MenuItem("Export");
                menuItem.setOnAction(this);
                menu.getItems().add(menuItem);


                menu.getItems().add(new SeparatorMenuItem());

                menuItem = new MenuItem("Exit");
                menuItem.setOnAction(this);
                menu.getItems().add(menuItem);

                menuBar.getMenus().add(menu);

                // Another menu for Edit

                menu = new Menu("Edit");

                menuItem = new MenuItem("Cut");
                menuItem.setOnAction(this);
                menu.getItems().add(menuItem);

                menuItem = new MenuItem("Copy");
                menuItem.setOnAction(this);
                menu.getItems().add(menuItem);

                menuItem = new MenuItem("Paste");
                menuItem.setOnAction(this);
                menu.getItems().add(menuItem);

                menu.getItems().add(new SeparatorMenuItem());
                menuItem = new MenuItem("Undo");
                menuItem.setOnAction(this);
                menu.getItems().add(menuItem);

                menuItem = new MenuItem("Redo");
                menuItem.setOnAction(this);
                menu.getItems().add(menuItem);

                menuBar.getMenus().add(menu);

                // Third menu for "Tools"

                menu = new Menu("Tools");

                menuItem = new MenuItem("Toggle Sidebar");
                menuItem.setOnAction(this);
                menu.getItems().add(menuItem);

                String[] tools = { "Circle", "Rectangle", "Square", "Squiggle", "Polyline", "Oval", "Triangle",
                        "Line", "Eraser" };

                for (String t : tools) {
                        menuItem = new MenuItem(t);
                        menuItem.setOnAction(this);
                        menu.getItems().add(menuItem);
                }

                menuBar.getMenus().add(menu);

                return menuBar;
        }


        @Override
        public void handle(ActionEvent event) {
                System.out.println(((MenuItem) event.getSource()).getText());
                String command = ((MenuItem) event.getSource()).getText();
                System.out.println(command);
                shapeChooserPanel.toggleButtonManually(command);

                if (command.equals("Exit")) {
                        Platform.exit();
                }

                if (command.equals("New")) {
                        this.paintModel.clearCanvas();

                }else if (command.equals("Save")){
                        this.paintModel.SaveCurrentCanvas();

                } else if (command.equals("Export")) {
                        this.paintPanel.ExportToPNG();

                } else if (command.equals("Open")){
                        FileChooser fileChooser = new FileChooser();
                        fileChooser.setTitle("Open Canvas File");

                        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)",
                                "*.txt");
                        fileChooser.getExtensionFilters().add(extFilter);

                        File initialDirectory = new File(Paths.get("output_files").toString());
                        fileChooser.setInitialDirectory(initialDirectory);

                        File file = fileChooser.showOpenDialog(mainStage);
                        if (file != null) {
                                this.paintModel.readFromFile(file.getPath());
                        }

                // What you're about to look at is the ugliest code i've ever written

                }else if (command.equals("Undo")) {
                        DrawCommand toUndo = this.paintModel.removeCommand();
                        if (DrawLineCmd.class.isInstance(toUndo)) {
                                this.paintModel.addToRedo(toUndo);
                                toUndo = this.paintModel.removeCommand();
                                if (DrawPolylineCmd.class.isInstance(toUndo)) {
                                        this.paintModel.addToRedo(toUndo);
                                } else {
                                        this.paintModel.addCommand(toUndo);
                                }
                        } else if (toUndo != null) {
                                this.paintModel.addToRedo(toUndo);
                                DrawCommand redoCheck = this.paintModel.removeCommand();
                                if (redoCheck.toString().equals(toUndo.toString())) { // yup, checking equality by checking their toString strings are the same.
                                        while (redoCheck.toString().equals(toUndo.toString())) {
                                                redoCheck = this.paintModel.removeCommand();
                                        }
                                }
                                this.paintModel.addCommand(redoCheck);
                        }
                } else if (command.equals("Redo")) {
                        DrawCommand toRedo = this.paintModel.removeFromRedo();
                        if (toRedo != null) {
                                this.paintModel.addCommand(toRedo);
                        }
                        if(DrawLineCmd.class.isInstance(toRedo)) {
                                DrawCommand polyCheck = this.paintModel.removeFromRedo();
                                if(polyCheck != null && DrawPolylineCmd.class.isInstance(polyCheck)) {
                                        this.paintModel.addCommand(polyCheck);
                                }
                        }
                }else if (command.equals("Toggle Sidebar")) {
                        this.sideBarShowing = !this.sideBarShowing;

                        if (this.sideBarShowing) {
                                this.shapeChooserPanel.showHide(false);
                        } else {
                                this.shapeChooserPanel.showHide(true);
                        }
                }else{
                        setMode(command);
                }
        }

}
