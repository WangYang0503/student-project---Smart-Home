package smart_things.app.android.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import smart_things.app.android.KaaManager.KaaManager;
import smart_things.app.android.R;

import static android.app.Activity.RESULT_OK;


public class LightFragment extends Fragment implements LightAdapter.MyViewHolder.ClickListener, Observer{
    private ArrayList<View> lightViews;
    private List<String> roomLightList = new ArrayList<>();
    private RecyclerView recyclerView;
    private LightAdapter mAdapter;
    private Switch switchLight;


    private FloatingActionButton fab;
    private Button changeColorBtn;
    private CoordinatorLayout coordinatorLayout;
    private ActionMode actionMode;
    private ActionModeCallback actionModeCallback = new ActionModeCallback();
    private AddLight mAddLight;
    private String roomname;
    private int redport, blueport, greenport;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_light, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if(roomLightList.isEmpty()){
        }
        if(KaaManager.getLightEventHandler()!=null) {
            final Snackbar snackbar = Snackbar.
                    make(MainActivity.getInstance().findViewById(R.id.coordinatorLayout),
                            R.string.load_list,Snackbar.LENGTH_INDEFINITE);
            snackbar.
                    setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            snackbar.dismiss();
                        }
                    })
                    .show();
            KaaManager.getLightEventHandler().addObserver(this);
        }
        super.onActivityCreated(savedInstanceState);

        coordinatorLayout = (CoordinatorLayout) getActivity().findViewById(R.id.coordinatorLayout);
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_view);

        mAdapter = new LightAdapter(roomLightList, this, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        changeColorBtn =(Button)getActivity().findViewById(R.id.light_bt_change_color);

        changeColorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ChangeLight.class);
                startActivityForResult(intent,2);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            Intent intent = new Intent(getActivity(),AddLight.class);
                startActivityForResult(intent,1);

            }
        });
            setGuiInitState();


    }


    @Override
    public void onItemClicked(int position) {
        if (actionMode != null) {
            toggleSelection(position);
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            actionMode = activity.startSupportActionMode(actionModeCallback);
        }else {
//            Intent intent = new Intent(getActivity(),ChangeLight.class);
//            intent.putExtra("currentItem",mAdapter.getItemId(position));
//            startActivityForResult(intent,2);

        }
    }

    @Override
    public boolean onItemLongClicked(int position) {
        if (actionMode == null) {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            actionMode = activity.startSupportActionMode(actionModeCallback);
        }
        toggleSelection(position);
        return true;
    }

    /**
     * Toggle the selection state of an item.
     *
     * If the item was the last one in the selection and is unselected, the selection is stopped.
     * Note that the selection must already be started (actionMode must not be null).
     *
     * @param position Position of the item to toggle the selection state
     */
    private void toggleSelection(int position) {
        mAdapter.toggleSelection(position);
        int count = mAdapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    @Override
    public void update(Observable o, Object arg) {

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                roomname=data.getStringExtra("roomname");
                redport=data.getIntExtra("redport",0);
                greenport=data.getIntExtra("greenport",0);
                blueport=data.getIntExtra("blueport",0);
                //KaaManager.getLightEventHandler().sendAddRGBLightEvent(roomname,roomname,redport,greenport,blueport);
                //roomLightList.add(roomname);
                roomLightList.clear();
                mAdapter.notifyDataSetChanged();
                prepareLightList();

            }
            if(requestCode==2){

            }
            mAdapter.notifyDataSetChanged();
        }

    }

    private class ActionModeCallback implements ActionMode.Callback {
        @SuppressWarnings("unused")
        private final String TAG = ActionModeCallback.class.getSimpleName();

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate (R.menu.context_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.item_delete:
                   Snackbar. make(MainActivity.getInstance().findViewById(R.id.coordinatorLayout),"Delete",Snackbar.LENGTH_LONG)
                           .show();
                    String checkedItems =roomLightList.get(item.getItemId());
                    for(int i=0; i<checkedItems.length();i++){
                        roomLightList.remove(item);
                        KaaManager.getLightEventHandler().sendRemoveLightEvent(roomname);
                        KaaManager.getLightEventHandler().sendRemoveRoomEvent(roomname);
                    }
                   // mAdapter.notifyItemRemoved(item.getItemId());

                    mAdapter.notifyDataSetChanged();
                    mode.finish();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mAdapter.clearSelection();
            actionMode = null;
        }
    }

    private void prepareLightList() {

            if (roomLightList.isEmpty()) {
                KaaManager.getLightEventHandler().initialize();
                List<String> list = KaaManager.getLightEventHandler().getRoomItems();

                for (String s : list) {
                    Log.i("LISTITEM", s);
                    roomLightList.add(s);
                }
                mAdapter.notifyDataSetChanged();
            }
            else{
                roomLightList.clear();
                KaaManager.getLightEventHandler().initialize();
                List<String> list = KaaManager.getLightEventHandler().getRoomItems();

                for (String s : list) {
                    Log.i("LISTITEM", s);
                    roomLightList.add(s);
                }
                mAdapter.notifyDataSetChanged();
            }

    }

    private void setGuiInitState() {
        if(KaaManager.getLightEventHandler()==null){
            fab.setClickable(false);
            fab.setAlpha((float) 0.5);
            changeColorBtn.setClickable(false);
            changeColorBtn.setAlpha(0.5f);
            Snackbar.
                    make(MainActivity.getInstance().findViewById(R.id.coordinatorLayout),
                            "Keine Verbindung zum Server",Snackbar.LENGTH_LONG)
            .show();
        }else
            prepareLightList();

    }




}