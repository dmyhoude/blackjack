/**
 * Gets thrown when trying to deal a card from a shoe which has been exhausted.
 *
 * @author Dany Houde
 */
public class EmptyShoeException extends Exception {

    public EmptyShoeException() {
        super(Strings.CANNOT_DEAL_FROM_EMPTY_SHOE);
    }

}
