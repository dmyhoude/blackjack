/**
 * Thrown when a player does not have enough cash to cover the attempted operation.
 *
 * @author Dany Houde
 */
public class InsufficientCashException extends Exception {

    public InsufficientCashException(int cashBalance, int amount) {
        super(Strings.CANNOT_DEBIT_AMOUNT_FROM_BALANCE_0 + amount +
              Strings.CANNOT_DEBIT_AMOUNT_FROM_BALANCE_1 + cashBalance +
              Strings.CANNOT_DEBIT_AMOUNT_FROM_BALANCE_2);
    }

}
