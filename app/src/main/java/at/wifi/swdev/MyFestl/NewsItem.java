package at.wifi.swdev.MyFestl;

public class NewsItem {

    String Title, Content, Date, Entfernung;
    int userPhoto;
    double Koordinatebreite, Koordinatelaenge;

    public NewsItem() {
    }

    public NewsItem(String title, String content, String date, int userPhoto, double koordinatebreite, double koordinatelaenge, String entfernung) {
        Title = title;
        Content = content;
        Date = date;
        this.userPhoto = userPhoto;
        Koordinatebreite = koordinatebreite;
        Koordinatelaenge = koordinatelaenge;
        Entfernung = entfernung;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setContent(String content) {
        Content = content;
    }

    public void setDate(String date) {
        Date = date;
    }

    public void setUserPhoto(int userPhoto) {
        this.userPhoto = userPhoto;
    }

    public void setKoordinatebreite(int koordinatebreite) {
        koordinatebreite = koordinatebreite;
    }

    public void setKoordinatelaenge(double koordinatelaenge) {
        koordinatelaenge = koordinatelaenge;
    }

    public void setEntfernung(String entfernung) {
        Entfernung = entfernung;
    }


    public String getTitle() {
        return Title;
    }

    public String getContent() {
        return Content;
    }

    public String getDate() {
        return Date;
    }

    public int getUserPhoto() {
        return userPhoto;
    }

    public double getKoordinatebreite() {
        return Koordinatebreite;
    }

    public double getKoordinatelaenge() {
        return Koordinatelaenge;
    }

    public String getEntfernung() {
        return Entfernung;
    }
}
