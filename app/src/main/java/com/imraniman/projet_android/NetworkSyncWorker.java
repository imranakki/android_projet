package com.imraniman.projet_android;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class NetworkSyncWorker extends Worker {

    private static final String TAG = "NetworkSyncWorker";
    private static final String SYNC_URL = "https://belatar.name/rest/gps_sync.php";

    public NetworkSyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "Starting network sync for GPS data...");

        GpsDatabaseHelper dbHelper = new GpsDatabaseHelper(getApplicationContext());
        List<GpsDatabaseHelper.GpsPosition> positions = dbHelper.getAllPositions();

        if (positions.isEmpty()) {
            Log.d(TAG, "No GPS positions to sync.");
            return Result.success();
        }

        try {

            JSONArray jsonArray = new JSONArray();
            List<Integer> idsToDelete = new ArrayList<>();

            for (GpsDatabaseHelper.GpsPosition pos : positions) {
                JSONObject obj = new JSONObject();
                obj.put("id", pos.id);
                obj.put("latitude", pos.latitude);
                obj.put("longitude", pos.longitude);
                obj.put("timestamp", pos.timestamp);
                jsonArray.put(obj);
                idsToDelete.add(pos.id);
            }

            URL url = new URL(SYNC_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            String jsonString = jsonArray.toString();
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int code = conn.getResponseCode();
            if (code >= 200 && code < 300) {
                Log.d(TAG, "Sync successful. Deleting local records.");

                dbHelper.deletePositions(idsToDelete);
                return Result.success();
            } else {
                Log.e(TAG, "Sync failed with HTTP code: " + code);
                return Result.retry();
            }

        } catch (Exception e) {
            Log.e(TAG, "Error during sync: " + e.getMessage());
            return Result.retry();
        }
    }
}
