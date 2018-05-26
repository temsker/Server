import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

import com.tekapic.Login;
import com.tekapic.Picture;
import com.tekapic.User;

public class SocketHandler extends Thread {
	Socket incoming;
	Sql sql;
	
	
	SocketHandler(Socket _in, Sql sql)
	{
		incoming=_in;
		this.sql = sql;
	}
	
	public void run()
	{
	    Object object = null;
	    User user = null;
	    Login login = null;
	    String email = null;
	    String stringFromUser = null;
	    Picture picture = null;
	    
	    
		try
		{

		   ObjectInputStream objectInputStream =  // read objcet from client
	        		   new ObjectInputStream (incoming.getInputStream());
	    
           BufferedReader bufferedReader =  // read string from client
              new BufferedReader(new
              InputStreamReader(incoming.getInputStream())); 
          
               
           DataOutputStream  dataOutputStream =  // write string to client
        		   new DataOutputStream (incoming.getOutputStream() );
           
           
           ObjectOutputStream objectOutputStream = // write object to client
                   new ObjectOutputStream(incoming.getOutputStream() );
                  
           
       while(true) {  
    	   
           
     	  try {
    		  
     		 object = objectInputStream.readObject();
			  	
 	  
	        	  if(object instanceof User) {
	        		  user = (User) object;
	        		  
	        		 String result = sql.registerNewUser(user);
	        		 
	        		 
	        		 dataOutputStream.writeBytes(result + '\n');
	        		 
	        	  }
	        	  
	        	  if(object instanceof Login) {
	        		  login = (Login) object;
	        		  
	        		 String result = sql.loginIntoSystem(login);
	        		     		 
	        		dataOutputStream.writeBytes(result + '\n');
	        		
	       		 	if(result.equals("2")) {
	       		 		email = login.getEmail();
        			  while(true) {
        				 //in the client, now user in the profile activity.
        				 //if user goes back to login activity, need to send to here
        				 //some indication ("0")for break from this while loop
        				 //for example:
        				    stringFromUser = bufferedReader.readLine();
        				  	if(stringFromUser.equals("logOut")){
        				  		break;
        				  	}
        				  	
        				  	else if(stringFromUser.equals("getAccountDetails")) {
        				  		user = sql.getUserData(email); 
        				  		objectOutputStream.writeObject(user);
        				  	}
        				  	else if(stringFromUser.equals("storePicture")) {
        				  		object = objectInputStream.readObject();
        			        	  if(object instanceof Picture) {
        			        		  picture = (Picture) object;
        			        		  //System.out.println(picture.getDate());
        			        		 sql.storePictureInUserTable(email, picture);        			        		    			         			        		 
        			        	  }
        				  	}
        				  	else {
        				  		
        				  	}
        				  
        				  
        				 
        				 
        			 }
        			 
	       		 	}
	        		 
	        	  }
	        	  

	        	  
     		 				
       
     	  } catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
           
	}
		
		
		
	}
		catch(IOException e)
		{
			e.printStackTrace();
			
		}

	}

}