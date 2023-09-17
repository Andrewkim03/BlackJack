import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main extends Application {
    private List<Card> deck;
    private List<Card> playerHand;
    private List<Card> dealerHand;

    private int playerScore;
    private int dealerScore;

    private Button hitButton;
    private Button standButton;
    private Button newGameButton;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        deck = new ArrayList<>();
        playerHand = new ArrayList<>();
        dealerHand = new ArrayList<>();
        playerScore = 0;
        dealerScore = 0;

        initializeDeck();

        hitButton = new Button("Hit");
        standButton = new Button("Stand");
        newGameButton = new Button("New Game");

        hitButton.setOnAction(e -> handleHit());
        standButton.setOnAction(e -> handleStand());
        newGameButton.setOnAction(e -> startNewGame());

        HBox buttonBox = new HBox(10, hitButton, standButton, newGameButton);

        Scene scene = new Scene(buttonBox, 300, 100);

        primaryStage.setTitle("Blackjack");
        primaryStage.setScene(scene);
        primaryStage.show();

        startNewGame();
    }

    private void initializeDeck() {
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};

        for (String suit : suits) {
            for (String rank : ranks) {
                deck.add(new Card(rank, suit));
            }
        }

        shuffleDeck();
    }

    private void shuffleDeck() {
        Collections.shuffle(deck);
    }

    private void dealInitialCards() {
        playerHand.clear();
        dealerHand.clear();
        playerScore = 0;
        dealerScore = 0;

        playerHand.add(drawCard());
        playerHand.add(drawCard());

        dealerHand.add(drawCard());
        dealerHand.add(drawCard());
    }

    private Card drawCard() {
        if (deck.isEmpty()) {
            initializeDeck();
        }
        Card card = deck.remove(deck.size() - 1);
        return card;
    }

    private void handleHit() {
        Card card = drawCard();
        playerHand.add(card);
        playerScore += card.getValue();

        if (playerScore > 21) {
            displayResult("Bust! You lose.");
        }
    }

    private void handleStand() {
        while (dealerScore < 17) {
            Card card = drawCard();
            dealerHand.add(card);
            dealerScore += card.getValue();
        }

        if (dealerScore > 21 || dealerScore < playerScore) {
            displayResult("You win!");
        } else if (dealerScore > playerScore) {
            displayResult("Dealer wins!");
        } else {
            displayResult("It's a tie!");
        }
    }

    private void startNewGame() {
        dealInitialCards();
        displayResult("");

        if (playerScore == 21) {
            displayResult("Blackjack! You win!");
        }
    }

    private void displayResult(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Game Result");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class Card {
        private final String rank;
        private final String suit;

        public Card(String rank, String suit) {
            this.rank = rank;
            this.suit = suit;
        }

        public String getRank() {
            return rank;
        }

        public String getSuit() {
            return suit;
        }

        public int getValue() {
            switch (rank) {
                case "2", "3", "4", "5", "6", "7", "8", "9", "10" -> {
                    return Integer.parseInt(rank);
                }
                case "Jack", "Queen", "King" -> {
                    return 10;
                }
                default -> {
                    return 11; // Ace
                }
            }
        }

        @Override
        public String toString() {
            return rank + " of " + suit;
        }
    }
}