package at.wifi.swdev.MyFestl;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
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
import android.widget.Button;
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
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;

import static com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE;


public class MainActivity extends AppCompatActivity implements android.support.v7.widget.PopupMenu.OnMenuItemClickListener, PopupMenu.OnMenuItemClickListener {


    private static final int MY_REQUEST_CODE = 1;
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






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);







// Creates instance of the manager.
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(this);

// Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

// Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    // For a flexible update, use AppUpdateType.FLEXIBLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            // Pass the intent that is returned by 'getAppUpdateInfo()'.
                            appUpdateInfo,
                            // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                            AppUpdateType.IMMEDIATE,
                            // The current activity making the update request.
                            this,
                            // Include a request code to later monitor this update request.
                            MY_REQUEST_CODE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }


        }

        );









      //  MobileAds.initialize(this, "ca-app-pub-7283581220811839~8808815852"); //App ID  ca-app-pub-7283581220811839~8808815852



     //   mAdview = (AdView) findViewById(R.id.adView);
     //   AdRequest adRequest = new AdRequest.Builder().build();
     //   mAdview.loadAd(adRequest);

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
                NewsItem("Markt Hartmannsdorfer Pfarrfasching\n", "Dorfhof/großer Saal, Hauptstraße 25, Markt Hartmannsdorf, ab 16 Uhr", "2020.02.16", R.drawable.fasching, 47.054848, 15.838523, ""));
        mData.add(new
                NewsItem("Schlachttage beim Gasthof Pock ‘Zur Puxamühle’", "Schlachttage von 14.02.2020 bis 16.02.2020 beim Gasthof Pock in Hof bei Straden, ab 18 Uhr", "2020.02.14", R.drawable.kulinarik, 46.787477, 15.894288, ""));
        mData.add(new
                NewsItem("Schlachttage beim Gasthof Pock ‘Zur Puxamühle’", "Schlachttage von 15.02.2020 bis 16.02.2020 beim Gasthof Pock in Hof bei Straden, ab 11:30 Uhr", "2020.02.15", R.drawable.kulinarik, 46.787477, 15.894288, ""));
        mData.add(new
                NewsItem("Schlachttage beim Gasthof Pock ‘Zur Puxamühle’", "Schlachttage von 14.02.2020 bis 16.02.2020 beim Gasthof Pock in Hof bei Straden, ab 11:30 Uhr", "2020.02.16", R.drawable.kulinarik, 46.787477, 15.894288, ""));
        mData.add(new
                NewsItem("Hobby-Tischtennisturnier", "Sport- und Kulturhalle Sinabelkirchen, 8261 Sinabelkirchen, Sinabelkirchen 200, ab 9 Uhr", "2020.02.15", R.drawable.veranstaltung, 47.100919, 15.828257, ""));
        mData.add(new
                NewsItem("Narrenstadl im Gasthaus Wallner\n", "Die Familie Hammer und Freunde hat wiederum ein buntes Faschingsprogramm vorbereitet.\n" +
                "Vorverkaufskarten beim Landgasthaus Wallner, Unterlamm 21, im Gemeindeamt Unterlamm oder unter Tel. 0664/1640032\n" +
                "Der Reinerlös der Veranstaltung wird gespendet!, ab 19 Uhr", "2020.02.15", R.drawable.fasching, 46.979365, 16.057617, ""));
        mData.add(new
                NewsItem("Theater ‘Mord im Hühnerstall’", "Kultur und Begegnungszentrum Eichkögl, Karten erhältlich beim GH Monschein Freißmuth , ab 17 Uhr", "2020.02.14", R.drawable.theater, 47.031213, 15.791701, ""));
        mData.add(new
                NewsItem("Theater ‘Mord im Hühnerstall’", "Kultur und Begegnungszentrum Eichkögl, Karten erhältlich beim GH Monschein Freißmuth , ab 17 Uhr", "2020.02.15", R.drawable.theater, 47.031213, 15.791701, ""));
        mData.add(new
                NewsItem("Theater ‘Mord im Hühnerstall’", "Kultur und Begegnungszentrum Eichkögl, Karten erhältlich beim GH Monschein Freißmuth , ab 17 Uhr", "2020.02.16", R.drawable.theater, 47.031213, 15.791701, ""));
        mData.add(new
                NewsItem("Playback Show\n", "Festhalle Edelsbach, ab 14 Uhr", "2020.02.16", R.drawable.disco, 46.989699, 15.836649, ""));
        mData.add(new
                NewsItem("Nachmittag für Seniorinnen und Senioren", "Gemeindesaal Sinabelkirchen, 8261 Sinabelkirchen, Sinabelkirchen 8, ab 13 Uhr", "2020.02.19", R.drawable.veranstaltung, 47.103314, 15.827789, ""));
        mData.add(new
                NewsItem("Gehölzraritäten für den Garten, Helmut Pirc, Markt Hartmannsdorf\n", "Dorfhof Markt Hartmannsdorf, Hauptstraße 25, 8311 Markt Hartmannsdorf, ab 19 Uhr", "2020.02.20", R.drawable.veranstaltung, 47.054548, 15.838705, ""));
        mData.add(new
                NewsItem("Vortrag: Abenteuer Familie – Eltern sind auch nur Menschen", "Referent: Dr. Gottfried Hofmann-Wellenhof, Gemeindezentrum Kapfenstein, ab 19 Uhr", "2020.02.20", R.drawable.vortrag, 46.885635, 15.974116, ""));
        mData.add(new
                NewsItem("LIMA – Lebensqualität im Alter mit Sandra Sommer", "Öffentliche Bücherei Sinabelkirchen, 8261 Sinabelkirchen, Sinabelkirchen 9/8, ab 14:30 Uhr", "2020.02.21", R.drawable.veranstaltung, 47.103332, 15.826907, ""));
        mData.add(new
                NewsItem("Faschingsball", "Gasthof-Pension Weninger Perlsdorf 63 8341 Paldau", "2020.02.21", R.drawable.fasching, 46.918270, 15.804737, ""));
        mData.add(new
                NewsItem("FASCHINGSPARTY\n", "Faschingsparty der FF Wiersdorf beim Rüsthaus ab 16:00 Uhr", "2020.02.21", R.drawable.fasching, 46.824898, 15.755617, ""));
        mData.add(new
                NewsItem("EROBERUNG DES RATHAUSES\n", "mit Clown Jako & Seifenblasen-Flashmob vor dem Rathaus, Feldbach, ab 16 Uhr", "2020.02.21", R.drawable.fasching, 46.954192, 15.888062, ""));
        mData.add(new
                NewsItem("Maskenball\n", "Maskenball mit Masenprämierung, Musik- und Sängerheim Siebing, ab 20 Uhr, Gemeinde St. Veit, Südsteiermark", "2020.02.22", R.drawable.fasching, 46.766800, 15.707435, ""));
        mData.add(new
                NewsItem("Landjugendball\n", "GH Rodler\n" +
                "Gemeinde Edelsbach", "2020.02.22", R.drawable.ball, 46.989417, 15.839133, ""));
        mData.add(new
                NewsItem("Workshop Obstgehölzeschnitt bei Familie Pirc in Muggendorf", "Helmut Pirc aus Muggendorf, Gärtner, Biologe und Gehölzerexperte, von 19:00 bis 21:00\n", "2020.02.22", R.drawable.veranstaltung, 46.821170, 15.874410, ""));
        mData.add(new
                NewsItem("Evergreentanzparty", "Kulturhaus Straden ab 20 Uhr", "2020.02.22", R.drawable.veranstaltung, 46.806198, 15.871406, ""));
        mData.add(new
                NewsItem("2. Dart-Turnier", "Festhalle Oberdorf\n" +
                "Gemeinde Kirchberg an der Raab, ab 10 IUhr", "2020.02.22", R.drawable.veranstaltung, 47.393661, 7.749283, ""));
        mData.add(new
                NewsItem("Kinderfasching", "KOMM-Zentrum, Leitersdorf, 13.30 Uhr\n", "2020.02.22", R.drawable.fasching, 46.951455, 15.888549, ""));
        mData.add(new
                NewsItem("Maskenball Riegersburg", "Vulkanlandhalle Riegersburg 53 8333 Riegersburg, ab 20 Uhr", "2020.02.22", R.drawable.fasching, 46.996633, 15.941589, ""));
        mData.add(new
                NewsItem("FASCHINGSUMZUG", "Ab 13:00 Uhr großer Faschingsumzug im Zentrum von St. Peter am Ottersbach", "2020.02.23", R.drawable.fasching, 46.798930, 15.759570, ""));
        mData.add(new
                NewsItem("Faschingsrummel der FF Unterlamm\n", "Ab 14 Uhr Faschingsrummel im Rüsthaus\n" +
                "Für jedes maskierte Kind gibt es eine Überraschung ! ", "2020.02.23", R.drawable.fasching, 46.977802, 16.058959, ""));
        mData.add(new
                NewsItem("SAUSCHÄDLBALL", "Sepp's Berglermühle\n" +
                "\n" +
                "Gemeinde St. Peter am Ottersbach, ab 19 Uhr", "2020.02.24", R.drawable.veranstaltung, 46.786966, 15.750817, ""));
        mData.add(new
                NewsItem("Bezirksmusikerball", "Festhalle Edelsbach, \n" +
                "Gemeinde Edelsbach, ab 19 Uhr", "2020.02.24", R.drawable.ball, 46.991233, 15.832159, ""));
        mData.add(new
                NewsItem("Korbflechter- und Besenbinderball", "Haus der Vulkane 8345 Straden, Stainz bei Straden 85, ab 19 Uhr", "2020.02.24", R.drawable.veranstaltung, 46.821946, 15.894467, ""));
        mData.add(new
                NewsItem("FASCHINGSDIENSTAG – RIESENWIENER", "Wirtshaus zum Bergler Schlössl, Gemeinde St. Peter am Ottersbach", "2020.02.25", R.drawable.fasching, 46.815375, 15.773041, ""));
        mData.add(new
                NewsItem("Faschingspreisschnapsen", "Rüsthaus Untergroßau, 8261 Sinabelkirchen, Untergroßau 145, ab 15 Uhr", "2020.02.25", R.drawable.veranstaltung, 47.101526, 15.810738, ""));
        mData.add(new
                NewsItem("Stradener Kinderfasching", "Kulturhaus Straden, ab 13 Uhr", "2020.02.25", R.drawable.fasching, 46.806198, 15.871406, ""));
        mData.add(new
                NewsItem("Aschenkreuzauflegung und Heilige Messe", "Pfarrkirche Straden, 18:30 Uhr", "2020.02.26", R.drawable.veranstaltung, 46.805888, 15.870534, ""));
        mData.add(new
                NewsItem("Naturgarten: Für lebendige Vielfalt ist im kleinsten Garten Platz, Irmgard Scheidl\n", "Gemeindeamt Halbenrain, 8492 Halbenrain 220, ab 19 Uhr, Praktische Tipps über den Naturgarten, über die Möglichkeiten der Gartengestaltung, von der eigenen Saatgutvermehrung über den Anbau im Hausgarten oder die Förderung von Nützlingen bis hin zur Verwendung in der Küche!", "2020.02.27", R.drawable.vortrag, 46.722650, 15.947967, ""));
        mData.add(new
                NewsItem("‘Körper und Gehirn brauchen Bewegung’ – Vortrag Eltern-Kind-Bildung\n", "Haus der Vulkane 8345 Straden, Stainz bei Straden 85, ab 19 Uhr", "2020.02.27", R.drawable.vortrag, 46.821946, 15.894467, ""));

        mData.add(new
                NewsItem("Fotowettbewerb 2019 – Preisverleihung", "Gemeindesaal Sinabelkirchen, 8261 Sinabelkirchen, Sinabelkirchen 8, ab 18 Uhr", "2020.02.28", R.drawable.veranstaltung, 47.103314, 15.827789, ""));
        mData.add(new
                NewsItem("Osterausstellung – Frühling auf Schloss Kornberg\n", "Täglich vom 29.2.2020 bis 10.4.2020 von 10 Uhr bis 18 Uhr", "2020.02.29", R.drawable.ausstellung, 46.980900, 15.877300, ""));
        mData.add(new
                NewsItem("One Night in Yiddishland\n", "Dorfhof/großer Saal, Hauptstraße 25, Gemeinde Markt Hartmannsdorf, ab 19 Uhr", "2020.02.29", R.drawable.konzert, 47.054848, 15.838523, ""));
        mData.add(new
                NewsItem("Traditionelles Fischspezialitätenbuffet Gasthaus Siebinger Hof Radl\n", "Heimischer Fisch, Adriafische und Krustentiere werden wie immer bei uns in innovativer Art zubereitet! ab 11:45 Uhr", "2020.03.01", R.drawable.kulinarik, 46.773583, 15.707015, ""));
        mData.add(new
                NewsItem("VORTRAG", "„Wissen hilft – das Leben ist es wert“ Referentinnen: Mag. Astrid Geiger und Mag. Kathrin Waidulak Zentrum, Feldbach, 15 Uhr", "2020.03.03", R.drawable.vortrag, 46.951455, 15.888549, ""));
        mData.add(new
                NewsItem("Offenes Singen", "Proberaum der Marktmusik Sinabelkirchen, 8261 Sinabelkirchen, Sinabelkirchen 8, ab 18 Uhr", "2020.03.03", R.drawable.konzert, 47.103314, 15.827789, ""));
        mData.add(new
                NewsItem("Zeit für eine SONNENUHR?", "Einladung zum Treffen aller interessierten für Sonnenuhren, Gasthaus Stradnerhof, ab 19 Uhr", "2020.03.03", R.drawable.veranstaltung, 46.807586, 15.869441, ""));
        mData.add(new
                NewsItem("Vortrag STREITEN WILL GELERNT SEIN", "Pfarrzentrum St. Stefan im Rosental\n" +
                "Gemeinde St. Stefan im Rosental, ab 19 Uhr", "2020.03.04", R.drawable.vortrag, 46.904381, 15.710678, ""));
        mData.add(new
                NewsItem("SINGEN NACH LUST UND LAUNE", "der offenen Singrunde Feldbach Bajazzo Stub’n, Feldbach, 19 Uhr", "2020.03.04", R.drawable.konzert, 46.957417, 15.888805, ""));
        mData.add(new
                NewsItem("PINOCCHIO – das Musical", "Zentrum, Feldbach, 16 Uhr", "2020.03.05", R.drawable.veranstaltung, 46.951455, 15.888549, ""));
        mData.add(new
                NewsItem("Casino im Wirtshaus\n", "Ein Abend voller Glück und Erfolg mit den Casinos Austria, Wirtshaus Huber\n" +
                "8311 Markt Hartmannsdorf, Hauptstraße 5", "2020.03.05", R.drawable.veranstaltung, 47.054721, 15.841162, ""));
        mData.add(new
                NewsItem("Vortrag: Erlebtes und Erschautes im Laufe der Zeit\n", "Jahreslauf in unserer Region, Referent: Karl Fink, Gemeindezentrum Kapfenstein ab 29 Uhr", "2020.03.05", R.drawable.vortrag, 46.885580, 15.974849, ""));
        mData.add(new
                NewsItem("Kindergarteneinschreibung 2020/2021\n", "Kindergärten Breitenfeld, Lödersdorf, Riegersburg\n" +
                "Gemeinde Riegersburg, von 13:30 bis 15:00", "2020.03.05", R.drawable.veranstaltung, 47.001072, 15.936220, ""));
        mData.add(new
                NewsItem("TAROCK – GRUNDKURS", "Referent: Dipl.-Päd. Günter Mogg Anmeldung: www.vhsstmk.at AK, Feldbach, 18 Uhr", "2020.03.06", R.drawable.veranstaltung, 46.951312, 15.889549, ""));
        mData.add(new
                NewsItem("Workshop Bienenwachstuch", "Gemeindesaal Sinabelkirchen, 8261 Sinabelkirchen, Sinabelkirchen 8, 18 Uhr", "2020.03.06", R.drawable.vortrag, 47.103314, 15.827789, ""));
        mData.add(new
                NewsItem("Sortenraritäten im Garten\n", "Referentin: Irmagard Scheidl, Gartenexpertin, Krennach Gh Prehm, eine \"Gesunde Gemeinde\" Veranstaltung ab 19 Uhr ", "2020.03.06", R.drawable.vortrag, 47.026205, 15.889276, ""));
        mData.add(new
                NewsItem("FRÜHLINGSPARTY\n", "Frühlingsparty der SPÖ St. Peter am Ottersbach in der Ottersbachhalle ab 20:00 Uhr mit DJ-Team P.M. Sounds", "2020.03.07", R.drawable.disco, 46.780916, 15.767835, ""));
        mData.add(new
                NewsItem("Tag der offenen Paulownia-Plantage", "Kölldorf 38, 8353 Kölldorf ab 10 Uhr", "2020.05.30", R.drawable.tagderoffenentuer, 46.886886, 15.960820, ""));
        mData.add(new
                NewsItem("Tag der offenen Paulownia-Plantage", "Kölldorf 38, 8353 Kölldorf ab 10 Uhr", "2020.05.31", R.drawable.tagderoffenentuer, 46.886886, 15.960820, ""));






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

