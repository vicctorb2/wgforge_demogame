package wgforge.vburak.demogame.fragments;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import wgforge.vburak.demogame.R;

import static wgforge.vburak.demogame.MainActivity.socket;

public class LoginFragment extends Fragment {

    View view;
    EditText loginInput;
    EditText passwordInput;
    Button loginButton;
    Bundle savedInstance;
    BufferedReader bufferedReader;
    static boolean loginSuccess = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login_fragment, container, false);
        savedInstance = savedInstanceState;
        initUI();
        return view;
    }

    private void initUI() {
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        loginInput = view.findViewById(R.id.loginInput);
        passwordInput = view.findViewById(R.id.password_input);
        loginButton = view.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    new SendLoginRequestTask().execute("AUTH " + loginInput.getText().toString() + " " + passwordInput.getText().toString());
                    while (!loginSuccess) {
                        new WaitForAuthSuccessTask().execute();
                    }
                    Toast.makeText(getActivity(),"LOGIN SUCCESSFULL!", Toast.LENGTH_SHORT);
                }
            }
        });
    }

    private boolean validate() {
        if (loginInput.getText().toString().equals("")) {
            loginInput.setError("Required field!");
            return false;
        } else {
            loginInput.setError(null);
        }
        if (passwordInput.getText().toString().equals("")) {
            passwordInput.setError("Required field!");
            return false;
        } else {
            passwordInput.setError(null);
        }
        return true;
    }

    @SuppressLint("StaticFieldLeak")
    class SendLoginRequestTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String message = strings[0];
            PrintWriter writer = null;
            try {
                writer = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert writer != null;
            writer.println("Login from Android device!");
            return null;
        }
    }

    class WaitForAuthSuccessTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            String line;
            try {
                while ((line = bufferedReader.readLine()) != null) {
                    Log.d("RECEIVED_FROM_SERV: ", line);
                    if (line.equals("SUCCESS")) {
                        Log.d("RECEIVED_FROM_SERV: ", line);
                        loginSuccess = true;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
