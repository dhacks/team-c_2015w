package com.example.takaki.ebimayo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * Created by Takaki on 2016/02/19.
 */
public class Profile_Activity extends Activity {
    Common global;

    ImageButton bt_calendar;
    ImageButton bt_search;
    ImageButton bt_favorite;
    ImageButton bt_setting;
    ImageButton bt_mail;
    ToggleButton bt_favorite_title;
GridView gv_event;
    String str_introduction;
    String[] str_event= new String[20];//リストの初期値

    TextView title;
    TextView introduction;
    public final static String TAG_CALENDAR = "CALENDAR";
    public final static String TAG_SEARCH = "SEARCH";
    public final static String TAG_FAVORITE = "FAVORITE";
    public final static String TAG_SETTING = "SETTING";
    public final static String TAG_FAVORITE_TITLE = "FAVORITE_TITLE";

    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.final_profile_layout);
    }

    public void onStart() {
        super.onStart();
        // グローバル変数を扱うクラスを取得する
        global = (Common) getApplication();

        global.event_num = 0;
        Url_connection.downLoadProfilePage(global.server_id);
        Url_connection.downLoadProviderEvents(global.server_id);
        System.out.println("aaa" + global.event_num);
        ///////////////////////////////////////////////////////////////////////////////////////////////////////
        //global.server_idをおくる
        /************(仮)**************/

        int event_num=0;/////////////////////////////////////////////////////////////////

        /*****************************/

        for(int i=0;i<20;i++){
            str_event[i]="_";
        }


        bt_calendar = (ImageButton) findViewById(R.id.bt_calendar_profile);
        bt_calendar.setTag(TAG_CALENDAR);
        bt_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile_Activity.this, Top_Activity.class);
                startActivity(intent);
            }
        });
        bt_search = (ImageButton) findViewById(R.id.bt_search_profile);
        bt_search.setTag(TAG_SEARCH);
        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile_Activity.this, Search_Activity.class);
                startActivity(intent);
            }
        });

        bt_favorite = (ImageButton) findViewById(R.id.bt_favorite_profile);
        bt_favorite.setTag(TAG_FAVORITE);
        bt_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile_Activity.this, Favorite_Activity.class);
                startActivity(intent);
            }
        });

        bt_setting = (ImageButton) findViewById(R.id.bt_setting_profile);
        bt_setting.setTag(TAG_SETTING);
        bt_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile_Activity.this, Setting_Activity.class);
                startActivity(intent);
            }
        });

        title = (TextView) findViewById(R.id.textView_title_profile);
        title.setText(global.page.title);



        introduction = (TextView) findViewById(R.id.textView_introduction_profile);
        str_introduction =
                "カテゴリー:"+global.page.Conversion_f(global.page.field)+"\n"+
                        "校地:"+global.page.Conversion_s(global.page.school)+"\n"+
                        "活動期間:"+global.page.week+"\n"+
                        "活動時間:"+global.page.time_st+ " ～ "+global.page.time_et+"\n"+
                        "人数:"+global.page.member;
        introduction.setText(str_introduction);

        for(int i=0;i<global.event_num;i++){
            str_event[i] = global.page.event_month[i]+"月"+global.page.event_day[i]+"日 "+global.page.event_title[i];
        }

        gv_event = (GridView)findViewById(R.id.gridView_event_profile);
        ArrayAdapter<String> adapter_event;
        adapter_event = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, str_event);
        gv_event.setAdapter(adapter_event);
        gv_event.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //global.data_fav[str_dialog_event[position]]
                //ダイアログの設定
                LayoutInflater factory = LayoutInflater.from(Profile_Activity.this);
                // week_dialog.xmlに, レイアウトを記述
                final View event_v = factory.inflate(R.layout.final_event_dialog_top, null);

                TextView tv_title = (TextView) event_v.findViewById(R.id.editText_eventname_event_dialog_top);
                tv_title.setText(global.page.event_title[position]);

                TextView tv_time = (TextView) event_v.findViewById(R.id.editText_time_event_dialog_top);
                tv_time.setText(global.page.event_time[position]);

                TextView tv_place = (TextView) event_v.findViewById(R.id.editText_place_event_dialog_top);
                tv_place.setText(global.page.event_place[position]);

                TextView tv_detail = (TextView) event_v.findViewById(R.id.editText_detail_event_dialog_top);
                tv_detail.setText(global.page.event_detail[position]);

                AlertDialog.Builder builder = new AlertDialog.Builder(Profile_Activity.this);
                builder.setTitle("===イベント詳細====") // タイトル設定
                        .setView(event_v) // レイアウト設定
                                // OK(肯定的な)ボタンの設定
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });

                builder.create().show(); // 表示
            }
        });


        bt_favorite_title = (ToggleButton) findViewById(R.id.bt_favoritetitle_profile);
        bt_favorite_title.setTag(TAG_FAVORITE_TITLE);
        bt_favorite_title.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    global.page.favorite = true;
                    //bt_favorite_title.setForeground(android.R.drawable.btn_rating_star_off_pressed);
                    saveText();
                } else {
                    global.page.favorite = false;
                }
            }
        });
        bt_mail = (ImageButton) findViewById(R.id.bt_mail_profile);
        bt_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Uri uri = Uri.parse("makuhari009@gmail.com");
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.putExtra(Intent.EXTRA_SUBJECT, "メールの件名");
                intent.putExtra(Intent.EXTRA_TEXT, "メールの本文");
                startActivity(intent);
            */
                AlertDialog.Builder ad=new AlertDialog.Builder(Profile_Activity.this);
                   ad.setMessage("メールで問い合わせますか？");
                    ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {


                                    try {
                                        Intent it = new Intent();
                                        it.setAction(Intent.ACTION_SEND);
                                        it.setData(Uri.parse("mailto:" + "makuhari009@com.jp"));
                                        it.putExtra(Intent.EXTRA_SUBJECT, "aaa");
                                        it.putExtra(Intent.EXTRA_TEXT, "メールの本文");
                                        startActivity(it);
                                    } catch (Exception e) {
                                        Toast.makeText(getApplicationContext(),
                                                "メールは利用できません．",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            });


                ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                         public void onClick(DialogInterface dialog,int whichButton) {
                               }
                        });
                    ad.create();
                    ad.show();

            }
        });
    }
    protected void saveText(){
        try {
            System.out.println("A");
            FileOutputStream fos = openFileOutput("favorite.txt", Context.MODE_APPEND);
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(fos,"UTF-8"));
                //writer.print(str_id[i] + " ");
                writer.println(global.server_id);
            writer.close();
            fos.close();
        }catch(Exception e){System.out.println("B");}
    }
}
