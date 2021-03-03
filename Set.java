package com.example.snakegame;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.snakegame.R;


public class Set extends AppCompatActivity {
    TextView t1s,t2s,t3,t4,t1r,t1g,t1b,t2r,t2g,t2b,t3s,t3r,t3g,t3b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_activityset);

    }
    private static int toInt(EditText view) {
        return Integer.parseInt(String.valueOf(view.getText()));
    }
    @Override
    public void onBackPressed() {
        t1s = findViewById(R.id.textOfBoundColors);
        t1r = findViewById(R.id.textOfBoundColorr);
        t1g = findViewById(R.id.textOfBoundColorg);
        t1b = findViewById(R.id.textOfBoundColorb);
        t2s = findViewById(R.id.textOfSnakeColors);
        t2r = findViewById(R.id.textOfSnakeColorr);
        t2g = findViewById(R.id.textOfSnakeColorg);
        t2b = findViewById(R.id.textOfSnakeColorb);
        t3s = findViewById(R.id.textOfFoodColors);
        t3r = findViewById(R.id.textOfFoodColorr);
        t3g = findViewById(R.id.textOfFoodColorg);
        t3b = findViewById(R.id.textOfFoodColorb);
        t4 = findViewById(R.id.textOfTime);
        t3 = findViewById(R.id.textOfDiv);
        try {
            Intent intent = getIntent();
            Bundle bundle = new Bundle();
            bundle.putIntArray("Bound", new int[]{toInt((EditText) t1s), toInt((EditText) t1r),
                    toInt((EditText) t1g), toInt((EditText) t1b)});
            bundle.putIntArray("Snake", new int[]{toInt((EditText) t2s), toInt((EditText) t2r),
                    toInt((EditText) t2g), toInt((EditText) t2b)});
            bundle.putIntArray("Food", new int[]{toInt((EditText) t3s), toInt((EditText) t3r),
                    toInt((EditText) t3g), toInt((EditText) t3b)});
            bundle.putInt("Time", toInt((EditText) t4));
            bundle.putInt("Div", toInt((EditText) t3));
            intent.putExtras(bundle);
            setResult(1, intent);
        } catch (Exception ignored) {
        }
        super.onBackPressed();
    }
}
