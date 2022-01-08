package com.example.todo.repository;

import com.example.todo.entity.Mail;

import javax.mail.MessagingException;

public interface SendMailService {
    void sendMail(Mail mail);

    void sendMailWithAttachments(Mail mail) throws MessagingException;
}