package com.xamarin.azuredevdays;

import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.app.Dialog;
import android.app.AlertDialog;
import android.content.DialogInterface;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    SentimentClient sentimentClient;
    Gson gson = new Gson();

    Toolbar toolbar;
    LinearLayout backgroundLayout;
    EditText sentimentText;
    Button getSentimentButton;
    Button crashButton;
    Button eventButton;
    Button colorButton;
    TextView emojiView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sentimentClient = new SentimentClient();
        try {
            sentimentClient.GetKey(
                    new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            System.out.println("Error getting key!");
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.isSuccessful()) {
                                Models.KeyResponse keyResponse = gson.fromJson(response.body().string(),Models.KeyResponse.class);
                                sentimentClient.sentimentAPIRegion = keyResponse.region;
                                sentimentClient.sentimentAPIKey = keyResponse.key;
                            }
                        }
                    }
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        int defaultBkgColor = Color.parseColor(Constants.Colors.defaultBackgroundColor);

        backgroundLayout = (LinearLayout) findViewById(R.id.backGroundLayout);
        backgroundLayout.setBackgroundColor(defaultBkgColor);
        toolbar.setBackgroundColor(defaultBkgColor);
        getWindow().setStatusBarColor(darkenColor(defaultBkgColor));

        sentimentText = (EditText) findViewById(R.id.sentimentText);
        emojiView = (TextView) findViewById(R.id.emojiView);

        getSentimentButton = (Button) findViewById(R.id.getSentimentButton);
        getSentimentButton.setOnClickListener(this);

        Button crashButton = (Button) findViewById(R.id.sendCrashButton);
        crashButton.setOnClickListener(this);

        Button eventButton = (Button) findViewById(R.id.sendEventButton);
        eventButton.setOnClickListener(this);

        Button colorButton = (Button) findViewById(R.id.sendColorButton);
        colorButton.setOnClickListener(this);


    }

    public void onClick(final View view){
        switch(view.getId()){
            case R.id.getSentimentButton:

                if(sentimentText.getText().toString().equals("")) {
                    Snackbar.make(view, "You must enter something!", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                String res = "";
                getSentimentButton.setText("Calculating");
                getSentimentButton.setEnabled(false);
                sentimentText.setEnabled(false);
                try {
                    sentimentClient.GetSentimentResult(sentimentText.getText().toString(),
                            new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    // Something went wrong
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {

                                    if (response.isSuccessful()) {

                                        Models.SentimentResponse sentimentResponse = gson.fromJson(response.body().string(),Models.SentimentResponse.class);

                                        final float score = sentimentResponse.documents.get(0).score;

                                        MainActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                UpdateUI(score);
                                            }
                                        });

                                    } else {
                                        Snackbar.make(view, response.message(), Snackbar.LENGTH_LONG).show();
                                        MainActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                UpdateUI(-1);
                                            }
                                        });
                                    }
                                }
                            });
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.sendCrashButton:
                DialogFragment crashDialog = new CrashDialog();
                crashDialog.show(getFragmentManager(), "crashDialog");
                break;

            case R.id.sendEventButton:
                DialogFragment eventDialog = new EventDialog();
                eventDialog.show(getFragmentManager(), "eventDialog");
                break;

            case R.id.sendColorButton:
                DialogFragment colorDialog = new ColorDialog();
                colorDialog.show(getFragmentManager(), "colorDialog");
                break;
        }
    }


    public void UpdateUI(float score){

        if(score!=-1) {
            int sentimentColor = Color.parseColor(getBackgroundColor(score));
            emojiView.setText(getEmojiString(score));
            backgroundLayout.setBackgroundColor(sentimentColor);
            toolbar.setBackgroundColor(sentimentColor);
            MainActivity.this.getWindow().setStatusBarColor(darkenColor(sentimentColor));
        }
        sentimentText.setEnabled(true);
        getSentimentButton.setText("Submit");
        getSentimentButton.setEnabled(true);
    }

    public String getEmojiString(float score){

        if(score < 0.4)
        {
            return Constants.Emojis.SadFaceEmoji;
        }
        else if (score >= 0.4 && score <=0.6)
        {
            return Constants.Emojis.NeutralFaceEmoji;
        }
        else if (score > 0.6){
            return Constants.Emojis.HappyFaceEMoji;
        }
        else{
            return "";
        }
    }

    public String getBackgroundColor(float score){
        if(score <= 0.1)
            return Constants.Colors.EmotionColor1;
        else if (score > 0.1 && score <= 0.2)
            return Constants.Colors.EmotionColor2;
        else if (score > 0.2 && score <= 0.3)
            return Constants.Colors.EmotionColor3;
        else if (score > 0.3 && score <= 0.4)
            return Constants.Colors.EmotionColor4;
        else if (score > 0.4 && score <= 0.6)
            return Constants.Colors.EmotionColor5;
        else if (score > 0.6 && score <= 0.7)
            return Constants.Colors.EmotionColor6;
        else if (score > 0.7 && score <= 0.8)
            return Constants.Colors.EmotionColor7;
        else if (score > 0.8 && score <= 0.9)
            return Constants.Colors.EmotionColor8;
        else if (score > 0.9)
            return Constants.Colors.EmotionColor9;
        else
            return "";
    }

    private static int darkenColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.8f;
        return Color.HSVToColor(hsv);
    }

    public static class CrashDialog extends DialogFragment {
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("A crash report will be sent when you reopen the app.")
                    .setPositiveButton("Crash app", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            throw new RuntimeException("crashing");
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            return builder.create();
        }
    }

    public static class EventDialog extends DialogFragment {
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Event sent").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            return builder.create();
        }
    }

    private static Map<String, String> properties = new HashMap<>();

    public static class ColorDialog extends DialogFragment {
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            CharSequence[] colors = {"Yellow", "Blue", "Red"};
            builder.setTitle("Pick a color").setItems(colors, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int index) {
                    switch (index) {
                        case 0:
                            properties.put("Color", "Yellow");
                            break;
                        case 1:
                            properties.put("Color", "Blue");
                            break;
                        case 2:
                            properties.put("Color", "Red");
                            break;
                    }
                }
            });
            return builder.create();
        }
    }

}
