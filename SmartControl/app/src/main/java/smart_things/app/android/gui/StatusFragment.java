package smart_things.app.android.gui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import smart_things.app.android.KaaManager.KaaManager;
import smart_things.app.android.R;
import smart_things.coffee.schema.InfoObject;
import smart_things.door.schema.MoveStates;

/**
 *
 */
public class StatusFragment extends Fragment {

    private TextView kaaStatus;
    private TextView carStatus;
    private TextView carStatusBattery;
    private TextView coffeeStatus;
    private TextView doorStatus;
    private TextView garageStatus;
    private Button emergencyButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_status, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        kaaStatus = (TextView) getActivity().findViewById(R.id.status_kaa);
        carStatus = (TextView) getActivity().findViewById(R.id.status_car);
        carStatusBattery = (TextView) getActivity().findViewById(R.id.status_car_battery);
        coffeeStatus = (TextView) getActivity().findViewById(R.id.status_coffee);
        doorStatus = (TextView) getActivity().findViewById(R.id.status_door);
        garageStatus = (TextView) getActivity().findViewById(R.id.status_garage);
        emergencyButton = (Button) getActivity().findViewById(R.id.button_emergency);

        emergencyButton.setOnClickListener(createEmergencyOnClickListener());

        updateStatus();
    }

    public void updateStatus() {
        // update kaa status
        if (KaaManager.isConnected()) {
            kaaStatus.setText(R.string.connected);
        } else {
            kaaStatus.setText(R.string.kaa_not_connected);
        }

        // update car status
        if (KaaManager.getMindstormsEventHandler() != null) {
            smart_things.car.schema.InfoObject carStatus = KaaManager.getMindstormsEventHandler().getInfoObject();
            String carStatusString;
            int carBatteryStatus;
            if (carStatus != null) {
                boolean isDriving = carStatus.getDriving();
                carStatusString = isDriving ? getString(R.string.driving) : getString(R.string.ready);
                carBatteryStatus = carStatus.getBattery();
                this.carStatusBattery.setText(String.format("%d%%", carBatteryStatus));
                this.carStatus.setText(carStatusString);
            }
        }
        if (KaaManager.getCoffeeMachineEventHandler() != null) {
            InfoObject coffeeStatus = KaaManager.getCoffeeMachineEventHandler().getCoffeeStatus();
            String coffeeStatusString;
            if (coffeeStatus != null) {
                switch (coffeeStatus.getStatus()) {
                    case UNKNOWN:
                        coffeeStatusString = getString(R.string.coffee_unknown);
                        break;
                    case READY:
                        coffeeStatusString = getString(R.string.coffee_ready);
                        break;
                    case GRINDING:
                        coffeeStatusString = getString(R.string.coffee_grinding);
                        break;
                    case BREWING:
                        coffeeStatusString = getString(R.string.coffee_brewing);
                        break;
                    default:
                        coffeeStatusString = getString(R.string.coffee_unknown);
                }
                this.coffeeStatus.setText(coffeeStatusString);
            }
        }
        if (KaaManager.getDoorEventHandler() != null) {
            smart_things.door.schema.InfoObject garageStatus = KaaManager.getDoorEventHandler().getDoorAndGarageStatus();
            if (garageStatus != null) {
                MoveStates garageMoveState = garageStatus.getGarageMoveState();
                if (garageMoveState == MoveStates.NONE) {
                    boolean garageOpen = garageStatus.getGarageOpened();
                    if (garageOpen) {
                        this.garageStatus.setText(R.string.garage_open);
                    } else {
                        this.garageStatus.setText(R.string.garage_closed);
                    }
                } else {
                    if (garageMoveState == MoveStates.OPENING) {
                        this.garageStatus.setText(R.string.garage_opening);
                    } else if (garageMoveState == MoveStates.CLOSING) {
                        this.garageStatus.setText(R.string.garage_closing);
                    }
                }
                MoveStates doorMoveState = garageStatus.getDoorMoveState();
                if (doorMoveState == MoveStates.NONE) {
                    boolean doorOpen = garageStatus.getDoorOpened();
                    if (doorOpen) {
                        this.doorStatus.setText(R.string.door_open);
                    } else {
                        this.doorStatus.setText(R.string.door_closed);
                    }
                } else {
                    if (doorMoveState == MoveStates.OPENING) {
                        this.doorStatus.setText(R.string.door_opening);
                    } else if (doorMoveState == MoveStates.CLOSING) {
                        this.doorStatus.setText(R.string.door_closing);
                    }
                }
            }
        }
    }

    private View.OnClickListener createEmergencyOnClickListener() {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (KaaManager.getLightEventHandler() != null) {
                    KaaManager.getLightEventHandler().sendEmergencyModeCloseEvent();
                }
            }
        };
    }
}
