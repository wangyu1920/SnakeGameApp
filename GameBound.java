package com.example.snakegame;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.Random;


public class GameBound extends View {
    //    ------------------------------------------
//    自定义的参数:背景颜色，蛇的颜色(argb),蛇的速度
    public int[] boundColor = {255, 255, 255, 255};
    public int[] snakeColor = {255, 255, 0, 0};
    public int[] foodColor = {255, 255, 255, 0};
    public int time = 500;
    public int mapSize = 12;
    //    ------------------------------------------------
//    蛇的参数
//    长度
    public int length = 0;
    //    主体
    private int[][] snakeArray = new int[mapSize * mapSize][3];
    //    移动方向
    public int direction = 1;
    //    蛇头位置记录
    public int head;
    public int foot;
    //----------------------------------------
//    地图/食物参数
    boolean isStart;
    int x;
    int y;
    int left = 0;
    int right = 800;
    int bottom = 800;
    int top = 0;
    //--------------------------------------------------
//    判断数组中是否含有元素的方法
    private boolean ArrayIsContain(int x,int y) {
        int[] childArray=new int[]{0,x,y};
        for (int i = 0; i < length; i++) {
            if (Arrays.equals(snakeArray[i], childArray)) {
                return true;
            }
        }
        return false;
    }

    //    自动生成食物
    private boolean createFood() {
        int loopNum = 0;
        Random random = new Random();
        do {
            loopNum++;
            x = random.nextInt(mapSize) + 1;
            y = random.nextInt(mapSize) + 1;
        } while (ArrayIsContain(x,y) && loopNum < mapSize * mapSize);
        return loopNum <= mapSize * mapSize;
    }

    //    判断是否吃到食物的方法
    public boolean isEat() {
        switch (direction) {
            case 1:
                return snakeArray[head][1] == x && snakeArray[head][2] - 1 == y;
            case 2:
                return snakeArray[head][1] == x && snakeArray[head][2] + 1 == y;
            case 3:
                return snakeArray[head][1] - 1 == x && snakeArray[head][2] == y;
            case 4:
                return snakeArray[head][1] + 1 == x && snakeArray[head][2] == y;
            default:
                return false;
        }
    }

    //    判断是否撞墙/撞身体
    public boolean isBreak() {
        if (snakeArray[head][1] > mapSize |
                snakeArray[head][1] < 1 |
                snakeArray[head][2] > mapSize |
                snakeArray[head][2] < 1) {
            return true;
        }
        for (int i = 1; i < length; i++) {
            if (((i != head && snakeArray[i][1] == snakeArray[head][1]) &&
                    snakeArray[i][2] == snakeArray[head][2])) {
                return true;
            }
        }
        return false;
    }

    //    清理屏幕
    public void clean() {
        snakeArray = new int[mapSize * mapSize][3];
        length = 0;
        x = y = 0;
    }

    //    生成一只蛇;外加食物
    public void born() {
        clean();
        snakeArray = new int[mapSize * mapSize][3];
        direction = 1;
        snakeArray[0][1] = mapSize / 2;
        snakeArray[0][2] = mapSize / 2;
        snakeArray[1][1] = mapSize / 2;
        snakeArray[1][2] = mapSize / 2 + 1;
        head = 0;
        foot = 1;
        length = 2;
        createFood();
    }

    //    开始游戏
    public void start() {
        isStart = true;
        born();
    }

    //    蛇移动的方法
    private void move(int direction) {
        if (isEat()) {
            System.arraycopy(snakeArray, head, snakeArray, head + 1, length - head);
            int[] headnum = new int[]{snakeArray[head][0],
                    snakeArray[head][1], snakeArray[head][2]};
            switch (direction) {
                case 1:
                    snakeArray[head][2] -= 1;
                    break;
                case 2:
                    snakeArray[head][2] += 1;
                    break;
                case 3:
                    snakeArray[head][1] -= 1;
                    break;
                case 4:
                    snakeArray[head][1] += 1;
            }
            snakeArray[head + 1] = headnum;
            length++;
            foot += (foot > head ? 1 : 0);
            if (createFood()) {
                if (length > mapSize * mapSize / 2) {
                    @SuppressLint("DrawAllocation") AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("You Win!");
                    builder.setPositiveButton("确定", null).show();
                    clean();
                    isStart = false;
                    return;
                }
                Toast.makeText(getContext(), "你变\'长\'了", Toast.LENGTH_SHORT).show();
            } else {
                @SuppressLint("DrawAllocation") AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("You Win!");
                builder.setPositiveButton("确定", null).show();
                clean();
                isStart = false;
            }
        } else {
            snakeArray[foot][1] = direction == 3 ? snakeArray[head][1] - 1 :
                    direction == 4 ? snakeArray[head][1] + 1 : snakeArray[head][1];
            snakeArray[foot][2] = direction == 1 ? snakeArray[head][2] - 1 :
                    direction == 2 ? snakeArray[head][2] + 1 : snakeArray[head][2];

            head = foot;
            foot = foot == 0 ? length - 1 : foot - 1;
        }
    }

    public GameBound(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        @SuppressLint("DrawAllocation") Paint paintBound = new Paint();
//        画背景
        paintBound.setColor(Color.argb(boundColor[0], boundColor[1],
                boundColor[2], boundColor[3]));
        canvas.drawRect(left, top, right, bottom, paintBound);
        if (isStart) {
            move(direction);
            if (isBreak()&&length>0) {
                @SuppressLint("DrawAllocation") AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Game Over");
                builder.setPositiveButton("确定", null).show();
                clean();
                isStart = false;
            }
//        定义画蛇笔
            Paint paintSnake = new Paint();
            paintSnake.setColor(Color.argb(snakeColor[0], snakeColor[1], snakeColor[2], snakeColor[3]));
//        画蛇
            for (int i = 0; i < length; i++) {
                canvas.drawRect((800 / mapSize) * (snakeArray[i][1] - 1),
                        (800 / mapSize) * (snakeArray[i][2] - 1),
                        (800 / mapSize) * snakeArray[i][1],
                        (800 / mapSize) * snakeArray[i][2],
                        paintSnake);
            }
//        画食物
            paintSnake.setColor(Color.argb(
                    foodColor[0], foodColor[1],
                    foodColor[2], foodColor[3]
            ));
            canvas.drawRect((800 / mapSize) * (x - 1),
                    (800 / mapSize) * (y - 1),
                    (800 / mapSize) * x,
                    (800 / mapSize) * y,
                    paintSnake);

        } else {

//        定义画蛇笔
            Paint paintSnake = new Paint();
            paintSnake.setColor(Color.argb(snakeColor[0], snakeColor[1], snakeColor[2], snakeColor[3]));
//        画蛇
            for (int i = 0; i < length; i++) {
                canvas.drawRect((800 / mapSize) * (snakeArray[i][1] - 1),
                        (800 / mapSize) * (snakeArray[i][2] - 1),
                        (800 / mapSize) * snakeArray[i][1],
                        (800 / mapSize) * snakeArray[i][2],
                        paintSnake);
            }
//        画食物
            paintSnake.setColor(Color.argb(
                    foodColor[0], foodColor[1],
                    foodColor[2], foodColor[3]
            ));
            canvas.drawRect((800 / mapSize) * (x - 1),
                    (800 / mapSize) * (y - 1),
                    (800 / mapSize) * x,
                    (800 / mapSize) * y,
                    paintSnake);
        }
        //            画边界
        paintBound.setColor(Color.BLACK);
        paintBound.setStrokeWidth(8);
        canvas.drawLine(left, top, right, top, paintBound);
        canvas.drawLine(left, bottom, right, bottom, paintBound);
        canvas.drawLine(right, top, right, bottom, paintBound);
        canvas.drawLine(left, top, left, bottom, paintBound);
        postInvalidateDelayed(time);
    }

}
