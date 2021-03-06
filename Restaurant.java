import java.util.*;
import java.time.*;

class Restaurant {
    private ArrayList<Table> tables = new ArrayList<>();
    private ArrayList<Waitstaff> waitstaff = new ArrayList<>();
    private ArrayList<Item> inventory = new ArrayList<>();

    // Tracks how many of the items in inventory have been sold.
    private ArrayList<Integer> stats = new ArrayList<>();

    // Restaurant initializer
    public Restaurant(String[] staff) {
        for (int i = 0; i < staff.length; i++) {
            waitstaff.add(new Waitstaff(staff[i]));
        }
    }

    public Restaurant() {}


    // Returns waitstaff.
    public ArrayList<Waitstaff> getWaitstaff() { return waitstaff; }

    public ArrayList<String> getStaffNames() {
        ArrayList<String> names = new ArrayList<>();
        Iterator<Waitstaff> iter = waitstaff.iterator();
        while (iter.hasNext()) {
            names.add(iter.next().getName());
        }
        return names;
    }

    public Waitstaff getStaff(String name) {
        Iterator<Waitstaff> iter = waitstaff.iterator();
        while (iter.hasNext()) {
            Waitstaff staff = iter.next();
            if (staff.getName().equals(name)) {
                return staff;
            }
        }
        return null;
    }
    // Initializer for tables.
    public void init_tables(int num) {
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < num; i++) {

            // Loops until user enters valid integer when prompted.
            int valid = 1;
            while (valid == 1) {
                try {
                    System.out.printf("What is the maximum seating capacity for table #%d: ", i + 1);
                    String input = scanner.nextLine();
                    int cap = Integer.parseInt(input);
                    tables.add(new Table(i + 1, cap));
                    valid = 0;
                } catch (NumberFormatException e) {
                    System.out.println("Invlaid input. Please only enter an integer.\n");
                }
            }

        }
    }

    public void addTable(int cap) {
        tables.add(new Table(numOfTables() + 1, cap));
    }

    public int numOfTables() { return tables.size(); }

    public int numOfStaff() { return waitstaff.size(); }

    // Retrieves table given its number
    public Table getTable(int num) { return tables.get(num - 1);}

    // Retrieves ArrayList of all tables associated with a staff member.
    public ArrayList<Integer> getTablesForStaff(String name) {
        if (!getStaffNames().contains(name)) {
            System.out.println("Staff member does not exist. Please make sure they have been added to the shift.");
            return null;
        }

        ArrayList<Integer> tbls = new ArrayList<>();
        Iterator<Table> iter = tables.iterator();
        while (iter.hasNext()) {
            Table t = iter.next();
            if (t.getStaff().equals(name)) {
                tbls.add(t.getNum());
            }
        }
        return tbls;
    }

    // Retrieved an ArrayList of the empty tables.
    public ArrayList<Table> getEmptyTables() {
        ArrayList<Table> empty = new ArrayList<>();
        Iterator<Table> iter = tables.iterator();
        while (iter.hasNext()) {
            Table t = iter.next();
            if (t.getCur_capacity() == 0) {
                empty.add(t);
            }
        }
        return empty;
    }

    // Provides status of each table including bill, staff member, and seat time.
    public void floor_status() {
        System.out.println("\n+---------------------------------------+");
        System.out.println("Floor Status: ");
        reportInventory();
        Iterator<Waitstaff> iter = waitstaff.iterator();
        while (iter.hasNext()) {
            Waitstaff staff = iter.next();
            System.out.printf("Product Sold by %s: $%.2f\n", staff.getName(), staff.getTotal());
        }
        Iterator<Table> iter2 = tables.iterator();
        while (iter2.hasNext()) {
            Table t = iter2.next();
            System.out.println("\n+---------------------------------------+");
            System.out.printf("Table #%d has %d guests. Bill is $%.2f", t.getNum(), t.getCur_capacity(), t.calcDue());
            System.out.printf("\nElapsed seating time: %.3f", t.getSeatTime());
            System.out.printf("\nStaff member assigned to table: %s", t.getStaff());
            System.out.println("\n+---------------------------------------+");
        }
    }
    // Assigns staff member to table.
    public int assignToTable(String name, int num) {
        // Checks if table number is valid
        if ( (num <= 0) || (num > tables.size())) {
            System.out.println("Invalid entry for table number.");
            return 1;
        }
        // Checks if staff member's name is valid.
        if (!getStaffNames().contains(name)) {
            System.out.println("Invalid entry for staff member.");
            return -1;
        }
        tables.get(num - 1).assign(name);
        return 0;

    }

    // Seats a number of guests at a table. Returns 0 if completed. Upon error, prints message and returns 1;
    public int seatGuests(int tblnum, int gnum) {
        // Checks if input for table number is valid
        if ( (tblnum <= 0) || (tblnum > tables.size())) {
            System.out.println("Invalid entry for table number.");
            return 1;
        }
        Table t = getTable(tblnum);
        // Checks if table is empty
        if (getEmptyTables().contains(t)) {
            // Checks if number of guests exceeds the max capacity of table
            if (gnum > t.getMax_capacity()) {
                System.out.println("Number of guests above table seating capacity.");
                return 1;
            }
            else {
                t.seat(gnum);
                return 0;
            }
        }
        else {
            System.out.println("Table is already being used.");
        }
        return 1;
    }

    // Adds staff to waistaff
    public int addStaff(String name) {
        if (waitstaff.contains(name)) {
            System.out.println("Staff member already added.");
            return 1;
        }

        waitstaff.add(new Waitstaff(name));
        return 0;
    }

    // Removes staff from waistaff
    public int remStaff(String name) {
        if (!getStaffNames().contains(name)) {
            System.out.println("Staff member does not exist.");
            return 1;
        }
        // Removes staff name from table assignments
        Iterator<Table> iter = tables.iterator();
        while (iter.hasNext()) {
            Table t = iter.next();
            if (t.getStaff().equals(name)) {
                t.assign("NONE");
            }
        }
        waitstaff.remove(getStaff(name));
        return 0;
    }

    // Helper function that retrieves an item from the inventory
    private Item getItem(String name) {
        Item found = null;
        Iterator<Item> iter = inventory.iterator();
        while (iter.hasNext()) {
            Item current = iter.next();
            if (current.getName().equals(name)) {
                return current;
            }
        }
        return found;
    }

    public void remItem(String dish) {
        if (checkInventory(getItem(dish))) {
            int si = statIndex(dish);
            inventory.remove(si);
            stats.remove(si);
        }
    }

    // Helper function that checks to see if an item in the inventoru shares the same name as a given dish
    private boolean checkInventory(Item dish) {
        Iterator<Item> iter = inventory.iterator();
        while (iter.hasNext()) {
            Item current = iter.next();
            if (current.getName().equals(dish.getName())) {
                return true;
            }
        }
        return false;
    }

    // Adds an item to the inventory
    public void addToInventory(Item dish) {
        if (inventory.contains(dish)) {
            return;
        }
        inventory.add(dish);
        stats.add(0);
        return;
    }

    // Retrieves the index of an inventory item based on its name
    private int statIndex(String order) {
        Iterator<Item> iter = inventory.iterator();
        int si = -1;
        int ci = -1;
        while (iter.hasNext()) {
            ci++;
            Item current = iter.next();
            if (current.getName().equals(order)) {
                si = ci;
            }
        }
        return si;
    }

    // Helper function that increments the units sold on an inventory item
    private void addStat(String dish) {
        int si = statIndex(dish);
        stats.set(stats.get(si), stats.get(si) + 1);
    }

    // Adds an item to a table's bill
    public int tblOrder(String dish, int table) {
        Item order = getItem(dish);
        if ((table > numOfTables()) || table <= 0) {
            return 1;
        }

        if (!checkInventory(order)) {
            return 1;
        }

        Table t = getTable(table);
        t.addToBill(order);
        return 0;
    }


    // Frees a table up
    public int freeTable(int t) {
        Table table = getTable(t);
        if (table.getCur_capacity() == 0) {
            System.out.print("Table is already empty.");
            return 1;
        }
        Waitstaff staff = getStaff(table.getStaff());
        staff.addToTotal(table.calcDue());
        // Adjusting stat numbers on all items on the bill
        ArrayList<Item> b = table.getBill();
        Iterator<Item> biter = b.iterator();
        while (biter.hasNext()) {
            Item bItem = biter.next();
            addStat(bItem.getName());
        }
        table.empty();
        return 0;

    }

    // Removes table
    public void remTable(int t) { tables.remove(getTable(t)); }

    // Essentially a toString method for the inventory

    public void reportInventory() {
        String str = "";
        Iterator<Item> iter = inventory.iterator();
        int i = -1;
        while (iter.hasNext()) {
            i++;
            Item current = iter.next();
            str += current.getName() + "..." + String.format("$%.2f", current.getPrice());
            str += String.format("...%d units sold\n", stats.get(i));
        }
        System.out.println(str);
    }

    // Recommends the table of a staff member that has sold the least amount of product
    // and is not overdue for a break. Returns 1 as default.
    public int recommend(int guests) {
        ArrayList<Table> empties = getEmptyTables();
        double min = getStaff(empties.get(0).getStaff()).getTotal();
        Iterator<Table> iter = empties.iterator();
        int rec = 1;
        while (iter.hasNext()) {
            Table current = iter.next();
            Waitstaff s = getStaff(current.getStaff());
            double tot = s.getTotal();
            if (tot < min) {
                // 5hr == 300 min for breaks
                if (s.getTimeWorked() < 300.0) {
                    rec = current.getNum();
                    min = s.getTotal();
                }
            }
        }
        return rec;
    }





    public static void main (String[] args) {
        Scanner scanner = new Scanner(System.in);
        Restaurant r = new Restaurant();

        r.addToInventory(new Item("Chicken", 9.99));
        System.out.println("+---------------------------------------------+");
        if (r.numOfTables() < 1) {
            boolean done = false;
            while (!done) {
                try {
                    System.out.println("Tables have not been initialized.");
                    System.out.print("How many tables is your restaurant using? ");
                    int n =  Integer.parseInt(scanner.nextLine());
                    r.init_tables(n);
                    done = true;
                } catch (Exception e) {
                    System.out.println("Invalid input. Please try again.");
                }
            }
            System.out.println("\n+---------------------------------------------+");
        }
        if (r.numOfStaff() < 1) {
            boolean done = false;
            while (!done) {
                try {
                    System.out.println("No staff entered onto current shift.");
                    System.out.print("Please enter your name. ");
                    String name = scanner.nextLine();
                    r.addStaff(name);
                    done = true;
                } catch (Exception e) {
                    System.out.println("Invalid input. Please try again.");
                }
            }
            System.out.println("\n+---------------------------------------------+");
        }

        int exit = 0;
        while (exit == 0) {
            r.floor_status();
            System.out.println("Select one of the following menu options:");
            System.out.println("[1] Seat Guest");
            System.out.println("[2] Add Employee to Shift");
            System.out.println("[3] Assign Staff to Table");
            System.out.println("[4] Edit Layout of Restaurant");
            System.out.println("[0] Exit");
            System.out.print("Input: ");
            String input = scanner.nextLine();
            if (Integer.parseInt(input) == 2) {
                System.out.print("Please enter their name. ");
                String name = scanner.nextLine();
                r.addStaff(name);
                System.out.printf("%s has been added to the shift.\n", name);
            }
            if (Integer.parseInt(input) == 3) {
                int a = 1;
                String name = null;
                int num = 0;
                while (a != 0) {
                    System.out.println("Current Staff:");
                    System.out.println(r.getStaffNames());
                    System.out.print("Who would you like to assign to a table? ");
                    name = scanner.nextLine();
                    System.out.printf("What table would you like to assign %s to? ", name);
                    num = Integer.parseInt(scanner.nextLine());
                    a = r.assignToTable(name, num);
                }
                System.out.printf("%s has been assigned to table #%d\n", name, num);
            }
            if (Integer.parseInt(input) == 0) {
                exit = 1;

                System.out.println("Closing Application...");
            }
        }

    }


}