package com.example.helper;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.app.AlertDialog;

import java.util.Locale;

// extends Activity로 변경, Manifest에 android:theme="@style/Theme.AppCompat" 추가 //
public class DivisionActivity extends Activity implements TextToSpeech.OnInitListener {

    private class DivisionQuiz {
        private String word, answer1, answer2, answer3, picture;

        public DivisionQuiz(String w, String a1, String a2, String a3) {
            word = w;
            answer1 = a1;
            answer2 = a2;
            answer3 = a3;
            //picture = p;
        }

        public String getWord() { return word; }
        public String getAnswer1() { return answer1; }
        public String getAnswer2() { return answer2; }
        public String getAnswer3() { return answer3; }
        public String getPicture() { return picture; }
    }

    private TextToSpeech tell;
    private String speech;
    private TextView tv_q, tv_a1, tv_a2, tv_a3;
    private Button submit;
    private String a1="", a2="", a3="";
    private DivisionQuiz quizzes[] = {new DivisionQuiz("곰", "ㄱ", "ㅗ", "ㅁ"),
            new DivisionQuiz("발", "ㅂ", "ㅏ", "ㄹ"), new DivisionQuiz("침", "ㅊ", "ㅣ", "ㅁ"),
            new DivisionQuiz("힘", "ㅎ", "ㅣ", "ㅁ"), new DivisionQuiz("돈", "ㄷ", "ㅗ", "ㄴ"),
            new DivisionQuiz("숨", "ㅅ", "ㅜ", "ㅁ"), new DivisionQuiz("솜", "ㅅ", "ㅗ", "ㅁ"),
            new DivisionQuiz("살", "ㅅ", "ㅏ", "ㄹ"), new DivisionQuiz("남", "ㄴ", "ㅏ", "ㅁ"),
            new DivisionQuiz("잠", "ㅈ", "ㅏ", "ㅁ"), new DivisionQuiz("영", "ㅇ", "ㅕ", "ㅇ"),
            new DivisionQuiz("알", "ㅇ", "ㅏ", "ㄹ"), new DivisionQuiz("문", "ㅁ", "ㅜ", "ㄴ"),
            new DivisionQuiz("길", "ㄱ", "ㅣ", "ㄹ"), new DivisionQuiz("실", "ㅅ", "ㅣ", "ㄹ"),
            new DivisionQuiz("손", "ㅅ", "ㅗ", "ㄴ"), new DivisionQuiz("발", "ㅂ", "ㅏ", "ㄹ"),
            new DivisionQuiz("약", "ㅇ", "ㅑ", "ㄱ"), new DivisionQuiz("싹", "ㅆ", "ㅏ", "ㄱ"),
            new DivisionQuiz("껌", "ㄲ", "ㅓ", "ㅁ"), new DivisionQuiz("감", "ㄱ", "ㅏ", "ㅁ"),
            new DivisionQuiz("형", "ㅎ", "ㅕ", "ㅇ"),  new DivisionQuiz("춤", "ㅊ", "ㅜ", "ㅁ"),
            new DivisionQuiz("밤", "ㅂ", "ㅏ", "ㅁ"), new DivisionQuiz("불", "ㅂ", "ㅜ", "ㄹ"),
            new DivisionQuiz("벽", "ㅂ", "ㅕ", "ㄱ"), new DivisionQuiz("색", "ㅅ", "ㅐ", "ㄱ"),
            new DivisionQuiz("뱀", "ㅂ", "ㅐ", "ㅁ"), new DivisionQuiz("빗", "ㅂ", "ㅣ", "ㅅ"),
            new DivisionQuiz("똥", "ㄸ", "ㅗ", "ㅇ")};
    private int maxQuizSize = quizzes.length;
    private int r_quiz[]; // 랜덤으로 뽑은 출제될 문제
    private int numberOfQuestions = 5; // 출제될 문제 수
    private int quizCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_division);

        tv_q  = (TextView) findViewById(R.id.text);
        tv_a1 = (TextView) findViewById(R.id.input1);
        tv_a2 = (TextView) findViewById(R.id.input2);
        tv_a3 = (TextView) findViewById(R.id.input3);
        submit = (Button) findViewById(R.id.submit);
        r_quiz = new int[numberOfQuestions];
        tell = new TextToSpeech(this, this);

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
            public void onClick(View v) {
                AlertDialog.Builder inputBuilder = new AlertDialog.Builder(DivisionActivity.this);

                inputBuilder.setTitle("정답 입력");
                inputBuilder.setMessage("첫 번째 음소를 입력해 주세요.");

                final EditText et_a1 = new EditText(DivisionActivity.this);
                inputBuilder.setView(et_a1);

                inputBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        a1 = et_a1.getText().toString();
                        tv_a1.setText(a1);
                    }
                });

                inputBuilder.show();
            }
        });

        tv_a2.setOnClickListener(new View.OnClickListener() { // 두 번째 음운
            @Override
            public void onClick(View v) {
                AlertDialog.Builder inputBuilder = new AlertDialog.Builder(DivisionActivity.this);

                inputBuilder.setTitle("정답 입력");
                inputBuilder.setMessage("두 번째 음소를 입력해 주세요.");

                final EditText et_a2 = new EditText(DivisionActivity.this);
                inputBuilder.setView(et_a2);

                inputBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        a2 = et_a2.getText().toString();
                        tv_a2.setText(a2);
                    }
                });

                inputBuilder.show();
            }
        });

        tv_a3.setOnClickListener(new View.OnClickListener() { // 세 번째 음운
            @Override
            public void onClick(View v) {
                AlertDialog.Builder inputBuilder = new AlertDialog.Builder(DivisionActivity.this);

                inputBuilder.setTitle("정답 입력");
                inputBuilder.setMessage("세 번째 음소를 입력해 주세요.");

                final EditText et_a3 = new EditText(DivisionActivity.this);
                inputBuilder.setView(et_a3);

                inputBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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
                    if(quizCount+1 == numberOfQuestions){
                        showResult();
                    }
                    else{
                        ++quizCount;
                        showNext();
                    }
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


}
