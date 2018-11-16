package com.myproject;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TableViewSample extends Application {

    private final ObservableList<Person> data =
            FXCollections.observableArrayList();
    private TableView<Person> table = new TableView<Person>();
    final HBox hb = new HBox();

    public static void main(String[] args) {

        ReadDates.readDates();
        ReadMass.readMass();
        launch(args);

    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new Group());
        stage.setTitle("Alpha version");
        stage.setWidth(490);
        stage.setHeight(550);


        final Label label = new Label("Body weight diary");
        label.setFont(new Font("Arial", 20));

        table.setEditable(true);

        // Date column
        TableColumn firstNameCol = new TableColumn("Date");
        firstNameCol.setMinWidth(100);
        firstNameCol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("dateValue"));

        // Body weight column
        TableColumn lastNameCol = new TableColumn("Body weight (kg)");
        lastNameCol.setMinWidth(140);
        lastNameCol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("bodyWeight"));

        // Additional info column
        TableColumn emailCol = new TableColumn("Email");
        emailCol.setMinWidth(200);
        emailCol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("email"));

        table.setItems(data);
        table.getColumns().addAll(firstNameCol, lastNameCol, emailCol);

        // Add date textfield
        final TextField addDate = new TextField();
        addDate.setMinWidth(130);
        addDate.setPromptText("Date (dd:mm:yyyy)");
        addDate.setMaxWidth(firstNameCol.getPrefWidth());

        // Add Body mass textfield
        final TextField addBodyMass = new TextField();
        addBodyMass.setMinWidth(120);
        addBodyMass.setMaxWidth(lastNameCol.getPrefWidth());
        addBodyMass.setPromptText("Body mass (kg)");

        // Add additional info textfield
        final TextField addEmail = new TextField();
        addEmail.setMaxWidth(emailCol.getPrefWidth());
        addEmail.setPromptText("Email");


        // Add values button
        final Button addButton = new Button("Add");
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addButtonMethod(event, data, addDate, addBodyMass, addEmail);
            }
        });

        // Fill columns with data
        for (int i = 0; i < ReadDates.readDates().size(); i++) {
            data.add(new Person(
                    ReadDates.readDates().get(i),
                    ReadMass.readMass().get(i),
                    "test@com"
            ));
        }

        hb.getChildren().addAll(addDate, addBodyMass, addEmail, addButton);
        hb.setSpacing(3);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table, hb);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        stage.setScene(scene);
        stage.show();
    }

    public static void addButtonMethod(ActionEvent event, ObservableList<Person> data, TextField addDate, TextField addBodyMass,
                                       TextField addEmail) {

        Pattern datePattern = Pattern.compile("^\\s*(3[01]|[12][0-9]|0?[1-9])\\.(1[012]|0?[1-9])\\.((?:19|20)\\d{2})\\s*$");
        Matcher dateMatcher = datePattern.matcher(addDate.getText());

        //TODO: Make dot in pattern optional
        Pattern weightPattern = Pattern.compile("[1-9][0-9]{0,2}(.\\d+)");
        Matcher weightMatcher = weightPattern.matcher(addBodyMass.getText());


        if (dateMatcher.matches() && weightMatcher.matches()) {
            System.out.println("matches");
            data.add(new Person(
                    addDate.getText(),
                    addBodyMass.getText() + "kg",
                    addEmail.getText()));
            addDate.clear();
            addBodyMass.clear();
            addEmail.clear();
        } else {
            Alert emptyFirstName = new Alert(Alert.AlertType.WARNING, "Warning", ButtonType.OK);
            Window owner = ((Node) event.getTarget()).getScene().getWindow();
            emptyFirstName.setContentText("Wrong input values format.\n\nDate: dd:mm:yyyy (e.g. 10.10.2018)\nBody mass: e.g. 70.0");
            emptyFirstName.initModality(Modality.APPLICATION_MODAL);
            emptyFirstName.initOwner(owner);
            emptyFirstName.showAndWait();;
        }
    }

}