import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Consumer;

public class JsonRewriter implements Consumer<byte[]> {
    private static final String TARGET_URL = "json_rewrite_url";
    private final URL targetUrl;
    private final ObjectMapper objectMapper;

    public JsonRewriter(URL target) {
        targetUrl = target;
        objectMapper = new ObjectMapper();
    }

    static JsonRewriter fromProperties() {
        String s = System.getenv().get(TARGET_URL);
        if (s != null) {
            try {
                URL target = new URL(s);
                return new JsonRewriter(target);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                throw new RuntimeException("invalid target URL " + s, e);
            }
        } else {
            System.out.println("json rewrite disabled - target url not set");
        }
        return null;
    }

    @Override
    public void accept(byte[] bytes) {
        if (bytes.length < 248) {
            System.out.println("ignoring data of length " + bytes.length);
            return;
        }
        Payload payload = new Payload();
        payload.setCurrentPower(readInt16(bytes, 72));
        payload.setYieldToday(readInt16(bytes, 76));
        payload.setYieldMonth(readInt16(bytes, 120));
        payload.setYieldTotal(readInt16(bytes, 130));

        try {
            HttpURLConnection httpCon = (HttpURLConnection) targetUrl.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setRequestMethod("PUT");
            try (OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream())) {
                String content = objectMapper.writeValueAsString(payload);
                System.out.println("rewriting to " + targetUrl + " as json: " + content);
                out.write(content);
            }
            httpCon.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int readInt16(byte[] bytes, int i) {
        int result = bytes[i+1] << 8;
        result += bytes[i];
        // unsigned
        return 0xFF & result;
    }
}

@Data
class Payload {
    private int currentPower;
    private int yieldToday;
    private int yieldMonth;
    private int yieldTotal;
}