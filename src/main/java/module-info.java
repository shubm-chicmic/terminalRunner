module com.example.terminalrunner {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.desktop; // Add this line to require the java.desktop module
//    requires htmlunit; // Add the HtmlUnit library here

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;
    requires org.jsoup;
    requires org.json;

    requires com.jfoenix;
    requires jdk.jsobject;
    requires freetts;
    requires sphinx4.core;
//    requires org.hildan.fxgson;




    opens com.example.terminalrunner to javafx.fxml;
    exports com.example.terminalrunner;
}