package com.example.ai_keyboard.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.provider.UserDictionary;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ai_keyboard.activity.ml.Model;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    Map<String, Integer> map;
    Map<Integer, String> newMap;
    public static final Integer SIZE_OF_ARR = 199;

    TextView textView;
    EditText editText;
    Button predictBtn;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.ans_to_show);
        editText = findViewById(R.id.text_to_write);
        predictBtn = findViewById(R.id.predict_text);

//        Classifier classifier = new Classifier();
        loadVocabData();

        predictBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String providedText = editText.getText().toString();
                //        String providedText = "আমি তোমার";
                String[] textData = spiltText(providedText);


                ArrayList<Integer> addedToSequence = new ArrayList<>();
                addedToSequence = addedTextToSequence(textData);

                ArrayList<Integer> zeroPaddedSequence = new ArrayList<>();
                zeroPaddedSequence = zeroPadding(addedToSequence);

                /*Here i Load the model and get
                 * the 10001 length probablity
                 * */
                float[] output = loadModel(zeroPaddedSequence);

                /* Sorted by probablity
                    first 10 output processed */

                ArrayList<Integer> sortedProbablity = new ArrayList<>();
                sortedProbablity = sortingOutput(output);

                String ans = printText(sortedProbablity);
                textView.setText(ans);
            }
        });

        UserDictionary.Words.addWord(this, "newMedicalWord", 1, UserDictionary.Words.LOCALE_TYPE_CURRENT);
    }


    /*  This section sort the probablity with the
    *   index value */

    public static ArrayList<Integer> sortingOutput(float[]  data){
        Pair[] array = new Pair[data.length];

        for (int i=0;i<data.length;i++){
            array[i] = new Pair(i,data[i]);
        }
        Arrays.sort(array);
        ArrayList<Integer>sortedText = new ArrayList<>();

        for (int i=1;i<10;i++){
            sortedText.add(array[i].index);
//            System.out.print(array[i].index);
//            System.out.print("-");
//            System.out.println(array[i].value);
        }
        return sortedText;
    }


    public float[] loadModel(ArrayList<Integer>data){
        float[] output = new float[10001];
        try {
            Model model = Model.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 199}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4*SIZE_OF_ARR);
            byteBuffer.order(ByteOrder.nativeOrder());

            for (int i=0;i<SIZE_OF_ARR;i++){
//                float fl = (float) data[i];
                byteBuffer.putInt(data.get(i));
            }
            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            float[] confidences = outputFeature0.getFloatArray();

            int biger = 0;
            float currentConfidence= 0.0F;

            for (int i=2;i<10000;i++){
                if(currentConfidence<confidences[i]){
                    biger = i;
                    currentConfidence = confidences[i];
                }
                float xx = confidences[i];
            }
            output = confidences;
//            textView.setText("Model Loaded");
//            textView.setText(biger);

            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            e.getMessage();
            // TODO Handle the exception
            Log.e(TAG, "Fail to load model", e);
        }
        return output;
    }




    /* From here i Load the json word vocab data
    * */

    public void loadVocabData(){
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



    //added text data to the sequence
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
//        for (int i=0;i<s.length;i++){
//            System.out.println(s[i]);
//        }
        return s;
    }
    public String printText(ArrayList<Integer>givenSequence){
        String texts = "";
        for (int i=0;i<givenSequence.size();i++){
            try {
                texts += newMap.get(givenSequence.get(i))+", "+"\n";
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
