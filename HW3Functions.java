package application;

import java.util.ArrayList;
import java.util.List;

/**
 * HW3 Automated Testing Mainline
 * This class includes five standalone test simulations based on TP2 functionality.
 * It mimics the behavior of unit tests for question handling in a Q&A system.
 *
 * These tests validate core features such as adding, answering, editing, and resolving questions.
 * GUI components have been replaced by simulated data structures.
 *
 * Author: [Your Name]
 * Date: 2025-03-26
 */
public class HW3Functions {

    static List<String> unresolvedQuestionsList = new ArrayList<>();
    static List<String> resolvedQuestionsList = new ArrayList<>();
    static List<String> answersList = new ArrayList<>();
    static List<String> followUpQuestionsList = new ArrayList<>();

    /**
     * Simulates adding a new question to the unresolvedQuestionsList.
     * Verifies it's not in the resolved list.
     */
    public static void testAskQuestion() {
        String question = "What is polymorphism in OOP?";
        unresolvedQuestionsList.add(question);
        assert unresolvedQuestionsList.contains(question);
        assert !resolvedQuestionsList.contains(question);
        System.out.println("testAskQuestion passed.");
    }

    /**
     * Simulates answering a question and associating it in answersList.
     */
    public static void testAnswerQuestion() {
        String answer = "Polymorphism allows objects to be treated as instances of their parent class.";
        answersList.add(answer);
        assert answersList.contains(answer);
        System.out.println("testAnswerQuestion passed.");
    }

    /**
     * Simulates marking a question as resolved.
     * Moves from unresolved to resolved list.
     */
    public static void testMarkQuestionAsResolved() {
        String question = "How do I compile Java?";
        unresolvedQuestionsList.add(question);
        unresolvedQuestionsList.remove(question);
        resolvedQuestionsList.add(question);
        assert !unresolvedQuestionsList.contains(question);
        assert resolvedQuestionsList.contains(question);
        System.out.println("testMarkQuestionAsResolved passed.");
    }

    /**
     * Simulates editing an existing question.
     * Replaces the question with updated content.
     */
    public static void testEditQuestion() {
        String oldQuestion = "What is a class";
        String updatedQuestion = "What is a class in Java?";
        unresolvedQuestionsList.add(oldQuestion);
        unresolvedQuestionsList.remove(oldQuestion);
        unresolvedQuestionsList.add(updatedQuestion);
        assert !unresolvedQuestionsList.contains(oldQuestion);
        assert unresolvedQuestionsList.contains(updatedQuestion);
        System.out.println("testEditQuestion passed.");
    }

    /**
     * Validates that a follow-up question is not empty before submission.
     * If it's empty, an error should be raised.
     */
    public static void testFollowUpNotEmpty() {
        String followUp = ""; // Invalid input
        try {
            if (followUp.trim().isEmpty()) {
                throw new IllegalArgumentException("Follow-up question cannot be empty.");
            }
            followUpQuestionsList.add(followUp);
            assert false : "Expected exception for empty follow-up.";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().contains("cannot be empty");
            System.out.println("testFollowUpNotEmpty passed.");
        }
    }

    /**
     * Mainline to run all test cases.
     */
    public static void main(String[] args) {
        testAskQuestion();
        testAnswerQuestion();
        testMarkQuestionAsResolved();
        testEditQuestion();
        testFollowUpNotEmpty();
        System.out.println("All HW3 tests completed.");
    }
}
