module david.buzas.whattoeat {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires static lombok;
    requires com.fasterxml.jackson.databind;

    opens david.buzas.whattoeat to javafx.fxml;
    exports david.buzas.whattoeat;
}