package com.s199219.MatteMoro;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

public class InnstillingerAktivitet extends AppCompatActivity {
    @SuppressLint("SetTextI18n")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.innstillingeraktivitet);

        // Hent delte preferanser for å lagre og hente innstillinger
        final SharedPreferences deltePreferanser = PreferenceManager.getDefaultSharedPreferences(this);

        // Konfigurer spinner for antall spørsmål
        konfigurerSpinner(R.id.spinnerAntallSporsmol, R.array.antallSporsmol, "antall_sporrsmal", "5");

        // Konfigurer spinner for språkvalg
        konfigurerSprakSpinner(R.id.spinnerSprok, R.array.sprok, "sprak_valg", "no");

        // Konfigurer Lagre-knappen
        konfigurerLagreKnapp(R.id.lagreKnapp, R.id.lagreBekreftelse);

        // Konfigurer Tilbake-knappen
        Button tilbakeKnapp = findViewById(R.id.tilbakeKnapp2);
        tilbakeKnapp.setOnClickListener(v -> finish()); // Avslutter denne aktiviteten og går tilbake til hovedaktiviteten
    }

    // Metode for å konfigurere en spinner med gitt ID og ressurs-ID for data, samt lagringsnøkkel og standardverdi
    private void konfigurerSpinner(int spinnerId, int arrayResId, String saveKey, String defaultValue) {
        Spinner spinner = findViewById(spinnerId);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, arrayResId, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String tidligereValg = sharedPreferences.getString(saveKey, defaultValue);
        spinner.setSelection(adapter.getPosition(tidligereValg));
    }

    // Metode for å konfigurere språkspinner med gitt ID, ressurs-ID for data, lagringsnøkkel og standardverdi
    private void konfigurerSprakSpinner(int spinnerId, int arrayResId, String saveKey, String defaultValue) {
        Spinner spinner = findViewById(spinnerId);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, arrayResId, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String tidligereSprakKode = sharedPreferences.getString(saveKey, defaultValue);
        String tidligereSprak = "no".equals(tidligereSprakKode) ? getString(R.string.norsk) : getString(R.string.tysk);
        spinner.setSelection(adapter.getPosition(tidligereSprak));
    }

    // Metode for å konfigurere Lagre-knappen
    private void konfigurerLagreKnapp(int buttonId, int textViewId) {
        Button lagreKnapp = findViewById(buttonId);
        TextView lagreBekreftelse = findViewById(textViewId);
        Spinner spinnerAntallSporsmol = findViewById(R.id.spinnerAntallSporsmol);
        Spinner spinnerSprok = findViewById(R.id.spinnerSprok);

        lagreKnapp.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            // Lagrer valgt antall spørsmål
            editor.putString("antall_sporrsmal", spinnerAntallSporsmol.getSelectedItem().toString());

            // Lagrer valgt språk
            String valgtSprak = spinnerSprok.getSelectedItem().toString();
            String sprakKode = "no"; // Setter standard til norsk
            if (getString(R.string.tysk).equals(valgtSprak)) {
                sprakKode = "de"; // Setter til tysk hvis valgt
            }

            editor.putString("sprak_valg", sprakKode);
            editor.apply();

            // Oppdaterer språkinnstillingen for appen
            SprokAktivitet.settSprok(this);

            // Viser en bekreftelsesmelding
            lagreBekreftelse.setText(R.string.innstillinger);
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
