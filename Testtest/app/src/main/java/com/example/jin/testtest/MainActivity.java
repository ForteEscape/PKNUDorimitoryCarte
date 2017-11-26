package com.example.jin.testtest;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String[][] menu;
    private static int ONE_MINUTE = 5626;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new AlarmHATT(getApplicationContext()).Alarm();

        final Button happy = findViewById(R.id.button3);
        final Button pknu = findViewById(R.id.button4);
        final TextView tv = findViewById(R.id.morning);
        final TextView tv2 = findViewById(R.id.lunch);
        final TextView tv3 = findViewById(R.id.dinner);
        final TextView td = findViewById(R.id.today);
        final TextView td1 = findViewById(R.id.textView);

        Calendar cal = Calendar.getInstance();
        int year = cal.get(cal.YEAR);
        int month = cal.get(cal.MONTH) + 1;
        int date = cal.get(cal.DATE);
        td.setText(" : " + year + "년 " + month + "월 " + date + "일 ");
        //디자인
        happy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    sendPost();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        pknu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread thread = new Thread() {
                    public void run() {
                        try {
                            int idx = 0;
                            List<String> list = new ArrayList<>();
                            menu = new String[1000][1000];
                            String address = "http://dormitory.pknu.ac.kr/03_notice/req_getSchedule.php";
                            org.jsoup.nodes.Document document = Jsoup.connect(address).get();
                            Elements elements = document.select("td");
                            for (org.jsoup.nodes.Element element : elements) {
                                list.add(element.text());
                            }

                            for (int i = 0; i < 3; i++) {
                                for (int j = 0; j < 8; j++) {
                                    menu[j][i] = list.get(idx);
                                    idx++;
                                }
                            }
                            idx = 0;
                            for (int i = 0; i < 3; i++) {
                                for (int j = 0; j < 8; j++) {
                                    System.out.println(menu[j][i]);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Calendar cal = Calendar.getInstance();
                        final String[] week = {"일", "월", "화", "수", "목", "금", "토"};
                        int dayday = cal.get(Calendar.DAY_OF_WEEK);
                        System.out.println("dayday" + dayday);
                        switch (dayday) {
                            case 1:
                                tv.setText(": " + menu[7][0]);
                                tv2.setText(": " + menu[7][1]);
                                tv3.setText(": " + menu[7][2]);
                                break;
                            case 2:
                                tv.setText("" + menu[1][0]);
                                tv2.setText("" + menu[1][1]);
                                tv3.setText("" + menu[1][2]);
                                break;
                            case 3:
                                tv.setText("" + menu[2][0]);
                                tv2.setText("" + menu[2][1]);
                                tv3.setText("" + menu[2][2]);
                                break;
                            case 4:
                                tv.setText("" + menu[3][0]);
                                tv2.setText("" + menu[3][1]);
                                tv3.setText("" + menu[3][2]);
                                break;
                            case 5:
                                tv.setText("" + menu[4][0]);
                                tv2.setText("" + menu[4][1]);
                                tv3.setText("" + menu[4][2]);
                                break;
                            case 6:
                                tv.setText("" + menu[5][0]);
                                tv2.setText("" + menu[5][1]);
                                tv3.setText("" + menu[5][2]);
                                break;
                            case 7:
                                tv.setText("" + menu[6][0]);
                                tv2.setText("" + menu[6][1]);
                                tv3.setText("" + menu[6][2]);
                                break;

                        }
                        td1.setText("식사 맛있게 하십시오 ^^*");
                        System.out.println("오늘 식단은 : " + tv.getText().toString());
                    }
                }, 3000);// 3초 정도 딜레이

            }
        });
    }

    private void sendPost() throws Exception {
        final TextView tv = findViewById(R.id.morning);
        final TextView tv2 = findViewById(R.id.lunch);
        final TextView tv3 = findViewById(R.id.dinner);
        final TextView td = findViewById(R.id.today);
        final TextView td1 = findViewById(R.id.textView);


        new Thread() {
            public void run() {
                try {
                    Calendar cal = Calendar.getInstance();
                    final String[] week = {"일", "월", "화", "수", "목", "금", "토"};
                    final int dayday = cal.get(Calendar.DAY_OF_WEEK);
                    System.out.println("dayday" + dayday);
                    String url = "https://busan.happydorm.or.kr/busan/food/getWeeklyMenu.kmc";
                    String USER_AGENT = "Mozilla/5.0";
                    URL obj = new URL(url);
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                    con.setDoOutput(true);
                    con.setRequestMethod("POST");
                    con.setRequestProperty("User-Agent", USER_AGENT);
                    String urlParameters = "locgbn=DD&sch_date=";
                    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                    wr.writeBytes(urlParameters);
                    wr.flush();
                    wr.close();
                    int responseCode = con.getResponseCode();
                    System.out.println("Sending 'POST' request to URL : " + url);
                    System.out.println("Post parameters : " + urlParameters);
                    System.out.println("Response Code : " + responseCode);

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    String response = new String();

                    while ((inputLine = in.readLine()) != null) {
                        response += inputLine;
                    }
                    in.close();
                    JsonParser jsonParser = new JsonParser();
                    JsonElement jsonElement = jsonParser.parse(response);
                    JsonObject jsonObject = (JsonObject) jsonElement;
                    JsonArray jsonObject1 = (JsonArray) jsonObject.get("root");
                    JsonObject jsonObject2 = (JsonObject) jsonObject1.get(0);
                    JsonArray jsonArray = (JsonArray) jsonObject2.get("WEEKLYMENU");
                    final JsonObject jsonObject3 = (JsonObject) jsonArray.get(0);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // 해당 작업을 처리함
                            tv.setText(jsonObject3.get("fo_menu_mor" + dayday).toString());
                            tv2.setText(jsonObject3.get("fo_menu_lun" + dayday).toString());
                            tv3.setText(jsonObject3.get("fo_menu_eve" + dayday).toString());
                            //식사맛있게하세요
                            td1.setText("식사 맛있게 하십시오 ^^*");
                        }
                    });


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public class AlarmHATT{
        private Context context;
        public AlarmHATT(Context context){
            this.context = context;
        }
        public void Alarm(){
            AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(MainActivity.this, BroadcastD.class);

            PendingIntent sender = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);

            Calendar calendar = Calendar.getInstance();
            //알람시간 calendar에 set해주기

            
            calendar.set(calendar.get(Calendar.YEAR)+0, calendar.get(Calendar.MONTH)+0, calendar.get(Calendar.DATE)+0, 8, 0, 0);
            calendar.set(calendar.get(Calendar.YEAR)+0, calendar.get(Calendar.MONTH)+0, calendar.get(Calendar.DATE)+0, 12, 30, 0);
            calendar.set(calendar.get(Calendar.YEAR)+0, calendar.get(Calendar.MONTH)+0, calendar.get(Calendar.DATE)+0, 18, 0, 0);
            Log.e("날짜 : ", String.valueOf(calendar.get(Calendar.DATE)+0));
            //알람 예약
            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);

        }
    }

};