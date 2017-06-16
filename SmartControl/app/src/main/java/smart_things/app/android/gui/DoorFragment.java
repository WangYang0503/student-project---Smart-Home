package smart_things.app.android.gui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import smart_things.app.android.KaaManager.KaaManager;
import smart_things.app.android.R;

public class DoorFragment extends Fragment {

    private ArrayList<View> doorViews;
    private boolean alreadyIntiated = false;

    boolean houseDoorOpenAction;
    boolean garageDoorOpenAction;

    private Button garageDoorButton;
    private Button houseDoorButton;
    private ProgressBar garageDoorProgressBar;
    private ProgressBar houseDoorProgressBar;
    private ImageView garageIcon;

    private TextView garageTitle;
    private TextView garageDescription;
    private TextView doorTitle;
    private TextView doorDescription;
    private ImageView doorIcon;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_door, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        doorViews = new ArrayList<>();

        garageDoorButton = (Button) getActivity().findViewById(R.id.garage_button);
        houseDoorButton = (Button) getActivity().findViewById(R.id.house_door_button);
        garageDoorProgressBar = (ProgressBar) getActivity().findViewById(R.id.garage_progressBar);
        houseDoorProgressBar = (ProgressBar) getActivity().findViewById(R.id.house_door_progressBar);
        garageIcon = (ImageView) getActivity().findViewById(R.id.garage_icon);

        garageTitle = (TextView) getActivity().findViewById(R.id.garage_door_label);
        garageDescription = (TextView) getActivity().findViewById(R.id.garage_description);
        doorTitle = (TextView) getActivity().findViewById(R.id.house_door_label);
        doorDescription = (TextView) getActivity().findViewById(R.id.house_door_description);
        doorIcon = (ImageView) getActivity().findViewById(R.id.house_door_icon);

        addAllDoorViewsToList();

        houseDoorButton.setOnClickListener(createHouseDoorOnClickListener());
        garageDoorButton.setOnClickListener(createGarageDoorOnClickListener());

        if (KaaManager.getDoorEventHandler() != null && KaaManager.getDoorEventHandler().getDoorAndGarageStatus() != null) {
            setAllViewsEnableState(true);
            setDoorAction(!KaaManager.getDoorEventHandler().getDoorAndGarageStatus().getDoorOpened());
            setGarageAction(!KaaManager.getDoorEventHandler().getDoorAndGarageStatus().getGarageOpened());


        } else {
            setAllViewsEnableState(false);
        }
    }

    private View.OnClickListener createHouseDoorOnClickListener() {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                houseDoorButton.setClickable(false);
                houseDoorButton.setVisibility(View.INVISIBLE);
                houseDoorProgressBar.setVisibility(View.VISIBLE);
                if (KaaManager.getDoorEventHandler() != null) {
                    if (houseDoorOpenAction) {
                        KaaManager.getDoorEventHandler().sendOpenDoor();
                    } else {
                        KaaManager.getDoorEventHandler().sendCloseDoor();
                    }
                }
            }
        };
    }

    private View.OnClickListener createGarageDoorOnClickListener() {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                garageDoorButton.setClickable(false);
                garageDoorButton.setVisibility(View.INVISIBLE);
                garageDoorProgressBar.setVisibility(View.VISIBLE);
                if (KaaManager.getDoorEventHandler() != null) {
                    if (garageDoorOpenAction) {
                        KaaManager.getDoorEventHandler().sendOpenGarage();
                    } else {
                        KaaManager.getDoorEventHandler().sendCloseGarage();
                    }
                }
            }
        };
    }

    public void setAllViewsEnableState(boolean enabled) {
        float alpha = enabled ? 1f : 0.5f;
        for (View view : doorViews) {
            view.setEnabled(enabled);
            view.setAlpha(alpha);
        }
    }


    public void setDoorAction(boolean actionOpen) {
        houseDoorProgressBar.setVisibility(View.INVISIBLE);
        houseDoorButton.setClickable(true);
        houseDoorButton.setVisibility(View.VISIBLE);
        if (actionOpen) {
            houseDoorButton.setText(R.string.open);
            houseDoorOpenAction = true;
        } else {
            houseDoorButton.setText(R.string.close);
            houseDoorOpenAction = false;
        }
        alreadyIntiated = true;
    }

    public void setGarageAction(boolean actionOpen) {
        garageDoorProgressBar.setVisibility(View.INVISIBLE);
        garageDoorButton.setClickable(true);
        garageDoorButton.setVisibility(View.VISIBLE);
        if (actionOpen) {
            garageDoorButton.setText(R.string.open);
            garageDoorOpenAction = true;
            garageIcon.setImageResource(R.drawable.ic_garage);
        } else {
            garageDoorButton.setText(R.string.close);
            garageDoorOpenAction = false;
            garageIcon.setImageResource(R.drawable.ic_garage_open);
        }
        alreadyIntiated = true;
    }

    public ImageView getGarageIcon() {
        return garageIcon;
    }

    public boolean isInitiated() {
        return alreadyIntiated;
    }

    public void setAlreadyIntiated(boolean alreadyIntiated) {
        this.alreadyIntiated = alreadyIntiated;
    }

    private void addAllDoorViewsToList() {
        doorViews.add(garageDoorButton);
        doorViews.add(houseDoorButton);
        doorViews.add(garageDoorProgressBar);
        doorViews.add(houseDoorProgressBar);
        doorViews.add(garageIcon);

        doorViews.add(garageTitle);
        doorViews.add(garageDescription);
        doorViews.add(doorTitle);
        doorViews.add(doorDescription);
        doorViews.add(doorIcon);
    }
}
