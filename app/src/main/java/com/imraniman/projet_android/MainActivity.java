package com.imraniman.projet_android;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private static final String API_URL =
            "https://belatar.name/rest/profile.php?login=test&passwd=test&id=9998&notes=true";
    private static final String BASE_PHOTO_URL = "https://belatar.name/images/";

    private static final int LOCATION_PERMISSION_REQUEST = 100;

    private ImageView networkImageViewPhoto;
    private ImageView imageViewFallback;
    private EditText editTextNom, editTextPrenom, editTextClasse, editTextRemarques;
    private MaterialButton buttonEnregistrer, buttonAppeler, buttonAddNote, buttonVoirNotes;
    private ListView listViewNotes;

    private String phoneNumber = "";
    private ArrayList<Note> noteList = new ArrayList<>();
    private NoteAdapter adapter;

    private ActivityResultLauncher<Intent> noteActivityLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noteActivityLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        result -> {
                            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                                Intent data = result.getData();
                                String matiere =
                                        data.getStringExtra(NoteActivity.RESULT_NOTE_MATIERE);
                                double score =
                                        data.getDoubleExtra(NoteActivity.RESULT_NOTE_SCORE, 0);
                                noteList.add(new Note(matiere, score));
                                StorageHelper.saveNotes(this, noteList);
                                if (adapter != null) adapter.notifyDataSetChanged();
                            }
                        });

        networkImageViewPhoto = findViewById(R.id.networkImageViewPhoto);
        imageViewFallback = findViewById(R.id.imageViewFallback);
        editTextNom = findViewById(R.id.editTextNom);
        editTextPrenom = findViewById(R.id.editTextPrenom);
        editTextClasse = findViewById(R.id.editTextClasse);
        editTextRemarques = findViewById(R.id.editTextRemarques);
        buttonEnregistrer = findViewById(R.id.buttonEnregistrer);
        buttonAppeler = findViewById(R.id.buttonAppeler);
        buttonAddNote = findViewById(R.id.buttonAddNote);
        buttonVoirNotes = findViewById(R.id.buttonVoirNotes);

        listViewNotes = findViewById(R.id.listViewNotes);

        noteList.addAll(StorageHelper.loadNotes(this));
        if (listViewNotes != null) {
            adapter = new NoteAdapter(this, noteList);
            listViewNotes.setAdapter(adapter);
        }
        loadLocalProfile();

        buttonEnregistrer.setOnClickListener(
                v -> {
                    StorageHelper.saveProfile(
                            this,
                            editTextNom.getText().toString().trim(),
                            editTextPrenom.getText().toString().trim(),
                            editTextClasse.getText().toString().trim(),
                            editTextRemarques.getText().toString().trim(),
                            phoneNumber);
                    Toast.makeText(this, R.string.toast_saved, Toast.LENGTH_SHORT).show();
                });

        buttonAppeler.setOnClickListener(
                v -> {
                    if (!phoneNumber.isEmpty()) {
                        startActivity(
                                new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber)));
                    } else {
                        Toast.makeText(this, R.string.toast_no_phone, Toast.LENGTH_SHORT).show();
                    }
                });

        buttonAddNote.setOnClickListener(
                v -> {
                    Intent intent = new Intent(this, NoteActivity.class);
                    intent.putExtra(
                            NoteActivity.EXTRA_STUDENT_NAME,
                            editTextNom.getText().toString().trim()
                                    + " "
                                    + editTextPrenom.getText().toString().trim());
                    noteActivityLauncher.launch(intent);
                });

        buttonVoirNotes.setOnClickListener(
                v -> {
                    Intent intent = new Intent(this, NotesViewerActivity.class);
                    intent.putExtra(
                            NotesViewerActivity.EXTRA_STUDENT_NAME,
                            editTextNom.getText().toString().trim()
                                    + " "
                                    + editTextPrenom.getText().toString().trim());
                    intent.putExtra(NotesViewerActivity.EXTRA_NOTES_LIST, noteList);
                    startActivity(intent);
                });

        fetchProfile();

        requestLocationPermissions();
        scheduleGpsSync();
    }

    private void loadLocalProfile() {
        String nom = StorageHelper.loadField(this, StorageHelper.KEY_NOM);
        String prenom = StorageHelper.loadField(this, StorageHelper.KEY_PRENOM);
        String classe = StorageHelper.loadField(this, StorageHelper.KEY_CLASSE);
        String remarques = StorageHelper.loadField(this, StorageHelper.KEY_REMARQUES);
        phoneNumber = StorageHelper.loadField(this, StorageHelper.KEY_PHONE);

        if (!nom.isEmpty()) editTextNom.setText(nom);
        if (!prenom.isEmpty()) editTextPrenom.setText(prenom);
        if (!classe.isEmpty()) editTextClasse.setText(classe);
        if (!remarques.isEmpty()) editTextRemarques.setText(remarques);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    private void fetchProfile() {
        if (!isNetworkAvailable()) {
            Toast.makeText(this, R.string.toast_no_network, Toast.LENGTH_LONG).show();
            return;
        }

        JsonObjectRequest request =
                new JsonObjectRequest(
                        API_URL,
                        null,
                        response -> {
                            try {
                                editTextNom.setText(response.getString("nom"));
                                editTextPrenom.setText(response.getString("prenom"));
                                editTextClasse.setText(response.getString("classe"));
                                phoneNumber = response.getString("phone");

                                String photoUrl = BASE_PHOTO_URL + response.getString("photo");
                                networkImageViewPhoto.setImageResource(android.R.color.transparent);

                                com.android.volley.toolbox.ImageRequest imgRequest =
                                        new com.android.volley.toolbox.ImageRequest(
                                                photoUrl,
                                                bitmap -> {
                                                    networkImageViewPhoto.setImageBitmap(bitmap);
                                                    if (imageViewFallback != null) {
                                                        imageViewFallback.setVisibility(
                                                                View.VISIBLE);
                                                    }
                                                },
                                                0,
                                                0,
                                                android.widget.ImageView.ScaleType.FIT_CENTER,
                                                null,
                                                error ->
                                                        android.util.Log.e(
                                                                "Image", "Failed to load image"));
                                VolleySingleton.getInstance(MainActivity.this)
                                        .getRequestQueue()
                                        .add(imgRequest);

                                if (response.has("notes") && !response.isNull("notes")) {
                                    JSONArray parsedNotes = response.optJSONArray("notes");
                                    if (parsedNotes != null) {

                                        noteList.clear();
                                        for (int i = 0; i < parsedNotes.length(); i++) {
                                            JSONObject noteObj = parsedNotes.getJSONObject(i);
                                            String label = noteObj.getString("label");
                                            double score = noteObj.getDouble("score");
                                            noteList.add(new Note(label, score));
                                        }

                                        StorageHelper.saveNotes(MainActivity.this, noteList);
                                    }
                                }

                                if (adapter != null) adapter.notifyDataSetChanged();

                            } catch (Exception e) {
                                Toast.makeText(
                                                this,
                                                R.string.toast_network_error,
                                                Toast.LENGTH_SHORT)
                                        .show();
                            }
                        },
                        error ->
                                Toast.makeText(
                                                this,
                                                R.string.toast_network_error,
                                                Toast.LENGTH_SHORT)
                                        .show());

        VolleySingleton.getInstance(this).getRequestQueue().add(request);
    }

    private void requestLocationPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[] {
                            Manifest.permission.POST_NOTIFICATIONS,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        },
                        LOCATION_PERMISSION_REQUEST);
                return;
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[] {
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    LOCATION_PERMISSION_REQUEST);
        } else {
            startTrackingService();
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startTrackingService();
        }
    }

    private void startTrackingService() {
        Intent serviceIntent = new Intent(this, TrackingService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
    }

    private void scheduleGpsSync() {
        Constraints constraints =
                new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();

        PeriodicWorkRequest syncWork =
                new PeriodicWorkRequest.Builder(NetworkSyncWorker.class, 15, TimeUnit.MINUTES)
                        .setConstraints(constraints)
                        .build();

        WorkManager.getInstance(this)
                .enqueueUniquePeriodicWork(
                        "GpsSyncWork", ExistingPeriodicWorkPolicy.KEEP, syncWork);
    }
}
