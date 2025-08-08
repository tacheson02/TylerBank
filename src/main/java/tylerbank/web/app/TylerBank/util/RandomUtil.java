package tylerbank.web.app.TylerBank.util;

/**
 * A utility class for generating random numbers
 * @since v2.1
 */
public class RandomUtil {

    /**
     * Generates a random long number with a specified length
     * @param length The desired length of the random number
     * @return A random long number of the specified length
     * @since v2.1
     */
    public long generateRandom(int length){
        StringBuilder sb = new StringBuilder(length);
        for(int i=0; i<length; i++){
            int randomNum = (int) (Math.random() * 10);
            sb.append(randomNum);
        }
        return Long.parseLong(sb.toString());
    }
}
