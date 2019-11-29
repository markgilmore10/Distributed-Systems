package ie.gmit.ds;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement
public class User {
	
	@NotNull
	private int userId;
	@NotNull
	private String username;
	@NotNull
	private String email;
	@NotNull
	private String password;
	private String hashedPassword;
	private String salt;
	
	public User() {
		
	}
	
	// for create and update
	public User(int userId, String username, String email, String password) {
			
		this.userId = userId;
		this.username = username;
		this.email = email;
		this.password = password;
		
	}
	
	// for display user
	public User(int userId, String username, String email, String password, String hashedPassword, String salt) {
		
		this.userId = userId;
		this.username = username;
		this.email = email;
		this.password = password;
		this.hashedPassword = hashedPassword;
		this.salt = salt;
	}

	@JsonProperty
	@XmlElement(required = true)
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@JsonProperty
	@XmlElement(required = true)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@JsonProperty
	@XmlElement(required = true)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@JsonProperty
	@XmlElement(required = true)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@JsonProperty
	@XmlElement(required = true)
	public String getHashedPassword() {
		return hashedPassword;
	}

	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}

	@JsonProperty
	@XmlElement(required = true)
	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}
	
}
