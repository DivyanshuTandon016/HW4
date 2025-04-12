This is my Homework 4 project for CSE 360. It is a JavaFX-based application that supports role-based login for admins, students, and staff. The app connects to an H2 embedded database to manage user data, questions, answers, and roles.

Admins can create invitation codes, manage user roles, and delete users. Students can ask and answer questions, view all Q&A, edit or delete their entries, and search through questions. Staff users are taken to a dedicated StaffHomePage, where they can view unanswered questions and leave feedback or comments. This page was added specifically for HW4 to fulfill the staff role user stories.

The application begins by running StartCSE360.java. If no users exist, the first screen helps create the initial admin. All login, setup, and password reset functionalities use FSM validation for usernames and graph-based validation for passwords. A secure OTP feature allows users to reset forgotten passwords.

To use the program, you can just ensure that JavaFX is configured in your IDE. Run the StartCSE360.java file, and the UI will guide you through login or setup based on your role. All key functionality has been tested and documented.
