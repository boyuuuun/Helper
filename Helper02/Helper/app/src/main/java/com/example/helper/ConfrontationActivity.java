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

public class ConfrontationActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{

    private class ConfrontationQuiz {
        private String word, confrontation1, confrontation2, answer, picture;

        public ConfrontationQuiz(String w, String c1, String c2, String a) {
            word = w;
            confrontation1 = c1;
            confrontation2 = c2;
            answer = a;
            //picture = p;
        }

        public String getWord() { return word; }
        public String getConfrontation1() { return confrontation1; }
        public String getConfrontation2() { return confrontation2; }
        public String getAnswer() { return answer; }
        public String getPicture() { return picture; }
    }

    private TextToSpeech tell;
    private String speech;
    private TextView tv_q, tv_c1, tv_c2, tv_result;
    private Button speak;
    private ConfrontationQuiz quizzes[] = { new ConfrontationQuiz("화장실", "ㅘ", "ㅚ", "회장실"),
            new ConfrontationQuiz("진구", "ㅣ", "ㅓ", "전구"), new ConfrontationQuiz("선물", "ㄴ", "ㅇ", "성물"),
            new ConfrontationQuiz("향수", "ㅑ", "ㅕ", "형수"), new ConfrontationQuiz("강", "ㅇ", "ㅁ", "감"),
            new ConfrontationQuiz("알약", "ㄹ", "ㄴ", "안약"), new ConfrontationQuiz("장사", "ㅅ", "ㅁ", "장마"),
            new ConfrontationQuiz("운전", "ㅇ", "ㅁ", "문전"), new ConfrontationQuiz("명화", "ㅁ", "ㅇ", "영화"),
            new ConfrontationQuiz("공사", "ㅗ", "ㅕ", "경사"), new ConfrontationQuiz("인형", "ㅎ", "ㅁ", "인명"),
            new ConfrontationQuiz("동글동글", "ㅗ", "ㅜ", "둥글둥글"), new ConfrontationQuiz("대구", "ㅜ", "ㅔ", "대게"),
            new ConfrontationQuiz("스티커", "ㅣ", "ㅗ", "스토커"), new ConfrontationQuiz("막", "ㅁ", "ㅆ", "싹"),
            new ConfrontationQuiz("똥", "ㄸ", "ㄱ", "공"), new ConfrontationQuiz("검", "ㄱ", "ㄲ", "껌"),
            new ConfrontationQuiz("영어", "ㅕ", "ㅣ", "잉어"), new ConfrontationQuiz("여행", "ㅕ", "ㅠ", "유행"),
            new ConfrontationQuiz("박", "ㅏ", "ㅕ", "벽"), new ConfrontationQuiz("숨", "ㅅ", "ㅊ", "춤"),
            new ConfrontationQuiz("장작", "ㅏ", "ㅓ", "정적") };
    private int maxQuizSize = quizzes.length;
    private int r_quiz[]; // 랜덤으로 뽑은 출제될 문제
    private int numberOfQuestions = 5; // 출제될 문제 수
    private int quizCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confrontation);

        tv_q = (TextView) findViewById(R.id.text);
        tv_c1 = (TextView) findViewById(R.id.word1);
        tv_c2 = (TextView) findViewById(R.id.word2);
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
