import java.util.function.Consumer;

class ConsoleOutput implements Consumer<byte[]> {
    private final int lineLength;

    ConsoleOutput(int lineLength) {
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
}
