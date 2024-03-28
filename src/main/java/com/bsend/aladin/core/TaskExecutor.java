package com.bsend.bsend.core;
import org.xbill.DNS.*;

import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;

import java.util.Hashtable;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TaskExecutor {
    private String [] lines;
   private ExecutorService executor;
    public TaskExecutor(String[] lines, int count) {
        this.lines = lines;
        this.executor = Executors.newFixedThreadPool(count);
    }

    public void run(){
        for (String line : lines) {
            Future<Boolean> future = executor.submit(() -> {
                String[] credentials = line.split(":");
                String email = credentials[0];
                String password = credentials[1];
                String smtp = "smtp."+email.split("@")[1];
                // Логика для попытки авторизации
                checkCredentials(smtp, email, password);
                return true;
            });
        }
        executor.shutdown();
    }
    private boolean checkCredentials(String smtp,String email,String password){
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", smtp);
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.sasl.enable", "true");
        props.put("mail.smtp.sasl.mechanisms", "XOAUTH2");
        props.put("mail.smtp.auth.login.disable", "true");
        props.put("mail.smtp.auth.plain.disable", "true");
        props.put("mail.smtp.user", "admin");
        Session session = Session.getDefaultInstance(props);
        try {
            // Попытка установить соединение с SMTP сервером
            Transport transport = session.getTransport("smtp");
            transport.connect(smtp, 465, email, password);
            System.out.println("Соединение с SMTP сервером успешно установлено!: "+smtp);
            // Закрытие соединения
            transport.close();
            return true;
        } catch (MessagingException e) {
            System.out.println("Не удалось установить соединение с SMTP сервером: " + e.getMessage());
            return false;
        }
    }

    private getSmptByDomain(String domain){
        try {
            // Замените "example.com" на домен, для которого вы хотите получить MX записи
            String domain = "example.com";
            Record[] records = new Lookup(domain, Type.MX).run();
            if (records != null) {
                for (Record record : records) {
                    MXRecord mx = (MXRecord) record;
                    System.out.println("Priority: " + mx.getPriority() + ", Host: " + mx.getTarget());
                }
            } else {
                System.out.println("No MX records found for domain " + domain);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
