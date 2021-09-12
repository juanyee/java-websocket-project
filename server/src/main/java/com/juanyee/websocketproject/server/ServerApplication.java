package com.juanyee.websocketproject.server;

import com.juanyee.websocketproject.server.endpoint.AppServerEndpoint;
import org.glassfish.tyrus.server.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;
import java.util.logging.Level;

@SpringBootApplication
public class ServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
		ServerApplication app = new ServerApplication();
		app.runServer();
	}

	private void runServer() {

		Set<Class<?>> beanClasses = getClassesFromString(AppServerEndpoint.class.getName());//com.b1soft.centrarlclierserver.endpoint.CentralServerEndpoint
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("org.glassfish.tyrus.incomingBufferSize", 524288000);

		Server server = new Server(
				"localhost", // localhost
				8025, // 8025
				"/central", // /central
				properties,
				beanClasses);

		try {
			server.start();
			System.out.println("");
			System.out.println("Press any key to stop the server..");
			new Scanner(System.in).nextLine();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			server.stop();
		}
	}

	private Set<Class<?>> getClassesFromString(String rawString) {
		Set<Class<?>> beanClasses = new HashSet<Class<?>>();
		StringTokenizer st = new StringTokenizer(rawString, ";");
		while (st.hasMoreTokens()) {
			String nextClassname = st.nextToken().trim();
			if (!"".equals(nextClassname)) {
				try {
					beanClasses.add(Class.forName(nextClassname));
				} catch (ClassNotFoundException cnfe) {
					throw new RuntimeException("Stop: cannot load class: " + nextClassname);
				}
			}
		}
		return beanClasses;
	}
}
