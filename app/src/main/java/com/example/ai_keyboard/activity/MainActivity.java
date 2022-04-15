package com.example.ai_keyboard.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.provider.UserDictionary;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.flatbuffers.Utf8;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    Map<String, Integer> map;
    Map<Integer, String> newMap;
    public static final Integer SIZE_OF_ARR = 199;

    TextView textView;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.ans_to_show);


        Classifier classifier = new Classifier();
//        loadVocabData1();



        String providedText = "আমার সোনার বাংলা আমি তোমায় ভালোবাসি";
        String[] textData = classifier.spiltText(providedText);


        ArrayList<Integer> addedToSequence = new ArrayList<>();
        addedToSequence = classifier.addedTextToSequence(textData);
        ArrayList<Integer> zeroPaddedSequence = new ArrayList<>();
        zeroPaddedSequence = classifier.zeroPadding(addedToSequence);

        String ans = classifier.printText(zeroPaddedSequence);
        textView.setText(ans);

//			ArrayList<Integer> paddedSequence = classifier.zeroPadding();
//			for (int i=0;i<paddedSequence.size();i++){
//				System.out.println(newMap.get(paddedSequence.get(i)));
//			}

        UserDictionary.Words.addWord(this, "newMedicalWord", 1, UserDictionary.Words.LOCALE_TYPE_CURRENT);
    }

    public void loadVocabData1(){
        BufferedReader reader = null;
        StringBuilder sb= new StringBuilder();
        String strFromJson="";
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("word_index.json"), "UTF-8"));

            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                sb.append(mLine);
            }

            //JSON parser object to parse read file
//            JSONParser jsonParser = new JSONParser(String.valueOf(sb));
            ObjectMapper objectMapper = new ObjectMapper();


            JSONObject jsonObject;
            jsonObject = new JSONObject(String.valueOf(sb));
            strFromJson = jsonObject.toString();


            map = objectMapper.readValue(strFromJson, Map.class);
            newMap = map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));

            System.out.printf(String.valueOf(sb));

        } catch (IOException | JSONException e) {
            e.getMessage();
        }
    }

}
