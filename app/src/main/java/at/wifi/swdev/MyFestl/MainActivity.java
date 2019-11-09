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
                NewsItem("Herbstwanderung", "Start beim Hauptplatz Gemeinde Ilz, von 13:30 bis 18:30", "2019.12.30", R.drawable.wandern, 47.086532, 15.925756, ""));

        mData.add(new
                NewsItem("Theateraufführung ‘Der Bürger als Edelmann’", "Dorfhof Markt Hartmannsdorf, von 19:00 bis 22:00", "2019.11.08", R.drawable.veranstaltung, 47.054548, 15.838705, ""));
        mData.add(new
                NewsItem("LIMA – Lebensqualität im Alter mit Sandra Sommer", "Öffentliche Bücherei Sinabelkirchen, 8261 Sinabelkirchen, Sinabelkirchen 9/8, ab 14:30 Uhr", "2019.11.08", R.drawable.ausstellung, 47.103332, 15.826907, ""));
        mData.add(new
                NewsItem("Finissage der Ausstellung ’60 Jahre Bücherei Straden’", "Mit Lesung \"Allerhand…Geschichten über die Zeit\", ab 19:30, Kulturhauskeller Straden\n", "2019.11.08", R.drawable.veranstaltung, 46.807590, 15.869380, ""));
        mData.add(new
                NewsItem("Junkerpräsentation", "Jungerverkostung mit 4 Gänge-Menu und musikalischer Umrahmung, Gasthof Pock \"Zur Puxamühle\" 8345 Straden, Hof bei Straden 34, ab 19 Uhr", "2019.11.08", R.drawable.kulinarik, 46.798921, 15.899173, ""));
        mData.add(new
                NewsItem("Bockbieranstich – Ilzer SV", "Kulturhaus Ilz, von 20:00 bis 02:00 ", "2019.11.09", R.drawable.bier, 47.090658, 15.927537, ""));
        mData.add(new
                NewsItem("Feuerlöscherüberprüfung FF Weinburg am Saßbach", "Rüsthaus Weinburg am Saßbach, ab 8 Uhr", "2019.11.09", R.drawable.feuerwehrfest, 46.753881, 15.721253, ""));
        mData.add(new
                NewsItem("Martinsfest Schlosskirche Weinburg am Saßbach", "Kindergarten und Schlosskirche Weinburg am Saßbach, ab 17:30 Uhr", "2019.11.09", R.drawable.veranstaltung, 46.754725, 15.719749, ""));
        mData.add(new
                NewsItem("Rabenhofer Hobby Turnier des USV Sterzkeller Rabenhof", "Sterzkeller`s Stockschützenhalle Rabenhof, Gemeinde St. Veit in der Südsteiermark, ab 8 Uhr", "2019.11.09", R.drawable.veranstaltung, 46.748278, 15.627049, ""));
        mData.add(new
                NewsItem("Trachtenball", "Die ÖVP-Ortsgruppe Jagerberg lädt zum 10. Trachtenball der Region, SteirerSound sorgt für eine tolle Stimmung, ab 20:30 Uhr, Mehrzweckhalle Jagerberg", "2019.11.09", R.drawable.ball, 46.854022, 15.737621, ""));
        mData.add(new
                NewsItem("Trachtenbockbieranstich FF Ratschendorf", "Teichhalle Ratschendorf", "2019.11.09", R.drawable.bier, 46.743779, 15.813348, ""));
        mData.add(new
                NewsItem("Leonhard Messe", "Kapelle Frutten, Gemeinde St. Anna, ab 9 Uhr", "2019.11.09", R.drawable.veranstaltung, 46.820858, 15.932974, ""));
        mData.add(new
                NewsItem("Martini-Frühschoppen", "Sport- und Kulturhalle Sinabelkirchen, 8261 Sinabelkirchen, Sinabelkirchen 200, ab 10 Uhr", "2019.11.10", R.drawable.veranstaltung, 47.100919, 15.828256, ""));
        mData.add(new
                NewsItem("Lagerpotenzial-Workshop", "Geleiteter Workshop in der Gesamtsteirischen Vinothek zum Thema Lagerpotenzial steirischer Weine aus mehreren Jahrgängen. Beginn ist um 15:00 Uhr, Anmeldung erwünscht. Gesamtsteirische Vinothek, Gemeinde St. Anna", "2019.11.10", R.drawable.information, 46.831455, 15.974216, ""));
        mData.add(new
                NewsItem("11. Vollmondwanderung", "Dorfplatz Jamm, Gemeinde St. Anna, ab 19 Uhr", "2019.11.12", R.drawable.wandern, 46.864483, 15.971726, ""));
        mData.add(new
                NewsItem("Nachmittagsbetreuung für Seniorinnen und Senioren", "ab 13:00, Gemeindesaal Sinabelkirchen, 8261 Sinabelkirchen, Sinabelkirchen 8", "2019.11.13", R.drawable.veranstaltung, 47.103314, 15.827789, ""));
        mData.add(new
                NewsItem("Info-Veranstaltung ‘Besser Heizen mit Holz’", "Spannender Selbstversuch am mobilen Heizstand, Fachvorträge, Bauhof Kirchberg a. d. Raab\n", "2019.11.15", R.drawable.information, 46.988526, 15.766725, ""));
        mData.add(new
                NewsItem("Theater Sandkorn spielt ‘Die rote Zora und ihre Bande’ von Kurt Held (2)", "Gemeindesaal Sinabelkirchen, 8261 Sinabelkirchen, Sinabelkirchen 8, ab 19:30 Uhr", "2019.11.18", R.drawable.theater, 47.103359, 15.827833, ""));
        mData.add(new
                NewsItem("EKB Workshop ‘Berührung. Die Welt mit allen unseren Sinnen erleben.’", "Erfahrungen, die wir mit den Sinnen der Haut, den Muskeln bei Körperkontakt und Bewegung sammeln, sind sehr wichtig für die Entwicklungund später für den schulischen Erfolg. Warum ist es wichtig, die Entwicklung der Sinne zu unterstützen? Wie kann ich mein Kind unterstützen? Was brauche ich, um mir und meinem Kind helfen zu können?, ab 19 Uhr", "2019.11.18", R.drawable.veranstaltung, 46.904381, 15.710678, ""));
        mData.add(new
                NewsItem("Nachmittagsbetreuung für Seniorinnen und Senioren", "ab 13:00, Gemeindesaal Sinabelkirchen, 8261 Sinabelkirchen, Sinabelkirchen 8", "2019.11.20", R.drawable.veranstaltung, 47.103314, 15.827789, ""));
        mData.add(new
                NewsItem("Kirtag (Mariä Opferung)", "Siniwelt-Parkplatz, 8261 Sinabelkirchen, Sinabelkirchen 280, 19:30 Uhr", "2019.11.21", R.drawable.veranstaltung, 47.103150, 15.827015, ""));
        mData.add(new
                NewsItem("Oldie-Nacht der SPÖ Söchau", "ab 20:00, Kultursaal Söchau", "2019.11.23", R.drawable.ball, 47.030868, 16.017124, ""));
        mData.add(new
                NewsItem("Upcycling Workshop", "von 09:00 bis 20:00, Pfarrzentrum Sinabelkirchen, 8261 Sinabelkirchen, Sinabelkirchen 1", "2019.11.23", R.drawable.information, 47.104014, 15.827970, ""));
        mData.add(new
                NewsItem("Kulinarium – Schmankerlabend", "ab 16:00, Gemeindezentrum Kirchberg, Gemeinde Kirchberg an der Raab", "2019.11.23", R.drawable.kulinarik, 46.988526, 15.766725, ""));
        mData.add(new
                NewsItem("LeKaro´s Adventzauber (3)", "ab 10:00, 8261 Sinabelkirchen, Gnies 177, Gemeinde Sinabelkirchen", "2019.11.23", R.drawable.veranstaltung, 47.098988, 15.854558, ""));
        mData.add(new
                NewsItem("Sparvereinsauszahlung Sparverein Neudorf", "von 11:00 bis 20:00, Kulturhaus Ilz, Gemeinde Ilz", "2019.11.24", R.drawable.veranstaltung, 47.090658, 15.927537, ""));
        mData.add(new
                NewsItem("Nikolomarkt mit Krampusumzug um 17:00 Uhr", "14:00 Uhr: Nikolo für die Kleinen beim Rüsthaus, 17:00 Uhr: Krampusumzug vom GemeindeServiceZentrum zum Rüsthaus, Rüsthausgelänge Markt Hartmannsdorf", "2019.11.24", R.drawable.veranstaltung, 47.054877, 15.839837, ""));
        mData.add(new
                NewsItem("Adventzauber am Bio Weinhof Rominger", "ab 13:00, Bio Weinhof Rominger, 8261 Sinabelkirchen, Frösau 5", "2019.11.24", R.drawable.sternschnuppe, 47.082141, 15.796526, ""));
        mData.add(new
                NewsItem("Kammermusikkonzert", "Kammermusikkonzert der Trachtenmusikkapelle Ottendorf um 17:00 Uhr in der Pfarrkirche Ottendorf. ab 17:00, Pfarrkirche Ottendorf, Gemeinde Ottendorf a.d. Rittschein", "2019.11.24", R.drawable.veranstaltung, 46.9526, 15.888799999, ""));
        mData.add(new
                NewsItem("Nachmittagsbetreuung für Seniorinnen und Senioren", "ab 13:00, Gemeindesaal Sinabelkirchen, 8261 Sinabelkirchen, Sinabelkirchen 8", "2019.11.27", R.drawable.veranstaltung, 47.103314, 15.827789, ""));
        mData.add(new
                NewsItem("Eröffnung Sinabelkirchner Advent", "Marktplatz Sinabelkirchen, 8261 Sinabelkirchen, ab 16 Uhr", "2019.11.29", R.drawable.veranstaltung, 47.103359, 15.827833, ""));
        mData.add(new
                NewsItem("Adventmarkt am Bauernhof Damm", "von 10:00 bis 18:00, Franz und Sonja Damm, Melben 53, Gemeinde Markt Hartmannsdorf", "2019.11.30", R.drawable.sternschnuppe, 47.064190, 15.847470, ""));
        mData.add(new
                NewsItem("Nestelbacher Christkindlmarkt", "Eröffnung am 30.11.2019 mit Beginn um 15.00 Uhr, Galerie \"Alte Raika\" und Dorfplatz Nestelbach im Ilztal", "2019.11.30", R.drawable.sternschnuppe, 47.091518, 15.881434, ""));
        mData.add(new
                NewsItem("Krampustreiben der FF Pöllau", "ab 17:00, Rüsthausgelände der FF Pöllau 82, Gemeinde Markt Hartmannsdorf", "2019.11.30", R.drawable.krampus, 47.046904, 15.813590, ""));
        mData.add(new
                NewsItem("Legoausstellung – Legoteam Friedl", "ab 09:00, Fam. Friedl, Rabenhof, Gemeinde St. Veit in der Südsteiermark", "2019.11.30", R.drawable.ausstellung, 46.748278, 15.627049, ""));
        mData.add(new
                NewsItem("Pöllauer Adventmarkt", "30.11.2019 - 01.12.2019, von 09:00 bis 18:00, Gemeinschaftsraum Pöllau 129, Gemeinde Markt Hartmannsdorf", "2019.12.01", R.drawable.sternschnuppe, 47.049178, 15.816876, ""));
        mData.add(new
                NewsItem("Steirische Taekwondo Landesmeisterschaft\n", "Sport- und Kulturhalle Sinabelkirchen, 8261 Sinabelkirchen, Sinabelkirchen 200\n" +
                "Gemeinde Sinabelkirchen, von 9 bis 18 Uhr", "2020.11.30", R.drawable.laufen_logo, 47.100919, 15.828256, ""));
        mData.add(new
                NewsItem("MATURABALL ‘Bond Girls – Auf letzter Mission’\n", "ab 19:00\n" +
                "\n" +
                "der HLW Feldbach VVK € 18,- / AK € 20,- arena, Feldbach, 19 Uhr\n" +
                "Gemeinde Feldbach", "2020.11.30", R.drawable.ball, 46.955034, 15.878048, ""));
        mData.add(new
                NewsItem("Adventkranzsegnung", "Pfarrkirche Straden ab 15 Uhr", "2020.11.30", R.drawable.veranstaltung, 46.954102, 15.886733, ""));
        mData.add(new
                NewsItem("KFG-Jugend-Adventstand", "ab 10:00, Kirchpark Kirchberg, Gemeinde Kirchberg an der Raab", "2019.11.30", R.drawable.veranstaltung, 46.987187, 15.764441, ""));
        mData.add(new
                NewsItem("Adventmarkt der Katholischen Frauenbewegung", "Die Katholische Frauenbewegung lädt zum Adventmarkt vor und nach der Adventkranzsegnung (18.30 Uhr in der Pfarrkirche) ein. Pfarrheim Edelsbach", "2019.12.30", R.drawable.veranstaltung, 46.988059, 15.839520, ""));
        mData.add(new
                NewsItem("Krampusmarkt der Freiwilligen Feuerwehr Söchau", "ab 09:30, Dorfplatz Söchau, Gemeinde Söchau", "2019.12.01", R.drawable.krampus, 47.032175, 16.015663, ""));
        mData.add(new
                NewsItem("Legoausstellung – Legoteam Friedl", "ab 09:00, Fam. Friedl, Rabenhof, Gemeinde St. Veit in der Südsteiermark", "2019.12.01", R.drawable.ausstellung, 46.748278, 15.627049, ""));
        mData.add(new
                NewsItem("Sparvereinsauszahlung Sparverein Tschecherl", "Kulturhalle Nestelbach im Ilztal, ab 10:00 Uhr", "2019.12.01", R.drawable.veranstaltung, 47.091518, 15.881434, ""));
        mData.add(new
                NewsItem("Nikolausfeier", "Nikolausfeier des Sportvereins, Um ca. 16 Uhr kommt der Nikolaus und bringt für jedes brave Kind ein Packerl. Für das leibliche Wohl sorgt das Team des Sportvereines! Der Reinerlös kommt der Jugendförderung zugute.", "2019.12.01", R.drawable.veranstaltung, 46.977802, 16.058959, ""));
        mData.add(new
                NewsItem("Der Nikolaus kommt in den Sparmarkt Landkauf Bund\n", "Sparmarkt Landkauf Bund 8345 Straden, Wieden-Klausen 35\n" +
                "Gemeinde Straden, von 14 bis 17 Uhr", "2020.12.02", R.drawable.veranstaltung, 46.789865, 15.852277, ""));
        mData.add(new
                NewsItem("Nachmittagsbetreuung für Seniorinnen und Senioren", "ab 13:00, Gemeindesaal Sinabelkirchen, 8261 Sinabelkirchen, Sinabelkirchen 8", "2019.12.04", R.drawable.veranstaltung, 47.103314, 15.827789, ""));
        mData.add(new
                NewsItem("Nikolomarkt der FF Untergroßau", "von 17:00 bis 23:00, Rüsthaus Untergroßau, 8261 Sinabelkirchen, Untergroßau 145", "2019.12.05", R.drawable.veranstaltung, 47.101526, 15.810738, ""));
        mData.add(new
                NewsItem("Krampusrummel der FF Egelsdorf", "von 16:00 bis 23:00, Rüsthaus Egelsdorf, 8261 Sinabelkirchen, Egelsdorf 61\n", "2019.12.05", R.drawable.krampus, 47.123101, 15.810856, ""));
        mData.add(new
                NewsItem("Adventkonzert", "ab 18:30, Pfarrkirche St. Stefan im Rosental\n", "2019.12.07", R.drawable.sternschnuppe, 46.926815, 15.718046, ""));
        mData.add(new
                NewsItem("SPAFUDLA – Lasst uns froh und munter sein", "Vorweihnachtliches, Kulturteam Bühne Weinburg, ab 19:00, Kultursaal Weinburg am Saßbach", "2019.12.07", R.drawable.veranstaltung, 46.753968, 15.719389, ""));
        mData.add(new
                NewsItem("Adventmarkt und Jubiläumskrippenausstellung", "Der Krippenverein lädt am Samstag, den 7. Dezember und am Sonntag, den 8. Dezember zum Adventmarkt\n" +
                "mit Jubiläumskrippenausstellung ein. Beginn ist um 13 Uhr beim Marktplatz St. Anna am Aigen.", "2019.12.08", R.drawable.sylvester, 46.835563, 15.975554, ""));
        mData.add(new
                NewsItem("Hallenturnier des HSV Markt Hartmannsdorf", "ab 08:00, Sporthalle Schulweg 247, Gemeinde Markt Hartmannsdorf", "2019.12.08", R.drawable.laufen_logo, 47.052735, 15.845292, ""));
        mData.add(new
                NewsItem("54. Weihnachtswunschkonzert EHJ Trachenkapelle St. Veit", "ab 14:30, Kultursaal St. Veit am Vogau\n", "2019.12.08", R.drawable.konzert, 46.748118, 15.626215, ""));
        mData.add(new
                NewsItem("Adventkonzert Chor der Pfarre Deutsch Goritz", "Gemeinde Deutsch Goritz", "2019.12.08", R.drawable.konzert, 46.751600, 15.829030, ""));
        mData.add(new
                NewsItem("Adventkonzert Pfarre St. Nikolai ob Draßling", "ab 17:30, Pfarrkirche St. Nikolai ob Draßling", "2019.12.08", R.drawable.konzert, 46.808238, 15.660508, ""));
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
                NewsItem("Adventfeier Volksschule Weinburg am Saßbach", "Kultursaal Weinburg am Saßbach, ab 15 Uhr", "2019.12.14", R.drawable.veranstaltung, 46.753968, 15.719389, ""));
        mData.add(new
                NewsItem("Adventl`n", "Adventl`n mit vielen Ausstellern und Ausschank am Kirchpark, Adventl`n mit vielen Ausstellern und Ausschank am Kirchpark, ab 13 Uhr ", "2019.12.14", R.drawable.veranstaltung, 46.988526, 15.766725, ""));
        mData.add(new
                NewsItem("100% Advent – a cappella Konzert", "Stephanus, 8083 St. Stefan im Rosental, Alteggerstraße 18, ab 17 Uhr", "2019.12.15", R.drawable.konzert, 46.929172, 15.692814, ""));
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
                NewsItem("Stefani-Preisschnapsen – Sportverein", "Cafe Neptun - Jagerberg, ab 13 Uhr", "2019.12.26", R.drawable.veranstaltung, 46.852245, 15.746166, ""));
        mData.add(new
                NewsItem("Asphaltschiessen ESV Siebing", "Sporthalle Siebing, ab 13 Uhr", "2019.12.26", R.drawable.stockschiessen, 46.766800, 15.707435, ""));
        mData.add(new
                NewsItem("Neujahrsgeigen Musikverein Siebing", "In den Ortschaften Weinburg am Saßbach Priebing, Pichla bei Mureck, Stangdorf und Siebing, ab 8 Uhr", "2019.12.26", R.drawable.konzert, 46.753881, 15.721253, ""));
        mData.add(new
                NewsItem("Johannisweinsegnung", "Pfarrkirche, Gemeinde St. Anna, ab 9 Uhr", "2019.12.27", R.drawable.veranstaltung, 46.830729, 15.974057, ""));
        mData.add(new
                NewsItem("Bauernsilvester", "ab 16:00, Dorfhaus Berndorf, Gemeinde Kirchberg an der Raab", "2019.12.30", R.drawable.sylvester, 46.988526, 15.766725, ""));
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
        mData.add(new
                NewsItem("titel", "beschreibung", "2020.11.01", R.drawable.ausstellung, 46.9526, 15.888799999, ""));


        //Vulkanland.at Ende++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


       // https://www.feldbach.gv.at/veranstaltungen/       Anfang++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++



        mData.add(new
                NewsItem("TANZKURS FÜR ANFÄNGER", "Kummer-Meine Tanzschule www.meine-tanzschule.at Tel.: 0676/3433272 GH Schwarz, Paurach, 19-20.30 Uhr ", "2019.12.30", R.drawable.veranstaltung, 46.967700, 15.841978, ""));
        mData.add(new
                NewsItem("TREFFPUNKT FELDBACHER KIRTAG", "„Leonhard“ Super Kirtags-Angebote im Handel und der Gastronomie Hauptplatz, Feldbach, 7 Uhr ", "2019.11.06", R.drawable.veranstaltung, 15.888683, 15.881192, ""));
        mData.add(new
                NewsItem("SINGEN NACH LUST UND LAUNE", "der offenen Singrunde Feldbach Bajazzo Stub’n, Feldbach, 19 Uhr ", "2019.11.06", R.drawable.veranstaltung, 46.957417, 15.888805, ""));
        mData.add(new
                NewsItem("ORGELKONZERT", "Stadtpfarrkirche, Feldbach, 19.30 Uhr", "2019.11.06", R.drawable.konzert, 46.954920, 15.888431, ""));
        mData.add(new
                NewsItem("BOCKBIERANSTICH", "Zentrum, Feldbach, 19.30 Uhr", "2019.11.08", R.drawable.bier, 15.888683, 15.881192, ""));
        mData.add(new
                NewsItem("JUBILÄUMSKONZERT", "mit den Chören der Stadtpfarre Feldbach und Orchester anlässlich des Jubiläums „20 Jahre Kirchenchor“ unter der Leitung von Mag. Sabine Monschein Stadtpfarrkirche, Feldbach, 19.30 Uhr ", "2019.11.09", R.drawable.konzert, 46.954920, 15.888431, ""));
        mData.add(new
                NewsItem("FELDBACHER ADVENT", "Rathaushof, Feldbach, 10-19 Uhr", "2019.11.29", R.drawable.sternschnuppe, 46.954920, 15.888431, ""));


        // https://www.feldbach.gv.at/veranstaltungen/       Ende++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


        //Gnas++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++



        mData.add(new
                NewsItem("FF Poppendorf Rehschmaus ", " Mittagsmenu 11.00 Uhr – 13.00 Uhr und ab 17.00 Uhr traditionieller Rehschmaus", "2019.11.09", R.drawable.feuerwehrfest, 46.864411, 15.855697, ""));
        mData.add(new
                NewsItem("Wildschmaus der FF Kohlberg ", " Beginn: 16:00 Uhr Festhalle Kohlberg", "2019.11.16", R.drawable.feuerwehrfest, 46.907661, 15.798117, ""));
        mData.add(new
                NewsItem("Kathreinmarkt in Gnas ", "ab 8:00 Uhr, Zentrum Gnas", "2019.11.29", R.drawable.veranstaltung, 46.874245, 15.826349, ""));
        mData.add(new
                NewsItem("Jugendball in der Kulturhalle Baumgarten", "Kultursaal Wörth bei Gnas, ab 19 Uhr", "2020.04.01", R.drawable.ball, 46.905183, 15.766026, ""));


//Gnas Ende++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        mData.add(new
                NewsItem("14. Riegersburger Perchtenlauf", "Ort: Marktplatz Riegersburg, Marktplatz, 8333 Riegersburg, Beginn 18:00 Uhr", "2019.12.07", R.drawable.krampus, 47.001103, 15.936765, ""));

        //Riegersburg Ende++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++




        //Fehring  ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        mData.add(new
                NewsItem("Bockbieranstich 2019", "Sporthalle Fehring, Hans-Kampel-Platz 5, 20:00 Uhr Stadtkapelle, ab 21:30 Skylight ", "2019.11.16", R.drawable.disco, 46.938775, 16.009789, ""));
        mData.add(new
                NewsItem("Krampusparty", "Rüsthaus der Freiw. Feuerwehr Hohenbrugg, Veranstalter: FZV Hohenbrugg, Beginn: 17:30 Uhr ", "2019.11.23", R.drawable.krampus, 46.945770, 16.064550, ""));
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

        //http://www.weiz.at/aktuelles/veranstaltungskalender  Anfang+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++



        mData.add(new
                NewsItem("BLUTSPENDEAKTION: Rotes Kreuz Weiz", "Info: Servicestelle Krottendorf, Garten der Generationen/Großer Saal, 13 bis 19 Uhr", "2019.11.13", R.drawable.roteskreuz, 47.213873, 15.645884, ""));
        mData.add(new
                NewsItem("BLUTSPENDEAKTION: Rotes Kreuz Weiz", "Info: Servicestelle Krottendorf, Garten der Generationen/Großer Saal, 13 bis 19 Uhr", "2019.11.14", R.drawable.roteskreuz, 47.213873, 15.645884, ""));
        mData.add(new
                NewsItem("WEIHNACHTSBASAR des Pensionistenverbandes", "Info: Kulturbüro, von 8 bis 15 Uhr, Volkshaus", "2019.11.15", R.drawable.veranstaltung, 47.217170, 15.622970, ""));
        mData.add(new
                NewsItem("Öffentliche GEMEINDERATSSITZUNG", "Rathaus/Stadtsaal ab 19 Uhr", "2019.11.18", R.drawable.information, 47.217170, 15.622970, ""));
        mData.add(new
                NewsItem("TAUSCHBASAR des Schivereins Weiz", "Info: Hr. Sommer, Volkshaus/Großer Saal 8 bis 11 Uhr", "2019.11.23", R.drawable.flohmarkt, 47.217170, 15.622970, ""));


        //http://www.weiz.at/aktuelles/veranstaltungskalender  Ende+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        //http://www.fehring.at/veranstaltungskalender  Anfang+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++



        mData.add(new
                NewsItem("12. Fehringer Junkerlauf", "Sporthalle Fehring \n" +
                "Anmeldeschluss: 7.11.2019\n" +
                "Weiter Auskünfte: Florian Gradwohl", "2019.11.09", R.drawable.flohmarkt, 46.937660, 16.010267, ""));
        mData.add(new
                NewsItem("Tag der offenen Tür ", "LFS Hatzendorf \n" +
                "Führung jeweils um 14:00 und um 15:30 Uhr", "2019.11.14", R.drawable.tagderoffenentuer, 46.980242, 16.004675, ""));
        mData.add(new
                NewsItem("Blackout Vorsorge Modell", "VS Unterlamm \n" +
                "Beginn 19:00 Uhr, Referenten und Infostände", "2019.11.14", R.drawable.information, 46.977878, 16.056947, ""));
        mData.add(new
                NewsItem("Bockbieranstich 2019", "Sporthalle Fehring, Hans-Kampel-Platz 5 \n" +
                "20:00 Uhr Stadtkapelle, ab 21:30 Fredl's Soundhaufen und Katakomben-Disco\n" +
                "Veranstalter: UFC-Fehring", "2019.11.16", R.drawable.bier, 46.937660, 16.010267, ""));
        mData.add(new
                NewsItem("5. Fehringer Musikantentreffen", "Gasthaus Gradwohl-Stadtkeller, Bahnhofstraße 4, 8350 Fehring \n" +
                "Beginn 19:00 Uhr\n" +
                "Kontakt: Gradwohl Robert", "2019.11.22", R.drawable.veranstaltung, 46.936707, 16.012189, ""));
        mData.add(new
                NewsItem("Theater in Hatzendorf: \"G'spenstermacher\"", "Gasthof Kraxner, Hatzendorf 23 \n" +
                "Beginn 19:30 Uhr", "2019.11.22", R.drawable.theater, 46.975856, 15.999532, ""));
        mData.add(new
                NewsItem("Krampusparty", "Rüsthaus FF Hohenbrugg \n" +
                "Beginn: 17:30 Uhr", "2019.11.23", R.drawable.krampus, 46.999259, 16.084746, ""));
        mData.add(new
                NewsItem("Theater in Hatzendorf: \"G'spenstermacher\"", "Gasthof Kraxner, Hatzendorf 23 ab 17 Uhr", "2019.11.23", R.drawable.theater, 46.975856, 15.999532, ""));
        mData.add(new
                NewsItem("Theater in Hatzendorf: \"G'spenstermacher\"", "Gasthof Kraxner, Hatzendorf 23 ab 17 Uhr", "2019.11.24", R.drawable.theater, 46.975856, 15.999532, ""));
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


        //https://www.riegersburg.gv.at/Veranstaltungen.201.0.html  Anfang+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        mData.add(new
                NewsItem("Martinimarkt", "Ort: Riegersburg \n" +
                "Beginn der Veranstaltung: 08:00 Uhr", "2019.11.10", R.drawable.veranstaltung, 47.001103, 15.936765, ""));

        //https://www.riegersburg.gv.at/Veranstaltungen.201.0.html  Ende+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        //gemeinde-rosental.at.server175-han.server-routing.com/pm_cms/index.php?option=com_content&view=article&id=204&Itemid=100021  Ende+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        mData.add(new
                NewsItem("Bockbieranstich TUS St. Stefan im Rosental", "20:00 Rosenhalle \n" +
                " \n" +
                "Musik: Die jungen Paldauer", "2019.11.09", R.drawable.bier, 46.906174, 15.715204, ""));
        mData.add(new
                NewsItem("Weihnachtsbasteln", "15:00 Pfarrzentrum St. Stefan", "2019.11.16", R.drawable.veranstaltung, 46.904381, 15.710678, ""));
        mData.add(new
                NewsItem("Großer Nikolausmarkt", "13:00 Diglas Firmengelände, Schichenauerstraße ", "2019.12.01", R.drawable.veranstaltung, 46.902552, 15.718845, ""));
        mData.add(new
                NewsItem("Rosentaler Christkindlmarkt", "Marktplatz St. Stefan im Rosental", "2019.12.07", R.drawable.sternschnuppe, 46.905436, 15.711464, ""));

        //gemeinde-rosental.at.server175-han.server-routing.com/pm_cms/index.php?option=com_content&view=article&id=204&Itemid=100021  Ende+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


        //https://booking.badradkersburg.at/badradkersburg/de/event/list?customHeader=true&lkCS=design2&AspxAutoDetectCookieSupport=1  Anfang+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++




        mData.add(new
                NewsItem("Adventmarkt im Kunst- & Genussladen\n", "Kunst- & Genussladen | Bad Radkersburg | Bad Radkersburg | Thermen- & Vulkanland Steiermark | Steiermark \n" +
                "Hauptplatz 6, AT-8490, Bad Radkersburg, 9 Uhr", "2019.11.09", R.drawable.sternschnuppe, 46.687284, 15.986767, ""));
        mData.add(new
                NewsItem("Grenzenlose Genussreise - regionale und überregionale Produkte\n", "ZEHNERHAUS Bad Radkersburg | Bad Radkersburg | Bad Radkersburg | Thermen- & Vulkanland Steiermark | Steiermark \n" +
                "Hauptplatz 10, AT-8490, Bad Radkersburg ab 10 Uhr", "2019.11.09", R.drawable.veranstaltung, 46.687354, 15.987030, ""));
        mData.add(new
                NewsItem("Weihnachtsmarkt der Hobbykünstler\n", "Kulturzentrum Mureck | Mureck | Radkersburger Teich- und Hügelland | Thermen- & Vulkanland Steiermark | Steiermark ab 9 Uhr", "2019.11.09", R.drawable.veranstaltung, 46.706790, 15.774590, ""));



        //https://booking.badradkersburg.at/badradkersburg/de/event/list?customHeader=true&lkCS=design2&AspxAutoDetectCookieSupport=1  Ende+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++



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
