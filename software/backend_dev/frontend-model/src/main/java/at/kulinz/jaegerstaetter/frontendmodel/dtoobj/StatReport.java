package at.kulinz.jaegerstaetter.frontendmodel.dtoobj;

import java.util.List;

public class StatReport {

    public final List<StatElementFullDesc> allElementDescs;

    public final List<StatElementFullDesc> diplElementDescs;

    public final List<StatElementFullDesc> normElementDescs;

    public final List<StatRefDesc> refDescs;

    public final StatRsFullStat rsPersonCount;

    public final StatRsFullStat rsPlaceCount;

    public final StatRsFullStat rsCorpCount;

    public final StatRsFullStat footnoteCount;

    public StatReport(List<StatElementFullDesc> allElementDescs, List<StatElementFullDesc> diplElementDescs, List<StatElementFullDesc> normElementDescs, List<StatRefDesc> refDescs, StatRsFullStat rsPersonCount, StatRsFullStat rsPlaceCount, StatRsFullStat rsCorpCount, StatRsFullStat footnoteCount) {
        this.allElementDescs = allElementDescs;
        this.diplElementDescs = diplElementDescs;
        this.normElementDescs = normElementDescs;
        this.refDescs = refDescs;
        this.rsPersonCount = rsPersonCount;
        this.rsPlaceCount = rsPlaceCount;
        this.rsCorpCount = rsCorpCount;
        this.footnoteCount = footnoteCount;
    }

    @Override
    public String toString() {
        return "EditionStats{" +
                // "allElementDescs=" + allElementDescs +
                // ", diplElementDescs=" + diplElementDescs +
                // ", normElementDescs=" + normElementDescs +
                // ", refDescs=" + refDescs +
                ", rsPersonCount=" + rsPersonCount.total +
                ", rsPlaceCount=" + rsPlaceCount.total +
                ", rsCorpCount=" + rsCorpCount.total +
                ", footnoteCount=" + footnoteCount.total +
                '}';
    }
}
