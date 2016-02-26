package com.example.takaki.ebimayo;

import android.app.Application;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Takaki on 2016/02/20.
 */
public class Common extends Application {//グローバルクラス，変数のやり取り用
    int search_result_num=0;
    int title_num = 100;
    int event_num=20;
    int fav_num=0;
    int event_current_num=0;
    int month;
    int dayOfMonth;
    int server_id;
    int id_favorite[] = new int[100];
    String server_title;
    int server_field;
    int server_school;
    String server_introduction;
    String server_place;

    String server_week;
    boolean server_week_mon;
    boolean server_week_tue;
    boolean server_week_wed;
    boolean server_week_thu;
    boolean server_week_fri;
    boolean server_week_sat;
    boolean server_week_sun;
    String server_time_st;
    String server_time_et;
    String server_mail;
    String server_member;
    boolean server_favorite;

    String SERVER_URL = "http://172.31.16.109:10000";

    //イベント
    String server_event_title[] = new String[event_num];
    int server_event_month[] = new int[event_num];
    int server_event_day[] = new int[event_num];
    String server_event_time[] = new String[event_num];
    String server_event_place[] = new String[event_num];
    String server_event_detail[] = new String[event_num];


    Data data[] = new Data[100];//データセット保存用
    Data data_fav[] = new Data[100];
    Data page;
    public void init() {
        load();
    }

    public void load() {
        for (int i = 0; i < title_num; i++) {
            id_favorite[i]=0;
            data[i] = new Data(i);
            data_fav[i] = new Data(i);
        }
        page = new Data(100);
    }

    public class Data {
        String title;
        String introduction;
        int id;
        int field;
        int school;
        String place;
        String time_st;
        String time_et;
        String member;
        String mail;
        boolean week_mon;
        boolean week_tue;
        boolean week_wed;
        boolean week_thu;
        boolean week_fri;
        boolean week_sat;
        boolean week_sun;
        String event_title[] = new String[event_num];
        int event_id[]= new int[event_num];
        int event_month[] = new int[event_num];
        int event_day[] = new int[event_num];
        String event_time[] = new String[event_num];
        String event_place[] = new String[event_num];
        String event_detail[] = new String[event_num];
        String week;//曜日ごとじゃなくて一つの文字列で管理

        boolean favorite;

        Data(int local_id) {
            title="";
            introduction="";
            id=0;
            field=0;
            school=0;
            place="";
            time_st="";
            time_et="";
            member="";
            week_mon=false;
           week_tue=false;
            week_wed=false;
            week_thu=false;
            week_fri=false;
            week_sat=false;
            week_sun=false;
            week="";
            for(int i=0;i<event_num;i++){
                event_title[i]="";
                event_month[i]=0;
                event_day[i]=0;
                event_time[i]="";
                event_place[i]="";
                event_detail[i]="";
            }
        }
        public String Conversion_f(int f){
            if(f==1){  return "サッカー";}
            else if(f==2){return "バドミントン"; }
            else if(f==3){return "野球"; }
            else if(f==4){return "映画"; }
            else if(f==5){return "---"; }
            else if(f==6){return "---"; }
            else if(f==7){return "---"; }
            else{return "---"; }
        };
        public String Conversion_s(int s){
            if(s==0){return "すべて";}
            if(s==1){  return "同志社今出川";}
            else if(s==2){return "同志社京田辺"; }
            else if(s==3){return "同女今出川"; }
            else if(s==4){return "同女京田辺"; }
            else if(s==5){return "---"; }
            else if(s==6){return "---"; }
            else if(s==7){return "---"; }
            else{return "---"; }
        }
    }



}
