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
    exports david.buzas.whattoeat.entities;
    opens david.buzas.whattoeat.entities to javafx.fxml;
    exports david.buzas.whattoeat.itemmanagement;
    opens david.buzas.whattoeat.itemmanagement to javafx.fxml;
    exports david.buzas.whattoeat.repositories;
    opens david.buzas.whattoeat.repositories to javafx.fxml;
    exports david.buzas.whattoeat.controllers;
    opens david.buzas.whattoeat.controllers to javafx.fxml;
}