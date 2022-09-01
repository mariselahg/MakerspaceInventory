package com.example.makerspace_inventory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    public static String PREFS_NAME="MyPrefsFile";
    private Button confirmBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        confirmBtn=findViewById(R.id.buttonConfirm);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText etAppID = findViewById(R.id.editTextAppID);
                EditText etDatabase = findViewById(R.id.editTextDatabase);
                EditText etCollection = findViewById(R.id.editTextCollection);

                String appID = etAppID.getText().toString();
                String database = etDatabase.getText().toString();
                String collection = etCollection.getText().toString();

                /*
                * Upon pressing the "Confirm" button, the content of the text fields is saved in String variables.
                * These variables are then sent to the MainActivity to handle the login authentication.
                */

                SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.PREFS_NAME,0);
                SharedPreferences.Editor editor= sharedPreferences.edit();

                editor.putBoolean("hasLoggedIn",true);
                editor.putString("appID",appID);
                editor.putString("database",database);
                editor.putString("collection", collection);
                editor.commit();

                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}