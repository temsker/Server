import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import com.mysql.jdbc.Connection;
import com.tekapic.Login;
import com.tekapic.Picture;
import com.tekapic.User;


public class Sql {

	private  Connection connect;
	
	
	public ArrayList<String> getAlbums(String email) {
		
		String username;
		String album[] = {"me", "family", "friends", "love", 
		"pets", "nature", "sport", "persons", "animals", "vehicles", "views", "food", "things", "funny"};
		
		ArrayList<String> albums = new ArrayList<String>();
		PreparedStatement statement = null;	
		
		username = findUsernameByeEmail(email);
		
		for(int i = 0; i < album.length; i++) {
			
			try {
				statement = connect.prepareStatement("SELECT " + album[i] + " FROM " + username);
				
				ResultSet result = statement.executeQuery();
				
				while(result.next())
				{
				  if(result.getBoolean(1) == true) {
					  albums.add(album[i]);
					  break;
				  }	
					
				}
				
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
		
		return albums;
		
	}
	
	
	
	public ArrayList<Picture> getPictures(String email, String album) {
		
        ArrayList<Picture> pictures = new ArrayList<Picture>();
        String username = "none";
        PreparedStatement statement = null;
		
		//get the username
		username = findUsernameByeEmail(email);
		
				
		
		try {
			
			if(album.isEmpty()) {
				 statement = connect.prepareStatement("SELECT * FROM " + username);
			}
			else {
				statement = connect.prepareStatement("SELECT * FROM " + username + " WHERE " + album + " = true");
			}
				
			ResultSet result = statement.executeQuery();
			
			while(result.next())
			{
				byte[] imageBytes = result.getBytes(2);
				
				
				Picture picture = new Picture();
				picture.setPictureInByteArray(imageBytes);
				
				pictures.add(picture);
				
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    
        return pictures;
		
	}   
	
	public String findUsernameByeEmail(String email) {
		String username = "none";
		
	
		try {
			
			PreparedStatement statement = connect.prepareStatement("SELECT username  FROM users WHERE email = '" + email  + "';");                        

			ResultSet result = statement.executeQuery();
			
			while(result.next())
			{
			   username = result.getString(1);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
				
		}
		
		
		return username;
	}
	
	
	public void storePictureInUserTable(String email, Picture picture) {
		
		String username = "none";
		boolean albums[] = picture.getAlbums();
		
		
		//get the username
		username = findUsernameByeEmail(email);
		
		try {
			

		//save the picture object in table user 
		
		String sqlInsert = "INSERT INTO " + username + " (date, picture, me, family, friends, love, "
				+ "pets, nature, sport, persons, animals, vehicles, views, food, things, funny) values (?,?,?,"
				+ "?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		
			PreparedStatement pst = connect.prepareStatement(sqlInsert);
			
							
			pst.setString(1, picture.getDate());
			pst.setBytes(2, picture.getPictureInByteArray());	
			
			for(int i = 3; i < (Picture.numberOfAlbums + 3); i++) {
				pst.setBoolean(i, albums[i-3]);	
			}
						
			
			pst.execute();

	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
				
		}
			
	}
	
	
	public void createPicturesTableForTheUser(User user) {
		
		String sqlCreateTable = "CREATE TABLE " + user.getUsername() + "(date VARCHAR(255) NULL, picture LONGBLOB NULL, me tinyint(1) NULL,"
				+ "family tinyint(1) NULL, friends tinyint(1) NULL, love tinyint(1) NULL, pets tinyint(1) NULL, nature tinyint(1) NULL,"
				+ "sport tinyint(1) NULL, persons tinyint(1) NULL, animals tinyint(1) NULL, vehicles tinyint(1) NULL, "
				+ "views tinyint(1) NULL, food tinyint(1) NULL, things tinyint(1) NULL, funny tinyint(1) NULL);";
		
		try {
			PreparedStatement pst = connect.prepareStatement(sqlCreateTable);

			pst.execute();
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	public User getUserData(String email) {
		
		User user = new User();
		
		try {
			
			PreparedStatement statement = connect.prepareStatement("SELECT * FROM users WHERE email = '" + email + "';");                        

			ResultSet result = statement.executeQuery();
			

			while(result.next())
			{
				user.setUsername(result.getString(1));
				user.setEmail(result.getString(2));
				user.setMobileNumber(result.getString(3));
				user.setFullName(result.getString(4));
				user.setPassword(result.getString(5));
			}

			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return user;
	}
	
	private int chekIfUserExistsInDataBase(String field, String value) {
		
		int count = 0;
			
		try {
			
			PreparedStatement statement = connect.prepareStatement("SELECT COUNT(" + field + ") AS total FROM users WHERE " + field + "=" + "'" + value + "';");                        

			ResultSet result = statement.executeQuery();
			
			while(result.next())
			{
			    count += result.getInt("total");
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return count;
	}
	
	public String loginIntoSystem(Login login) {
		
		//check if email exits in database.
		if(chekIfUserExistsInDataBase("email", login.getEmail()) == 0 ) {
			return "0"; // if email doesn't exists in database.
		}
		
		
		//////////////////////////
		
		int count = 0;
		
		try {
			
			PreparedStatement statement = connect.prepareStatement("SELECT COUNT('email') AS total FROM users WHERE email = '" + login.getEmail() + "' AND password = '" + login.getPassword() + "';");                        

			ResultSet result = statement.executeQuery();
			
			while(result.next())
			{
			    count += result.getInt("total");
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(count == 0) {
			return "1"; //if wrong password.
		}
		

		return "2"; // if email and password are correct.
	}
	
	
	public  String registerNewUser(User user) {
		
		//check if mobile_number exits in database.
		if(chekIfUserExistsInDataBase("mobile_number", user.getMobileNumber()) != 0 ) {
			return "0";
		}
		
		//check if email exits in database.
		if(chekIfUserExistsInDataBase("email", user.getEmail()) != 0 ) {
			return "1";
		}
		
		//check if username exits in database.
		if(chekIfUserExistsInDataBase("username", user.getUsername()) != 0 ) {
			return "2";
		}
		
		

		String sqlInsert = "INSERT INTO users (username, email, mobile_number, full_name, password) values (?,?,?,?,?)";
		
		try {
			PreparedStatement pst = connect.prepareStatement(sqlInsert);
			
			pst.setString(1, user.getUsername());
			pst.setString(2, user.getEmail());
			pst.setString(3, user.getMobileNumber());
			pst.setString(4, user.getFullName());
			pst.setString(5, user.getPassword());
	
			
			pst.execute();
			createPicturesTableForTheUser(user);

			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			
		}
		
		return "3";
		
	}
	

	public  void connection()
	{
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Works");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public  void connectingToSQL ()
	{
		
		connection();
		String host = "jdbc:mysql://localhost:3306/tekapic_db";
		String username = "root";
		String password = "311216";
		
		
		try {
			 connect = (Connection) DriverManager.getConnection(host, username, password);
		System.out.println("work");
		System.out.println();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
