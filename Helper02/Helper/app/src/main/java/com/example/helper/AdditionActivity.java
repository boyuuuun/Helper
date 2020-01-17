package com.example.helper;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class AdditionActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{

    private class ComposeQuiz {
        private String answer, word1, word2, word3, picture;

        public ComposeQuiz(String a, String w1, String w2, String w3) {
            answer = a;
            word1 = w1;
            word2 = w2;
            word3 = w3;
            //picture = p;
        }

        public String getAnswer() { return answer; }
        public String getWord1() { return word1; }
        public String getWord2() { return word2; }
        public String getWord3() { return word3; }
        public String getPicture() { return picture; }
    }

    private TextToSpeech tell;
    private String speech;
    private TextView tv_w1, tv_w2, tv_w3, tv_result;
    private Button speak;
    private ComposeQuiz quizzes[] = {new ComposeQuiz("곰", "ㄱ", "ㅗ", "ㅁ"),
            new ComposeQuiz("발", "ㅂ", "ㅏ", "ㄹ"), new ComposeQuiz("침", "ㅊ", "ㅣ", "ㅁ"),
            new ComposeQuiz("힘", "ㅎ", "ㅣ", "ㅁ"), new ComposeQuiz("돈", "ㄷ", "ㅗ", "ㄴ"),
            new ComposeQuiz("숨", "ㅅ", "ㅜ", "ㅁ"), new ComposeQuiz("솜", "ㅅ", "ㅗ", "ㅁ"),
            new ComposeQuiz("살", "ㅅ", "ㅏ", "ㄹ"), new ComposeQuiz("남", "ㄴ", "ㅏ", "ㅁ"),
            new ComposeQuiz("잠", "ㅈ", "ㅏ", "ㅁ"), new ComposeQuiz("영", "ㅇ", "ㅕ", "ㅇ"),
            new ComposeQuiz("알", "ㅇ", "ㅏ", "ㄹ"), new ComposeQuiz("문", "ㅁ", "ㅜ", "ㄴ"),
            new ComposeQuiz("길", "ㄱ", "ㅣ", "ㄹ"), new ComposeQuiz("실", "ㅅ", "ㅣ", "ㄹ"),
            new ComposeQuiz("손", "ㅅ", "ㅗ", "ㄴ"), new ComposeQuiz("발", "ㅂ", "ㅏ", "ㄹ"),
            new ComposeQuiz("약", "ㅇ", "ㅑ", "ㄱ"), new ComposeQuiz("싹", "ㅆ", "ㅏ", "ㄱ"),
            new ComposeQuiz("껌", "ㄲ", "ㅓ", "ㅁ"), new ComposeQuiz("감", "ㄱ", "ㅏ", "ㅁ"),
            new ComposeQuiz("형", "ㅎ", "ㅕ", "ㅇ"),  new ComposeQuiz("춤", "ㅊ", "ㅜ", "ㅁ"),
            new ComposeQuiz("밤", "ㅂ", "ㅏ", "ㅁ"), new ComposeQuiz("불", "ㅂ", "ㅜ", "ㄹ"),
            new ComposeQuiz("벽", "ㅂ", "ㅕ", "ㄱ"), new ComposeQuiz("색", "ㅅ", "ㅐ", "ㄱ"),
            new ComposeQuiz("뱀", "ㅂ", "ㅐ", "ㅁ"), new ComposeQuiz("빗", "ㅂ", "ㅣ", "ㅅ"),
            new ComposeQuiz("똥", "ㄸ", "ㅗ", "ㅇ")};
    private int maxQuizSize = quizzes.length;
    private int r_quiz[]; // 랜덤으로 뽑은 출제될 문제
    private int numberOfQuestions = 5; // 출제될 문제 수
    private int quizCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addition);

        tv_w1 = (TextView) findViewById(R.id.word1);
        tv_w2 = (TextView) findViewById(R.id.word2);
        tv_w3 = (TextView) findViewById(R.id.word3);
        tv_result = (TextView) findViewById(R.id.result);
        speak = (Button) findViewById(R.id.speak);
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
                        /*if(quizCount == numberOfQuestions){
                            showResult();
                        }
                        else {
                            showNext();
                        }*/
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

}
