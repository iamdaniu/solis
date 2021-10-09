import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Main {
    private static final String LISTEN_PORT = "listen_port";

    public static void main(String[] args) {
        if (args.length > 0) {
            displayHelp();
            return;
        }

        var listenPort = Integer.parseInt(Optional.ofNullable(System.getenv().get(LISTEN_PORT))
                .orElseThrow(() -> new IllegalArgumentException("property not set: " + LISTEN_PORT)));

        UdpServer udpServer = new UdpServer(listenPort);

        // add data handlers
        List<Supplier<Consumer<byte[]>>> cf = List.of(
                ConsoleOutput::fromProperties,
                FileStorage::fromProperties,
                UdpWriter::fromProperties
        );
        for (Supplier<Consumer<byte[]>> consumers : cf) {
            Optional.ofNullable(consumers.get())
                    .ifPresent(udpServer::addConsumer);
        }
        udpServer.start();
    }

    private static void displayHelp() {
        System.out.println("available environment variables:");
        System.out.println("- general:");
        System.out.println(" - " + LISTEN_PORT + " - the udp port to listen to (required)");
        System.out.println("- console:");
        System.out.println(" - " + ConsoleOutput.DISABLE_CONSOLE + " - set to disable output");
        System.out.println(" - " + ConsoleOutput.LINE_LENGTH + " - bytes per output line (default 8)");
        System.out.println("- file:");
        System.out.println(" - " + FileStorage.STORAGE_DIR + " - target directory (required)");
        System.out.println("- udp resend:");
        System.out.println(" - " + UdpWriter.TARGET_HOST + " - target host (required)");
        System.out.println(" - " + UdpWriter.TARGET_PORT + " - target port (default 9999)");
        System.out.println(" - " + UdpWriter.UDP_PORT + " - local port to send from (default 9999)");
    }

}
