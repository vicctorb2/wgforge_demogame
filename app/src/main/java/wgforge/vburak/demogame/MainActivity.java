package wgforge.vburak.demogame;

import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import java.io.IOException;
import java.net.Socket;

import wgforge.vburak.demogame.fragments.ConnectionFragment;

public class MainActivity extends AppCompatActivity {


    public static Socket socket;

    ConnectionFragment connectionFragment;

    FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }
    private void initUI() {
        frameLayout = findViewById(R.id.fragments_placeholder);
        connectionFragment = new ConnectionFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(frameLayout.getId(),connectionFragment,"CONNECTION_FRAGMENT");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
