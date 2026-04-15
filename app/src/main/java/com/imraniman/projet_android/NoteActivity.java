package com.imraniman.projet_android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class NoteActivity extends AppCompatActivity {

    public static final String EXTRA_STUDENT_NAME = "student_name";
    public static final String RESULT_NOTE_MATIERE = "result_matiere";
    public static final String RESULT_NOTE_SCORE = "result_score";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        String studentName = getIntent().getStringExtra(EXTRA_STUDENT_NAME);

        TextView tvStudentName = findViewById(R.id.tvStudentName);
        if (studentName != null && !studentName.trim().isEmpty()) {
            tvStudentName.setText("Note de " + studentName);
        }

        TextInputEditText editMatiere = findViewById(R.id.editTextMatiere);
        TextInputEditText editScore = findViewById(R.id.editTextScore);
        MaterialButton btnConfirm = findViewById(R.id.buttonAddNote);

        btnConfirm.setOnClickListener(
                v -> {
                    String matiere =
                            editMatiere.getText() != null
                                    ? editMatiere.getText().toString().trim()
                                    : "";
                    String scoreStr =
                            editScore.getText() != null
                                    ? editScore.getText().toString().trim()
                                    : "";

                    if (matiere.isEmpty() || scoreStr.isEmpty()) {
                        Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT)
                                .show();
                        return;
                    }

                    double score;
                    try {
                        score = Double.parseDouble(scoreStr);
                        if (score < 0 || score > 20) {
                            Toast.makeText(
                                            this,
                                            "La note doit être entre 0 et 20",
                                            Toast.LENGTH_SHORT)
                                    .show();
                            return;
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "Note invalide", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Intent result = new Intent();
                    result.putExtra(RESULT_NOTE_MATIERE, matiere);
                    result.putExtra(RESULT_NOTE_SCORE, score);
                    setResult(RESULT_OK, result);
                    finish();
                });
    }
}
