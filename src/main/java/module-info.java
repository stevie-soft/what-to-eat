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
    requires com.fasterxml.jackson.datatype.jsr310;

    opens david.buzas.whattoeat to javafx.fxml;
    exports david.buzas.whattoeat;
    exports david.buzas.whattoeat.entities;
    opens david.buzas.whattoeat.entities to javafx.fxml;
    exports david.buzas.whattoeat.utils.itemmanager;
    opens david.buzas.whattoeat.utils.itemmanager to javafx.fxml;
    exports david.buzas.whattoeat.repositories;
    opens david.buzas.whattoeat.repositories to javafx.fxml;
    exports david.buzas.whattoeat.ui.controllers;
    opens david.buzas.whattoeat.ui.controllers to javafx.fxml;
    exports david.buzas.whattoeat.ui.components;
    opens david.buzas.whattoeat.ui.components to javafx.fxml;
    exports david.buzas.whattoeat.states;
    opens david.buzas.whattoeat.states to javafx.fxml;
}