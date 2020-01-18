package com.example.helper;

import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class CustomDialog extends AppCompatActivity implements View.OnClickListener {
    Button positiveBtn;
    TextView title;
    TextView text;
    ImageView imageView;
    DialogListenerInterface customDialogLister;
    TextToSpeech speech;
    LinearLayout container;
    boolean isRead;

    public CustomDialog(Context context, String titleString, String textString, String bottomString, int topImageResource , boolean isRead){
        super();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.customdialog);
        container = findViewById(R.id.container);
        title = (TextView)findViewById(R.id.title);
        text = (TextView)findViewById(R.id.text);
        imageView = (ImageView)findViewById(R.id.image);
        this.isRead = isRead;
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) container.getLayoutParams();
        int width = context.getResources().getDisplayMetrics().widthPixels;
        width =(int) (width* 0.7);
        params.width = width;
        int height = context.getResources().getDisplayMetrics().heightPixels;
        params.height = (int) (height * 0.4);
        container.setLayoutParams(params);

        positiveBtn = (Button)findViewById(R.id.positiveBtn);
        positiveBtn.setOnClickListener(this);
        title.setText(titleString);
        text.setText(textString);
        final String temp = titleString+"\n"+textString;
        imageView.setBackgroundResource(topImageResource);
        positiveBtn.setText(bottomString);
        if(isRead) {
            speech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    speech.setLanguage(Locale.KOREAN);
                    speech.setPitch(0.6f);
                    speech.setSpeechRate(0.95f);
                    speech.speak(temp, TextToSpeech.QUEUE_FLUSH, null);
                }
            });

        }

    }

    public void setDialogListener(DialogListenerInterface customDialogListener){
        this.customDialogLister = customDialogListener;
    }
    public void dismiss(){
        if(isRead) {
            speech.stop();
            speech.stop();
            speech = null;
        }
    }
    public void onClick(View v){
        if(v == positiveBtn){
            if(customDialogLister!=null) {
                customDialogLister.onPositiveClicked();
            }
            dismiss();
        }
    }

}
