import java.util.List;

/**
 * Class representing a Blackjack dealer.
 *
 * @author Dany Houde
 */
public class Dealer {

    // The dealer's unique hand
    BasicHand hand;

    /**
     * Create a new Dealer. The Dealer will be initialized with a new BasicHand.
     */
    public Dealer() {
        this.hand = new BasicHand();
    }

    /**
     * Replace this Dealer's hand with the provided hand.
     * @param aHand the new Dealer hand
     */
    public void setHand(BasicHand aHand) {
        this.hand = aHand;
    }

    /**
     * @return this Dealer's hand
     */
    public BasicHand getHand() {
        return this.hand;
    }

    /**
     * @return true if this Dealer is holding a natural
     */
    public boolean hasNatural() {
        return null != this.hand && this.hand.isNatural();
    }


    /**
     * @return a String representation of this Dealer
     */
    public String toString() {
        StringBuilder returnString = new StringBuilder();
        returnString.append("Dealer\n\t");
        returnString.append(null == this.hand ? "No hand" : this.hand.toString());
        returnString.append("\n");

        return returnString.toString();
    }

}
