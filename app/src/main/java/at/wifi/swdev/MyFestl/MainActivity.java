package at.wifi.swdev.MyFestl;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;


public class MainActivity extends AppCompatActivity implements android.support.v7.widget.PopupMenu.OnMenuItemClickListener, PopupMenu.OnMenuItemClickListener {

    AdView mAdview;

    public static final int REQUEST_CODE = 1;
    //gps+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    private FusedLocationProviderClient client;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback; //wenn app pausiert das nicht abgefragt wird
    //gps++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    RecyclerView NewsRecyclerView;
    NewsAdapter newsAdapter;
    List<NewsItem> mData;
    EditText searchInput;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, "ca-app-pub-7283581220811839~8808815852"); //App ID  ca-app-pub-7283581220811839~8808815852

        mAdview = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdview.loadAd(adRequest);

        //gps++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        client = LocationServices.getFusedLocationProviderClient(this);

        // Location Request bauen
        locationRequest = createLocationRequest();

        // Location Callback definieren
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {

                if (locationResult == null) {
                    return;
                }
                Log.d("Location", "onLocationResult: Got new Location");
                Location location = locationResult.getLastLocation();

                double gpsLatitude = location.getLatitude();
                double gpsLongitude = location.getLongitude();


                for (NewsItem r : mData) {

                    int radius = 6371;
                    double lat = Math.toRadians(r.getKoordinatebreite() - gpsLatitude);
                    double lon = Math.toRadians(r.getKoordinatelaenge() - gpsLongitude);

                    double a = Math.sin(lat / 2) * Math.sin(lat / 2) + Math.cos(Math.toRadians(gpsLatitude)) *
                            Math.cos(Math.toRadians(r.getKoordinatebreite())) * Math.sin(lon / 2) * Math.sin(lon / 2);
                    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                    double z = radius * c;
                    double u = Math.abs(z);

                    String s = String.format(((u % 0.0D) == 0.0D) ? "%.0f" : "%.0f", u);
                    r.setEntfernung(String.valueOf(s));

                }

                super.onLocationResult(locationResult);
            }

            ;
        };
        //gps++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


        //action bar verstecken
        getSupportActionBar().hide();

        // initialisieren view
        searchInput =
                findViewById(R.id.search_input);
        NewsRecyclerView =
                findViewById(R.id.news_rv);


        mData = new ArrayList<>();
        // Liste mit Daten füllen
        // vl erhalten daten aus db, sqlite??? wie?


        searchInput.setBackgroundResource(R.drawable.search_input_style); // Suchfunktion

        //ArrayList++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        //Vulkanland.at Anfang++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++





        mData.add(new
                NewsItem("ADVENTMARKT", "„Miteinander Advent leben“ mit Adventstand der Jungsteirerkapelle Feldbach, Kinderbastelstube von 14-18 Uhr Rathaushof, Feldbach, 10-19 Uhr", "2019.12.22", R.drawable.sternschnuppe, 46.954209, 15.888130, ""));
        mData.add(new
                NewsItem("ADVENTMARKT", "„Miteinander Advent leben“ mit Adventstand der Jungsteirerkapelle Feldbach, Kinderbastelstube von 14-18 Uhr Rathaushof, Feldbach, 10-19 Uhr", "2019.12.23", R.drawable.sternschnuppe, 46.954209, 15.888130, ""));
        mData.add(new
                NewsItem("ADVENTMARKT", "„Miteinander Advent leben“ mit Adventstand der Jungsteirerkapelle Feldbach, Kinderbastelstube von 14-18 Uhr Rathaushof, Feldbach, 10-19 Uhr", "2019.12.24", R.drawable.sternschnuppe, 46.954209, 15.888130, ""));
        mData.add(new
                NewsItem("HERBERGSUCHE", "Kapelle, Oberweißenbach, 18 Uhr", "2019.12.22", R.drawable.veranstaltung, 46.936260, 15.848740, ""));
        mData.add(new
                NewsItem("Adventandacht", "Adventandacht in der Haselbachkapelle\n" +
                "mit Mag. Friedrich Weingartmann\n" +
                "mit musikalischer Umrahmung, im Anschluss Agape\n" +
                "Auf Eurer Kommen freut sich die Kapellengemeinschaft, ab 16 Uhr", "2019.12.21", R.drawable.sternschnuppe, 46.964188, 16.095008, ""));
        mData.add(new
                NewsItem("SPEND TONIGHT", "Benefizkonzert mit Künstlern aus der Region Ein Abend zugunsten von Caritas, Licht ins Dunkel, Steirer helfen Steirern und der Volkshilfe Steiermark Zentrum, Feldbach, 19 Uhr", "2019.12.22", R.drawable.veranstaltung, 46.953959, 15.888722, ""));
        mData.add(new
                NewsItem("ADVENTANDACHT", "bei der Krippe der FF Edersgraben (K13), Treffpunkt: Dorfplatz, Edersgraben, 17 Uhr", "2019.12.22", R.drawable.veranstaltung, 46.925103, 15.925151, ""));
        mData.add(new
                NewsItem("FRIEDENSLICHT", "„Segnung und Ausgabe“ Rathaushof, Feldbach, 18 Uhr Von 24.12.2019 bis 6.1.2020 kann das Friedenslicht bei der Krippe in der Stadtpfarrkirche geholt werden!", "2019.12.23", R.drawable.veranstaltung, 46.954102, 15.886733, ""));
        mData.add(new
                NewsItem("ADVENTMARKT-STIMMUNG", "mit frischen Waffeln, Glühwein und Punsch Musik: Yang Sox Urbanistub’n, Feldbach, 17 Uhr", "2019.12.23", R.drawable.veranstaltung, 46.953739, 15.884880, ""));
        mData.add(new
                NewsItem("‘Nationale’ und ‘Internationale Weihnacht’ mit Schnalzer 3\n", "Dorfhof/großer Saal, Hauptstraße 25, Markt Hartmannsdorf, ab 19 Uhr", "2019.12.23", R.drawable.konzert, 47.054848, 15.838523, ""));
        mData.add(new
                NewsItem("Nikolomarkt der FF Ilz", "von 14:00 bis 20:00, Rüsthaus Ilz", "2019.12.23", R.drawable.feuerwehrfest, 47.086612, 15.925993, ""));
        mData.add(new
                NewsItem("Seniorenweihnachtsfeier", "Gemeinde Jagerberg", "2019.12.22", R.drawable.veranstaltung, 46.854022, 15.737621, ""));
        mData.add(new
                NewsItem("Kindermette Pfarre St. Veit am Vogau", "Pfarrkirche St. Veit am Vogau, ab 16 Uhr", "2019.12.24", R.drawable.veranstaltung, 46.747279, 15.625208, ""));
        mData.add(new
                NewsItem("Christmette Pfarre St. Veit am Vogau", "Pfarrkirche St. Veit am Vogau, ab 22 Uhr", "2019.12.24", R.drawable.veranstaltung, 46.747279, 15.625208, ""));
        mData.add(new
                NewsItem("Christmette Schlosskirche Weinburg", "Schlosskirche Weinburg am Saßbach, 20:30 Uhr", "2019.12.24", R.drawable.veranstaltung, 46.754725, 15.719749, ""));
        mData.add(new
                NewsItem("Christmette – ab 21:25 Uhr Turmblasen", "8311 Markt Hartmannsdorf, GSZ Hauptstraße 157", "2019.12.24", R.drawable.veranstaltung, 47.054515, 15.842930, ""));
        mData.add(new
                NewsItem("LATE-NIGHT-CHRISTMASPARTY", "Urbanistub’n, Feldbach, ab 21.30 Uhr", "2019.12.24", R.drawable.veranstaltung, 46.953739, 15.884880, ""));

        mData.add(new
                NewsItem("Christtag – Weihnachtsgottesdienst\n", "ab 09:30, Pfarrkirche Straden", "2019.12.25", R.drawable.veranstaltung, 46.805888, 46.805888, ""));
        mData.add(new
                NewsItem("Stefanitag – Gottesdienst mit Johannesweinsegnung\n", "ab 09:30, Pfarrkirche Straden, Anschließend Jungweinverkostung Weinbauverein Straden, Pfarrsaal 10:30 Uhr", "2019.12.26", R.drawable.veranstaltung, 46.805888, 46.805888, ""));
        mData.add(new
                NewsItem("Lateinisches Hochamt des Gesangvereines Gnas\n", "ab 8 Uhr, Pufarrkirche Gnas", "2019.12.25", R.drawable.veranstaltung, 46.873277, 15.827183, ""));
        mData.add(new
                NewsItem("PFIFFPARTY", "Urbanistub’n, Feldbach, 19 Uhrn", "2019.12.26", R.drawable.veranstaltung, 46.953739, 15.884880, ""));
        mData.add(new
                NewsItem("STEFANIMESSE", "mit der Jungsteirerkapelle Feldbach Stadtpfarrkirche, Feldbach, 8.30 Uhr", "2019.12.26", R.drawable.veranstaltung, 46.954102, 15.886733, ""));
        mData.add(new
                NewsItem("Jungweinverkostung Weinbauverein Straden", "Pfarrsaal Straden, ab 10:30 Uhr", "2019.12.05", R.drawable.veranstaltung, 46.808137, 15.869526, ""));
        mData.add(new
                NewsItem("‘No amol’ Weinburger Dorfkino", "Kultursaal Weinburg am Saßbach", "2019.12.29", R.drawable.veranstaltung, 46.753968, 15.719389, ""));
        mData.add(new
                NewsItem("‘No amol’ Weinburger Dorfkino", "Kultursaal Weinburg am Saßbach", "2019.12.30", R.drawable.veranstaltung, 46.753968, 15.719389, ""));

        mData.add(new
                NewsItem("Stefani-Preisschnapsen – Sportverein", "Cafe Neptun - Jagerberg, ab 13 Uhr", "2019.12.26", R.drawable.veranstaltung, 46.852245, 15.746166, ""));
        mData.add(new
                NewsItem("Asphaltschiessen ESV Siebing", "Sporthalle Siebing, ab 13 Uhr", "2019.12.26", R.drawable.stockschiessen, 46.766800, 15.707435, ""));
        mData.add(new
                NewsItem("Neujahrsgeigen Musikverein Siebing", "In den Ortschaften Weinburg am Saßbach Priebing, Pichla bei Mureck, Stangdorf und Siebing, ab 8 Uhr", "2019.12.26", R.drawable.konzert, 46.753881, 15.721253, ""));
        mData.add(new
                NewsItem("Johannisweinsegnung", "Pfarrkirche, Gemeinde St. Anna, ab 9 Uhr", "2019.12.27", R.drawable.veranstaltung, 46.830729, 15.974057, ""));
        mData.add(new
                NewsItem("Rauhnachtslauf in Aug-Radisch\n", "Beginn: 18:00 Uhr, Vor der Veranstaltungshalle Aug-Radisch", "2019.12.28", R.drawable.laufen_logo, 46.850263, 15.789166, ""));
        mData.add(new
                NewsItem("Bauernsilvester", "ab 16:00, Dorfhaus Berndorf, Gemeinde Kirchberg an der Raab", "2019.12.30", R.drawable.sylvester, 46.988526, 15.766725, ""));
        mData.add(new
                NewsItem("Bauernsilvester in Wörth", "Kulturhalle Wörth, ab 17 Uhr", "2019.12.30", R.drawable.sylvester, 46.904482, 15.765650, ""));
        mData.add(new
                NewsItem("Silvester Warm-Up des USV Unterlamm\n", "Spar-Parkplatz in Unterlamm 31, ab 10 Uhr", "2019.12.31", R.drawable.veranstaltung, 46.978546, 16.058037, ""));
        mData.add(new
                NewsItem("Neujahrsbrunch\n", "Restaurant Malerwinkl\n" +
                "Gemeinde Riegersburg, ab 11:30 Uhr", "2020.01.01", R.drawable.kulinarik, 46.979941, 16.006353, ""));
        mData.add(new
                NewsItem("FANTURNIER", "des ESV Auersbach MZH oder Zwisch‘nzwoateichhitt‘n, Auersbach, 9 Uhr", "2020.01.04", R.drawable.veranstaltung, 46.993636, 15.870885, ""));
        mData.add(new
                NewsItem("JUGENDFUSSBALLTURNIER", "des SV Mühldorf MZH, Mühldorf, 9 Uhr", "2020.01.04", R.drawable.fussball, 46.955065, 15.878244, ""));
        mData.add(new
                NewsItem("Feuerwehrball der FF Riegersburg", "Vulkanlandhalle Riegersburg, Riegersburg 53, 8333 Riegersburg, ab 20:30 Uhr", "2020.01.04", R.drawable.feuerwehrfest, 46.996633, 15.941589, ""));
        mData.add(new
                NewsItem("Jugendball in der Kulturhalle", "Mehrzweckhalle Wörth, Gemeinde Gnas, ab 20 Uhr", "2020.01.04", R.drawable.veranstaltung, 46.919400, 15.744722, ""));
        mData.add(new
                NewsItem("Tag der offenen Tür im Haus der Jugend\n", "Haus der Jugend\n" +
                "\n" +
                "Gemeinde Unterlamm, ab 14 Uhr", "2020.01.04", R.drawable.veranstaltung, 46.977802, 16.058959, ""));
        mData.add(new
                NewsItem("‘Schlagernacht’ Ball des Sportverein Breitenfeld", "Gasthaus Prehm Krennach bei Riegersburg, Krennach 13, 8333 Riegersburg, ab 20 Uhr", "2020.01.05", R.drawable.veranstaltung, 47.026205, 15.889276, ""));
        mData.add(new
                NewsItem("STERNSINGER DANKGOTTESDIENST", "Stadtpfarrkirche, Feldbach, 10 Uhr", "2020.01.06", R.drawable.veranstaltung, 46.954930, 15.888164, ""));
        mData.add(new
                NewsItem("HEILIGE MESSE", "Stadtpfarrkirche, Feldbach, 8.30 Uhr", "2020.01.06", R.drawable.veranstaltung, 46.954930, 15.888164, ""));
        mData.add(new
                NewsItem("Vortrag ‘Rituale machen Kinder stark’ (Eltern-Kind-Bildung)\n", "Gemeindeamt Unterlamm, 18:30 Uhr", "2020.01.09", R.drawable.veranstaltung, 46.977802, 16.058959, ""));
        mData.add(new
                NewsItem("Midnight-Cup", "Sporthalle Straden, ab 18 Uhr", "2020.01.10", R.drawable.fussball, 46.808429, 15.868833, ""));
        mData.add(new
                NewsItem("50. MATURABALL DER HAK FELDBACH", "„Fifty Shades of HAK“ arena, Feldbach, 19.30 Uhr", "2020.01.11", R.drawable.ball, 46.955034, 15.878048, ""));
        mData.add(new
                NewsItem("Pfarrball Gnas", "Pfarrheim Gnas, ab 14 Uhr", "2020.01.11", R.drawable.veranstaltung, 46.873230, 15.827855, ""));
        mData.add(new
                NewsItem("Seniorenball", "Gasthaus Pock, Gemeinde Straden ab 13 Uhr", "2020.01.11", R.drawable.veranstaltung, 46.885930, 15.545987, ""));
        mData.add(new
                NewsItem("Preisschnapsen UDFC Hof", "Bulldogwirt, Hof bei Straden 2, 8345 Straden\n", "2020.01.11", R.drawable.veranstaltung, 46.800049, 15.885960, ""));
        mData.add(new
                NewsItem("Jägerball 2.0\n", "Kultursaal St. Veit am Vogau, Schulstraße 11, ab 19 Uhr", "2020.01.11", R.drawable.veranstaltung, 46.748118, 15.626215, ""));
        mData.add(new
                NewsItem("Hl. Messe bei der Lourdes-Mariengrotte\n", "Pfarrkirche zum Hl. Heinrich in Unterlamm, ab 19:00\n", "2020.01.11", R.drawable.veranstaltung, 46.978248, 16.060323, ""));
        mData.add(new
                NewsItem("Film & Foto Show ‘Faszination Alpen’ – Zu Fuß 8.500 km auf den schönsten Wanderwegen\n", "Film & Foto Show \"Faszination Alpen\" – Zu Fuß 8.500 km auf den schönsten Wanderwegen\n" +
                "von Verena & Andreas Jeitler, Pfarrheim Gnas, ab 18 Uhr", "2020.01.12", R.drawable.veranstaltung, 46.873230, 15.827855, ""));
        mData.add(new
                NewsItem("MULTIVISIONSSHOW\n", "Multivisionsshow von Josef Stallmajer \"Von Madeira bis ans Ende der Welt\", Beginn um 19:30 Uhr in der Ottersbachhalle.\n", "2020.01.16", R.drawable.veranstaltung, 46.799033, 15.755575, ""));
        mData.add(new
                NewsItem("Vielfalt im Obstgarten, Stefan Tschiggerl\n", "ielfalt im Obstgarten: Pflege von Jung- und Altbäumen, alte Obstsorten erhalten, ökologische und ge-sundheitliche Aspekte im extensiven Obstbau\n" +
                "mit Stefan Tschiggerl, Gemeindeamt Kapfenstein, 8353 Kapfenstein 123, ab 19 Uhr", "2020.01.16", R.drawable.veranstaltung, 46.885635, 15.974116, ""));
        mData.add(new
                NewsItem("NEUJAHRSEMPFANG", "der Stadtgemeinde Feldbach Zentrum, Feldbach, Einlass 18.30 Uhr, Beginn 19 Uhr\n", "2020.01.16", R.drawable.veranstaltung, 46.951455, 15.888549, ""));
        mData.add(new
                NewsItem("LITERATURTREFF", "Thema: Kriminell gute Geschichten „Die Männer sind alle Verbrecher ...“ Vorwiegend neue und unbekannte Krimis aus aller Welt über menschliche Schattenseiten und den ewigen Kampf der Guten gegen das Böse. Stadtbibliothek, Feldbach, 18 Uhr", "2020.01.17", R.drawable.veranstaltung, 46.955192, 15.885444, ""));
        mData.add(new
                NewsItem("BALL DER STADTMUSIK FELDBACH", "GH Kleinmeier, Unterweißenbach, 20 Uhr", "2020.01.18", R.drawable.ball, 46.947381, 15.885674, ""));
        mData.add(new
                NewsItem("Blumenball der Marktgemeinde Straden\n", "Musik: Junge Paldauer\n" +
                "Disco im Kulturhauskeller, Kulturhaus Straden ab 19:30 Uhr", "2020.01.18", R.drawable.ball, 46.806198, 15.871406, ""));
        mData.add(new
                NewsItem("Motorsägen-Wettbewerb", "Vereinsgelände des ESV Perlsdorf-Grabenhof, ab 12 Uhr", "2020.01.18", R.drawable.veranstaltung, 46.917889, 15.818587, ""));
        mData.add(new
                NewsItem("Hallenfußballturnier Bombers", "Sport- und Kulturhalle Sinabelkirchen, 8261 Sinabelkirchen, Sinabelkirchen 200, ab 8 Uhr", "2020.01.18", R.drawable.veranstaltung, 47.100919, 15.828256, ""));
        mData.add(new
                NewsItem("Kinderfaschingsfest", "Dorfhof/großer Saal, Hauptstraße 25, Gemeinde Markt Hartmannsdorf, ab 14 Uhr", "2020.01.19", R.drawable.fasching, 47.054848, 15.838523, ""));
        mData.add(new
                NewsItem("DORFRUNDE\n", "Dorfrunde mit Pfarrer um 19:00 Uhr im Mehrzwecksaal in Dietersdorf am Gnasbach", "2020.01.22", R.drawable.veranstaltung, 46.805719, 15.812124, ""));
        mData.add(new
                NewsItem("Rotkreuz-Ball", "ab 19:00, Dorfhof/großer Saal, Hauptstraße 25, Gemeinde Markt Hartmannsdorf", "2020.01.25", R.drawable.ball, 47.054848, 15.838523, ""));
        mData.add(new
                NewsItem("Oldtimer-Treffen", "ab 09:00, Dorfhof-Gelände bzw. alten Sportplatz, Peintstraße, Gemeinde Markt Hartmannsdorf", "2020.06.07", R.drawable.oldtimertreffen, 47.054222, 15.830208, ""));



        //Vulkanland.at Ende++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++









        //ArrayList++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        //Ausblenden von Items wenn das Datum älter ist als das aktuelle Datum++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        Iterator<NewsItem> iter = mData.iterator();

        while (iter.hasNext()) {
            NewsItem d = iter.next();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
            String stringDatumListe = d.getDate();
            Date dat = null;
            try {
                dat = (Date) sdf.parse(stringDatumListe);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat newDate = new SimpleDateFormat("yyyy.MM.dd");
            String ActDate = newDate.format(dat);

            Date today = Calendar.getInstance().getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
            String date = formatter.format(today);

            int after = ActDate.compareTo(date);

            if (after < 0)
                iter.remove();
        }

        //Ausblenden Ende++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


        //Adapter Initialisierung und Setup++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        newsAdapter = new

                NewsAdapter(this, mData);
        NewsRecyclerView.setAdapter(newsAdapter);
        NewsRecyclerView.setLayoutManager(new

                LinearLayoutManager(this));


        searchInput.addTextChangedListener(new

                                                   TextWatcher() {

                                                       @Override
                                                       public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                                       }

                                                       @Override
                                                       public void onTextChanged(CharSequence s, int start, int before, int count) {
                                                           newsAdapter.getFilter().filter(s);
                                                       }

                                                       @Override
                                                       public void afterTextChanged(Editable s) {
                                                       }


                                                   });
    }


    //GPS+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    @Override // resume und pause, bei pause wegnehmen
    protected void onResume() {
        super.onResume();

        // Location Updates abbonieren
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        client.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    @Override // resume und pause, bei pause wegnehmen
    protected void onPause() {
        super.onPause();

        //Location Updates deaktivieren
        client.removeLocationUpdates(locationCallback);
    }

    private LocationRequest createLocationRequest() {

        // LocationRequest konfigurieren
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000); // Intervall ist in Millisekunden
        locationRequest.setFastestInterval(5000); // Obere grenze, updates zwischen 5 und 10 Sekunden
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); // priorotät setzen

        return locationRequest;
    }
    //Gps Ende++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    //ab hier wird die Liste nach Entfernung sortiert++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    private void sortArrayListEntfernung() {

        Collections.sort(mData, new Comparator<NewsItem>() {
            @Override
            public int compare(NewsItem s1, NewsItem s2) {
                try {
                    String a = s1.getEntfernung();
                    String b = s2.getEntfernung();
                    Integer c = Integer.valueOf(a);
                    Integer d = Integer.valueOf(b);
                    return c.compareTo(d);
                } catch (Exception e) {

                }

                return 0;
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            newsAdapter.notifyDataSetChanged();
        } else {
            //wenn nicht müssen wir sie anfordern
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }
    }


    //ab hier wird die Liste nach DATUM sortiert+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    private void sortArrayListDate() {
        Collections.sort(mData, new Comparator<NewsItem>() {
            @Override
            public int compare(NewsItem o1, NewsItem o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });
        newsAdapter.notifyDataSetChanged();
    }


    //ab hier wird die Liste nach Namen sortiert+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    private void sortArrayListTitle() {
        Collections.sort(mData, new Comparator<NewsItem>() {
            @Override
            public int compare(NewsItem o1, NewsItem o2) {
                return o1.getTitle().compareTo(o2.getTitle());
            }
        });
        newsAdapter.notifyDataSetChanged();
    }


    //hier wird die Mail geöffnet und gesendet+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    public void btnmail(View view) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:myfestl@gmx.at"));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "Keinen Mailaccount gefunden, Mail an myfestl@gmx.at", Toast.LENGTH_LONG).show();
        }
    }


    //Hier befindet sich das Popup Menü mit den Auswahlmöglichkeiten++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.popup_menu);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.item1:
                sortArrayListTitle();
                Toast.makeText(this, "nach Name", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item2:
                sortArrayListDate();
                Toast.makeText(this, "nach Datum", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item3:
                Toast.makeText(this, "Entfernung", Toast.LENGTH_SHORT).show();
                sortArrayListEntfernung();
                return true;
            default:
                return false;

        }
    }

    //Hier wird nach der Berechtigung gefragt+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        // ist das mein request code? muss immer stimmen
        if (requestCode == REQUEST_CODE) {

            //wie hat der user entschieden?
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //der user hat der berechtigung zugestimmt
                newsAdapter.notifyDataSetChanged();
            } else {
                //der user hat abgelehnt
                Toast.makeText(this, "Berechtigung nicht erteilt", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
