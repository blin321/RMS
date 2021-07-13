import java.util.*;
import java.time.*;

class Table {
    private int num;
    private int max_capacity;
    private int cur_capacity = 0;
    private long timeSeated;
    private Bill bill = new Bill();
    private String staff = "NONE";
    private int pos_x = 0;
    private int pos_y = 0;

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

    // Frees up the table
    public void empty() {
        cur_capacity = 0;
        timeSeated = 0;
        // Resets bill of table to $0.00
        bill = new Bill();
    }

    // Returns the bill
    public Bill getBill() {return bill;}

    public double calcDue() { return this.bill.getBalance(); }

    // Adds price of item to bill.
    public void addToBill(Item order) { this.bill.addItem(order); }

    public void remFromBill(Item order) {
        ArrayList<Item> items = bill.getItems();
        int index = 0;
        Iterator<Item> iter = items.iterator();
        while (iter.hasNext()) {
            if (order.getName().equals(iter.next().getName())) {
                bill.removeItem(index);
                return;
            }
            index++;
        }
    }

    public int getPos_x() { return pos_x; }

    public int getPos_y() { return pos_y; }

    public void setPos_x(int x) { pos_x = x; }

    public void setPos_y(int y) { pos_y = y; }


}