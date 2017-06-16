package com.smartHome.SmartHomeApp.gui;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.smartHome.SmartHomeApp.R;
import com.smartHome.SmartHomeApp.controller.AlarmReceiver;
import com.smartHome.SmartHomeApp.controller.ReadJson;
import com.smartHome.SmartHomeApp.controller.WriteJson;
import com.smartHome.SmartHomeApp.notifications.IntroductionNotification;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

import static java.util.Calendar.FRIDAY;
import static java.util.Calendar.MONDAY;
import static java.util.Calendar.SATURDAY;
import static java.util.Calendar.SUNDAY;
import static java.util.Calendar.THURSDAY;
import static java.util.Calendar.TUESDAY;
import static java.util.Calendar.WEDNESDAY;

/**
 * Created by Anja on 12.01.2017.
 * the user chooses time and date for the alarm.
 * saves the choosen alarm in an array.
 * the user can delete the alarms
 * TODO find the path for a file to write the alarm 
 */

@SuppressWarnings("deprecation")
public class AlarmPreference extends PreferenceActivity
        implements Preference.OnPreferenceChangeListener {


    public TimePickerDialog.OnTimeSetListener mTimeSetListener;
    public DatePickerDialog.OnDateSetListener datepickerListener;
    private ArrayList<String> empty = new ArrayList<>();
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    private AlarmManager alarmManager;
    private Intent myIntent;
    private PendingIntent pendingIntent_Alarm;

    public static int intentAlarmNumbers = -1;

    private ArrayList<String> alarmListForAlarmListActivity;

    //choose coffe oder light for alarm
    private boolean coffee = false;
    private boolean light = false;
    private String lightCoffee = "";

    private String formattedDate;
    private Calendar calendar;

    private String formattedDayDate;

    //repeat alarm
    private Calendar calMon;
    private Calendar calDi;
    private Calendar calMi;
    private Calendar calDo;
    private Calendar calFr;
    private Calendar calSa;
    private Calendar calSo;

    private final Calendar actDateCalender = Calendar.getInstance();
    private Calendar c = Calendar.getInstance();
    public static int min_x;
    public static int hour_x;
    public static int year_x;
    public static int month_x;
    public static int day_x;

    Preference buttonAlarmAn;

    private Preference buttonList;
    private ReadJson readJson;
    private Preference buttonListDelete;
    private Preference buttonAlarmOn;
    private Preference buttonCoffeeLight;
    private WriteJson writeJson;
    private boolean select = false;
    boolean setSave = false;
    boolean setDay = false;
    boolean setMonth = false;
    boolean setYear = false;

    private static AlarmPreference ourInstance = null;

    public Preference getButtonCoffeeLight() {
        return buttonCoffeeLight;
    }

    public Preference getButtonAlarmOn() {
        return buttonAlarmOn;
    }

    public static AlarmPreference getInstance() {
        if (ourInstance == null) {
            ourInstance = new AlarmPreference();

        } else {
            getInstance();
        }
        return ourInstance;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.alarmpreferences);
        //format for date
        SimpleDateFormat df;
        SimpleDateFormat day;
     
        //GUI
        buttonCoffeeLight = getPreferenceManager().findPreference("prefLightCoffee");
        buttonList = getPreferenceManager().findPreference("preference_AlarmList");
        buttonListDelete = getPreferenceManager().findPreference("preference_AlarmeDelete");
        Preference buttonDate = getPreferenceManager().findPreference("preferenceDate");
        Preference buttonTime = getPreferenceManager().findPreference("preferenceTime");
        buttonAlarmOn = getPreferenceManager().findPreference("prefOn");
        buttonAlarmAn = getPreferenceManager().findPreference("prefOnF");
        final Preference buttonAlarmReg = getPreferenceManager().findPreference("preference_AlarmeRegular");
   
        doFirstRun();
        readJson = ReadJson.getInstance();
        readJson.read(getApplicationContext());
        writeJson = WriteJson.getInstance();

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        //saveDataOn();
        if (alarmListForAlarmListActivity == null) {
            alarmListForAlarmListActivity = new ArrayList<String>();
        }
        //read the old data from json

        if (writeJson != null && readJson.StringToObject() != null) {
            buttonListDelete.setSelectable(true);
            buttonList.setSelectable(true);
            if (!readJson.StringToObject().isEmpty() || !readJson.StringToObject().matches("")) {
                String correctAlarm = readJson.StringToObject();
                String correctAlarm1 = correctAlarm.replace("[", "");
                String correctAlarm2 = correctAlarm1.replace("\"", "");
                String correctAlarm3 = correctAlarm2.replace("]", "");
                alarmListForAlarmListActivity = new ArrayList<String>(Arrays.asList(correctAlarm3));
                Log.d("AlarmPreference", "Not E");
            }
            buttonListDelete.setSelectable(true);
            buttonList.setSelectable(true);
        } else {
            buttonListDelete.setSelectable(true);
            buttonList.setSelectable(true);
        }

        if (!alarmListForAlarmListActivity.isEmpty()) {
            setIntentAlarmNumbers(alarmListForAlarmListActivity.size());
        }
        //format date
        df = new SimpleDateFormat("hh:mm", Locale.GERMANY);
        formattedDate = df.format(c.getTime());
        calendar = Calendar.getInstance();
        day = new SimpleDateFormat("yyyy-dd-MM", Locale.GERMANY);
        formattedDayDate = day.format(c.getTime());

        year_x = actDateCalender.get(Calendar.YEAR);
        month_x = actDateCalender.get(Calendar.MONTH);
        day_x = actDateCalender.get(Calendar.DAY_OF_MONTH);

        if (setSave) {
            buttonAlarmOn.setSelectable(true);
        } else {
            buttonAlarmOn.setSelectable(false);
        }
        /**
         * choose light or coffee settings for alarm
         */
        buttonCoffeeLight.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AlarmPreference.this);

                builder.setTitle("Wähle Alarm für");
                builder.setPositiveButton("Licht", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        setLight(true);
                        isLight();
                        Intent lightIntent = new Intent(getApplicationContext(), LightsActivity.class);
                        startActivity(lightIntent);
                    }
                });
                builder.setNegativeButton("Kaffeemaschine", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        setCoffee(true);
                        isCoffee();
                        Intent coffeeIntent = new Intent(getApplicationContext(), CoffeeAlarmActivity.class);
                        coffeeIntent.putExtra("SetAlarmOn", true);
                        startActivity(coffeeIntent);
                    }
                });
                builder.show();

                return true;

            }
        });

        buttonAlarmAn.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                setAlarm();
                return true;
            }
        });
        /**
         * choose the date
         */
        buttonDate.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                datepickerListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        year_x = year;
                        month_x = month;
                        day_x = day;
                    }
                };
                DatePickerDialog datePickerDialog = new DatePickerDialog(AlarmPreference.this, datepickerListener, year_x, month_x, day_x);
                datePickerDialog.show();
                return true;
            }
        });

        /**
         * choose the time for alarm and save the alarm in json
         */
        buttonTime.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                mTimeSetListener =
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view,
                                                  int hourOfDay, int minute) {
                                hour_x = hourOfDay;
                                min_x = minute;

                            }

                        };
                TimePickerDialog timePickerFragment = new TimePickerDialog(AlarmPreference.this, mTimeSetListener, hour_x, min_x, true);
                timePickerFragment.show();
                return true;
            }
        });


        /**
         * choose which day will be regular alarm
         */
        buttonAlarmReg.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AlarmPreference.this);
                String days[] = {"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag"};
                builder.setTitle("Wähle Wochentag für regelmäßigen Alarm aus");
                builder.setItems(days, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg) {
                        switch (arg) {
                            case 0:
                                Log.d("AlarmActivity", "regelmÃ¤ÃŸig jeden Montag");
                                calMon = (Calendar) calendar.clone();
                                calMon.set(Calendar.DAY_OF_WEEK, MONDAY);
                                Toast.makeText(getApplicationContext(), "Alarm wurde angeschaltet " + hour_x + " : " + min_x, Toast.LENGTH_SHORT).show();
                                if (intentAlarmNumbers >= 0) {
                                    alarmListForAlarmListActivity.add(intentAlarmNumbers, lightCoffee + "Montag regelmÃ¤ÃŸig; Uhrzeit:" + hour_x + ":" + min_x);
                                }
                                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calMon.getTimeInMillis(),
                                        AlarmManager.INTERVAL_DAY, pendingIntent_Alarm);
                                break;
                            case 1:
                                Log.d("AlarmActivity", "regelmÃ¤ÃŸig jeden Dienstag");
                                calDi = (Calendar) calendar.clone();
                                calDi.set(Calendar.DAY_OF_WEEK, TUESDAY);
                                Toast.makeText(getApplicationContext(), "Alarm wurde angeschaltet " + hour_x + " : " + min_x, Toast.LENGTH_SHORT).show();
                                if (intentAlarmNumbers >= 0) {
                                    alarmListForAlarmListActivity.add(intentAlarmNumbers, lightCoffee + "Dienstag regelmÃ¤ÃŸig; Uhrzeit:" + hour_x + ":" + min_x);
                                }
                                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calDi.getTimeInMillis(),
                                        AlarmManager.INTERVAL_DAY, pendingIntent_Alarm);
                                break;
                            case 2:
                                Log.d("AlarmActivity", "regelmÃ¤ÃŸig jeden Mittwoch");
                                calMi = (Calendar) calendar.clone();
                                calMi.set(Calendar.DAY_OF_WEEK, WEDNESDAY);
                                Toast.makeText(getApplicationContext(), "Alarm wurde angeschaltet " + hour_x + " : " + min_x, Toast.LENGTH_SHORT).show();
                                if (intentAlarmNumbers >= 0) {
                                    alarmListForAlarmListActivity.add(intentAlarmNumbers, lightCoffee + "Mittwoch regelmÃ¤ÃŸig; Uhrzeit:" + hour_x + ":" + min_x);
                                }
                                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calMi.getTimeInMillis(),
                                        AlarmManager.INTERVAL_DAY, pendingIntent_Alarm);
                                break;
                            case 3:
                                Log.d("AlarmActivity", "regelmÃ¤ÃŸig jeden Donnerstag");
                                calDo = (Calendar) calendar.clone();
                                calDo.set(Calendar.DAY_OF_WEEK, THURSDAY);
                                Toast.makeText(getApplicationContext(), "Alarm wurde angeschaltet " + hour_x + " : " + min_x, Toast.LENGTH_SHORT).show();
                                if (intentAlarmNumbers >= 0) {
                                    alarmListForAlarmListActivity.add(intentAlarmNumbers, lightCoffee + "Donnerstag regelmÃ¤ÃŸig; Uhrzeit:" + hour_x + ":" + min_x);
                                }
                                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calDo.getTimeInMillis(),
                                        AlarmManager.INTERVAL_DAY, pendingIntent_Alarm);
                                break;
                            case 4:
                                Log.d("AlarmActivity", "regelmÃ¤ÃŸig jeden Freitag");
                                calFr = (Calendar) calendar.clone();
                                calFr.set(Calendar.DAY_OF_WEEK, FRIDAY);
                                Toast.makeText(getApplicationContext(), "Alarm wurde angeschaltet " + hour_x + " : " + min_x, Toast.LENGTH_SHORT).show();
                                if (intentAlarmNumbers >= 0) {
                                    alarmListForAlarmListActivity.add(intentAlarmNumbers, lightCoffee + "Freitag regelmÃ¤ÃŸig; Uhrzeit:" + hour_x + ":" + min_x);
                                }
                                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calFr.getTimeInMillis(),
                                        AlarmManager.INTERVAL_DAY, pendingIntent_Alarm);
                                break;
                            case 5:
                                Log.d("AlarmActivity", "regelmÃ¤ÃŸig jeden Samstag");
                                calSa = (Calendar) calendar.clone();
                                calSa.set(Calendar.DAY_OF_WEEK, SATURDAY);
                                Toast.makeText(getApplicationContext(), "Alarm wurde angeschaltet " + hour_x + " : " + min_x, Toast.LENGTH_SHORT).show();
                                if (intentAlarmNumbers >= 0) {
                                    alarmListForAlarmListActivity.add(intentAlarmNumbers, lightCoffee + "Samstag regelmÃ¤ÃŸig; Uhrzeit:" + hour_x + ":" + min_x);
                                }
                                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calSa.getTimeInMillis(),
                                        AlarmManager.INTERVAL_DAY, pendingIntent_Alarm);
                                break;
                            case 6:
                                Log.d("AlarmActivity", "regelmÃ¤ÃŸig jeden Sonntag");
                                calSo = (Calendar) calendar.clone();
                                calSo.set(Calendar.DAY_OF_WEEK, SUNDAY);
                                Toast.makeText(getApplicationContext(), "Alarm wurde angeschaltet " + hour_x + " : " + min_x, Toast.LENGTH_SHORT).show();
                                if (intentAlarmNumbers >= 0) {
                                    alarmListForAlarmListActivity.add(intentAlarmNumbers, lightCoffee + "Sonntag regelmÃ¤ÃŸig; Uhrzeit:" + hour_x + ":" + min_x);
                                }
                                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calSo.getTimeInMillis(),
                                        AlarmManager.INTERVAL_DAY, pendingIntent_Alarm);
                                break;
                            default:
                                break;
                        }
                    }
                });
                builder.show();
                return true;
            }
        });

        /**
         * show all alarms
         **/
        buttonList.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent AlarmIntent = new Intent(getApplicationContext(), AlarmListActivity.class);
                startActivity(AlarmIntent);
                return true;
            }
        });

        /**
         * delete all alarms
         */
        buttonListDelete.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                if (!alarmListForAlarmListActivity.isEmpty()) {
                    for (int i = 0; i < 100; i++) {
                        myIntent.setData(Uri.parse("custom://" + intentAlarmNumbers));
                        myIntent.setAction(Integer.toString(intentAlarmNumbers));
                        pendingIntent_Alarm = PendingIntent.getActivity(AlarmPreference.this,
                                intentAlarmNumbers, myIntent, 0);
                        pendingIntent_Alarm.cancel();
                        alarmManager.cancel(pendingIntent_Alarm);
                    }

                }
              /*  CoffeeAlarmActivity.getInstance().getCoffeeSettings().clear();
                alarmListForAlarmListActivity.clear();
                setIntentAlarmNumbers(0);
                writeJson.write(empty,getApplicationContext());
                writeJson.writeCoffeeSettings(empty,getApplicationContext());*/
                return true;
            }
        });
    }


    /**
     * save if it is a coffee or a light alarm in a string
     *
     * @return String for List
     */
    protected boolean isCoffee() {
        lightCoffee = "Kaffee ";
        return coffee;
    }

    private void setCoffee(boolean coffee) {
        this.coffee = coffee;
    }

    protected boolean isLight() {
        lightCoffee = "Licht ";
        return light;
    }

    private void setLight(boolean light) {
        this.light = light;
    }

    public int getIntentAlarmNumbers() {
        return intentAlarmNumbers;
    }

    public void setIntentAlarmNumbers(int intentAlarmNumbers) {
        this.intentAlarmNumbers = intentAlarmNumbers;
    }


    //TODO should be tested
    private void saveData(Intent data) {
        alarmManager.cancel(pendingIntent_Alarm);
        //read the old data from json

        ArrayList<String> alarma = data.getStringArrayListExtra("Alarm");
        alarmListForAlarmListActivity.addAll(alarma);

        //split the string and set the alarmJson again that isnt deleted
        for (int i = 0; i < alarma.size(); i++) {
            String[] stringileinchen = alarma.get(i).split(":");
            Log.d("aeras", stringileinchen[1] + stringileinchen[2] + "");
            stringileinchen[1].replaceAll("\\s+", "");
            stringileinchen[2].replaceAll("\\s+", "");
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(stringileinchen[1]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(stringileinchen[2]));
            myIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
            //save each time to an intent
            pendingIntent_Alarm = PendingIntent.getBroadcast(AlarmPreference.this, i, myIntent, 0);
            //pass each intent to the alarmmanger
            if (stringileinchen[0].contains("regelmäßig")) {
                if (stringileinchen[0].contains("Montag")) {
                    calMon = (Calendar) calendar.clone();
                    calMon.set(Calendar.DAY_OF_WEEK, MONDAY);
                }
                if (stringileinchen[0].contains("Dienstag")) {
                    calMon = (Calendar) calendar.clone();
                    calMon.set(Calendar.DAY_OF_WEEK, TUESDAY);
                }
                if (stringileinchen[0].contains("Mittwoch")) {
                    calMon = (Calendar) calendar.clone();
                    calMon.set(Calendar.DAY_OF_WEEK, WEDNESDAY);
                }
                if (stringileinchen[0].contains("Donnerstag")) {
                    calMon = (Calendar) calendar.clone();
                    calMon.set(Calendar.DAY_OF_WEEK, THURSDAY);
                }
                if (stringileinchen[0].contains("Freitag")) {
                    calMon = (Calendar) calendar.clone();
                    calMon.set(Calendar.DAY_OF_WEEK, FRIDAY);
                }
                if (stringileinchen[0].contains("Samstag")) {
                    calMon = (Calendar) calendar.clone();
                    calMon.set(Calendar.DAY_OF_WEEK, SATURDAY);
                }
                if (stringileinchen[0].contains("Sonntag")) {
                    calMon = (Calendar) calendar.clone();
                    calMon.set(Calendar.DAY_OF_WEEK, SUNDAY);
                }
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calMon.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, pendingIntent_Alarm);

            } else {
                myIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
                //save each time to an intent
                pendingIntent_Alarm = PendingIntent.getBroadcast(AlarmPreference.this, i, myIntent, 0);
                //pass each intent to the alarmmanger
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent_Alarm);
                }
            }
        }
        //set the size from the array to the global array
        intentAlarmNumbers = alarma.size();

    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        return false;
    }

    /**
     * choose alarm for light or coffee
     */
    public void setAlarm() {
        myIntent = new Intent(getApplicationContext(), AlarmReceiver.class);

        /**
         * set each time on clicking the button one alarmJson
         **/
        //setting time
        calendar.set(Calendar.HOUR_OF_DAY, hour_x);
        calendar.set(Calendar.MINUTE, min_x);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.YEAR, year_x);
        calendar.set(Calendar.MONTH, month_x);
        calendar.set(Calendar.DAY_OF_MONTH, day_x);

        String[] date = formattedDayDate.split("-");
        //set date alarm
        Integer styear = Integer.parseInt(date[0]);
        Integer stmonth = Integer.parseInt(date[2]);
        Integer stday = Integer.parseInt(date[1]);
        //set clock alarm
        String[] clock = formattedDate.split(":");

        Integer hourAlarm = Integer.parseInt(clock[0]);
        Integer minute = Integer.parseInt(clock[1]);
        String minute_string = "";
        Integer hour;

        if (hourAlarm < 10) {
            minute_string = "0" + clock[1];
            hour = Integer.parseInt(minute_string);
        } else {
            hour = hourAlarm;
        }
        saveAlarmInArray();
        //exam if the day,month, year is not in past
        if (styear < year_x && stmonth == month_x && stday == day_x || (styear == year_x && stmonth < month_x && stday == day_x) || (stmonth == month_x && styear == year_x && stday > day_x)) {
            Log.d("Alarm", "Datum liegt in der Vergangenheit");
            Toast.makeText(getApplicationContext(), "Datum liegt in der Vergangenheit, Alarm wurde auf den nächsten Tag gesetzt", Toast.LENGTH_SHORT).show();
        } //else {
        //exam if the hour is not in the past of the actual day
        if ((hourAlarm < hour_x && stmonth == month_x && styear == year_x && stday == day_x) || (minute <= min_x && hour == hour_x && stmonth == month_x && styear == year_x && stday == day_x)) {
            //TODO alarmMillis += 86400000L;

            Toast.makeText(getApplicationContext(), "Uhrzeit liegt in der Vergangenheit, Alarm wurde auf den nächsten Tag gesetzt", Toast.LENGTH_SHORT).show();
            Log.d("aktu Zeit", date + "");
            calendar.add(Calendar.DAY_OF_MONTH, 24);
        }

        Log.d("MyActivity", "Alarm On");
        myIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
        myIntent.setData(Uri.parse("custom://" + intentAlarmNumbers));
        myIntent.setAction(Integer.toString(intentAlarmNumbers));
        //save each time to an intent
        pendingIntent_Alarm = PendingIntent.getBroadcast(AlarmPreference.this, intentAlarmNumbers, myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        //add all alarms to a list
        alarmListForAlarmListActivity.add(intentAlarmNumbers, " " + lightCoffee + "Datum " + year_x + "-" + month_x + "-" + day_x + " Uhrzeit:" + hour_x + ":" + min_x);
        //set alarm
        if (android.os.Build.VERSION.SDK_INT >= 19) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent_Alarm);
        } else {

        }
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent_Alarm);
        Log.d("Alarm ", intentAlarmNumbers + " |" + calendar);
        //pass each intent to the alarmmanger
        Log.d("AlarmPreference", "" + intentAlarmNumbers);

        Toast.makeText(getApplicationContext(), "Alarm wurde angeschaltet", Toast.LENGTH_SHORT).show();

        if (alarmListForAlarmListActivity != null && alarmListForAlarmListActivity.size() > 1)

        {
            /**
             *   exam if the alarm exists already in the list

             for (int i = 0; i <= intentAlarmNumbers - 2; i++) {
             if (alarmListForAlarmListActivity.get(intentAlarmNumbers - 1).equals(alarmListForAlarmListActivity.get(i))) {
             Toast.makeText(getApplicationContext(), "Alarm existiert bereits", Toast.LENGTH_SHORT).show();
             int lol = intentAlarmNumbers - 1;
             myIntent.setData(Uri.parse("custom://" + lol));
             myIntent.setAction(Integer.toString(lol));
             pendingIntent_Alarm = PendingIntent.getActivity(AlarmPreference.this,
             lol, myIntent, 0);
             pendingIntent_Alarm.cancel();
             alarmManager.cancel(pendingIntent_Alarm);
             //if alarm is duplicate then delete it
             int lastAlarm = alarmListForAlarmListActivity.size();
             alarmListForAlarmListActivity.remove(lastAlarm - 1);
             intentAlarmNumbers = alarmListForAlarmListActivity.size();
             }
             }
             }
             **/
            empty.clear();
            writeJson.write(empty, getApplicationContext());
            if (!alarmListForAlarmListActivity.isEmpty()) {
                writeJson.write(alarmListForAlarmListActivity, getApplicationContext());
            }
        }
    }

    public void saveAlarmInArray() {

        if (getIntentAlarmNumbers() == -1) {
            setIntentAlarmNumbers(0);
        }
        if (getIntentAlarmNumbers() >= 0) {
            if (getIntentAlarmNumbers() == 100) {
                setIntentAlarmNumbers(0);
                Log.d("Alarm", "alte Alarme werden gelöscht");
                Toast.makeText(getApplicationContext(), "alte Alarme werden gelöscht", Toast.LENGTH_SHORT).show();
            }
            alarmListForAlarmListActivity.add(intentAlarmNumbers, " " + lightCoffee + "Datum " + year_x + "-" + month_x + "-" + day_x + " Uhrzeit:" + hour_x + ":" + min_x);
        }
        Log.d("", "");
        intentAlarmNumbers++;
    }


    /**
     * checks, if it's the first time the app runs
     * and show the introduction dialog
     */
    private void doFirstRun() {
        sharedPreferences = getSharedPreferences("MainActivity", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("isFirstRun", true)) {
            IntroductionNotification.introductionAlarmDialog(AlarmPreference.this);
            editor = sharedPreferences.edit();
            editor.putBoolean("isFirstRun", false);
            editor.commit();
        }
    }

    /**
     * save the list if the user returns from the a activity
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // saveDataOn();
        //saveDAta
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //   saveDataOn();
    }

    @Override
    protected void onRestoreInstanceState(Bundle outState) {
        super.onRestoreInstanceState(outState);
        saveDataOn();
    }

    private void saveDataOn() {
        buttonListDelete.setSelectable(true);
        buttonList.setSelectable(true);

        alarmListForAlarmListActivity = new ArrayList<String>();
        //read the old data from json
        if (writeJson != null || !alarmListForAlarmListActivity.isEmpty() || readJson != null) {
            if (readJson != null || !readJson.StringToObject().isEmpty() || !readJson.StringToObject().matches("")) {
                String correctAlarm = readJson.StringToObject();
                String correctAlarm1 = correctAlarm.replace("[", "");
                String correctAlarm2 = correctAlarm1.replace("\"", "");
                String correctAlarm3 = correctAlarm2.replace("]", "");
                alarmListForAlarmListActivity = new ArrayList<String>(Arrays.asList(correctAlarm3));
                Log.d("File", "Not E");
            }
        }
        if (!alarmListForAlarmListActivity.isEmpty()) {
            intentAlarmNumbers = alarmListForAlarmListActivity.size();
        }

    }

}
