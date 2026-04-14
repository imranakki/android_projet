package com.imraniman.projet_android;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StorageHelper {

    private static final String PREFS_NAME    = "student_data";

    public static final String KEY_NOM        = "nom";
    public static final String KEY_PRENOM     = "prenom";
    public static final String KEY_CLASSE     = "classe";
    public static final String KEY_REMARQUES  = "remarques";
    public static final String KEY_PHONE      = "phone";
    private static final String KEY_NOTES     = "notes_json";

    public static void saveProfile(Context ctx,
                                   String nom, String prenom,
                                   String classe, String remarques, String phone) {
        prefs(ctx).edit()
                .putString(KEY_NOM,       nom)
                .putString(KEY_PRENOM,    prenom)
                .putString(KEY_CLASSE,    classe)
                .putString(KEY_REMARQUES, remarques)
                .putString(KEY_PHONE,     phone)
                .apply();
    }

    public static String loadField(Context ctx, String key) {
        return prefs(ctx).getString(key, "");
    }

    public static void saveNotes(Context ctx, ArrayList<Note> notes) {
        JSONArray arr = new JSONArray();
        for (Note n : notes) {
            try {
                JSONObject obj = new JSONObject();
                obj.put("matiere", n.getMatiere());
                obj.put("note",    n.getNote());
                arr.put(obj);
            } catch (JSONException ignored) { }
        }
        prefs(ctx).edit().putString(KEY_NOTES, arr.toString()).apply();
    }

    public static ArrayList<Note> loadNotes(Context ctx) {
        ArrayList<Note> list = new ArrayList<>();
        String json = prefs(ctx).getString(KEY_NOTES, "[]");
        try {
            JSONArray arr = new JSONArray(json);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                list.add(new Note(obj.getString("matiere"), obj.getDouble("note")));
            }
        } catch (JSONException ignored) { }
        return list;
    }

    private static SharedPreferences prefs(Context ctx) {
        return ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
}

