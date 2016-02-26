package com.example.takaki.ebimayo;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;
import java.security.Provider;

/**
 * Created by Takaki on 2016/02/19.
 */
public class Setting_Activity extends Activity {

    Common global;
    Button bt_top;
    Button bt_provider;
    Button bt_guid;
    Button bt_info;
    TextView tv_id_dialog;
    TextView tv_password_dialog;

    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.final_setting_layout);
    }

    public void onStart() {
        super.onStart();
        global = (Common) getApplication();

        bt_top = (Button) findViewById(R.id.bt_top_setting);
        bt_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setting_Activity.this, Top_Activity.class);
                startActivity(intent);
            }
        });
        bt_provider = (Button) findViewById(R.id.bt_login_setting);
        bt_provider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ダイアログの設定
                LayoutInflater inflater_event = LayoutInflater.from(Setting_Activity.this);
                View login_v = inflater_event.inflate(R.layout.login_dialog, null);
                tv_id_dialog = (TextView) login_v.findViewById(R.id.textView_id_login);
                tv_password_dialog = (TextView) login_v.findViewById(R.id.textView_password_login);
                AlertDialog.Builder builder = new AlertDialog.Builder(Setting_Activity.this);
                builder.setTitle("選択してください") // タイトル設定
                        .setView(login_v) // レイアウト設定
                                // OK(肯定的な)ボタンの設定
                        .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String id = tv_id_dialog.getText().toString();
                                String pass = tv_password_dialog.getText().toString();
                                System.out.println(id);
                                System.out.println(pass);
                                boolean login = false;

                                String downLoadPassword = Url_connection.getPassword(id);
                                System.out.println("downLoadPassword: " + downLoadPassword + pass);
                                if(downLoadPassword != null && pass.equals(downLoadPassword)) {
                                    login = true;
                                }
                                /////***********************データベース処理**************************/////
                                //global.server_id
                                /*
                                * idとpassがデータべスに登録されているサークルのidとpassと一致しているか
                                * 一致してたらloginにtrueを返し
                                *
                                *
                                * */

                                if (login == true) {
                                    Intent intent = new Intent(Setting_Activity.this, Provider_Activity.class);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(getApplicationContext(),
                                            "ユーザID,またはパスワードが正しくありません．",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int id) {

                            }
                        });
                builder.create().show(); // 表示

            }
        });
        bt_guid = (Button) findViewById(R.id.bt_guide_setting);
        bt_guid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //global.data_fav[str_dialog_event[position]]
                //ダイアログの設定
                LayoutInflater factory = LayoutInflater.from(Setting_Activity.this);
                // week_dialog.xmlに, レイアウトを記述
                final View event_v = factory.inflate(R.layout.about_layout, null);



                AlertDialog.Builder builder = new AlertDialog.Builder(Setting_Activity.this);
                builder.setTitle("===詳細====") // タイトル設定
                        .setView(event_v) // レイアウト設定
                                // OK(肯定的な)ボタンの設定
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });

                builder.create().show(); // 表示
            }
        });
        bt_info =(Button)findViewById(R.id.bt_info_setting);
        bt_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setting_Activity.this, About_Activity.class);
                startActivity(intent);
            }
        });
            }

    }

