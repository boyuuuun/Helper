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

public class CountNumActivity extends AppCompatActivity implements View.OnClickListener, TextToSpeech.OnInitListener {
    private TextView questionText;
    private Button backButton;
    private TextToSpeech tell;
    private String speech;
    private String rightAnswer;
    private int rightImage;
    private int quizCount = 1;
    private Typeface mTypeface;
    private String test;

    ArrayList<ArrayList<String>> quizArray = new ArrayList<>();
    ArrayList<ArrayList<String>> quizAnswerArray = new ArrayList<>();
    ArrayList<ArrayList<Integer>> quizImage = new ArrayList<>();

    String question[][] = {
        {"공사중"},
        {"여행"},
        {"화장실"},
        {"버스터미널"},
        {"횡단보도"},
        {"문화재"},
        {"교통카드"},
        {"생활용품"},
        {"쓰레기통"},
        {"신호등"},
        {"친구"},
        {"손톱깎이"},
        {"설거지"},
        {"학교"},
        {"휴대전화"},
        {"선생님"},
        {"실내화"},
        {"가위바위보"},
        {"미끄럼틀"},
        {"안경"},
        {"부모님"},
        {"파인애플"},
        {"지하철"},
        {"텔레비전"},
        {"지우개"},
        {"초콜릿"},
        {"선물"},
        {"크레파스"},
        {"스케치북"},
        {"와이파이"},
        {"곰"},
        {"발"},
        {"침"},
        {"힘"},
        {"돈"},
        {"숨"},
        {"솜"},
        {"살"},
        {"남"},
        {"잠"},
        {"영"},
        {"알"},
        {"문"},
        {"길"},
        {"실"},
        {"손"},
        {"발"},
        {"약"},
        {"싹"},
        {"껌"},
        {"강"},
        {"형"},
        {"춤"},
        {"밤"},
        {"불"},
        {"벽"},
        {"새"},
        {"뱀"},
        {"빗"},
        {"똥"},};

    String answer[][]={
            {"세 글자"},
            {"두 글자"},
            {"세 글자"},
            {"다섯 글자"},
            {"네 글자"},
            {"세 글자"},
            {"네 글자"},
            {"네 글자"},
            {"네 글자"},
            {"세 글자"},
            {"두 글자"},
            {"네 글자"},
            {"세 글자"},
            {"두 글자"},
            {"네 글자"},
            {"세 글자"},
            {"세 글자"},
            {"다섯 글자"},
            {"네 글자"},
            {"두 글자"},
            {"세 글자"},
            {"네 글자"},
            {"세 글자"},
            {"네 글자"},
            {"세 글자"},
            {"세 글자"},
            {"두 글자"},
            {"네 글자"},
            {"네 글자"},
            {"네 글자"},
            {"한 글자"},
            {"한 글자"},
            {"한 글자"},
            {"한 글자"},
            {"한 글자"},
            {"한 글자"},
            {"한 글자"},
            {"한 글자"},
            {"한 글자"},
            {"한 글자"},
            {"한 글자"},
            {"한 글자"},
            {"한 글자"},
            {"한 글자"},
            {"한 글자"},
            {"한 글자"},
            {"한 글자"},
            {"한 글자"},
            {"한 글자"},
            {"한 글자"},
            {"한 글자"},
            {"한 글자"},
            {"한 글자"},
            {"한 글자"},
            {"한 글자"},
            {"한 글자"},
            {"한 글자"},
            {"한 글자"},
            {"한 글자"},
            {"한 글자"},
    };

    int image[] = {
      R.drawable.construction,
            R.drawable.trip,
            R.drawable.toilet,
            R.drawable.bus_stop,
            R.drawable.crosswalk,
            R.drawable.cultural_heritage,
            R.drawable.traffic_card,
            R.drawable.household_goods,
            R.drawable.trash_can,
            R.drawable.traffic_light,
            R.drawable.friend,
            R.drawable.nail_clippers,
            R.drawable.washing_dishes,
            R.drawable.school,
            R.drawable.cellphone,
            R.drawable.teacher,
            R.drawable.slipper,
            R.drawable.rock_paper_scissors,
            R.drawable.slide,
            R.drawable.glasses,
            R.drawable.parents,
            R.drawable.pineapple,
            R.drawable.subway,
            R.drawable.television,
            R.drawable.eraser,
            R.drawable.chocolate,
            R.drawable.present,
            R.drawable.crayon,
            R.drawable.sketchbook,
            R.drawable.wifi,
            R.drawable.bear,
            R.drawable.feet,
            R.drawable.spit,
            R.drawable.strong,
            R.drawable.money,
            R.drawable.breath,
            R.drawable.cotton,
            R.drawable.skin,
            R.drawable.male,
            R.drawable.sleep,
            R.drawable.zero,
            R.drawable.egg,
            R.drawable.door,
            R.drawable.road,
            R.drawable.thread,
            R.drawable.hand,
            R.drawable.feet,
            R.drawable.pill,
            R.drawable.sprout,
            R.drawable.gum,
            R.drawable.river,
            R.drawable.brother,
            R.drawable.dance,
            R.drawable.night,
            R.drawable.fire,
            R.drawable.wall,
            R.drawable.bird,
            R.drawable.snake,
            R.drawable.hairbrush,
            R.drawable.dung
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_number);
        mTypeface = Typeface.createFromAsset(getAssets(), "NotoSansCJKkr-Regular.otf");
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        setGlobalFont(root);
        backButton = (Button)findViewById(R.id.back);
        backButton.setOnClickListener(this);
        questionText = findViewById(R.id.text);
        tell = new TextToSpeech(this,this);

        Intent intent =  getIntent();
        test = intent.getStringExtra("test");

        for(int i=0; i<question.length; i++){
            ArrayList<String> tmpArray = new ArrayList<>();
            tmpArray.add(question[i][0]); //image name
            ArrayList<String> tmpArray2 = new ArrayList<>();
            tmpArray2.add(answer[i][0]); //right answer
            ArrayList<Integer> tmpArray3 = new ArrayList<>();
            tmpArray3.add(image[i]);

            quizArray.add(tmpArray);
            quizAnswerArray.add(tmpArray2);
            quizImage.add(tmpArray3);
        }

        Random random = new Random();

        int randomNum = random.nextInt(quizArray.size());

        ArrayList<String> quiz = quizArray.get(randomNum);
        ArrayList<String> answer = quizAnswerArray.get(randomNum);
        ArrayList<Integer> image = quizImage.get(randomNum);

        questionText.setText(quiz.get(0));
        rightAnswer = answer.get(0);
        rightImage = image.get(0);

        speech = "글자수를 맞춰보세요.";

        quizArray.remove(randomNum);
        quizAnswerArray.remove(randomNum);
        quizImage.remove(randomNum);
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

    public void showNext(){
        Random random = new Random();

        int randomNum = random.nextInt(quizArray.size());

        ArrayList<String> quiz = quizArray.get(randomNum);
        ArrayList<String> answer = quizAnswerArray.get(randomNum);
        ArrayList<Integer> image = quizImage.get(randomNum);

        questionText.setText(quiz.get(0));
        rightAnswer = answer.get(0);
        rightImage = image.get(0);
        speech = "글자수를 맞춰보세요.";
        tell.setLanguage(Locale.KOREAN);
        tell.setPitch(0.6f);
        tell.setSpeechRate(0.95f);
        tell.speak(speech, TextToSpeech.QUEUE_FLUSH, null);

        quizArray.remove(randomNum);
        quizAnswerArray.remove(randomNum);
        quizImage.remove(randomNum);
    }

    public void showResult(){
        if(test.equals("test")){
            Intent intent = new Intent(getApplicationContext(), ComposeActivity.class);
            startActivityForResult(intent,5000);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("끝났습니다.");
        speech = "끝났습니다.";
        tell.setLanguage(Locale.KOREAN);
        tell.setPitch(0.6f);
        tell.setSpeechRate(0.95f);
        tell.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
        builder.setNegativeButton("종료", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivityForResult(intent,5000);
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
        }
        Button answerBtn = (Button) findViewById(view.getId());

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
                    android.app.AlertDialog.Builder inputBuilder = new android.app.AlertDialog.Builder(CountNumActivity.this);
                    inputBuilder.setTitle("사진으로 보는 정답");

                    final ImageView iv = new ImageView(CountNumActivity.this);
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
}
