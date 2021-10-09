import java.util.Optional;
import java.util.Properties;
import java.util.function.Consumer;

class ConsoleOutput implements Consumer<byte[]> {
    static final String DISABLE_CONSOLE = "console_disabled";
    static final String LINE_LENGTH = "console_line_length";
    private final int lineLength;

    private ConsoleOutput(int lineLength) {
        this.lineLength = lineLength;
    }

    @Override
    public void accept(byte[] bytes) {
        int currentPosition = 0;
        int limit = Math.min(lineLength, bytes.length - currentPosition);
        while (currentPosition < bytes.length) {
            System.out.println(outputLine(bytes, currentPosition, currentPosition + limit));
            currentPosition += lineLength;
            limit = Math.min(lineLength, bytes.length - currentPosition);
        }
    }
    private String outputLine(byte[] data, int startPosition, int endPosition) {
        String result = String.format("%02x: ", startPosition);
        for (int i = startPosition; i < endPosition; i++) {
            result += String.format("%02x ", data[i]);
        }
        return result;
    }

    static ConsoleOutput fromProperties() {
        if (System.getenv().containsKey(DISABLE_CONSOLE)) {
            System.out.println("console output of data inactive due to set " + DISABLE_CONSOLE);
            return null;
        }
        Integer lineLength = Optional.ofNullable(System.getenv().get(LINE_LENGTH))
                .map(Integer::parseInt)
                .orElse(8);
        System.out.println("console output of data active with line width " + lineLength);
        return new ConsoleOutput(lineLength);
    }
}
