package book.yong.cn.book.activity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import book.yong.cn.book.R;
import book.yong.cn.book.jutil.Http;
import book.yong.cn.book.jutil.SendEmail;
import book.yong.cn.book.jutil.StaticConstant;
import book.yong.cn.book.jutil.VerCode;
import book.yong.cn.book.pojo.EmailInfo;
import book.yong.cn.book.pojo.User;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 注册用户
 *
 * @author yong
 * @time 2019/10/30 18:38
 */
@SuppressLint("Registered")
public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.imageView2)
    ImageView imageView2;
    @Bind(R.id.username)
    EditText username;
    @Bind(R.id.userEmail)
    EditText userEmail;
    @Bind(R.id.verCode)
    EditText verCode;
    @Bind(R.id.sendEmail)
    Button sendEmail;
    @Bind(R.id.userPassword)
    EditText userPassword;
    @Bind(R.id.confirmPassword)
    EditText confirmPassword;
    @Bind(R.id.register)
    Button register;
    @Bind(R.id.linearLayout)
    LinearLayout linearLayout;

    //邮箱
    private String email;
    //验证码
    private String code;
    //倒计时
    private int time = 30;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressLint({"SetTextI18n", "ResourceType"})
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                sendEmail.setText(time + "s");
                sendEmail.setEnabled(false);
                if (time == 0) {
                    sendEmail.setText("重新发送");
                    sendEmail.setEnabled(true);
                }
            }
            if (msg.what == 2 && msg.obj != null) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject((String) msg.obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (jsonObject.getInt("code") == 0) {
                        Toast.makeText(RegisterActivity.this, "对不起，该账户已被他人注册。", Toast.LENGTH_SHORT).show();
                        register.setEnabled(true);
                    } else {
                        register.setId(666);
                        Toast.makeText(RegisterActivity.this, "恭喜你注册成功，请返回上一页登陆吧。", Toast.LENGTH_SHORT).show();
                        register.setOnClickListener(RegisterActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if (msg.what == 2){
                Toast.makeText(RegisterActivity.this, "对不起，该账户已被他人注册。", Toast.LENGTH_SHORT).show();
                register.setEnabled(true);
            }
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void initDate() {

    }

    @Override
    public void initView() {
        //给获取验证码添加点击监听事件
        sendEmail.setOnClickListener(this);
        //给注册按钮添加点击监听事件
        register.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);

        initView();
    }

    @SuppressLint({"SetTextI18n"})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendEmail:
                email = userEmail.getText().toString();
                if (!"".equals(email)) {
                    if (isEmail(email)) {
                        sendEmail.setText(time + "s");
                        sendEmail.setEnabled(false);

                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                EmailInfo emailInfo = new EmailInfo();
                                emailInfo.setReceiveUserName(email);
                                emailInfo.setSubject("一本好书验证码");
                                code = VerCode.getInstance().generationCheckCode();
                                emailInfo.setContent("<h1>你好</h1>" +
                                        "<p>你的验证码：<span style = \"color=#12b7f5;font-size=14px;\">" + code +
                                        "</span></p><p>该验证码仅用于一本好书app用户注册。</p>" +
                                        "<p>该验证吗30分钟后失效，请尽快使用</p>");
                                SendEmail.send(emailInfo);
                            }
                        });

                        thread.start();

                        Thread countDown = new Thread() {
                            @Override
                            public void run() {
                                while (true) {
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                        break;
                                    }
                                    time--;
                                    //如果事件为0 结束进程
                                    if (time == -1) {
                                        time = 30;
                                        break;
                                    }
                                    Message message = new Message();
                                    message.what = 1;
                                    handler.sendMessage(message);
                                }
                            }
                        };
                        countDown.start();
                    } else {
                        Toast.makeText(this, "邮箱格式错误", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "请输入qq邮箱", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.register:
                final String userName = username.getText().toString();
                final String sendEmail = userEmail.getText().toString();
                String vercode = verCode.getText().toString();
                final String uPassword = userPassword.getText().toString();
                String cPassword = confirmPassword.getText().toString();
                //防止注册前更改邮箱
                if (!email.equals(sendEmail)) {
                    Toast.makeText(this, "注意：请不要更改邮箱", Toast.LENGTH_SHORT).show();
                    break;
                } else if (!uPassword.equals(cPassword)) {
                    Toast.makeText(this, "前后密码输入不相同", Toast.LENGTH_SHORT).show();
                } else if (!code.equals(vercode)) {
                    Toast.makeText(this, "验证码错误", Toast.LENGTH_SHORT).show();
                } else if ("".equals(userName) && "".equals(uPassword)) {
                    Toast.makeText(this, "用户名/密码为null", Toast.LENGTH_SHORT).show();
                } else {
                    register.setEnabled(false);
                    Thread sendEmailThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String msg = Http.sendPost(StaticConstant.URL_USER_REGISTER,
                                    "userName=" + userName +
                                            "&userEmail=" + sendEmail +
                                            "&userPassword=" + uPassword);
                            Message message = new Message();
                            message.what = 2;
                            message.obj = msg;
                            Log.e("success", msg + "");
                            handler.sendMessage(message);
                        }
                    });
                    sendEmailThread.start();
                }
                break;
            case 666:
                Toast.makeText(this, "亲，你已完成注册，请不要重复点击哟", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    /**
     * 判断是否是Email
     *
     * @return
     */
    public boolean isEmail(String email) {
        Pattern emailPattern = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}");
        Matcher matcher = emailPattern.matcher(email);
        return matcher.find();
    }
}
