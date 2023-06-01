package at.kulinz.jaegerstaetter.frontendmodel.dtoobj;

import java.util.List;

public class StatRsFullStat {
    public final int total;

    public final List<StatRsSingleStat> singleStats;

    public StatRsFullStat(int total, List<StatRsSingleStat> singleStats) {
        this.total = total;
        this.singleStats = singleStats;
    }

    @Override
    public String toString() {
        return "RsFullStat{" +
                "total=" + total +
                ", singleStats=" + singleStats +
                '}';
    }
}
