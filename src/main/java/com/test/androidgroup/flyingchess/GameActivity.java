package com.test.androidgroup.flyingchess;


import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;



import com.test.androidgroup.flyingchess.Resource.*;


public class GameActivity extends Activity implements View.OnTouchListener{

    private TextView title;//标题上的字
    private LinearLayout game_layout;
    private ImageButton sound_btn;
    private ImageView chess_color;
    private RelativeLayout game_information;
    int[] colorToPic = new int[]{
            R.mipmap.f_green,
            R.mipmap.f_red,
            R.mipmap.f_blue,
            R.mipmap.f_orange,
    };
    private ImageView dice;
    int[] dicePic = new int[]{
            R.mipmap.dice1,
            R.mipmap.dice2,
            R.mipmap.dice3,
            R.mipmap.dice4,
            R.mipmap.dice5,
            R.mipmap.dice6,
    };
    boolean mute = false;//静音
    boolean canMove = false;//点击棋子后是否可移动
    public AnimationDrawable animationDrawable = null;
    int diceResult;//摇骰子结果
    float[] chesslocationX = new float[96];
    float[] chesslocationY = new float[96];
    int[] chesslocationID = new int[16];
    Player[] players = new Player[4];
    GameManager gameManager;


    //棋盘相关
    private RelativeLayout layoutBoard;
    private ImageView ivBoard;
    private int boardWidth; //棋盘宽度（in pixel）
    private int boardHeight; //棋盘高度（in pixel）
    private int curY; //棋子当前位置 （in pixel）
    private int curX;
    private int nextY; //棋子下一步位置 （in pixel）
    private int nextX;
    private float halfChessPercentage;
//    private double widthPercentage; //位置点与棋盘宽度的比例
//    private double heightPercentage;

    private ImageView chessGreen1;//16个棋子
    private ImageView chessGreen2;
    private ImageView chessGreen3;
    private ImageView chessGreen4;
    private ImageView chessRed1;
    private ImageView chessRed2;
    private ImageView chessRed3;
    private ImageView chessRed4;
    private ImageView chessBlue1;
    private ImageView chessBlue2;
    private ImageView chessBlue3;
    private ImageView chessBlue4;
    private ImageView chessOrange1;
    private ImageView chessOrange2;
    private ImageView chessOrange3;
    private ImageView chessOrange4;

//    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams
//                (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    private int mode;
    private int player_count = 3;

    @Override
    public void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);
        Intent intent = getIntent();
        mode = intent.getIntExtra("mode", -1);//0为房间，1为wifi，2为单机，3为蓝牙，-1为异常
        player_count = intent.getIntExtra("playerNumber",-1);
        init();
        getLocationFromRaw();
        initChessBoard();

    }


    void init(){
        game_layout = (LinearLayout) findViewById(R.id.game_layout);

        title = (TextView) findViewById(R.id.title);
        title.setText("游戏中");

        //测试，默认有4个玩家
        Vector<Chess> chessSetRed = new Vector<>();
        Vector<Chess> chessSetGreen = new Vector<>();
        chessSetGreen.add(new Chess(Color.Green,null,R.id.chess_green1));
        chessSetGreen.add(new Chess(Color.Green,null,R.id.chess_green2));
        chessSetGreen.add(new Chess(Color.Green,null,R.id.chess_green3));
        chessSetGreen.add(new Chess(Color.Green,null,R.id.chess_green4));
        players[0] = new Player(Color.Green,chessSetGreen,false);
        chessSetRed.add(new Chess(Color.Red,null,R.id.chess_red1));
        chessSetRed.add(new Chess(Color.Red,null,R.id.chess_red2));
        chessSetRed.add(new Chess(Color.Red,null,R.id.chess_red3));
        chessSetRed.add(new Chess(Color.Red,null,R.id.chess_red4));
        players[1] = new Player(Color.Red,chessSetRed,true);
        Vector<Chess> chessSetBlue = new Vector<>();
        chessSetBlue.add(new Chess(Color.Blue,null,R.id.chess_blue1));
        chessSetBlue.add(new Chess(Color.Blue,null,R.id.chess_blue2));
        chessSetBlue.add(new Chess(Color.Blue,null,R.id.chess_blue3));
        chessSetBlue.add(new Chess(Color.Blue,null,R.id.chess_blue4));
        players[2] = new Player(Color.Blue,chessSetBlue,true);
        Vector<Chess> chessSetOrange = new Vector<>();
        chessSetOrange.add(new Chess(Color.Orange,null,R.id.chess_orange1));
        chessSetOrange.add(new Chess(Color.Orange,null,R.id.chess_orange2));
        chessSetOrange.add(new Chess(Color.Orange,null,R.id.chess_orange3));
        chessSetOrange.add(new Chess(Color.Orange,null,R.id.chess_orange4));
        players[3] = new Player(Color.Orange,chessSetOrange,true);

        if (mode == 2) {
            gameManager = new GameManager(players[0],this, false);
        } else {
            gameManager = new GameManager(players[0],this, true);
        }

        for(int i = 0; i <= player_count - 1; ++i ) {
            try {
                gameManager.addPlayer(players[i]);
            }
            catch (Exception e){}
        }
        try {
            gameManager.init(9);
        }
        catch(Exception e){};

        chess_color = (ImageView)findViewById(R.id.f_color);

        sound_btn = (ImageButton) findViewById(R.id.sound_btn);
        sound_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mute = closeVoice();切换当前音效状态（静音/非静音），并返回新状态（true/false)
                if (mute) {
                    sound_btn.setBackgroundResource(R.mipmap.sound_btn);
                    mute = false;
                } else {
                    sound_btn.setBackgroundResource(R.mipmap.sound_btn_ban);
                    mute = true;
                }
            }
        });

        game_information = (RelativeLayout)findViewById(R.id.game_information);
        switch (players[0].getColor().ordinal()){
            case 0:game_information.setBackgroundResource(R.color.f_green);break;
            case 1:game_information.setBackgroundResource(R.color.f_red);break;
            case 2:game_information.setBackgroundResource(R.color.f_blue);break;
            case 3:game_information.setBackgroundResource(R.color.f_orange);break;
            default:game_information.setBackgroundResource(R.color.f_green);break;
        }


        dice = (ImageView) findViewById(R.id.dice);
        animationDrawable = (AnimationDrawable) dice.getDrawable();
        animationDrawable.stop();

        dice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diceResult = gameManager.rollDice();

                if (diceResult != -1) {
                    startRollDiceAnimation();
                    /*diceCount = new DiceloopCount(1000, 1000);
                    diceCount.start();*/
                }
            }
        });
    }

    void initChessBoard(){
        layoutBoard = (RelativeLayout) findViewById(R.id.layout_board);
        ivBoard = (ImageView) findViewById(R.id.iv_board);
        ivBoard.getLayoutParams().width = getWindowManager().getDefaultDisplay().getWidth();
        ivBoard.getLayoutParams().height = getWindowManager().getDefaultDisplay().getWidth();



        ViewTreeObserver viewTreeObserver = ivBoard.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ivBoard.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                //当棋盘ImageView完成measure之后才能getWidth getHeight
                boardWidth = ivBoard.getWidth();
                boardHeight = ivBoard.getHeight();

                //boardWidth*(43/2)/809：按照棋子与棋盘的比例大小计算得到的半个棋子的缩放后的相对像素数（因为不同屏幕像素的缩放不同）：
                // 因为getWidth getHeight得到的是棋子的左上角位置
                //43:棋子像素数，809：棋盘像素数（同一套图片下）
                //halfChessPercentage:半个棋子相对于棋盘的比例
                halfChessPercentage = (float) (((float) boardWidth) * (43.0 / 2.0) / 809.0 / ((float) boardWidth));

                chessGreen1 = new ImageView(GameActivity.this);
                chessGreen1.setImageResource(R.drawable.f_green);
                chessGreen1.setId(R.id.chess_green1);
                chessGreen1.setOnTouchListener(GameActivity.this);
                chesslocationID[0] = 80;
                addChess(chessGreen1, chesslocationID[0]);

                chessGreen2 = new ImageView(GameActivity.this);
                chessGreen2.setImageResource(R.drawable.f_green);
                chessGreen2.setId(R.id.chess_green2);
                chessGreen2.setOnTouchListener(GameActivity.this);
                chesslocationID[1] = 84;
                addChess(chessGreen2, chesslocationID[1]);

                chessGreen3 = new ImageView(GameActivity.this);
                chessGreen3.setImageResource(R.drawable.f_green);
                chessGreen3.setId(R.id.chess_green3);
                chessGreen3.setOnTouchListener(GameActivity.this);
                chesslocationID[2] = 88;
                addChess(chessGreen3, chesslocationID[2]);

                chessGreen4 = new ImageView(GameActivity.this);
                chessGreen4.setImageResource(R.drawable.f_green);
                chessGreen4.setId(R.id.chess_green4);
                chessGreen4.setOnTouchListener(GameActivity.this);
                chesslocationID[3] = 92;
                addChess(chessGreen4, chesslocationID[3]);

                chessRed1 = new ImageView(GameActivity.this);
                chessRed1.setImageResource(R.drawable.f_red);
                chessRed1.setId(R.id.chess_red1);
                chessRed1.setOnTouchListener(GameActivity.this);
                chesslocationID[4] = 81;
                addChess(chessRed1, chesslocationID[4]);

                chessRed2 = new ImageView(GameActivity.this);
                chessRed2.setImageResource(R.drawable.f_red);
                chessRed2.setId(R.id.chess_red2);
                chessRed2.setOnTouchListener(GameActivity.this);
                chesslocationID[5] = 85;
                addChess(chessRed2, chesslocationID[5]);

                chessRed3 = new ImageView(GameActivity.this);
                chessRed3.setImageResource(R.drawable.f_red);
                chessRed3.setId(R.id.chess_red3);
                chessRed3.setOnTouchListener(GameActivity.this);
                chesslocationID[6] = 89;
                addChess(chessRed3, chesslocationID[6]);

                chessRed4 = new ImageView(GameActivity.this);
                chessRed4.setImageResource(R.drawable.f_red);
                chessRed4.setId(R.id.chess_red4);
                chessRed4.setOnTouchListener(GameActivity.this);
                chesslocationID[7] = 93;
                addChess(chessRed4, chesslocationID[7]);

                if(player_count >= 3) {
                    chessBlue1 = new ImageView(GameActivity.this);
                    chessBlue1.setImageResource(R.drawable.f_blue);
                    chessBlue1.setId(R.id.chess_blue1);
                    chessBlue1.setOnTouchListener(GameActivity.this);
                    chesslocationID[8] = 82;
                    addChess(chessBlue1, chesslocationID[8]);

                    chessBlue2 = new ImageView(GameActivity.this);
                    chessBlue2.setImageResource(R.drawable.f_blue);
                    chessBlue2.setId(R.id.chess_blue2);
                    chessBlue2.setOnTouchListener(GameActivity.this);
                    chesslocationID[9] = 86;
                    addChess(chessBlue2, chesslocationID[9]);

                    chessBlue3 = new ImageView(GameActivity.this);
                    chessBlue3.setImageResource(R.drawable.f_blue);
                    chessBlue3.setId(R.id.chess_blue3);
                    chessBlue3.setOnTouchListener(GameActivity.this);
                    chesslocationID[10] = 90;
                    addChess(chessBlue3, chesslocationID[10]);

                    chessBlue4 = new ImageView(GameActivity.this);
                    chessBlue4.setImageResource(R.drawable.f_blue);
                    chessBlue4.setId(R.id.chess_blue4);
                    chessBlue4.setOnTouchListener(GameActivity.this);
                    chesslocationID[11] = 94;
                    addChess(chessBlue4, chesslocationID[11]);
                }

                if(player_count >= 4) {
                    chessOrange1 = new ImageView(GameActivity.this);
                    chessOrange1.setImageResource(R.drawable.f_orange);
                    chessOrange1.setId(R.id.chess_orange1);
                    chessOrange1.setOnTouchListener(GameActivity.this);
                    chesslocationID[12] = 83;
                    addChess(chessOrange1, chesslocationID[12]);

                    chessOrange2 = new ImageView(GameActivity.this);
                    chessOrange2.setImageResource(R.drawable.f_orange);
                    chessOrange2.setId(R.id.chess_orange2);
                    chessOrange2.setOnTouchListener(GameActivity.this);
                    chesslocationID[13] = 87;
                    addChess(chessOrange2, chesslocationID[13]);

                    chessOrange3 = new ImageView(GameActivity.this);
                    chessOrange3.setImageResource(R.drawable.f_orange);
                    chessOrange3.setId(R.id.chess_orange3);
                    chessOrange3.setOnTouchListener(GameActivity.this);
                    chesslocationID[14] = 91;
                    addChess(chessOrange3, chesslocationID[14]);

                    chessOrange4 = new ImageView(GameActivity.this);
                    chessOrange4.setImageResource(R.drawable.f_orange);
                    chessOrange4.setId(R.id.chess_orange4);
                    chessOrange4.setOnTouchListener(GameActivity.this);
                    chesslocationID[15] = 95;
                    addChess(chessOrange4, chesslocationID[15]);
                }

//
//

            }
        });
    }

    void addChess(ImageView chess,int currentID){

        curX = (int) ((( boardHeight) * ((float)chesslocationX[currentID] - halfChessPercentage))+0.5);
        curY = (int) ((( boardWidth) * ((float)chesslocationY[currentID] - halfChessPercentage))+0.5);

        //棋盘上一个格子的直径为20，整个棋盘的大小为380*380

        /*RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);*/

        int boardSize = getWindowManager().getDefaultDisplay().getWidth();
        int chessSize = boardSize * 20 / 380;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams
                (chessSize,chessSize);

        layoutParams.topMargin = curY;
        layoutParams.leftMargin = curX;
        chess.setLayoutParams(layoutParams);
        layoutBoard.addView(chess);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP){
            return false;
        }
        //判断当前是否为可移动状态
        if(!canMove) return false;
        canMove = false;
        int id = v.getId();

        gameManager.findTheMoveChessAndMove(id);
        return true;
    }

    void setRollResult(int rollResult){
        dice.setImageResource(dicePic[rollResult - 1]);
        //animationDrawable.stop();
    }

    void setRollReady(){
        dice.setImageResource(R.mipmap.dice_roll);
    }

    void setF_color(int playerIndex){
        chess_color.setImageResource(colorToPic[players[playerIndex].getColor().ordinal()]);
    }


    void startRollDiceAnimation(){
        dice.setImageResource(R.drawable.loopdice);
        animationDrawable = (AnimationDrawable) dice.getDrawable();
        animationDrawable.start();
    }

    void moveToNext(int chessID,int currentID,int nextID){

        ImageView chess = (ImageView)findViewById(chessID);
        int currentX = (int) ((( boardHeight) * ((float)chesslocationX[currentID] - halfChessPercentage))+0.5);
        int currentY = (int) ((( boardHeight) * ((float)chesslocationY[currentID] - halfChessPercentage))+0.5);

        int nextX = (int) ((( boardHeight) * ((float)chesslocationX[nextID] - halfChessPercentage))+0.5);
        int nextY = (int) ((( boardWidth) * ((float)chesslocationY[nextID] - halfChessPercentage))+0.5);

        ObjectAnimator oa1=ObjectAnimator.ofFloat(chess, "x", currentX, nextX);
        oa1.setDuration(1000);
        oa1.start();

        ObjectAnimator oa2=ObjectAnimator.ofFloat(chess, "y", currentY, nextY);
        oa2.setDuration(1000);
        oa2.start();
    }

    public void getLocationFromRaw(){

        String result = "";
        try{
            InputStream in = getResources().openRawResource(R.raw.chesslocation);
            InputStreamReader reader = new InputStreamReader(in);
            BufferedReader bufferedReader = new  BufferedReader(reader);
            StringBuffer string_buffer = new StringBuffer("");
            String str;
            while((str = bufferedReader.readLine()) != null){
                string_buffer.append(str);
                string_buffer.append(" ");
            }
            result = string_buffer.toString();
            in.close();
        } catch (Exception e){
            e.printStackTrace();
        }

        String[] rawdata = result.split(" ");

        for(int i = 0; i < 96; ++i){
            chesslocationX[i] = Float.parseFloat(rawdata[i*3+1]);
            chesslocationY[i] = Float.parseFloat(rawdata[i*3+2]);
        }
    }

}
