package com.example.colour_bomb;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    MediaPlayer mymusic;


    int[] motos = {
            R.drawable.boot,
            R.drawable.ktmhelmet,
            R.drawable.rider,
            R.drawable.suzuki,
            R.drawable.yamaha,
            R.drawable.yamahalogo
    };

    int widthOfBlock, noOfBlocks = 8, widthOfScreen;
    ArrayList<ImageView> moto = new ArrayList<>();
    int motoToBeDragged, motoToBeReplaced;
    int notMoto = R.drawable.transparent;
    Handler mHandler;
    int interval = 100;
    TextView scoreResult;
    int score = 0;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mymusic = MediaPlayer.create(getApplicationContext(),R.raw.motomusik);
        mymusic.start();
        scoreResult = findViewById(R.id.score);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        widthOfScreen = displayMetrics.widthPixels;
        int heightOfScreen = displayMetrics.heightPixels;
        widthOfBlock = widthOfScreen / noOfBlocks;
        createBoard();
        for (final ImageView imageView : moto) {
            imageView.setOnTouchListener(new OnSwipeListener(this) {
                @Override
                void onSwipeLeft() {
                    super.onSwipeLeft();
                    motoToBeDragged = imageView.getId();
                    motoToBeReplaced = motoToBeDragged - 1;
                    motoInterchange();
                }

                @Override
                void onSwipeRight() {
                    super.onSwipeRight();
                    motoToBeDragged = imageView.getId();
                    motoToBeReplaced = motoToBeDragged + 1;
                    motoInterchange();
                }

                @Override
                void onSwipeTop() {
                    super.onSwipeTop();
                    motoToBeDragged = imageView.getId();
                    motoToBeReplaced = motoToBeDragged - noOfBlocks;
                    motoInterchange();
                }

                @Override
                void onSwipeBottom() {
                    super.onSwipeBottom();
                    motoToBeDragged = imageView.getId();
                    motoToBeReplaced = motoToBeDragged + noOfBlocks;
                    motoInterchange();
                }
            });
        }

        mHandler = new Handler();
        startRepeat();

    }

    private void checkRowForThree()
    {
        for (int i = 0; i < 62; i++)
        {
            int chosedMoto = (int) moto.get(i).getTag();
            boolean isBlank = (int) moto.get(i).getTag() == notMoto;
            Integer[] notValid = {6,7,14,15,22,23,30,31,38,39,46,47,54,55};
            List<Integer> list = Arrays.asList(notValid);
            if (!list.contains(i))
            {
                int x = i;
                if ((int) moto.get(x++).getTag() == chosedMoto && !isBlank &&
                        (int) moto.get(x++).getTag() == chosedMoto &&
                        (int) moto.get(x).getTag() == chosedMoto) {
                    score = score + 3;
                    scoreResult.setText(String.valueOf(score));
                    moto.get(x).setImageResource(notMoto);
                    moto.get(x).setTag(notMoto);
                    x--;
                    moto.get(x).setImageResource(notMoto);
                    moto.get(x).setTag(notMoto);
                    x--;
                    moto.get(x).setImageResource(notMoto);
                    moto.get(x).setTag(notMoto);

                }
            }
        }
        moveDownMotos();
    }

    private void checkColumnForThree() {
        for (int i = 0; i < 47; i++) {
            int chosedMoto = (int) moto.get(i).getTag();
            boolean isBlank = (int) moto.get(i).getTag() == notMoto;
                int x = i;
                if ((int) moto.get(x).getTag() == chosedMoto && !isBlank &&
                        (int) moto.get(x+noOfBlocks).getTag() == chosedMoto &&
                        (int) moto.get(x+2*noOfBlocks).getTag() == chosedMoto) {
                    score = score + 3;
                    scoreResult.setText(String.valueOf(score));
                    moto.get(x).setImageResource(notMoto);
                    moto.get(x).setTag(notMoto);
                    x = x+ noOfBlocks;
                    moto.get(x).setImageResource(notMoto);
                    moto.get(x).setTag(notMoto);
                    x = x+ noOfBlocks;
                    moto.get(x).setImageResource(notMoto);
                    moto.get(x).setTag(notMoto);
                }
        }

        moveDownMotos();
    }

    private void moveDownMotos()
    {
        Integer[] firstRow = {0, 1, 2, 3, 4, 5, 6, 7};
        List<Integer> list = Arrays.asList(firstRow);
        for (int i=55; i>=0; i--)
        {
            if ((int) moto.get(i + noOfBlocks).getTag() == notMoto)
            {
                moto.get(i + noOfBlocks).setImageResource((int) moto.get(i).getTag());
                moto.get(i + noOfBlocks).setTag(moto.get(i).getTag());
                moto.get(i).setImageResource(notMoto);
                moto.get(i).setTag(notMoto);


                if (list.contains(i) && (int) moto.get(i).getTag() == notMoto)
                {
                    int randomColor = (int) Math.floor(Math.random() * motos.length);
                    moto.get(i).setImageResource(motos[randomColor]);
                    moto.get(i).setTag(motos[randomColor]);
                }
            }
        }
        for (int i=0; i < 8; i++)
        {
            if ((int) moto.get(i).getTag() == notMoto)
            {
                int randomColor = (int) Math.floor(Math.random() * motos.length);
                moto.get(i).setImageResource(motos[randomColor]);
                moto.get(i).setTag(motos[randomColor]);
            }
        }
    }

    Runnable repeatChecker = new Runnable() {
        @Override
        public void run() {
            try {
                checkRowForThree();
                checkColumnForThree();
                moveDownMotos();
            }
            finally {
                mHandler.postDelayed(repeatChecker, interval);
            }
        }
    };


    void startRepeat()
    {
        repeatChecker.run();
    }

    private void motoInterchange()
    {
        int background = (int) moto.get(motoToBeReplaced).getTag();
        int background1 = (int) moto.get(motoToBeDragged).getTag();
        moto.get(motoToBeDragged).setImageResource(background);
        moto.get(motoToBeReplaced).setImageResource(background1);
        moto.get(motoToBeDragged).setTag(background);
        moto.get(motoToBeReplaced).setTag(background1);
    }
    private void createBoard() {
        GridLayout gridLayout = findViewById(R.id.board);
        gridLayout.setRowCount(noOfBlocks);
        gridLayout.setColumnCount(noOfBlocks);
        //square//
        gridLayout.getLayoutParams().width = widthOfScreen;
        gridLayout.getLayoutParams().height = widthOfScreen;
        for (int i=0; i< noOfBlocks * noOfBlocks; i++)
        {
            ImageView imageView = new ImageView(this);
            imageView.setId(i);
            imageView.setLayoutParams(new
                    android.view.ViewGroup.LayoutParams(widthOfBlock, widthOfBlock));
            imageView.setMaxHeight(widthOfBlock);
            imageView.setMaxWidth(widthOfBlock);
            int randomMoto = (int) Math.floor(Math.random() * motos.length); //generira random index
            //from stars array
            imageView.setImageResource(motos[randomMoto]);
            imageView.setTag(motos[randomMoto]);
            moto.add(imageView);
            gridLayout.addView(imageView);

        }

    }

    protected void onPause()
    {
        super.onPause();
        mymusic.release();
    }
}
