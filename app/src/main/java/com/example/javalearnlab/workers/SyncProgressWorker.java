package com.example.javalearnlab.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.javalearnlab.data.local.AppDatabase;
import com.example.javalearnlab.data.local.PendingProgress;
import com.example.javalearnlab.utils.ApiClient;
import com.example.javalearnlab.utils.ServerManager;

import org.json.JSONObject;

import java.util.List;

public class SyncProgressWorker extends Worker {

    public SyncProgressWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        Context context = getApplicationContext();
        AppDatabase db = AppDatabase.getInstance(context);
        List<PendingProgress> pendingList = db.progressDao().getAll();

        if (pendingList.isEmpty()) {
            return Result.success();
        }

        for (PendingProgress item : pendingList) {
            try {
                JSONObject body = new JSONObject();
                body.put("email", item.email);
                body.put("key", item.key);
                body.put("value", item.value);

                String response = ApiClient.post(ServerManager.getBaseUrl(context) + "/api/progress/save", body);

                if (response != null) {
                    // Успешно отправлено – удаляем из очереди
                    db.progressDao().delete(item);
                } else {
                    // Ошибка сети – оставляем в очереди для следующей попытки
                }
            } catch (Exception e) {
                e.printStackTrace();
                // Ошибка при отправке, оставляем запись
            }
        }

        return Result.success();
    }
}
