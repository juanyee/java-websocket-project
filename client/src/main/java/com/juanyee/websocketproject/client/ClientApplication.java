package com.juanyee.websocketproject.client;

import com.juanyee.websocketproject.client.endpoint.ClientManagerSingleton;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

@SpringBootApplication
public class ClientApplication {

	public static void main(String[] args) throws DeploymentException, URISyntaxException, IOException {
		SpringApplication.run(ClientApplication.class, args);
		ClientManagerSingleton.getInstance().connectToWebSocket();
		ClientManagerSingleton.getInstance().sendMessage("Hi, are you there?");

		System.out.println("Press any key to stop the client..");
		new Scanner(System.in).nextLine();
	}

}
