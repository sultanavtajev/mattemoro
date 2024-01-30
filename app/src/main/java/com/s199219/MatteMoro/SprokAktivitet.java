package com.s199219.MatteMoro;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;
import androidx.preference.PreferenceManager;

public class SprokAktivitet {
    // Metode for å sette applikasjonens språk
    public static void settSprok(Context context) {
        // Henter delte preferanser for å få tilgang til tidligere lagrede innstillinger
        SharedPreferences deltePreferanser = hentDeltePreferanser(context);

        // Henter språkkoden som er lagret i delte preferanser ("no" er standardverdien for norsk)
        String sprakKode = hentSprakKode(deltePreferanser);

        // Setter appens Locale basert på den valgte språkkoden
        settAppLocale(sprakKode);
    }

    // Henter delte preferanser
    private static SharedPreferences hentDeltePreferanser(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    // Henter språkkoden fra delte preferanser
    private static String hentSprakKode(SharedPreferences deltePreferanser) {
        return deltePreferanser.getString("sprak_valg", "no");
    }

    // Setter applikasjonens Locale
    private static void settAppLocale(String sprakKode) {
        LocaleListCompat appLocale = LocaleListCompat.forLanguageTags(sprakKode);
        AppCompatDelegate.setApplicationLocales(appLocale);
    }
}
