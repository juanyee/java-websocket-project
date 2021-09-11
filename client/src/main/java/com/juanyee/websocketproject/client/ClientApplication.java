package com.juanyee.websocketproject.client;

import com.juanyee.websocketproject.client.endpoint.ClientManagerSingleton;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.net.URISyntaxException;

@SpringBootApplication
public class ClientApplication {

	public static void main(String[] args) throws DeploymentException, URISyntaxException, IOException {
		SpringApplication.run(ClientApplication.class, args);
		System.out.println("Hello world");
		ClientManagerSingleton.getInstance().conectarWebSocket();
	}

}
