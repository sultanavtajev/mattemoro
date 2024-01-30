package com.s199219.MatteMoro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SpillAktivitet extends AppCompatActivity {
    // Deklarerer variabler og lister for spillet
    private ArrayList<String> alleSporsmolListe, alleHjelpListe, alleSvarListe;
    private final Random random = new Random();
    ProgressBar fremdriftBar;
    private TextView sporsmolText, tilbakemeldingText;
    private EditText svarInput;
    private Button hjelpKnapp, sjekkSvarKnapp, provKnapp, nesteKnapp, resultatKnapp, tilbakeKnapp5;
    private Button knapp0, knapp1, knapp2, knapp3, knapp4, knapp5, knapp6, knapp7, knapp8, knapp9, knappAngre;
    private int maxAntallSporrsmal;
    long startTid, sluttTid, spillTid;
    List<Integer> brukteIndekser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Setter layouten for denne aktiviteten
        setContentView(R.layout.spillaktivitet);
        // Setter språk
        SprokAktivitet.settSprok(this);
        // Initialiser tastatur
        initialiserTastatur();
        // Initialiser variabler
        initialiserVariabler();
        // Last inn alle spørsmål, svar og hjelpetekster
        sporsmolHjelp();
        // Lagrer starttidspunktet for spillet
        startTid = System.currentTimeMillis();
        // Hent valgt antall spørsmål fra SharedPreferences
        hentShared();
        // Starter spillet ved å vise det første spørsmålet
        nesteSporsmol();
    }

    // Initialiserer tastaturet ved å sette opp knappene og deres funksjonalitet
    private void initialiserTastatur() {
        knapp0 = findViewById(R.id.knapp0);
        knapp1 = findViewById(R.id.knapp1);
        knapp2 = findViewById(R.id.knapp2);
        knapp3 = findViewById(R.id.knapp3);
        knapp4 = findViewById(R.id.knapp4);
        knapp5 = findViewById(R.id.knapp5);
        knapp6 = findViewById(R.id.knapp6);
        knapp7 = findViewById(R.id.knapp7);
        knapp8 = findViewById(R.id.knapp8);
        knapp9 = findViewById(R.id.knapp9);
        knappAngre = findViewById(R.id.knappAngre);

        knapp0.setOnClickListener(view -> leggNrTilInput("0"));

        knapp1.setOnClickListener(view -> leggNrTilInput("1"));

        knapp2.setOnClickListener(view -> leggNrTilInput("2"));

        knapp3.setOnClickListener(view -> leggNrTilInput("3"));

        knapp4.setOnClickListener(view -> leggNrTilInput("4"));

        knapp5.setOnClickListener(view -> leggNrTilInput("5"));

        knapp6.setOnClickListener(view -> leggNrTilInput("6"));

        knapp7.setOnClickListener(view -> leggNrTilInput("7"));

        knapp8.setOnClickListener(view -> leggNrTilInput("8"));

        knapp9.setOnClickListener(view -> leggNrTilInput("9"));

        knappAngre.setOnClickListener(view -> angreSisteTall());
    }

    // Initialiserer variabler og setter opp knappefunksjonalitet
    private void initialiserVariabler() {
        // Finn alle views og knytte dem til variabler
        fremdriftBar = findViewById(R.id.fremdriftBar);
        sporsmolText = findViewById(R.id.sporsmolText);
        svarInput = findViewById(R.id.svarInput);
        hjelpKnapp = findViewById(R.id.hjelpKnapp);
        sjekkSvarKnapp = findViewById(R.id.sjekkSvarKnapp);
        provKnapp = findViewById(R.id.provKnapp);
        nesteKnapp = findViewById(R.id.nesteKnapp);
        resultatKnapp = findViewById(R.id.resultatKnapp);
        tilbakeKnapp5 = findViewById(R.id.tilbakeKnapp5);
        tilbakemeldingText = findViewById(R.id.tilbakemeldingText);

        // Setter opp klikk-lyttere for hver knapp
        sjekkSvarKnapp.setOnClickListener(view -> sjekkSvar());
        hjelpKnapp.setOnClickListener(view -> hjelp());

        provKnapp.setOnClickListener(view -> {
            provKnapp.setVisibility(View.GONE);
            nesteKnapp.setVisibility(View.GONE);
            hjelpKnapp.setVisibility(View.VISIBLE);
            sjekkSvarKnapp.setVisibility(View.VISIBLE);
            svarInput.setText("");
            tilbakemeldingText.setText("");
        });

        nesteKnapp.setOnClickListener(view -> {
            alleSporsmolListe.remove(indeks);
            alleHjelpListe.remove(indeks);
            alleSvarListe.remove(indeks);
            provKnapp.setVisibility(View.GONE);
            nesteKnapp.setVisibility(View.GONE);
            hjelpKnapp.setVisibility(View.VISIBLE);
            sjekkSvarKnapp.setVisibility(View.VISIBLE);
            svarInput.setText("");
            tilbakemeldingText.setText("");
            nesteSporsmol();
        });

        resultatKnapp.setOnClickListener(view -> {
            alleSporsmolListe.remove(indeks);
            alleHjelpListe.remove(indeks);
            alleSvarListe.remove(indeks);
            Intent intent = new Intent(SpillAktivitet.this, ResultatAktivitet.class);
            intent.putExtra("spillTid", spillTid);
            intent.putExtra("totaltAntall", totaltAntall);
            intent.putExtra("riktigeAntall", riktigAntall);
            intent.putExtra("feilAntall", feilAntall);
            startActivity(intent);
            finish();
        });

        // Legg til en klikk-lytter for knappen som starter spillet
        tilbakeKnapp5.setOnClickListener(v -> {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = prefs.edit();

            // Nullstiller SharedPreferences
            editor.remove("brukteIndekser");
            editor.apply();

            // Oppretter en intet for å starte SpillAktivitet
            Intent gameIntent = new Intent(SpillAktivitet.this, HovedAktivitet.class);
            startActivity(gameIntent);
        });
    }

    // Metode for å legge til et nummer til svarInput-feltet
    private void leggNrTilInput(String number) {
        // Henter den nåværende teksten i svarInput
        String currentInput = svarInput.getText().toString();
        // Legger til det nye nummeret til den eksisterende teksten
        currentInput += number;
        // Setter den oppdaterte teksten tilbake i svarInput
        svarInput.setText(currentInput);
    }

    // Metode for å fjerne det siste tallet i svarInput-feltet
    private void angreSisteTall() {
        // Henter den nåværende teksten i svarInput
        String currentInput = svarInput.getText().toString();

        // Sjekker om teksten er lengre enn 0 tegn
        if (currentInput.length() > 0) {
            // Fjerner det siste tegnet fra teksten
            currentInput = currentInput.substring(0, currentInput.length() - 1);

            // Setter den oppdaterte teksten tilbake i svarInput
            svarInput.setText(currentInput);
        }
    }


    // Metode for å hente brukerpreferanser fra SharedPreferences
    private void hentShared() {
        // Henter SharedPreferences-objektet for denne aktiviteten
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Henter verdien for nøkkelen "antall_sporrsmal" fra SharedPreferences
        // Hvis nøkkelen ikke finnes, blir standardverdien satt til "5"
        String valgtAntallSporrsmal = sharedPreferences.getString("antall_sporrsmal", "5");

        // Konverterer den hentede strengverdien til en heltallsverdi og lagrer den i maxAntallSporrsmal
        maxAntallSporrsmal = Integer.parseInt(valgtAntallSporrsmal);
    }


    private void sporsmolHjelp() {
        brukteIndekser = new ArrayList<>();
        hentBrukteSporsmal();

        if (brukteIndekser == null || brukteIndekser.isEmpty()) {
            // Last inn alle tilgjengelige spørsmål
            String[] alleSporsmol = getResources().getStringArray(R.array.sporsmol);
            alleSporsmolListe = new ArrayList<>(Arrays.asList(alleSporsmol));

            // Last inn alle tilgjengelige hjelpetekster
            String[] alleHjelp = getResources().getStringArray(R.array.hjelpetekst);
            alleHjelpListe = new ArrayList<>(Arrays.asList(alleHjelp));

            // Last inn alle tilgjengelige svar
            String[] alleSvar = getResources().getStringArray(R.array.svar);
            alleSvarListe = new ArrayList<>(Arrays.asList(alleSvar));
            return;
        }
        // Last inn alle tilgjengelige spørsmål
        String[] alleSporsmol = getResources().getStringArray(R.array.sporsmol);
        alleSporsmolListe = new ArrayList<>(Arrays.asList(alleSporsmol));

        // Last inn alle tilgjengelige hjelpetekster
        String[] alleHjelp = getResources().getStringArray(R.array.hjelpetekst);
        alleHjelpListe = new ArrayList<>(Arrays.asList(alleHjelp));

        // Last inn alle tilgjengelige svar
        String[] alleSvar = getResources().getStringArray(R.array.svar);
        alleSvarListe = new ArrayList<>(Arrays.asList(alleSvar));

        Collections.sort(brukteIndekser, Collections.reverseOrder());
        // Fjern elementer basert på indeks
        for (int index : brukteIndekser) {
            if (alleSporsmolListe != null && index < alleSporsmolListe.size()) {
                alleSporsmolListe.remove(index);
            }
            if (alleHjelpListe != null && index < alleHjelpListe.size()) {
                alleHjelpListe.remove(index);
            }
            if (alleSvarListe != null && index < alleSvarListe.size()) {
                alleSvarListe.remove(index);
            }
        }
    }

    private void nesteSporsmol() {
        // Genererer et nytt spørsmål
        String sporsmol = genererSporsmal();

        // Sjekker om et nytt spørsmål ble generert
        if (sporsmol != null) {
            // Setter det nye spørsmålet som tekst i sporsmolText view
            sporsmolText.setText(sporsmol);
        } else {
            // Hvis det ikke er flere spørsmål igjen, viser en melding og skjuler unødvendige elementer
            sporsmolText.setText(R.string.ingen_flere);
            svarInput.setText("");
            tilbakemeldingText.setText("");

            // Gjør diverse knapper og elementer usynlige
            hjelpKnapp.setVisibility(View.GONE);
            sjekkSvarKnapp.setVisibility(View.GONE);
            resultatKnapp.setVisibility(View.GONE);
            svarInput.setVisibility(View.GONE);
            fremdriftBar.setVisibility(View.GONE);
            knapp0.setVisibility(View.GONE);
            knapp1.setVisibility(View.GONE);
            knapp2.setVisibility(View.GONE);
            knapp3.setVisibility(View.GONE);
            knapp4.setVisibility(View.GONE);
            knapp5.setVisibility(View.GONE);
            knapp6.setVisibility(View.GONE);
            knapp7.setVisibility(View.GONE);
            knapp8.setVisibility(View.GONE);
            knapp9.setVisibility(View.GONE);
            knappAngre.setVisibility(View.GONE);

            // Gjør "Tilbake"-knappen synlig
            tilbakeKnapp5.setVisibility(View.VISIBLE);
        }

        // Resett status for om brukeren har forsøkt å svare på det neste spørsmålet
        forsokt = false;
    }


    int indeks;
    int antallSporsmol;

    private String genererSporsmal() {
        // Sjekker om det er flere spørsmål igjen i listen
        if (alleSporsmolListe.isEmpty()) {
            return null; // Ingen flere spørsmål igjen, returnerer null
        }

        // Genererer en tilfeldig indeks fra listen over alle spørsmål
        indeks = random.nextInt(alleSporsmolListe.size());

        // Henter spørsmålet ved den tilfeldige indeksen
        String sporsmol = alleSporsmolListe.get(indeks);

        // Legger til den brukte indeksen i listen over brukte indekser
        brukteIndekser.add(indeks);

        // Øker telleren for antall spørsmål som har blitt stilt
        antallSporsmol++;

        // Lagrer den oppdaterte listen over brukte spørsmål
        lagreBrukteSporsmal();

        // Returnerer det genererte spørsmålet
        return sporsmol;
    }


    private void hjelp() {
        // Henter hjelpeteksten fra listen 'alleHjelpListe' basert på indeksen til det aktuelle spørsmålet
        String hjelpTekst = alleHjelpListe.get(indeks);

        // Setter hjelpeteksten til å vises i 'tilbakemeldingText' TextView
        tilbakemeldingText.setText(hjelpTekst);
    }

    int totaltAntall;
    int riktigAntall;
    int feilAntall;
    boolean forsokt = false;

    private void sjekkSvar() {
        // Hvis dette er første forsøk på dette spørsmålet, oppdater totaltAntall
        if (!forsokt) {
            totaltAntall++;
        }

        // Henter gitt svar fra EditText-feltet
        String gittSvar = svarInput.getText().toString();

        // Valider at brukeren har skrevet noe
        if (TextUtils.isEmpty(gittSvar)) {
            // Viser en melding om at brukeren må gi et svar
            tilbakemeldingText.setText(R.string.vennligst_skriv_et_svar);
            return;
        }

        // Oppdaterer fremdriftsbaren basert på hvor mange spørsmål som er besvart
        int progress = (antallSporsmol * 100) / maxAntallSporrsmal;
        fremdriftBar.setProgress(progress);

        // Finn det riktige svaret basert på indeksen til det aktuelle spørsmålet
        String riktigSvar = alleSvarListe.get(indeks);

        // Sammenlign det gitt svaret med det riktige svaret
        if (gittSvar.equals(riktigSvar)) {
            // Sjekk om vi har nådd maks antall spørsmål
            if (antallSporsmol >= maxAntallSporrsmal) {
                sluttTid = System.currentTimeMillis();
                spillTid = sluttTid - startTid;

                if (!forsokt) {
                    riktigAntall++;
                }
                hjelpKnapp.setVisibility(View.GONE);
                sjekkSvarKnapp.setVisibility(View.GONE);
                resultatKnapp.setVisibility(View.VISIBLE);
                tilbakemeldingText.setText(R.string.resultat);
            } else {
                // Svaret er riktig
                if (!forsokt) {
                    riktigAntall++;
                }
                hjelpKnapp.setVisibility(View.GONE);
                sjekkSvarKnapp.setVisibility(View.GONE);
                nesteKnapp.setVisibility(View.VISIBLE);
                tilbakemeldingText.setText(R.string.riktig_svar_godt_gjort);
            }
        } else {
            // Sjekk om vi har nådd maks antall spørsmål
            if (antallSporsmol >= maxAntallSporrsmal) {
                sluttTid = System.currentTimeMillis();
                spillTid = sluttTid - startTid;

                if (!forsokt) {
                    feilAntall++;
                }
                hjelpKnapp.setVisibility(View.GONE);
                sjekkSvarKnapp.setVisibility(View.GONE);
                provKnapp.setVisibility(View.VISIBLE);
                resultatKnapp.setVisibility(View.VISIBLE);
                tilbakemeldingText.setText(R.string.test_ferdig);
            } else {
                // Svaret er feil
                if (!forsokt) {
                    feilAntall++;
                }
                hjelpKnapp.setVisibility(View.GONE);
                sjekkSvarKnapp.setVisibility(View.GONE);
                provKnapp.setVisibility(View.VISIBLE);
                nesteKnapp.setVisibility(View.VISIBLE);
                tilbakemeldingText.setText(R.string.feil_svar_prov);
            }
        }
        // Merk dette spørsmålet som forsøkt
        forsokt = true;
    }

    // Lagrer indeksene for brukte spørsmål i SharedPreferences
    private void lagreBrukteSporsmal() {
        // Får tilgang til SharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();

        // Konverterer listen av indekser til en kommaseparert streng
        String spm = TextUtils.join(",", brukteIndekser);

        // Lagrer denne strengen i SharedPreferences
        editor.putString("brukteIndekser", spm);
        editor.apply();
    }

    // Henter tidligere brukte spørsmål fra SharedPreferences
    private void hentBrukteSporsmal() {
        // Får tilgang til SharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Henter den lagrede kommaseparerte strengen
        String storedIndicesString = prefs.getString("brukteIndekser", "");

        // Sjekker om strengen er tom eller null
        if (storedIndicesString == null || storedIndicesString.isEmpty()) {
            // Initialiserer 'brukteIndekser' som en tom ArrayList
            brukteIndekser = new ArrayList<>();
            return;
        }

        // Deler opp den lagrede strengen til en liste
        List<String> indicesList = Arrays.asList(storedIndicesString.split(","));
        brukteIndekser = new ArrayList<>();

        try {
            // Konverterer hver streng i listen til et heltall og legger det til i 'brukteIndekser'
            for (String s : indicesList) {
                if (!s.isEmpty()) { // Ekstra sjekk for å unngå NumberFormatException
                    brukteIndekser.add(Integer.parseInt(s));
                }
            }
            // For debugging: Skriver ut listen 'brukteIndekser'
            System.out.println("brukteIndekser ut: " + brukteIndekser);
        } catch (NumberFormatException e) {
            // Logg feilen
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        // Oppretter en ny AlertDialog når brukeren trykker på tilbake-knappen
        new AlertDialog.Builder(this)
                .setTitle("Bekreft avslutning")  // Setter tittelen på dialogen
                .setMessage("Er du sikker på at du vil avslutte?")  // Setter meldingen i dialogen
                .setNegativeButton("Nei, bli i appen", (dialog, which) -> {
                    // Hvis brukeren trykker "Nei, bli i appen", lukkes bare dialogen og ingenting mer skjer
                })
                .setPositiveButton("Ja, avslutt", (arg0, arg1) -> SpillAktivitet.super.onBackPressed())
                // Hvis brukeren trykker "Ja, avslutt", kaller vi på superklassens onBackPressed for å avslutte aktiviteten
                .show();  // Viser dialogen til brukeren
    }
}