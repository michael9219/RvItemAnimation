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
                NewsItem("Nachmittagsbetreuung für Seniorinnen und Senioren", "ab 13:00, Gemeindesaal Sinabelkirchen, 8261 Sinabelkirchen, Sinabelkirchen 8", "2019.12.11", R.drawable.veranstaltung, 47.103314, 15.827789, ""));
        mData.add(new
                NewsItem("11. Vollmondwanderung", "Dorfplatz Jamm, Gemeinde St. Anna, ab 19 Uhr", "2019.12.12", R.drawable.wandern, 46.864483, 15.971726, ""));
        mData.add(new
                NewsItem("Weihnachtsmarkt ‘Allerhand’", "Die Töpferrunde lädt zum Weihnachtsmarkt \"Allerhand\" in den Töpferraum (Keller des Gemeindeamtes) ein.\n" +
                "Ausgestellt werden nicht nur Produkte der Töpferrunde, sondern auch Erzeugnisse von Edelsbacher Betrieben und Künstlern.\n" +
                "Lassen Sie sich überraschen. von 10:00 bis 16:00, Töpferraum Edelsbach", "2019.12.14", R.drawable.veranstaltung, 46.989699, 15.836649, ""));
        mData.add(new
                NewsItem("Weihnachtswunschkonzert Marktmusik", "Weihnachtswunschkonzert der Marktmusikkapelle Jagerberg, von 19:30 bis 23:30, Mehrzweckhalle Jagerberg", "2019.12.14", R.drawable.konzert, 46.854022, 15.737621, ""));
        mData.add(new
                NewsItem("MITEINANDER LEBEN IN VIELFALT", "„Englisch für alle“ mit Mag. Iryna Wachtler Begegnungsraum, Grazer Straße 2, Feldbach, 9 Uhr", "2019.12.14", R.drawable.veranstaltung, 46.953024, 15.886747, ""));
        mData.add(new
                NewsItem("Orgelkonzert", "Stadtpfarrkirche, Feldbach, 19.30 Uhr", "2019.12.14", R.drawable.veranstaltung, 46.954920, 15.888431, ""));
        mData.add(new
                NewsItem("KFG-Weihnachtsmesse", "ab 18:00, Pfarrkirche Kirchberg an der Raab", "2019.12.14", R.drawable.krampus, 46.987527, 15.764423, ""));
        mData.add(new
                NewsItem("Adventfeier Volksschule Weinburg am Saßbach", "Kultursaal Weinburg am Saßbach, ab 15 Uhr", "2019.12.14", R.drawable.veranstaltung, 46.753968, 15.719389, ""));
        mData.add(new
                NewsItem("Adventl`n", "Adventl`n mit vielen Ausstellern und Ausschank am Kirchpark, Adventl`n mit vielen Ausstellern und Ausschank am Kirchpark, ab 13 Uhr ", "2019.12.14", R.drawable.veranstaltung, 46.988526, 15.766725, ""));
        mData.add(new
                NewsItem("100% Advent – a cappella Konzert", "Stephanus, 8083 St. Stefan im Rosental, Alteggerstraße 18, ab 17 Uhr", "2019.12.15", R.drawable.konzert, 46.929172, 15.692814, ""));

        mData.add(new
                NewsItem("Adventkonzert Gnas", "Adventkonzert, Pfarrkirche Gnas, 18.00 Uhr \n" +
                "Mitwirkende: Streicher-Ensemble, Familienmusik Tropper, Chor der Volksschule Gnas, Saitenmusik der Musikschule Gnas, Gesangverein Gnas, Männerchor, \n" +
                "Lesung: OSR Dir. Gabriele Stangl  \n" +
                "Gesmatleitung: Oleksandra Polytsia", "2019.12.15", R.drawable.konzert, 46.873277, 15.827183, ""));
        mData.add(new
                NewsItem("Stradener Adventmarkt", "von 09:00 bis 15:00, Vorplatz Greißlerei De Merin", "2019.12.15", R.drawable.sternschnuppe, 46.805888, 15.870534, ""));
        mData.add(new
                NewsItem("Jahresabschlussfeier Buschenschank Dunkl\n", "ab 17:00 Uhr, Buschenschank Dunkl 8345 Straden, Nägelsdorf 24a", "2019.12.15", R.drawable.veranstaltung, 46.802506, 15.837195, ""));
        mData.add(new
                NewsItem("Musik im Advent", "Eine Stunde besinnliche Musik mit den Schülern der Musikschule der Stadt Feldbach KOMM-Zentrum, Leitersdorf, 17 Uhr", "2019.12.15", R.drawable.sternschnuppe, 46.941556, 15.932604, ""));
        mData.add(new
                NewsItem("ARTE NOAH – ADVENTSPAZIERGANG", "für Tierliebhaber mit oder ohne Hund – jeder Vierbeiner erhält ein Weihnachtspackerl Rathaushof, Feldbach, 15 Uhr", "2019.12.15", R.drawable.veranstaltung, 46.954209, 15.888130, ""));
        mData.add(new
                NewsItem("TUS-WEIHNACHTSTURNEN", "Turnhalle, Feldbach, 15 Uhr", "2019.12.15", R.drawable.veranstaltung, 46.952790, 15.887840, ""));
        mData.add(new
                NewsItem("TAUFERNEUERUNGSFEIER", "der Erstkommunionkinder der Volksschule I Stadtpfarrkirche, Feldbach, 8.30 Uhr", "2019.12.15", R.drawable.veranstaltung, 46.954920, 15.888431, ""));
        mData.add(new
                NewsItem("Adventsingen", "ab 16:00, Pfarrkirche Straden", "2019.12.15", R.drawable.veranstaltung, 46.805888, 15.870534, ""));
        mData.add(new
                NewsItem("Blutspendetermin ", "Blutspenden des Roten Kreuzes\n" +
                "im Rüsthaus der Freiwilligen Feuerwehr Wagendorf, Florianiweg 1, 8423 Wagendorf, von 15 bis 19 Uhr", "2019.12.16", R.drawable.veranstaltung, 46.758670, 15.610263, ""));
        mData.add(new
                NewsItem("TREFFPUNKT ADVENT", "Glühwein, Punsch & Kekse Café Beisl, Pfeiler´s Bürgerstüberl, Sissi´s Weinbar & Urbanistube, ab 17 Uhr", "2019.12.18", R.drawable.veranstaltung, 46.953739, 15.884880, ""));
        mData.add(new
                NewsItem("STAMMTISCH FÜR PFLEGENDE ANGEHÖRIGE", "Begegnungsraum, Grazer Straße 2 (Grazer Tor), Feldbach, 18.30 Uhr", "2019.12.18", R.drawable.information, 46.953024, 15.886747, ""));
        mData.add(new
                NewsItem("Ihr Kinderlein kommet – Weihnachten in der BIM\n", "ab 17:00, Bücherei, Gemeinde Straden", "2019.12.18", R.drawable.veranstaltung, 46.806241, 15.871275, ""));
        mData.add(new
                NewsItem("RORATE", "Frühmesse gestaltet von der Kath. Frauenbewegung Stadtpfarrkirche, Feldbach, 6.30 Uhr", "2019.12.19", R.drawable.veranstaltung, 46.954102, 15.886733, ""));
        mData.add(new
                NewsItem("GLÜHWEINAUSSCHANK", "Der FF Mühldorf ef-Einkaufszentrum Feldbach OST, Koller&Koller, ab 9 Uhr", "2019.12.21", R.drawable.veranstaltung, 46.949026, 15.914735, ""));
        mData.add(new
                NewsItem("Zipfelmützenparty", "Buschenschank Bierbauer, 8261 Sinabelkirchen, Frösau, ab 18 Uhr", "2019.12.21", R.drawable.veranstaltung, 47.089799, 15.815793, ""));
        mData.add(new
                NewsItem("KeyWest Charity Event\n", "Wir Spenden for the Kids, KeyWest in Straden, Marktl 51", "2019.12.21", R.drawable.veranstaltung, 46.810017, 15.878262, ""));
        mData.add(new
                NewsItem("ADVENTMARKT", "„Miteinander Advent leben“ mit Adventstand der Jungsteirerkapelle Feldbach, Kinderbastelstube von 14-18 Uhr Rathaushof, Feldbach, 10-19 Uhr", "2019.12.21", R.drawable.sternschnuppe, 46.954209, 15.888130, ""));
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
                NewsItem("Kinderfaschingsfest", "ab 14:00, Dorfhof/großer Saal, Hauptstraße 25, Gemeinde Markt Hartmannsdorf", "2020.01.19", R.drawable.fasching, 47.054848, 15.838523, ""));
        mData.add(new
                NewsItem("Rotkreuz-Ball", "ab 19:00, Dorfhof/großer Saal, Hauptstraße 25, Gemeinde Markt Hartmannsdorf", "2020.01.25", R.drawable.ball, 47.054848, 15.838523, ""));
        mData.add(new
                NewsItem("Oldtimer-Treffen", "ab 09:00, Dorfhof-Gelände bzw. alten Sportplatz, Peintstraße, Gemeinde Markt Hartmannsdorf", "2020.06.07", R.drawable.oldtimertreffen, 47.054222, 15.830208, ""));

        mData.add(new
                NewsItem("JAHRESAUSKLANG UND SILVESTER IN FELDBACH", "Feldbach, ganztägig, ab 8:00 Uhr", "2019.12.30", R.drawable.sylvester, 46.9526, 15.888799999, ""));
        mData.add(new
                NewsItem("AUSSTELLUNG SCHLICHTBAROCK", "1 UHr, Kugelmühle Feldbach", "2020.11.01", R.drawable.ausstellung, 46.9526, 15.888799999, ""));



        //Vulkanland.at Ende++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


       // https://www.feldbach.gv.at/veranstaltungen/       Anfang++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++



        mData.add(new
                NewsItem("TANZKURS FÜR ANFÄNGER", "Kummer-Meine Tanzschule www.meine-tanzschule.at Tel.: 0676/3433272 GH Schwarz, Paurach, 19-20.30 Uhr ", "2019.12.30", R.drawable.veranstaltung, 46.967700, 15.841978, ""));
        mData.add(new
                NewsItem("JUBILÄUMSKONZERT", "mit den Chören der Stadtpfarre Feldbach und Orchester anlässlich des Jubiläums „20 Jahre Kirchenchor“ unter der Leitung von Mag. Sabine Monschein Stadtpfarrkirche, Feldbach, 19.30 Uhr ", "2019.11.09", R.drawable.konzert, 46.954920, 15.888431, ""));
        mData.add(new
                NewsItem("FELDBACHER ADVENT", "Rathaushof, Feldbach, 10-19 Uhr", "2019.11.29", R.drawable.sternschnuppe, 46.954920, 15.888431, ""));


        // https://www.feldbach.gv.at/veranstaltungen/       Ende++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


        //Gnas++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++




        mData.add(new
                NewsItem("Kathreinmarkt in Gnas ", "ab 8:00 Uhr, Zentrum Gnas", "2019.11.29", R.drawable.veranstaltung, 46.874245, 15.826349, ""));
        mData.add(new
                NewsItem("Jugendball in der Kulturhalle Baumgarten", "Kultursaal Wörth bei Gnas, ab 19 Uhr", "2020.04.01", R.drawable.ball, 46.905183, 15.766026, ""));


//Gnas Ende++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++





        //Fehring  ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        mData.add(new
                NewsItem("Krampusrummel der FF Fehring", "Hauptplatz in Fehring, Beginn: 17:00 Uhr", "2019.12.05", R.drawable.krampus, 46.935007, 16.009851, ""));

        //Fehring Ende ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++



        mData.add(new
                NewsItem("Schiball", " Koralmhalle - Deutschlandsberg, Steiermark, 20:00 h", "2019.12.25", R.drawable.ball, 46.817472, 15.222044, ""));
        mData.add(new
                NewsItem("Schilcherland GALA", " ,20:00 h", "2020.02.01", R.drawable.veranstaltung, 46.817472, 15.222044, ""));
        mData.add(new
                NewsItem("Kabarettabend mit Lydia Prenner-Kasper", "Koralmhalle - Deutschlandsberg, Steiermark  ,19:30 h", "2020.03.12", R.drawable.kabarett, 46.817472, 15.222044, ""));

        //Deutschlandsberg Ende++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++



        //http://www.fehring.at/veranstaltungskalender  Anfang+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++




        mData.add(new
                NewsItem("Theater in Hatzendorf: \"G'spenstermacher\"", "Gasthof Kraxner, Hatzendorf 23 ab 17 Uhr", "2019.11.27", R.drawable.theater, 46.975856, 15.999532, ""));
        mData.add(new
                NewsItem("Theater in Hatzendorf: \"G'spenstermacher\"", "Gasthof Kraxner, Hatzendorf 23 ab 17 Uhr", "2019.11.29", R.drawable.theater, 46.975856, 15.999532, ""));
        mData.add(new
                NewsItem("Theater in Hatzendorf: \"G'spenstermacher\"", "Gasthof Kraxner, Hatzendorf 23 ab 17 Uhr", "2019.11.30", R.drawable.theater, 46.975856, 15.999532, ""));
        mData.add(new
                NewsItem("Krippenausstellung Franz Hammer", "Stadtparrkirche Fehring \n" +
                "ganztägig ", "2019.12.01", R.drawable.ausstellung, 46.936764, 16.011044, ""));
        mData.add(new
                NewsItem("Krampusrummel der FF Fehring", "Hauptplatz in Fehring \n" +
                "Beginn: 17:00 Uhr", "2019.12.05", R.drawable.krampus, 46.935482, 16.010113, ""));
        mData.add(new
                NewsItem("Wunschkonzert Stadtkapelle Fehring", "Sporthalle Fehring, Hans-Kampel-Platz 5 \n" +
                "Beginn 19:30 Uhr\n" +
                "Veranstalter: Stadtkapelle Fehring, Obmann Klaus Sundl ", "2019.12.07", R.drawable.konzert, 46.938775, 16.009789, ""));
        mData.add(new
                NewsItem("Weihnachtsmarkt", "Fehringer Hauptplatz", "2019.12.14", R.drawable.sternschnuppe, 46.935482, 16.010113, ""));

        //bis dezember
        //http://www.fehring.at/veranstaltungskalender  Ende+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++




        //gemeinde-rosental.at.server175-han.server-routing.com/pm_cms/index.php?option=com_content&view=article&id=204&Itemid=100021  Ende+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


        mData.add(new
                NewsItem("Großer Nikolausmarkt", "13:00 Diglas Firmengelände, Schichenauerstraße ", "2019.12.01", R.drawable.veranstaltung, 46.902552, 15.718845, ""));
        mData.add(new
                NewsItem("Rosentaler Christkindlmarkt", "Marktplatz St. Stefan im Rosental", "2019.12.07", R.drawable.sternschnuppe, 46.905436, 15.711464, ""));

        //gemeinde-rosental.at.server175-han.server-routing.com/pm_cms/index.php?option=com_content&view=article&id=204&Itemid=100021  Ende+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++






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
