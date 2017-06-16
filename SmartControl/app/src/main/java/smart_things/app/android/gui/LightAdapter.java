package smart_things.app.android.gui;

import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import smart_things.app.android.KaaManager.KaaManager;
import smart_things.app.android.KaaManager.eventHandler.LightEventHandler;
import smart_things.app.android.R;
import smart_things.light.schema.LEDInfoObject;

/**
 * Created by Lukas on 15.02.2017.
 */

public class LightAdapter extends SelectableAdapter<LightAdapter.MyViewHolder> {

    public static List<String> getRoomLightList() {
        return roomLightList;
    }

    public static void setRoomLightList(List<String> roomLightList) {
        LightAdapter.roomLightList = roomLightList;
    }

    private static List<String> roomLightList;
    private LightFragment lightFragment;
    private static final String TAG = LightAdapter.class.getSimpleName();

    private static final int TYPE_INACTIVE = 0;
    private static final int TYPE_ACTIVE = 1;

    private static final int ITEM_COUNT = 50;
    private MyViewHolder.ClickListener clickListener;

    public LightAdapter(List<String> roomLightList, MyViewHolder.ClickListener clickListener, LightFragment lightFragment) {
        super();

        this.clickListener = clickListener;
        this.roomLightList = roomLightList;
        this.lightFragment = lightFragment;

        // Create some items
//        Random random = new Random();
//        items = new ArrayList<>();
//        for (int i = 0; i < ITEM_COUNT; ++i) {
//            items.add(new ClipData.Item("Item " + i, "This is the item number " + i, random.nextBoolean()));
//        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_row, parent, false);

        return new MyViewHolder(itemView, clickListener);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        LightEventHandler light = KaaManager.getLightEventHandler();
        holder.roomName.setText(light.getRoomItems().get(position));

        if (isSelected(position)) {
            holder.itemView.setBackgroundColor(lightFragment.getActivity().getResources().getColor(R.color.primary_dark));
        } else {
            holder.itemView.setBackgroundColor(lightFragment.getActivity().getResources().getColor(R.color.icons_selected));
        }
    }

    @Override
    public int getItemCount() {
        return roomLightList.size();
    }

    public void add(int position, String item) {
        roomLightList.add(position, item);

        notifyItemInserted(position);

    }

    public void removeItem(int position) {
        roomLightList.remove(position);

        notifyItemRemoved(position);
    }

    public void removeItems(List<Integer> positions) {
        // Reverse-sort the list
        Collections.sort(positions, new Comparator<Integer>() {
            @Override
            public int compare(Integer lhs, Integer rhs) {
                return rhs - lhs;
            }
        });

        // Split the list in ranges
        while (!positions.isEmpty()) {
            if (positions.size() == 1) {
                removeItem(positions.get(0));
                positions.remove(0);
            } else {
                int count = 1;
                while (positions.size() > count && positions.get(count).equals(positions.get(count - 1) - 1)) {
                    ++count;
                }

                if (count == 1) {
                    removeItem(positions.get(0));
                } else {
                    removeRange(positions.get(count - 1), count);
                }

                for (int i = 0; i < count; ++i) {
                    positions.remove(0);
                }
            }
        }
    }

    /**
     * remove all the items one has selected
     * sends removeRoomEvent
     */

    private void removeRange(int positionStart, int itemCount) {
        for (int i = 0; i < itemCount; ++i) {
            roomLightList.remove(positionStart);
        }
        notifyItemRangeRemoved(positionStart, itemCount);
    }


    /**
     * MyView Holder Class
     */
    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener, CompoundButton.OnCheckedChangeListener {

        private static final String TAG = MyViewHolder.class.getSimpleName();
        TextView roomName;
        SwitchCompat mSwitch;
        View selectedOverlay;
        List<LEDInfoObject> lightBrighntes;

        private ClickListener listener;


        public MyViewHolder(View itemView, ClickListener listener) {
            super(itemView);

            roomName = (TextView) itemView.findViewById(R.id.title);
            mSwitch = (SwitchCompat) itemView.findViewById(R.id.switch_compat);
            selectedOverlay = itemView.findViewById(R.id.selected_overlay);

            lightBrighntes = KaaManager.getLightEventHandler().getResponseObject();


            //should  get the switch in the right position
            if (!lightBrighntes.isEmpty()) {
                for (int position = 0; position < lightBrighntes.size(); position++) {
                    if (lightBrighntes.get(position).getBlue() != 0 || lightBrighntes.get(position).getRed() != 0
                            || lightBrighntes.get(position).getGreen() != 0) {
                        Log.d("Licht an", "Da " + position);
                       mSwitch.setTag(lightBrighntes.get(position));

                    }
                }
            }
            this.listener = listener;

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            mSwitch.setOnCheckedChangeListener(this);


        }


        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClicked(getLayoutPosition());

            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (listener != null) {
                return listener.onItemLongClicked(getLayoutPosition());
            }

            return false;
        }

        /**
         * setst the light on/ off
         * @param buttonView
         * @param isChecked
         */
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            if (mSwitch.isChecked()) {
                KaaManager.getLightEventHandler().sendColorEvent(roomLightList.get(getLayoutPosition()), 255, 255, 255);

            } else {
                KaaManager.getLightEventHandler().sendColorEvent(roomLightList.get(getLayoutPosition()), 0, 0, 0);

            }
        }

        public interface ClickListener {
            void onItemClicked(int position);

            boolean onItemLongClicked(int position);
        }

    }

}

