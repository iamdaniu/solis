import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.function.Consumer;

public class TcpWriter implements Consumer<byte[]> {
    private final InetAddress targetHost;
    private int port;

    public TcpWriter(String host, int port) throws UnknownHostException {
        targetHost = Inet4Address.getByName(host);
        this.port = port;
    }

    @Override
    public void accept(byte[] bytes) {
        try (Socket socket = new Socket(targetHost, port)) {
            socket.getOutputStream().write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
