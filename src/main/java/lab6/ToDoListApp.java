package lab6;

import javafx.application.Application;
import javafx.application.Platform;
//import javafx.event.ActionEvent;
//import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * A simple to do list app
 **/
// The ToDoListApp is a subclass of the Application class, which allows ToDoListApp to inherit all the methods and fields of the Application class
// which allowed us to override the start method of the Application class

// The Application class is an abstract class that provides the framework for a JavaFX application
// It's like a template that we can use to create our own JavaFX application
public class ToDoListApp extends Application {
    private ListView<String> listView = new ListView<>(); // instance variable listView

    /**
        * Save the items in the list view to a CSV file
        * @param listView the list view to save
        */
    private void saveToCSV(ListView<String> listView) {
        try (FileWriter writer = new FileWriter("todolist.csv")) {
            for (String item : listView.getItems()) {
                writer.write(item + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
        * Load the items from a CSV file to the list view
        */
    private void loadFromCSV() {
        File file = new File("todolist.csv");
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))){
                String line;
                while ((line = reader.readLine()) != null) {
                    listView.getItems().add(line);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
        * The main entry point for all JavaFX applications
        * @param primaryStage the primary stage for this application, onto which the application scene can be set
        */
    // Overriding the start method of the Application class, which is an abstract method
    // By overriding the start method, we can define the UI of the app and how it should behave
    @Override
    public void start(Stage primaryStage) {

        // These are subclasses of the Node class, which is a superclass of all JavaFX nodes
        // TextFields, buttons, labels, etc. are all nodes
        // Which allows me to call node methods on them (polymorphism), i.e. setStyle, setPadding, etc.
        // setStyle is a generic method that allows me to set the style of the node and its subclasses
        TextField inputField = new TextField();
        Button addButton = new Button("Add");
        Button deleteButton = new Button("Delete");
        Button markAsDoneButton = new Button("Mark as done");
        Label statusLabel = new Label("Current Time: ");
        listView = new ListView<>();
        loadFromCSV();

        // Add button action, override the handle method of the EventHandler interface
        // EventHandler is an interface
        // allows us to handle events like button clicks, mouse clicks, key presses, etc.
//        addButton.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                String text = inputField.getText().trim();
//                if (!text.isEmpty()) {
//                    listView.getItems().add(text);
//                    inputField.clear();
//                }
//            }
//        });
        addButton.setOnAction(event -> {
            String text = inputField.getText().trim();
            if (!text.isEmpty()) {
                listView.getItems().add(text);
                inputField.clear();
            }
        });

        // Delete button action, delete the selected item from the list
//        deleteButton.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                int index = listView.getSelectionModel().getSelectedIndex();
//                if (index >= 0) {
//                    listView.getItems().remove(index);
//                }
//            }
//        });

        deleteButton.setOnAction(event -> {
            int index = listView.getSelectionModel().getSelectedIndex();
            if (index >= 0) {
                listView.getItems().remove(index);
            }
        });

        // Mark as done button action, mark (toggle) the selected item as done
//        markAsDoneButton.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                int index = listView.getSelectionModel().getSelectedIndex();
//                if (index >= 0) {
//                    String item = listView.getItems().get(index);
//                    if(!item.endsWith(" (done)")) {
//                        listView.getItems().set(index, item + " (done)");
//                    } else {
//                        listView.getItems().set(index, item.substring(0, item.length() - 7));
//                    }
//
//                }
//            }
//        });

        markAsDoneButton.setOnAction(event -> {
            int index = listView.getSelectionModel().getSelectedIndex();
            if (index >= 0) {
                String item = listView.getItems().get(index);
                if(!item.endsWith(" (done)")) {
                    listView.getItems().set(index, item + " (done)");
                } else {
                    listView.getItems().set(index, item.substring(0, item.length() - 7));
                }
            }
        });

        // Layout Pane
        VBox root = new VBox();
        HBox buttons = new HBox();

        buttons.getChildren().addAll(addButton, deleteButton, markAsDoneButton);
        root.getChildren().addAll(inputField, buttons, listView, statusLabel);

        Thread timeThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(() -> {
                    statusLabel.setText("Current Time: " + LocalTime.now().format(DateTimeFormatter.ofPattern("h:mm:ss a")));
                });
            }
        });

        // Some Styling
        root.setStyle("-fx-padding: 10px");
        root.setSpacing(5);
        buttons.setStyle("-fx-spacing: 10px");
        buttons.setPadding(new javafx.geometry.Insets(10));
        listView.setStyle("-fx-font-size: 16px");

        Scene scene = new Scene(root, 400, 400);
        primaryStage.setTitle("To Do List App");
        primaryStage.setScene(scene);
        timeThread.start();
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> saveToCSV(listView)); // Save the list when the app is closed


    }
}


