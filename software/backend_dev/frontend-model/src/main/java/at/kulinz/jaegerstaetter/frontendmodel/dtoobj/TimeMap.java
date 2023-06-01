package at.kulinz.jaegerstaetter.frontendmodel.dtoobj;

import java.util.List;

public class TimeMap {

    public final String key;

    public final String title;

    public final List<TimeMapPoint> timeMapPoints;

    public TimeMap(String key, String title, List<TimeMapPoint> timeMapPoints) {
        this.key = key;
        this.title = title;
        this.timeMapPoints = timeMapPoints;
    }
}
