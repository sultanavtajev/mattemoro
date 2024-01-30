package com.s199219.MatteMoro;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class OmSpillAktivitet extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SprokAktivitet.settSprok(this);  // Setter språket
        setContentView(R.layout.omaktivitet);  // Setter layout

        // Konfigurerer Tilbake-knappen
        konfigurerTilbakeKnapp(R.id.tilbakeKnapp, HovedAktivitet.class);
    }

    // Metode for å konfigurere Tilbake-knappen
    private void konfigurerTilbakeKnapp(int knappId, Class<?> maalAktivitet) {
        Button tilbakeKnapp = findViewById(knappId);
        tilbakeKnapp.setOnClickListener(view -> {
            // Starter hovedaktiviteten
            Intent intent = new Intent(OmSpillAktivitet.this, maalAktivitet);
            startActivity(intent);

            // Avslutter nåværende aktivitet
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        // Viser en dialogboks for å bekrefte avslutning av appen
        visBekreftAvslutningsDialog();
    }

    // Metode for å vise en dialogboks for å bekrefte avslutning av appen
    private void visBekreftAvslutningsDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Bekreft avslutning")
                .setMessage("Er du sikker på at du vil avslutte?")
                .setNegativeButton("Nei, bli i appen", (dialog, hvilken) -> {
                    // Ikke gjør noe, bare lukk dialogen
                })
                .setPositiveButton("Ja, avslutt", (dialog, hvilken) -> super.onBackPressed())
                .show();
    }
}
