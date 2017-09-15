import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

/**
 * Media streaming utility
 *
 * Reference from https://github.com/aruld/jersey-streaming/blob/master/src/main/java/com/aruld/jersey/streaming/MediaStreamer.java
 */
public class MediaStreamer implements StreamingOutput {

    private int length;
    private RandomAccessFile file;
    final byte[] buffer = new byte[4096];

    public MediaStreamer(int length, RandomAccessFile file) {
        this.length = length;
        this.file = file;
    }

    @Override
    public void write(OutputStream outputStream) throws IOException, WebApplicationException {
        try {
            while( length != 0) {
                int read = file.read(buffer, 0, buffer.length > length ? length : buffer.length);
                outputStream.write(buffer, 0, read);
                length -= read;
            }
        } finally {
            file.close();
        }
    }

    public int getLenth() {
        return length;
    }
}