package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class MyMailService {
    @Autowired
    private JavaMailSender javaMailSender; //邮件发送
    @Autowired
    private TemplateEngine templateEngine; //邮件模板引擎(最后一个例子用)
    @Value("${mail.fromMail.addr}")
    private String from;
    private final Logger log = LoggerFactory.getLogger( this.getClass() ); //日志
// 四种邮件：简单邮件发送、附件邮件发送、图片邮件发送、模板邮件发送(见后)

    //简单邮件发送
    public void sendSimpleMail( String to, String subject, String content ) {
        SimpleMailMessage message = new SimpleMailMessage(); //创建简单邮件
        message.setFrom(from); // 发件人
        message.setTo(to); // 接收方
        message.setSubject(subject); // 邮件主题
        message.setText(content); // 邮件内容
        try {
            javaMailSender.send(message); //发送邮件
            log.info("简单邮件发送成功");
        } catch (MailException e) {
            log.error("简单邮件发送失败");
        }
    }

    //附件邮件发送
    public void sendAttachmentsMail(String to, String subject, String content, String filePath) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage(); //创建带附件的邮件
        MimeMessageHelper helper = new MimeMessageHelper(message, true); // true表示支持附件
        helper.setFrom(from); // 发件人
        helper.setTo(to); // 接收方
        helper.setSubject(subject); // 邮件主题
        helper.setText(content, true); // 邮件内容，true表示支持 html 格式内容
// 添加附件
        FileSystemResource fileSystemResource = new FileSystemResource( new File(filePath) ); // 获取附件文件
        String fileName = fileSystemResource.getFilename(); // 获取附件文件名
        helper.addAttachment(fileName, fileSystemResource); // 添加附件，参数1是附件名字，参数2是附件文件
        try {
            javaMailSender.send(message); // 发送邮件
            log.info("附件邮件发送成功");
        } catch (MailException e) {
            log.error("附件邮件发送失败");
        }
    }

    //图片邮件发送
    public void sendImageMail(String to, String subject, String content, String imgPath, String imgId) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage(); //创建带附件的邮件
        MimeMessageHelper helper = new MimeMessageHelper(message, true); // true表示支持附件
        helper.setFrom(from); // 发件人
        helper.setTo(to); // 接收方
        helper.setSubject(subject); // 邮件主题
        helper.setText(content, true); // 邮件内容，true表示支持 html 格式内容
// 添加图片
        FileSystemResource fileSystemResource = new FileSystemResource( new File(imgPath) ); // 获取图片文件
        helper.addInline(imgId, fileSystemResource); //添加图片，参数1是图片id，参数2是图片文件
        try {
            javaMailSender.send(message); // 发送邮件
            log.info("图片邮件发送成功");
        } catch (MailException e) {
            log.error("图片邮件发送失败");
        }
    }

    public void sendTemplateMail(String to, String subject) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
// 使用templateEngine加载邮件模板
        Context context = new Context(); // context视为一张空白邮件
        context.setVariable("id", "123"); // 设置模板id参数的值，假设为123
        String content = templateEngine.process("email_template", context); //将模板作为邮件内容
        helper.setFrom(from); //发送方
        helper.setTo(to); //接收方
        helper.setSubject(subject); //邮件主题
        helper.setText(content, true); //邮件内容，true表示支持 html 格式内容
        try {
            javaMailSender.send(message);
            log.info("模板邮件发送成功");
        } catch (MailException e) {
            log.error("模板邮件发送失败");
        }
    }

}