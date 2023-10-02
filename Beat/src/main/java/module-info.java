module com.szoftmern.beat {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;

    requires java.sql;

    opens com.szoftmern.beat to javafx.fxml;
    exports com.szoftmern.beat;
}