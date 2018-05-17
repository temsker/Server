import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.imageio.ImageIO;
import com.mysql.jdbc.Connection;
import com.tekapic.Login;
import com.tekapic.User;


public class Sql {

	private  Connection connect;
	
	
	
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