package com.android.shagogo;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class PedometerSharedPreferences {

    // записать текущие шаги и значения времени
    public static void WriteCurrentStepsAndTime(Context context) {
        String mCurrentDate;
        JSONObject jsonObject = null;
        SharedPreferences mSharedPreferences;

        mSharedPreferences = context.getSharedPreferences(context.getResources().
                getString(R.string.SharedPreferences_MainKey), Context.MODE_PRIVATE);
        Date date = new Date();
        // формат day - month - year
        mCurrentDate = "";
        mCurrentDate += String.valueOf(date.getDate());
        mCurrentDate += String.valueOf(date.getMonth());
        mCurrentDate += String.valueOf(date.getYear() + 1900);

        // загрузить данные из service
        int steps = PedometerService.getSteps();
        int time = PedometerService.getTotalTime();
        if (mSharedPreferences.contains(mCurrentDate) == false) {
            // пока нет preference на этт день, сбросить данные в service (новое вычисление)
            if (date.getHours() == 0) { // полночь, новый день
                PedometerService.resetTime();
                PedometerService.resetSteps(context);
                time = steps = 0;
            }
        } else {
            // preference уже есть, прочитайте его, а затем запишите данные
            String string = mSharedPreferences.getString(mCurrentDate, null);
            try {
                jsonObject = new JSONObject(string);
            } catch (JSONException e) {
                e.getStackTrace();
            }
        }
        if (jsonObject == null) {
            jsonObject = new JSONObject();
        }
        try {
            jsonObject.put(String.valueOf(date.getHours()), steps);
        } catch (JSONException e) {
            e.getStackTrace();
        }
        Log.d("PedometerPreferences", "saved jsonObject: " + jsonObject);
        Log.d("PedometerPreferences", "saved date: " + mCurrentDate);
        mSharedPreferences.edit().putString(mCurrentDate, jsonObject.toString()).commit();
        mSharedPreferences.edit().putInt(mCurrentDate + "t", time).commit();
    }

    // читать (и записать, если нужно) шаги и значения времени за день
    public static int[] ReadStepsAndTime(Context context, String Date) {
        int[] stepsAndTime = new int[25];
        SharedPreferences mSharedPreferences;
        JSONObject jsonObject = new JSONObject();

        Log.d("PedometerPreferences", "read date: " + Date);
        mSharedPreferences = context.getSharedPreferences(context.getResources().
                getString(R.string.SharedPreferences_MainKey), Context.MODE_PRIVATE);
        if (mSharedPreferences.contains(Date) == false) {
            Log.d("PedometerPreferences", "no preference for this day yet, all is zero");
            for (int i = 0; i < 25; i++) {

                stepsAndTime[i] = 0;
            }
        } else {
            String string = mSharedPreferences.getString(Date, null);
            Log.d("PedometerPreferences", "preference: " +string);
            try {
                jsonObject = new JSONObject(string);
            } catch (JSONException e) {
                e.getStackTrace();
            }
            // load steps
            for (int i = 0; i < 24; i++) {
                if (jsonObject.has(String.valueOf(i)) == true) {
                    try {
                        stepsAndTime[i] = jsonObject.getInt(String.valueOf(i));
                    } catch (JSONException e) {
                        e.getStackTrace();
                    }
                } else {
                    stepsAndTime[i] = 0;
                }
            }
            stepsAndTime[24] = mSharedPreferences.getInt(Date + "t", 0);
        }
        return stepsAndTime;
    }

    // отсчистить все preferences
    public static void ClearAllPreferences(Context context) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(context.getResources().
                getString(R.string.SharedPreferences_MainKey), Context.MODE_PRIVATE);
        mSharedPreferences.edit().clear().commit();
    }
}
