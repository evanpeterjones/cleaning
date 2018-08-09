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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.*;

public class catalogUI extends Application {
    String newFilePath;
    @Override
    public void start(Stage primaryStage) {
        Label title = new Label("Catalog File (tsv only):");
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
        VBox updatePanel = new VBox();
        Label errors = new Label("...sweep away");

        updatePanel.getChildren().add(errors);
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
        //button linked to "b" for catalog file input
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    catalog k = new catalog(txt.getText());
                    newFilePath = k.run();
                    setFilePath(k.getFilePath());
                    errors.setText("File created at: " + newFilePath);
                    b.getChildren().addAll(l, input, fix);
                } catch (IOException e) {
                    errors.setText("Caught IOException: " + e);
                }
            }
        });
        //button linked to "input" for error file
        fix.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    if (newFilePath != null) {
                        importFailures temp = new importFailures(input.getText(), newFilePath);
                        errors.setText("UPCs Successfully Removed: " + newFilePath.replace(".tsv","_FIXED.tsv"));
                    } else {
                        errors.setText("Formatted catalog directory lost");
                    }
                } catch (Exception e) {
                    errors.setText("Caught Exception: " + e);
                }
            }
        });
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        root.setCenter(b);
        root.setBottom(updatePanel);
        primaryStage.getIcons().add(new Image("ecrs.png"));
        primaryStage.setTitle("ECRS Catalog Sweeper");
        primaryStage.setScene(new Scene(root, 300, 350));
        primaryStage.show();
    }
    private void setFilePath(String path) {
        newFilePath = path;
    }
    public static void main(String[] args) {
        launch(args);
    }
}