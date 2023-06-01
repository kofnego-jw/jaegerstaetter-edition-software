package at.kulinz.jaegerstaetter.frontendmodel.dtoobj;

public class TimeMapPoint {

    public final String date;

    public final String notBefore;

    public final String notAfter;

    public final RegistryEntryPlace place;

    public final String description;

    public TimeMapPoint(String date, String notBefore, String notAfter, RegistryEntryPlace place, String description) {
        this.date = date;
        this.notBefore = notBefore;
        this.notAfter = notAfter;
        this.place = place;
        this.description = description;
    }
}
