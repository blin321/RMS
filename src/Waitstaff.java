import java.util.*
import java.time.*

public class Waitstaff {
    private String name;
    private long startTime;
    private double total = 0.0;

    public Waitstaff(String n) {
        name = n;
        startTime = System.currentTimeMillis();
    }

    public String getName() {
        return name;
    }

    public double getTotal() {
        return total;
    }

    public double addToTotal(double bill) {
        total += bill;
        return total;
    }

    public long getStartTime() {
        return startTime;
    }

    public float getTimeWorked() {
        long current_time = System.currentTimeMillis();
        float milli_sec = current_time - startTime;
        float min = (milli_sec / 1000f) / 60f;
        return min;
    }


}