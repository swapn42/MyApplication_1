package com.example.jarvis.myapplication;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends Activity  implements  TextToSpeech.OnInitListener {

private TextToSpeech textToSpeech;

protected static final int RESULT_SPEECH = 1;

    private TextView mTextView;
    private ImageButton btnSpeak;
    private TextView txtText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
                btnSpeak = (ImageButton) stub.findViewById(R.id.btnSpeak);
                txtText = (TextView) stub.findViewById(R.id.txtText);
               // convertTextToSpeech("hello sir good morning");
                btnSpeak.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(
                                RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

                        try {
                            startActivityForResult(intent, RESULT_SPEECH);
                            txtText.setText("");
                        } catch (ActivityNotFoundException a) {
                            Toast t = Toast.makeText(getApplicationContext(),
                                    "Ops! Your device doesn't support Speech to Text",
                                    Toast.LENGTH_SHORT);
                            t.show();
                        }
                    }
                });
            }
        });





    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SPEECH: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> text = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    txtText.setText(text.get(0));
                    if(txtText.getText().toString().equals("hello Jarvis")){
                        txtText.setText("hi swapnil sir");
                        convertTextToSpeech(txtText.getText().toString());
                    }
                    if(txtText.getText().toString().equals("ok")){
                        txtText.setText("ok sir");
                        convertTextToSpeech(txtText.getText().toString());
                    }
                    if(txtText.getText().toString().equals("how are you")){
                        txtText.setText("i am fine sir");
                        convertTextToSpeech(txtText.getText().toString());
                    }

                }
                break;
            }

        }
    }

    private void convertTextToSpeech(String string) {
        String text =string;
        if (null == text || "".equals(text)) {
            text = "Please give some input.";
        }
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("error", "This Language is not supported");
            } else {
                convertTextToSpeech("hello sir good morning! how can i help u sir?");
            }
        } else {
            Log.e("error", "Initilization Failed!");
        }

    }
}
