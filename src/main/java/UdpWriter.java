import java.io.IOException;
import java.net.*;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Consumer;

public class UdpWriter implements Consumer<byte[]> {
    static final String TARGET_PORT = "udp_target_port";
    static final String TARGET_HOST = "udp_target_host";
    static final String UDP_PORT = "udp_send_port";

    // the port we send from
    private final int localPort;
    // the host and port we send the data to
    private final InetAddress targetHost;
    private final int targetPort;

    private UdpWriter(String host, int targetPort, int localPort) {
        try {
            targetHost = Inet4Address.getByName(host);
            this.targetPort = targetPort;
            this.localPort = localPort;
        } catch (UnknownHostException e) {
            throw new RuntimeException("Cannot resolve " + host, e);
        }
    }

    @Override
    public void accept(byte[] bytes) {
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length, targetHost, targetPort);
        try (DatagramSocket socket = new DatagramSocket(localPort)) {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static UdpWriter fromProperties() {
        String host = System.getenv().get(TARGET_HOST);
        if (host == null) {
            System.out.println("no udp target host given in " + TARGET_HOST + ", udp retransmit disabled");
            return null;
        }
        String port = Optional.ofNullable(System.getenv().get(TARGET_PORT)).orElse("9999");
        String localPort = Optional.ofNullable(System.getenv().get(UDP_PORT)).orElse("9999");
        System.out.println("udp resend active from port " + localPort + " to " + host + ":" + port);
        return new UdpWriter(host, Integer.parseInt(port), Integer.parseInt(localPort));
    }
}
