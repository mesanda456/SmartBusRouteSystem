package com.example.demo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Application Class
 * Run this to start the Spring Boot server
 */
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
		System.out.println("\n" +
				"╔═══════════════════════════════════════════════╗\n" +
				"║   🚌 Bus Route Planner API Started! 🚌      ║\n" +
				"║                                               ║\n" +
				"║   Server running on: http://localhost:8080   ║\n" +
				"║                                               ║\n" +
				"║   Available Endpoints:                        ║\n" +
				"║   • GET  /api/health                         ║\n" +
				"║   • GET  /api/stops                          ║\n" +
				"║   • POST /api/route                          ║\n" +
				"║   • GET  /api/route?source=A&destination=E   ║\n" +
				"╚═══════════════════════════════════════════════╝\n");
	}
}