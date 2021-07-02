import java.util.*;
import java.time.*;

public class Table{
    private int num;
    private int max_capacity;
    private int cur_capacity = 0;
    private long timeSeated;
    private double bill = 0.0;
    private String staff = "NONE";

    // Initializer for table
    public Table(int n, int max) {
        num = n;
        max_capacity = max;
    }

    // Assigns staff member to table
    public void assign(String name) {
        staff = name;
    }

    // Returns staff member assigned to table
    public String getStaff() {
        return staff;
    }

    // Returns table number
    public int getNum() {
        return num;
    }

    // Returns maximum capacity of table
    public int getMax_capacity() {
        return max_capacity;
    }

    // Returns current capacity of table
    public int getCur_capacity() {
        return cur_capacity;
    }

    /* Checks if table can hold the given number of guests. Prints error message and returns 1 if unable to seat
       guests.
     */
    public int seat(int guests) {
        if (guests > max_capacity) {
            System.out.print("Too many guests! Select a bigger table.");
            return 1;
        }
        else {
            cur_capacity = guests;
            timeSeated = System.currentTimeMillis();
        }
        return 0;
    }

    /* Checks if table is empty. If not, prints the number of the elapsed number of minutes a table has been
       in-use for.
     */
    public float getSeatTime() {
        if (cur_capacity == 0) {
            return 0;
        }
        long current_time = System.currentTimeMillis();
        float milli_sec = current_time - timeSeated;
        float min = (milli_sec / 1000f) / 60f;
        return min;
    }

    // Frees up a table and returns 0. Prints error message and returns 1 if table is empty.
    public int freeTable() {
        if (cur_capacity == 0) {
            System.out.print("Table is already empty.");
            return 1;
        }
        else {
            cur_capacity = 0;
            timeSeated = 0;
            // Resets bill of table to $0.00
            bill = 0;
        }
        return 0;

    }

    // Returns the bill
    public double getBill() {
        return bill;
    }

    // Adds price of item to bill.
    public void addToBill(double item) {
        bill += item;
    }

}