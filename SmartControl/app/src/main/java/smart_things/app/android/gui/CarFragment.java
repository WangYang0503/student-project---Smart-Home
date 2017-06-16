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
import smart_things.app.android.gui.message.CarMessage;


public class CarFragment extends Fragment {

    private ArrayList<View> carViews;

    private CarMessage carMessage;
    private Button startStopButton;
    private ProgressBar startStopProgress;
    private TextView title;
    private TextView description;
    private ImageView icon;

    boolean buttonActionIsStart;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        carMessage = new CarMessage(this);
        return inflater.inflate(R.layout.fragment_car, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        carViews = new ArrayList<>();

        startStopButton = (Button) getActivity().findViewById(R.id.car_button);
        startStopProgress = (ProgressBar) getActivity().findViewById(R.id.car_progress);
        title = (TextView) getActivity().findViewById(R.id.car_label);
        description = (TextView) getActivity().findViewById(R.id.car_description);
        icon = (ImageView) getActivity().findViewById(R.id.car_icon);

        addAllCarViewsToList();
        startStopButton.setOnClickListener(createStartStopOnClickListener());

        if (KaaManager.getMindstormsEventHandler() != null && KaaManager.getMindstormsEventHandler().getInfoObject() != null) {
            setAllViewsEnableState(true);
            setButtonAction(!KaaManager.getMindstormsEventHandler().getInfoObject().getDriving());
        } else {
            setAllViewsEnableState(false);
        }
    }

    /**
     * @param actionStart if set to true, the button displays a text to start the car;
     *                    if set to false; the button displays a text to stop the car
     */
    public void setButtonAction(boolean actionStart) {
        startStopProgress.setVisibility(View.INVISIBLE);
        startStopButton.setClickable(true);
        startStopButton.setVisibility(View.VISIBLE);
        if (actionStart) {
            startStopButton.setText(R.string.start);
            buttonActionIsStart = true;
        } else {
            startStopButton.setText(R.string.stop);
            buttonActionIsStart = false;
        }
    }

    public void setAllViewsEnableState(boolean enabled) {
        float alpha = enabled ? 1f : 0.5f;
        for (View view : carViews) {
            view.setEnabled(enabled);
            view.setAlpha(alpha);
        }
    }

    private View.OnClickListener createStartStopOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carMessage.showCarStartedMessage();
                startStopButton.setClickable(false);
                startStopButton.setVisibility(View.INVISIBLE);
                startStopProgress.setVisibility(View.VISIBLE);
                if (KaaManager.getMindstormsEventHandler() != null) {
                    if (buttonActionIsStart) {
                        KaaManager.getMindstormsEventHandler().sendStartDrivingEvent();
                    } else {
                        KaaManager.getMindstormsEventHandler().sendStopDrivingEvent();
                    }
                }
            }
        };
    }

    private void addAllCarViewsToList() {
        carViews.add(startStopButton);
        carViews.add(startStopProgress);
        carViews.add(title);
        carViews.add(description);
        carViews.add(icon);
    }
}
