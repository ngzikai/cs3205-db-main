package utils;

import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

public class SystemConfig{
  public static final String resourceLocation = "serverInfo";
  public static final ResourceBundle resourceBundle = ResourceBundle.getBundle(resourceLocation);

  public SystemConfig(){
  }

  public static String getConfig(String key){
    try{
      return resourceBundle.getString(key);
    }catch(Exception e){
      return "key not found";
    }
  }

  public static <T> T getConfig(final String key, final Class<T> clazz) {
      if (clazz == byte[].class) {
          return clazz.cast(getConfig(key).getBytes(StandardCharsets.UTF_8));
      } else if (clazz == long.class || clazz == Long.class) {
          return clazz.cast(Long.valueOf(getConfig(key)));
      } else {
          return clazz.cast(getConfig(key));
      }
  }

}
