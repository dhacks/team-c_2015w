package com.example.takaki.ebimayo;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Takaki on 2016/02/22.
 */
public class Provider_Activity extends Activity {
    Common global;
    int EVENT_NUM = 20;
    int event_num = 0;
    InputMethodManager imm;
    Button bt_title;
    Button bt_place;
    Button bt_week;
    Button bt_member;
    Button bt_mail;
    Button bt_event[]= new Button[EVENT_NUM];

    CheckBox cb_mon;
    CheckBox cb_tue;
    CheckBox cb_wed;
    CheckBox cb_thu;
    CheckBox cb_fri;
    CheckBox cb_sat;
    CheckBox cb_sun;
    TextView tv_title;
    TextView tv_place;
    TextView tv_week;
    TextView tv_field;
    TextView tv_member;
    TextView tv_mail;

    TextView tv_title_dialog;
    TextView tv_event_dialog;
    TextView tv_place_dialog;
    TextView tv_detail_dialog;
    TextView tv_event[] = new TextView[EVENT_NUM];
    //TextView tv_event_comment[] = new TextView[EVENT_NUM];


    Spinner sp_event_time[] = new Spinner[EVENT_NUM];
    Spinner sp_st;
    Spinner sp_et;
    Spinner sp_school;
    String str_field[] = {"サッカー", "テニス", "野球", "文化系"};
    String str_school[] = {"すべて","同今出川", "同京田辺", "同女今出川", "同女京田辺", "同今出川,同京田辺", "同女今出川,同女京田辺"};
    String str_month[] = new String[12];
    String str_day[] = new String[31];
    String str_hour[] =new String[24*2];
    String str_hour2[] =new String[24*4];

    Button bt_register;

    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.final_provider_layout);
    }

    public void onStart() {
        super.onStart();
        global = (Common) getApplication();

        System.out.println(global.server_id);

        global.event_num = 0;

        Url_connection.downLoadProviderPage(global.server_id);
        Url_connection.downLoadProviderEvents(global.server_id);
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////




        for (int i = 0; i < 12; i++) {
            str_month[i] = "" + (i + 1);
        }
        for (int i = 0; i < 31; i++) {
            str_day[i] = "" + (i + 1);
        }
        int H=0;
        for (int i=0;i<=23*2;){
            str_hour[i++] = H+":"+"00";
            str_hour[i++] = H+":"+"30";
            H++;
        }
         H=0;
        for (int i=0;i<=23*4;){
                str_hour2[i++] = H+":"+"00";
            str_hour2[i++] = H+":"+"15";
            str_hour2[i++] = H+":"+"30";
            str_hour2[i++] = H+":"+"45";
            H++;
        }

        /////*********固定フィールド************//////
        tv_title = (TextView) findViewById(R.id.textView_title_provider);
        tv_title.setText(global.page.title);

        tv_field =(TextView)findViewById(R.id.textView_categoryField_provider);
        tv_field.setText(global.page.Conversion_f(global.page.field));

        tv_member = (TextView) findViewById(R.id.textView_member_provider);
        tv_member.setText(global.page.member);

        tv_mail = (TextView) findViewById(R.id.textView_mail_provider);
        tv_mail.setText(global.page.mail);

        ////***********************************/////
        tv_place = (TextView) findViewById(R.id.textView_place_provider);
        tv_place.setText(global.page.place);

        tv_week =(TextView) findViewById(R.id.textView_week_provider);
        tv_week.setText(global.page.week);

        ////**********************************/////



        sp_school = (Spinner) findViewById(R.id.spinner_school_provider);
        ArrayAdapter<String> adapter_school = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, str_school);
        // adapter_et.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        sp_school.setAdapter(adapter_school);
        sp_school.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                global.server_school = (position);
                System.out.println("school:"+global.server_school);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        tv_place = (TextView) findViewById(R.id.textView_place_provider);
        tv_place.setEnabled(false);
        tv_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_place.setText("");
            }
        });

        tv_place.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus == false) {
                    //キーボードを閉じる
                    imm.hideSoftInputFromWindow(tv_place.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    tv_place.setEnabled(false);
                    TextView tv = (TextView) v;
                    global.server_place = tv.getText().toString();
                    System.out.println("place:"+global.server_place);
                }
            }
        });
        tv_place.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //キーボードを閉じる
                imm.hideSoftInputFromWindow(tv_place.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                tv_place.setEnabled(false);
                TextView tv = (TextView) v;
                global.server_place = tv.getText().toString();
                System.out.println("place:"+global.server_place);
                return false;
            }
        });

        //EditTextにリスナーをセット
        tv_place.setOnKeyListener(new View.OnKeyListener() {
            //コールバックとしてonKey()メソッドを定義
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //イベントを取得するタイミングには、ボタンが押されてなおかつエンターキーだったときを指定
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    //キーボードを閉じる
                    imm.hideSoftInputFromWindow(tv_place.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    tv_place.setEnabled(false);
                    return true;
                }
                return false;
            }
        });
        //tv_week = (TextView) findViewById(R.id.textView_week_provider);
        tv_week.setEnabled(false);
        bt_week = (Button) findViewById(R.id.bt_week_provider);
        bt_week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ダイアログの設定
                LayoutInflater factory = LayoutInflater.from(Provider_Activity.this);
                // week_dialog.xmlに, レイアウトを記述
                final View week_v = factory.inflate(R.layout.week_dialog, null);
                cb_mon = (CheckBox) week_v.findViewById(R.id.cb_monday);
                cb_tue = (CheckBox) week_v.findViewById(R.id.cb_tuesday);
                cb_wed = (CheckBox) week_v.findViewById(R.id.cb_wednesday);
                cb_thu = (CheckBox) week_v.findViewById(R.id.cb_thursday);
                cb_fri = (CheckBox) week_v.findViewById(R.id.cb_friday);
                cb_sat = (CheckBox) week_v.findViewById(R.id.cb_saturday);
                cb_sun = (CheckBox) week_v.findViewById(R.id.cb_sunday);
                AlertDialog.Builder builder = new AlertDialog.Builder(Provider_Activity.this);
                builder.setTitle("選択してください") // タイトル設定
                        .setView(week_v) // レイアウト設定
                                // OK(肯定的な)ボタンの設定
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String str_week = "";

                                global.server_week_mon = cb_mon.isChecked();
                                global.server_week_tue = cb_tue.isChecked();
                                global.server_week_wed = cb_wed.isChecked();
                                global.server_week_thu = cb_thu.isChecked();
                                global.server_week_fri = cb_fri.isChecked();
                                global.server_week_sat = cb_sat.isChecked();
                                global.server_week_sun = cb_sun.isChecked();

                                if (global.server_week_mon == true) {
                                    str_week = "月";
                                }
                                if (global.server_week_tue == true) {
                                    str_week = str_week + "火";
                                }
                                if (global.server_week_wed == true) {
                                    str_week = str_week + "水";
                                }
                                if (global.server_week_thu == true) {
                                    str_week = str_week + "木";
                                }
                                if (global.server_week_fri == true) {
                                    str_week = str_week + "金";
                                }
                                if (global.server_week_sat == true) {
                                    str_week = str_week + "土";
                                }
                                if (global.server_week_sun == true) {
                                    str_week = str_week + "日";
                                }
                                global.server_week = str_week;
                                tv_week.setText(str_week);
                                System.out.println("week:" + global.server_week);
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

        sp_st = (Spinner) findViewById(R.id.spinner_startTime_provider);
        ArrayAdapter<String> adapter_st = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, str_hour);
        adapter_st.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        sp_st.setAdapter(adapter_st);
        sp_st.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                global.server_time_st = (String) parent.getItemAtPosition(position);
                System.out.println("time_st:"+global.server_time_st);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        sp_et = (Spinner) findViewById(R.id.spinner_endTime_provider);
        ArrayAdapter<String> adapter_et = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, str_hour);
        // adapter_et.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        sp_et.setAdapter(adapter_et);
        sp_et.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                global.server_time_et = (String) parent.getItemAtPosition(position);
                System.out.println("time_et:"+global.server_time_et);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        bt_place = (Button) findViewById(R.id.bt_place_provider);
        bt_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //imm.hideSoftInputFromWindow(tv_mail.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                tv_place.setEnabled(true);
            }
        });


        bt_event[0] = (Button) findViewById(R.id.bt_event1_provider);
        bt_event[1] = (Button) findViewById(R.id.bt_event2_provider);
        bt_event[2] = (Button) findViewById(R.id.bt_event3_provider);
        bt_event[3] = (Button) findViewById(R.id.bt_event4_provider);
        bt_event[4] = (Button) findViewById(R.id.bt_event5_provider);
        bt_event[5] = (Button) findViewById(R.id.bt_event6_provider);
        bt_event[6] = (Button) findViewById(R.id.bt_event7_provider);
        bt_event[7] = (Button) findViewById(R.id.bt_event8_provider);
        bt_event[8] = (Button) findViewById(R.id.bt_event9_provider);
        bt_event[9] = (Button) findViewById(R.id.bt_event10_provider);
        bt_event[10] = (Button) findViewById(R.id.bt_event11_provider);
        bt_event[11] = (Button) findViewById(R.id.bt_event12_provider);
        bt_event[12] = (Button) findViewById(R.id.bt_event13_provider);
        bt_event[13] = (Button) findViewById(R.id.bt_event14_provider);
        bt_event[14] = (Button) findViewById(R.id.bt_event15_provider);
        bt_event[15] = (Button) findViewById(R.id.bt_event16_provider);
        bt_event[16] = (Button) findViewById(R.id.bt_event17_provider);
        bt_event[17] = (Button) findViewById(R.id.bt_event18_provider);
        bt_event[18] = (Button) findViewById(R.id.bt_event19_provider);
        bt_event[19] = (Button) findViewById(R.id.bt_event20_provider);

        tv_event[0] = (TextView) findViewById(R.id.textView_event1_provider);
        tv_event[1] = (TextView) findViewById(R.id.textView_event2_provider);
        tv_event[2] = (TextView) findViewById(R.id.textView_event3_provider);
        tv_event[3] = (TextView) findViewById(R.id.textView_event4_provider);
        tv_event[4] = (TextView) findViewById(R.id.textView_event5_provider);
        tv_event[5] = (TextView) findViewById(R.id.textView_event6_provider);
        tv_event[6] = (TextView) findViewById(R.id.textView_event7_provider);
        tv_event[7] = (TextView) findViewById(R.id.textView_event8_provider);
        tv_event[8] = (TextView) findViewById(R.id.textView_event9_provider);
        tv_event[9] = (TextView) findViewById(R.id.textView_event10_provider);
        tv_event[10] = (TextView) findViewById(R.id.textView_event11_provider);
        tv_event[11] = (TextView) findViewById(R.id.textView_event12_provider);
        tv_event[12] = (TextView) findViewById(R.id.textView_event13_provider);
        tv_event[13] = (TextView) findViewById(R.id.textView_event14_provider);
        tv_event[14] = (TextView) findViewById(R.id.textView_event15_provider);
        tv_event[15] = (TextView) findViewById(R.id.textView_event16_provider);
        tv_event[16] = (TextView) findViewById(R.id.textView_event17_provider);
        tv_event[17] = (TextView) findViewById(R.id.textView_event18_provider);
        tv_event[18] = (TextView) findViewById(R.id.textView_event19_provider);
        tv_event[19] = (TextView) findViewById(R.id.textView_event20_provider);

for(int i=0;i<global.event_num;i++) {
    if(global.page.event_title[i]!="") {
        tv_event[i].setText(global.page.event_month[i] + "月 " + global.page.event_day[i] + "日 " +
                global.page.event_title[i]);
    }
}


            bt_event[0].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("Q");
                    event_num=0;
                    //ダイアログの設定
                    LayoutInflater inflater_event = LayoutInflater.from(Provider_Activity.this);
                    View event_v = inflater_event.inflate(R.layout.final_event_dialog, null);

                    tv_title_dialog = (TextView) event_v.findViewById(R.id.editText_eventname_event);
                    tv_title_dialog.setText(global.page.event_title[event_num]);
                    tv_place_dialog = (TextView) event_v.findViewById(R.id.editText_place_event);
                   tv_place_dialog.setText(global.page.event_place[event_num]);
                    tv_detail_dialog = (TextView) event_v.findViewById(R.id.editText_detail_event);
                    tv_detail_dialog.setText(global.page.event_detail[event_num]);
                    final Spinner sp_month_dialog = (Spinner) event_v.findViewById(R.id.spinner_month_event);
                    ArrayAdapter<String> adapter_month = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_month);
                    sp_month_dialog.setAdapter(adapter_month);
                    final Spinner sp_day_dialog = (Spinner) event_v.findViewById(R.id.spinner_day_event);
                    ArrayAdapter<String> adapter_day = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_day);
                    sp_day_dialog.setAdapter(adapter_day);
                    final Spinner sp_time_dialog = (Spinner) event_v.findViewById(R.id.spinner_time_event);
                    ArrayAdapter<String> adapter_time = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_hour2);
                    sp_time_dialog.setAdapter(adapter_time);
                    AlertDialog.Builder builder = new AlertDialog.Builder(Provider_Activity.this);
                    builder.setTitle("選択してください") // タイトル設定
                            .setView(event_v) // レイアウト設定
                                    // OK(肯定的な)ボタンの設定
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    global.page.event_title[event_num] = tv_title_dialog.getText().toString();
                                    global.page.event_place[event_num] = tv_place_dialog.getText().toString();
                                    global.page.event_detail[event_num] = tv_detail_dialog.getText().toString();
                                    global.page.event_month[event_num] = Integer.parseInt(sp_month_dialog.getItemAtPosition(sp_month_dialog.getSelectedItemPosition()).toString());
                                    global.page.event_day[event_num] = Integer.parseInt(sp_day_dialog.getItemAtPosition(sp_day_dialog.getSelectedItemPosition()).toString());
                                    global.page.event_time[event_num] = sp_time_dialog.getItemAtPosition(sp_time_dialog.getSelectedItemPosition()).toString();
                                    tv_event[event_num].setText(global.page.event_month[event_num] + "月 " + global.page.event_day[event_num] + "日 " +
                                            global.page.event_title[event_num]);
                                }
                            })
                            .setNegativeButton("削除", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface d, int id) {
                                    tv_event[event_num].setText("New ");
                                    tv_title_dialog.setText("");
                                    tv_place_dialog.setText("");
                                    tv_detail_dialog.setText("");
                                }
                            });
                    builder.create().show(); // 表示
                }
            });


        bt_event[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Q");
                event_num = 1;
                //ダイアログの設定
                LayoutInflater inflater_event = LayoutInflater.from(Provider_Activity.this);
                View event_v = inflater_event.inflate(R.layout.final_event_dialog, null);

                tv_title_dialog = (TextView) event_v.findViewById(R.id.editText_eventname_event);
                tv_title_dialog.setText(global.page.event_title[event_num]);
                tv_place_dialog = (TextView) event_v.findViewById(R.id.editText_place_event);
                tv_place_dialog.setText(global.page.event_place[event_num]);
                tv_detail_dialog = (TextView) event_v.findViewById(R.id.editText_detail_event);
                tv_detail_dialog.setText(global.page.event_detail[event_num]);
                final Spinner sp_month_dialog = (Spinner) event_v.findViewById(R.id.spinner_month_event);
                ArrayAdapter<String> adapter_month = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_month);
                sp_month_dialog.setAdapter(adapter_month);
                final Spinner sp_day_dialog = (Spinner) event_v.findViewById(R.id.spinner_day_event);
                ArrayAdapter<String> adapter_day = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_day);
                sp_day_dialog.setAdapter(adapter_day);
                final Spinner sp_time_dialog = (Spinner) event_v.findViewById(R.id.spinner_time_event);
                ArrayAdapter<String> adapter_time = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_hour2);
                sp_time_dialog.setAdapter(adapter_time);
                AlertDialog.Builder builder = new AlertDialog.Builder(Provider_Activity.this);
                builder.setTitle("選択してください") // タイトル設定
                        .setView(event_v) // レイアウト設定
                                // OK(肯定的な)ボタンの設定
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                global.page.event_title[event_num] = tv_title_dialog.getText().toString();
                                global.page.event_place[event_num] = tv_place_dialog.getText().toString();
                                global.page.event_detail[event_num] = tv_detail_dialog.getText().toString();
                                global.page.event_month[event_num] = Integer.parseInt(sp_month_dialog.getItemAtPosition(sp_month_dialog.getSelectedItemPosition()).toString());
                                global.page.event_day[event_num] = Integer.parseInt(sp_day_dialog.getItemAtPosition(sp_day_dialog.getSelectedItemPosition()).toString());
                                global.page.event_time[event_num] = sp_time_dialog.getItemAtPosition(sp_time_dialog.getSelectedItemPosition()).toString();
                                tv_event[event_num].setText(global.page.event_month[event_num] + "月 " + global.page.event_day[event_num] + "日 " +
                                        global.page.event_title[event_num]);
                            }
                        })
                        .setNegativeButton("削除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int id) {
                                tv_event[event_num].setText("");
                                tv_title_dialog.setText("");
                                tv_place_dialog.setText("");
                                tv_detail_dialog.setText("");
                            }
                        });
                builder.create().show(); // 表示
            }
        });


        bt_event[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Q");
                event_num = 2;
                //ダイアログの設定
                LayoutInflater inflater_event = LayoutInflater.from(Provider_Activity.this);
                View event_v = inflater_event.inflate(R.layout.final_event_dialog, null);

                tv_title_dialog = (TextView) event_v.findViewById(R.id.editText_eventname_event);
                tv_title_dialog.setText(global.page.event_title[event_num]);
                tv_place_dialog = (TextView) event_v.findViewById(R.id.editText_place_event);
                tv_place_dialog.setText(global.page.event_place[event_num]);
                tv_detail_dialog = (TextView) event_v.findViewById(R.id.editText_detail_event);
                tv_detail_dialog.setText(global.page.event_detail[event_num]);
                final Spinner sp_month_dialog = (Spinner) event_v.findViewById(R.id.spinner_month_event);
                ArrayAdapter<String> adapter_month = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_month);
                sp_month_dialog.setAdapter(adapter_month);
                final Spinner sp_day_dialog = (Spinner) event_v.findViewById(R.id.spinner_day_event);
                ArrayAdapter<String> adapter_day = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_day);
                sp_day_dialog.setAdapter(adapter_day);
                final Spinner sp_time_dialog = (Spinner) event_v.findViewById(R.id.spinner_time_event);
                ArrayAdapter<String> adapter_time = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_hour2);
                sp_time_dialog.setAdapter(adapter_time);
                AlertDialog.Builder builder = new AlertDialog.Builder(Provider_Activity.this);
                builder.setTitle("選択してください") // タイトル設定
                        .setView(event_v) // レイアウト設定
                                // OK(肯定的な)ボタンの設定
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                global.page.event_title[event_num] = tv_title_dialog.getText().toString();
                                global.page.event_place[event_num] = tv_place_dialog.getText().toString();
                                global.page.event_detail[event_num] = tv_detail_dialog.getText().toString();
                                global.page.event_month[event_num] = Integer.parseInt(sp_month_dialog.getItemAtPosition(sp_month_dialog.getSelectedItemPosition()).toString());
                                global.page.event_day[event_num] = Integer.parseInt(sp_day_dialog.getItemAtPosition(sp_day_dialog.getSelectedItemPosition()).toString());
                                global.page.event_time[event_num] = sp_time_dialog.getItemAtPosition(sp_time_dialog.getSelectedItemPosition()).toString();
                                tv_event[event_num].setText(global.page.event_month[event_num] + "月 " + global.page.event_day[event_num] + "日 " +
                                        global.page.event_title[event_num]);
                            }
                        })
                        .setNegativeButton("削除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int id) {
                                tv_event[event_num].setText("");
                                tv_title_dialog.setText("");
                                tv_place_dialog.setText("");
                                tv_detail_dialog.setText("");
                            }
                        });
                builder.create().show(); // 表示
            }
        });


        bt_event[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Q");
                event_num = 3;
                //ダイアログの設定
                LayoutInflater inflater_event = LayoutInflater.from(Provider_Activity.this);
                View event_v = inflater_event.inflate(R.layout.final_event_dialog, null);

                tv_title_dialog = (TextView) event_v.findViewById(R.id.editText_eventname_event);
                tv_title_dialog.setText(global.page.event_title[event_num]);
                tv_place_dialog = (TextView) event_v.findViewById(R.id.editText_place_event);
                tv_place_dialog.setText(global.page.event_place[event_num]);
                tv_detail_dialog = (TextView) event_v.findViewById(R.id.editText_detail_event);
                tv_detail_dialog.setText(global.page.event_detail[event_num]);
                final Spinner sp_month_dialog = (Spinner) event_v.findViewById(R.id.spinner_month_event);
                ArrayAdapter<String> adapter_month = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_month);
                sp_month_dialog.setAdapter(adapter_month);
                final Spinner sp_day_dialog = (Spinner) event_v.findViewById(R.id.spinner_day_event);
                ArrayAdapter<String> adapter_day = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_day);
                sp_day_dialog.setAdapter(adapter_day);
                final Spinner sp_time_dialog = (Spinner) event_v.findViewById(R.id.spinner_time_event);
                ArrayAdapter<String> adapter_time = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_hour2);
                sp_time_dialog.setAdapter(adapter_time);
                AlertDialog.Builder builder = new AlertDialog.Builder(Provider_Activity.this);
                builder.setTitle("選択してください") // タイトル設定
                        .setView(event_v) // レイアウト設定
                                // OK(肯定的な)ボタンの設定
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                global.page.event_title[event_num] = tv_title_dialog.getText().toString();
                                global.page.event_place[event_num] = tv_place_dialog.getText().toString();
                                global.page.event_detail[event_num] = tv_detail_dialog.getText().toString();
                                global.page.event_month[event_num] = Integer.parseInt(sp_month_dialog.getItemAtPosition(sp_month_dialog.getSelectedItemPosition()).toString());
                                global.page.event_day[event_num] = Integer.parseInt(sp_day_dialog.getItemAtPosition(sp_day_dialog.getSelectedItemPosition()).toString());
                                global.page.event_time[event_num] = sp_time_dialog.getItemAtPosition(sp_time_dialog.getSelectedItemPosition()).toString();
                                tv_event[event_num].setText(global.page.event_month[event_num] + "月 " + global.page.event_day[event_num] + "日 " +
                                        global.page.event_title[event_num]);
                            }
                        })
                        .setNegativeButton("削除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int id) {
                                tv_event[event_num].setText("");
                                tv_title_dialog.setText("");
                                tv_place_dialog.setText("");
                                tv_detail_dialog.setText("");
                            }
                        });
                builder.create().show(); // 表示
            }
        });


        bt_event[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Q");
                event_num = 4;
                //ダイアログの設定
                LayoutInflater inflater_event = LayoutInflater.from(Provider_Activity.this);
                View event_v = inflater_event.inflate(R.layout.final_event_dialog, null);

                tv_title_dialog = (TextView) event_v.findViewById(R.id.editText_eventname_event);
                tv_title_dialog.setText(global.page.event_title[event_num]);
                tv_place_dialog = (TextView) event_v.findViewById(R.id.editText_place_event);
                tv_place_dialog.setText(global.page.event_place[event_num]);
                tv_detail_dialog = (TextView) event_v.findViewById(R.id.editText_detail_event);
                tv_detail_dialog.setText(global.page.event_detail[event_num]);
                final Spinner sp_month_dialog = (Spinner) event_v.findViewById(R.id.spinner_month_event);
                ArrayAdapter<String> adapter_month = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_month);
                sp_month_dialog.setAdapter(adapter_month);
                final Spinner sp_day_dialog = (Spinner) event_v.findViewById(R.id.spinner_day_event);
                ArrayAdapter<String> adapter_day = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_day);
                sp_day_dialog.setAdapter(adapter_day);
                final Spinner sp_time_dialog = (Spinner) event_v.findViewById(R.id.spinner_time_event);
                ArrayAdapter<String> adapter_time = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_hour2);
                sp_time_dialog.setAdapter(adapter_time);
                AlertDialog.Builder builder = new AlertDialog.Builder(Provider_Activity.this);
                builder.setTitle("選択してください") // タイトル設定
                        .setView(event_v) // レイアウト設定
                                // OK(肯定的な)ボタンの設定
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                global.page.event_title[event_num] = tv_title_dialog.getText().toString();
                                global.page.event_place[event_num] = tv_place_dialog.getText().toString();
                                global.page.event_detail[event_num] = tv_detail_dialog.getText().toString();
                                global.page.event_month[event_num] = Integer.parseInt(sp_month_dialog.getItemAtPosition(sp_month_dialog.getSelectedItemPosition()).toString());
                                global.page.event_day[event_num] = Integer.parseInt(sp_day_dialog.getItemAtPosition(sp_day_dialog.getSelectedItemPosition()).toString());
                                global.page.event_time[event_num] = sp_time_dialog.getItemAtPosition(sp_time_dialog.getSelectedItemPosition()).toString();
                                tv_event[event_num].setText(global.page.event_month[event_num] + "月 " + global.page.event_day[event_num] + "日 " +
                                        global.page.event_title[event_num]);
                            }
                        })
                        .setNegativeButton("削除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int id) {
                                tv_event[event_num].setText("");
                                tv_title_dialog.setText("");
                                tv_place_dialog.setText("");
                                tv_detail_dialog.setText("");
                            }
                        });
                builder.create().show(); // 表示
            }
        });


        bt_event[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Q");
                event_num = 5;
                //ダイアログの設定
                LayoutInflater inflater_event = LayoutInflater.from(Provider_Activity.this);
                View event_v = inflater_event.inflate(R.layout.final_event_dialog, null);

                tv_title_dialog = (TextView) event_v.findViewById(R.id.editText_eventname_event);
                tv_title_dialog.setText(global.page.event_title[event_num]);
                tv_place_dialog = (TextView) event_v.findViewById(R.id.editText_place_event);
                tv_place_dialog.setText(global.page.event_place[event_num]);
                tv_detail_dialog = (TextView) event_v.findViewById(R.id.editText_detail_event);
                tv_detail_dialog.setText(global.page.event_detail[event_num]);
                final Spinner sp_month_dialog = (Spinner) event_v.findViewById(R.id.spinner_month_event);
                ArrayAdapter<String> adapter_month = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_month);
                sp_month_dialog.setAdapter(adapter_month);
                final Spinner sp_day_dialog = (Spinner) event_v.findViewById(R.id.spinner_day_event);
                ArrayAdapter<String> adapter_day = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_day);
                sp_day_dialog.setAdapter(adapter_day);
                final Spinner sp_time_dialog = (Spinner) event_v.findViewById(R.id.spinner_time_event);
                ArrayAdapter<String> adapter_time = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_hour2);
                sp_time_dialog.setAdapter(adapter_time);
                AlertDialog.Builder builder = new AlertDialog.Builder(Provider_Activity.this);
                builder.setTitle("選択してください") // タイトル設定
                        .setView(event_v) // レイアウト設定
                                // OK(肯定的な)ボタンの設定
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                global.page.event_title[event_num] = tv_title_dialog.getText().toString();
                                global.page.event_place[event_num] = tv_place_dialog.getText().toString();
                                global.page.event_detail[event_num] = tv_detail_dialog.getText().toString();
                                global.page.event_month[event_num] = Integer.parseInt(sp_month_dialog.getItemAtPosition(sp_month_dialog.getSelectedItemPosition()).toString());
                                global.page.event_day[event_num] = Integer.parseInt(sp_day_dialog.getItemAtPosition(sp_day_dialog.getSelectedItemPosition()).toString());
                                global.page.event_time[event_num] = sp_time_dialog.getItemAtPosition(sp_time_dialog.getSelectedItemPosition()).toString();
                                tv_event[event_num].setText(global.page.event_month[event_num] + "月 " + global.page.event_day[event_num] + "日 " +
                                        global.page.event_title[event_num]);
                            }
                        })
                        .setNegativeButton("削除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int id) {
                                tv_event[event_num].setText("");
                                tv_title_dialog.setText("");
                                tv_place_dialog.setText("");
                                tv_detail_dialog.setText("");
                            }
                        });
                builder.create().show(); // 表示
            }
        });


        bt_event[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Q");
                event_num = 6;
                //ダイアログの設定
                LayoutInflater inflater_event = LayoutInflater.from(Provider_Activity.this);
                View event_v = inflater_event.inflate(R.layout.final_event_dialog, null);

                tv_title_dialog = (TextView) event_v.findViewById(R.id.editText_eventname_event);
                tv_title_dialog.setText(global.page.event_title[event_num]);
                tv_place_dialog = (TextView) event_v.findViewById(R.id.editText_place_event);
                tv_place_dialog.setText(global.page.event_place[event_num]);
                tv_detail_dialog = (TextView) event_v.findViewById(R.id.editText_detail_event);
                tv_detail_dialog.setText(global.page.event_detail[event_num]);
                final Spinner sp_month_dialog = (Spinner) event_v.findViewById(R.id.spinner_month_event);
                ArrayAdapter<String> adapter_month = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_month);
                sp_month_dialog.setAdapter(adapter_month);
                final Spinner sp_day_dialog = (Spinner) event_v.findViewById(R.id.spinner_day_event);
                ArrayAdapter<String> adapter_day = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_day);
                sp_day_dialog.setAdapter(adapter_day);
                final Spinner sp_time_dialog = (Spinner) event_v.findViewById(R.id.spinner_time_event);
                ArrayAdapter<String> adapter_time = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_hour2);
                sp_time_dialog.setAdapter(adapter_time);
                AlertDialog.Builder builder = new AlertDialog.Builder(Provider_Activity.this);
                builder.setTitle("選択してください") // タイトル設定
                        .setView(event_v) // レイアウト設定
                                // OK(肯定的な)ボタンの設定
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                global.page.event_title[event_num] = tv_title_dialog.getText().toString();
                                global.page.event_place[event_num] = tv_place_dialog.getText().toString();
                                global.page.event_detail[event_num] = tv_detail_dialog.getText().toString();
                                global.page.event_month[event_num] = Integer.parseInt(sp_month_dialog.getItemAtPosition(sp_month_dialog.getSelectedItemPosition()).toString());
                                global.page.event_day[event_num] = Integer.parseInt(sp_day_dialog.getItemAtPosition(sp_day_dialog.getSelectedItemPosition()).toString());
                                global.page.event_time[event_num] = sp_time_dialog.getItemAtPosition(sp_time_dialog.getSelectedItemPosition()).toString();
                                tv_event[event_num].setText(global.page.event_month[event_num] + "月 " + global.page.event_day[event_num] + "日 " +
                                        global.page.event_title[event_num]);
                            }
                        })
                        .setNegativeButton("削除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int id) {
                                tv_event[event_num].setText("");
                                tv_title_dialog.setText("");
                                tv_place_dialog.setText("");
                                tv_detail_dialog.setText("");
                            }
                        });
                builder.create().show(); // 表示
            }
        });


        bt_event[7].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Q");
                event_num = 7;
                //ダイアログの設定
                LayoutInflater inflater_event = LayoutInflater.from(Provider_Activity.this);
                View event_v = inflater_event.inflate(R.layout.final_event_dialog, null);

                tv_title_dialog = (TextView) event_v.findViewById(R.id.editText_eventname_event);
                tv_title_dialog.setText(global.page.event_title[event_num]);
                tv_place_dialog = (TextView) event_v.findViewById(R.id.editText_place_event);
                tv_place_dialog.setText(global.page.event_place[event_num]);
                tv_detail_dialog = (TextView) event_v.findViewById(R.id.editText_detail_event);
                tv_detail_dialog.setText(global.page.event_detail[event_num]);
                final Spinner sp_month_dialog = (Spinner) event_v.findViewById(R.id.spinner_month_event);
                ArrayAdapter<String> adapter_month = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_month);
                sp_month_dialog.setAdapter(adapter_month);
                final Spinner sp_day_dialog = (Spinner) event_v.findViewById(R.id.spinner_day_event);
                ArrayAdapter<String> adapter_day = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_day);
                sp_day_dialog.setAdapter(adapter_day);
                final Spinner sp_time_dialog = (Spinner) event_v.findViewById(R.id.spinner_time_event);
                ArrayAdapter<String> adapter_time = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_hour2);
                sp_time_dialog.setAdapter(adapter_time);
                AlertDialog.Builder builder = new AlertDialog.Builder(Provider_Activity.this);
                builder.setTitle("選択してください") // タイトル設定
                        .setView(event_v) // レイアウト設定
                                // OK(肯定的な)ボタンの設定
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                global.page.event_title[event_num] = tv_title_dialog.getText().toString();
                                global.page.event_place[event_num] = tv_place_dialog.getText().toString();
                                global.page.event_detail[event_num] = tv_detail_dialog.getText().toString();
                                global.page.event_month[event_num] = Integer.parseInt(sp_month_dialog.getItemAtPosition(sp_month_dialog.getSelectedItemPosition()).toString());
                                global.page.event_day[event_num] = Integer.parseInt(sp_day_dialog.getItemAtPosition(sp_day_dialog.getSelectedItemPosition()).toString());
                                global.page.event_time[event_num] = sp_time_dialog.getItemAtPosition(sp_time_dialog.getSelectedItemPosition()).toString();
                                tv_event[event_num].setText(global.page.event_month[event_num] + "月 " + global.page.event_day[event_num] + "日 " +
                                        global.page.event_title[event_num]);
                            }
                        })
                        .setNegativeButton("削除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int id) {
                                tv_event[event_num].setText("");
                                tv_title_dialog.setText("");
                                tv_place_dialog.setText("");
                                tv_detail_dialog.setText("");
                            }
                        });
                builder.create().show(); // 表示
            }
        });


        bt_event[8].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Q");
                event_num = 8;
                //ダイアログの設定
                LayoutInflater inflater_event = LayoutInflater.from(Provider_Activity.this);
                View event_v = inflater_event.inflate(R.layout.final_event_dialog, null);

                tv_title_dialog = (TextView) event_v.findViewById(R.id.editText_eventname_event);
                tv_title_dialog.setText(global.page.event_title[event_num]);
                tv_place_dialog = (TextView) event_v.findViewById(R.id.editText_place_event);
                tv_place_dialog.setText(global.page.event_place[event_num]);
                tv_detail_dialog = (TextView) event_v.findViewById(R.id.editText_detail_event);
                tv_detail_dialog.setText(global.page.event_detail[event_num]);
                final Spinner sp_month_dialog = (Spinner) event_v.findViewById(R.id.spinner_month_event);
                ArrayAdapter<String> adapter_month = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_month);
                sp_month_dialog.setAdapter(adapter_month);
                final Spinner sp_day_dialog = (Spinner) event_v.findViewById(R.id.spinner_day_event);
                ArrayAdapter<String> adapter_day = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_day);
                sp_day_dialog.setAdapter(adapter_day);
                final Spinner sp_time_dialog = (Spinner) event_v.findViewById(R.id.spinner_time_event);
                ArrayAdapter<String> adapter_time = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_hour2);
                sp_time_dialog.setAdapter(adapter_time);
                AlertDialog.Builder builder = new AlertDialog.Builder(Provider_Activity.this);
                builder.setTitle("選択してください") // タイトル設定
                        .setView(event_v) // レイアウト設定
                                // OK(肯定的な)ボタンの設定
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                global.page.event_title[event_num] = tv_title_dialog.getText().toString();
                                global.page.event_place[event_num] = tv_place_dialog.getText().toString();
                                global.page.event_detail[event_num] = tv_detail_dialog.getText().toString();
                                global.page.event_month[event_num] = Integer.parseInt(sp_month_dialog.getItemAtPosition(sp_month_dialog.getSelectedItemPosition()).toString());
                                global.page.event_day[event_num] = Integer.parseInt(sp_day_dialog.getItemAtPosition(sp_day_dialog.getSelectedItemPosition()).toString());
                                global.page.event_time[event_num] = sp_time_dialog.getItemAtPosition(sp_time_dialog.getSelectedItemPosition()).toString();
                                tv_event[event_num].setText(global.page.event_month[event_num] + "月 " + global.page.event_day[event_num] + "日 " +
                                        global.page.event_title[event_num]);
                            }
                        })
                        .setNegativeButton("削除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int id) {
                                tv_event[event_num].setText("");
                                tv_title_dialog.setText("");
                                tv_place_dialog.setText("");
                                tv_detail_dialog.setText("");
                            }
                        });
                builder.create().show(); // 表示
            }
        });



        bt_event[9].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Q");
                event_num = 9;
                //ダイアログの設定
                LayoutInflater inflater_event = LayoutInflater.from(Provider_Activity.this);
                View event_v = inflater_event.inflate(R.layout.final_event_dialog, null);

                tv_title_dialog = (TextView) event_v.findViewById(R.id.editText_eventname_event);
                tv_title_dialog.setText(global.page.event_title[event_num]);
                tv_place_dialog = (TextView) event_v.findViewById(R.id.editText_place_event);
                tv_place_dialog.setText(global.page.event_place[event_num]);
                tv_detail_dialog = (TextView) event_v.findViewById(R.id.editText_detail_event);
                tv_detail_dialog.setText(global.page.event_detail[event_num]);
                final Spinner sp_month_dialog = (Spinner) event_v.findViewById(R.id.spinner_month_event);
                ArrayAdapter<String> adapter_month = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_month);
                sp_month_dialog.setAdapter(adapter_month);
                final Spinner sp_day_dialog = (Spinner) event_v.findViewById(R.id.spinner_day_event);
                ArrayAdapter<String> adapter_day = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_day);
                sp_day_dialog.setAdapter(adapter_day);
                final Spinner sp_time_dialog = (Spinner) event_v.findViewById(R.id.spinner_time_event);
                ArrayAdapter<String> adapter_time = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_hour2);
                sp_time_dialog.setAdapter(adapter_time);
                AlertDialog.Builder builder = new AlertDialog.Builder(Provider_Activity.this);
                builder.setTitle("選択してください") // タイトル設定
                        .setView(event_v) // レイアウト設定
                                // OK(肯定的な)ボタンの設定
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                global.page.event_title[event_num] = tv_title_dialog.getText().toString();
                                global.page.event_place[event_num] = tv_place_dialog.getText().toString();
                                global.page.event_detail[event_num] = tv_detail_dialog.getText().toString();
                                global.page.event_month[event_num] = Integer.parseInt(sp_month_dialog.getItemAtPosition(sp_month_dialog.getSelectedItemPosition()).toString());
                                global.page.event_day[event_num] = Integer.parseInt(sp_day_dialog.getItemAtPosition(sp_day_dialog.getSelectedItemPosition()).toString());
                                global.page.event_time[event_num] = sp_time_dialog.getItemAtPosition(sp_time_dialog.getSelectedItemPosition()).toString();
                                tv_event[event_num].setText(global.page.event_month[event_num] + "月 " + global.page.event_day[event_num] + "日 " +
                                        global.page.event_title[event_num]);
                            }
                        })
                        .setNegativeButton("削除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int id) {
                                tv_event[event_num].setText("");
                                tv_title_dialog.setText("");
                                tv_place_dialog.setText("");
                                tv_detail_dialog.setText("");
                            }
                        });
                builder.create().show(); // 表示
            }
        });



        bt_event[10].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Q");
                event_num = 10;
                //ダイアログの設定
                LayoutInflater inflater_event = LayoutInflater.from(Provider_Activity.this);
                View event_v = inflater_event.inflate(R.layout.final_event_dialog, null);

                tv_title_dialog = (TextView) event_v.findViewById(R.id.editText_eventname_event);
                tv_title_dialog.setText(global.page.event_title[event_num]);
                tv_place_dialog = (TextView) event_v.findViewById(R.id.editText_place_event);
                tv_place_dialog.setText(global.page.event_place[event_num]);
                tv_detail_dialog = (TextView) event_v.findViewById(R.id.editText_detail_event);
                tv_detail_dialog.setText(global.page.event_detail[event_num]);
                final Spinner sp_month_dialog = (Spinner) event_v.findViewById(R.id.spinner_month_event);
                ArrayAdapter<String> adapter_month = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_month);
                sp_month_dialog.setAdapter(adapter_month);
                final Spinner sp_day_dialog = (Spinner) event_v.findViewById(R.id.spinner_day_event);
                ArrayAdapter<String> adapter_day = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_day);
                sp_day_dialog.setAdapter(adapter_day);
                final Spinner sp_time_dialog = (Spinner) event_v.findViewById(R.id.spinner_time_event);
                ArrayAdapter<String> adapter_time = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_hour2);
                sp_time_dialog.setAdapter(adapter_time);
                AlertDialog.Builder builder = new AlertDialog.Builder(Provider_Activity.this);
                builder.setTitle("選択してください") // タイトル設定
                        .setView(event_v) // レイアウト設定
                                // OK(肯定的な)ボタンの設定
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                global.page.event_title[event_num] = tv_title_dialog.getText().toString();
                                global.page.event_place[event_num] = tv_place_dialog.getText().toString();
                                global.page.event_detail[event_num] = tv_detail_dialog.getText().toString();
                                global.page.event_month[event_num] = Integer.parseInt(sp_month_dialog.getItemAtPosition(sp_month_dialog.getSelectedItemPosition()).toString());
                                global.page.event_day[event_num] = Integer.parseInt(sp_day_dialog.getItemAtPosition(sp_day_dialog.getSelectedItemPosition()).toString());
                                global.page.event_time[event_num] = sp_time_dialog.getItemAtPosition(sp_time_dialog.getSelectedItemPosition()).toString();
                                tv_event[event_num].setText(global.page.event_month[event_num] + "月 " + global.page.event_day[event_num] + "日 " +
                                        global.page.event_title[event_num]);
                            }
                        })
                        .setNegativeButton("削除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int id) {
                                tv_event[event_num].setText("");
                                tv_title_dialog.setText("");
                                tv_place_dialog.setText("");
                                tv_detail_dialog.setText("");
                            }
                        });
                builder.create().show(); // 表示
            }
        });



        bt_event[11].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Q");
                event_num = 11;
                //ダイアログの設定
                LayoutInflater inflater_event = LayoutInflater.from(Provider_Activity.this);
                View event_v = inflater_event.inflate(R.layout.final_event_dialog, null);

                tv_title_dialog = (TextView) event_v.findViewById(R.id.editText_eventname_event);
                tv_title_dialog.setText(global.page.event_title[event_num]);
                tv_place_dialog = (TextView) event_v.findViewById(R.id.editText_place_event);
                tv_place_dialog.setText(global.page.event_place[event_num]);
                tv_detail_dialog = (TextView) event_v.findViewById(R.id.editText_detail_event);
                tv_detail_dialog.setText(global.page.event_detail[event_num]);
                final Spinner sp_month_dialog = (Spinner) event_v.findViewById(R.id.spinner_month_event);
                ArrayAdapter<String> adapter_month = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_month);
                sp_month_dialog.setAdapter(adapter_month);
                final Spinner sp_day_dialog = (Spinner) event_v.findViewById(R.id.spinner_day_event);
                ArrayAdapter<String> adapter_day = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_day);
                sp_day_dialog.setAdapter(adapter_day);
                final Spinner sp_time_dialog = (Spinner) event_v.findViewById(R.id.spinner_time_event);
                ArrayAdapter<String> adapter_time = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_hour2);
                sp_time_dialog.setAdapter(adapter_time);
                AlertDialog.Builder builder = new AlertDialog.Builder(Provider_Activity.this);
                builder.setTitle("選択してください") // タイトル設定
                        .setView(event_v) // レイアウト設定
                                // OK(肯定的な)ボタンの設定
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                global.page.event_title[event_num] = tv_title_dialog.getText().toString();
                                global.page.event_place[event_num] = tv_place_dialog.getText().toString();
                                global.page.event_detail[event_num] = tv_detail_dialog.getText().toString();
                                global.page.event_month[event_num] = Integer.parseInt(sp_month_dialog.getItemAtPosition(sp_month_dialog.getSelectedItemPosition()).toString());
                                global.page.event_day[event_num] = Integer.parseInt(sp_day_dialog.getItemAtPosition(sp_day_dialog.getSelectedItemPosition()).toString());
                                global.page.event_time[event_num] = sp_time_dialog.getItemAtPosition(sp_time_dialog.getSelectedItemPosition()).toString();
                                tv_event[event_num].setText(global.page.event_month[event_num] + "月 " + global.page.event_day[event_num] + "日 " +
                                        global.page.event_title[event_num]);
                            }
                        })
                        .setNegativeButton("削除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int id) {
                                tv_event[event_num].setText("");
                                tv_title_dialog.setText("");
                                tv_place_dialog.setText("");
                                tv_detail_dialog.setText("");
                            }
                        });
                builder.create().show(); // 表示
            }
        });



        bt_event[12].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Q");
                event_num = 12;
                //ダイアログの設定
                LayoutInflater inflater_event = LayoutInflater.from(Provider_Activity.this);
                View event_v = inflater_event.inflate(R.layout.final_event_dialog, null);

                tv_title_dialog = (TextView) event_v.findViewById(R.id.editText_eventname_event);
                tv_title_dialog.setText(global.page.event_title[event_num]);
                tv_place_dialog = (TextView) event_v.findViewById(R.id.editText_place_event);
                tv_place_dialog.setText(global.page.event_place[event_num]);
                tv_detail_dialog = (TextView) event_v.findViewById(R.id.editText_detail_event);
                tv_detail_dialog.setText(global.page.event_detail[event_num]);
                final Spinner sp_month_dialog = (Spinner) event_v.findViewById(R.id.spinner_month_event);
                ArrayAdapter<String> adapter_month = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_month);
                sp_month_dialog.setAdapter(adapter_month);
                final Spinner sp_day_dialog = (Spinner) event_v.findViewById(R.id.spinner_day_event);
                ArrayAdapter<String> adapter_day = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_day);
                sp_day_dialog.setAdapter(adapter_day);
                final Spinner sp_time_dialog = (Spinner) event_v.findViewById(R.id.spinner_time_event);
                ArrayAdapter<String> adapter_time = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_hour2);
                sp_time_dialog.setAdapter(adapter_time);
                AlertDialog.Builder builder = new AlertDialog.Builder(Provider_Activity.this);
                builder.setTitle("選択してください") // タイトル設定
                        .setView(event_v) // レイアウト設定
                                // OK(肯定的な)ボタンの設定
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                global.page.event_title[event_num] = tv_title_dialog.getText().toString();
                                global.page.event_place[event_num] = tv_place_dialog.getText().toString();
                                global.page.event_detail[event_num] = tv_detail_dialog.getText().toString();
                                global.page.event_month[event_num] = Integer.parseInt(sp_month_dialog.getItemAtPosition(sp_month_dialog.getSelectedItemPosition()).toString());
                                global.page.event_day[event_num] = Integer.parseInt(sp_day_dialog.getItemAtPosition(sp_day_dialog.getSelectedItemPosition()).toString());
                                global.page.event_time[event_num] = sp_time_dialog.getItemAtPosition(sp_time_dialog.getSelectedItemPosition()).toString();
                                tv_event[event_num].setText(global.page.event_month[event_num] + "月 " + global.page.event_day[event_num] + "日 " +
                                        global.page.event_title[event_num]);
                            }
                        })
                        .setNegativeButton("削除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int id) {
                                tv_event[event_num].setText("");
                                tv_title_dialog.setText("");
                                tv_place_dialog.setText("");
                                tv_detail_dialog.setText("");
                            }
                        });
                builder.create().show(); // 表示
            }
        });



        bt_event[13].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Q");
                event_num = 13;
                //ダイアログの設定
                LayoutInflater inflater_event = LayoutInflater.from(Provider_Activity.this);
                View event_v = inflater_event.inflate(R.layout.final_event_dialog, null);

                tv_title_dialog = (TextView) event_v.findViewById(R.id.editText_eventname_event);
                tv_title_dialog.setText(global.page.event_title[event_num]);
                tv_place_dialog = (TextView) event_v.findViewById(R.id.editText_place_event);
                tv_place_dialog.setText(global.page.event_place[event_num]);
                tv_detail_dialog = (TextView) event_v.findViewById(R.id.editText_detail_event);
                tv_detail_dialog.setText(global.page.event_detail[event_num]);
                final Spinner sp_month_dialog = (Spinner) event_v.findViewById(R.id.spinner_month_event);
                ArrayAdapter<String> adapter_month = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_month);
                sp_month_dialog.setAdapter(adapter_month);
                final Spinner sp_day_dialog = (Spinner) event_v.findViewById(R.id.spinner_day_event);
                ArrayAdapter<String> adapter_day = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_day);
                sp_day_dialog.setAdapter(adapter_day);
                final Spinner sp_time_dialog = (Spinner) event_v.findViewById(R.id.spinner_time_event);
                ArrayAdapter<String> adapter_time = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_hour2);
                sp_time_dialog.setAdapter(adapter_time);
                AlertDialog.Builder builder = new AlertDialog.Builder(Provider_Activity.this);
                builder.setTitle("選択してください") // タイトル設定
                        .setView(event_v) // レイアウト設定
                                // OK(肯定的な)ボタンの設定
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                global.page.event_title[event_num] = tv_title_dialog.getText().toString();
                                global.page.event_place[event_num] = tv_place_dialog.getText().toString();
                                global.page.event_detail[event_num] = tv_detail_dialog.getText().toString();
                                global.page.event_month[event_num] = Integer.parseInt(sp_month_dialog.getItemAtPosition(sp_month_dialog.getSelectedItemPosition()).toString());
                                global.page.event_day[event_num] = Integer.parseInt(sp_day_dialog.getItemAtPosition(sp_day_dialog.getSelectedItemPosition()).toString());
                                global.page.event_time[event_num] = sp_time_dialog.getItemAtPosition(sp_time_dialog.getSelectedItemPosition()).toString();
                                tv_event[event_num].setText(global.page.event_month[event_num] + "月 " + global.page.event_day[event_num] + "日 " +
                                        global.page.event_title[event_num]);
                            }
                        })
                        .setNegativeButton("削除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int id) {
                                tv_event[event_num].setText("");
                                tv_title_dialog.setText("");
                                tv_place_dialog.setText("");
                                tv_detail_dialog.setText("");
                            }
                        });
                builder.create().show(); // 表示
            }
        });



        bt_event[14].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Q");
                event_num = 14;
                //ダイアログの設定
                LayoutInflater inflater_event = LayoutInflater.from(Provider_Activity.this);
                View event_v = inflater_event.inflate(R.layout.final_event_dialog, null);

                tv_title_dialog = (TextView) event_v.findViewById(R.id.editText_eventname_event);
                tv_title_dialog.setText(global.page.event_title[event_num]);
                tv_place_dialog = (TextView) event_v.findViewById(R.id.editText_place_event);
                tv_place_dialog.setText(global.page.event_place[event_num]);
                tv_detail_dialog = (TextView) event_v.findViewById(R.id.editText_detail_event);
                tv_detail_dialog.setText(global.page.event_detail[event_num]);
                final Spinner sp_month_dialog = (Spinner) event_v.findViewById(R.id.spinner_month_event);
                ArrayAdapter<String> adapter_month = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_month);
                sp_month_dialog.setAdapter(adapter_month);
                final Spinner sp_day_dialog = (Spinner) event_v.findViewById(R.id.spinner_day_event);
                ArrayAdapter<String> adapter_day = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_day);
                sp_day_dialog.setAdapter(adapter_day);
                final Spinner sp_time_dialog = (Spinner) event_v.findViewById(R.id.spinner_time_event);
                ArrayAdapter<String> adapter_time = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_hour2);
                sp_time_dialog.setAdapter(adapter_time);
                AlertDialog.Builder builder = new AlertDialog.Builder(Provider_Activity.this);
                builder.setTitle("選択してください") // タイトル設定
                        .setView(event_v) // レイアウト設定
                                // OK(肯定的な)ボタンの設定
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                global.page.event_title[event_num] = tv_title_dialog.getText().toString();
                                global.page.event_place[event_num] = tv_place_dialog.getText().toString();
                                global.page.event_detail[event_num] = tv_detail_dialog.getText().toString();
                                global.page.event_month[event_num] = Integer.parseInt(sp_month_dialog.getItemAtPosition(sp_month_dialog.getSelectedItemPosition()).toString());
                                global.page.event_day[event_num] = Integer.parseInt(sp_day_dialog.getItemAtPosition(sp_day_dialog.getSelectedItemPosition()).toString());
                                global.page.event_time[event_num] = sp_time_dialog.getItemAtPosition(sp_time_dialog.getSelectedItemPosition()).toString();
                                tv_event[event_num].setText(global.page.event_month[event_num] + "月 " + global.page.event_day[event_num] + "日 " +
                                        global.page.event_title[event_num]);
                            }
                        })
                        .setNegativeButton("削除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int id) {
                                tv_event[event_num].setText("");
                                tv_title_dialog.setText("");
                                tv_place_dialog.setText("");
                                tv_detail_dialog.setText("");
                            }
                        });
                builder.create().show(); // 表示
            }
        });



        bt_event[15].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Q");
                event_num = 15;
                //ダイアログの設定
                LayoutInflater inflater_event = LayoutInflater.from(Provider_Activity.this);
                View event_v = inflater_event.inflate(R.layout.final_event_dialog, null);

                tv_title_dialog = (TextView) event_v.findViewById(R.id.editText_eventname_event);
                tv_title_dialog.setText(global.page.event_title[event_num]);
                tv_place_dialog = (TextView) event_v.findViewById(R.id.editText_place_event);
                tv_place_dialog.setText(global.page.event_place[event_num]);
                tv_detail_dialog = (TextView) event_v.findViewById(R.id.editText_detail_event);
                tv_detail_dialog.setText(global.page.event_detail[event_num]);
                final Spinner sp_month_dialog = (Spinner) event_v.findViewById(R.id.spinner_month_event);
                ArrayAdapter<String> adapter_month = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_month);
                sp_month_dialog.setAdapter(adapter_month);
                final Spinner sp_day_dialog = (Spinner) event_v.findViewById(R.id.spinner_day_event);
                ArrayAdapter<String> adapter_day = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_day);
                sp_day_dialog.setAdapter(adapter_day);
                final Spinner sp_time_dialog = (Spinner) event_v.findViewById(R.id.spinner_time_event);
                ArrayAdapter<String> adapter_time = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_hour2);
                sp_time_dialog.setAdapter(adapter_time);
                AlertDialog.Builder builder = new AlertDialog.Builder(Provider_Activity.this);
                builder.setTitle("選択してください") // タイトル設定
                        .setView(event_v) // レイアウト設定
                                // OK(肯定的な)ボタンの設定
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                global.page.event_title[event_num] = tv_title_dialog.getText().toString();
                                global.page.event_place[event_num] = tv_place_dialog.getText().toString();
                                global.page.event_detail[event_num] = tv_detail_dialog.getText().toString();
                                global.page.event_month[event_num] = Integer.parseInt(sp_month_dialog.getItemAtPosition(sp_month_dialog.getSelectedItemPosition()).toString());
                                global.page.event_day[event_num] = Integer.parseInt(sp_day_dialog.getItemAtPosition(sp_day_dialog.getSelectedItemPosition()).toString());
                                global.page.event_time[event_num] = sp_time_dialog.getItemAtPosition(sp_time_dialog.getSelectedItemPosition()).toString();
                                tv_event[event_num].setText(global.page.event_month[event_num] + "月 " + global.page.event_day[event_num] + "日 " +
                                        global.page.event_title[event_num]);
                            }
                        })
                        .setNegativeButton("削除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int id) {
                                tv_event[event_num].setText("");
                                tv_title_dialog.setText("");
                                tv_place_dialog.setText("");
                                tv_detail_dialog.setText("");
                            }
                        });
                builder.create().show(); // 表示
            }
        });



        bt_event[16].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Q");
                event_num = 16;
                //ダイアログの設定
                LayoutInflater inflater_event = LayoutInflater.from(Provider_Activity.this);
                View event_v = inflater_event.inflate(R.layout.final_event_dialog, null);

                tv_title_dialog = (TextView) event_v.findViewById(R.id.editText_eventname_event);
                tv_title_dialog.setText(global.page.event_title[event_num]);
                tv_place_dialog = (TextView) event_v.findViewById(R.id.editText_place_event);
                tv_place_dialog.setText(global.page.event_place[event_num]);
                tv_detail_dialog = (TextView) event_v.findViewById(R.id.editText_detail_event);
                tv_detail_dialog.setText(global.page.event_detail[event_num]);
                final Spinner sp_month_dialog = (Spinner) event_v.findViewById(R.id.spinner_month_event);
                ArrayAdapter<String> adapter_month = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_month);
                sp_month_dialog.setAdapter(adapter_month);
                final Spinner sp_day_dialog = (Spinner) event_v.findViewById(R.id.spinner_day_event);
                ArrayAdapter<String> adapter_day = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_day);
                sp_day_dialog.setAdapter(adapter_day);
                final Spinner sp_time_dialog = (Spinner) event_v.findViewById(R.id.spinner_time_event);
                ArrayAdapter<String> adapter_time = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_hour2);
                sp_time_dialog.setAdapter(adapter_time);
                AlertDialog.Builder builder = new AlertDialog.Builder(Provider_Activity.this);
                builder.setTitle("選択してください") // タイトル設定
                        .setView(event_v) // レイアウト設定
                                // OK(肯定的な)ボタンの設定
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                global.page.event_title[event_num] = tv_title_dialog.getText().toString();
                                global.page.event_place[event_num] = tv_place_dialog.getText().toString();
                                global.page.event_detail[event_num] = tv_detail_dialog.getText().toString();
                                global.page.event_month[event_num] = Integer.parseInt(sp_month_dialog.getItemAtPosition(sp_month_dialog.getSelectedItemPosition()).toString());
                                global.page.event_day[event_num] = Integer.parseInt(sp_day_dialog.getItemAtPosition(sp_day_dialog.getSelectedItemPosition()).toString());
                                global.page.event_time[event_num] = sp_time_dialog.getItemAtPosition(sp_time_dialog.getSelectedItemPosition()).toString();
                                tv_event[event_num].setText(global.page.event_month[event_num] + "月 " + global.page.event_day[event_num] + "日 " +
                                        global.page.event_title[event_num]);
                            }
                        })
                        .setNegativeButton("削除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int id) {
                                tv_event[event_num].setText("");
                                tv_title_dialog.setText("");
                                tv_place_dialog.setText("");
                                tv_detail_dialog.setText("");
                            }
                        });
                builder.create().show(); // 表示
            }
        });



        bt_event[17].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Q");
                event_num = 17;
                //ダイアログの設定
                LayoutInflater inflater_event = LayoutInflater.from(Provider_Activity.this);
                View event_v = inflater_event.inflate(R.layout.final_event_dialog, null);

                tv_title_dialog = (TextView) event_v.findViewById(R.id.editText_eventname_event);
                tv_title_dialog.setText(global.page.event_title[event_num]);
                tv_place_dialog = (TextView) event_v.findViewById(R.id.editText_place_event);
                tv_place_dialog.setText(global.page.event_place[event_num]);
                tv_detail_dialog = (TextView) event_v.findViewById(R.id.editText_detail_event);
                tv_detail_dialog.setText(global.page.event_detail[event_num]);
                final Spinner sp_month_dialog = (Spinner) event_v.findViewById(R.id.spinner_month_event);
                ArrayAdapter<String> adapter_month = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_month);
                sp_month_dialog.setAdapter(adapter_month);
                final Spinner sp_day_dialog = (Spinner) event_v.findViewById(R.id.spinner_day_event);
                ArrayAdapter<String> adapter_day = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_day);
                sp_day_dialog.setAdapter(adapter_day);
                final Spinner sp_time_dialog = (Spinner) event_v.findViewById(R.id.spinner_time_event);
                ArrayAdapter<String> adapter_time = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_hour2);
                sp_time_dialog.setAdapter(adapter_time);
                AlertDialog.Builder builder = new AlertDialog.Builder(Provider_Activity.this);
                builder.setTitle("選択してください") // タイトル設定
                        .setView(event_v) // レイアウト設定
                                // OK(肯定的な)ボタンの設定
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                global.page.event_title[event_num] = tv_title_dialog.getText().toString();
                                global.page.event_place[event_num] = tv_place_dialog.getText().toString();
                                global.page.event_detail[event_num] = tv_detail_dialog.getText().toString();
                                global.page.event_month[event_num] = Integer.parseInt(sp_month_dialog.getItemAtPosition(sp_month_dialog.getSelectedItemPosition()).toString());
                                global.page.event_day[event_num] = Integer.parseInt(sp_day_dialog.getItemAtPosition(sp_day_dialog.getSelectedItemPosition()).toString());
                                global.page.event_time[event_num] = sp_time_dialog.getItemAtPosition(sp_time_dialog.getSelectedItemPosition()).toString();
                                tv_event[event_num].setText(global.page.event_month[event_num] + "月 " + global.page.event_day[event_num] + "日 " +
                                        global.page.event_title[event_num]);
                            }
                        })
                        .setNegativeButton("削除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int id) {
                                tv_event[event_num].setText("");
                                tv_title_dialog.setText("");
                                tv_place_dialog.setText("");
                                tv_detail_dialog.setText("");
                            }
                        });
                builder.create().show(); // 表示
            }
        });



        bt_event[18].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Q");
                event_num =18;
                //ダイアログの設定
                LayoutInflater inflater_event = LayoutInflater.from(Provider_Activity.this);
                View event_v = inflater_event.inflate(R.layout.final_event_dialog, null);

                tv_title_dialog = (TextView) event_v.findViewById(R.id.editText_eventname_event);
                tv_title_dialog.setText(global.page.event_title[event_num]);
                tv_place_dialog = (TextView) event_v.findViewById(R.id.editText_place_event);
                tv_place_dialog.setText(global.page.event_place[event_num]);
                tv_detail_dialog = (TextView) event_v.findViewById(R.id.editText_detail_event);
                tv_detail_dialog.setText(global.page.event_detail[event_num]);
                final Spinner sp_month_dialog = (Spinner) event_v.findViewById(R.id.spinner_month_event);
                ArrayAdapter<String> adapter_month = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_month);
                sp_month_dialog.setAdapter(adapter_month);
                final Spinner sp_day_dialog = (Spinner) event_v.findViewById(R.id.spinner_day_event);
                ArrayAdapter<String> adapter_day = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_day);
                sp_day_dialog.setAdapter(adapter_day);
                final Spinner sp_time_dialog = (Spinner) event_v.findViewById(R.id.spinner_time_event);
                ArrayAdapter<String> adapter_time = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_hour2);
                sp_time_dialog.setAdapter(adapter_time);
                AlertDialog.Builder builder = new AlertDialog.Builder(Provider_Activity.this);
                builder.setTitle("選択してください") // タイトル設定
                        .setView(event_v) // レイアウト設定
                                // OK(肯定的な)ボタンの設定
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                global.page.event_title[event_num] = tv_title_dialog.getText().toString();
                                global.page.event_place[event_num] = tv_place_dialog.getText().toString();
                                global.page.event_detail[event_num] = tv_detail_dialog.getText().toString();
                                global.page.event_month[event_num] = Integer.parseInt(sp_month_dialog.getItemAtPosition(sp_month_dialog.getSelectedItemPosition()).toString());
                                global.page.event_day[event_num] = Integer.parseInt(sp_day_dialog.getItemAtPosition(sp_day_dialog.getSelectedItemPosition()).toString());
                                global.page.event_time[event_num] = sp_time_dialog.getItemAtPosition(sp_time_dialog.getSelectedItemPosition()).toString();
                                tv_event[event_num].setText(global.page.event_month[event_num] + "月 " + global.page.event_day[event_num] + "日 " +
                                        global.page.event_title[event_num]);
                            }
                        })
                        .setNegativeButton("削除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int id) {
                                tv_event[event_num].setText("");
                                tv_title_dialog.setText("");
                                tv_place_dialog.setText("");
                                tv_detail_dialog.setText("");
                            }
                        });
                builder.create().show(); // 表示
            }
        });



        bt_event[19].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Q");
                event_num = 19;
                //ダイアログの設定
                LayoutInflater inflater_event = LayoutInflater.from(Provider_Activity.this);
                View event_v = inflater_event.inflate(R.layout.final_event_dialog, null);

                tv_title_dialog = (TextView) event_v.findViewById(R.id.editText_eventname_event);
                tv_title_dialog.setText(global.page.event_title[event_num]);
                tv_place_dialog = (TextView) event_v.findViewById(R.id.editText_place_event);
                tv_place_dialog.setText(global.page.event_place[event_num]);
                tv_detail_dialog = (TextView) event_v.findViewById(R.id.editText_detail_event);
                tv_detail_dialog.setText(global.page.event_detail[event_num]);
                final Spinner sp_month_dialog = (Spinner) event_v.findViewById(R.id.spinner_month_event);
                ArrayAdapter<String> adapter_month = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_month);
                sp_month_dialog.setAdapter(adapter_month);
                final Spinner sp_day_dialog = (Spinner) event_v.findViewById(R.id.spinner_day_event);
                ArrayAdapter<String> adapter_day = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_day);
                sp_day_dialog.setAdapter(adapter_day);
                final Spinner sp_time_dialog = (Spinner) event_v.findViewById(R.id.spinner_time_event);
                ArrayAdapter<String> adapter_time = new ArrayAdapter<String>(Provider_Activity.this, android.R.layout.simple_list_item_1, str_hour2);
                sp_time_dialog.setAdapter(adapter_time);
                AlertDialog.Builder builder = new AlertDialog.Builder(Provider_Activity.this);
                builder.setTitle("選択してください") // タイトル設定
                        .setView(event_v) // レイアウト設定
                                // OK(肯定的な)ボタンの設定
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                global.page.event_title[event_num] = tv_title_dialog.getText().toString();
                                global.page.event_place[event_num] = tv_place_dialog.getText().toString();
                                global.page.event_detail[event_num] = tv_detail_dialog.getText().toString();
                                global.page.event_month[event_num] = Integer.parseInt(sp_month_dialog.getItemAtPosition(sp_month_dialog.getSelectedItemPosition()).toString());
                                global.page.event_day[event_num] = Integer.parseInt(sp_day_dialog.getItemAtPosition(sp_day_dialog.getSelectedItemPosition()).toString());
                                global.page.event_time[event_num] = sp_time_dialog.getItemAtPosition(sp_time_dialog.getSelectedItemPosition()).toString();
                                tv_event[event_num].setText(global.page.event_month[event_num] + "月 " + global.page.event_day[event_num] + "日 " +
                                        global.page.event_title[event_num]);
                            }
                        })
                        .setNegativeButton("削除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int id) {
                                tv_event[event_num].setText("");
                                tv_title_dialog.setText("");
                                tv_place_dialog.setText("");
                                tv_detail_dialog.setText("");
                            }
                        });
                builder.create().show(); // 表示
            }
        });



        // ArrayAdapter<String> adapter_event = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, str_hour);
/*
        for(int i=0;i<EVENT_NUM;i++){
            event=i;
            sp_event_time[i] = (Spinner) findViewById(R.id.spinner_event1_time_provider);
            sp_event_time[i].setAdapter(adapter_event);
            sp_event_time[i].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    global.server_event_time[event] = (String) parent.getItemAtPosition(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            tv_event_title[i] = (TextView) findViewById(R.id.textView_event1_provider);
            tv_event_title[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_event_title[event].setText("");
                }
            });
            tv_event_title[i].setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus == false) {
                        TextView tv = (TextView) v;
                    }
                }
            });
            tv_event_title[i].setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    System.out.println(v);
                    return false;
                }
            });


            tv_event_comment[i] = (TextView) findViewById(R.id.textView_event1_provider);
            tv_event_comment[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_event_comment[event].setText("");
                }
            });
            tv_event_comment[i].setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus == false) {
                        TextView tv = (TextView) v;
                    }
                }
            });
            tv_event_comment[i].setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    System.out.println(v);
                    return false;
                }
            });
        }
  */

        bt_register = (Button) findViewById(R.id.bt_register_provider_register);
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Url_connection.upLoadProviderPage();
                /*以下の変数をデータベースに格納
                * あとIDの割り当ても
                *
                * global.server_title
                * global.server_field
                * global.server_school
                * global.server_place
                * global.server_week_mon
                * global.server_week_tue
                * global.server_week_wed
                * global.server_week_thu
                * global.server_week_fri
                * global.server_week_sat
                * global.server_week_sun
                * global.server_week
                * global.server_time_st
                * global.server_time_et
                * global.server_member
                * global.server_mail
                * global.server_event_detail[event_num]
                * global.server_event_month[event_num]
                * global.server_event_day[event_num]
                * global.server_event_time[event_num]

                * */
                Toast.makeText(getApplicationContext(),
                        "登録は完了しました.",
                        Toast.LENGTH_LONG).show();
                System.out.println(global.server_title + "\n" + global.server_field + "\n" + global.server_school + "\n" + global.server_place + "\n" +
                         global.server_week+ "\n"+ global.server_time_st + "\n" + global.server_time_et +  "\n"+ global.server_mail+"\n"+
                        global.server_event_title[0]+"\n"+global.server_event_month[0]+"\n"+global.server_event_day[0]+"\n"+global.server_event_time[0]+
                        "\n"+global.server_event_detail[0]);
                Intent intent = new Intent(Provider_Activity.this, Top_Activity.class);
                startActivity(intent);
            }
        });
    }
}
