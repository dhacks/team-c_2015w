package com.example.takaki.ebimayo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by Takaki on 2016/02/19.
 */
public class Search_Activity extends Activity implements View.OnClickListener {

    private Common global;
    public int STORAGE = 100;
    ImageButton bt_calender;
    ImageButton bt_search;
    ImageButton bt_favorite;
    ImageButton bt_setting;
    public final static String TAG_CALENDAR = "CALENDAR";
    public final static String TAG_SEARCH = "SEARCH";
    public final static String TAG_FAVORITE = "FAVORITE";
    public final static String TAG_RESPONSE = "RESPONSE";
    public final static String TAG_SETTING = "SETTING";
    public final static String TAG_SEARCH_TITLE = "SEARCH_TITLE";

    int select_field = 0;
    int select_school = 0;
    Spinner sp_field;
    Spinner sp_school;
    ImageButton bt_search_title;
    String[] str_field = {
            "すべて", "サッカー", "テニス", "野球", "文化系"
    };
    String[] str_school = {
            "すべて", "同今出川", "同京田辺", "同女今出川", "同女京田辺", "同今出川,同京田辺", "同女今出川,同女京田辺"
    };

    GridView gridView;
    ArrayAdapter<String> adapter_title;
    String[] str_title = new String[100];


    public class BindData {
        int iconId;
        String title;

        BindData(int id, String s) {
            this.iconId = id;
            this.title = s;
        }
    }

    private BindData[] mDatas = new BindData[100];
    int str_id[] = new int[100];


    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.final_search_layout1);
    }

    public void onStart() {
        super.onStart();

        // グローバル変数を扱うクラスを取得する
        global = (Common) getApplication();
        for (int i = 0; i < global.title_num; i++) {
            str_title[i] = "_";
        }

        for (int i = 0; i < 100; i++) {
            mDatas[i] = new BindData(android.R.drawable.ic_menu_search, "_");
        }

        bt_calender = (ImageButton) findViewById(R.id.bt_calendar_search);
        bt_calender.setTag(TAG_CALENDAR);
        bt_calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Search_Activity.this, Top_Activity.class);
                startActivity(intent);
            }
        });
        bt_search = (ImageButton) findViewById(R.id.bt_search_search);
        bt_search.setTag(TAG_SEARCH);
        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Search_Activity.this, Search_Activity.class);
                startActivity(intent);
            }
        });
        bt_favorite = (ImageButton) findViewById(R.id.bt_favorite_search);
        bt_favorite.setTag(TAG_FAVORITE);
        bt_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Search_Activity.this, Favorite_Activity.class);
                startActivity(intent);
            }
        });

        bt_setting = (ImageButton) findViewById(R.id.bt_setting_search);
        bt_setting.setTag(TAG_SETTING);
        bt_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Search_Activity.this, Setting_Activity.class);
                startActivity(intent);
            }
        });

        sp_field = (Spinner) findViewById(R.id.spinner_field_search);
        ArrayAdapter<String> adapter_field = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, str_field);
        adapter_field.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        sp_field.setAdapter(adapter_field);
        sp_field.setAdapter(adapter_field);
        sp_field.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                select_field = position;
                global.server_field = select_field;
                System.out.println("カテゴリー:" + select_field);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        sp_school = (Spinner) findViewById(R.id.spinner_school_search);
        ArrayAdapter<String> adapter_school = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, str_school);
        adapter_school.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        sp_school.setAdapter(adapter_school);
        sp_school.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                select_school = position;
                global.server_school = select_school;
                System.out.println("校地:" + select_school);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        gridView = (GridView) findViewById(R.id.gridView_title_search);
        adapter_title = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, str_title);
        gridView.setAdapter(new MyAdapter(this, R.layout.item, mDatas));
        //gridView.setAdapter(adapter_title);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                global.server_id = global.data[position].id;
                System.out.println(global.server_id);
                //////////////////////////////////////////////////////////////////////////////////////
                Intent intent = new Intent(Search_Activity.this, Profile_Activity.class);
                startActivity(intent);
            }
        });

        bt_search_title = (ImageButton) findViewById(R.id.bt_searchtitle_search);
        bt_search_title.setTag(TAG_SEARCH_TITLE);
        bt_search_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = 0;
                for (int i = 0; i < STORAGE; i++) {//searchボタンが押されるたびにリストを初期化
                    mDatas[i].iconId = android.R.drawable.ic_menu_search;
                    mDatas[i].title = "_";
                }
                global.search_result_num = 0;
                Url_connection.getSearchResult(global.server_field, global.server_school);

                /*
                global.data.server_field
                global.data.server_school
                global.data[].id =
                global.data[].title =
                global.data[].field =
                global.data[].school =
                */
                /*********(仮)***********/

                /**************************/
                for(int i=0;i<global.search_result_num;i++) {
                    mDatas[i] = new BindData(android.R.drawable.ic_menu_search, global.data[i].title);
                    str_id[i] = global.data[i].id;
                }

                gridView.setAdapter(new MyAdapter(Search_Activity.this, R.layout.item, mDatas));
            }
        });

    }


    static class ViewHolder {
        TextView textview;
        ImageView imageview;
    }

    public class MyAdapter extends ArrayAdapter<BindData> {//画像と文字のGridViewの定義
        private LayoutInflater inflater;
        private int layoutId;

        public MyAdapter(Context context, int layoutId, BindData[] objects) {
            super(context, 0, objects);
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.layoutId = layoutId;
        }

        public View getView(int position, View convertview, ViewGroup parent) {
            ViewHolder holder;

            if (convertview == null) {
                convertview = inflater.inflate(layoutId, parent, false);
                holder = new ViewHolder();
                holder.textview = (TextView) convertview.findViewById(R.id.textview);
                holder.imageview = (ImageView) convertview.findViewById(R.id.imageview);
                convertview.setTag(holder);
            } else {
                holder = (ViewHolder) convertview.getTag();
            }
            BindData data = getItem(position);
            holder.textview.setText(data.title);
            holder.imageview.setImageResource(data.iconId);
            return convertview;
        }
    }

    public void onClick(View view) {
        String tag = (String) view.getTag();
        System.out.print("ttt");
    }

    public void onRestart() {
        super.onRestart();
        System.out.println("WWW");
    }
}
