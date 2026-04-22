package com.example.javalearnlab.utils;

import android.content.Context;

import com.example.javalearnlab.theory.model.Question;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.example.javalearnlab.R;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TestRepository {

    public static List<Question> getQuestions(Context context, int topicId) {
        try {
            InputStream is = context.getResources().openRawResource(R.raw.tests);

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(is, StandardCharsets.UTF_8)
            );

            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            String json = sb.toString();

            JsonObject root = JsonParser.parseString(json).getAsJsonObject();
            JsonArray topics = root.getAsJsonArray("topics");

            Gson gson = new Gson();

            for (JsonElement el : topics) {
                JsonObject obj = el.getAsJsonObject();

                if (obj.get("topic_id").getAsInt() == topicId) {

                    Type type = new TypeToken<List<Question>>(){}.getType();
                    return gson.fromJson(obj.get("questions"), type);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }
}