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

public class ConfrontationActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{

    private class ConfrontationQuiz {
        private String word, confrontation1, confrontation2, answer;
        int picture;

        public ConfrontationQuiz(String w, String c1, String c2, String a, int p) {
            word = w;
            confrontation1 = c1;
            confrontation2 = c2;
            answer = a;
            picture = p;
        }

        public String getWord() { return word; }
        public String getConfrontation1() { return confrontation1; }
        public String getConfrontation2() { return confrontation2; }
        public String getAnswer() { return answer; }
        public int getPicture() { return picture; }
    }

    private Typeface mTypeface;
    private TextToSpeech tell;
    private String speech;
    private TextView tv_q, tv_c1, tv_c2, tv_result;
    private Button speak;
    private ConfrontationQuiz quizzes[] = { new ConfrontationQuiz("화장실", "ㅘ", "ㅚ", "회장실", R.drawable.president_room),
            new ConfrontationQuiz("진구", "ㅣ", "ㅓ", "전구", R.drawable.light_bulb), new ConfrontationQuiz("성물", "ㅇ", "ㄴ", "선물", R.drawable.present),
            new ConfrontationQuiz("형수", "ㅕ", "ㅑ", "향수", R.drawable.perfume), new ConfrontationQuiz("강", "ㅇ", "ㅁ", "감", R.drawable.river),
            new ConfrontationQuiz("안약", "ㄴ", "ㄹ", "알약", R.drawable.pill), new ConfrontationQuiz("장사", "ㅅ", "ㅁ", "장마", R.drawable.rain),
            new ConfrontationQuiz("문전", "ㅁ", "ㅇ", "운전", R.drawable.driving), new ConfrontationQuiz("명화", "ㅁ", "ㅇ", "영화", R.drawable.movie),
            new ConfrontationQuiz("공사", "ㅗ", "ㅕ", "경사", R.drawable.slope), new ConfrontationQuiz("인명", "ㅁ", "ㅎ", "인형", R.drawable.doll),
            new ConfrontationQuiz("둥글둥글", "ㅜ", "ㅗ", "동글동글", R.drawable.circle), new ConfrontationQuiz("대구", "ㅜ", "ㅔ", "대게", R.drawable.snow_crab),
            new ConfrontationQuiz("스티커", "ㅣ", "ㅗ", "스토커", R.drawable.stalker), new ConfrontationQuiz("막", "ㅁ", "ㅆ", "싹", R.drawable.sprout),
            new ConfrontationQuiz("똥", "ㄸ", "ㄱ", "공", R.drawable.ball), new ConfrontationQuiz("검", "ㄱ", "ㄲ", "껌", R.drawable.gum),
            new ConfrontationQuiz("영어", "ㅕ", "ㅣ", "잉어", R.drawable.carp), new ConfrontationQuiz("여행", "ㅕ", "ㅠ", "유행", R.drawable.fashion_show),
            new ConfrontationQuiz("박", "ㅏ", "ㅕ", "벽", R.drawable.wall), new ConfrontationQuiz("숨", "ㅅ", "ㅊ", "춤", R.drawable.dance),
            new ConfrontationQuiz("장작", "ㅏ", "ㅓ", "정적", R.drawable.silence) };
    private int maxQuizSize = quizzes.length;
    private int r_quiz[]; // 랜덤으로 뽑은 출제될 문제
    private int numberOfQuestions = 5; // 출제될 문제 수
    private int quizCount = 0;
    private String test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confrontation);
        mTypeface = Typeface.createFromAsset(getAssets(), "NotoSansCJKkr-Regular.otf");
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        setGlobalFont(root);

        tv_q = (TextView) findViewById(R.id.text);
        tv_c1 = (TextView) findViewById(R.id.word1);
        tv_c2 = (TextView) findViewById(R.id.word2);
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
        tv_q.setText(quizzes[r].getWord());
        tv_c1.setText(quizzes[r].getConfrontation1());
        tv_c2.setText(quizzes[r].getConfrontation2());
        tv_result.setText("내가 한 말");
        speech = "제시된 음운을 알맞게 바꾸어 말해보세요. 정답을 말할 때는 마이크 버튼을 누르고 말하면 됩니다!";

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
            res = res.replaceAll(" ", ""); // 문자열 사이의 공백 제거 (되는지 안되는지 테스트는 안해봄)
            tv_result.setText(res); // 인식된 단어들 중 첫 번째 단어를 tv_result로.

            ConfrontationQuiz cq = quizzes[r_quiz[quizCount]];
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
                        ConfrontationQuiz cq = quizzes[r_quiz[quizCount]];
                        // 정답 사진 다이어로그 띄우기
                        android.app.AlertDialog.Builder inputBuilder = new android.app.AlertDialog.Builder(ConfrontationActivity.this);
                        inputBuilder.setTitle("사진으로 보는 정답");

                        final ImageView iv = new ImageView(ConfrontationActivity.this);
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

                    }
                });
                builder.show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // 다음 문제 출제
    public void showNext() {
        int r = r_quiz[quizCount];
        tv_q.setText(quizzes[r].getWord());
        tv_c1.setText(quizzes[r].getConfrontation1());
        tv_c2.setText(quizzes[r].getConfrontation2());
        tv_result.setText("내가 한 말");

        speech = "제시된 음운을 알맞게 바꾸어 말해보세요.";
        tell.setLanguage(Locale.KOREAN);
        tell.setPitch(0.6f);
        tell.setSpeechRate(0.95f);
        tell.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
    }

    // 결과 출력
    public void showResult() {
        if(test.equals("test")){
            Intent intent = new Intent(getApplicationContext(), DistinctionActivity.class);
            startActivityForResult(intent,5000);
        }

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
