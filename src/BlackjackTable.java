/**
 * An abstract BlackjackTable model which can be extended to implement
 * different representations of a Blackjack table.
 *
 * @author Dany Houde
 */
public abstract class BlackjackTable {

    protected Player[]  players;
    protected Dealer dealer;

    protected BlackjackTable(Player[] players, Dealer aDealer) {
        this.players = players;
        this.dealer = aDealer;
    }

    /**
     * @return a String representation of this BlackjackTable.
     */
    public abstract String toString();

    /**
     * Update the display of the table representation.
     */
    public abstract void refresh();
}
