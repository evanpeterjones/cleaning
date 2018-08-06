package src;

import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.*;

public class catalogUI extends Application {
    @Override
    public void start(Stage primaryStage) {
        Label title = new Label("File (tsv only):");
        Button btn = new Button();
        TextField txt = new TextField();
        Button fix = new Button();
        TextField input = new TextField();
        Label l = new Label("If Import fails, paste errors in a text file, file here...");
        txt.setText("Catalog Directory");
        btn.setText("Format Catalog");
        fix.setText("Remove Errors");
        input.setText("Error File Directory");
        VBox b = new VBox();
        b.getChildren().addAll(title,txt, btn);
        txt.setOnDragOver(new EventHandler<DragEvent>(){
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                if (db.hasString() && event.getGestureSource() != b) {
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
            }
        });
        txt.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard b = event.getDragboard();
                boolean success = false;
                if (b.hasString()) {
                    txt.setText(b.getString());
                    success=true;
                    System.out.println("Drag Successful");
                }
                event.setDropCompleted(success);
                event.consume();
            }
        });
        input.setOnDragOver(new EventHandler<DragEvent>(){
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                if (db.hasString() && event.getGestureSource() != input) {
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
            }
        });
        input.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard b = event.getDragboard();
                boolean success = false;
                if (b.hasString()) {
                    input.setText(b.getString());
                    success=true;
                    System.out.println("Drag Successful");
                }
                event.setDropCompleted(success);
                event.consume();
            }
        });
        fix.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    importFailures k = new importFailures(txt.getText(), input.getText());
                } catch (IOException e) {
                    System.err.println("Caught IOException: " + e);
                }
            }
        });
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    catalog k = new catalog(txt.getText());
                    System.out.println(k.run());
                    b.getChildren().addAll(l, input, fix);
                } catch (IOException e) {
                    System.err.println("Caught IOException: " + e);
                }
            }
        });
        StackPane root = new StackPane();
        root.getChildren().add(b);
        primaryStage.getIcons().add(new Image("ecrs.png"));
        primaryStage.setTitle("ECRS Catalog Sweeper");
        primaryStage.setScene(new Scene(root, 300, 350));
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}