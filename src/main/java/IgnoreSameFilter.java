import java.util.Arrays;
import java.util.function.Predicate;

public class IgnoreSameFilter implements Predicate<byte[]> {
    private byte[] previous;

    @Override
    public boolean test(byte[] bytes) {
        boolean result = !Arrays.equals(previous, bytes);
        previous = bytes;
        return result;
    }
}
