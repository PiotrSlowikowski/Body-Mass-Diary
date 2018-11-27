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
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main extends Application {

    private static final ObservableList<Person> data =
            FXCollections.observableArrayList();
    private TableView<Person> table = new TableView<Person>();
    HBox hb1 = new HBox();
    HBox hb2 = new HBox();
    HBox hb3 = new HBox();
    ComboBox fileFormatComboBox = new ComboBox();
    ComboBox heightComboBox = new ComboBox();
    TextField fileNameTextField = new TextField("weight");

    public static void main(String[] args) throws Exception {

        ReadDates.readDates();
        ReadMass.readMass();
        launch(args);

    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new Group(), Color.CORNFLOWERBLUE);

        scene.getStylesheets().add("css/style.css");

        stage.setTitle("Body weight diary");
        stage.setWidth(500);
        stage.setHeight(600);

        Label label = new Label("Body weight diary");

        label.setFont(new Font("Arial", 20));
        label.setTextFill(Color.WHITE);

        Label labelAbout = new Label("More about BMI can be found here: ");

        Hyperlink hyperlink = new Hyperlink();
        hyperlink.setText("www.cdc.gov");
        hyperlink.setOnAction(event ->  {
            getHostServices().showDocument("https://www.cdc.gov/healthyweight/assessing/bmi/adult_bmi/index.html");
            } );


        hb1.getStyleClass().addAll("textfields");
        hb1.setAlignment(Pos.CENTER);
        hb1.getChildren().addAll(labelAbout, hyperlink);

        table.setEditable(true);

        // Date column
        TableColumn dateCol = new TableColumn("Date");

        dateCol.setMinWidth(100);
        dateCol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("dateValue"));
        dateCol.setCellFactory(TextFieldTableCell.forTableColumn());
        dateCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Person, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Person, String> t) {
                        ((Person) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setDateValue(t.getNewValue());
                    }
                }
        );

        // Body weight column
        TableColumn bodyWeightCol = new TableColumn("Body weight (kg)");
        bodyWeightCol.setMinWidth(140);
        bodyWeightCol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("bodyWeight"));
        bodyWeightCol.setCellFactory(TextFieldTableCell.forTableColumn());
        bodyWeightCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Person, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Person, String> t) {
                        ((Person) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setBodyWeight(t.getNewValue());
                    }
                }

        );


        // BMI column
        TableColumn bmiCol = new TableColumn("BMI Index");
        bmiCol.setMinWidth(200);
        bmiCol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("BMI"));

        table.setItems(data);
        table.getColumns().addAll(dateCol, bodyWeightCol, bmiCol);


        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        // Add date textfield
        TextField addDate = new TextField(dateFormat.format(date));
        addDate.getStyleClass().add("textfields");
        addDate.setMinWidth(130);
        addDate.setPromptText("Date (dd:mm:yyyy)");
        addDate.setMaxWidth(dateCol.getPrefWidth());

        // Add Body mass textfield
        TextField addBodyMass = new TextField();
        addBodyMass.requestFocus();
        addBodyMass.getStyleClass().add("textfields");
        addBodyMass.setMinWidth(120);
        addBodyMass.setMaxWidth(bodyWeightCol.getPrefWidth());
        addBodyMass.setPromptText("Body mass (kg)");


        // Add values button
        Button addButton = new Button("Add");
        addButton.getStyleClass().add("buttons");

        addButton.setOnAction(event ->  {
                addButtonMethod(event, data, addDate, addBodyMass, bmiCol);
        });


        // Populating combobox
        for (int i=150; i<=215; i++) {
            heightComboBox.getItems().addAll(i + " cm");
        }
        heightComboBox.getSelectionModel().select(27);
        heightComboBox.getStyleClass().add("combobox");


        Label heightLabel = new Label("Height: ");
        heightLabel.getStyleClass().add("labels");

        Label fileNameLabel = new Label("File name: ");
        fileNameLabel.getStyleClass().add("labels");

        fileNameTextField.setPromptText("File name");
        fileNameTextField.getStyleClass().add("textfields");

        fileFormatComboBox.getItems().addAll(
                ".txt",
                ".xls"
        );
        fileFormatComboBox.getStyleClass().add("combobox");
        fileFormatComboBox.getSelectionModel().selectFirst();


        // save values button
        Button saveButton = new Button("Save");
        saveButton.getStyleClass().add("buttons");
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
        exitButton.getStyleClass().add("buttons");
        exitButton.prefWidth(150);

        exitButton.setOnAction(event -> {
            stage.close();
        });


        // Fill columns with data
        for (int i = 0; i < ReadDates.readDates().size(); i++) {
            double BMI = ((Double.parseDouble((ReadMass.readMass().get(i)).substring(0,4)))/Math.pow(Double.parseDouble(heightComboBox.getValue().toString().substring(0,3))/100,2));
            String BMIAsString = String.format("%.1f", BMI).replaceAll(",", ".");

            data.add(new Person(
                    ReadDates.readDates().get(i),
                    ReadMass.readMass().get(i),
                    BMIAsString
            ));


        }

        hb2.getChildren().addAll(addDate, addBodyMass, addButton, heightLabel, heightComboBox);
        hb2.setAlignment(Pos. CENTER_LEFT);
        hb2.setSpacing(3);

        hb3.getChildren().addAll(fileNameLabel, fileNameTextField, fileFormatComboBox, saveButton);
        hb3.setAlignment(Pos. CENTER_LEFT);
        hb3.setSpacing(3);

        VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(5, 0, 0, 15));
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(label, hb1, table, hb2, hb3, exitButton);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        table.scrollTo(data.size());

        stage.setScene(scene);
        stage.show();

    }

    // Adding values to the tableView
    public void addButtonMethod(ActionEvent event, ObservableList<Person> data, TextField addDate, TextField addBodyMass, TableColumn bmiCol) {

        Pattern datePattern = Pattern.compile("^\\s*(3[01]|[12][0-9]|0?[1-9])\\.(1[012]|0?[1-9])\\.((?:19|20)\\d{2})\\s*$");
        Matcher dateMatcher = datePattern.matcher(addDate.getText());


        //TODO: Make dot in pattern optional
        Pattern weightPattern = Pattern.compile("[1-9][0-9]{0,2}(.\\d+)");
        Matcher weightMatcher = weightPattern.matcher(addBodyMass.getText());


        if (dateMatcher.matches() && weightMatcher.matches()) {

            double BMICalculation = Double.parseDouble(addBodyMass.getText().substring(0,2))/Math.pow((Double.parseDouble(heightComboBox.getValue().toString().substring(0,3)))/100,2);
            String BMICalculationToString = String.format("%.1f", BMICalculation);

            data.add(new Person(
                    addDate.getText(),
                    addBodyMass.getText().replaceAll(",", ".")+"kg",
                    BMICalculationToString.replaceAll(",", ".")
            ));
            addDate.clear();
            addBodyMass.clear();
            table.scrollTo(data.size());
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
                String text = person.getDateValue() + "\t" + person.getBodyWeight() + "\t" + person.getBMI();
                ((BufferedWriter) writer).newLine();
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