package controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import utils.Cryptography;
import utils.db.MySQLAccess;

public class TimeseriesController {

	public Response getTimeseries(String key) {
		String sql = "SELECT value FROM secrets WHERE `key` = ?";

		Connection connect = MySQLAccess.connectDatabase();
		String value = null;

		try {
			PreparedStatement ps = connect.prepareStatement(sql);
			ps.setString(1, key);

			ResultSet rs = MySQLAccess.readDataBasePS(ps);

			while(rs.next()) {
				value = rs.getString(1);
			}

			if(value != null) {
				sql = "DELETE FROM secrets WHERE `key` = ?";

				ps = connect.prepareStatement(sql);
				ps.setString(1, key);

				MySQLAccess.updateDataBasePS(ps);

			}else {
				System.out.println("value: " + value);
				System.out.println("Unable to Find Results With Time Series");
				throw new Exception();
			}

		} catch(Exception e) {
			e.printStackTrace();
			MySQLAccess.close();
			return null;
		}
		
		sql = "SELECT creationdate, content FROM data WHERE type = 'GMSv5_2-Foot_R' AND isAvailable = 1 AND uid = ? ORDER BY creationdate ASC";
		
		LinkedList<String> paths = new LinkedList<String>();
		
		try {
			PreparedStatement ps = connect.prepareStatement(sql);
			ps.setString(1, value);
			
			ResultSet rs = MySQLAccess.readDataBasePS(ps);
			
			while(rs.next()) {
				paths.addFirst(rs.getString(2));
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			MySQLAccess.close();
			return null;
		}
		
		MySQLAccess.close();
		
		
		if(paths.isEmpty()) {
			return null;
		}else {
			return generateResponse(paths, key);
		}
	}
	
	public Response generateResponse(LinkedList<String> paths, String key) {
		Cryptography crypto = Cryptography.getInstance();
		
		StreamingOutput fileStream = new StreamingOutput() {
			@Override
			public void write(java.io.OutputStream output) throws IOException, WebApplicationException{
				try {
					while(!paths.isEmpty()) {
						java.nio.file.Path path = Paths.get(paths.getFirst());
						byte[] data = Files.readAllBytes(path);
						byte[] decrypt = crypto.decrypt(data);
						output.write(decrypt);
						output.flush();
					}
				} catch(Exception e) {
					//HANDLE ERROR RESPONSE
					throw new WebApplicationException("File Not Found");
				}
			}
		};
		
		return Response.ok(fileStream, MediaType.APPLICATION_OCTET_STREAM).header("content-disposition", "attachment; filename = " + key +".json").build(); 
		
	}

}
