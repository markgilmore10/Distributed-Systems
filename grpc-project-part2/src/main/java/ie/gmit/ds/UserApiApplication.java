package ie.gmit.ds;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class UserApiApplication extends Application<UserApiConfig> {

	public static void main(String[] args) throws Exception {
		new UserApiApplication().run(args);
	}

	public void run(UserApiConfig configuration, Environment environment) throws Exception {
		
		UserApiResource resource = new UserApiResource();
		ExampleHealthCheck healthCheck = new ExampleHealthCheck();

        environment.jersey().register(resource);
        environment.healthChecks().register("example", healthCheck);
		
	}

}
