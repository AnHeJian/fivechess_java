package com.example.fivechess;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import static java.security.AccessController.getContext;

public class BeginActivity extends AppCompatActivity {

    private FiveChessPanel mfivechess;
    private int choice = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begin);

        mfivechess = (FiveChessPanel) findViewById(R.id.id_fivechess);
    }

    public void changeToRenji(View v)
    {
        Intent intent = new Intent(this,MainActivity.class);

        Bundle bundle = new Bundle();
        bundle.putBoolean("RenJi",true);
        bundle.putInt("IsSoundOn",choice);
        intent.putExtra("bundle",bundle);

        startActivity(intent);
    }
    public void changeToRenRen(View v)
    {
        Intent intent = new Intent(this,MainActivity.class);

        Bundle bundle = new Bundle();
        bundle.putBoolean("RenJi",false);
        bundle.putInt("IsSoundOn",choice);
        intent.putExtra("bundle",bundle);

        startActivity(intent);
    }
    public void setting(View v)
    {
        final String[] Items = {"开启声音", "关闭声音"};
        new AlertDialog.Builder(BeginActivity.this).setTitle("设置")
                .setSingleChoiceItems(Items, choice, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        choice = i;
                        Toast.makeText(BeginActivity.this,Items[i],Toast.LENGTH_SHORT).show();
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
        {
            if(System.currentTimeMillis()-exitTime > 2000)
            {
                Toast.makeText(BeginActivity.this,"再按一次退出游戏",Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            }else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
