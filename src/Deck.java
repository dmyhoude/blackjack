import java.util.ArrayList;
import java.util.List;

/**
 * A utility class to spawn decks of cards.
 *
 * @author Dany Houde
 */
public class Deck {

    // A constant indicating how many cards are in Decks spawned by this class
    private static final int DECK_SIZE = Card.Rank.values().length * Card.Suit.values().length;

    // The list of Cards in this Deck
    private List<Card> cards;

    /**
     * Build a new Deck of cards
     */
    private Deck() {
        this.cards = new ArrayList(DECK_SIZE);

        for(Card.Rank r : Card.Rank.values()) {
            for(Card.Suit s : Card.Suit.values()) {
                this.cards.add(new Card(s, r, false));
            }
        }
    }


    /**
     * @return the list of Cards in this Deck
     */
    public List<Card> getCards() {
        return this.cards;
    }


    /**
     * @return the constant size of a standard deck created by this class
     */
    public static int getSize() {
        return DECK_SIZE;
    }

    /**
     * Returns a new Deck of cards to the caller.
     * @return a new Deck of cards
     */
    public static Deck getNewDeck() {
        return new Deck();
    }
}

