/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.controller;

/**
 *
 * @author Tania
 */
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import common.Client;
import common.Credentials;
import common.Server;
import server.integration.DatabaseAccess;
import server.model.Files;
import server.model.UserManager;

// class that implements the remote interface on the server side
// export operation (make Java RMI aware of the fact that remotely callable objects exist) is performed in the UnicastRemoteObject

//ska bara skicka emellan

public class Controller extends UnicastRemoteObject implements Server{

	UserManager userManager = new UserManager();
	DatabaseAccess databaseAccess = new DatabaseAccess();
	
	//UnicastRemoteObject constructor will be executed, it calls the superclass constructor
	public Controller() throws RemoteException {}
	
	@Override
	public Boolean credentialCheck(Credentials credentials) {
		
		Boolean credentialCheck = databaseAccess.checkLogin(credentials);
		
		if (!credentialCheck) {
			return false;
		}
		else {
		return true;
		}
	}

	@Override
	public String login(Client remoteNode, Credentials credentials) throws RemoteException {
		
		String clientName = userManager.addUser(remoteNode, credentials);		
		return clientName;	
	}

	@Override
	public void logout(String clientName) throws RemoteException {
		
		userManager.removeUser(clientName);		
	}

	@Override
	public void register(Credentials credentials) throws RemoteException {
		
		databaseAccess.register(credentials);
	}

	@Override
	public void unregister(Credentials credentials) throws RemoteException {
		
		databaseAccess.unregister(credentials);;
	}

	@Override
	public void upload(Files file) throws RemoteException {
		
		databaseAccess.uploadFile(file);
	}

	@Override
	public Files download(String fileName, String client) throws RemoteException {
		
		Files file = databaseAccess.downloadFile(fileName);
		
		if (file.getNotification().equals("yes")) {
			
			userManager.sendNotification(file.getOwner(), client, "view");
		}
		
		return file;
	}
	
	@Override
	public void update(Files file, String client) throws RemoteException {
		
		
		if (file.getNotification().equals("yes")) {
			
			userManager.sendNotification(file.getOwner(), client, "update");
		}
		
		databaseAccess.updateFile(file);
	}

	@Override
	public void delete(String fileName) throws RemoteException {
		
		Files file = databaseAccess.downloadFile(fileName);
		
		databaseAccess.deleteFile(fileName);
	}
	
	@Override
	public List<Files> list() throws RemoteException {
		
		List<Files> files = databaseAccess.listFiles();
		
		return files;
	}

	@Override
	public void sendMessage(int id, String message) throws RemoteException {
		// TODO Auto-generated method stub
		
	}






}

