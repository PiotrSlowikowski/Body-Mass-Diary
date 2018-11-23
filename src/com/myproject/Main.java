package com.myproject;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main extends Application {

    private static final ObservableList<Person> data =
            FXCollections.observableArrayList();
    private TableView<Person> table = new TableView<Person>();
    HBox hb = new HBox();
    HBox hb2 = new HBox();
    ComboBox fileFormatComboBox = new ComboBox();
    TextField fileNameTextField = new TextField("waga");
    final double height = 1.74;

    public static void main(String[] args) throws Exception {

        ReadDates.readDates();
        ReadMass.readMass();
        ReadAdditional.readAdditional();
        launch(args);

    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new Group());
        stage.setTitle("Alpha version");
        stage.setWidth(490);
        stage.setHeight(600);



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
        TableColumn emailCol = new TableColumn("BMI Index");
        emailCol.setMinWidth(200);
        emailCol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("email"));

        table.setItems(data);
        table.getColumns().addAll(firstNameCol, lastNameCol, emailCol);


        // Add date textfield
        TextField addDate = new TextField();
        addDate.setMinWidth(130);
        addDate.setPromptText("Date (dd:mm:yyyy)");
        addDate.setMaxWidth(firstNameCol.getPrefWidth());

        // Add Body mass textfield
        TextField addBodyMass = new TextField();
        addBodyMass.setMinWidth(120);
        addBodyMass.setMaxWidth(lastNameCol.getPrefWidth());
        addBodyMass.setPromptText("Body mass (kg)");

        // Add additional info textfield
        TextField addEmail = new TextField();
        addEmail.setMaxWidth(emailCol.getPrefWidth());
        addEmail.setPromptText("BMI");


        // Add values button
        final Button addButton = new Button("Add");
        addButton.setOnAction(event ->  {
                addButtonMethod(event, data, addDate, addBodyMass, addEmail);
        });

        Label fileNameLabel = new Label("File name: ");

        fileNameTextField.setPromptText("File name");

        fileFormatComboBox.getItems().addAll(
                ".txt",
                ".xls"
        );
        fileFormatComboBox.getSelectionModel().selectFirst();


        // save values button
        final Button saveButton = new Button("Save");
        saveButton.setOnAction(event ->  {
                try {
                    if (fileFormatComboBox.getValue().equals(".xls")) {
                        writeToXls(table, fileNameTextField);
                    } else if (fileFormatComboBox.getValue().equals(".txt")){
                        writetoTxtFile(fileFormatComboBox, fileNameTextField);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

        });

        Button exitButton = new Button("Exit program");
        exitButton.prefWidth(150);

        exitButton.setOnAction(event -> {
            stage.close();
        });

        // Fill columns with data
        for (int i = 0; i < ReadDates.readDates().size(); i++) {
            String subbedString = (ReadMass.readMass().get(i)).substring(0,4);
            double weightParsed = Double.parseDouble(subbedString);
            double BMIValue = (weightParsed / Math.pow(height,2));
            String result = String.format("%.1f", BMIValue);

            data.add(new Person(
                    ReadDates.readDates().get(i),
                    ReadMass.readMass().get(i),
                    result
            ));


        }



        hb.getChildren().addAll(addDate, addBodyMass, addEmail, addButton);
        hb.setSpacing(3);

        hb2.getChildren().addAll(fileNameLabel, fileNameTextField, fileFormatComboBox, saveButton);
        hb2.setSpacing(3);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table, hb, hb2, exitButton);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);


        stage.setScene(scene);
        stage.show();

//        try {
//            writeToXls(table, fileNameTextField);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    // Adding values to the tableView
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

                    "BMI"));
            addDate.clear();
            addBodyMass.clear();
            addEmail.clear();
        } else {
            Alert emptyFirstName = new Alert(Alert.AlertType.WARNING, "Warning", ButtonType.OK);
            Window owner = ((Node) event.getTarget()).getScene().getWindow();
            emptyFirstName.setContentText("Wrong input values format.\n\nDate: dd:mm:yyyy (e.g. 10.10.2018)\nBody mass: e.g. 70.0");
            emptyFirstName.initModality(Modality.APPLICATION_MODAL);
            emptyFirstName.initOwner(owner);
            emptyFirstName.showAndWait();
        }

    }

    // Saving to txt format file
    public static void writetoTxtFile(ComboBox fileFormatComboBox, TextField fileNameTextField) throws Exception {
        Writer writer = null;
        try {
            File file = new File(fileNameTextField.getText() + fileFormatComboBox.getValue());
            writer = new BufferedWriter(new FileWriter(file));
            for (Person person : data) {
                String text = person.getDateValue() + "\t" + person.getBodyWeight() + "\t" + person.getEmail() + "\n";
                writer.write(text);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            writer.flush();
            writer.close();
        }
    }

    // Saving to xls format file
    public static void writeToXls(TableView<Person> table, TextField fileNameTextField) throws IOException {

        Workbook workbook = new HSSFWorkbook();
        Sheet spreadsheet = workbook.createSheet("sample");

        Row row = spreadsheet.createRow(0);

        for (int j = 0; j < table.getColumns().size(); j++) {
            row.createCell(j).setCellValue(table.getColumns().get(j).getText());
        }

        for (int i = 0; i < table.getItems().size(); i++) {
            row = spreadsheet.createRow(i + 1);
            for (int j = 0; j < table.getColumns().size(); j++) {
                if(table.getColumns().get(j).getCellData(i) != null) {
                    row.createCell(j).setCellValue(table.getColumns().get(j).getCellData(i).toString());
                }
                else {
                    row.createCell(j).setCellValue("");
                }
            }
        }

        FileOutputStream fileOut = new FileOutputStream(fileNameTextField.getText() + ".xls");
        workbook.write(fileOut);
        fileOut.close();

    }

}