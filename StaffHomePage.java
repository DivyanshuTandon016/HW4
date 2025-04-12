package application;

import databasePart1.DatabaseHelper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

public class StaffHomePage {

    private final DatabaseHelper databaseHelper;

    public StaffHomePage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void show(Stage primaryStage) {
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(20));

        Label title = new Label("Staff Dashboard");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        layout.setTop(title);
        BorderPane.setAlignment(title, Pos.CENTER);

        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(
                viewUnansweredQuestionsTab(),
                flagInappropriateTab(),
                leaveFeedbackTab()
        );

        layout.setCenter(tabPane);

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> new SetupLoginSelectionPage(databaseHelper).show(primaryStage));
        VBox bottomBox = new VBox(logoutButton);
        bottomBox.setAlignment(Pos.CENTER);
        layout.setBottom(bottomBox);

        Scene scene = new Scene(layout, 800, 500);
        primaryStage.setTitle("Staff Home Page");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Tab viewUnansweredQuestionsTab() {
        Tab tab = new Tab("Unanswered Questions");
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        Label label = new Label("Questions without answers:");
        ListView<String> questionList = new ListView<>();

        try {
            List<String[]> questions = databaseHelper.getAllQuestions();
            List<String[]> answers = databaseHelper.getAllAnswers();

            for (String[] q : questions) {
                boolean isAnswered = answers.stream().anyMatch(a -> a[0].equals(q[0]));
                if (!isAnswered) {
                    questionList.getItems().add("Q" + q[0] + ": " + q[1]);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        layout.getChildren().addAll(label, questionList);
        tab.setContent(layout);
        return tab;
    }

    private Tab flagInappropriateTab() {
        Tab tab = new Tab("Flag Inappropriate");
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        Label label = new Label("Select a question to flag as inappropriate:");
        ListView<String> questionList = new ListView<>();
        Button flagButton = new Button("Flag Selected");
        Label statusLabel = new Label();

        try {
            List<String[]> questions = databaseHelper.getAllQuestions();
            for (String[] q : questions) {
                questionList.getItems().add("Q" + q[0] + ": " + q[1]);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        flagButton.setOnAction(e -> {
            String selected = questionList.getSelectionModel().getSelectedItem();
            if (selected == null) {
                statusLabel.setText("Please select a question to flag.");
            } else {
                statusLabel.setText("Flagged: " + selected);
                // In real case: store flags in DB.
            }
        });

        layout.getChildren().addAll(label, questionList, flagButton, statusLabel);
        tab.setContent(layout);
        return tab;
    }

    private Tab leaveFeedbackTab() {
        Tab tab = new Tab("Leave Feedback");
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        Label label = new Label("Select an answer to leave feedback:");
        ListView<String> answerList = new ListView<>();
        TextField feedbackField = new TextField();
        feedbackField.setPromptText("Enter feedback here...");
        Button submitButton = new Button("Submit Feedback");
        Label statusLabel = new Label();

        try {
            List<String[]> answers = databaseHelper.getAllAnswers();
            for (String[] a : answers) {
                answerList.getItems().add("A" + a[0] + ": " + a[1]);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        submitButton.setOnAction(e -> {
            String selected = answerList.getSelectionModel().getSelectedItem();
            String feedback = feedbackField.getText();
            if (selected == null || feedback.isEmpty()) {
                statusLabel.setText("Please select an answer and write feedback.");
            } else {
                statusLabel.setText("Feedback submitted for: " + selected);
                feedbackField.clear();
            }
        });

        layout.getChildren().addAll(label, answerList, feedbackField, submitButton, statusLabel);
        tab.setContent(layout);
        return tab;
    }
}
