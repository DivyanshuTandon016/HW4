package application;

import databasePart1.DatabaseHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.SQLException;

public class StudentHomePage {
    private DatabaseHelper databaseHelper;
    // Shared list to store unresolved questions.
    private ObservableList<String> unresolvedQuestionsList = FXCollections.observableArrayList();
    // List to store all asked questions and, if answered, their answer.
    private ObservableList<String> allQuestionsList = FXCollections.observableArrayList();
    // Counter for sequentially numbering questions.
    private int questionCounter = 1;

    // Constructor that accepts a DatabaseHelper instance.
    public StudentHomePage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    // The show method creates the UI and displays the Student Home Page on the given Stage.
    public void show(Stage stage) {
        // Connect to the database.
        try {
            databaseHelper.connectToDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // Create a TabPane for the different student tasks.
        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(
            createAskQuestionTab(),
            createUnresolvedQuestionsTab(),
            createAnswerQuestionTab(),
            createAllQATab(),
            createSearchTab(),
            createDeleteTab(), // New Delete Q&A tab
            createEditQATab()  // Added the Edit Q&A tab
        );
        
        // Set up the overall layout using a BorderPane.
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        
        // Top: Header label.
        Label headerLabel = new Label("Student Home Page");
        headerLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        root.setTop(headerLabel);
        BorderPane.setAlignment(headerLabel, Pos.CENTER);
        
        // Center: The TabPane.
        root.setCenter(tabPane);
        
        // Bottom: A simple exit button.
        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> stage.close());
        HBox bottomBox = new HBox(exitButton);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(10, 0, 0, 0));
        root.setBottom(bottomBox);
        
        Scene scene = new Scene(root, 1000, 600);
        stage.setScene(scene);
        stage.setTitle("Student Home Page");
        stage.show();
    }
    
    // Tab 1: Ask a Question and Receive Answers
    private Tab createAskQuestionTab() {
        Tab tab = new Tab("Ask Question");
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.setAlignment(Pos.CENTER_LEFT);
        
        Label instruction = new Label("Enter your question:");
        TextField questionField = new TextField();
        questionField.setPromptText("Type your question here...");
        Button submitButton = new Button("Submit Question");
        Label feedbackLabel = new Label();
        
        submitButton.setOnAction(e -> {
            String questionText = questionField.getText().trim();
            if (questionText.isEmpty()) {
                feedbackLabel.setText("Question cannot be empty.");
            } else {
                // Use sequential numbering for the question.
                int id = questionCounter++;
                Question q = new Question(id, questionText);
                String baseQuestion = "Q" + id + ": " + questionText;
                feedbackLabel.setText("Question submitted: " + q);
                questionField.clear();
                // Add the new question to the shared unresolved questions list.
                unresolvedQuestionsList.add(baseQuestion);
                // Also add to the all questions list.
                allQuestionsList.add(baseQuestion);
            }
        });
        
        layout.getChildren().addAll(instruction, questionField, submitButton, feedbackLabel);
        tab.setContent(layout);
        return tab;
    }
    
    // Tab 2: View Unresolved Questions and Their Answers
    private Tab createUnresolvedQuestionsTab() {
        Tab tab = new Tab("Unresolved Questions");
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        
        Label header = new Label("List of Unresolved Questions:");
        // Bind the ListView to the shared ObservableList.
        ListView<String> questionListView = new ListView<>(unresolvedQuestionsList);
        
        layout.getChildren().addAll(header, questionListView);
        tab.setContent(layout);
        return tab;
    }
    
    // Tab 3: Answer a Selected Question
    private Tab createAnswerQuestionTab() {
        Tab tab = new Tab("Answer a Question");
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        
        Label instruction = new Label("Select a question and type your answer:");
        // Bind the question combo box to the shared unresolved questions list.
        ComboBox<String> questionCombo = new ComboBox<>(unresolvedQuestionsList);
        TextField answerField = new TextField();
        answerField.setPromptText("Type your answer here...");
        Button submitAnswerButton = new Button("Submit Answer");
        Label feedback = new Label();
        
        submitAnswerButton.setOnAction(e -> {
            String question = questionCombo.getValue();
            String answerText = answerField.getText().trim();
            if (question == null || answerText.isEmpty()) {
                feedback.setText("Please select a question and provide an answer.");
            } else {
                feedback.setText("Your answer has been submitted for: " + question);
                answerField.clear();
                // Remove the answered question from the unresolved questions list.
                unresolvedQuestionsList.remove(question);
                // Also update the all questions list to show the answer.
                for (int i = 0; i < allQuestionsList.size(); i++) {
                    String entry = allQuestionsList.get(i);
                    if (entry.equals(question)) {
                        allQuestionsList.set(i, question + " | Answer: " + answerText);
                        break;
                    }
                }
            }
        });
        
        layout.getChildren().addAll(instruction, questionCombo, answerField, submitAnswerButton, feedback);
        tab.setContent(layout);
        return tab;
    }
    
    // Tab 4: All Questions and Their Answers
    private Tab createAllQATab() {
        Tab tab = new Tab("All Q&A");
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        
        Label header = new Label("All Asked Questions and Their Answers:");
        ListView<String> qaListView = new ListView<>(allQuestionsList);
        
        layout.getChildren().addAll(header, qaListView);
        tab.setContent(layout);
        return tab;
    }
    
    // Tab 5: Search for Questions in All Q&A (Answered and Unanswered)
    private Tab createSearchTab() {
        Tab tab = new Tab("Search");
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        
        Label instruction = new Label("Enter a keyword to search for questions:");
        TextField keywordField = new TextField();
        keywordField.setPromptText("Enter keyword...");
        Button searchButton = new Button("Search");
        TextArea searchResults = new TextArea();
        searchResults.setEditable(false);
        searchResults.setWrapText(true);
        
        searchButton.setOnAction(e -> {
            String keyword = keywordField.getText().trim().toLowerCase();
            if (keyword.isEmpty()) {
                searchResults.setText("Please enter a keyword.");
            } else {
                StringBuilder results = new StringBuilder();
                results.append("Search Results for '").append(keyword).append("':\n");
                boolean found = false;
                // Search within the allQuestionsList.
                for (String entry : allQuestionsList) {
                    if (entry.toLowerCase().contains(keyword)) {
                        results.append(entry).append("\n");
                        found = true;
                    }
                }
                if (!found) {
                    results.append("No matching questions found.\n");
                }
                searchResults.setText(results.toString());
            }
        });
        
        layout.getChildren().addAll(instruction, keywordField, searchButton, searchResults);
        tab.setContent(layout);
        return tab;
    }
    
    // New Tab 6: Delete Questions and Answers
    private Tab createDeleteTab() {
        Tab tab = new Tab("Delete Q&A");
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        
        Label instruction = new Label("Select a question to delete:");
        // ListView showing all questions (and answers if available)
        ListView<String> deleteListView = new ListView<>(allQuestionsList);
        Button deleteButton = new Button("Delete Selected");
        Label feedbackLabel = new Label();
        
        deleteButton.setOnAction(e -> {
            String selected = deleteListView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                feedbackLabel.setText("Please select a question/answer to delete.");
            } else {
                // Remove the selected item from both lists.
                allQuestionsList.remove(selected);
                unresolvedQuestionsList.remove(selected);
                feedbackLabel.setText("Deleted: " + selected);
            }
        });
        
        layout.getChildren().addAll(instruction, deleteListView, deleteButton, feedbackLabel);
        tab.setContent(layout);
        return tab;
    }
    
    // New Tab: Edit Q&A
    private Tab createEditQATab() {
        Tab tab = new Tab("Edit Q&A");
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.setAlignment(Pos.CENTER_LEFT);

        Label instruction = new Label("Select a Question or Answer to Edit:");
        ListView<String> editListView = new ListView<>(allQuestionsList);
        TextField editField = new TextField();
        editField.setPromptText("Edit your selected question or answer here...");
        Button updateButton = new Button("Update");
        Label feedbackLabel = new Label();

        updateButton.setOnAction(e -> {
            String selected = editListView.getSelectionModel().getSelectedItem();
            String newText = editField.getText().trim();

            if (selected == null) {
                feedbackLabel.setText("Please select a question or answer to edit.");
                return;
            }

            if (newText.isEmpty()) {
                feedbackLabel.setText("Edited text cannot be empty.");
                return;
            }

            try {
                int id = extractQuestionId(selected);
                databaseHelper.updateQuestion(id, newText);
                allQuestionsList.set(editListView.getSelectionModel().getSelectedIndex(), "Q" + id + ": " + newText);
                feedbackLabel.setText("Updated successfully.");
            } catch (SQLException ex) {
                feedbackLabel.setText("Error updating.");
                ex.printStackTrace();
            }
        });

        layout.getChildren().addAll(instruction, editListView, editField, updateButton, feedbackLabel);
        tab.setContent(layout);
        return tab;
    }

    // Extracts the question ID from a given string
    private int extractQuestionId(String questionText) {
        String[] parts = questionText.split(":");
        try {
            return Integer.parseInt(parts[0].replace("Q", "").trim());
        } catch (NumberFormatException e) {
            return -1; // Invalid ID
        }
    }
}
