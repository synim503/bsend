package com.bsend.aladin.core;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TaskExecutor {
    private String[] lines;
    private ExecutorService executor;

    public TaskExecutor(String[] lines, int count) {
        this.lines = lines;
        this.executor = Executors.newFixedThreadPool(count);
    }

    public void run() {
        for (String line : lines) {
            Future<Boolean> future = executor.submit(() -> {
                String[] credentials = line.split(":");
                String email = credentials[0];
                String password = credentials[1];
                String smtp = getSmptByDomain(email.split("@")[1]);
                System.out.println();
                // Логика для попытки автор
                checkCredentials(smtp, email, password);
                return true;
            });
        }
        executor.shutdown();
    }

    private boolean checkCredentials(String smtp, String email, String password) {
        Properties props = new Properties();
        props.put("mail.smtp.host", smtp);
        props.put("mail.smtp.port", 465);
        props.put("mail.smtp.timeout", "5000"); // Timeout in milliseconds

        Session session = Session.getDefaultInstance(props, null);
        Transport transport = null;

        try {
            transport = session.getTransport("smtp");
            transport.connect();
            System.out.println("SMTP server connection successful");
        } catch (MessagingException e) {
            System.out.println("Failed to connect to SMTP server: " + e.getMessage());
        } finally {
            if (transport != null) {
                try {
                    transport.close();
                } catch (MessagingException e) {
                    // Handle exception
                }
            }
        }
        return false;
    }

    private String getSmptByDomain(String domain) {
        String s = null;
        try {
        Process p = Runtime.getRuntime().exec("nslookup -type=mx " + domain);
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            // read the output from the command
            while ((s = stdInput.readLine()) != null) {
                if(s.contains("mail exchanger = ")){
                   String smpt =  s.split("mail exchanger = ")[1];
                   return smpt;
                }
            }
            return null;
        } catch (IOException e) {
            System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
        }
        return null;
    }

}
