
import javax.swing.*;

public class NumberGuessingGame {
    private static final int MAX_ATTEMPTS = 10; // Constant for maximum allowed attempts per round
    private static int attempts; // Tracks the number of attempts made in the current round
    private static int score = 0; // Total score accumulated over all rounds
    private static int round = 1; // Tracks the current round number

    public static void main(String[] args) {
        while (true) { 
            int numberToGuess = (int) (Math.random() * 100) + 1; // Random number between 1 and 100
            attempts = 0; // Reset attempts for the new round
            
            // Initial message for each round
            JOptionPane.showMessageDialog(null, "Round " + round + ": Guess the number between 1 and 100. You have " + MAX_ATTEMPTS + " attempts.");

            while (attempts < MAX_ATTEMPTS) { // Attempt loop
                String response = JOptionPane.showInputDialog(null, "Enter your guess:");
                int guess = Integer.parseInt(response); 
                attempts++; 

                if (guess < numberToGuess) {
                    JOptionPane.showMessageDialog(null, "Your guess is too low!"); 
                } else if (guess > numberToGuess) {
                    JOptionPane.showMessageDialog(null, "Your guess is too high!"); 
                } else {
                    // Calculate score for the round
                    score += (MAX_ATTEMPTS - attempts + 1) * 10; 
                    JOptionPane.showMessageDialog(null, "Correct! You've guessed the number in " + attempts + " attempts.\nYour score: " + score); // Success message
                    break; // Exit the attempts loop as the correct number was guessed
                }

                if (attempts == MAX_ATTEMPTS) {
                    // Message when no attempts left and user hasn't guessed the number
                    JOptionPane.showMessageDialog(null, "You've used all your attempts! The number was: " + numberToGuess + "\nYour score: " + score);
                }
            }

            round++; // Increment round counter
            int playAgain = JOptionPane.showConfirmDialog(null, "Play another round?", "Guess the Number", JOptionPane.YES_NO_OPTION); // Ask user to play again
            if (playAgain != JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(null, "Final Score: " + score + "\nThanks for playing!"); // End game message
                break; // Exit the game loop
            }
        }
    }
}
