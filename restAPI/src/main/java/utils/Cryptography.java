package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/*
 * Cryptography class handles the encryption and decryption of the files in the application.
 * This class applies the singleton pattern where only one instance of it will be created and used.
 */
public class Cryptography {
	public static final int KEY_SIZE = 256 ;
	public static final int IV_SIZE = 16 ;
	public static final String AES_METHOD = "AES/CBC/PKCS5Padding" ;
	private Key key = null;
	private static Cryptography cryptoInstance = new Cryptography();
	private static final String CONFIG_FILE_PATH = "/usr/keys/crypto.conf";
	
	private Cryptography() {
		loadKey();
	}
	
	/*
	 * Use this method to get the instance of the cryptopgrahy class. 
	 * This controls the instance for this singleton class. 
	 */
	public static Cryptography getInstance() {
		return cryptoInstance;
	}
	
	/*
	 * This method will encrypt the byte[] message with the given key and iv. 
	 * Then it will embed the iv into the front of the encrypted message and returned.
	 */
	public byte[] encrypt(byte[] message) {
		IvParameterSpec iv = generateIV();
		byte[] encryptedData = null;
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		} catch(NoSuchAlgorithmException e) {
			System.out.println("Exception while encrypting. Algorithm being requested is not available in this environment " + e); 
			return null;
		}
		catch(NoSuchPaddingException e) {
			System.out.println("Exception while encrypting. Padding Scheme being requested is not available this environment " + e);
			return null;
		}

		try {
			cipher.init(Cipher.ENCRYPT_MODE, key, iv);
		} catch (InvalidKeyException e) {
			System.out.println("Exception while encrypting. Invalid key. It could be due to invalid encoding, wrong length or uninitialized " + e) ;
			return null;
		} catch (InvalidAlgorithmParameterException e) {
			System.out.println("Exception while encrypting. Invalid algorithm parameters  " + e) ;
			return null;
		}

		try {
			encryptedData = cipher.doFinal(message);
		} catch (IllegalBlockSizeException e) {
			System.out.println("Exception while encrypting, due to block size " + e) ; 
			return null;
		}catch(BadPaddingException e) {
			System.out.println("Exception while encrypting, due to padding scheme " + e) ;
			return null;
		}
		ByteBuffer buffer = ByteBuffer.allocate(encryptedData.length + IV_SIZE);
		buffer.put(iv.getIV()).put(encryptedData);
		return buffer.array();
	}

	/*
	 * This method will take in an encrypted data and a decryption key. Then it will extract
	 * the iv from the encrypted data and use the iv and the key to decrypt the data, 
	 * returning the plain text.
	 */
	public byte[] decrypt(byte[] encryptedData) {
		byte[] plaintext = null;
		Cipher cipher = null;
		byte[] iv = new byte[IV_SIZE];
		ByteBuffer buffer = ByteBuffer.wrap(encryptedData);
		buffer = buffer.get(iv).slice();
		byte[] data = new byte[buffer.remaining()];
		buffer.get(data);		
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Exception while decrypting. Algorithm being requested is not available in environment " + e);  
			return null;
		} catch(NoSuchPaddingException e) {
			System.out.println("Exception while decrypting. Padding scheme being requested is not available in environment " + e);
			return null;
		}
		try {
			cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
		} catch(InvalidKeyException e) {
			System.out.println("Exception while encrypting. Key being used is not valid. It could be due to invalid encoding, wrong length or uninitialized " + e); 
			return null;
		} catch(InvalidAlgorithmParameterException e) {
			System.out.println("Exception while encrypting. Algorithm Param being used is not valid. " + e) ;
			return null;
		}
		try {
			plaintext = cipher.doFinal(data) ;
		} catch(IllegalBlockSizeException e) {
			System.out.println("Exception while decryption, due to block size " + e) ; 
			return null;
		} catch(BadPaddingException e) {
			System.out.println("Exception while decryption, due to padding scheme " + e) ;
			return null;
		}

		return plaintext;
	}

	/*
	 * This method will generate a AES Key of KEY_SIZE.
	 */
	private boolean generateKey() {
		if(key != null) {
			return false;
		}
		
		KeyGenerator keygen;
		try {
			keygen = KeyGenerator.getInstance("AES");
			keygen.init(KEY_SIZE) ; // Key size is specified here.
			byte[] newKey = keygen.generateKey().getEncoded();

			key = new SecretKeySpec(newKey, "AES");
			System.out.println("Generating key");
			return true;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // key generator to be used with AES algorithm.
		return false;
	}

	/*
	 * This method will create an IV using Java SecureRandom class, which
	 * provides a cryptographically strong random number generator.
	 */
	public IvParameterSpec generateIV() {
		byte[] iv = new byte[IV_SIZE];
		SecureRandom secRandom = new SecureRandom() ;
		secRandom.nextBytes(iv);
		return new IvParameterSpec(iv);
	}
	
	/*
	 * This method checks the config file if it exists. 
	 * If exists, load config.
	 * Else, generate a new AES key and create a config file.
	 */
	private void loadKey(){
		Path path = Paths.get(CONFIG_FILE_PATH);
		File file = path.toFile();
		if(file.exists()) {
			loadConfig(path);
		} else {
			generateKey();
			String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());
			//Use try-with-resource to get auto-closeable writer instance
			try (BufferedWriter writer = Files.newBufferedWriter(path))
			{
			    writer.write(encodedKey);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * This method loads the config file and updates the key used.
	 */
	private void loadConfig(Path path) {
		String encodedKey = "";
		try (BufferedReader reader = Files.newBufferedReader(path)) {
			encodedKey = reader.readLine();
		    System.out.println("Loading key");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// decode the base64 encoded string
		byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
		// rebuild key using SecretKeySpec
		key = new SecretKeySpec(decodedKey, "AES"); 
	}

}
