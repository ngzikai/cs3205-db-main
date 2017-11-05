package restapi.team2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import utils.Cryptography;

@Path("team2/encrypt")
public class EncryptFiles {
	
	private int count = 0;
	
	@GET
	public void encryptFiles() {
		walk("/Users/zikai/Downloads/output/output_data");
		System.out.println(count + " files will be encrypted");
	}
	
	public void walk( String path ) {

		File root = new File( path );
		File[] list = root.listFiles();

		if (list == null) return;

		for ( File f : list ) {
			if ( f.isDirectory() ) {
				walk( f.getAbsolutePath() );
				//System.out.println( "Dir:" + f.getAbsoluteFile() );
			}
			else {
				System.out.println( "File:" + f.getAbsoluteFile() );
				Cryptography crypto = Cryptography.getInstance();
				
				try {
					System.out.println(f.toPath());
					byte[] plaintext = Files.readAllBytes(f.toPath());
					byte[] encrypted = crypto.encrypt(plaintext);
					
					FileOutputStream fos = new FileOutputStream(f.getPath());
					try {
						fos.write(encrypted);
					} finally {
						fos.close();
					}
					
				} catch (IOException e) {
					System.out.println("Unable to read file: " + f.getAbsolutePath());
				}
				
				count++;
			}
		}
		
	}

}
