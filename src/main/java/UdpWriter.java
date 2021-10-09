import java.io.IOException;
import java.net.*;
import java.util.function.Consumer;

public class UdpWriter implements Consumer<byte[]> {
    private final InetAddress targetHost;
    private final int port;

    UdpWriter(String host, int port) throws UnknownHostException {
        targetHost = Inet4Address.getByName(host);
        this.port = port;
    }

    @Override
    public void accept(byte[] bytes) {
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
        try (DatagramSocket socket = new DatagramSocket(port, targetHost)) {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
