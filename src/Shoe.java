import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A class representing the shoe containing cards dealt by the dealer.
 * Contains logic to keep track of when and how to shuffle the shoe.
 *
 * @author Dany Houde
 */
public class Shoe {

    private List<Card> cards;

    // With 4 or more decks, place an insert in the deck to keep track
    // of when the Shoe should be reshuffled.
    private int shuffleMarkerPosition;

    // The number of cards dealt from the Shoe since the last shuffle.
    private int numberOfDealtCards;


    /**
     * Construct a new Shoe containing the specified number of card decks.
     * @param numberOfDecks how many card decks to use in the constructed Shoe
     */
    public Shoe(int numberOfDecks) {

        this.cards = new ArrayList<Card>(Deck.getSize());

        for(int i=0; i<numberOfDecks; i++) {
            this.cards.addAll(Deck.getNewDeck().getCards());
        }

        // Place the marker at the 3/4 mark of the Shoe when there
        // are at least 4 decks. Otherwise, leave the marker at the beginning
        if(numberOfDecks >= 4) {
            this.shuffleMarkerPosition = 3 * this.cards.size() / 4;
        } else {
            this.shuffleMarkerPosition = 0;
        }
    }


    /**
     * Returns whether the Shoe should be reshuffled, based on whether
     * we have dealt past the marker.
     */
    public boolean shouldBeReshuffled() {
        if(numberOfDealtCards >= shuffleMarkerPosition) {
            return true;
        }

        return false;
    }


    /**
     * @return how many cards are currently in this Shoe
     */
    public int getNumCards() {
        return this.cards.size();
    }


    /**
     * Add all provided cards to this Shoe.
     * @param cards the cards to add to this Shoe
     */
    public void addAll(List<Card> cards) {
        this.cards.addAll(cards);
    }


    /**
     * Shuffle the shoe and reset the marker position.
     */
    public void shuffle() {
        Collections.shuffle(this.cards);

        // Reset the shuffle marker
        this.numberOfDealtCards = 0;
    }


    /**
     * Deal a card from this Shoe, face up or down based on the provided value.
     * @param faceUp true to deal the card face up, false for face down
     * @return the dealt card
     * @throws EmptyShoeException if the shoe is empty and a card cannot be dealt
     */
    public Card deal(boolean faceUp) throws EmptyShoeException {
        if(this.cards.isEmpty()) {
            throw new EmptyShoeException();
        }

        this.numberOfDealtCards++;
        Card returnCard = cards.remove(0);
        returnCard.setFaceUp(faceUp);
        return returnCard;
    }

}
