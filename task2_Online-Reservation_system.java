import javax.swing.*;
import java.awt.*;
import java.io.*;

// Main class that launches the application
public class OnlineReservationSystem {
    public static void main(String[] args) {
        new LoginForm();
    }
}

// Login form class that handles user authentication
class LoginForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    // Constructor to initialize and set up the login form
    public LoginForm() {
        setTitle("Login Form");
        setSize(300, 150);
        setLayout(new GridLayout(3, 2));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Adding username field
        add(new JLabel("Username:"));
        usernameField = new JTextField();
        add(usernameField);

        // Adding password field
        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        // Adding login button with action listener
        loginButton = new JButton("Login");
        loginButton.addActionListener(e -> handleLogin());
        add(loginButton);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Method to handle login logic
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        // Validation to ensure no blank entries
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username or password cannot be blank.");
            return;
        }

        // Attempt to log in and write attempts to a file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("login.txt", true))) {
            if (username.equalsIgnoreCase("Admin")) { // Deny access to admin
                bw.write("Failed admin access attempt by: " + username + "\n");
                JOptionPane.showMessageDialog(this, "You can't access admin.");
            } else if (!username.isEmpty() && !password.isEmpty()) { // General user access
                bw.write("Login Successful: " + username + " with password: " + password + "\n");
                JOptionPane.showMessageDialog(this, "Login Successful!");
                dispose(); // Close the login window
                new OptionForm().setVisible(true); // Open the option selection form
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials!");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

// Option form to select either reservation or cancellation
class OptionForm extends JFrame {
    public OptionForm() {
        setTitle("Select Option");
        setSize(300, 100);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Button to make a reservation
        JButton reservationButton = new JButton("Make a Reservation");
        reservationButton.addActionListener(e -> {
            dispose();
            new ReservationForm().setVisible(true);
        });
        add(reservationButton);

        // Button to cancel a reservation
        JButton cancellationButton = new JButton("Cancel a Reservation");
        cancellationButton.addActionListener(e -> {
            dispose();
            new CancellationForm().setVisible(true);
        });
        add(cancellationButton);

        setLocationRelativeTo(null);
        setVisible(true);
    }
}

// Reservation form for entering reservation details
class ReservationForm extends JFrame {
    private JTextField trainNumberField, fromField, toField, dateField;
    private JButton reserveButton;

    public ReservationForm() {
        setTitle("Reservation Form");
        setLayout(new GridLayout(5, 2));
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Fields for reservation details
        add(new JLabel("Train Number:"));
        trainNumberField = new JTextField(10);
        add(trainNumberField);

        add(new JLabel("From:"));
        fromField = new JTextField(10);
        add(fromField);

        add(new JLabel("To:"));
        toField = new JTextField(10);
        add(toField);   

        add(new JLabel("Date of Journey:"));
        dateField = new JTextField(10);
        add(dateField);

        // Button to submit the reservation
        reserveButton = new JButton("Reserve");
        reserveButton.addActionListener(e -> handleReservation());
        add(reserveButton);

        JButton cancellationButton = new JButton("Cancel a Reservation");
        cancellationButton.addActionListener(e -> {
            dispose();
            new CancellationForm().setVisible(true);
        });
        add(cancellationButton);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Handle reservation 
    private void handleReservation() {
        if (trainNumberField.getText().trim().isEmpty() || fromField.getText().trim().isEmpty() ||
                toField.getText().trim().isEmpty() || dateField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled.");
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("reservation.txt", true))) {
            String reservationDetails = "Train No: " + trainNumberField.getText() + ", From: " + fromField.getText()
                    + ", To: " + toField.getText() + ", Date: " + dateField.getText() + "\n";
            bw.write(reservationDetails);
            JOptionPane.showMessageDialog(this, "Reservation successful!");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

// Cancellation form to cancel an existing reservation
class CancellationForm extends JFrame {
    private JTextField trainNumberField;
    private JButton cancelButton;

    public CancellationForm() {
        setTitle("Cancellation Form");
        setLayout(new GridLayout(2, 2));
        setSize(300, 100);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Field to enter the train number for cancellation
        add(new JLabel("Train Number:"));
        trainNumberField = new JTextField(10);
        add(trainNumberField);

        // Button to perform cancellation
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> handleCancellation());
        add(cancelButton);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Handle cancellation
    private void handleCancellation() {
        String trainNumber = trainNumberField.getText().trim();
    
        if (trainNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Train number cannot be blank.");
            return;
        }
    
        File inputFile = new File("reservation.txt");
        StringBuilder newContent = new StringBuilder();
        boolean found = false;
    
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter cancelWriter = new BufferedWriter(new FileWriter("cancellation.txt", true))) {
    
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.trim().contains("Train No: " + trainNumber + ",")) {
                    cancelWriter.write(currentLine.trim() + "\n");
                    found = true;
                    continue;
                }
                newContent.append(currentLine).append(System.lineSeparator());
            }
    
            if (!found) {
                JOptionPane.showMessageDialog(this, "No reservation found for Train No: " + trainNumber);
            } else {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile))) {
                    writer.write(newContent.toString());
                    JOptionPane.showMessageDialog(this, "Cancellation successful!");
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
