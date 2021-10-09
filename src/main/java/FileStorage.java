import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Consumer;

class FileStorage implements Consumer<byte[]> {
    static final String STORAGE_DIR = "file_storage_dir";

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss-SSS");
    private final String baseDir;

    private FileStorage(String dir) {
        try {
            Files.createDirectories(Paths.get(dir));
        } catch (IOException e) {
            throw new RuntimeException("Failed to create " + dir, e);
        }
        baseDir = dir;
    }

    static FileStorage fromProperties() {
        String storageDir = System.getenv().get(STORAGE_DIR);
        if (storageDir != null) {
            FileStorage storage = new FileStorage(storageDir);
            System.out.println("file storage of data active, storing to " + storageDir);
            return storage;
        }
        System.out.println("storage directory not given in " + STORAGE_DIR + ", file storage not active");
        return null;
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
