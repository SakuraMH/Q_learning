module com.example.q_learning {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires jade;
    requires jdk.jfr;

    opens com.example.q_learning to javafx.fxml;
    //exports com.example.q_learning;
    exports com.example.q_learning.SequantialFx;
    exports com.example.q_learning.smaFx;
    opens com.example.q_learning.SequantialFx to javafx.fxml;
}