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
    requires lombok;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires commons.validator;
    requires jbcrypt;
    requires jakarta.mail;

    opens com.szoftmern.beat to javafx.fxml, org.hibernate.orm.core;
    exports com.szoftmern.beat;
}