import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class UdpServer {
    private final int port;
    private final List<Consumer<byte[]>> consumers = new ArrayList<>();
    private final Predicate<byte[]> filter = new IgnoreSameFilter();

    public UdpServer(int port) {
        this.port = port;
    }

    void addConsumer(Consumer<byte[]> consumer) {
        consumers.add(consumer);
    }

    void start() {
        byte[] buf = new byte[1024];
        try (DatagramSocket socket = new DatagramSocket(port)) {
            //noinspection InfiniteLoopStatement
            while (true) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);

                socket.receive(packet);

                final byte[] actualData = new byte[packet.getLength()];
                System.arraycopy(packet.getData(), 0, actualData, 0, packet.getLength());

                if (filter.test(actualData)) {
                    consumers.forEach(c -> consumeSafely(actualData, c));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void consumeSafely(byte[] data, Consumer<byte[]> consumer) {
        try {
            consumer.accept(data);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
        }
    }
}

