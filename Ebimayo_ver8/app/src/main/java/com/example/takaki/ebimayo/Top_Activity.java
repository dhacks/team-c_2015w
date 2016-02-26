package com.example.takaki.ebimayo;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.*;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Takaki on 2016/02/19.
 */
public class Top_Activity extends Activity implements View.OnClickListener {//Iop画面のアクティビティ
    private Common global;
    int grid_num = 0;
    ImageButton bt_calendar;
    ImageButton bt_search;
    ImageButton bt_favorite;
    ImageButton bt_setting;
    CalendarView cv;
    GridView gv;

    char char_favorite[];

    public final static String TAG_CALENDAR = "CALENDAR";
    public final static String TAG_SEARCH = "SEARCH";
    public final static String TAG_FAVORITE = "FAVORITE";
    public final static String TAG_RESPONSE = "RESPONSE";
    public final static String TAG_SETTING = "SETTING";
    public final static int RECORD = 1;
    
    boolean downloaded;
    int num;
    int favorite_max;
    ArrayAdapter<String> adapter_event;
    String[] str_event= new String[100];//リストの初期値
    int[] str_dialog_event = new int[100];
    int[] str_dialog_event_num = new int[20];
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.final_top_layout);
       //loadText();


    }

    public void onStart() {
        super.onStart();
        // グローバル変数を扱うクラスを取得する
        global = (Common) getApplication();
        global.init();
        Url_connection g = new Url_connection(global);
        global.id_favorite[0]=1001;
        global.id_favorite[1]=2001;
        global.id_favorite[2]=3001;
        global.id_favorite[3]=4001;
        global.id_favorite[4]=4003;
        favorite_max = 5;

        /////***********************データベース処理**************************/////
        /*
        * id_favorite[]にidが格納されているからそれ送信
        * 返り値は
        * global.data_fav[].id///////////////////////追加
        *global.data_fav[].title
        * global.data_fav[].field
        * global.data_fav[].school
        * 
        * 
        * global.data_fav[].event_title[]
        * global.data_fav[].event_month[]
        * global.data_fav[].event.day[]
        * global.data_fav[].event_time[]
        * global.data_fav[].event_detail[]
        *
        * */
        num = 0;
        for(int n = 0; n < favorite_max; n++){
          Url_connection.downLoadFavoriteCircle(n);
          Url_connection.downLoadFavoriteEvent(n);
        }
        for(int n = 0; n < favorite_max; n++){
            System.out.println(global.data_fav[n].title);
            System.out.println(global.data_fav[n].field);
            System.out.println(global.data_fav[n].school);
        }


        for(int i=0;i<global.title_num;i++){
            str_event[i]="_";
        }

        /*下のボタンの画面遷移ここから*/
        bt_calendar = (ImageButton) findViewById(R.id.bt_calendar_top);
        bt_calendar.setTag(TAG_CALENDAR);
        bt_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Top_Activity.this, Top_Activity.class);
                startActivity(intent);
            }
        });
        bt_search = (ImageButton) findViewById(R.id.bt_search_top);
        bt_search.setTag(TAG_SEARCH);
        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Top_Activity.this, Search_Activity.class);
                startActivity(intent);
            }
        });
        bt_favorite = (ImageButton) findViewById(R.id.bt_favorite_top);
        bt_favorite.setTag(TAG_FAVORITE);
        bt_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Top_Activity.this, Favorite_Activity.class);
                startActivity(intent);
            }
        });

        bt_setting = (ImageButton) findViewById(R.id.bt_setting_top);
        bt_setting.setTag(TAG_SETTING);
        bt_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Top_Activity.this, Setting_Activity.class);
                startActivity(intent);
            }
        });
        /*(仮)*/


        /*ここまで*/
        cv = (CalendarView) findViewById(R.id.calendarView_top);
        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                for(int i=0;i<global.title_num;i++) {
                    str_event[i] ="_";
                    str_dialog_event[i]=100;
                }grid_num=0;
                for(int i=0;i<global.event_num;i++) {
                    str_dialog_event_num[i]=100;
                }

            for(int i=0;i<global.title_num;i++){
                for(int j=0;j<global.event_num;j++) {
                    if(global.data_fav[i].event_month[j] == (month+1) && global.data_fav[i].event_day[j] == dayOfMonth){
                        str_event[grid_num]=global.data_fav[i].title+"  "+global.data_fav[i].event_title[j];
                        System.out.println(global.data_fav[i].event_title[j]);
                        str_dialog_event[grid_num]=i;
                        str_dialog_event_num[grid_num]=j;
                        grid_num++;
                    }
                }
            }
                adapter_event = new ArrayAdapter<String>(Top_Activity.this, android.R.layout.simple_list_item_1, str_event);
                gv.setAdapter(adapter_event);

            }
        });

        gv = (GridView)findViewById(R.id.gridView_event_top);
        adapter_event = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, str_event);
        gv.setAdapter(adapter_event);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //global.data_fav[str_dialog_event[position]]
                //ダイアログの設定
                LayoutInflater factory = LayoutInflater.from(Top_Activity.this);
                // week_dialog.xmlに, レイアウトを記述
                final View event_v = factory.inflate(R.layout.final_event_dialog_top, null);

                TextView tv_title = (TextView) event_v.findViewById(R.id.editText_eventname_event_dialog_top);
                tv_title.setText(global.data_fav[str_dialog_event[position]].event_title[str_dialog_event_num[position]]);

                TextView tv_time = (TextView) event_v.findViewById(R.id.editText_time_event_dialog_top);
                tv_time.setText(global.data_fav[str_dialog_event[position]].event_time[str_dialog_event_num[position]]);

                TextView tv_place = (TextView) event_v.findViewById(R.id.editText_place_event_dialog_top);
                tv_place.setText(global.data_fav[str_dialog_event[position]].event_place[str_dialog_event_num[position]]);

                TextView tv_detail = (TextView) event_v.findViewById(R.id.editText_detail_event_dialog_top);
                tv_detail.setText(global.data_fav[str_dialog_event[position]].event_detail[str_dialog_event_num[position]]);

                AlertDialog.Builder builder = new AlertDialog.Builder(Top_Activity.this);
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
    }

    public void onClick(View view) {
        String tag = (String) view.getTag();
        System.out.print("ttt");

    }

    protected void loadText() {
        try {
            FileInputStream fis = openFileInput("favorite.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            StringBuffer sb = new StringBuffer();
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            str = sb.toString();
            char_favorite = str.toCharArray();
            System.out.println(str);
            System.out.println(str.length());

            for (int i = 0; i < str.length(); i++) {
                System.out.println(char_favorite[i]);
                global.id_favorite[i] = (int) char_favorite[i];
            }

        } catch (Exception e) {
            System.out.println("xxxxxx");
        }
    }
    
}










