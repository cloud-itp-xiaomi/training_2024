package com.example.demo.controller;

import com.example.demo.service.MyMailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class EmailController {
    /*
    // 日志
    private final Logger log = LoggerFactory.getLogger( this.getClass() );
    @Autowired
    public MyMailService myMailService; //使用自定义的邮件服务
// 测试：简单邮件发送、附件邮件发送、图片邮件发送、模板邮件发送(见后)

    @RequestMapping("/sendSimpleEmail")
    public void sendSimpleEmail() {
        myMailService.sendSimpleMail("2206586835@qq.com", "测试", "来自zz的问候");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置日期格式
        log.info("邮件已经发送，发送时间:" + df.format(new Date()));
    }

    @RequestMapping("/sendAttachmentMail")
    public void sendAttachmentMail() throws MessagingException {
        String filePath="C:\\Users\\22065\\Desktop\\新建 文本文档.txt"; //准备好要发送的文件
        String content="来自<span style='color:red;font-size:30px;'>zz</span>的问候"; //邮件内容带HTML
        myMailService.sendAttachmentsMail( "2206586835@qq.com", "测试", content, filePath );
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        log.info("邮件已经发送，发送时间:" + df.format(new Date()));
    }

    @RequestMapping("/sendImageMail")
    public void sendImageMail() throws MessagingException {
        String imgPath="C:\\Users\\22065\\Desktop\\cat.jpg"; //准备好要发送的图片文件
        String imgId="p01"; //设置图片id
// 注意：cid 放在<img> src属性中，p01为imgId值
        String content="来自zz的问候,送你一张贺卡<img src='cid:p01' style='width:300px;height:200px'>"; //邮件内容带HTML
        myMailService.sendImageMail( "2206586835@qq.com", "测试",content, imgPath, imgId );
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置日期格式
        log.info("邮件已经发送，发送时间:" + df.format(new Date()));
    }

    @RequestMapping("/sendTemplateMail")
    public void sendTemplateMail() throws MessagingException {
        myMailService.sendTemplateMail("2206586835@qq.com","主题:这是激活邮件");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        log.info("邮件已经发送，发送时间:" + df.format(new Date()));
    }
    //用户验证
    @RequestMapping("/activate/{id}")
    public String activate( @PathVariable String id ){
        if( id.equals("123") ) return "激活成功";
        else return "激活失败";
    }
*/
}