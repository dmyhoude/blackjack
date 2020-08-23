/**
 * A class representing a French set of playing card.
 *
 * @author Dany Houde
 */
public class Card {

    public enum Rank { DEUCE, THREE, FOUR, FIVE, SIX,
        SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE;

        @Override
        public String toString() {
            switch(this) {
                case DEUCE:
                    return "2";
                case THREE:
                    return "3";
                case FOUR:
                    return "4";
                case FIVE:
                    return "5";
                case SIX:
                    return "6";
                case SEVEN:
                    return "7";
                case EIGHT:
                    return "8";
                case NINE:
                    return "9";
                case TEN:
                    return "10";
                case JACK:
                    return "J";
                case QUEEN:
                    return "Q";
                case KING:
                    return "K";
                 case ACE:
                    return "A";
                default:
                    return "X";
            }
        }

        public int getPipValue() {
            switch(this) {
                case ACE:
                    return 1;
                case DEUCE:
                    return 2;
                case THREE:
                    return 3;
                case FOUR:
                    return 4;
                case FIVE:
                    return 5;
                case SIX:
                    return 6;
                case SEVEN:
                    return 7;
                case EIGHT:
                    return 8;
                case NINE:
                    return 9;
                case TEN:
                    return 10;
                case JACK:
                case QUEEN:
                case KING:
                    return 10;
                default:
                    return Integer.MIN_VALUE;
            }
        }
    }

    public enum Suit { CLUBS, DIAMONDS, HEARTS, SPADES;

        @Override
        public String toString() {
            switch(this) {
                case CLUBS:
                    return "♣";
                case DIAMONDS:
                    return "♦";
                case HEARTS:
                    return "♥";
                case SPADES:
                    return "♠";
                default:
                    return "X";
            }

        }

    }

    // Representation of a facing down card
    private static final String FACE_DOWN_REPRESENTATION = "???";

    // If false, then this Card's face is down. Down by default.
    private boolean isFaceUp;

    private final Suit suit;
    private final Rank rank;

    /**
     * Construct a new Card of the provided Suit and Rank.
     * @param aSuit
     * @param aRank
     * @param faceUp whether the Card should be facing up on creation
     */
    public Card(Suit aSuit, Rank aRank, boolean faceUp) {
       this.suit = aSuit;
       this.rank = aRank;
       this.isFaceUp = faceUp;
    }

    /**
     * @return the Card's "pip" (numeric) value
     */
    public int getPipValue() {
        return this.rank.getPipValue();
    }


    public void setFaceUp(boolean faceUp) {
        this.isFaceUp = faceUp;
    }

    public Suit getSuit() {
        return this.suit;
    }

    public Rank getRank() {
        return this.rank;
    }

    public boolean isFaceUp() {
        return this.isFaceUp;
    }

    /**
     * @return a String representation of this Card
     */
    public String toString() {
        if(this.isFaceUp) {
            return this.suit.toString() + this.rank.toString();
        } else {
            return FACE_DOWN_REPRESENTATION;
        }
    }


    public static void main(String[] args) {
        Card testCard = new Card(Suit.CLUBS, Rank.TEN, true);

        System.out.println(testCard);
    }

}
