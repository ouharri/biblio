module com.biblio {
    requires javafx.controls;
    requires javafx.fxml;
        requires javafx.web;
            
        requires org.controlsfx.controls;
            requires com.dlsc.formsfx;
            requires net.synedra.validatorfx;
            requires org.kordamp.ikonli.javafx;
            requires org.kordamp.bootstrapfx.core;
//            requires eu.hansolo.tilesfx;
    requires java.sql;
    requires lombok;
    requires jBCrypt;
    requires mysql.connector.java;

    opens com.biblio to javafx.fxml;
    exports com.biblio;
}