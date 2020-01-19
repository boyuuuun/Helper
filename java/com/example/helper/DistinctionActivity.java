package com.example.helper;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class DistinctionActivity extends AppCompatActivity implements View.OnClickListener, TextToSpeech.OnInitListener {
    private TextView questionText;
    private TextView text, text2, text3;
    private Button backButton;
    private Button one, two, three;
    private TextToSpeech tell;
    private String speech;
    private String rightAnswer;
    private int rightImage;
    private int quizCount = 1;
    private Typeface mTypeface;
    private String test;

    ArrayList<ArrayList<String>> quizArray = new ArrayList<>();
    ArrayList<ArrayList<String>> quiz2Array = new ArrayList<>();
    ArrayList<ArrayList<String>> quiz3Array = new ArrayList<>();
    ArrayList<ArrayList<String>> quizAnswerArray = new ArrayList<>();
    ArrayList<ArrayList<String>> quizText = new ArrayList<>();
    ArrayList<ArrayList<Integer>> quizImage = new ArrayList<>();

    String question[][] = {
            {"사자","바다","자라"},
            {"바나나","나비","다리미"},
            {"라면","마음","아이스크림"},
            {"미역","치아","비누"},
            {"리본","지리","기린"},
            {"이성","시소","닌자"},
            {"안경","산초","잔소리"},
            {"선생님","언어","천막"},
            {"보리","오리","고리"},
            {"구역","무역","수영"},
            {"거미","터널","커피"},
            {"숙면","국기","죽"},
            {"연어","면허증","변호사"},
            {"햄스터","냄비","캠프"},
            {"운명","준비물","훈장"},
            {"인사","빈대","민물"},
            {"실내화","일개미","필수품"},
            {"파이프","차이","가위"},
            {"초콜릿","코스모스","토마토"},
            {"골뱅이","솔방울","롤러스케이트"},
    };

    String answer[][]={
            {"사자"},
            {"나비"},
            {"마음"},
            {"비누"},
            {"리본"},
            {"이성"},
            {"잔소리"},
            {"천막"},
            {"보리"},
            {"무역"},
            {"커피"},
            {"숙면"},
            {"변호사"},
            {"햄스터"},
            {"운명"},
            {"민물"},
            {"필수품"},
            {"차이"},
            {"토마토"},
            {"롤러스케이트"},
    };

    String setence[][] = {
            {"'사'자로 시작하는 단어를 맞춰보세요."},
            {"'나'자로 시작하는 단어를 맞춰보세요."},
            {"'마'자로 시작하는 단어를 맞춰보세요."},
            {"'비'자로 시작하는 단어를 맞춰보세요."},
            {"'리'자로 시작하는 단어를 맞춰보세요."},
            {"'이'자로 시작하는 단어를 맞춰보세요."},
            {"'잔'자로 시작하는 단어를 맞춰보세요."},
            {"'천'자로 시작하는 단어를 맞춰보세요."},
            {"'보'자로 시작하는 단어를 맞춰보세요."},
            {"'무'자로 시작하는 단어를 맞춰보세요."},
            {"'커'자로 시작하는 단어를 맞춰보세요."},
            {"'숙'자로 시작하는 단어를 맞춰보세요."},
            {"'변'자로 시작하는 단어를 맞춰보세요."},
            {"'햄'자로 시작하는 단어를 맞춰보세요."},
            {"'운'자로 시작하는 단어를 맞춰보세요."},
            {"'민'자로 시작하는 단어를 맞춰보세요."},
            {"'필'자로 시작하는 단어를 맞춰보세요."},
            {"'차'자로 시작하는 단어를 맞춰보세요."},
            {"'토'자로 시작하는 단어를 맞춰보세요."},
            {"'롤'자로 시작하는 단어를 맞춰보세요."},
    };

    int image[] = {
            R.drawable.lion,
            R.drawable.butterfly,
            R.drawable.mind,
            R.drawable.soap,
            R.drawable.ribbon,
            R.drawable.couple,
            R.drawable.talk,
            R.drawable.tent,
            R.drawable.barley,
            R.drawable.trade,
            R.drawable.coffee,
            R.drawable.sleep,
            R.drawable.lawyer,
            R.drawable.hamster,
            R.drawable.destiny,
            R.drawable.river,
            R.drawable.household_goods,
            R.drawable.difference,
            R.drawable.tomato,
            R.drawable.roller_skates
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distinction);
        mTypeface = Typeface.createFromAsset(getAssets(), "NotoSansCJKkr-Regular.otf");
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        setGlobalFont(root);
        backButton = (Button)findViewById(R.id.back);
        backButton.setOnClickListener(this);
        questionText = findViewById(R.id.sentence);
        text = findViewById(R.id.text);
        text2 = findViewById(R.id.text2);
        text3 = findViewById(R.id.text3);
        one = findViewById(R.id.one);
        two = findViewById(R.id.two);
        three = findViewById(R.id.three);
        tell = new TextToSpeech(this,this);

        Intent intent =  getIntent();
        test = intent.getStringExtra("test");

        for(int i=0; i<question.length; i++){
            ArrayList<String> tmpArray = new ArrayList<>();
            tmpArray.add(question[i][0]);
            ArrayList<String> tmpArray2 = new ArrayList<>();
            tmpArray2.add(question[i][1]);
            ArrayList<String> tmpArray3 = new ArrayList<>();
            tmpArray3.add(question[i][2]);
            ArrayList<String> tmpArray4 = new ArrayList<>();
            tmpArray4.add(answer[i][0]);
            ArrayList<String> tmpArray5 = new ArrayList<>();
            tmpArray5.add(setence[i][0]);
            ArrayList<Integer> tmpArray6 = new ArrayList<>();
            tmpArray6.add(image[i]);

            quizArray.add(tmpArray);
            quiz2Array.add(tmpArray2);
            quiz3Array.add(tmpArray3);
            quizAnswerArray.add(tmpArray4);
            quizText.add(tmpArray5);
            quizImage.add(tmpArray6);
        }

        Random random = new Random();

        int randomNum = random.nextInt(quizArray.size());

        ArrayList<String> quiz1 = quizArray.get(randomNum);
        ArrayList<String> quiz2 = quiz2Array.get(randomNum);
        ArrayList<String> quiz3 = quiz3Array.get(randomNum);
        ArrayList<String> answer = quizAnswerArray.get(randomNum);
        ArrayList<String> speak = quizText.get(randomNum);
        ArrayList<Integer> image = quizImage.get(randomNum);

        text.setText(quiz1.get(0));
        text2.setText(quiz2.get(0));
        text3.setText(quiz3.get(0));
        one.setText(quiz1.get(0));
        two.setText(quiz2.get(0));
        three.setText(quiz3.get(0));
        questionText.setText(speak.get(0));
        rightAnswer = answer.get(0);
        rightImage = image.get(0);

        speech = speak.get(0);

        quizArray.remove(randomNum);
        quiz2Array.remove(randomNum);
        quiz3Array.remove(randomNum);
        quizAnswerArray.remove(randomNum);
        quizText.remove(randomNum);
        quizImage.remove(randomNum);
    }

    public void showNext(){
        Random random = new Random();

        int randomNum = random.nextInt(quizArray.size());

        ArrayList<String> quiz1 = quizArray.get(randomNum);
        ArrayList<String> quiz2 = quiz2Array.get(randomNum);
        ArrayList<String> quiz3 = quiz3Array.get(randomNum);
        ArrayList<String> answer = quizAnswerArray.get(randomNum);
        ArrayList<String> speak = quizText.get(randomNum);
        ArrayList<Integer> image = quizImage.get(randomNum);

        text.setText(quiz1.get(0));
        text2.setText(quiz2.get(0));
        text3.setText(quiz3.get(0));
        one.setText(quiz1.get(0));
        two.setText(quiz2.get(0));
        three.setText(quiz3.get(0));
        questionText.setText(speak.get(0));
        rightAnswer = answer.get(0);
        rightImage = image.get(0);

        speech = speak.get(0);
        tell.setLanguage(Locale.KOREAN);
        tell.setPitch(0.6f);
        tell.setSpeechRate(0.95f);
        tell.speak(speech, TextToSpeech.QUEUE_FLUSH, null);

        quizArray.remove(randomNum);
        quiz2Array.remove(randomNum);
        quiz3Array.remove(randomNum);
        quizAnswerArray.remove(randomNum);
        quizText.remove(randomNum);
        quizImage.remove(randomNum);
    }

    public void showResult(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("문제를 모두 푸셨네요!");

        speech = "잘 하셨어요! 문제를 다 푸셨습니다. ";
        tell.setLanguage(Locale.KOREAN);
        tell.setPitch(0.6f);
        tell.setSpeechRate(0.95f);
        tell.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
        builder.setNegativeButton("종료", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivityForResult(intent, 5000);
            }
        });
        builder.show();
    }

    @Override
    public void onInit(int i) {
        tell.setLanguage(Locale.KOREAN);
        tell.setPitch(0.6f);
        tell.setSpeechRate(0.95f);
        tell.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.back:
                onBackPressed();
                return;
        }Button answerBtn = (Button) findViewById(view.getId());

        if(answerBtn.getText().equals(rightAnswer)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            speech = "정답입니다!";
            tell.setLanguage(Locale.KOREAN);
            tell.setPitch(0.6f);
            tell.setSpeechRate(0.95f);
            tell.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
            builder.setTitle("정답입니다!");
            builder.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // 정답 사진 다이어로그 띄우기
                    android.app.AlertDialog.Builder inputBuilder = new android.app.AlertDialog.Builder(DistinctionActivity.this);
                    inputBuilder.setTitle("사진으로 보는 정답");

                    final ImageView iv = new ImageView(DistinctionActivity.this);
                    iv.setImageResource(rightImage); // 사진 소스는 여기!!!!
                    inputBuilder.setView(iv);

                    inputBuilder.setPositiveButton("다음으로 넘어가기", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(quizArray.size()<1){
                                //quizArray is empty.
                                showResult();
                            }
                            else if(quizCount==5){
                                showResult();
                            }
                            else{
                                quizCount++;
                                showNext();
                            }
                        }
                    });

                    inputBuilder.show();
                }
            });
            builder.show();
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("다시 생각해보세요.");
            speech = "다시 생각해보세요.";
            tell.setLanguage(Locale.KOREAN);
            tell.setPitch(0.6f);
            tell.setSpeechRate(0.95f);
            tell.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
            builder.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(test.equals("test")){
                        if(quizCount==5){
                            showResult();
                        }
                        else{
                            quizCount++;
                            showNext();
                        }
                    }
                }
            });
            builder.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tell.shutdown();
    }

    private void setGlobalFont(ViewGroup root) {
        for (int i = 0; i < root.getChildCount(); i++) {
            View child = root.getChildAt(i);
            if (child instanceof TextView)
                ((TextView)child).setTypeface(mTypeface);
            else if (child instanceof ViewGroup)
                setGlobalFont((ViewGroup)child);
        }
    }
}