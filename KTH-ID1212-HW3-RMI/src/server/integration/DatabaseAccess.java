package server.integration;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


import common.Credentials;
import server.model.Files;
import server.model.User;

//class for accessing the database and handling all the database calls
public class DatabaseAccess {
	
	private Files file;
	private Boolean credentialCheck;
	
	private PreparedStatement registerStmt;
	private PreparedStatement unregisterStmt;
	private PreparedStatement uploadFileStmt;
	private PreparedStatement deleteFileStmt;
	private PreparedStatement updateFileStmt;
	private PreparedStatement downloadFileStmt;
	private PreparedStatement listFilesStmt;
	private PreparedStatement checkLoginStatement;
	
	
	// connect to the database
	public DatabaseAccess() {
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/id1212", "root", "root");
			prepareStatements(connection);
		} 
		catch (SQLException | ClassNotFoundException e) {	
			e.printStackTrace();
		}	
	}	
	
	//register a user
	public void register(Credentials credentials) throws DatabaseAccessException{
		String exceptionMsg = "Username exist";
		try {
			registerStmt.setString(1, credentials.getUsername());
			registerStmt.setString(2, credentials.getPassword());
			int rows = registerStmt.executeUpdate();
                        if (rows!=1){
                            throw new DatabaseAccessException(exceptionMsg);
                        }
		} 
		catch (SQLException e) {
			
			throw new DatabaseAccessException(exceptionMsg, e);
		}
	}
	
	//unregister a user
	public void unregister(Credentials credentials) {	
		
		try {
			unregisterStmt.setString(1, credentials.getUsername());
			unregisterStmt.setString(2, credentials.getPassword());
			unregisterStmt.executeUpdate();
		} 
		catch (SQLException e) {
			
			System.out.println("Could not unregister user");
			e.printStackTrace();
		}
	}
	
	public void checkUsername() {
		
	}
	
	//upload a file, insert a file to the database
	public void uploadFile(Files file) {
		
		try {
			uploadFileStmt.setString(1, file.getName());
			uploadFileStmt.setInt(2, file.getSize());
			uploadFileStmt.setString(3, file.getOwner());
			uploadFileStmt.setString(4, file.getAccess());
			uploadFileStmt.setString(5, file.getPermission());
			uploadFileStmt.setString(6, file.getNotification());
			uploadFileStmt.executeUpdate();
		} 
		catch (SQLException e) {
			System.out.println("Could not upload file");
			e.printStackTrace();
		}
	}
	
	public void updateFile(Files file) {
		
		try {
			
			updateFileStmt.setInt(1, file.getSize());
			updateFileStmt.setString(2, file.getOwner());
			updateFileStmt.setString(3, file.getAccess());
			updateFileStmt.setString(4, file.getPermission());
			updateFileStmt.setString(5, file.getNotification());
			updateFileStmt.setString(6, file.getName());
			
			updateFileStmt.executeUpdate();
		} 
		catch (SQLException e) {
			
			System.out.println("Could not update file");
			e.printStackTrace();
		}
	}
	
	//download a file from the database
	public Files downloadFile(String fileName) {
		
		try {
			
			downloadFileStmt.setString(1, fileName);
			ResultSet result = downloadFileStmt.executeQuery();
			
			while (result.next()) {
				
				file = new Files(result.getString(1), result.getInt(2), result.getString(3), result.getString(4), result.getString(5), result.getString(6));
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		System.out.println(file.toString());
		
		return file;
		
	}
	
	//delete a file, remove from database
	public void deleteFile(String fileName) {
		
		try {
			deleteFileStmt.setString(1, fileName);
			deleteFileStmt.executeUpdate();
		} 
		catch (SQLException e) {
			System.out.println("File could not be deleted");
			e.printStackTrace();
		}	
	}
	
	public List<Files> listFiles() {
		
		List<Files> files = new ArrayList<>();
		
		try {
			
			ResultSet result = listFilesStmt.executeQuery();
			while (result.next()) {
				
				file = new Files(result.getString(1), result.getInt(2), result.getString(3), result.getString(4), result.getString(5), result.getString(6));
				files.add(file);
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return files;
	}
	
	//download a file from the database
	public Boolean checkLogin(Credentials credentials) {
		
		try {
			checkLoginStatement.setString(1, credentials.getUsername());
			checkLoginStatement.setString(2, credentials.getPassword());
			ResultSet result = checkLoginStatement.executeQuery();
			
			if (result.next()) {
				
				credentialCheck = true;
			}
			else {
				
				credentialCheck = false;	
				}
			
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return credentialCheck;	
	}
	
	
	public void prepareStatements(Connection connection) throws SQLException {
		
		registerStmt = connection.prepareStatement("insert into user values(?, ?)");
		unregisterStmt = connection.prepareStatement("delete from user where username = ? and password = ?");
		uploadFileStmt = connection.prepareStatement("insert into file values(?, ?, ?, ?, ?, ?)");
		deleteFileStmt = connection.prepareStatement("delete from file where fileName = ?");
		downloadFileStmt = connection.prepareStatement("select * from file where fileName = ?");
		listFilesStmt = connection.prepareStatement("select * from file");
		checkLoginStatement = connection.prepareStatement("select * from user where username = ? and password = ?");
		
	}
}
