package utils.team3;

import javax.crypto.spec.SecretKeySpec;
import utils.Cryptography;

public class Encryption extends Cryptography{

  private String keyString = "";
  public void setKey(String key){
    this.keyString = key;
    super.key = new SecretKeySpec(keyString.getBytes(), "AES");
  }
}
