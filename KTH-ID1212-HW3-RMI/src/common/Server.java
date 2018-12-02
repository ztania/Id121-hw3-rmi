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
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import server.model.Files;

// remote methods clients can call on the server
public interface Server extends Remote {
	
	public static final String SERVER_REGISTRY_NAME = "Server";
	
	//login/logout for client
	//returns the id of the logged in/out client
	String login(Client remoteNode, Credentials credentials) throws RemoteException;
	void logout(String clientName) throws RemoteException;
	
	//register/unregister client
	//returns the id of the client
	void register(Credentials credentials) throws RemoteException;
	void unregister(Credentials credentials) throws RemoteException;
	
	void upload(Files file) throws RemoteException;
	Files download(String fileName, String client) throws RemoteException;
	void update(Files file, String client) throws RemoteException;

	void delete(String fileName) throws RemoteException;
	
	List<Files> list() throws RemoteException;
	
	// used to send message
	void sendMessage(int id, String message) throws RemoteException;
	
	Boolean credentialCheck(Credentials credentials) throws RemoteException;
	
}

