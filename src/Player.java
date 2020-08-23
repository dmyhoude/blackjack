import java.util.LinkedList;
import java.util.List;

/**
 * A class representing a Blackjack player.
 *
 * @author Dany Houde
 */
public class Player {

    // How much money the player has remaining to bet
    private int cashBalance;

    // Player's number
    private int number;

    // Whether this player is still in the game
    private boolean isPlaying;

    // This player's hands
    List<PlayerHand> hands;


    /**
     * Construct a new Player with the specified number and starting cash balance
     * @param aNumber the new Player's number
     * @param startingCash the new Player's starting cash balance
     */
    public Player(int aNumber, int startingCash) {
        this.number = aNumber;
        this.cashBalance = startingCash;
        this.hands = new LinkedList<PlayerHand>();
        this.hands.add(new PlayerHand());
        this.isPlaying = true;
    }


    /**
     * Splits the specified hand in two. One card is removed from the specified hand
     * and placed into the newly split hand.
     *
     * @param handIndex the index of the hand to split
     * @return the newly split hand
     * @throws UnsplittableHandException if the hand cannot be split
     */
    public PlayerHand splitHand(int handIndex) throws UnsplittableHandException {
        PlayerHand playerHandToSplit = this.hands.get(handIndex);

        if(null == playerHandToSplit || !playerHandToSplit.canBeSplit(this.cashBalance)) {
            throw new UnsplittableHandException();
        }

        PlayerHand splitPlayerHand = new PlayerHand();
        splitPlayerHand.setWager(playerHandToSplit.getWager());
        this.cashBalance -= playerHandToSplit.getWager();

        Card secondCard = playerHandToSplit.getCards().remove(1);

        if(secondCard.getRank() == Card.Rank.ACE) {
            splitPlayerHand.setCameFromSplitAces(true);
            playerHandToSplit.setCameFromSplitAces(true);
        }

        splitPlayerHand.addCard(secondCard);
        return splitPlayerHand;
    }


    /**
     * Returns true if this player has at least one hand which is neither
     * a Blackjack or a bust.
     *
     * @return true if at least one of the player's hands is not Blackjack
     * or bust.
     */
    public boolean isActive() {
        for(PlayerHand h : this.getHands()) {
            if(h.isActive()) {
                return true;
            }
        }

        return false;
    }


    /**
     * Reduce this Player's cash balance by the specified amount.
     * @param amount how much to reduce the balance by
     * @throws InsufficientCashException if the Player has insufficient cash to carry out this operation
     */
    public void decreaseCashBalanceBy(int amount) throws InsufficientCashException {
        if((this.cashBalance - amount) < 0) {
            throw new InsufficientCashException(this.cashBalance, amount);
        }

        this.cashBalance -= amount;
    }

    /**
     * Reset this player's hands to a single empty hand
     */
    public void resetHands() {
        this.hands = new LinkedList<PlayerHand>();
        this.hands.add(new PlayerHand());
    }

    /**
     * Increase this Player's cash balance by the specified amount.
     * @param amount how much to increase this Player's balance by.
     */
    public void increaseCashBalanceBy(int amount) {
        this.cashBalance += amount;
    }


    /**
     * Add a new hand to this Player's list of hands.
     * @param aPlayerHand the hand to add
     */
    public void addHand(PlayerHand aPlayerHand) {
        this.hands.add(aPlayerHand);
    }


    /**
     * @return a String representation of this Player
     */
    public String toString() {
        StringBuilder returnString = new StringBuilder();

        returnString.append("Player ") ;
        returnString.append(this.number);
        returnString.append("\n");
        returnString.append("\tCash balance: $");
        returnString.append(this.cashBalance);
        returnString.append("\n");

        if(this.hands.isEmpty()) {
            returnString.append("\tNo hands\n");
        } else {
            for(PlayerHand h : this.hands) {
                returnString.append("\t");
                returnString.append(h.toString());
                returnString.append("\n");
            }
        }

        return returnString.toString();
    }


    public int getCashBalance() {
        return cashBalance;
    }

    public void setCashBalance(int cashBalance) {
        this.cashBalance = cashBalance;
    }

    public int getNumber() {
        return number;
    }

    public List<PlayerHand> getHands() {
        return hands;
    }

    public PlayerHand removeHand(int handIndex) {
        return this.hands.remove(handIndex);
    }

    public void setHands(List<PlayerHand> playerHands) {
        this.hands = playerHands;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public static void main(String[] args) {

        // Test splitting hands
        Player testPlayer = new Player(0, 1000);



        Card aCard = new Card(Card.Suit.CLUBS, Card.Rank.KING, true);
//        Card bCard = new Card(Card.Suit.SPADES, Card.Rank.DEUCE);
        Card cCard = new Card(Card.Suit.HEARTS, Card.Rank.TEN, true);

        PlayerHand testPlayerHand = testPlayer.getHands().get(0);
        testPlayerHand.addCard(aCard);
//        testHand.addCard(bCard);
        testPlayerHand.addCard(cCard);

        System.out.println(testPlayer.toString());
        System.out.println("Can hand be split? " + testPlayerHand.canBeSplit(1000));

        try {
            testPlayer.splitHand(0);
            System.out.println("Player after split: ****");
            System.out.println(testPlayer.toString());
        } catch(UnsplittableHandException uhe) {
            System.err.println("PlayerHand cannot be split!");
        }

    }
}
