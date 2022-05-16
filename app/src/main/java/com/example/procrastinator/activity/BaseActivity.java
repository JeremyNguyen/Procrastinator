package com.example.procrastinator.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.procrastinator.R;
import com.example.procrastinator.constant.AppConstant;
import com.example.procrastinator.util.DatabaseUtil;
import com.google.firebase.firestore.FirebaseFirestore;

public class BaseActivity extends AppCompatActivity {

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = FirebaseFirestore.getInstance();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (R.id.debug == item.getItemId()) {
            debug();
        }
        if (R.id.viewTasks == item.getItemId()) {
            Intent intent = new Intent(this, ListTasksActivity.class);
            startActivity(intent);
        }
        if (R.id.setUser == item.getItemId()) {
            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
            View mView = layoutInflaterAndroid.inflate(R.layout.dialog_set_user, null);
            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
            alertDialogBuilderUserInput.setView(mView);
            final EditText editText = mView.findViewById(R.id.setUserInput);
            alertDialogBuilderUserInput
                    .setCancelable(false)
                    .setPositiveButton("Confirm", (dialogBox, id) -> {
                        String input = editText.getText().toString();
                        if ("Jeremy".equals(input) || "Noemie".equals(input)) {
                            SharedPreferences settings = getSharedPreferences(AppConstant.PREFERENCES_NAME, 0);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString(AppConstant.PREFERENCE_USER, input);
                            editor.apply();
                            Toast.makeText(this, "Username set !", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Username must be \"Jeremy\" or \"Noemie\"", Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton("Cancel", (dialogBox, id) -> dialogBox.cancel());
            AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
            alertDialogAndroid.show();
        }
        if (R.id.export == item.getItemId()) {
            Intent intent = new Intent(this, ExportActivity.class);
            startActivity(intent);
        }
        return (super.onOptionsItemSelected(item));
    }

    public void debug() {
        DatabaseUtil.getTasksSendReminders(getApplicationContext(), db);
    }
}