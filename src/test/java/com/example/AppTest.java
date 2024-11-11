package com.example;

import org.testng.annotations.Test;
import org.testng.annotations.Test;
import java.net.HttpURLConnection;
import java.net.URL;
import static org.testng.Assert.assertEquals;

public class AppTest {

    @Test
    public void testAppRunning() throws Exception {
        // Start the server (App.main will run on a separate thread)
        String[] args = {};
        new Thread(() -> App.main(args)).start();

        // Wait for the server to start (or use a more robust solution like health check)
        Thread.sleep(5000);  // Simulate waiting for server to be ready

        // Send a request to the server
        URL url = new URL("http://localhost:8080/api/users");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        int responseCode = con.getResponseCode();
        assertEquals(responseCode, 200);  // Assuming 200 OK for a successful request
    }
}
