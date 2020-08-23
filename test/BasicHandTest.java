import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for BasicHand class.
 *
 * @author Dany Houde
 */
public class BasicHandTest {

    private BasicHand testHand;

    @Before
    public void setupBasicHand() {
        testHand = new BasicHand();
    }

    @Test
    public void testEmptyHandValue() {
        assertEquals("Empty hand should have value 0", 0, testHand.getValue());
    }

    @Test
    public void testSingleDeuceHandValue() {
        testHand.addCard(new Card(Card.Suit.CLUBS, Card.Rank.DEUCE, true));
        assertEquals("Hand with single deuce should have value 2", 2, testHand.getValue());
    }
    @Test
    public void testSingleAceHandValue() throws Exception {
        testHand.addCard(new Card(Card.Suit.CLUBS, Card.Rank.ACE, true));

        assertEquals("Hand with a single ace should have value 11", 11, testHand.getValue());
    }

    @Test
    public void testTwoAceHandValue() throws Exception {

        testHand.addCard(new Card(Card.Suit.CLUBS, Card.Rank.ACE, true));
        testHand.addCard(new Card(Card.Suit.SPADES, Card.Rank.ACE, true));

        assertEquals("Hand with two aces should have value 12", 12, testHand.getValue());
    }

    @Test
    public void testTwoAcesOneTenHandValue() throws Exception {

        testHand.addCard(new Card(Card.Suit.CLUBS, Card.Rank.ACE, true));
        testHand.addCard(new Card(Card.Suit.SPADES, Card.Rank.ACE, true));
        testHand.addCard(new Card(Card.Suit.HEARTS, Card.Rank.TEN, true));

        assertEquals("Hand with two aces and one ten should have value 1+1+10=12", 12, testHand.getValue());
    }

    @Test
    public void testThreeAceHandValue() throws Exception {

        testHand.addCard(new Card(Card.Suit.CLUBS, Card.Rank.ACE, true));
        testHand.addCard(new Card(Card.Suit.SPADES, Card.Rank.ACE, true));
        testHand.addCard(new Card(Card.Suit.HEARTS, Card.Rank.ACE, true));

        assertEquals("Hand with three aces should have value 11 + 1 + 1 = 13", 13, testHand.getValue());
    }




}

