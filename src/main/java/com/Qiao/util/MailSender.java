package com.Qiao.util;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.velocity.VelocityEngineUtils;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.util.Map;
import java.util.Properties;

/**
 * Created by white and black on 2016/8/28.
 */
public class MailSender implements InitializingBean {
    private static final Logger logger=Logger.getLogger(MailSender.class);
    private JavaMailSenderImpl mailSender;
    @Autowired
    private VelocityEngine velocityEngine;

    public boolean sendWithHTMLTemplate(String to,String subject,String template,Map<String,Object> model){
        try{
            String nick= MimeUtility.encodeText("世界真奇怪");
            InternetAddress from=new InternetAddress(nick+"<qiao142857@163.com");
            MimeMessage mimeMessage=mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(mimeMessage);
            String result= VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,template,"UTF-8",model);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(result,true);
            mailSender.send(mimeMessage);
            return true;
        }catch (Exception e){
            logger.error("发送邮件失败"+e.getMessage());
            return false;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        mailSender=new JavaMailSenderImpl();
        mailSender.setUsername("qiao142857@163.com");
        mailSender.setPassword("qiao285714");
        mailSender.setHost("smtp.qq.com");
        mailSender.setPort(465);
        mailSender.setProtocol("smtps");
        mailSender.setDefaultEncoding("utf8");
        Properties javaMailProperties=new Properties();
        javaMailProperties.put("mail.smtp.ssl.enable",true);
        mailSender.setJavaMailProperties(javaMailProperties);

    }
}
