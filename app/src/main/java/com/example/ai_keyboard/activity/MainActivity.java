package com.example.ai_keyboard.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.provider.UserDictionary;
import android.widget.TextView;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;
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


//        Classifier classifier = new Classifier();
        loadVocabData1();



        String providedText = "আমার সোনার বাংলা আমি তোমায় ভালোবাসি";
        String[] textData = spiltText(providedText);


        ArrayList<Integer> addedToSequence = new ArrayList<>();
        addedToSequence = addedTextToSequence(textData);

        ArrayList<Integer> zeroPaddedSequence = new ArrayList<>();
        zeroPaddedSequence = zeroPadding(addedToSequence);

        String ans = printText(zeroPaddedSequence);
        textView.setText(addedToSequence.toString());

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

        } catch (IOException | JSONException e) {
            e.getMessage();
        }
    }



    public ArrayList<Integer> addedTextToSequence(String[] str){
        ArrayList<Integer> sequence=new ArrayList<>();
        try {
//            System.out.println("Map Size is " + map.size());
            Integer next;
            for (int i=0;i<str.length;i++){
                if(map.get(str[i])!=null){
                    next = map.get(str[i]);
                    sequence.add(next);
                }else{
                    sequence.add(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sequence;
    }

    public String[] spiltText(String text){
        String[] s = text.split("\\s+");
        for (int i=0;i<s.length;i++){
            System.out.println(s[i]);
        }
        return s;
    }
    public String printText(ArrayList<Integer>givenSequence){
        String texts = "";
        for (int i=0;i<givenSequence.size();i++){
            try {
                texts += newMap.get(givenSequence.get(i));
            }
            catch(NullPointerException e) {
                e.printStackTrace();
            }
        }
        return texts;
    }

    public ArrayList<Integer> zeroPadding(ArrayList<Integer>sequence){
        ArrayList<Integer>pad = new ArrayList<>();
        for (int i=0;i<(SIZE_OF_ARR-sequence.size());i++){
            pad.add(1);
        }
        for (int j=0;j<sequence.size();j++)
            pad.add(sequence.get(j));

        return pad;
    }
}
