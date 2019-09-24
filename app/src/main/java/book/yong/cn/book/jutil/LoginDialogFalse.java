package book.yong.cn.book.jutil;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import book.yong.cn.book.R;

public class LoginDialogFalse extends Dialog {
    private Context context;
    private Button button;
    public LoginDialogFalse(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_login_false);

        Button button = findViewById(R.id.dialog_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDialog();
            }
        });
    }

    public void closeDialog(){
        this.dismiss();
        this.setCancelable(false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }
}
