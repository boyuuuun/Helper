package com.example.helper;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import android.app.AlertDialog;

import java.util.Locale;

// extends Activity로 변경, Manifest에 android:theme="@style/Theme.AppCompat" 추가 //
public class DivisionActivity extends Activity implements TextToSpeech.OnInitListener {

    private class DivisionQuiz {
        private String word, answer1, answer2, answer3;
        private int picture;

        public DivisionQuiz(String w, String a1, String a2, String a3, int p) {
            word = w;
            answer1 = a1;
            answer2 = a2;
            answer3 = a3;
            picture = p;
        }

        public String getWord() { return word; }
        public String getAnswer1() { return answer1; }
        public String getAnswer2() { return answer2; }
        public String getAnswer3() { return answer3; }
        public int getPicture() { return picture; }
    }

    private Typeface mTypeface;
    private TextToSpeech tell;
    private String speech;
    private TextView tv_q, tv_a1, tv_a2, tv_a3;
    private Button submit;
    private String a1="", a2="", a3="";
    private DivisionQuiz quizzes[] = {new DivisionQuiz("곰", "ㄱ", "ㅗ", "ㅁ", R.drawable.bear),
            new DivisionQuiz("발", "ㅂ", "ㅏ", "ㄹ", R.drawable.feet), new DivisionQuiz("침", "ㅊ", "ㅣ", "ㅁ", R.drawable.money),
            new DivisionQuiz("힘", "ㅎ", "ㅣ", "ㅁ", R.drawable.strong), new DivisionQuiz("돈", "ㄷ", "ㅗ", "ㄴ", R.drawable.spit),
            new DivisionQuiz("숨", "ㅅ", "ㅜ", "ㅁ", R.drawable.breath), new DivisionQuiz("솜", "ㅅ", "ㅗ", "ㅁ", R.drawable.cotton),
            new DivisionQuiz("살", "ㅅ", "ㅏ", "ㄹ", R.drawable.skin), new DivisionQuiz("남", "ㄴ", "ㅏ", "ㅁ", R.drawable.male),
            new DivisionQuiz("잠", "ㅈ", "ㅏ", "ㅁ", R.drawable.sleep), new DivisionQuiz("영", "ㅇ", "ㅕ", "ㅇ", R.drawable.zero),
            new DivisionQuiz("알", "ㅇ", "ㅏ", "ㄹ", R.drawable.egg), new DivisionQuiz("문", "ㅁ", "ㅜ", "ㄴ", R.drawable.door),
            new DivisionQuiz("길", "ㄱ", "ㅣ", "ㄹ", R.drawable.road), new DivisionQuiz("실", "ㅅ", "ㅣ", "ㄹ", R.drawable.thread),
            new DivisionQuiz("손", "ㅅ", "ㅗ", "ㄴ", R.drawable.hand), new DivisionQuiz("발", "ㅂ", "ㅏ", "ㄹ", R.drawable.feet),
            new DivisionQuiz("약", "ㅇ", "ㅑ", "ㄱ", R.drawable.pill), new DivisionQuiz("싹", "ㅆ", "ㅏ", "ㄱ", R.drawable.sprout),
            new DivisionQuiz("껌", "ㄲ", "ㅓ", "ㅁ", R.drawable.gum), new DivisionQuiz("감", "ㄱ", "ㅏ", "ㅁ", R.drawable.persimmon),
            new DivisionQuiz("형", "ㅎ", "ㅕ", "ㅇ", R.drawable.brother),  new DivisionQuiz("춤", "ㅊ", "ㅜ", "ㅁ", R.drawable.dance),
            new DivisionQuiz("밤", "ㅂ", "ㅏ", "ㅁ", R.drawable.night), new DivisionQuiz("불", "ㅂ", "ㅜ", "ㄹ", R.drawable.fire),
            new DivisionQuiz("벽", "ㅂ", "ㅕ", "ㄱ", R.drawable.wall), new DivisionQuiz("색", "ㅅ", "ㅐ", "ㄱ", R.drawable.color),
            new DivisionQuiz("뱀", "ㅂ", "ㅐ", "ㅁ", R.drawable.snake), new DivisionQuiz("빗", "ㅂ", "ㅣ", "ㅅ", R.drawable.hairbrush),
            new DivisionQuiz("똥", "ㄸ", "ㅗ", "ㅇ", R.drawable.dung)};
    private int maxQuizSize = quizzes.length;
    private int r_quiz[]; // 랜덤으로 뽑은 출제될 문제
    private int numberOfQuestions = 5; // 출제될 문제 수
    private int quizCount = 0;
    private String test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_division);
        mTypeface = Typeface.createFromAsset(getAssets(), "NotoSansCJKkr-Regular.otf");
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        setGlobalFont(root);

        tv_q  = (TextView) findViewById(R.id.text);
        tv_a1 = (TextView) findViewById(R.id.input1);
        tv_a2 = (TextView) findViewById(R.id.input2);
        tv_a3 = (TextView) findViewById(R.id.input3);
        submit = (Button) findViewById(R.id.submit);
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
        speech = "위 단어를 음소 단위로 나누어 입력해보세요. 박스를 클릭하여 정답을 입력하고 버튼을 클릭하시면 됩니다!";

        // 정답 입력 박스 클릭 시, 정답 입력 창 띄움
        tv_a1.setOnClickListener(new View.OnClickListener() { // 첫 번째 음운
            @Override
            public void onClick(View v) { // 첫 번째 음소
                AlertDialog.Builder inputBuilder = new AlertDialog.Builder(DivisionActivity.this);

                inputBuilder.setTitle("정답 입력");
                inputBuilder.setMessage("첫 번째 음소를 입력해 주세요.");

                final EditText et_a1 = new EditText(DivisionActivity.this);
                inputBuilder.setView(et_a1);

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); // 키보드 자동으로 올라옴
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                et_a1.setFocusableInTouchMode(true); // 자동 포커스
                et_a1.requestFocus();

                inputBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(et_a1.getWindowToken(),0); // 키보드 자동으로 내림
                        a1 = et_a1.getText().toString();
                        tv_a1.setText(a1);
                    }
                });

                inputBuilder.show();
            }
        });

        tv_a2.setOnClickListener(new View.OnClickListener() { // 두 번째 음운
            @Override
            public void onClick(View v) { // 두 번째 음소
                AlertDialog.Builder inputBuilder = new AlertDialog.Builder(DivisionActivity.this);

                inputBuilder.setTitle("정답 입력");
                inputBuilder.setMessage("두 번째 음소를 입력해 주세요.");

                final EditText et_a2 = new EditText(DivisionActivity.this);
                inputBuilder.setView(et_a2);

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); // 키보드 자동으로 올라옴
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                et_a2.setFocusableInTouchMode(true); // 자동 포커스
                et_a2.requestFocus();

                inputBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(et_a2.getWindowToken(),0); // 키보드 자동으로 내림
                        a2 = et_a2.getText().toString();
                        tv_a2.setText(a2);
                    }
                });

                inputBuilder.show();
            }
        });

        tv_a3.setOnClickListener(new View.OnClickListener() { // 세 번째 음운
            @Override
            public void onClick(View v) { // 세 번째 음소
                AlertDialog.Builder inputBuilder = new AlertDialog.Builder(DivisionActivity.this);

                inputBuilder.setTitle("정답 입력");
                inputBuilder.setMessage("세 번째 음소를 입력해 주세요.");

                final EditText et_a3 = new EditText(DivisionActivity.this);
                inputBuilder.setView(et_a3);

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); // 키보드 자동으로 올라옴
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                et_a3.setFocusableInTouchMode(true); // 자동 포커스
                et_a3.requestFocus();

                inputBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(et_a3.getWindowToken(),0); // 키보드 자동으로 내림
                        a3 = et_a3.getText().toString();
                        tv_a3.setText(a3);
                    }
                });

                inputBuilder.show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubmitAnswer();
            }
        });


        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void SubmitAnswer() {
        DivisionQuiz cq = quizzes[r_quiz[quizCount]];

        // 정답을 하나라도 적지 않은 경우
        if(a1.equals("") || a2.equals("") || a3.equals("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("모든 정답을 작성해주세요.");
            speech = "모든 정답을 작성해주세요.";
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
        // 정답일 경우
        else if(a1.equals(cq.getAnswer1()) && a2.equals(cq.getAnswer2()) && a3.equals(cq.getAnswer3())) {
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
                    DivisionQuiz cq = quizzes[r_quiz[quizCount]];
                    // 정답 사진 다이어로그 띄우기
                    android.app.AlertDialog.Builder inputBuilder = new android.app.AlertDialog.Builder(DivisionActivity.this);
                    inputBuilder.setTitle("사진으로 보는 정답");

                    final ImageView iv = new ImageView(DivisionActivity.this);
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

    private void showNext() {
        int r = r_quiz[quizCount];
        tv_q.setText(quizzes[r].getWord());
        tv_a1.setText("");
        tv_a2.setText("");
        tv_a3.setText("");
        a1 = a2 = a3 = "";

        speech = "위 단어를 음소 단위로 나누어 입력해보세요.";
        tell.setLanguage(Locale.KOREAN);
        tell.setPitch(0.6f);
        tell.setSpeechRate(0.95f);
        tell.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
    }

    // 결과 출력
    public void showResult() {
        if (test.equals("test")) {
            Intent intent = new Intent(getApplicationContext(), AdditionActivity.class);
            intent.putExtra("test", test);
            startActivityForResult(intent, 5000);
        } else {
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
                    Intent intent = new Intent(DivisionActivity.this, MainActivity.class);
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