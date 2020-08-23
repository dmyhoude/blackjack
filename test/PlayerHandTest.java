import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for PlayerHand class.
 *
 * @author Dany Houde
 */
public class PlayerHandTest {

    private PlayerHand testHand;

    @Before
    public void setupBasicHand() {
        testHand = new PlayerHand();
    }


    @Test
    public void testCanBeSplit() {
        testHand.addCard(new Card(Card.Suit.CLUBS, Card.Rank.DEUCE, true));
        testHand.addCard(new Card(Card.Suit.SPADES, Card.Rank.DEUCE, true));
        testHand.setWager(10);

        assertEquals("Hand with $10 wager and two same-rank cards should be splittable with $10",
                true, testHand.canBeSplit(10));
    }


    @Test
    public void testCanBeSplitDifferentRanks() {
        testHand.addCard(new Card(Card.Suit.CLUBS, Card.Rank.TEN, true));
        testHand.addCard(new Card(Card.Suit.SPADES, Card.Rank.QUEEN, true));
        testHand.setWager(10);

        assertEquals("Hand with two same-valued, different Ranks should be splittable",
                true, testHand.canBeSplit(10));
    }

    @Test
    public void testCannotBeSplitWrongRank() {
        testHand.addCard(new Card(Card.Suit.CLUBS, Card.Rank.THREE, true));
        testHand.addCard(new Card(Card.Suit.SPADES, Card.Rank.DEUCE, true));
        testHand.setWager(10);

        assertEquals("Hand with two different card ranks should NOT be splittable even with sufficient money",
                false, testHand.canBeSplit(10));
    }

    @Test
    public void testCannotBeSplitInsufficientMoney() {
        testHand.addCard(new Card(Card.Suit.CLUBS, Card.Rank.DEUCE, true));
        testHand.addCard(new Card(Card.Suit.SPADES, Card.Rank.DEUCE, true));
        testHand.setWager(20);

        assertEquals("Hand with $20 wager and two same-rank cards should NOT be splittable with $10",
                false, testHand.canBeSplit(10));
    }



}
