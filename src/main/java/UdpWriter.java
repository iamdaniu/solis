import java.io.IOException;
import java.net.*;
import java.util.function.Consumer;

public class UdpWriter implements Consumer<byte[]> {
    private static final int LOCAL_PORT = 9999;
    private final InetAddress targetHost;
    private final int targetPort;

    UdpWriter(String host, int targetPort) throws UnknownHostException {
        targetHost = Inet4Address.getByName(host);
        this.targetPort = targetPort;
    }

    @Override
    public void accept(byte[] bytes) {
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length, targetHost, targetPort);
        try (DatagramSocket socket = new DatagramSocket(LOCAL_PORT)) {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
