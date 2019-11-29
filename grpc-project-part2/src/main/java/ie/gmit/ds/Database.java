package ie.gmit.ds;

import java.util.*;

public class Database {
	
	private HashMap<Integer, User> userDatabase = new HashMap<Integer, User>();
	static Database database = new Database();
	
	private Database() {
		
	}
	
	public static Database getInstance() {
		return database;
	}
	
	// Add User
	public void addUser(User user) {
		userDatabase.put(user.getUserId(), user);
	}
	
	// List All Users
	public Collection<User> listUsers() {
		return userDatabase.values();
	}
	
	// Get User by ID
	public User getUser(int id) {
		return userDatabase.get(id);
	}
	
	// Edit User
	public boolean updateUser(int id, User user) {
		if (userDatabase.get(id) == null) {
			return false;
		} else {
			userDatabase.replace(id, user);
			return true;
		}
	}

	// Delete User
	public boolean deleteUser(int id) {
		if(userDatabase.get(id) == null) {
			return false;
		} else {
			userDatabase.remove(id);
			return true;
		}
	}
	
	// Add Hashed Password and Salt to User
	public boolean hashAndSalt(int id, String hashedPassword, String salt) {
		User user = userDatabase.get(id);
		
		if (user == null || hashedPassword == null || salt == null) {
			System.out.println("\nError...");
			return false;
		} else {
			user.setHashedPassword(hashedPassword);
			user.setSalt(salt);
			System.out.println("\nHashed..." + hashedPassword + salt);
			return true;
		}
	}

}
