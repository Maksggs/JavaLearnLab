package com.example.javalearnlab.data;

import android.content.Context;

import com.example.javalearnlab.R;
import com.example.javalearnlab.theory.model.Topic;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TheoryRepository {

    public static List<Topic> loadTopics(Context context) {
        try {
            InputStream is = context.getResources().openRawResource(R.raw.theory);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String json = new String(buffer, "UTF-8");

            Gson gson = new Gson();
            JsonObject obj = JsonParser.parseString(json).getAsJsonObject();

            Type type = new TypeToken<List<Topic>>(){}.getType();
            return gson.fromJson(obj.get("topics"), type);

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
