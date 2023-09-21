module com.biblio {
    requires java.sql;
    requires lombok;
    requires jBCrypt;
    requires mysql.connector.java;
    requires java.desktop;
    requires jcalendar;

    opens com.biblio to javafx.fxml;
    exports com.biblio;
}