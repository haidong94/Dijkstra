package com.example.user.scatterplotgraphview;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by DONG on 20-Jun-17.
 */

public class StartActivity extends AppCompatActivity {
    private EditText ed_sodiem,ed_soduong;
    private Button btn_Start;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        addControl();
        addEvent();
    }

    private void addEvent() {

        btn_Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sodiem = Integer.parseInt("0" + ed_sodiem.getText().toString());
                int soduong = Integer.parseInt("0" + ed_soduong.getText().toString());
                if (sodiem > 20)
                {
                    Toast.makeText(StartActivity.this,"Sô điểm phải nhỏ hơn 20",Toast.LENGTH_SHORT).show();
                }
                if(soduong>400)
                    Toast.makeText(StartActivity.this,"Sô đường phải nhỏ hơn 400",Toast.LENGTH_SHORT).show();

                if(sodiem<20&&soduong<400) {
                    Intent intent = new Intent(StartActivity.this, MainActivity.class);
                    intent.putExtra("sodiem", sodiem);
                    intent.putExtra("soduong", soduong);
                    startActivity(intent);
                }
            }
        });
    }

    private void addControl() {
        ed_sodiem= (EditText) findViewById(R.id.ed_sodiem);
        ed_soduong= (EditText) findViewById(R.id.ed_soduong);
        btn_Start= (Button) findViewById(R.id.btn_start);

    }
}
