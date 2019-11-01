package ie.gmit.ds.grpc_project;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import ie.gmit.ds.HashRequest;
import ie.gmit.ds.HashResponse;
import ie.gmit.ds.PasswordServiceGrpc;
import ie.gmit.ds.ValidationRequest;
import ie.gmit.ds.ValidationResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class Client {
	
	private static final Logger logger = Logger.getLogger(Client.class.getName());
	private final ManagedChannel channel;
	private final PasswordServiceGrpc.PasswordServiceBlockingStub passwordClientStub;

	public Client(String host, int port) {
		this.channel = ManagedChannelBuilder.forAddress(host, port)
				.usePlaintext()
				.build();
		passwordClientStub = PasswordServiceGrpc.newBlockingStub(channel);
	}

	public void shutdown() throws InterruptedException {
		channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
	}

	public void hash(int userId, String password) {
	
		HashRequest request;
		HashResponse response;
		
		request = HashRequest.newBuilder()
				.setUserId(userId)
				.setPassword(password)
				.build();

		try {
				response = passwordClientStub.hash(request);
				
		} catch (StatusRuntimeException e) {
			logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
			return;
		}
		
		System.out.println("\nUser ID: " + response.getUserId() + "\nSalt: " + response.getSalt().toString() + "\nHashed Password: " 
				+ response.getHashedPassword().toString());
	} // hash

	
	public void validate(String password, String salt, String hashedPassword) {
	
		ValidationRequest request = ValidationRequest.newBuilder()
				.setPassword(password)
				.setHashedPassword(hashedPassword)
				.setSalt(salt)
				.build();
		
		ValidationResponse response;
		
		try {
				response = passwordClientStub.validate(request);
				
				boolean passwordsMatch = response.getPasswordValid();
				System.out.println("\nPasswords match: " + passwordsMatch);
				
		} catch (StatusRuntimeException e) {
			logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
			return;
		}
		
	} // validate

	public static void main(String[] args) throws Exception {

		Scanner console = new Scanner(System.in);

		Client client = new Client("localhost", 10000);

		try {
				System.out.print("Enter User ID Number: ");
				int userId = console.nextInt();
	
				System.out.print("\nEnter Password: ");
				String password = console.nextLine();
				password = console.nextLine();

				client.hash(userId, password);
	
				System.out.print("\nPlease enter your password: ");
				String pass = console.nextLine();
	
				System.out.print("\nEnter Salt: ");
				String salt = console.nextLine();
				
				System.out.print("\nEnter Hashed Password: ");
				String hash = console.nextLine();
				
				client.validate(pass, salt, hash);

		} finally {
			console.close();
			client.shutdown();
		}
	}
	
	

}
