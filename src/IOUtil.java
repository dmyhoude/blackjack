import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Class providing utility methods for I/O.
 *
 * @author Dany Houde
 */
public class IOUtil {

    /**
     * Asks the user the specified question, without mentioning a player. The question has two
     * possible single-letter answers, and the user must enter one of those two. The answers are
     * case insensitive.
     *
     * @param question the question to ask the user
     * @param answer1 the first possible answer to the question
     * @param answer2 the second possible answer to the question
     * @return answer1 or answer2, based on the user's input
     */
    public static char askBinaryQuestion(String question, char answer1, char answer2) {
        StringBuilder questionBuilder = new StringBuilder(question);

        questionBuilder.append(" [");
        questionBuilder.append(answer1);
        questionBuilder.append(", ");
        questionBuilder.append(answer2);
        questionBuilder.append("]: ");

        char answer = 0;
        while(true) {
             System.out.print(questionBuilder);
             try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
                String input = bufferedReader.readLine();

                if(input.length() < 1) {
                    System.out.println(Strings.INVALID_ANSWER);
                    continue;
                }
                answer = input.charAt(0);
                if(Character.toUpperCase(answer) != Character.toUpperCase(answer1) &&
                        Character.toUpperCase(answer) != Character.toUpperCase(answer2)) {
                    System.out.println(Strings.INVALID_ANSWER);
                    continue;
                }

                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(Character.toUpperCase(answer) == Character.toUpperCase(answer1)) {
            return answer1;
        }

        return answer2;

    }


    /**
    * Asks the specified player a question. The question has two possible single-letter answers,
    * and the player must enter one of those two. The answers are case insensitive.
    *
    * @param question the question to ask the user
    * @param answer1 the first possible answer to the question
    * @param answer2 the second possible answer to the question
    * @return answer1 or answer2, based on the user's input
    */
    public static char askBinaryQuestion(Player aPlayer, String question, char answer1, char answer2) {
        StringBuilder questionBuilder = new StringBuilder();

        if(null != aPlayer) {
            questionBuilder.append("Player ");
            questionBuilder.append(aPlayer.getNumber());
            questionBuilder.append(", ");
        }

        questionBuilder.append(question);

        return askBinaryQuestion(questionBuilder.toString(), answer1, answer2);
    }


    /**
     * Pause the program and display a message to the user indicating he must
     * press Enter to continue.
     */
    public static void waitForEnterToContinue() {
         System.out.print(Strings.PRESS_ENTER_TO_CONTINUE);

         try {
           BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            bufferedReader.readLine();
         } catch (IOException e) {
            e.printStackTrace(System.err);
         }
    }


    /**
     * Pause the program and display a message to the user indicating he must
     * press Enter to continue.
     * @param message the additional message to display to the user, in addition
     *                to pressing Enter.
     */
    public static void displayMessageAndWait(String message) {
         System.out.println(message);

         waitForEnterToContinue();
    }


     /**
     * Pause the program and display a message to the specified player, indicating
     * he must press Enter to continue.
     * @ param p the player to direct the message to
     * @param message the additional message to display to the user, in addition
     *                to pressing Enter.
     */
    public static void displayMessageAndWait(Player p, String message) {
         displayMessage(p, message);

         waitForEnterToContinue();
    }


    /**
     * Display the provided message to the user and move to a new line
     * This method does not pause program execution.
     *
     * @param message the message to display to the user.
     */
    public static void displayMessage(String message) {
        System.out.println(message);
    }

    public static void displayMessage(Player p, String message) {
        StringBuilder messageBuilder = new StringBuilder();

        if(null != p) {
            messageBuilder.append("Player ");
            messageBuilder.append(p.getNumber());
            messageBuilder.append(" ");
        }

        messageBuilder.append(message);
        System.out.println(messageBuilder.toString());
    }


    /**
     * Asks the provided question to the user, and expects an integer answer between
     * the specified limits, inclusively. The user is provided with the range of acceptable
     * answers.
     *
     * @param question the question asked to the user
     * @param lowerLimit the lowest possible acceptable answer
     * @param upperLimit the highest possible acceptable answer
     * @return the answer provided by the player
     */
    public static int askIntegerInRangeQuestion(String question, int lowerLimit, int upperLimit) {

        StringBuilder questionBuilder = new StringBuilder(question);

        questionBuilder.append(" [");
        questionBuilder.append(lowerLimit);
        questionBuilder.append(" to ");
        questionBuilder.append(upperLimit);
        questionBuilder.append("]: ");

        int answer = 0;
        while(true) {
             System.out.print(questionBuilder);
             try {
                 BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
                 String input = bufferedReader.readLine();
                 answer = Integer.parseInt(input);

                 if(answer < lowerLimit || answer > upperLimit) {
                     System.out.println(Strings.INVALID_ANSWER);
                     continue;
                 }

                 break;
             }  catch (NumberFormatException nfe) {
                System.out.println(Strings.INVALID_ANSWER);
                continue;
             } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }

        return answer;

    }


    /**
     * Display the current cash balances for all provided players.
     * @param players the players whose cash balances to display
     */
    public static void printPlayerBalances(Player[] players) {

        for(int i=0; i<players.length; i++) {
            Player p = players[i];
            if(null == p) {
                continue;
            }
            StringBuilder outputBuilder = new StringBuilder();

            outputBuilder.append("Player ");
            outputBuilder.append(p.getNumber());
            outputBuilder.append(":\t$");
            outputBuilder.append(p.getCashBalance());

            System.out.println(outputBuilder.toString());
        }
    }


    /**
     * Asks the provided question to the specified player, and expects an integer answer between
     * the specified limits, inclusively. The player is provided with the range of acceptable
     * answers.
     *
     * @param aPlayer the player to direct the question to
     * @param question the question asked to the user
     * @param lowerLimit the lowest possible acceptable answer
     * @param upperLimit the highest possible acceptable answer
     * @return the answer provided by the player
     */
    public static int askIntegerInRangeQuestion(Player aPlayer, String question, int lowerLimit, int upperLimit) {

        StringBuilder questionBuilder = new StringBuilder();

        if(null != aPlayer) {
            questionBuilder.append("Player ");
            questionBuilder.append(aPlayer.getNumber());
            questionBuilder.append(", ");
        }

        questionBuilder.append(question);

        return askIntegerInRangeQuestion(questionBuilder.toString(), lowerLimit, upperLimit);
    }


    public static void main(String[] args) {

        Player testPlayer = new Player(0, 500);

        System.out.println(askBinaryQuestion(testPlayer, "Would you want a or b?", 'a', 'b'));
        char continuePlaying = IOUtil.askBinaryQuestion(Strings.CONTINUE_PLAYING_PROMPT, 'y', 'n');
        System.out.println("Continue playing? " + continuePlaying);
    }


}
