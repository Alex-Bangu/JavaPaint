package dev.alexbangu.javapaint;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class ShapeChooserPanel extends GridPane implements EventHandler<ActionEvent> {

        private View view;
        private final ColorPicker colorPicker = new ColorPicker();
        private final CheckBox isOutlined = new CheckBox("Outline");
        ArrayList<ToggleButton> buttonList = new ArrayList<ToggleButton>();

        /**
         * Manually the toggles the button with text equivalent to the
         * given text.
         *
         * @param buttonTextIn string of the button to be toggled
         */
        public void toggleButtonManually(String buttonTextIn){
                for (ToggleButton button : buttonList) {
                        if (button.getText().equals(buttonTextIn)){
                                button.setSelected(true);
                                button.requestFocus();
                        }
                }

        }

        //This adds a listener which changes the colour of toggle buttons when it observes
        //their selectedProperty change (when they are selected or unselected).
        ChangeListener<Boolean> toggleChangeListener = new ChangeListener<>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

                        //This gets the toggle button object itself from the property using getBean
                        ToggleButton source = (ToggleButton) ((ReadOnlyBooleanProperty)observable).getBean();

                        //If newValue is true the button is selected so we change its colours, otherwise
                        //we give it an empty string which resets the colours back to its default
                        if (newValue) {
                                source.setStyle("-fx-background-color: #000000; -fx-text-fill: white;");
                        } else {
                                source.setStyle("");
                        }
                }
        };

        public ShapeChooserPanel(View view) {

                isOutlined.setOnAction(this);
                this.add(isOutlined,0,0);

                colorPicker.setValue(Color.GREEN);
                colorPicker.setOnAction(this);
                this.add(colorPicker,0,1);

                this.view = view;

                String[] buttonLabels = { "Circle", "Rectangle", "Square", "Squiggle", "Polyline", "Oval", "Triangle",
                        "Line", "Eraser"};
                ToggleGroup group = new ToggleGroup();

                int row = 2;
                for (String label : buttonLabels) {
                        String filename = "file:src/main/java/ca/utoronto/utm/assignment2/paint/icons/" + label +".bmp";
                        Image icon = new Image(filename);


                        if (icon.isError()) {System.out.println("bruh");}

                        ImageView imageView = new ImageView(icon);
                        // imageView.setPreserveRatio(true);

                        imageView.setFitHeight(20);
                        imageView.setFitWidth(20);

                        ToggleButton button = new ToggleButton(label, imageView);

                        buttonList.add(button);
                        button.setToggleGroup(group);

                        //Adds a toggle change listener to each button
                        button.selectedProperty().addListener(toggleChangeListener);

                        //This sets the default tool selected
                        if (label.equals("Circle")){button.setSelected(true);}

                        button.setGraphic(imageView);

                        button.setMinWidth(100);
                        this.add(button, 0, row);
                        row++;
                        button.setOnAction(this);
                }
        }

        @Override
        public void handle(ActionEvent event) {

                if(event.getSource() == colorPicker){

                        this.view.setCurrentColor(colorPicker.getValue());

                } else if (event.getSource() == isOutlined) {

                        this.view.toggleOutline();

                } else {
                        String command = ((ToggleButton) event.getSource()).getText();

                        //Ensures that one button is always selected
                        ((ToggleButton) event.getSource()).setSelected(true);

                        view.setMode(command);
                        System.out.println(command);
                }
        }

        public void showHide(boolean hidden) {
                if (hidden) {
                        for (Node child : this.getChildren()) {
                                child.setManaged(false);
                                child.setVisible(false);
                        }
                        this.setManaged(false);
                } else {
                        this.setManaged(true);
                        for (Node child : this.getChildren()) {
                                child.setManaged(true);
                                child.setVisible(true);
                        }
                }
        }
}


