package wgforge.vburak.demogame.fragments;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import wgforge.vburak.demogame.R;

import static wgforge.vburak.demogame.MainActivity.socket;

public class ConnectionFragment extends Fragment {

    private static final String IPv4_REGEX =
            "^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                    "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                    "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                    "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";

    private static final Pattern IPv4_PATTERN = Pattern.compile(IPv4_REGEX);


    EditText ipAddressInput;
    EditText portInput;
    Button connectButton;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.connection_fragment, parent, false);
        initUI(view);
        return view;
    }

    private void initUI(View view) {

        ipAddressInput = view.findViewById(R.id.loginInput);
        portInput = view.findViewById(R.id.port_input);
        connectButton = view.findViewById(R.id.connectButton);

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    new ConnectTask().execute(ipAddressInput.getText().toString(), portInput.getText().toString());
                }
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    class ConnectTask extends AsyncTask<String, Void, Void> {

        final static String TAG = "CONN_FRAGMENT_CONN_TASK";

        @Override
        protected Void doInBackground(String... strings) {
            String ipAddress = strings[0];
            int port = Integer.valueOf(strings[1]);
            try {
                socket = new Socket(ipAddress, port);
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                writer.println("Hello from Android device!");
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragments_placeholder, new LoginFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            } catch (IOException e) {
                Log.e(TAG, "Somethin went wrong while trying to execute connection task: " + e.getMessage());
            }
            return null;
        }
    }

    private boolean validate() {
        Matcher matcher = IPv4_PATTERN.matcher(ipAddressInput.getText().toString());
        if (ipAddressInput.getText().toString().equals("")) {
            ipAddressInput.setError("Required field!");
            return false;
        } else {
            ipAddressInput.setError(null);
        }
        if (!matcher.matches()) {
            ipAddressInput.setError("Incorrect IP-Address!");
            return false;
        } else {
            ipAddressInput.setError(null);
        }
        if (portInput.getText().toString().equals("")) {
            portInput.setError("Required field!");
            return false;
        } else {
            portInput.setError(null);
        }
        return true;
    }

}
