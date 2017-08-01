package com.example.fivechess;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity{

    private FiveChessPanel mfivechess;
    private TextView textinfo ;
    private boolean isRenji;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       mfivechess = (FiveChessPanel) findViewById(R.id.id_fivechess);

        final Button button_restart = (Button) findViewById(R.id.id_button_restart);
        button_restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mfivechess.reStart();
            }
        });

        final Button button_regret = (Button) findViewById(R.id.id_button_regret);
        button_regret.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
               mfivechess.regret();
            }
        });

        textinfo = (TextView) findViewById(R.id.id_textView);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        isRenji = bundle.getBoolean("RenJi");
        mfivechess.setSoundSetting(bundle.getInt("IsSoundOn"));
        if(isRenji)
        {
            String renji = "人机对战";
            textinfo.setText(renji);
        }else{
            String renji = "人人对战";
            textinfo.setText(renji);
        }
        mfivechess.setisRenji(isRenji);
    }

    @Override
    public void onBackPressed()
    {
        if(!mfivechess.getmIsGameOver())
        {
            new AlertDialog.Builder(MainActivity.this).setTitle("游戏还未结束，是否退出？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            MainActivity.this.finish();
                        }
                    }).setNegativeButton("取消",null).show();
        }
        else
            MainActivity.this.finish();

    }
}
