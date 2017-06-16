package smart_things.app.android.gui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import smart_things.app.android.KaaManager.KaaManager;
import smart_things.app.android.R;
import smart_things.coffee.schema.InfoObject;
import smart_things.coffee.schema.StatusTypes;
import smart_things.coffee.schema.StrengthTypes;


/**
 * A simple {@link Fragment} subclass.
 * user can configure the number of cups, see the waterlevel
 * setting the minutes to heat the plate
 */
public class CoffeeFragment extends Fragment {


    public enum WaterLevel {
        EMPTY
        // TODO: add water level enums
    }

    private ArrayList<View> coffeeViews;
    private int cupProgress;
    private boolean actionIsCooking = true;
    private SeekBar cupCountBar;
    private TextView cupCountCounter;
    private Switch grinderToggle;
    private RadioGroup strengthGroup;
    private RadioButton strengthWeak;
    private RadioButton strengthMedium;
    private RadioButton strengthStrong;
    private Button cookCoffeeBt;
    private ProgressBar cookCoffeeProgress;
    private ProgressBar waterLevelIndicator;
    private TextView waterLevelIndicatorCounter;
    private Button hotPlateBtn;


    // static UI elements
    private TextView waterLevelIndicatorLabel;
    private TextView cupCountLabel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // myFragmentView = inflater.inflate(R.layout.fragment_coffee, container, false);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_coffee, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        coffeeViews = new ArrayList<>();

        cupCountBar = (SeekBar) getActivity().findViewById(R.id.cup_count_bar);
        cupCountCounter = (TextView) getActivity().findViewById(R.id.cup_count_counter);
        grinderToggle = (Switch) getActivity().findViewById(R.id.grinder_toggle);
        strengthGroup = (RadioGroup) getActivity().findViewById(R.id.strength_group);
        strengthWeak = (RadioButton) getActivity().findViewById(R.id.strength_weak);
        strengthMedium = (RadioButton) getActivity().findViewById(R.id.strength_medium);
        strengthStrong = (RadioButton) getActivity().findViewById(R.id.strength_strong);
        cookCoffeeBt = (Button) getActivity().findViewById(R.id.coffee_cook_bt);
        cookCoffeeProgress = (ProgressBar) getActivity().findViewById(R.id.coffee_cook_bt_progress);
        waterLevelIndicator = (ProgressBar) getActivity().findViewById(R.id.water_level_indicator);
        waterLevelIndicatorCounter = (TextView) getActivity().findViewById(R.id.water_level_indicator_counter);

        addAllCoffeeViewsToList();

        waterLevelIndicatorLabel = (TextView) getActivity().findViewById(R.id.water_level_indicator_label);
        cupCountLabel = (TextView) getActivity().findViewById(R.id.cup_count_label);
        hotPlateBtn = (Button) getActivity().findViewById(R.id.heizplatteId);
        hotPlateBtn.setOnClickListener(hotPlateListener());
        cupCountBar.setOnSeekBarChangeListener(getSeekBarListener());
        grinderToggle.setOnCheckedChangeListener(grinderListener());
        strengthWeak.setOnClickListener(getcoffeeStrengthListenerWeak());
        strengthMedium.setOnClickListener(getcoffeeStrengthListenerMedium());
        strengthStrong.setOnClickListener(getcoffeeStrengthListenerStrong());
        cookCoffeeBt.setOnClickListener(brewingListener());

        //should always check if connection is there
        if (KaaManager.getCoffeeMachineEventHandler() != null && KaaManager.getCoffeeMachineEventHandler()
                .getCoffeeStatus() != null) {
            setGuiElementInitState();
        } else {
            setAllViewsEnableState(false);

        }
    }

    /**
     * Method for the Seekbar Listener.
     * Allows to change  the  amount of cups and sends the amount to the coffee mashine
     *
     * @return new SeekBar.OnSeekBarChangeListener()
     */
    private SeekBar.OnSeekBarChangeListener getSeekBarListener() {
        return new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                progress += 1;
                cupCountCounter.setText("" + progress);
                cupProgress = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                KaaManager.getCoffeeMachineEventHandler().sendSetCupCountEvent(cupProgress);

            }
        };
    }

    /**
     * Method of the OnClickListener for the hotPlate
     * setting the minutes for hotPlate
     *
     * @return
     */
    private CompoundButton.OnClickListener hotPlateListener() {
        return new CompoundButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoObject coffeeObj;
                if(KaaManager.getCoffeeMachineEventHandler() != null && KaaManager.getCoffeeMachineEventHandler()
                        .getCoffeeStatus() != null) {
                    hotPlateBtn.setVisibility(View.VISIBLE);
                    //ask the status of the coffeemaschine, beacause of safety aspects
                    InfoObject infoObject=    KaaManager.getCoffeeMachineEventHandler().getCoffeeStatus();
                    if (infoObject.getStatus() == StatusTypes.UNKNOWN ) {
                        //Toast.makeText(getActivity(), "Kaffeemaschine kann gerade nicht benutzt wertden. Warten sie einen Moment.", Toast.LENGTH_SHORT).show();
                    }else{
                        Snackbar.make(MainActivity.getInstance().findViewById(R.id.coffee_fragment),
                               "Verbindung zum Server nicht vorhanden" , Snackbar.LENGTH_LONG).show();
                    }

                    if (infoObject.getStatus() == StatusTypes.BREWING ||
                            infoObject.getStatus() == StatusTypes.GRINDING) {
                        //  KaaManager.getCoffeeMachineEventHandler().sendSetStrengthEvent(StrengthTypes.STRONG);
                    } else {
                        //get the value of minutes to heat the plate
                        final EditText input = new EditText(getActivity());
                        input.setInputType(InputType.TYPE_CLASS_NUMBER);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);
                        input.setLayoutParams(lp);

                        new AlertDialog.Builder(getActivity())
                                .setView(input)
                                .setTitle("Hotplate")
                                .setMessage("Wie lange soll der Kaffee warmgehalten werden? min 4  max 45")

                                .setPositiveButton(R.string.start,
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                // user can set time 4 until 45min
                                                int minutes = Integer.parseInt(input.getText().toString());
                                                if (minutes < 5 && minutes > 45) {
                                                    KaaManager.getCoffeeMachineEventHandler().sendHotPlateTypeEvent(minutes);
                                                }
                                            }
                                        })
                                .show();
                    }
                }
            }
        };
    }

    /**
     * Method of the ChangeListener for the grinderToggle switch
     * when the grinderToggle isChecked then the radio buttons are visible
     * Sends an Event to CoffeMashineEvent handler and changes the display of the coffee machine
     *
     * @returnnew CompoundButton.OnCheckedChangeListener()
     */
    private CompoundButton.OnCheckedChangeListener grinderListener() {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (grinderToggle.isChecked()) {
                    KaaManager.getCoffeeMachineEventHandler().sendToggleBrewingTypeEvent();
                    strengthGroup.setVisibility(View.VISIBLE);

                } else {
                    KaaManager.getCoffeeMachineEventHandler().sendToggleBrewingTypeEvent();
                    strengthGroup.setVisibility(View.GONE);
                }

            }
        };
    }

    /**
     * Method of the OnClickListener for the coffee strength radio buttons
     * gets the selected Coffee strength for the radioList
     * has got the 3 Types: WEAK, STRONG, MEDIUM
     * sends also the Event to the  KaaManager, so the Strength status of the Coffee Machine display
     * can be changed
     */
    private CompoundButton.OnClickListener getcoffeeStrengthListenerWeak() {
        return new CompoundButton.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (strengthWeak.isChecked()) {
                    KaaManager.getCoffeeMachineEventHandler().sendSetStrengthEvent(StrengthTypes.WEAK);
                }
            }
        };
    }

    /**
     * Method of the OnClickListener for the coffee strength radio buttons
     * gets the selected Coffee strength for the radioList
     * has got the 3 Types: WEAK, STRONG, MEDIUM
     * sends also the Event to the  KaaManager, so the Strength status of the Coffee Machine display
     * can be changed
     */
    private CompoundButton.OnClickListener getcoffeeStrengthListenerMedium() {
        return new CompoundButton.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (strengthMedium.isChecked()) {
                    KaaManager.getCoffeeMachineEventHandler().sendSetStrengthEvent(StrengthTypes.MEDIUM);
                }
            }
        };
    }

    /**
     * Method of the OnClickListener for the coffee strength radio buttons
     * gets the selected Coffee strength for the radioList
     * has got the 3 Types: WEAK, STRONG, MEDIUM
     * sends also the Event to the  KaaManager, so the Strength status of the Coffee Machine display
     * can be changed
     */
    private CompoundButton.OnClickListener getcoffeeStrengthListenerStrong() {
        return new CompoundButton.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (strengthStrong.isChecked()) {
                    KaaManager.getCoffeeMachineEventHandler().sendSetStrengthEvent(StrengthTypes.STRONG);
                }
            }
        };
    }

    /**
     * Method of the OnClickListener for the cookCoffeeBt
     * Calls the method startBrewing();
     *
     * @return
     */
    private CompoundButton.OnClickListener brewingListener() {
        return new CompoundButton.OnClickListener() {

            @Override
            public void onClick(View v) {
                cookCoffeeBt.setVisibility(View.INVISIBLE);
                cookCoffeeBt.setClickable(false);
                cookCoffeeProgress.setVisibility(View.VISIBLE);
                if (actionIsCooking) {
                    startBrewing();
                } else {
                    if (KaaManager.getCoffeeMachineEventHandler() != null) {
                        KaaManager.getCoffeeMachineEventHandler().sendStopBrewingEvent();
                    }
                }
            }
        };
    }


    public void setAllViewsEnableState(boolean enabled) {
        float alpha = enabled ? 1f : 0.5f;
        for (View view : coffeeViews) {
            view.setEnabled(enabled);
            view.setAlpha(alpha);
        }
    }

    /**
     * Method gets the actual status of the coffeemaker  and shows it in the app
     * if  eventhandler can not be reached  all the buttons are not clickable
     * gets the status auf the actual coffee strength
     */
    public void setGuiElementInitState() {

        if (KaaManager.getCoffeeMachineEventHandler().getCoffeeStatus() == null) {
            return;
        }

        //checks  if  filter is checked in Coffeemaker
        if (!KaaManager.getCoffeeMachineEventHandler().getCoffeeStatus().getFilter()) {

            strengthGroup.setVisibility(View.VISIBLE);
            //Checks the right coffee strength according to what is currently selected by the Coffeemaker
            switch (KaaManager.getCoffeeMachineEventHandler().getCoffeeStatus().getStrength()) {
                case WEAK:
                    strengthWeak.setChecked(true);
                    break;
                case MEDIUM:
                    strengthMedium.setChecked(true);
                    break;
                case STRONG:
                    strengthStrong.setChecked(true);
                    break;
                default:
                    strengthWeak.setChecked(false);
                    strengthMedium.setChecked(false);
                    strengthStrong.setChecked(false);
            }
        } else {

            strengthGroup.setVisibility(View.GONE);
            //Checks the right coffee strength according to what is currently selected by the Coffeemaker
            switch (KaaManager.getCoffeeMachineEventHandler().getCoffeeStatus().getStrength()) {
                case WEAK:
                    strengthWeak.setChecked(true);
                    break;
                case MEDIUM:
                    strengthMedium.setChecked(true);
                    break;
                case STRONG:
                    strengthStrong.setChecked(true);
                    break;
                default:
                    strengthWeak.setChecked(false);
                    strengthMedium.setChecked(false);
                    strengthStrong.setChecked(false);
            }
        }
        //gets the amount of cups of the coffeemaker
        cupCountBar.setProgress(KaaManager.getCoffeeMachineEventHandler().getCoffeeStatus().getCupsNumber() - 1);

        //update water status
        float quotient = waterLevelIndicator.getMax() / 100;
        switch (KaaManager.getCoffeeMachineEventHandler().getCoffeeStatus().getWaterLevel()) {
            case EMPTY:
                waterLevelIndicator.setProgress((int) (2 * quotient));
                waterLevelIndicatorCounter.setText(R.string.empty);
                Snackbar.make(MainActivity.getInstance().findViewById(R.id.coffee_fragment),
                        "Wasserstand leer", Snackbar.LENGTH_LONG).show();
                break;
            case LESS:
                waterLevelIndicator.setProgress((int) (25 * quotient));
                waterLevelIndicatorCounter.setText(R.string.quarter);
                break;
            case HALF:
                waterLevelIndicator.setProgress((int) (50 * quotient));
                waterLevelIndicatorCounter.setText(R.string.half);
                Snackbar.make(MainActivity.getInstance().findViewById(R.id.coffee_fragment),
                        "Wasserstand halbvoll", Snackbar.LENGTH_LONG).show();
                break;
            case FULL:
                waterLevelIndicator.setProgress((int) (100 * quotient));
                waterLevelIndicatorCounter.setText(R.string.full);
                break;
            case UNKNOWN:
            default:
                waterLevelIndicator.setProgress((int) (25 * quotient));
                waterLevelIndicatorCounter.setText(R.string.quarter);
        }
        cookCoffeeProgress.setVisibility(View.INVISIBLE);
        cookCoffeeBt.setVisibility(View.VISIBLE);
        cookCoffeeBt.setClickable(true);
        switch (KaaManager.getCoffeeMachineEventHandler().getCoffeeStatus().getStatus()) {
            case BREWING:
            case GRINDING:
                cookCoffeeBt.setText(R.string.coffee_stop);
                actionIsCooking = false;
                break;
            case READY:
            case UNKNOWN:
            default:
                cookCoffeeBt.setText(R.string.cooffee_start);
                actionIsCooking = true;
        }
    }


    /**
     * Method to use all the views simultaneously (for example  to use it for enable the buttons )
     */
    private void addAllCoffeeViewsToList() {
        coffeeViews.add(cupCountBar);
        coffeeViews.add(cupCountCounter);
        coffeeViews.add(grinderToggle);
        //  coffeeViews.add(strengthGroup);
        coffeeViews.add(strengthWeak);
        coffeeViews.add(strengthMedium);
        coffeeViews.add(strengthStrong);
        coffeeViews.add(cookCoffeeBt);
        //   coffeeViews.add(waterLevelIndicatorLabel);
        coffeeViews.add(cupCountCounter);
        //   coffeeViews.add(cupCountLabel);
        coffeeViews.add(waterLevelIndicator);
        coffeeViews.add(waterLevelIndicatorCounter);
    }


    /**
     * Start brewing method. Sends the  Cooking Event only when the Coffeemashine is ready
     */
    private void startBrewing() {

        //asks the coffeeMachine status, because of safety aspects
        //only brewing coffee when coffeeMachine is available
        if (KaaManager.getCoffeeMachineEventHandler().getCoffeeStatus().getStatus() == StatusTypes.READY) {
            KaaManager.getCoffeeMachineEventHandler().sendStartBrewingEvent();
            Snackbar.make(MainActivity.getInstance().findViewById(R.id.coffee_fragment),
                    "Coffee will be cooked", Snackbar.LENGTH_LONG).show();
        }
        if (KaaManager.getCoffeeMachineEventHandler().getCoffeeStatus().getStatus() == StatusTypes.BREWING ||
                KaaManager.getCoffeeMachineEventHandler().getCoffeeStatus().getStatus() == StatusTypes.GRINDING) {
            Snackbar.make(MainActivity.getInstance().findViewById(R.id.coffee_fragment),
                    R.string.coffee_machine_in_use, Snackbar.LENGTH_LONG).show();
        }
        if (KaaManager.getCoffeeMachineEventHandler().getCoffeeStatus().getStatus() == StatusTypes.UNKNOWN) {
            Snackbar.make(MainActivity.getInstance().findViewById(R.id.coffee_fragment),
                    R.string.unknown_state, Snackbar.LENGTH_LONG).show();
        }
    }
}
