/**
 * A class containing most UI strings used in the game.
 * Could be a starting point for eventual localization.
 *
 * @author Dany Houde
 */
public class Strings {

    // Config file keys
    public static final String CONFIG_MIN_PLAYERS_KEY = "minplayers";
    public static final String CONFIG_MAX_PLAYERS_KEY = "maxplayers";
    public static final String CONFIG_NUM_DECKS_KEY = "numdecks";
    public static final String CONFIG_MIN_BET_KEY = "minbet";
    public static final String CONFIG_MAX_SPLITS_KEY = "maxsplits";
    public static final String CONFIG_STARTING_CASH_KEY = "startingcash";
    public static final String CONFIG_NATURAL_PAYOUT_FACTOR_KEY = "naturalpayoutfactor";

    // Early game messages
    public static final String WELCOME_MESSAGE = "Welcome to DH Blackjack!\n";
    public static final String TWO_CARDS_WILL_BE_FLIPPED = "Two cards will now be dealt for each player.";


    // Game play questions
    public static final String ENTER_NUM_PLAYERS_PROMPT = "Please enter a number of players ";
    public static final String ENTER_BET_AMOUNT_PROMPT = "please enter your bet";
    public static final String WANT_TO_HIT_OR_STAND_PROMPT = "would you want to hit or stand for this hand?";
    public static final String WANT_TO_DOUBLE_DOWN_PROMPT = "would you want to double down for this hand?";
    public static final String WANT_TO_SPLIT_PROMPT = "would you want to split for this hand?";


    public static final String WILL_REVEAL_CARDS = "Hidden cards will now be flipped.";
    public static final String DEALER_HAS_NATURAL = "The dealer has a natural! The players lose all hands which are not naturals.";
    public static final String PLAYER_HAS_NATURAL = "has a natural! His gains will be added to his wager immediately.";
    public static final String PLAYERS_DONT_HAVE_ENOUGH_MONEY_LEFT = "No player has enough money left to play. The game will now end, thanks for playing!";
    public static final String DEALER_WILL_NOW_PLAY = "The dealer will now play.";
    public static final String ROUND_IS_OVER = "The round is over! You can see the final table above.";
    public static final String HERE_ARE_PLAYER_BALANCES = "Here are the current player balances:";
    public static final String CONTINUE_PLAYING_PROMPT = "Would you like to play one more round?";
    public static final String THANKS_FOR_PLAYING = "Thanks for playing!";
    public static final String PRESS_ENTER_TO_CONTINUE = "Press Enter to continue...";


    // Exception and error messages
    public static final String ERROR_LOADING_CONFIG_FILE = "Error while loading config file:";
    public static final String CONFIG_FILE_NOT_FOUND = "Config file could not be found: ";
    public static final String UNEXPECTED_ERROR = "Unexpected error!";
    public static final String INVALID_ANSWER = "Invalid answer, please try again.";

    public static final String CANNOT_DEAL_FROM_EMPTY_SHOE = "Cannot deal from empty shoe!";
    public static final String CANNOT_DEBIT_AMOUNT_FROM_BALANCE_0 = "Cannot debit ";
    public static final String CANNOT_DEBIT_AMOUNT_FROM_BALANCE_1 = " from balance ";
    public static final String CANNOT_DEBIT_AMOUNT_FROM_BALANCE_2 = ": insufficient funds available";


}
