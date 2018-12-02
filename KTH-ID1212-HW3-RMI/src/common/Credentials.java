/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

/**
 *
 * @author Tania
 */
import java.io.Serializable;

public class Credentials implements Serializable{
	
	private String username;
	private String password;
	
    public Credentials(String username, String password) {
        
    	this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
    
	public String toString() {
		return "username: " + username + "\npassword: " + password;
	}
}

