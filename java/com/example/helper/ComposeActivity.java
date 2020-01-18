package com.example.helper;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

// -- 단어 합성 -- //
public class ComposeActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{

    private class ComposeQuiz {
        private String word1, word2, answer, answer_s;
        private int picture;

        public ComposeQuiz(String w1, String w2, int p) {
            word1 = w1;
            word2 = w2;
            answer = word1 + word2;
            answer_s = word1 + " " + word2; //띄어쓰기 포함
            picture = p;
        }

        public String getWord1() { return word1; }
        public String getWord2() { return word2; }
        public String getAnswer() { return answer; }
        public String getAnswer_s() { return answer_s; }
        public int getPicture() { return picture; }
    }

    private Typeface mTypeface;
    private TextToSpeech tell;
    private String speech;
    private TextView tv_result, tv_text1, tv_text2;
    private ComposeQuiz quizzes[] = {new ComposeQuiz("여름","방학", R.drawable.summer_vacation),
            new ComposeQuiz("쓰레기", "봉지", R.drawable.trash_bag), new ComposeQuiz("검정", "치마", R.drawable.black_skirt),
            new ComposeQuiz("바다","여행", R.drawable.beach_trip), new ComposeQuiz("공중","화장실", R.drawable.public_toilet),
            new ComposeQuiz("버스","정류장", R.drawable.bus_stop), new ComposeQuiz("사과","주스", R.drawable.apple_juice),
            new ComposeQuiz("고추","잠자리", R.drawable.red_dragonfly), new ComposeQuiz("돼지","고기", R.drawable.pork),
            new ComposeQuiz("양념","치킨", R.drawable.seasoned_chicken), new ComposeQuiz("눈","사람", R.drawable.snowman),
            new ComposeQuiz("생일","선물", R.drawable.birthday_present), new ComposeQuiz("한국","사람", R.drawable.korean),
            new ComposeQuiz("딸기","사탕", R.drawable.strawberry_candy), new ComposeQuiz("시험","공부", R.drawable.test_study),
            new ComposeQuiz("반찬","가게", R.drawable.side_dish_shop), new ComposeQuiz("김치","볶음밥", R.drawable.kimchi_fried_rice)};
    private int maxQuizSize = quizzes.length;
    private int r_quiz[]; // 랜덤으로 뽑은 출제될 문제
    private int numberOfQuestions = 5; // 출제될 문제 수
    private int quizCount = 0;
    private String test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        mTypeface = Typeface.createFromAsset(getAssets(), "NotoSansCJKkr-Regular.otf");
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        setGlobalFont(root);

        tv_text1 = (TextView) findViewById(R.id.text);
        tv_text2 = (TextView) findViewById(R.id.text2);
        tv_result = (TextView) findViewById(R.id.result);
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
        tv_text1.setText(quizzes[r].getWord1());
        tv_text2.setText(quizzes[r].getWord2());
        tv_result.setText("내가 한 말");
        speech = "두 단어를 합쳐서 말해보세요. 정답을 말할 때는 마이크 버튼을 누르고 말하면 됩니다!";

        // 버튼 클릭시 stt 호출
        findViewById(R.id.speak).setOnClickListener(new View.OnClickListener() {
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

    // 다음 문제 출제
    public void showNext() {
        int r = r_quiz[quizCount];
        tv_text1.setText(quizzes[r].getWord1());
        tv_text2.setText(quizzes[r].getWord2());
        tv_result.setText("내가 한 말");
        speech = "두 단어를 합쳐서 말해보세요.";
        tell.setLanguage(Locale.KOREAN);
        tell.setPitch(0.6f);
        tell.setSpeechRate(0.95f);
        tell.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
    }

    // 결과 출력
    public void showResult() {
        if(test.equals("test")){
            Intent intent = new Intent(getApplicationContext(), DivisionActivity.class);
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

    // 음성 듣기
    public void voice() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "말하세요");
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 20);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        startActivityForResult(intent, 1);
    }

    // 음성 인식 결과 출력, 정답 확인
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1 && resultCode==RESULT_OK) {
            ArrayList<String> r = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String res = r.get(0);
            tv_result.setText(res); // 인식된 단어들 중 첫 번째 단어를 tv_result로.

            ComposeQuiz cq = quizzes[r_quiz[quizCount]];
            // 정답일 경우
            if(res.equals(cq.getAnswer()) || res.equals(cq.getAnswer_s())) {
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
                        android.app.AlertDialog.Builder inputBuilder = new android.app.AlertDialog.Builder(ComposeActivity.this);
                        inputBuilder.setTitle("사진으로 보는 정답");

                        final ImageView iv = new ImageView(ComposeActivity.this);
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
                        if(quizCount == numberOfQuestions){
                            showResult();
                        }
                        else {
                            showNext();
                        }
                    }
                });
                builder.show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
