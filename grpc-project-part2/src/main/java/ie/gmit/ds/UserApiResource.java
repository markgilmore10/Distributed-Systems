package ie.gmit.ds;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.*;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/users")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})

public class UserApiResource {
	
	private Client cli;
	private ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	private Validator validator = factory.getValidator();
	Set<ConstraintViolation<User>> violation;
	Database database = Database.getInstance();

	public UserApiResource() {
	   cli = new Client();
	}
	
	@GET
	public Collection<User> getUsers() {
		Collection<User> users = database.listUsers();
		return users;
	   
	} // Get Users
	
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response addUser(User user) {
		violation = validator.validate(user);
		
		if(!violation.isEmpty()) {
			return Response.status(404).entity("Unable to add user..." + violation.toString()).build();
		} else {
			database.addUser(user);
			cli.hash(user);
			return Response.status(200).entity("User added...").build();
		}
		
	} // Add User
	
	@Path("/{userId}")
	@GET
	public Response getUserById(@PathParam("userId") int id) {
		if(database.getUser(id) == null) {
			return Response.status(404).type(MediaType.APPLICATION_JSON).entity("Not found getuserbyid").build(); 
		} else { 
			return Response.status(200).type(MediaType.APPLICATION_JSON).entity("gotbyid user" + database.getUser(id)).build();
		}
		
	} // Get User By Id
	
	@Path("/{userId}")
	@PUT
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response updateUser(@PathParam("userId") int id, User user) {
		if(database.updateUser(id, user) == false) {
			return Response.status(404).type(MediaType.APPLICATION_JSON).entity("Not found").build(); 
		} else { 
			return Response.status(200).type(MediaType.APPLICATION_JSON).entity("updated user" + database.updateUser(id, user)).build();
		}
		
	} // Update User
	
	@Path("/{userId}")
	@DELETE
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response deleteUser(@PathParam("userId") int id) {
		if(database.deleteUser(id) == false) {
			return Response.status(404).type(MediaType.APPLICATION_JSON).entity("Not found delete user").build(); 
		} else { 
			return Response.status(200).type(MediaType.APPLICATION_JSON).entity("deleted user" + database.deleteUser(id)).build();
		}
		
	} // Delete User
	
	@Path("/login")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response login(HashResponse response) {
		if(cli.validate(response.getUserId(), response.getHashedPassword(), response.getSalt()))
		{
			return Response.status(200).type(MediaType.APPLICATION_JSON).entity("Logged in...").build();
		} else {
			return Response.status(400).type(MediaType.APPLICATION_JSON).entity("Unable to log in...").build();
		}
		
	} // Login
	
}
