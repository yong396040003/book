package book.yong.cn.book.pojo;

import java.io.Serializable;

/**
 * Description:
 * Date: 9:31 2019/10/26
 *
 * @author yong
 * @see
 */
public class EmailInfo implements Serializable {
    /**
     * 发件人名
     */
    private final String sendUsername = "权少";
    /**
     * 发件人邮箱
     */
    private final String sendEmail = "396040003@qq.com";
    /**
     * 接收人邮箱
     */
    private String receiveUserName;
    /**
     * 邮件标题
     */
    private String subject;
    /**

     * 发送内容content(html)
     */
    private String content;
    /**
     * 发送内容text
     */
    private String text;
    /**
     * 发送用户授权码
     */
    private final String authorization = "tkrbthpohcxobjhh";

    public String getSendUsername() {
        return sendUsername;
    }

    public String getSendEmail() {
        return sendEmail;
    }

    public String getReceiveUserName() {
        return receiveUserName;
    }

    public void setReceiveUserName(String receiveUserName) {
        this.receiveUserName = receiveUserName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthorization() {
        return authorization;
    }

    @Override
    public String toString() {
        return "EmailInfo{" +
                "sendUsername='" + sendUsername + '\'' +
                ", sendEmail='" + sendEmail + '\'' +
                ", receiveUserName='" + receiveUserName + '\'' +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                ", text='" + text + '\'' +
                ", authorization='" + authorization + '\'' +
                '}';
    }
}
