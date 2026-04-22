package com.example.javalearnlab.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.javalearnlab.R;
import com.example.javalearnlab.utils.ApiClient;
import com.example.javalearnlab.utils.AuthManager;
import com.example.javalearnlab.utils.ServerManager;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private EditText nameEdit, emailEdit, passwordEdit;
    private Button registerButton, goLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameEdit = findViewById(R.id.name);
        emailEdit = findViewById(R.id.email);
        passwordEdit = findViewById(R.id.password);
        registerButton = findViewById(R.id.registerBtn);

        registerButton.setOnClickListener(v -> attemptRegister());
    }

    private void attemptRegister() {
        final String name = nameEdit.getText().toString().trim();
        final String email = emailEdit.getText().toString().trim();
        final String password = passwordEdit.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            nameEdit.setError("Введите имя");
            nameEdit.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            emailEdit.setError("Введите email");
            emailEdit.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            passwordEdit.setError("Введите пароль");
            passwordEdit.requestFocus();
            return;
        }

        registerButton.setEnabled(false);
        registerButton.setText("Регистрация...");

        final RegisterActivity context = this;

        new Thread(() -> {
            try {
                JSONObject body = new JSONObject();
                body.put("name", name);
                body.put("email", email);
                body.put("password", password);

                String response = ApiClient.post(ServerManager.getBaseUrl(context) + "/register", body);

                runOnUiThread(() -> {
                    registerButton.setEnabled(true);
                    registerButton.setText("Зарегистрироваться");

                    if (response != null) {
                        try {
                            JSONObject json = new JSONObject(response);
                            String userName = json.getString("name");
                            AuthManager.login(context, userName, email);
                            Toast.makeText(context, "Регистрация успешна!", Toast.LENGTH_SHORT).show();
                            finish();
                        } catch (JSONException e) {
                            Toast.makeText(context, "Ошибка ответа сервера", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "Ошибка соединения", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    registerButton.setEnabled(true);
                    registerButton.setText("Зарегистрироваться");
                    Toast.makeText(context, "Ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }
}