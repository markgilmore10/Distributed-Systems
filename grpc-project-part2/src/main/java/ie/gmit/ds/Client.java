package ie.gmit.ds;

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
import io.grpc.stub.StreamObserver;

public class Client {
	
	private static final Logger logger = Logger.getLogger(Client.class.getName()); 
	private final ManagedChannel channel;
	private final PasswordServiceGrpc.PasswordServiceStub asyncPasswordStub;
	private final PasswordServiceGrpc.PasswordServiceBlockingStub syncPasswordStub;

	public Client() {
		
		this.channel = ManagedChannelBuilder.forAddress("localhost", 10001)
				.usePlaintext()
				.build();
		
		asyncPasswordStub = PasswordServiceGrpc.newStub(channel);
		syncPasswordStub = PasswordServiceGrpc.newBlockingStub(channel);
	}

	public void shutdown() throws InterruptedException {
		channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
	}

	
	public void hash(final User user) {
		
		StreamObserver<HashResponse> responseObserver = new StreamObserver<HashResponse>() {
		
		@Override
		public void onNext(HashResponse response) {
			
			Database database = Database.getInstance();

			User newUser = new User(user.getUserId(), user.getUsername(), user.getEmail(), user.getPassword(), response.getHashedPassword(), response.getSalt());
			
			database.addUser(newUser);
			
		}

		@Override
		public void onError(Throwable t) {
			System.out.println("\nError... ");
		}

		@Override
		public void onCompleted() {
			System.out.println("\nDone...");
		}
		
		
	};
		HashRequest request;
		
		request = HashRequest.newBuilder()
				.setUserId(user.getUserId())
				.setPassword(user.getPassword())
				.build();

		try {
			
			asyncPasswordStub.hash(request, responseObserver);
				
		} catch (StatusRuntimeException e) {
			logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
			return;
		}
		
		
	} // hash

	
	public boolean validate(int userId, String hashedPassword, String salt) {
	
		ValidationRequest request = ValidationRequest.newBuilder()
				.setHashedPassword(hashedPassword)
				.setSalt(salt)
				.build();
		
		ValidationResponse response;
		
		try {
				response = syncPasswordStub.validate(request);
				
				boolean passwordsMatch = response.getPasswordValid();
				
				System.out.println("\nPasswords match: " + passwordsMatch);
				
				return passwordsMatch;
				
		} catch (StatusRuntimeException e) {
			logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
			return false;
		}
		
		
	} // validate

	
		
}
	