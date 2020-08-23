/**
 * A class used to show a basic representation of a Blackjack table on the system console.
 *
 * @author Dany Houde
 */
public class SimpleBlackjackTable extends BlackjackTable {


    private static final String HORIZONTAL_LINE =  "***************************************\n";
    private static final String DH_BLACKJACK = "***********   DH Blackjack   **********\n";

    public SimpleBlackjackTable(Player[] thePlayers, Dealer aDealer) {
        super(thePlayers, aDealer);
    }

    @Override
    public void refresh() {
        // Simply print the current table out.

        System.out.println(this.toString());
    }

    @Override
    public String toString() {

        StringBuilder returnString = new StringBuilder("\n");

        returnString.append(HORIZONTAL_LINE);
        returnString.append(DH_BLACKJACK);
        returnString.append(HORIZONTAL_LINE);
        returnString.append(dealer.toString());

        for(int i=0; i<players.length; i++) {
            if(null == players[i]) {
               continue;
            }

            returnString.append(players[i].toString());
        }

        returnString.append(HORIZONTAL_LINE);

        return returnString.toString();
    }

}
