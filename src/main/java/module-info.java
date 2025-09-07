module dev.alexbangu.javapaint {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens dev.alexbangu.javapaint to javafx.fxml;
    exports dev.alexbangu.javapaint;
}