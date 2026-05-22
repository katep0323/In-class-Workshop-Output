package com.example.todoapp;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.control.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.beans.property.SimpleStringProperty;

import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.example.todoapp.utils.DatabaseConnection;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {

        VBox root = new VBox(20);

        root.setPadding(new Insets(40));
        root.setAlignment(Pos.CENTER);

        root.setStyle(
                "-fx-background-color: #f4f4f4;"
        );

        VBox loginCard = new VBox(15);

        loginCard.setAlignment(Pos.CENTER);

        loginCard.setPadding(new Insets(35));

        loginCard.setMaxWidth(380);

        loginCard.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 15;" +
                        "-fx-border-radius: 15;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 12,0,0,4);"
        );

        Label title = new Label("Task Tracker Login");

        title.setStyle(
                "-fx-font-size: 28px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #222;"
        );

        Label subtitle =
                new Label("Manage your daily tasks easily");

        subtitle.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-text-fill: #666;"
        );

        TextField username = new TextField();

        username.setPromptText("Username");

        username.setPrefHeight(42);

        username.setMaxWidth(300);

        username.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-background-radius: 8;"
        );

        PasswordField password = new PasswordField();

        password.setPromptText("Password");

        password.setPrefHeight(42);

        password.setMaxWidth(300);

        password.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-background-radius: 8;"
        );

        Button loginButton = new Button("Login");

        loginButton.setPrefWidth(300);

        loginButton.setPrefHeight(42);

        loginButton.setStyle(
                "-fx-background-color: #1E88E5;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8;"
        );

        loginCard.getChildren().addAll(
                title,
                subtitle,
                username,
                password,
                loginButton
        );

        root.getChildren().add(loginCard);

        loginButton.setOnAction(e -> {

            HBox dashboard = new HBox(25);

            dashboard.setPadding(new Insets(25));

            dashboard.setStyle(
                    "-fx-background-color: #f4f4f4;"
            );

            VBox leftPanel = new VBox(15);

            leftPanel.setPrefWidth(280);

            VBox rightPanel = new VBox(10);

            Label dashboardTitle =
                    new Label("Task Tracker Dashboard");

            dashboardTitle.setStyle(
                    "-fx-font-size: 30px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-text-fill: #222;"
            );

            TextField taskField = new TextField();

            taskField.setPromptText("Enter Task Name");

            taskField.setPrefHeight(40);

            taskField.setStyle(
                    "-fx-font-size: 14px;" +
                            "-fx-background-radius: 8;"
            );

            ComboBox<String> statusBox = new ComboBox<>();

            statusBox.getItems().addAll(
                    "Pending",
                    "In Progress",
                    "Completed"
            );

            statusBox.setPromptText("Select Status");

            statusBox.setPrefHeight(40);

            statusBox.setStyle(
                    "-fx-font-size: 14px;"
            );

            DatePicker datePicker = new DatePicker();

            datePicker.setPrefHeight(40);

            datePicker.setStyle(
                    "-fx-font-size: 14px;"
            );

            TableView<String[]> table = new TableView<>();

            TableColumn<String[], String> idColumn =
                    new TableColumn<>("ID");

            idColumn.setCellValueFactory(data ->
                    new SimpleStringProperty(
                            data.getValue()[0]
                    )
            );

            TableColumn<String[], String> titleColumn =
                    new TableColumn<>("Task");

            titleColumn.setCellValueFactory(data ->
                    new SimpleStringProperty(
                            data.getValue()[1]
                    )
            );

            TableColumn<String[], String> dueDateColumn =
                    new TableColumn<>("Due Date");

            dueDateColumn.setCellValueFactory(data ->
                    new SimpleStringProperty(
                            data.getValue()[2]
                    )
            );

            TableColumn<String[], String> statusColumn =
                    new TableColumn<>("Status");

            statusColumn.setCellValueFactory(data ->
                    new SimpleStringProperty(
                            data.getValue()[3]
                    )
            );

            table.getColumns().addAll(
                    idColumn,
                    titleColumn,
                    dueDateColumn,
                    statusColumn
            );

            idColumn.setPrefWidth(60);
            titleColumn.setPrefWidth(220);
            dueDateColumn.setPrefWidth(180);
            statusColumn.setPrefWidth(180);

            table.setPrefWidth(700);

            table.setPrefHeight(520);

            table.setStyle(
                    "-fx-font-size: 14px;"
            );

            Button addButton =
                    new Button("Add Task");

            Button deleteButton =
                    new Button("Delete Task");

            addButton.setPrefWidth(250);

            addButton.setPrefHeight(42);

            deleteButton.setPrefWidth(250);

            deleteButton.setPrefHeight(42);

            addButton.setStyle(
                    "-fx-background-color: #16a34a;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 15px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-radius: 8;"
            );

            deleteButton.setStyle(
                    "-fx-background-color: #dc2626;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 15px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-radius: 8;"
            );

            ObservableList<String[]> data =
                    FXCollections.observableArrayList();

            Runnable loadTable = () -> {

                try {

                    data.clear();

                    Connection connection =
                            DatabaseConnection.connect();

                    String sql =
                            "SELECT * FROM tasks ORDER BY id ASC";

                    PreparedStatement preparedStatement =
                            connection.prepareStatement(sql);

                    ResultSet resultSet =
                            preparedStatement.executeQuery();

                    while (resultSet.next()) {

                        data.add(new String[]{

                                resultSet.getString("id"),

                                resultSet.getString("title"),

                                resultSet.getString("description"),

                                resultSet.getString("status")
                        });
                    }

                    table.setItems(data);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            };

            loadTable.run();

            addButton.setOnAction(event -> {

                try {

                    Connection connection =
                            DatabaseConnection.connect();

                    String sql =
                            "INSERT INTO tasks(title, description, status) VALUES (?, ?, ?)";

                    PreparedStatement preparedStatement =
                            connection.prepareStatement(sql);

                    preparedStatement.setString(
                            1,
                            taskField.getText()
                    );

                    preparedStatement.setString(
                            2,
                            datePicker.getValue().toString()
                    );

                    preparedStatement.setString(
                            3,
                            statusBox.getValue()
                    );

                    preparedStatement.executeUpdate();

                    taskField.clear();

                    statusBox.setValue(null);

                    datePicker.setValue(null);

                    loadTable.run();

                    System.out.println("Task Added");

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            deleteButton.setOnAction(event -> {

                try {

                    String[] selectedTask =
                            table.getSelectionModel()
                                    .getSelectedItem();

                    if (selectedTask == null) {

                        System.out.println("No task selected");
                        return;
                    }

                    Connection connection =
                            DatabaseConnection.connect();

                    String sql =
                            "DELETE FROM tasks WHERE id=?";

                    PreparedStatement preparedStatement =
                            connection.prepareStatement(sql);

                    preparedStatement.setInt(
                            1,
                            Integer.parseInt(selectedTask[0])
                    );

                    preparedStatement.executeUpdate();

                    loadTable.run();

                    System.out.println("Task Deleted");

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            leftPanel.getChildren().addAll(

                    dashboardTitle,

                    new Label("Task Name"),
                    taskField,

                    new Label("Task Status"),
                    statusBox,

                    new Label("Due Date"),
                    datePicker,

                    addButton,
                    deleteButton
            );

            rightPanel.getChildren().addAll(

                    new Label("Recorded Tasks"),
                    table
            );

            dashboard.getChildren().addAll(
                    leftPanel,
                    rightPanel
            );

            Scene dashboardScene =
                    new Scene(dashboard, 1150, 680);

            stage.setScene(dashboardScene);
        });

        Scene scene =
                new Scene(root, 500, 500);

        stage.setTitle("Todo App");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}