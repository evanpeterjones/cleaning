package src;

import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Pos;

public class catalogUI extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("ECRS Catalog Sweeper");
        Button btn = new Button();
        TextField txt = new TextField();
        txt.setText("Catalog Directory");
        txt.setAlignment(Pos.CENTER_LEFT);
        btn.setText("Sort Catalog");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    catalog k = new catalog(txt.getText());
                    System.out.println(k.run());
                } catch (IOException e) {
                    System.err.println("Caught IOException: " + e);
                }
            }
        });
        StackPane root = new StackPane();
        root.getChildren().addAll(txt,btn);
        VBox b = new VBox(2);
        b.getChildren().addAll(txt, btn);
        primaryStage.setScene(new Scene(b, 300, 350));
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}