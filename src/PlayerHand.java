import java.util.List;

/**
 *
 * A player hand, i.e. a list of cards whose values add up to a given total
 * in the current round.
 *
 * @author Dany Houde
 */
public class PlayerHand extends BasicHand {

    // The wager associated with this hand
    private int wager;

    // True if the player performed a double down on this hand
    private boolean isDoubleDown;

    // True if this hand came from splitting two aces
    private boolean cameFromSplitAces;


    public PlayerHand() {
        super();
        this.wager = 0;
        this.cameFromSplitAces = false;
        this.isDoubleDown = false;
    }

   /**
     * Lets the caller know whether this hand is still "active", meaning
     * that it is neither a natural or a bust.
     *
     * @return false if the hand is considered active
     */
    public boolean isActive() {
        return !this.isNatural() && (this.getValue() <= BlackjackGame.BLACKJACK_NUMBER);
    }


    public void setDoubleDown(boolean isDoubleDown) {
        this.isDoubleDown = isDoubleDown;
    }

    public boolean isDoubleDown() {
        return this.isDoubleDown;
    }


    /**
     * Lets the caller know whether this hand can be split or not.
     * A hand can be split if both its cards have the same value,
     * and if the player has enough to cover the split hand wagers.
     * @return
     */
    public boolean canBeSplit(int availableMoney) {
        if(2 != this.cards.size() || availableMoney < this.wager) {
            return false;
        }

        return cards.get(0).getPipValue() == cards.get(1).getPipValue();
    }


    public void reset() {
        super.reset();
        this.isDoubleDown = false;
        this.wager = 0;
        this.cameFromSplitAces = false;
    }


    public void setCameFromSplitAces(boolean wasSplitOnAces) {
        this.cameFromSplitAces = wasSplitOnAces;
    }

    public boolean cameFromSplitAces() {
        return this.cameFromSplitAces;
    }


    public int getWager() {
        return this.wager;
    }


    public void setWager(int aWager) {
        this.wager = aWager;
    }

    /**
     * Returns a string representation of this PlayerHand, including
     * the hand's value if all cards are face up
     * @return
     */
    @Override
    public String toString() {
        if(this.cards.isEmpty()) {
            return "No cards";
        }

        StringBuilder sb = new StringBuilder(super.toString());

        sb.append("\tWager: $");
        sb.append(this.getWager());

        return sb.toString();
    }


    public static void main(String[] args) {

        PlayerHand testHand = new PlayerHand();

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
