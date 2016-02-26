package com.example.takaki.ebimayo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ToggleButton;

/**
 * Created by Takaki on 2016/02/26.
 */
public class About_Activity extends Activity {
    ImageButton bt_calendar;
    ImageButton bt_search;
    ImageButton bt_favorite;
    ImageButton bt_setting;
    ImageButton bt_mail;
    ToggleButton bt_favorite_title;
    public void onCreate(Bundle b){
        super.onCreate(b);

          /*下のボタンの画面遷移ここから*/
        setContentView(R.layout.about_layout);
        bt_calendar = (ImageButton) findViewById(R.id.bt_calender_about);

        bt_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(About_Activity.this, Top_Activity.class);
                startActivity(intent);
            }
        });
        bt_search = (ImageButton) findViewById(R.id.bt_search_about);
        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(About_Activity.this, Search_Activity.class);
                startActivity(intent);
            }
        });
        bt_favorite = (ImageButton) findViewById(R.id.bt_favorite_about);
        bt_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(About_Activity.this, Favorite_Activity.class);
                startActivity(intent);
            }
        });

        bt_setting = (ImageButton) findViewById(R.id.bt_setting_about);
        bt_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(About_Activity.this, Setting_Activity.class);
                startActivity(intent);
            }
        });
    }
}
