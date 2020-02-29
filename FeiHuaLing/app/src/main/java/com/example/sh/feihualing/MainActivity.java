package com.example.sh.feihualing;

import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    private TextView textSentence;
    private TextView textTitle;
    private String[] resultSentence;//搜索的诗句
    private String[] resultTitle;//搜索的题目
    private String[] resultAuthor;
    private Button btnSearch;
    private Button btnPre;
    private Button btnNex;
    private EditText editKey;
    private int totalSentence;
    private TextView textPage;
    private int currentSentence;
    private TextView textSign;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(editKey.getText())) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                    dialog.setTitle("警告");
                    dialog.setMessage("请先输入");
                    dialog.show();
                }else  {
                    totalSentence = 0;
                    String temTitle = "";
                    String temAuthor = "";
                    int hasKey = 0;
                    InputStream inputStream = getResources().openRawResource(R.raw.shi300);
                    try{
                        Scanner scanner = new Scanner(inputStream);
                        String str;
                        while(scanner.hasNext()) {
                            str = scanner.nextLine();
                            if(str.contains("：")) {
                                temTitle = str.split("：")[1];
                                temAuthor = str.split("：")[0].substring(3);
                            }else {
                                if(str.contains(editKey.getText().toString())) {
                                    totalSentence ++;
                                    resultSentence[totalSentence] = str;
                                    resultTitle[totalSentence] = temTitle;
                                    resultAuthor[totalSentence] = temAuthor;
                                }
                            }
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(totalSentence == 0) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                        dialog.setTitle("搜索结果");
                        dialog.setMessage("唐诗300首没没有含有"+editKey.getText().toString()+"的诗句");
                        dialog.show();
                    }else {
                        currentSentence = 1;
                        showSentence();
                    }
                }

            }
        });

        btnNex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(totalSentence == 0) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                    dialog.setTitle("警告");
                    dialog.setMessage("请先搜索");
                    dialog.show();
                }else {
                    if(currentSentence == totalSentence) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                        dialog.setTitle("警告");
                        dialog.setMessage("当前已经是最后一个句子");
                        dialog.show();
                    }else {
                        currentSentence ++;
                        showSentence();
                    }
                }

            }
        });

        btnPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(totalSentence == 0){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                    dialog.setTitle("警告");
                    dialog.setMessage("请先搜索");
                    dialog.show();
                }else {
                    if(currentSentence == 1) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                        dialog.setTitle("警告");
                        dialog.setMessage("当前已经是第一个句子");
                        dialog.show();
                    }else{
                        currentSentence --;
                        showSentence();
                    }
                }
            }
        });


    }

    private void showSentence(){
        SpannableStringBuilder style = new SpannableStringBuilder(resultSentence[currentSentence]);
        int k = resultSentence[currentSentence].indexOf(editKey.getText().toString());
        while(k != -1) {
            style.setSpan(new ForegroundColorSpan(Color.RED),k,k+editKey.getText().toString().length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            k = resultSentence[currentSentence].indexOf(editKey.getText().toString(),k + 1);
        }
        textSentence.setText(style);
        textTitle.setText(resultAuthor[currentSentence]+"《"+resultTitle[currentSentence]+"》");
        textPage.setText(currentSentence+"/"+totalSentence);
        textSign.setText("——");
    }


    private void init(){
        textSign = findViewById(R.id.text_sign);
        textSentence = findViewById(R.id.text_sentence);
        textTitle = findViewById(R.id.text_title);
        textPage = findViewById(R.id.text_page);
        btnSearch = findViewById(R.id.btn_search);
        btnPre = findViewById(R.id.btn_pre);
        btnNex = findViewById(R.id.btn_next);
        editKey = findViewById(R.id.edit_word);
        resultSentence = new String[301];
        resultTitle = new String[301];
        resultAuthor = new String[301];
    }
}
