package utils.team3;

// Java imports
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.InputStream;
import java.io.IOException;

public class CommonUtil{

  /**
   * Convert Object to byte array
   * @param Object to be converted
   *
   * @return byte[] of the object
   */
  // toByteArray and toObject are taken from: http://tinyurl.com/69h8l7x
  public static byte[] toByteArray(Object obj) throws IOException {
      byte[] bytes = null;
      ByteArrayOutputStream bos = null;
      ObjectOutputStream oos = null;
      try {
          bos = new ByteArrayOutputStream();
          oos = new ObjectOutputStream(bos);
          oos.writeObject(obj);
          oos.flush();
          bytes = bos.toByteArray();
      } finally {
          if (oos != null) {
              oos.close();
          }
          if (bos != null) {
              bos.close();
          }
      }
      return bytes;
  }

  /**
   * Convert inputstream to byte array
   * @param InputStream the stream to convert
   *
   * @return byte[] of the inputstream
   */
  public static byte[] inputStreamToByteArray(InputStream inputstream) throws Exception {
    byte[] buffer = new byte[8192];
    int bytesRead;
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    while ((bytesRead = inputstream.read(buffer)) != -1){
        output.write(buffer, 0, bytesRead);
    }
    byte[] array = output.toByteArray();
    output.close();
    return array;
  }
}
