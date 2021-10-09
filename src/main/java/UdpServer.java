import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class UdpServer {
    private final int port;

    private final List<Consumer<byte[]>> consumers = new ArrayList<>();

    public UdpServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws IOException {
        new UdpServer(4545)
                .addConsumer(new ConsoleOutput(8))
                .addConsumer(new FileStorage("/logs"))
                .addConsumer(new UdpWriter("rhas", 9999))
//                .addConsumer(new TcpWriter("rhas", 9999))
                .start();
    }

    public UdpServer addConsumer(Consumer<byte[]> consumer) {
        this.consumers.add(consumer);
        return this;
    }

    private void start() {
        byte[] buf = new byte[1024];
        try (DatagramSocket socket = new DatagramSocket(port)) {
            //noinspection InfiniteLoopStatement
            while (true) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);

                socket.receive(packet);

                final byte[] actualData = new byte[packet.getLength()];
                System.arraycopy(packet.getData(), 0, actualData, 0, packet.getLength());

                consumers.forEach(c -> c.accept(actualData));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

