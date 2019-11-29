package ie.gmit.ds.grpc_project;

import java.io.IOException;
import java.util.logging.Logger;

import ie.gmit.ds.HashRequest;
import ie.gmit.ds.HashResponse;
import ie.gmit.ds.PasswordServiceGrpc;
import ie.gmit.ds.ValidationRequest;
import ie.gmit.ds.ValidationResponse;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

public class Server {
	
	private io.grpc.Server grpcServer;
    private static final Logger logger = Logger.getLogger(Server.class.getName());
    private static final int PORT = 10001;
    
    public static void main(String[] args) throws IOException, InterruptedException {
        final Server server = new Server();
        server.start();
        server.blockUntilShutdown();
    }
    
    private void start() throws IOException {
        grpcServer = ServerBuilder.forPort(PORT)
                .addService(new PasswordServiceImpl())
                .build()
                .start();
        logger.info("Server listening on " + PORT);

    }

    private void stop() {
        if (grpcServer != null) {
            grpcServer.shutdown();
        }
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (grpcServer != null) {
            grpcServer.awaitTermination();
        }
    }
    
    static class PasswordServiceImpl extends PasswordServiceGrpc.PasswordServiceImplBase {
    	
    	private Password passwordManager = new Password();
    	private String password;
    	private byte[] salt;
    	private byte[] hashedPass;
    	private boolean valid;
    
    	
    	@Override
    	public void hash(HashRequest request, StreamObserver<HashResponse> responseObserver) {
    		
    		password = (request.getPassword());
    		salt = passwordManager.getNextSalt();
        	hashedPass = passwordManager.hashPassword(password,salt);
        	
    		HashResponse reply = HashResponse.newBuilder().setUserId(request.getUserId())
    				.setHashedPassword(hashedPass.toString())
    				.setSalt(salt.toString())
    				.build();
    		responseObserver.onNext(reply);
    		responseObserver.onCompleted();

    	} // hash
    	
    	@Override
    	public void validate(ValidationRequest request, StreamObserver<ValidationResponse> responseObserver) {
    		
        	valid = passwordManager.isExpectedPassword(request.getPassword(), request.getSalt().getBytes(), request.getHashedPassword().getBytes());
        	
			ValidationResponse reply = ValidationResponse.newBuilder()
					.setPasswordValid(valid)
					.build();
			responseObserver.onNext(reply);
			responseObserver.onCompleted();
			
    	} // validate
    }

}
