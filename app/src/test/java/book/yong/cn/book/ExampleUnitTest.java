package book.yong.cn.book;

import org.junit.Test;

import book.yong.cn.book.jutil.SendEmail;
import book.yong.cn.book.jutil.VerCode;
import book.yong.cn.book.pojo.EmailInfo;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void send() {
        EmailInfo emailInfo = new EmailInfo();
        emailInfo.setReceiveUserName("1272514432@qq.com");
        emailInfo.setSubject("一本好书验证码");
        String verCode = VerCode.getInstance().generationCheckCode();
        emailInfo.setContent("<h1>你好</h1>" +
                "<p>你的验证码：<span style = \"color=#12b7f5;font-size=14px;\">" + verCode +
                "</span></p><p>该验证码仅用于一本好书app用户注册。</p>" +
                "<p>该验证吗30分钟后失效，请尽快使用</p>");
        SendEmail.send(emailInfo);
    }
}