package com.example.helper;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.RecognizerIntent;
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

public class AdditionActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{

    private class ComposeQuiz {
        private String answer, word1, word2, word3;
        int picture;

        public ComposeQuiz(String a, String w1, String w2, String w3, int p) {
            answer = a;
            word1 = w1;
            word2 = w2;
            word3 = w3;
            picture = p;
        }

        public String getAnswer() { return answer; }
        public String getWord1() { return word1; }
        public String getWord2() { return word2; }
        public String getWord3() { return word3; }
        public int getPicture() { return picture; }
    }

    private Typeface mTypeface;
    private TextToSpeech tell;
    private String speech;
    private TextView tv_w1, tv_w2, tv_w3, tv_result;
    private Button speak;
    private ComposeQuiz quizzes[] = {new ComposeQuiz("곰", "ㄱ", "ㅗ", "ㅁ", R.drawable.bear), new ComposeQuiz("침", "ㅊ", "ㅣ", "ㅁ", R.drawable.spit),
            new ComposeQuiz("힘", "ㅎ", "ㅣ", "ㅁ", R.drawable.strong), new ComposeQuiz("돈", "ㄷ", "ㅗ", "ㄴ", R.drawable.money),
            new ComposeQuiz("숨", "ㅅ", "ㅜ", "ㅁ", R.drawable.breath), new ComposeQuiz("솜", "ㅅ", "ㅗ", "ㅁ", R.drawable.cotton),
            new ComposeQuiz("금", "ㄱ", "ㅡ", "ㅁ", R.drawable.gold), new ComposeQuiz("남", "ㄴ", "ㅏ", "ㅁ", R.drawable.male),
            new ComposeQuiz("잠", "ㅈ", "ㅏ", "ㅁ", R.drawable.sleep), new ComposeQuiz("연", "ㅇ", "ㅕ", "ㄴ", R.drawable.kite),
            new ComposeQuiz("알", "ㅇ", "ㅏ", "ㄹ", R.drawable.egg), new ComposeQuiz("문", "ㅁ", "ㅜ", "ㄴ", R.drawable.door),
            new ComposeQuiz("길", "ㄱ", "ㅣ", "ㄹ", R.drawable.road), new ComposeQuiz("실", "ㅅ", "ㅣ", "ㄹ", R.drawable.thread),
            new ComposeQuiz("손", "ㅅ", "ㅗ", "ㄴ", R.drawable.hand), new ComposeQuiz("발", "ㅂ", "ㅏ", "ㄹ", R.drawable.feet),
            new ComposeQuiz("약", "ㅇ", "ㅑ", "ㄱ", R.drawable.pill), new ComposeQuiz("싹", "ㅆ", "ㅏ", "ㄱ", R.drawable.sprout),
            new ComposeQuiz("껌", "ㄲ", "ㅓ", "ㅁ", R.drawable.gum), new ComposeQuiz("감", "ㄱ", "ㅏ", "ㅁ", R.drawable.persimmon),
            new ComposeQuiz("형", "ㅎ", "ㅕ", "ㅇ", R.drawable.brother),  new ComposeQuiz("춤", "ㅊ", "ㅜ", "ㅁ", R.drawable.dance),
            new ComposeQuiz("밤", "ㅂ", "ㅏ", "ㅁ", R.drawable.night), new ComposeQuiz("불", "ㅂ", "ㅜ", "ㄹ", R.drawable.fire),
            new ComposeQuiz("벽", "ㅂ", "ㅕ", "ㄱ", R.drawable.wall), new ComposeQuiz("색", "ㅅ", "ㅐ", "ㄱ", R.drawable.color),
            new ComposeQuiz("뱀", "ㅂ", "ㅐ", "ㅁ", R.drawable.snake), new ComposeQuiz("빛", "ㅂ", "ㅣ", "ㅊ", R.drawable.light_bulb),
            new ComposeQuiz("똥", "ㄸ", "ㅗ", "ㅇ", R.drawable.dung)};
    private int maxQuizSize = quizzes.length;
    private int r_quiz[]; // 랜덤으로 뽑은 출제될 문제
    private int numberOfQuestions = 5; // 출제될 문제 수
    private int quizCount = 0;
    private String test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addition);
        mTypeface = Typeface.createFromAsset(getAssets(), "NotoSansCJKkr-Regular.otf");
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        setGlobalFont(root);

        tv_w1 = (TextView) findViewById(R.id.word1);
        tv_w2 = (TextView) findViewById(R.id.word2);
        tv_w3 = (TextView) findViewById(R.id.word3);
        tv_result = (TextView) findViewById(R.id.result);
        speak = (Button) findViewById(R.id.speak);
        r_quiz = new int[numberOfQuestions];
        tell = new TextToSpeech(this, this);

        Intent intent =  getIntent();
        test = intent.getStringExtra("test");

        // 출제할 문제 랜덤으로 뽑음
        for(int i=0; i<numberOfQuestions; i++) {
            r_quiz[i] = (int)(Math.random()*maxQuizSize);
            // 중복 제거
            for(int j=0; j<i; j++) {
                if(r_quiz[i]==r_quiz[j]) {
                    i--;
                    break;
                }
            }
        }

        // 문제 출제
        int r = r_quiz[quizCount];
        tv_w1.setText(quizzes[r].getWord1());
        tv_w2.setText(quizzes[r].getWord2());
        tv_w3.setText(quizzes[r].getWord3());
        tv_result.setText("내가 한 말");
        speech = "세 음소를 합쳐서 말해보세요. 정답을 말할 때는 마이크 버튼을 누르고 말하면 됩니다!";

        // 버튼 클릭시 stt 호출
        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voice();
            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    // 음성 듣기
    public void voice() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "말하세요");
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 20);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        startActivityForResult(intent, 1);
    }

    // 음성 인식 결과 출력
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1 && resultCode==RESULT_OK) {
            ArrayList<String> r = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String res = r.get(0);
            tv_result.setText(res); // 인식된 단어들 중 첫 번째 단어를 tv_result로.

            ComposeQuiz cq = quizzes[r_quiz[quizCount]];
            // 정답일 경우
            if(res.equals(cq.getAnswer())) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle("정답입니다!");
                speech = "정답입니다!";
                tell.setLanguage(Locale.KOREAN);
                tell.setPitch(0.6f);
                tell.setSpeechRate(0.95f);
                tell.speak(speech, TextToSpeech.QUEUE_FLUSH, null);

                builder.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ComposeQuiz cq = quizzes[r_quiz[quizCount]];
                        // 정답 사진 다이어로그 띄우기
                        android.app.AlertDialog.Builder inputBuilder = new android.app.AlertDialog.Builder(AdditionActivity.this);
                        inputBuilder.setTitle("사진으로 보는 정답");

                        final ImageView iv = new ImageView(AdditionActivity.this);
                        iv.setImageResource(cq.getPicture()); // 사진 소스는 여기!!
                        inputBuilder.setView(iv); // 다이어로그에 사진 띄우기

                        inputBuilder.setPositiveButton("다음으로 넘어가기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(quizCount+1 == numberOfQuestions){
                                    showResult();
                                }
                                else{
                                    ++quizCount;
                                    showNext();
                                }
                            }
                        });

                        inputBuilder.show();
                    }
                });
                builder.show();
            }
            // 오답인 경우
            else {
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
                            if(quizCount+1 == numberOfQuestions){
                                showResult();
                            }
                            else{
                                ++quizCount;
                                showNext();
                            }
                        }
                    }
                });
                builder.show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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

    // 다음 문제 출제
    public void showNext() {
        int r = r_quiz[quizCount];
        tv_w1.setText(quizzes[r].getWord1());
        tv_w2.setText(quizzes[r].getWord2());
        tv_w3.setText(quizzes[r].getWord3());
        tv_result.setText("내가 한 말");
        speech = "세 음소를 합쳐서 말해보세요.";
        tell.setLanguage(Locale.KOREAN);
        tell.setPitch(0.6f);
        tell.setSpeechRate(0.95f);
        tell.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
    }

    // 결과 출력
    public void showResult() {
        if(test.equals("test")){
            Intent intent = new Intent(getApplicationContext(), ConfrontationActivity.class);
            intent.putExtra("test", test);
            startActivityForResult(intent,5000);
        }
        else {
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
    }

    @Override
    public void onInit(int i) {
        tell.setLanguage(Locale.KOREAN);
        tell.setPitch(0.6f);
        tell.setSpeechRate(0.95f);
        tell.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tell.shutdown();
    }

}