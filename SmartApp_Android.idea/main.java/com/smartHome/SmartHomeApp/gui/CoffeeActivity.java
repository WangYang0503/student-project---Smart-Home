package com.smartHome.SmartHomeApp.gui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.smartHome.SmartHomeApp.R;
import com.smartHome.SmartHomeApp.kaa.KaaManager;
import com.smartHome.SmartHomeApp.kaa.eventHandler.CoffeeMachineEventHandler;
import com.smartHome.SmartHomeApp.notifications.CoffeeNotifications;

import java.util.ArrayList;
import java.util.Calendar;

import smart_things.coffee.schema.InfoObject;
import smart_things.coffee.schema.StatusTypes;
import smart_things.coffee.schema.StrengthTypes;
import smart_things.coffee.schema.WaterLevelTypes;


/**
 * Created by Lia on 29.08.2016.
 */
public class CoffeeActivity extends AppCompatActivity {

    private static EditText editCup;
    /**
     * Singleton class TODO for...
     */
    private static CoffeeActivity ourInstance = new CoffeeActivity();
    // for datepicker and timepicker
    private int year_x, month_x, day_x = 0;
    private int min_x, hour_x = 0;
    private RadioButton radioButtonFilter;
    private RadioButton radioButtonGrinder;
    private RadioButton radioButtonMedium;
    private RadioButton radioButtonStrong;
    private RadioButton radioButtonWeak;
    private Button cookingCoffeeButton;
    private Button buttonKAbbrechen;



    /**
     * Singleton class TODO for...
     */


    public static CoffeeActivity getInstance() {
        return ourInstance;
    }

    private DatePickerDialog.OnDateSetListener datepListener
            = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            year_x = year;
            month_x = month + 1;
            day_x = day;
            Log.i("", "Das Datum " + year_x + " " + month_x + " " + day_x + " wurde gesetzt-");
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coffeemashine_layout);

        radioButtonAction();
        cupAmount();
        buttonStart();
        buttonAbbrechen();
        updateWaterLvlDisplay();
        enableHotPlate();
        calendarSettings();

        // preselect current active coffee settings
        updateRadioButtons();

        cookingCoffeeButton.setEnabled(true);
        buttonKAbbrechen.setEnabled(false);
        cookingCoffeeButton.setAlpha(1f);
        buttonKAbbrechen.setAlpha(0.5f);

    }

    /**
     * for setting the time for coffee making
     */
    private void calendarSettings() {

        final Calendar c = Calendar.getInstance();
        year_x = c.get(Calendar.YEAR);
        month_x = c.get(Calendar.MONTH);
        day_x = c.get(Calendar.DAY_OF_MONTH);
        min_x = c.get(Calendar.MINUTE);
        hour_x = c.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * enables the heating of the heating plate
     * the duration is received from the user
     */

    public void enableHotPlate() {
        Button buttonPlate = (Button) findViewById(R.id.HeatplateButton);
        buttonPlate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //ask the status of the coffeemaschine, beacause of safety aspects
                if (KaaManager.getCoffeeMachineEventHandler().getCoffeeStatus().getStatus() == StatusTypes.UNKNOWN) {
                    Toast.makeText(getApplicationContext(), "Kaffeemaschine kann gerade nicht benutzt wertden. Warten sie einen Moment.", Toast.LENGTH_SHORT).show();
                }

                if (KaaManager.getCoffeeMachineEventHandler().getCoffeeStatus().getStatus() == StatusTypes.BREWING ||
                        KaaManager.getCoffeeMachineEventHandler().getCoffeeStatus().getStatus() == StatusTypes.GRINDING) {
                    CoffeeNotifications.alreadyBrewing();
                } else {
                    Toast.makeText(getApplicationContext(), "Heißplatte wurde angeschaltet", Toast.LENGTH_LONG);
                    //get the value of minutes to heat the plate
                    final EditText input = new EditText(CoffeeActivity.this);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    input.setLayoutParams(lp);

                    new AlertDialog.Builder(CoffeeActivity.this)
                            .setView(input)
                            .setTitle("Heizplatte")
                            .setMessage("Wie lange soll der Kaffee warmgehalten werden? min 4  max 45")

                            .setPositiveButton(R.string.alert_dialog_ok,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {

                                            int minutes = Integer.parseInt(input.getText().toString());
                                            if (minutes < 5 && minutes > 45) {
                                                KaaManager.getCoffeeMachineEventHandler().sendHotPlateTypeEvent(minutes);
                                            }
                                        }
                                    })
                            .show();
                }
            }
        });

        //time  4 until 45min
        Log.d("Kaffeemaschine", "Heizplatte an");

    }

    /**
     * shows the actual amount of whater in the coffeemashine
     * there are 4 different states for the water lvl
     */
    public void updateWaterLvlDisplay() {
        TextView textViewLvl1 = (TextView) findViewById(R.id.textViewWaterlvl1);
        TextView textViewLvl2 = (TextView) findViewById(R.id.textViewWaterlvl2);
        TextView textViewLvl3 = (TextView) findViewById(R.id.textViewWaterlvl3);

        textViewLvl1.setBackground(getResources().getDrawable(R.drawable.borderempty));
        textViewLvl2.setBackground(getResources().getDrawable(R.drawable.borderempty));
        textViewLvl3.setBackground(getResources().getDrawable(R.drawable.borderempty));
        if (KaaManager.isConnected() && KaaManager.getCoffeeMachineEventHandler().getCoffeeStatus() != null) {

            // show waterlvl empty

            if (KaaManager.getCoffeeMachineEventHandler().getCoffeeStatus().getWaterLevel().equals(WaterLevelTypes.EMPTY)) {
                textViewLvl1.setBackground(getResources().getDrawable(R.drawable.borderempty));
                textViewLvl2.setBackground(getResources().getDrawable(R.drawable.borderempty));
                textViewLvl3.setBackground(getResources().getDrawable(R.drawable.borderempty));
                Log.d("Kaffeemaschine", "Wasserlvl 0");
            }
            //show waterlvl 3
            if (KaaManager.getCoffeeMachineEventHandler().getCoffeeStatus().getWaterLevel().equals(WaterLevelTypes.FULL)) {
                textViewLvl1.setBackground(getResources().getDrawable(R.drawable.border));
                textViewLvl2.setBackground(getResources().getDrawable(R.drawable.border));
                textViewLvl3.setBackground(getResources().getDrawable(R.drawable.border));
                Log.d("Kaffeemaschine", "Wasserlvl voll");
            }
            // showWater lvl 2
            if (KaaManager.getCoffeeMachineEventHandler().getCoffeeStatus().getWaterLevel().equals(WaterLevelTypes.HALF)) {
                textViewLvl1.setBackground(getResources().getDrawable(R.drawable.border));
                textViewLvl2.setBackground(getResources().getDrawable(R.drawable.border));
                textViewLvl3.setBackground(getResources().getDrawable(R.drawable.borderempty));
                Log.d("Kaffeemaschine", "Wasserlvl Hälfte");
            }
            //showWater lvl 1
            if (KaaManager.getCoffeeMachineEventHandler().getCoffeeStatus().getWaterLevel().equals(WaterLevelTypes.LESS)) {
                textViewLvl1.setBackground(getResources().getDrawable(R.drawable.border));
                textViewLvl3.setBackground(getResources().getDrawable(R.drawable.borderempty));
                textViewLvl3.setBackground(getResources().getDrawable(R.drawable.borderempty));
                Log.d("Kaffeemaschine", "Wasserlvl Wenig");
            }
        }

    }

    /**
     * checks the right radio buttons according to what is currently selected at the coffeeMachine
     */
    public void updateRadioButtons() {
        CoffeeMachineEventHandler cmeh = KaaManager.getCoffeeMachineEventHandler();
        if (cmeh != null) {
            InfoObject coffeeMachineStatus = cmeh.getCoffeeStatus();
            if (coffeeMachineStatus != null) {
                if (coffeeMachineStatus.getFilter()) {
                    radioButtonGrinder.setChecked(false);
                    radioButtonFilter.setChecked(true);
                } else {
                    radioButtonFilter.setChecked(false);
                    radioButtonGrinder.setChecked(true);
                }
                switch (coffeeMachineStatus.getStrength()) {
                    case WEAK:
                        radioButtonWeak.setChecked(true);
                        break;
                    case MEDIUM:
                        radioButtonMedium.setChecked(true);
                        break;
                    case STRONG:
                        radioButtonStrong.setChecked(true);
                        break;
                    default:
                        radioButtonWeak.setChecked(false);
                        radioButtonMedium.setChecked(false);
                        radioButtonStrong.setChecked(false);
                }

            } else {
                // no coffeeMachineStatus available
                // deselect all radio buttons
                radioButtonFilter.setChecked(false);
                radioButtonGrinder.setChecked(false);
                radioButtonWeak.setChecked(false);
                radioButtonMedium.setChecked(false);
                radioButtonStrong.setChecked(false);
            }

            // TODO: Bug, exception is thrown on next line
            // editCup.setText(cupNumber);

        } else {
            // no coffeeMachineStatus available
            // deselect all radio buttons
            radioButtonFilter.setChecked(false);
            radioButtonGrinder.setChecked(false);
            radioButtonWeak.setChecked(false);
            radioButtonMedium.setChecked(false);
            radioButtonStrong.setChecked(false);
        }
    }

    //TODO do we need it?
    private void updateCupAmount() {

    }

    /**
     * This method allows the user to select how many coffee one wants to cook
     * The amount is described with cups. One can cook coffe for max 12 cups.
     * when the water lvl is lover than the amount of cups one wants to make a default amount depending
     * on the actual water lvl
     */
    public void cupAmount() {
        editCup = (EditText) findViewById(R.id.numberCupText);

        editCup.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                String numbersOfCup = editCup.getText().toString().trim();
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    //ask the status because of the coffeemaschine, beacause of safety aspects
                    if (KaaManager.getCoffeeMachineEventHandler().getCoffeeStatus().getStatus() == StatusTypes.UNKNOWN) {
                        Toast.makeText(getApplicationContext(), "Kaffeemaschine kann gerade nicht benutzt wertden. Warten sie einen Moment.", Toast.LENGTH_SHORT).show();
                    }

                    if (KaaManager.getCoffeeMachineEventHandler().getCoffeeStatus().getStatus() == StatusTypes.BREWING ||
                            KaaManager.getCoffeeMachineEventHandler().getCoffeeStatus().getStatus() == StatusTypes.GRINDING) {

                    } else {
                        if (KaaManager.getCoffeeMachineEventHandler().getCoffeeStatus().getWaterLevel().equals(WaterLevelTypes.EMPTY)) {
                            editCup.setText("0");
                        }
                        if (KaaManager.getCoffeeMachineEventHandler().getCoffeeStatus().getWaterLevel().equals(WaterLevelTypes.LESS)) {
                            if (Integer.parseInt(numbersOfCup) <= 3) {
                                editCup.setText(numbersOfCup);
                            } else {
                                CoffeeNotifications.HighCupNumber(CoffeeActivity.this);
                                editCup.setText("3");
                                numbersOfCup = "3";
                            }
                        }
                        if (KaaManager.getCoffeeMachineEventHandler().getCoffeeStatus().getWaterLevel().equals(WaterLevelTypes.HALF)) {
                            if (Integer.parseInt(numbersOfCup) <= 6) {
                                editCup.setText(numbersOfCup);
                            } else {
                                CoffeeNotifications.HighCupNumber(CoffeeActivity.this);
                                editCup.setText("6");
                                numbersOfCup = "6";
                            }
                        }
                        if (KaaManager.getCoffeeMachineEventHandler().getCoffeeStatus().getWaterLevel().equals(WaterLevelTypes.FULL)) {
                            if (Integer.parseInt(numbersOfCup) > 12) {
                                CoffeeNotifications.HighCupNumber(CoffeeActivity.this);
                                editCup.setText("12");
                                numbersOfCup = "12";
                            }
                        }


                        if (Integer.parseInt(numbersOfCup) == 0) {
                            CoffeeNotifications.LowCupNumber(CoffeeActivity.this);
                            editCup.setText("1");
                            numbersOfCup = "1";
                        }

                        KaaManager.getCoffeeMachineEventHandler().sendSetCupCountEvent
                                (Integer.parseInt(numbersOfCup));
                    }
                }
                return false;
            }
        });
    }

    /**
     * On click Method for the Kaffee Eingießen Button.
     * makes possible to use the Button so the coffee machine starts working if button was clicked
     * and reads the number of cups that it should produce
     */
    public void buttonStart() {
        cookingCoffeeButton = (Button) findViewById(R.id.cookCoffeeButton);
        cookingCoffeeButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        final String numbersOfCup = editCup.getText().toString().trim();

                        //ask the status because of the coffeemaschine, beacause of safety aspects
                        //only brewing coffee when coffeemaschine is available
                        if (KaaManager.getCoffeeMachineEventHandler().getCoffeeStatus().getStatus() == StatusTypes.READY) {
                            KaaManager.getCoffeeMachineEventHandler().sendStartBrewingEvent();
                        }

                        if (KaaManager.getCoffeeMachineEventHandler().getCoffeeStatus().getStatus() == StatusTypes.BREWING ||
                                KaaManager.getCoffeeMachineEventHandler().getCoffeeStatus().getStatus() == StatusTypes.GRINDING) {
                            CoffeeNotifications.alreadyBrewing();
                        }
                        if (KaaManager.getCoffeeMachineEventHandler().getCoffeeStatus().getStatus() == StatusTypes.UNKNOWN) {
                            Toast.makeText(getApplicationContext(), "Kaffeemaschine kann gerade nicht benutzt wertden. Warten sie einen Moment.", Toast.LENGTH_SHORT).show();
                        }
                        cookingCoffeeButton.setEnabled(false);
                        buttonKAbbrechen.setEnabled(true);
                        cookingCoffeeButton.setAlpha(0.5f);
                        buttonKAbbrechen.setAlpha(1f);
                        Log.d("Kaffeemaschine", "Starten");
                    }
                });
    }

    /**
     * On click Method for the  Vorgang Abbrechen Button.
     * makes possible to use the Button so the coffee machine stops working if button was
     * clicked
     */
    public void buttonAbbrechen() {
        buttonKAbbrechen = (Button) findViewById(R.id.buttonKAbbrechen);

        buttonKAbbrechen.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        KaaManager.getCoffeeMachineEventHandler().sendStopBrewingEvent();
                        cookingCoffeeButton.setEnabled(true);
                        buttonKAbbrechen.setEnabled(false);
                        cookingCoffeeButton.setAlpha(1f);
                        buttonKAbbrechen.setAlpha(0.5f);
                        Log.d("Kaffeemaschine", "Abbrechen");
                    }
                });

    }

    /**
     * Method for the different radio button Activities
     */
    public void radioButtonAction() {
        radioButtonWeak = (RadioButton) findViewById(R.id.strengthWeakRadioButton);
        radioButtonMedium = (RadioButton) findViewById(R.id.strengthMediumRadioButton);
        radioButtonStrong = (RadioButton) findViewById(R.id.strengthStrongRadioButton);
        radioButtonFilter = (RadioButton) findViewById(R.id.filterRadioButton);
        radioButtonGrinder = (RadioButton) findViewById(R.id.grinderRadioButton);
        radioButtonWeak.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        //ask the status because of the coffeeMachine, because of safety aspects
                        if (KaaManager.getCoffeeMachineEventHandler().getCoffeeStatus().getStatus() == StatusTypes.UNKNOWN) {
                            Toast.makeText(getApplicationContext(), "Kaffeemaschine kann gerade nicht benutzt wertden." +
                                    "Warten sie einen Moment.", Toast.LENGTH_SHORT).show();
                        }

                        if (KaaManager.getCoffeeMachineEventHandler().getCoffeeStatus().getStatus() == StatusTypes.BREWING ||
                                KaaManager.getCoffeeMachineEventHandler().getCoffeeStatus().getStatus() == StatusTypes.GRINDING) {
                            CoffeeNotifications.alreadyBrewing();
                        } else {
                            KaaManager.getCoffeeMachineEventHandler().sendSetStrengthEvent(StrengthTypes.WEAK);
                        }

                    }
                });
        radioButtonMedium.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        //ask the status because of the coffeeMachine, because of safety aspects
                        if (KaaManager.getCoffeeMachineEventHandler().getCoffeeStatus().getStatus() == StatusTypes.UNKNOWN) {
                            Toast.makeText(getApplicationContext(), "Kaffeemaschine kann gerade nicht benutzt wertden." +
                                    "Warten sie einen Moment.", Toast.LENGTH_SHORT).show();

                        }

                        if (KaaManager.getCoffeeMachineEventHandler().getCoffeeStatus().getStatus() == StatusTypes.BREWING ||
                                KaaManager.getCoffeeMachineEventHandler().getCoffeeStatus().getStatus() == StatusTypes.GRINDING) {
                            CoffeeNotifications.alreadyBrewing();
                        } else {
                            KaaManager.getCoffeeMachineEventHandler().sendSetStrengthEvent(StrengthTypes.MEDIUM);
                        }

                    }
                });
        radioButtonStrong.setOnClickListener(
                new Button.OnClickListener()

                {
                    public void onClick(View v) {
                        //ask the status because of the coffeeMachine, because of safety aspects
                        if (KaaManager.getCoffeeMachineEventHandler().getCoffeeStatus().getStatus() == StatusTypes.UNKNOWN) {
                            Toast.makeText(getApplicationContext(), "Kaffeemaschine kann gerade nicht benutzt wertden." +
                                    "Warten sie einen Moment.", Toast.LENGTH_SHORT).show();
                        }

                        if (KaaManager.getCoffeeMachineEventHandler().getCoffeeStatus().getStatus() == StatusTypes.BREWING ||
                                KaaManager.getCoffeeMachineEventHandler().getCoffeeStatus().getStatus() == StatusTypes.GRINDING) {
                            CoffeeNotifications.alreadyBrewing();
                        } else {
                            KaaManager.getCoffeeMachineEventHandler().sendSetStrengthEvent(StrengthTypes.STRONG);
                        }


                    }
                });

        //radio Button for the filter and grinder
        radioButtonFilter.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ask the status because of the coffeeMachine, because of safety aspects
                if (KaaManager.getCoffeeMachineEventHandler().getCoffeeStatus().getStatus() == StatusTypes.UNKNOWN) {
                    Toast.makeText(getApplicationContext(),
                            "Kaffeemaschine kann gerade nicht benutzt wertden. Warten sie einen Moment.", Toast.LENGTH_SHORT).show();
                }
                if (KaaManager.getCoffeeMachineEventHandler().getCoffeeStatus().getStatus() == StatusTypes.BREWING ||
                        KaaManager.getCoffeeMachineEventHandler().getCoffeeStatus().getStatus() == StatusTypes.GRINDING) {
                    CoffeeNotifications.alreadyBrewing();
                }

                // only change brewing type if device is connected to kaa
                if (KaaManager.isConnected()) {
                    InfoObject coffeeMachineStatus = KaaManager.getCoffeeMachineEventHandler()
                            .getCoffeeStatus();
                    if (coffeeMachineStatus != null
                            && !coffeeMachineStatus.getFilter()) {
                        // grinder currently selected
                        KaaManager.getCoffeeMachineEventHandler().sendToggleBrewingTypeEvent();
                        // temporary set the filter type to prevent toggling before the updated coffeeMachineStatus
                        // has been received from the coffeeServer
                        coffeeMachineStatus.setFilter(true);
                    }
                    Log.d("CoffeeActivity", "Filter selected");
                } else {
                    // TODO: decide what todo if kaa is not connected
                }
            }
        });

        radioButtonGrinder.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ask the status because of the coffeeMachine, because of safety aspects
                if (KaaManager.getCoffeeMachineEventHandler().getCoffeeStatus().getStatus() ==
                        StatusTypes.UNKNOWN) {
                    Toast.makeText(getApplicationContext(),
                            "Kaffeemaschine kann gerade nicht benutzt werden. Warten sie einen Moment.",
                            Toast.LENGTH_SHORT).show();
                }
                if (KaaManager.getCoffeeMachineEventHandler().getCoffeeStatus().getStatus() == StatusTypes.BREWING ||
                        KaaManager.getCoffeeMachineEventHandler().getCoffeeStatus().getStatus() == StatusTypes.GRINDING) {
                    CoffeeNotifications.alreadyBrewing();
                }
                // only change brewing type if device is connected to kaa
                if (KaaManager.isConnected()) {
                    InfoObject coffeeMachineStatus = KaaManager.getCoffeeMachineEventHandler().getCoffeeStatus();
                    if (coffeeMachineStatus != null
                            && coffeeMachineStatus.getFilter()) {
                        // grinder currently selected
                        KaaManager.getCoffeeMachineEventHandler().sendToggleBrewingTypeEvent();
                        // temporary set the filter type to prevent toggling before the updated coffeeMachineStatus
                        // has been received from the coffeeServer
                        coffeeMachineStatus.setFilter(false);
                    }
                    Log.d("CoffeeActivity", "Filter selected");
                } else {
                    // TODO: decide what todo if kaa is not connected
                }
            }
        });
    }

    /**
     * sets the enable state of the radioButtons of the CoffeeActivity corresponding to the enable boolean
     *
     * @param enable boolean weather there is currently a coffee brewing or not
     */
    public void setRadioButtonsEnableState(boolean enable) {
        radioButtonFilter.setEnabled(enable);
        radioButtonGrinder.setEnabled(enable);
        radioButtonWeak.setEnabled(enable);
        radioButtonMedium.setEnabled(enable);
        radioButtonStrong.setEnabled(enable);

        float alphaValue = (enable) ? 1f : 0.5f;
        radioButtonFilter.setAlpha(alphaValue);
        radioButtonGrinder.setAlpha(alphaValue);
        radioButtonWeak.setAlpha(alphaValue);
        radioButtonMedium.setAlpha(alphaValue);
        radioButtonStrong.setAlpha(alphaValue);
    }

    /**
     * sets the enable state of the cookingCoffeeButton and buttonKAbbrechen
     * corerspond to weather there is currently a coffee brewing or not
     *
     * @param isBrewing boolean weather there is currently a coffee brewing or not
     */
    public void setCookingButtonsEnableState(boolean isBrewing) {
        if (isBrewing) {
            cookingCoffeeButton.setEnabled(false);
            buttonKAbbrechen.setEnabled(true);
            cookingCoffeeButton.setAlpha(0.5f);
            buttonKAbbrechen.setAlpha(1f);
        } else {
            cookingCoffeeButton.setEnabled(true);
            buttonKAbbrechen.setEnabled(false);
            cookingCoffeeButton.setAlpha(1f);
            buttonKAbbrechen.setAlpha(0.5f);
        }
    }


}