import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

class FileStorage implements Consumer<byte[]> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss-SSS");
    private final String baseDir;

    FileStorage(String dir) throws IOException {
        Files.createDirectories(Paths.get(dir));
        baseDir = dir;
    }

    @Override
    public void accept(byte[] bytes) {
        var timestamp = FORMATTER.format(LocalDateTime.now());
        String filename = timestamp + ".data";

        Path path = Paths.get(baseDir, filename);
        System.out.println("writing to " + path);
        try (var output = new FileOutputStream(Files.createFile(path).toFile())) {
            output.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
