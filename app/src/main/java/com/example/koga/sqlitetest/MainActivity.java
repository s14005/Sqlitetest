package com.example.koga.sqlitetest;

import android.content.ContentValues;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper helper;
    public static SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ＤＢファイルの真偽
        File file = new File("/data/data/com.example.koga.sqlitetest/databases/uma.db");
        if(file.exists()){
            file.delete();
            Log.d("kawasaki_debug", "ファイルを消去しました");
        }else{
            Log.d("kawasaki_debug","ファイルはありません");
        }

        //ＤＢの作成
        helper = new DatabaseHelper(this);

        //CSVファイルの読み込み準備
        AssetManager as = getResources().getAssets();
        //ＤＢオープン
        mDb = helper.getWritableDatabase();
        //ＤＢ値格納変数
        ContentValues value = new ContentValues();
        try {

            BufferedReader bf = new BufferedReader(new InputStreamReader(as.open("uma.csv")));
            String s;
            while((s = bf.readLine()) != null){
                String [] strAry = s.split(",");
                Log.d("kawasaki_debug","" + strAry[2] + "");
                value.put("NAME", strAry[0]);
                value.put("TEL",strAry[1]);
                value.put("STE3",strAry[2]);
                value.put("STE4",strAry[3]);
                value.put("STE5",strAry[4]);
                //value.put("STE6",strAry[5]);
                //value.put("STE7",strAry[6]);
                //value.put("STE8",strAry[7]);
                //value.put("STE9",strAry[8]);
                //value.put("STE10",strAry[9]);
                mDb.insert("BOOK", null, value);
            }
            String text = "";
            //String sql = "select * from BOOK order by random() limit 2";
            String sql ="SELECT * FROM BOOK ORDER BY RANDOM() LIMIT 1";
            Cursor c = mDb.rawQuery(sql, null);
            boolean isEof = c.moveToFirst();
            while(isEof) {
                text += String.format(c.getString(4));
            isEof = c.moveToNext();
            }

            TextView textSetting = (TextView)findViewById(R.id.textSetting);
            textSetting.setText(text);

        } catch (IOException e) {
            // TODO 自動生成された catch ブロック
            Log.d("kawasaki_debug", "" + e + "");
        }
    }
    //DBクローズ
    @Override
    public void onDestroy(){
        super.onDestroy();
        helper.close();
    }
}
