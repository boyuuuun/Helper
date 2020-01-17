package com.example.helper;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

// -- 단어 합성 -- //
public class ComposeActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{

    private class ComposeQuiz {
        private String word1, word2, answer, answer_s, picture;

        public ComposeQuiz(String w1, String w2, String p) {
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
        public String getPicture() { return picture; }
    }

    private TextToSpeech tell;
    private String speech;
    private TextView tv_result, tv_text1, tv_text2;
    private ComposeQuiz quizzes[];
    private int maxQuizSize;
    private int r_quiz[]; // 랜덤으로 뽑은 출제될 문제
    private int numberOfQuestions = 5; // 출제될 문제 수
    private int quizCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        String[][] q = { {"여름","방학", "summer_vacation"}, {"쓰레기", "봉지", "trash_bag"}, {"검정", "치마", "black_skirt"},
                {"바다","여행", "beach_trip"}, {"공중","화장실", "public_toilet"}, {"버스","정류장", "bus_stop"},
                {"사과","주스", "apple_juice"}, {"고추","잠자리", "red_dragonfly"}, {"돼지","고기", "pork"},
                {"양념","치킨", "seasoned_chicken"}, {"눈","사람", "snowman"}, {"생일","선물", "birthday_present"},
                {"한국","사람", "korean"}, {"딸기","사탕", "strawberry_candy"}, {"시험","공부", "test_study"},
                {"그림","일기", "drawing_diary"}, {"순대","떡볶이", "sundae_tteokbokki"} };

        tv_text1 = (TextView) findViewById(R.id.text);
        tv_text2 = (TextView) findViewById(R.id.text2);
        tv_result = (TextView) findViewById(R.id.result);
        maxQuizSize = q.length;
        quizzes = new ComposeQuiz[maxQuizSize];
        r_quiz = new int[numberOfQuestions];
        tell = new TextToSpeech(this, this);

        // 문제를 quizzes 배열에 추가
        for(int i=0; i<quizzes.length; i++) {
            quizzes[i] = new ComposeQuiz(q[i][0], q[i][1], q[i][2]);
        }

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

    // 음성 인식 결과 출력
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

                /*Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.image_dialog);
                dialog.setTitle("정답 이미지 확인");

//                ImageView image = (ImageView) dialog.findViewById(R.id.image);
//                image.setImageResource(R.drawable.summer_vacation);

                TextView imageName = (TextView) dialog.findViewById(R.id.imageName);
                imageName.setText("gkgk");

                dialog.show();*/

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
}
