
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


/**
 * Unit tests for the Player class.
 *
 * @author Dany Houde
 */
public class PlayerTest {

    Player testPlayer;

    @Before
    public void setupTestPlayer() {
        int testPlayerNumber = 7;
        int testStartingBalance = 1000;
        testPlayer = new Player(testPlayerNumber, testStartingBalance);
    }

    @Test
    public void testPlayerConstructor() {
        assertEquals("Player number should be 7", 7, testPlayer.getNumber());
        assertEquals("Player cash balance should be 1000", 1000, testPlayer.getCashBalance());
        assertEquals("Player should be playing", true, testPlayer.isPlaying());

        assertNotNull("Player list of hands should not be null", testPlayer.getHands());
        assertEquals("Player list of hands should have size 1", 1, testPlayer.getHands().size());
    }


    @Test
    public void testSplitAceHand() {
        PlayerHand testHand = new PlayerHand();

        testHand.addCard(new Card(Card.Suit.CLUBS, Card.Rank.ACE, true));
        testHand.addCard(new Card(Card.Suit.SPADES, Card.Rank.ACE, true));
        testHand.setWager(10);

        testPlayer.addHand(testHand);

        try {
            PlayerHand splitHand = testPlayer.splitHand(testPlayer.getHands().size()-1);

            assertEquals("Hand should be marked as having come from split Aces", true, splitHand.cameFromSplitAces());
            assertEquals("Hand should be marked as having come from split Aces", true, testHand.cameFromSplitAces());
        } catch (UnsplittableHandException uhe) {
            Assert.fail("Hand with two aces should be splittable");
        }

    }



}
