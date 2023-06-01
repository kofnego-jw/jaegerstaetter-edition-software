package at.kulinz.jaegerstaetter.frontendmodel.dtoobj;

public class StatRsSingleStat {
    public final int count;

    public final String filename;

    public StatRsSingleStat(int count, String filename) {
        this.count = count;
        this.filename = filename;
    }

    @Override
    public String toString() {
        return "RsSingleStat{" +
                "count=" + count +
                ", filename='" + filename + '\'' +
                '}';
    }
}
