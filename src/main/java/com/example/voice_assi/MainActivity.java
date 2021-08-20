package com.example.voice_assi;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.Manifest;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.example.ai_webza_tec.ai_method.checkForPreviousCallList;
import static com.example.ai_webza_tec.ai_method.clearContactListSavedData;
import static com.example.ai_webza_tec.ai_method.getContactList;
import static com.example.ai_webza_tec.ai_method.makeCall;
import static com.example.ai_webza_tec.ai_method.makeCallFromSavedContactList;
import static com.example.voice_assi.function.fetchName;
import static com.example.voice_assi.function.wishme;

public class MainActivity extends AppCompatActivity {
    private SpeechRecognizer recognizer;
    private TextToSpeech tts;
    private final TextView textView= findViewById(R.id.textView);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Dexter.withContext(this)
                .withPermission(Manifest.permission.RECORD_AUDIO)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {/* ... */}
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {
                        System.exit(0);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }


                }).check();

        initializetexttospeech();
        initializeResult();
    }

    private void initializetexttospeech() {
        tts=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(tts.getEngines().size()==0){
                    Toast.makeText(MainActivity.this, "Engine is not available", Toast.LENGTH_SHORT).show();
                }
                else{
                    String s=wishme();
                    speak(s+" i am jarvis..");

                }
            }
        });
    }



    private void speak(String msg) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(msg,TextToSpeech.QUEUE_FLUSH,null,null);
        }
        else{
            tts.speak(msg,TextToSpeech.QUEUE_FLUSH,null);
        }
    }


    private void initializeResult() {
        if(SpeechRecognizer.isRecognitionAvailable(this)){
            recognizer=SpeechRecognizer.createSpeechRecognizer(this);
            recognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle bundle) {

                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float v) {

                }

                @Override
                public void onBufferReceived(byte[] bytes) {

                }

                @Override

                public void onEndOfSpeech() {

                }

                @Override
                public void onError(int i) {

                }

                @Override
                public void onResults(Bundle bundle) {
                    ArrayList<String> result=bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    Toast.makeText(MainActivity.this, ""+result.get(0), Toast.LENGTH_SHORT).show();
                    response(result.get(0));
                }

                @Override
                public void onPartialResults(Bundle bundle) {

                }

                @Override
                public void onEvent(int i, Bundle bundle) {

                }
            });
        }
    }

    private void response(String msgs) {
        String msg = msgs.toLowerCase();
        if (msgs.contains("hi")) {
            speak("hello sir! how are you?");
        }
        if (msgs.contains("fine")) {
            speak("it's nice to hear that you are fine...how may i help you?");

        }
        if (msgs.contains("what is your name ")){

            speak("i am jarvis..");

        }
        if (msgs.contains("time")) {

            Date date = new Date();
            String time = DateUtils.formatDateTime(this, date.getTime(), DateUtils.FORMAT_SHOW_TIME);
            speak("it's" + time);
            textView.setText(time);

        }
        if (msgs.contains(" date")) {

            SimpleDateFormat df = new SimpleDateFormat("dd MM yyyy");
            Calendar cal = Calendar.getInstance();
            String today_date = df.format(cal.getTime());
            speak("it's" + today_date);
            textView.setText(today_date);

        }

        if (msgs.contains("search")){
            speak("searching in google" );
            Uri searchuri =Uri.parse("http://www.google.com/search?hl=en&site=&source=hp&q="+msgs);
            Intent intent=new Intent(Intent.ACTION_VIEW,searchuri);
            startActivity(intent);
        }


        if (msgs.contains("open google") ||msgs.contains("open chrome")||msgs.contains("open browser")) {
            speak("here it is");
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"));
            startActivity(intent);
        }

        if (msgs.contains("open youtube")) {
            speak("here it is");
            Context ctx = this;
            Intent i = ctx.getPackageManager().getLaunchIntentForPackage(("com.google.android.youtube"));
            ctx.startActivity(i);
        }
        if (msgs.contains("open facebook")) {
            speak("here it is");
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com"));
            startActivity(intent);
        }
        if (msgs.contains("open instagram")) {
            speak("here it is");
            Context ctx = this;
            Intent i = ctx.getPackageManager().getLaunchIntentForPackage(("com.instagram.android"));
            ctx.startActivity(i);
        }
        if (msgs.contains("open whatsapp")) {
            speak("here it is");
            Context ctx = this;
            Intent i = ctx.getPackageManager().getLaunchIntentForPackage(("com.whatsapp"));
            ctx.startActivity(i);
        }
        if (msgs.contains("open music player")) {
            speak("here it is");
            Context ctx = this;
            Intent i = ctx.getPackageManager().getLaunchIntentForPackage(("com.google.android.music"));
            ctx.startActivity(i);
        }


        if (msgs.contains("open map")) {
            Context ctx = this;
            Intent i = ctx.getPackageManager().getLaunchIntentForPackage(("com.google.android.apps.maps"));
            ctx.startActivity(i);
        }
        if (msgs.contains(" open photos")) {
            Context ctx = this;
            Intent i = ctx.getPackageManager().getLaunchIntentForPackage(("com.google.android.apps.photos"));
            ctx.startActivity(i);
        }




            if (msgs.contains("call")) {
                final String[] listName = new String[0];
                final String name = fetchName(msgs);
                Log.d("name", name);

                Dexter.withContext(this)
                        .withPermissions(Manifest.permission.READ_CONTACTS,
                                Manifest.permission.CALL_PHONE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport Report) {
                                if (Report.areAllPermissionsGranted()) {
                                    if (checkForPreviousCallList(MainActivity.this)) {
                                        speak(makeCallFromSavedContactList(MainActivity.this, name));
                                    } else {
                                        HashMap<String, String> list = getContactList(MainActivity.this, name);
                                        if (list.size() > 1) {
                                            for (String i : list.keySet()) {
                                                listName[0]=listName[0].concat("...................!" + i);
                                            }
                                            speak("which one?.. there is" + listName[0]);
                                        } else if (list.size() == 1) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                makeCall(MainActivity.this, list.values().stream().findFirst().get());
                                                clearContactListSavedData(MainActivity.this);
                                            }
                                        } else {
                                            speak("no contacts found");
                                            clearContactListSavedData(MainActivity.this);
                                        }
                                    }
                                }
                                if (Report.isAnyPermissionPermanentlyDenied()) {
                                    System.exit(0);
                                }


                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }


                        }).check();
            }

        }


        public void startRecording(View view) {
            Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);

            recognizer.startListening(intent);
        }
    }

