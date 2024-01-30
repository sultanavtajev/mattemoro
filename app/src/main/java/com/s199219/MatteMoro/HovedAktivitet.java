package com.s199219.MatteMoro;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class HovedAktivitet extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SprokAktivitet.settSprok(this);

        setContentView(R.layout.hovedaktivitet);

        // Oppretter knapper og legger til klikk-lyttere
        konfigurerKnapp(R.id.startSpillKnapp, SpillAktivitet.class);
        konfigurerKnapp(R.id.omSpillKnapp, OmSpillAktivitet.class);
        konfigurerKnapp(R.id.innstillingerKnapp, InnstillingerAktivitet.class);
    }

    // Metode for å konfigurere knappene med gitt ID og målaktivitet
    private void konfigurerKnapp(int knappId, Class<?> maalAktivitet) {
        Button knapp = findViewById(knappId);
        knapp.setOnClickListener(v -> {
            Intent intensjon = new Intent(HovedAktivitet.this, maalAktivitet);
            startActivity(intensjon);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
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
