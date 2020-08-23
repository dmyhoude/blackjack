import java.util.LinkedList;
import java.util.List;

/**
 * A basic hand, such as the one used by the dealer.
 * This hand does not support splitting, wagers, etc.
 *
 * @author Dany Houde
 */
public class BasicHand {

    // The cards in this hand
    protected List<Card> cards;

    public BasicHand() {
        this.cards = new LinkedList<Card>();
    }

    public void addCard(Card aCard) {
        this.cards.add(aCard);
    }

     /**
     * Return the value of this hand. An ace is worth 11
     * unless this brings the hand's value over 21; in that case it
     * is only worth 1.
     * @return the value of this hand
     */
    public int getValue() {

        boolean handHasAnAce = false;
        int handValue = 0;
        // Compute the "minimum" sum, valuing all Aces as 1
        for(Card c: cards) {
            handValue += c.getPipValue();

            if(Card.Rank.ACE == c.getRank()) {
                handHasAnAce = true;
            }
        }

        // If the sum is small enough, we can value one ace as 11
        if(handHasAnAce && handValue <= 11) {
            // Add 10 to the hand value, effectively changing one ace's value from 1 to 11
            handValue += 10;
        }

        return handValue;
    }


    /**
     * Return whether the hand represents a natural Blackjack
     * (only two cards whose value sum up to 21)
     * @return if the hand represents a natural Blackjack, false otherwise
     */
    public boolean isNatural() {
        return (2 == this.cards.size() &&
                this.getValue() == BlackjackGame.BLACKJACK_NUMBER) ;
    }

    /**
     * @return the list of cards contained in this hand
     */
    public List<Card> getCards() {
        return this.cards;
    }


    /**
     * Resets the state of this hand: remove all cards, wagers and
     * double down status.
     */
    public void reset() {
        this.cards = new LinkedList<Card>();
    }


    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        boolean firstCard = true;
        boolean hasFaceDownCard = false;
        for(Card c : cards) {
            if(!firstCard) {
                sb.append(" ");
            } else {
                firstCard = false;
            }

            if(!c.isFaceUp()) {
                hasFaceDownCard = true;
            }
            sb.append(c.toString());
        }

        if(!hasFaceDownCard) {
            sb.append("\tValue: ");
            sb.append(this.getValue());
        }

        return sb.toString();
    }



    public static void main(String[] args) {

        BasicHand testHand = new BasicHand();

        Card testCard1 = new Card(Card.Suit.CLUBS, Card.Rank.DEUCE, true);
        Card testCard2 = new Card(Card.Suit.DIAMONDS, Card.Rank.TEN, true);

        testHand.addCard(testCard1);
        testHand.addCard(testCard2);


        List<Card> discardedCards = testHand.getCards();
        testHand.reset();
        System.out.println("Size of discarded cards: " + discardedCards.size());
        System.out.println("Size of hand: " + testHand.getCards().size());
    }

}
