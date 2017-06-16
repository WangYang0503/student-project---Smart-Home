package smart_things.app.android.gui;

        import android.app.Activity;
        import android.content.Context;
        import android.content.Intent;
        import android.net.Uri;
        import android.os.Bundle;
        import android.support.design.widget.Snackbar;
        import android.support.design.widget.TextInputLayout;
        import android.support.v4.app.Fragment;
        import android.text.Editable;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.view.WindowManager;
        import android.widget.Button;
        import android.widget.EditText;
        import android.text.TextWatcher;

        import java.util.ArrayList;
        import java.util.List;

        import smart_things.app.android.KaaManager.KaaManager;
        import smart_things.app.android.R;

        import static smart_things.app.android.R.id.coordinatorLayout;
        import static smart_things.app.android.R.id.input_roomname;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddLight.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddLight#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddLight extends Activity {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private EditText inputRoomName, inputPortRed, inputPortGreen, inputPortBlue;
    private TextInputLayout inputLayoutRoomName, inputLayoutPortRed, inputLayoutPortGreen, inputLayoutPortBlue;
    private Button btnAddRoom;

    private List<String> roomlightlist;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_light);

        inputLayoutRoomName = (TextInputLayout) findViewById(R.id.input_layout_name);
        inputLayoutPortRed = (TextInputLayout) findViewById(R.id.input_layout_redport);
        inputLayoutPortGreen = (TextInputLayout) findViewById(R.id.input_layout_greenport);
        inputLayoutPortBlue = (TextInputLayout) findViewById(R.id.input_layout_blueport);

        inputRoomName = (EditText) findViewById(R.id.input_roomname);
        inputPortRed = (EditText) findViewById(R.id.input_redport);
        inputPortGreen = (EditText) findViewById(R.id.input_greenport);
        inputPortBlue = (EditText) findViewById(R.id.input_blueport);

        btnAddRoom = (Button) findViewById(R.id.btn_add_room);

        inputRoomName.addTextChangedListener(new MyTextWatcher(inputRoomName));
        inputPortRed.addTextChangedListener(new MyTextWatcher(inputPortRed));
        inputPortGreen.addTextChangedListener(new MyTextWatcher(inputPortGreen));
        inputPortBlue.addTextChangedListener(new MyTextWatcher(inputPortBlue));


        btnAddRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });

    }

    /**
     * Validating form
     */
    private void submitForm() {

        roomlightlist= KaaManager.getLightEventHandler().getRoomItems();
        if (!validateRoomName()) {
            return;
        }

        if (!validateRedPort()) {
            return;
        }

        if (!validateBluePort()) {
            return;
        }

        if (!validateGreenPort()) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra("roomname",inputRoomName.getText().toString().trim());
        intent.putExtra("redport",Integer.parseInt(inputPortRed.getText().toString().trim()));
        intent.putExtra("greenport",Integer.parseInt(inputPortGreen.getText().toString().trim()));
        intent.putExtra("blueport",Integer.parseInt(inputPortBlue.getText().toString().trim()));

        KaaManager.getLightEventHandler().sendAddRGBLightEvent(inputRoomName.getText().toString(),
                inputRoomName.getText().toString(),
                Integer.parseInt( inputPortRed.getText().toString()),
                Integer.parseInt(inputPortGreen.getText().toString()),
                Integer.parseInt(inputPortBlue.getText().toString()));
        roomlightlist.add(inputRoomName.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    private boolean validateRoomName() {
        if (inputRoomName.getText().toString().trim().isEmpty()) {
            inputLayoutRoomName.setError(getString(R.string.err_msg_roomname));
            requestFocus(inputRoomName);
            return false;
        } else {
            inputLayoutRoomName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateRedPort() {
        if (inputPortRed.getText().toString().trim().isEmpty()
                || !(android.text.TextUtils.isDigitsOnly(inputPortRed.getText().toString().trim()))
                || !(inputPortRed.getText().toString().trim().length() <= 3)) {
            inputLayoutPortRed.setError(getString(R.string.err_msg_port));
            requestFocus(inputPortRed);
            return false;
        }

        inputLayoutPortRed.setErrorEnabled(false);
        return true;
    }

    private boolean validateBluePort() {
        if (inputPortBlue.getText().toString().trim().isEmpty()
                || !(android.text.TextUtils.isDigitsOnly(inputPortBlue.getText().toString().trim()))
                || !(inputPortBlue.getText().toString().trim().length() <= 3)) {
            inputLayoutPortBlue.setError(getString(R.string.err_msg_port));
            requestFocus(inputPortBlue);
            return false;
        }


        inputLayoutPortBlue.setErrorEnabled(false);
        return true;
    }

    private boolean validateGreenPort() {
        if (inputPortGreen.getText().toString().trim().isEmpty()
                || !(android.text.TextUtils.isDigitsOnly(inputPortGreen.getText().toString().trim()))
                || !(inputPortGreen.getText().toString().trim().length() <= 3)) {
            inputLayoutPortGreen.setError(getString(R.string.err_msg_port));
            requestFocus(inputPortGreen);
            return false;
        }

        inputLayoutPortGreen.setErrorEnabled(false);
        return true;
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public AddLight() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddLight.
     */

    public static AddLight newInstance(String param1, String param2) {
        AddLight fragment = new AddLight();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        //fragment.setArguments(args);
        return fragment;
    }



    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_roomname:
                    validateRoomName();
                    break;
                case R.id.input_blueport:
                    validateBluePort();
                case R.id.input_greenport:
                    validateGreenPort();
                case R.id.input_redport:
                    validateRedPort();
                    break;
            }
        }
    }
}