package com.example.android.bakr.model;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by conde on 5/12/2018.
 */

public class Utils {


    //Reads and return Json file that will then be parsed
    public static String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("baking.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static List<Dish> getDishes(Context context, String data) {
        List<Dish> dishes = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(data);
            for(int i = 0; i<jsonArray.length(); i++){
            JSONObject dishObject = jsonArray.getJSONObject(i);
                dishes.add(new Dish(dishObject.getString("name"),
                        dishObject.getInt("id"),
                        dishObject.getString("image")));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dishes;
    }

    //Returns a specific dish on the Json file.
    //Used to retrieve infos for the dish that is clicked on.
    public static List<Ingredient> getIngredients(Context context, String data , String name){
        List<Ingredient> ingredients = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(data);
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject dishObject = jsonArray.getJSONObject(i);
                if(dishObject.getString("name").equals(name)){
                    JSONArray ingredientArray = dishObject.getJSONArray("ingredients");
                    for(int n = 0; n < jsonArray.length(); n++){
                        JSONObject ingredientObject = ingredientArray.getJSONObject(n);
                        ingredients.add(new Ingredient(ingredientObject.getString("ingredient"),
                                ingredientObject.getInt("quantity"),
                                ingredientObject.getString("measure")));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ingredients;
    }

    //Petty much the same as 'getIngredients'. This one retrieves the steps
    public static List<Step> getSteps(Context context, String data, String name){
        List<Step> steps = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(data);
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject dishObject = jsonArray.getJSONObject(i);
                if(dishObject.getString("name").equals(name)){
                    JSONArray stepsArray = dishObject.getJSONArray("steps");
                    for(int n = 0; i < stepsArray.length(); n++){
                        JSONObject stepsObject = stepsArray.getJSONObject(n);
                        steps.add(new Step(stepsObject.getInt("id"),
                                stepsObject.getString("shortDescription"),
                                stepsObject.getString("description"),
                                stepsObject.getString("videoURL"),
                                stepsObject.getString("thumbnailURL")
                                ));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return steps;
    }

}
