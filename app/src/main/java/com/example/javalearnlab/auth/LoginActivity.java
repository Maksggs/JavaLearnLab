package com.example.javalearnlab.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.javalearnlab.R;
import com.example.javalearnlab.utils.ApiClient;
import com.example.javalearnlab.utils.AuthManager;
import com.example.javalearnlab.utils.ProgressManager;
import com.example.javalearnlab.utils.ServerManager;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEdit, passwordEdit;
    private Button loginButton, goRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEdit = findViewById(R.id.email);
        passwordEdit = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginBtn);
        goRegisterButton = findViewById(R.id.goRegister);

        goRegisterButton.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class))
        );

        loginButton.setOnClickListener(v -> attemptLogin());
    }

    private void attemptLogin() {
        final String email = emailEdit.getText().toString().trim();
        final String password = passwordEdit.getText().toString().trim();

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

        loginButton.setEnabled(false);
        loginButton.setText("Вход...");

        final LoginActivity context = this;

        new Thread(() -> {
            try {
                JSONObject body = new JSONObject();
                body.put("email", email);
                body.put("password", password);

                String response = ApiClient.post(ServerManager.getBaseUrl(context) + "/login", body);

                runOnUiThread(() -> {
                    loginButton.setEnabled(true);
                    loginButton.setText("Войти");

                    if (response != null) {
                        try {
                            JSONObject json = new JSONObject(response);
                            String name = json.getString("name");
                            AuthManager.login(context, name, email);
                            // Загружаем прогресс именно этого аккаунта с сервера
                            ProgressManager.syncFromServer(context, () -> {
                                Toast.makeText(context, "Добро пожаловать, " + name + "!", Toast.LENGTH_SHORT).show();
                                finish();
                            });
                        } catch (JSONException e) {
                            Toast.makeText(context, "Ошибка ответа сервера", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "Неверный email или пароль", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    loginButton.setEnabled(true);
                    loginButton.setText("Войти");
                    Toast.makeText(context, "Ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }
}