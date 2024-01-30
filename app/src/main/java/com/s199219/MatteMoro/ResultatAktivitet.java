package com.s199219.MatteMoro;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class ResultatAktivitet extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resultataktivitet);
        SprokAktivitet.settSprok(this);  // Setter språket for aktiviteten

        // Konfigurerer Tilbake-knappen
        konfigurerTilbakeKnapp(R.id.tilbakeKnapp3, HovedAktivitet.class);

        // Henter og viser spillresultatene
        visSpillResultater();
    }

    // Metode for å konfigurere Tilbake-knappen
    private void konfigurerTilbakeKnapp(int knappId, Class<?> maalAktivitet) {
        Button tilbakeKnapp = findViewById(knappId);
        tilbakeKnapp.setOnClickListener(v -> {
            // Oppretter en Intent for å navigere til målaktiviteten
            Intent intent = new Intent(ResultatAktivitet.this, maalAktivitet);
            startActivity(intent);
        });
    }

    // Metode for å hente og vise spillresultater
    private void visSpillResultater() {
        TextView spillTidText = findViewById(R.id.spillTid);
        TextView totaltAntallText = findViewById(R.id.totaltAntall);
        TextView riktigeAntallText = findViewById(R.id.riktigeAntall);
        TextView feilAntallText = findViewById(R.id.feilAntall);

        Intent intent = getIntent();
        long spillTid = intent.getLongExtra("spillTid", 0);
        int totaltAntall = intent.getIntExtra("totaltAntall", 0);
        int riktigeAntall = intent.getIntExtra("riktigeAntall", 0);
        int feilAntall = intent.getIntExtra("feilAntall", 0);

        String formatertTid = konverterMillisTilMinSek(spillTid);
        spillTidText.setText(getString(R.string.tid_brukt2, formatertTid));
        totaltAntallText.setText(getString(R.string.totalt_sp_rsm_l, totaltAntall));
        riktigeAntallText.setText(getString(R.string.riktige_svar, riktigeAntall));
        feilAntallText.setText(getString(R.string.feil_svar, feilAntall));
    }

    // Metode for å konvertere millisekunder til minutter og sekunder
    public String konverterMillisTilMinSek(long spillTid) {
        long totalSekunder = spillTid / 1000;
        long minutter = totalSekunder / 60;
        long sekunder = totalSekunder % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", minutter, sekunder);
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
