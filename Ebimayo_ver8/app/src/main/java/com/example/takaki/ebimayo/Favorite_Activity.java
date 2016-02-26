package com.example.takaki.ebimayo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * Created by Takaki on 2016/02/19.
 */
public class Favorite_Activity extends Activity {//お気に入り画面のアクティビティ
    Common global;
    ImageButton bt_calendar;
    ImageButton bt_search;
    ImageButton bt_favorite;
    ImageButton bt_setting;
    public final static String TAG_CALENDAR = "CALENDAR";
    public final static String TAG_SEARCH = "SEARCH";
    public final static String TAG_FAVORITE = "FAVORITE";
    public final static String TAG_RESPONSE = "RESPONSE";
    public final static String TAG_SETTING = "SETTING";
    GridView gridView;
    ArrayAdapter<String> adapter_favorite;
    String[] str_favorite = new String[100];//リストの初期値


    int str_id[]=new int[100];//idをリストの0～格納するための配列
    int num = 0;
    int id_favorite[]=new int[100];
    char char_favorite[];

    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.final_favorite_layout);
        //loadText();

        /////***********************データベース処理**************************/////
        /*
        id_favorite[]でidを送信,返り値はtitle
        str_favorite[num]にtitleを格納
        str_favarite
           */

    }

    public void onStart() {
        super.onStart();

        global = (Common) getApplication();

        for(int i=0;i<global.title_num;i++){
            str_favorite[i]="_";
        }
        bt_calendar = (ImageButton) findViewById(R.id.bt_calendar_favorite);
        bt_calendar.setTag(TAG_CALENDAR);
        bt_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Favorite_Activity.this, Top_Activity.class);
                startActivity(intent);
            }
        });
        bt_search = (ImageButton) findViewById(R.id.bt_search_favorite);
        bt_search.setTag(TAG_SEARCH);
        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Favorite_Activity.this, Search_Activity.class);
                startActivity(intent);
            }
        });
        bt_favorite = (ImageButton) findViewById(R.id.bt_favorite_favorite);
        bt_favorite.setTag(TAG_FAVORITE);
        bt_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Favorite_Activity.this, Favorite_Activity.class);
                startActivity(intent);
            }
        });

        bt_setting = (ImageButton) findViewById(R.id.bt_setting_favorite);
        bt_setting.setTag(TAG_SETTING);
        bt_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Favorite_Activity.this, Setting_Activity.class);
                startActivity(intent);
            }
        });
/*
        num=0;
        for (int i = 0; i < global.title_num; i++) {
            if (global.data[i].favorite == true) {
                str_favorite[num] = global.data[i].title;
                //str_id[num] = global.data[i].id;
                num++;
            }
        }*/
        /*******************/

         /**************/
        for(int i=0;i<global.title_num;i++){
            str_favorite[i]=global.data_fav[i].title;
        }

        gridView = (GridView) findViewById(R.id.gridView_title_favorite);
        adapter_favorite = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, str_favorite);
        gridView.setAdapter(adapter_favorite);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int set_id = global.id_favorite[position];
                global.server_id = set_id;
                Intent intent = new Intent(Favorite_Activity.this, Profile_Activity.class);
                startActivity(intent);
            }
        });
    }
    protected void loadText(){
        try{
            FileInputStream fis = openFileInput("favorite.txt");
            BufferedReader br =new BufferedReader(new InputStreamReader(fis));
            StringBuffer sb = new StringBuffer();
            String str;
            while((str = br.readLine()) != null){
                sb.append(str);
            }
            str = sb.toString();
            char_favorite =str.toCharArray();
            System.out.println(str);
            System.out.println(str.length());

            for(int i=0;i<str.length();i++) {
                System.out.println(char_favorite[i]);
                id_favorite[i] = (int)char_favorite[i];
                str_id[i]=id_favorite[i];
            }

        }catch(Exception e){
            System.out.println("xxxxxxxx");
        }
    }
}
