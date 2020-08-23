import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * The main driving class of the Blackjack game.
 * Contains user input and game event loops.
 *
 * @author Dany Houde
 */
public class BlackjackGame {


    public static final int BLACKJACK_NUMBER = 21;
    private static final int DEALER_THRESHOLD = 17;


    // List of players at the table
    private Player[] players;

    private Dealer dealer;

    // The number of players input by the user at the beginning of the game
    private int initialNumberOfPlayers;

    // The Blackjack table used to display game status
    private BlackjackTable table;

    // The dealer's shoe
    private Shoe shoe;

    // The pile of discarded cards
    private List<Card> discardPile;

    private double naturalPayoutFactor;

    // Config values
    private int minNumberOfPlayers;
    private int maxNumberOfPlayers;
    private int numDecks;
    private int minBet;
    private int maxNumberOfSplits;
    private int startingCashBalance;


    /**
     * Constructs a new BlackjackGame and loads config values from the file at the
     * specified path
     * @param configFilePath path to the configuration file
     */
    public BlackjackGame(String configFilePath) {

        this.loadConfiguration(configFilePath);

        this.players = new Player[this.maxNumberOfPlayers];
        this.dealer = new Dealer();

        this.table = new SimpleBlackjackTable(this.players, this.dealer);

        // Set up the shoe
        this.shoe = new Shoe(this.numDecks);
        this.shoe.shuffle();

        this.discardPile = new LinkedList<Card>();
    }


    /**
     * Starts a new game of Blackjack. Keep playing until no player can
     * afford to play anymore, or the user quits after a complete round.
     */
    public void start() {

        IOUtil.displayMessage(Strings.WELCOME_MESSAGE);

        this.initialNumberOfPlayers = getNumberOfPlayers();

        for(int i=0; i<this.initialNumberOfPlayers; i++) {
            this.players[i] = new Player(i, this.startingCashBalance);
        }

        while(true) {
            this.playRound();

            this.table.refresh();
            IOUtil.displayMessageAndWait(Strings.ROUND_IS_OVER);

            this.performBetweenRoundMaintenance();
        }
    }


    /**
     * Plays one round with the players at the table.
     */
    private void playRound() {

        this.takeBets();

        IOUtil.displayMessageAndWait(Strings.TWO_CARDS_WILL_BE_FLIPPED);
        this.dealFirstTwoCards();

        this.table.refresh();

        if(this.dealer.hasNatural()) {
            // The dealer had a natural, so this round is over
            // Flip his cards so players can see it
            this.dealer.getHand().getCards().get(1).setFaceUp(true);
            IOUtil.displayMessageAndWait(Strings.DEALER_HAS_NATURAL);
            return;
        }

        this.doPlayersPlay();
        IOUtil.displayMessageAndWait(Strings.WILL_REVEAL_CARDS);

        // Flip the dealer's second card and the double-down cards
        this.dealer.getHand().getCards().get(1).setFaceUp(true);
        this.showDoubleDownCards();

        this.table.refresh();

        if(this.somePlayersAreStillInPlay() &&
                this.dealer.getHand().getValue() < DEALER_THRESHOLD) {
            IOUtil.displayMessageAndWait(Strings.DEALER_WILL_NOW_PLAY);
            this.doDealerPlay();
        }
    }


    /**
     * Flip up the 3rd cards on double-down hands.
     */
    private void showDoubleDownCards() {
         for(int pi=0; pi<this.players.length; pi++) {
             Player p = players[pi];

             if(null == p || !p.isPlaying()) {
                 continue;
             }

             for(PlayerHand ph : p.getHands()) {
                 if (ph.isDoubleDown()) {
                     ph.getCards().get(ph.getCards().size()-1).setFaceUp(true);
                 }
             }
        }
    }


    /**
     * Lets the caller know whether some players still have active hands
     * at the table
     * @return true if at least one player has an active hand a the table, false otherwise
     */
    private boolean somePlayersAreStillInPlay() {
        for(int pi=0; pi<this.players.length; pi++) {
            Player p = players[pi];

            if(null == p || !p.isPlaying()) {
                continue;
            }

            if(p.isActive()) {
                return true;
            }
        }

        return false;
    }


    /**
     * Go around the table and ask players who are still in play
     * whether they would like to split (if they have a pair),
     * hit, or stand. Display the table after each player turn.
      */
    private void doPlayersPlay() {

        for(int pi=0; pi<this.players.length; pi++) {
            Player p = this.players[pi];

            if(null == p || !p.isPlaying()) {
                continue;
            }

            // Check for split possibilities
            int splitCount = 0;
            int handIndex = 0;
            while(handIndex < p.getHands().size()) {

                PlayerHand currentPlayerHand = p.getHands().get(handIndex);
                if(splitCount < this.maxNumberOfSplits && currentPlayerHand.canBeSplit(p.getCashBalance()) &&
                        playerWantsToSplit(p, currentPlayerHand)) {
                    try {
                        PlayerHand newPlayerHand = p.splitHand(handIndex);
                        p.getHands().add(handIndex+1, newPlayerHand);

                        // Deal one more card on top of each split hand
                        try {
                            currentPlayerHand.addCard(this.shoe.deal(true));
                            newPlayerHand.addCard(this.shoe.deal(true));

                        } catch (EmptyShoeException ese) {
                            System.err.print(ese.getMessage());
                            ese.printStackTrace(System.err);
                        }

                        splitCount++;

                        // Restart from the top of the loop without incrementing
                        // handIndex; this way we can split again as needed
                        continue;
                    } catch (UnsplittableHandException uhe) {
                        System.err.println(uhe.getMessage());
                        uhe.printStackTrace(System.err);
                    }
                }

                // The current hand is not being split
                if(currentPlayerHand.cameFromSplitAces()) {
                    // The player has no say in what happens to this hand.
                    handIndex++;
                    continue;
                } else if(currentPlayerHand.isNatural()) {
                    // Immediately add the payout to the hand's wager
                    // Player gets his wager back, in addition to payout, as configured
                    IOUtil.displayMessageAndWait(p, Strings.PLAYER_HAS_NATURAL);
                    currentPlayerHand.setWager(currentPlayerHand.getWager() +
                        (int) Math.round(this.naturalPayoutFactor * (double) currentPlayerHand.getWager()));
                    handIndex++;
                    continue;
                }

                // Otherwise, the player can choose to hit or stand as he pleases
                this.havePlayerPlayHand(p, currentPlayerHand);
                handIndex++;
            }  // player hand loop

            this.table.refresh();
        } // player loop
    }


    /**
     * Perform the dealer play. The dealer draws until he hits 17 or more.
     */
    private void doDealerPlay() {

        while(this.dealer.getHand().getValue() < DEALER_THRESHOLD) {
            // The dealer hits
            try {
                this.dealer.getHand().addCard(shoe.deal(true));
            } catch (EmptyShoeException ese) {
                System.err.print(ese.getMessage());
                ese.printStackTrace(System.err);
            }
        }
    }


    /**
     * Asks the currentPlayer how he wants to play the provided hand.
     * @param currentPlayer the player whose turn it is
     * @param aPlayerHand the hand to play
     */
    private void havePlayerPlayHand(Player currentPlayer, PlayerHand aPlayerHand) {
        if(aPlayerHand.cameFromSplitAces()) {
            return;
        }

        // Offer the player a chance to double down.
        if(currentPlayer.getCashBalance() >= aPlayerHand.getWager() &&
            aPlayerHand.getValue() < BLACKJACK_NUMBER &&
            playerWantsToDoubleDown(currentPlayer, aPlayerHand)) {
            try {
                currentPlayer.decreaseCashBalanceBy(aPlayerHand.getWager());
                aPlayerHand.setWager(2 * aPlayerHand.getWager());
                aPlayerHand.setDoubleDown(true);

                // Deal one last card for this hand, face down
                aPlayerHand.addCard(shoe.deal(false));
            } catch (InsufficientCashException|EmptyShoeException e) {
                System.err.println(e.getMessage());
                e.printStackTrace(System.err);
            }
        } else {
            // No double down.
            // Ask the player to hit or stand, as long as he hasn't gone bust
            // or obtained a blackjack.
            while(aPlayerHand.getValue() < BLACKJACK_NUMBER && playerWantsToHit(currentPlayer, aPlayerHand)) {
                // Draw one more card, face up
                try {
                    aPlayerHand.addCard(shoe.deal(true));
                } catch (EmptyShoeException ese) {
                    System.err.println(ese.getMessage());
                    ese.printStackTrace(System.err);
                }
            }
        }

    }


    /**
     * Determine whether the provided player wants to hit the provided hand.
     * @param p the current player
     * @param aPlayerHand the hand to play
     * @return true if the player wants to hit the hand, false otherwise
     */
    private boolean playerWantsToHit(Player p, PlayerHand aPlayerHand) {
        char answer = IOUtil.askBinaryQuestion(p,
               Strings.WANT_TO_HIT_OR_STAND_PROMPT + "\n\t" +
                aPlayerHand.toString() + "\n", 'h', 's');

        return (answer == 'h');
    }


    /**
     * Determine whether the provided player wants to double down on the
     * provided hand.
     * @param p the current player
     * @param aPlayerHand the hand to play
     * @return true if the player wants to double down, false otherwise
     */
    private boolean playerWantsToDoubleDown(Player p, PlayerHand aPlayerHand) {
        char answer = IOUtil.askBinaryQuestion(p,
               Strings.WANT_TO_DOUBLE_DOWN_PROMPT + "\n\t" +
                aPlayerHand.toString() + "\n", 'y', 'n');

        return (answer == 'y');
    }

    /**
     * Determine whether the provided player wants to split the
     * provided hand.
     * @param p the current player
     * @param aPlayerHand the hand to play
     * @return true if the player wants to split, false otherwise
     */
    private boolean playerWantsToSplit(Player p, PlayerHand aPlayerHand) {
        char answer = IOUtil.askBinaryQuestion(p,
               Strings.WANT_TO_SPLIT_PROMPT + "\n\t" +
                aPlayerHand.toString() + "\n", 'y', 'n');

        return (answer == 'y');
    }


    /**
     * Asks the players how much they want to bet in the current round.
     */
    private void takeBets() {

        // Have to check the cash balance before allowing a bet
        // set p.isPlaying to false if cash insufficient
        // set bet limit to min(max_bet, avail cash balance)
        for(int i=0; i<players.length; i++) {
            Player p = players[i];
            if(null == p || !p.isPlaying()) {
                continue;
            }

            if(p.getCashBalance() < this.minBet) {
                // This player can no longer play
                p.setPlaying(false);
                continue;
            }

            int betAmount = IOUtil.askIntegerInRangeQuestion(p, Strings.ENTER_BET_AMOUNT_PROMPT,
                    this.minBet, p.getCashBalance());

            p.getHands().get(0).setWager(betAmount);
            try {
                p.decreaseCashBalanceBy(betAmount);
            } catch (InsufficientCashException ice) {
                System.err.println(ice.getMessage());
                ice.printStackTrace(System.err);
            }
        }

    }


    /**
      * Deal one card for each player hand, then one card face up to the dealer
      * Then one more card for reach player hand, and finally one last card
      * face down to the dealer
     */
    private void dealFirstTwoCards() {

         for(int i=0; i<players.length; i++) {
            Player p = players[i];
            if(null == p || !p.isPlaying()) {
                continue;
            }

            PlayerHand firstPlayerHand = p.getHands().get(0);
            try {
                firstPlayerHand.addCard(shoe.deal(true));
            } catch (EmptyShoeException ese) {
                System.err.println(ese.getMessage());
                ese.printStackTrace(System.err);
            }
        }

        // Deal first card to dealer
        BasicHand dealerHand = dealer.getHand();
        try {
            Card dealerCard = shoe.deal(true);
            dealerHand.addCard(dealerCard);
        } catch (EmptyShoeException ese) {
            System.err.println(ese.getMessage());
            ese.printStackTrace(System.err);
        }

        // Deal second card to players
        for(int i=0; i<players.length; i++) {
            Player p = players[i];
            if(null == p || !p.isPlaying()) {
                continue;
            }

            PlayerHand playerHand = p.getHands().get(0);
            try {
                playerHand.addCard(shoe.deal(true));
            } catch (EmptyShoeException ese) {
                System.err.println(ese.getMessage());
                ese.printStackTrace(System.err);
            }
        }

        // Deal second card to dealer (face down)
        try {
            Card dealerCard = shoe.deal(false);
            dealerHand.addCard(dealerCard);
        } catch (EmptyShoeException ese) {
            System.err.println(ese.getMessage());
            ese.printStackTrace(System.err);
        }
    }


    /**
     * Perform in-between round maintenance: distribute payouts, collect losing
     * bets, collect cards, etc. and asks whether to go for one more round.
     * If so, shuffle shoe cards when appropriate.
     *
     * Could be improved to allow players to come and go between rounds.
     */
    private void performBetweenRoundMaintenance() {
        // Pay out winning bets and collect losing ones
        int dealerHandValue = dealer.getHand().getValue();

        if(dealer.hasNatural()) {
            // Iterate over all the player hands
            // The wagers from the hands which are also naturals go back to the player's
            // cash balance. Otherwise, they will get wiped.
            for(int pi=0; pi<players.length; pi++) {
                Player p = players[pi];
                if(null == p || !p.isPlaying()) {
                    continue;
                }

                List<PlayerHand> playerHands = p.getHands();
                if(null == playerHands) {
                    continue;
                }

                for(PlayerHand h : p.getHands()) {
                    if(h.isNatural()) {
                        // Push with the dealer. The player gets his wager back
                        p.increaseCashBalanceBy(h.getWager());
                    }

                    // Discard the hand and wagers
                    this.discardPile.addAll(h.getCards());
                    h.reset();
                }

                p.resetHands();
            }
        } else if(dealerHandValue == BLACKJACK_NUMBER) {
            // The dealer didn't get a natural, but still got to 21.
            // Collect all wagers which are not 21.

            for(int pi=0; pi<this.players.length; pi++) {
                Player p = this.players[pi];
                if(null == p || !p.isPlaying()) {
                    continue;
                }

                for(PlayerHand h : p.getHands()) {
                    if(h.getValue() == BLACKJACK_NUMBER) {
                        // The player gets his wager back
                        p.increaseCashBalanceBy(h.getWager());
                    }

                    // Discard the hand and lose wagers
                    this.discardPile.addAll(h.getCards());
                    h.reset();
                }

                p.resetHands();
            }
        } else if (dealerHandValue > BLACKJACK_NUMBER) {
            // The dealer went bust. Players collect wagers for all hands
            // hands which didn't go bust.

            for(int pi=0; pi<this.players.length; pi++) {
                Player p = this.players[pi];
                if(null == p || !p.isPlaying()) {
                    continue;
                }

                for(PlayerHand h : p.getHands()) {
                    if(h.isActive()) {
                        // The player gets his wager back
                        p.increaseCashBalanceBy(2 * h.getWager());
                    } else if (h.isNatural()) {
                        // The payout has already been included in this hand's wager.
                        // Simply give it back to the player.
                        p.increaseCashBalanceBy(h.getWager());
                    }

                    // Discard the hand and lose wagers
                    this.discardPile.addAll(h.getCards());
                    h.reset();
                }

                p.resetHands();
            }
        } else {
            // The dealer has at least 17 but less than 21. Players collect
            // wagers for each non-natural hand still in play whose value
            // is more than that of the dealer's.

            for(int pi=0; pi<this.players.length; pi++) {
                Player p = this.players[pi];
                if(null == p || !p.isPlaying()) {
                    continue;
                }

                for(PlayerHand h : p.getHands()) {
                    if(h.isActive() && (h.getValue() > dealerHandValue)) {
                        // Player doubles his wagers
                        p.increaseCashBalanceBy(2 * h.getWager());
                    } else if (h.isNatural() || h.getValue() == dealerHandValue) {
                        // The payout for naturals has already been included in the hand wager.
                        // Similarly, if both the player's and the dealer's hands have the same
                        // value, just give the wager back to the player.
                        p.increaseCashBalanceBy(h.getWager());
                    }

                    // Discard the hand and lose wagers
                    this.discardPile.addAll(h.getCards());
                    h.reset();
                }

                p.resetHands();
            }
        }

        // Discard the dealer's cards
        this.discardPile.addAll(dealer.getHand().getCards());
        dealer.getHand().reset();

        // Make sure we accounted for all cards
        assert (this.discardPile.size() + this.shoe.getNumCards() ==
                this.numDecks * Deck.getSize()) :
                "Some cards have not been handled at the end of the round!";

        // Check if any players have money left
        if(!this.playersHaveEnoughMoneyLeft()) {
            IOUtil.displayMessage(Strings.PLAYERS_DONT_HAVE_ENOUGH_MONEY_LEFT);
            IOUtil.printPlayerBalances(this.players);
            System.exit(0);
        }

        IOUtil.displayMessage(Strings.HERE_ARE_PLAYER_BALANCES);
        IOUtil.printPlayerBalances(this.players);
        // Ask whether to continue playing
        char continuePlaying = IOUtil.askBinaryQuestion(Strings.CONTINUE_PLAYING_PROMPT, 'y', 'n');
        if(continuePlaying != 'y') {
            IOUtil.displayMessage(Strings.THANKS_FOR_PLAYING);
            System.exit(0);
        }

        // Shuffle if appropriate
        if(this.shoe.shouldBeReshuffled()) {
            this.shoe.addAll(this.discardPile);
            this.discardPile = new LinkedList<Card>();
            this.shoe.shuffle();
        }
    }


    /**
     * Returns whether at least one player has enough money to cover the minimum bet
     * @return true if at least one player has enough money to cover the minimum bet
     */
    private boolean playersHaveEnoughMoneyLeft() {
         for(int pi=0; pi<this.players.length; pi++) {
            Player p = players[pi];

            if(null == p || !p.isPlaying()) {
                continue;
            }

            if(p.getCashBalance() >= this.minBet) {
                return true;
            }
        }

        return false;

    }


    /**
     * Reads in various game parameters from the config file.
     */
    private void loadConfiguration(String configFilePath) {

        FileInputStream configFileIS = null;

        try
        {
            configFileIS = new FileInputStream(configFilePath);
        } catch (FileNotFoundException e)
        {
            System.err.println(Strings.CONFIG_FILE_NOT_FOUND + configFilePath);
            System.exit(2);
        } catch (Exception e)
        {
            System.err.println(Strings.UNEXPECTED_ERROR);
            e.printStackTrace();
            System.exit(3);
        }

        Properties configProperties = new Properties();

        try {
            configProperties.load(configFileIS);
        } catch (IOException ioe) {
            System.err.println(Strings.ERROR_LOADING_CONFIG_FILE);
            ioe.printStackTrace(System.err);
        }

        this.minNumberOfPlayers = Integer.parseInt(configProperties.getProperty(Strings.CONFIG_MIN_PLAYERS_KEY));
        this.maxNumberOfPlayers = Integer.parseInt(configProperties.getProperty(Strings.CONFIG_MAX_PLAYERS_KEY));
        this.numDecks = Integer.parseInt(configProperties.getProperty(Strings.CONFIG_NUM_DECKS_KEY));
        this.minBet = Integer.parseInt(configProperties.getProperty(Strings.CONFIG_MIN_BET_KEY));
        this.maxNumberOfSplits = Integer.parseInt(configProperties.getProperty(Strings.CONFIG_MAX_SPLITS_KEY));
        this.startingCashBalance = Integer.parseInt(configProperties.getProperty(Strings.CONFIG_STARTING_CASH_KEY));
        this.naturalPayoutFactor = Double.parseDouble(configProperties.getProperty(Strings.CONFIG_NATURAL_PAYOUT_FACTOR_KEY));
    }



    /**
     * Asks for and reads in the number of players at the table from the console.
     * @return the number of players input on the console
     */
    private int getNumberOfPlayers() {
        return IOUtil.askIntegerInRangeQuestion(null, Strings.ENTER_NUM_PLAYERS_PROMPT,
                this.minNumberOfPlayers, this.maxNumberOfPlayers);
    }


    /**
     * Main entry method.
     *
     * @param args a String array whose first element is the path to the config file
     */
    public static void main(String[] args) {

        String configFilePath = args[0];

        BlackjackGame game = new BlackjackGame(configFilePath);

        game.start();
    }

}
