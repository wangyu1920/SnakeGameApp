package com.example.snakegame;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {
    GameBound gameBound;
    GestureDetector detector;
    final int distance=50;
    Button start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detector = new GestureDetector(this,this);
        setContentView(R.layout.activity_main);
        gameBound = findViewById(R.id.game);
        start = findViewById(R.id.button_start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameBound.start();
            }
        });
        findViewById(R.id.TextOfMain_lianxi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(MainActivity.this, childActivity.class));
                startActivity(intent);
            }
        });
        Button button_pause = findViewById(R.id.button_pause);
        button_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gameBound.length >1) {
                    gameBound.isStart= !gameBound.isStart;
                }
            }
        });
        Button button_up = findViewById(R.id.button_up);
        button_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gameBound.direction==2|!gameBound.isStart) {
                    return;
                }
                gameBound.direction=1;
            }
        });
        Button button_down = findViewById(R.id.button_down);
        button_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (gameBound.direction==1|!gameBound.isStart) {
                    return;
                }
                gameBound.direction=2;
            }
        });
        Button button_left = findViewById(R.id.button_left);
        button_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gameBound.direction==4|!gameBound.isStart) {
                    return;
                }
                gameBound.direction=3;
            }
        });
        Button button_right = findViewById(R.id.button_right);
        button_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gameBound.direction==3|!gameBound.isStart) {
                    return;
                }
                gameBound.direction=4;
            }
        });
        findViewById(R.id.TextOfMain_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameBound.isStart=false;
                gameBound.clean();
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(MainActivity.this, Set.class));
                startActivityForResult(intent,1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle= data != null ? data.getExtras() : null;
        if (bundle!=null){
            System.out.println(Arrays.toString(bundle.getIntArray("Bound")));
            System.out.println(Arrays.toString(bundle.getIntArray("Snake")));
            gameBound=findViewById(R.id.game);
            gameBound.time= bundle.getInt("Time");
            gameBound.mapSize=bundle.getInt("Div");
            gameBound.boundColor = bundle.getIntArray("Bound");
            gameBound.snakeColor = bundle.getIntArray("Snake");
            gameBound.foodColor = bundle.getIntArray("Food");
        }
    }
    public long exitTime=0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1.getY()>1550) {
            return false;
        }
        float dX =e2.getX()-e1.getX();
        float dY =e2.getY()-e1.getY();
        if (Math.abs(dX)>distance|Math.abs(dY)>distance&gameBound.isStart) {
            if (Math.abs(dY/dX)>1&&dY>0) {
                if (gameBound.direction!=1) {
                    gameBound.direction=2;
                }
            } else if (Math.abs(dY / dX) > 1 && dY < 0) {
                if (gameBound.direction!=2) {
                    gameBound.direction=1;
                }
            }else if (Math.abs(dY / dX) < 1 && dX < 0) {
                if (gameBound.direction!=4) {
                    gameBound.direction=3;
                }
            }else if (Math.abs(dY / dX) < 1 && dX > 0) {
                if (gameBound.direction!=3) {
                    gameBound.direction=4;
                }
            }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return detector.onTouchEvent(event);
    }
}
