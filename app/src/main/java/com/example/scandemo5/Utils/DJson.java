package com.example.scandemo5.Utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sam on 2017/8/28.
 */

public class DJson {

    private static Gson gson = new Gson();

    public static String ObjectToJson(Object object){
        return gson.toJson(object);
    }

    public static <T> List<T> JsonToList(String Json, Class<T> tClass){
        List<T> list = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(Json).getAsJsonArray();
        for(final JsonElement elem : array){
            list.add(gson.fromJson(elem, tClass));
        }
        return list;
    }
}
