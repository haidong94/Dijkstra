package com.example.user.scatterplotgraphview;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sdsmdg.harjot.rotatingtext.RotatingTextWrapper;
import com.sdsmdg.harjot.rotatingtext.models.Rotatable;

/**
 * Created by DONG on 20-Jun-17.
 */

public class StartActivity extends AppCompatActivity {
    TextInputLayout layout_line,layout_point;
    private EditText ed_sodiem,ed_soduong;
    private Button btn_Start;
    String point, line;
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

                point=ed_sodiem.getText().toString();
                line=ed_soduong.getText().toString();

                if(TextUtils.isEmpty(point))
                    layout_point.setError(getResources().getString(R.string.EmptyPoint));
                else
                    layout_point.setErrorEnabled(false);
                if(TextUtils.isEmpty(line))
                    layout_line.setError(getResources().getString(R.string.EmptyLine));
                else
                    layout_line.setErrorEnabled(false);

                if(!TextUtils.isEmpty(point)&&!TextUtils.isEmpty(line))
                {
                    int sodiem = Integer.parseInt(point);
                    int soduong = Integer.parseInt(line);
                    if(soduong>sodiem*(sodiem-1)/2)
                    {
                        layout_line.setError(getResources().getString(R.string.ErrorLine)+" "+(sodiem*(sodiem-1)/2));
                    }
                    else
                    {
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

                }
            }
        });
    }

    private void addControl() {
        layout_point= (TextInputLayout) findViewById(R.id.layout_point);
        layout_line= (TextInputLayout) findViewById(R.id.layout_line);
        ed_sodiem= (EditText) findViewById(R.id.ed_sodiem);
        ed_soduong= (EditText) findViewById(R.id.ed_soduong);
        btn_Start= (Button) findViewById(R.id.btn_start);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/LoveYaLikeASister.ttf");
        Typeface typeface2 = Typeface.createFromAsset(getAssets(), "fonts/Reckoner_Bold.ttf");

        RotatingTextWrapper rotatingTextWrapper = (RotatingTextWrapper) findViewById(R.id.custom_switcher);
        rotatingTextWrapper.setSize(35);
        rotatingTextWrapper.setTypeface(typeface2);

        Rotatable rotatable = new Rotatable(Color.parseColor("#FFA036"), 1000, "Thuật Toán Dijkstra","By","Hoàng Hải Đông");
        rotatable.setSize(35);
        rotatable.setTypeface(typeface);
        rotatable.setInterpolator(new AccelerateInterpolator());
        rotatable.setAnimationDuration(500);

        Rotatable rotatable2 = new Rotatable(Color.parseColor("#123456"), 1000," Thuật Toán","  Dijkstra","      By", "  Hải Đông");
        rotatable2.setSize(25);
        rotatable2.setTypeface(typeface);
        rotatable2.setInterpolator(new DecelerateInterpolator());
        rotatable2.setAnimationDuration(500);

        rotatingTextWrapper.setContent("This is ? ", rotatable2);

    }
}
