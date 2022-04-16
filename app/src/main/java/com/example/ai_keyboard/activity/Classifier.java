package com.example.ai_keyboard.activity;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONException;
import org.json.JSONObject;



public class Classifier {


//    public Classifier() {
//    }
//
//    Map<String, Integer> map;
//    Map<Integer, String> newMap;
//    public static final Integer SIZE_OF_ARR=199;
//
//
//    //	word_index file read from the code below and store it inside #map
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    public void loadVocabData() {
//        BufferedReader reader = null;
//        StringBuilder sb= new StringBuilder();
//        String strFromJson="";
//        try {
//            reader = new BufferedReader(
//                    new InputStreamReader(getAssets().open("word_index.json"), "UTF-8"));
//
//            // do reading, usually loop until end of file reading
//            String mLine;
//            while ((mLine = reader.readLine()) != null) {
//                sb.append(mLine);
//            }
//
//            //JSON parser object to parse read file
////            JSONParser jsonParser = new JSONParser(String.valueOf(sb));
//            ObjectMapper objectMapper = new ObjectMapper();
//
//
//            JSONObject jsonObject;
//            jsonObject = new JSONObject(String.valueOf(sb));
//            strFromJson = jsonObject.toString();
//
//
//            map = objectMapper.readValue(strFromJson, Map.class);
//            newMap = map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
//        } catch (IOException | JSONException e) {
//            e.getMessage();
//        }
//    }
//
//    public ArrayList<Integer> addedTextToSequence(String[] str){
//        ArrayList<Integer> sequence=new ArrayList<>();
//        try {
////            System.out.println("Map Size is " + map.size());
//            Integer next;
//            for (int i=0;i<str.length;i++){
//                if(map.get(str[i])!=null){
//                    next = map.get(str[i]);
//                    sequence.add(next);
//                }else{
//                    sequence.add(1);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return sequence;
//    }
//
//    public String[] spiltText(String text){
//        String[] s = text.split("\\s+");
//        for (int i=0;i<s.length;i++){
//            System.out.println(s[i]);
//        }
//        return s;
//    }
//    public ArrayList<String> printText(ArrayList<Integer>givenSequence){
//        String texts = "";
//        for (int i=0;i<givenSequence.size();i++){
//            try {
//                texts += newMap.get(givenSequence.get(i));
//            }
//            catch(NullPointerException e) {
//                e.printStackTrace();
//            }
//        }
//        return texts;
//    }
//
//    public ArrayList<Integer> zeroPadding(ArrayList<Integer>sequence){
//        ArrayList<Integer>pad = new ArrayList<>();
//        for (int i=0;i<(SIZE_OF_ARR-sequence.size());i++){
//            pad.add(1);
//        }
//        for (int j=0;j<sequence.size();j++)
//            pad.add(sequence.get(j));
//
//        return pad;
//    }
}
