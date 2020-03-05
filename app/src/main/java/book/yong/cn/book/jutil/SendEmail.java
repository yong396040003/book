package book.yong.cn.book.jutil;

import android.util.Log;

import book.yong.cn.book.pojo.EmailInfo;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.activation.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Properties;

/**
 * Description:
 * 发送邮件
 * Date: 21:55 2019/10/25
 *
 * @author yong
 * @see
 */
public class SendEmail {

    public static void send(EmailInfo emailInfo) {
        //key value:配置参数。真正发送邮件时再配置
        Properties props = new Properties();

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream in = classLoader.getResourceAsStream("email.properties");
        try {
            props.load(in);
            in.close();
        } catch (IOException e) {
            System.err.println("email.properties加载异常");
            e.printStackTrace();
            return;
        }

        Session session = Session.getInstance(props);
        session.setDebug(true);
        MimeMessage message = new MimeMessage(session);

        //发件人邮箱
        try {
            message.setFrom(new InternetAddress(emailInfo.getSendEmail(), emailInfo.getSendUsername()));
            //收件人的邮箱
            message.setRecipients(Message.RecipientType.TO, emailInfo.getReceiveUserName());
            //邮件标题
            message.setSubject(emailInfo.getSubject());
            // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
            Multipart mainPart = new MimeMultipart();
            // 创建一个包含HTML内容的MimeBodyPart
            BodyPart html = new MimeBodyPart();
            // 设置HTML内容
            html.setContent(emailInfo.getContent(), "text/html; charset=utf-8");
            mainPart.addBodyPart(html);
            //设置正文(html)
            message.setContent(mainPart);
            //message.setText("<h1>hello</h1>");//纯文本

            message.saveChanges();

            //发送邮件
            Transport ts = session.getTransport();
            // 密码为授权码不是邮箱的登录密码
            ts.connect(emailInfo.getSendEmail(), emailInfo.getAuthorization());
            MailcapCommandMap mc = (MailcapCommandMap)CommandMap.getDefaultCommandMap();
            mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
            mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
            mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
            mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
            mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
            CommandMap.setDefaultCommandMap(mc);
            Thread.currentThread().setContextClassLoader(SendEmail.class.getClassLoader());
            //对象，用实例方法
            ts.sendMessage(message, message.getAllRecipients());
            ts.close();
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
