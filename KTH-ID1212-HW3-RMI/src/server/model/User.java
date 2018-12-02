/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.model;

/**
 *
 * @author Tania
 */
import java.rmi.RemoteException;

import common.Client;

//represents an individual user of the file system 
//holds data related to a client
public class User {
	
	private Client remoteNode;
	private UserManager userManager;
	private String username;			//unique for the user
	
	public User(String username, Client remoteNode, UserManager userManager) {
		
		this.username = username;
		this.remoteNode = remoteNode;
		this.userManager = userManager;
	}
	
	public void send(String notification) {
		
		try {
			remoteNode.receiveMessage(notification);
		} 
		catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public String getUsername() {
		return username;
	}

}

