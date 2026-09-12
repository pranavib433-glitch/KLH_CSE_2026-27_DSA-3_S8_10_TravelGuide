import java.util.Scanner;

// ============================================================
// SMART TOURISM & TRAVEL BOOKING SYSTEM
// ============================================================
// DSA Algorithms Used:
// 1. KMP String Matching
// 2. Rabin-Karp String Matching
// 3. Edit Distance - Wagner-Fischer Algorithm
// 4. Fuzzy Matching
// 5. Selection Sort
// ============================================================

public class Tourism {

    static Scanner sc = new Scanner(System.in);

    // =========================================================
    // DESTINATION DATA
    // =========================================================

    static String[] destinations = {
        "Hyderabad",
        "Goa",
        "Kerala",
        "Delhi",
        "Mumbai",
        "Bangalore",
        "Jaipur",
        "Agra",
        "Manali",
        "Ooty"
    };

    static String[] descriptions = {
        "City of Pearls, Charminar, Golconda Fort and Hussain Sagar",
        "Beautiful beaches, nightlife, forts and water sports",
        "God's Own Country with backwaters, beaches and hill stations",
        "Capital city with Red Fort, India Gate and Qutub Minar",
        "City of Dreams with Gateway of India and Marine Drive",
        "Garden City with technology parks and beautiful gardens",
        "Pink City with forts, palaces and royal heritage",
        "Home of the Taj Mahal and famous historical monuments",
        "Beautiful hill station with snow, mountains and adventure",
        "Queen of Hill Stations with tea gardens and pleasant weather"
    };

    // =========================================================
    // PACKAGE DATA
    // =========================================================

    static String[] packageNames = {
        "Hyderabad Heritage Tour",
        "Goa Beach Escape",
        "Kerala Backwater Holiday",
        "Delhi Historical Tour",
        "Mumbai City Explorer",
        "Bangalore Weekend Trip",
        "Jaipur Royal Experience",
        "Agra Taj Mahal Tour",
        "Manali Adventure Package",
        "Ooty Nature Retreat"
    };

    static double[] packagePrices = {
        6500,
        8500,
        12000,
        7000,
        7500,
        6000,
        9000,
        5500,
        14000,
        8000
    };

    static int[] packageDays = {
        3,
        4,
        5,
        3,
        3,
        2,
        4,
        2,
        5,
        3
    };

    // =========================================================
    // BOOKING HISTORY
    // =========================================================

    static String[] bookingHistory = new String[100];

    static int bookingCount = 0;

    // =========================================================
    // WISHLIST
    // =========================================================

    static String[] wishlist = new String[50];

    static int wishlistCount = 0;

    // =========================================================
    // MAIN METHOD
    // =========================================================

    public static void main(String[] args) {

        System.out.println("================================================");
        System.out.println("       SMART TOURISM & TRAVEL SYSTEM");
        System.out.println("================================================");

        boolean loggedIn = login();

        if (!loggedIn) {

            System.out.println();
            System.out.println("Too many login attempts.");
            System.out.println("Program terminated.");

            return;
        }

        mainMenu();
    }

    // =========================================================
    // LOGIN
    // =========================================================

    static boolean login() {

        String correctUsername = "pranavi";
        String correctPassword = "1234";

        int attempts = 3;

        while (attempts > 0) {

            System.out.println();
            System.out.println("---------------- LOGIN ----------------");

            System.out.print("Enter username: ");
            String username = sc.nextLine();

            System.out.print("Enter password: ");
            String password = sc.nextLine();

            if (username.equals(correctUsername)
                    && password.equals(correctPassword)) {

                System.out.println();
                System.out.println("Login Successful!");
                System.out.println("Welcome, " + username + "!");

                return true;
            }

            attempts--;

            System.out.println();
            System.out.println("Invalid username or password.");
            System.out.println("Attempts remaining: " + attempts);
        }

        return false;
    }

    // =========================================================
    // MAIN MENU
    // =========================================================

    static void mainMenu() {

        while (true) {

            System.out.println();
            System.out.println("================================================");
            System.out.println("                 MAIN MENU");
            System.out.println("================================================");

            System.out.println("1. View Destinations");
            System.out.println("2. Search Destination using KMP");
            System.out.println("3. Search Package using Rabin-Karp");
            System.out.println("4. Fuzzy Destination Search");
            System.out.println("5. View All Packages");
            System.out.println("6. Sort Packages by Price");
            System.out.println("7. Book a Package");
            System.out.println("8. Add Destination to Wishlist");
            System.out.println("9. View Wishlist");
            System.out.println("10. View Booking History");
            System.out.println("11. Exit");

            System.out.print("\nEnter your choice: ");

            int choice = readInt();

            switch (choice) {

                case 1:
                    viewDestinations();
                    break;

                case 2:
                    kmpDestinationSearch();
                    break;

                case 3:
                    rabinKarpPackageSearch();
                    break;

                case 4:
                    fuzzySearch();
                    break;

                case 5:
                    viewPackages();
                    break;

                case 6:
                    sortPackages();
                    break;

                case 7:
                    bookPackage();
                    break;

                case 8:
                    addWishlist();
                    break;

                case 9:
                    viewWishlist();
                    break;

                case 10:
                    viewBookingHistory();
                    break;

                case 11:

                    System.out.println();
                    System.out.println("Thank you for using");
                    System.out.println("Smart Tourism & Travel System!");

                    System.out.println("Goodbye!");

                    return;

                default:

                    System.out.println();
                    System.out.println("Invalid choice.");
            }
        }
    }

    // =========================================================
    // 1. VIEW DESTINATIONS
    // =========================================================

    static void viewDestinations() {

        System.out.println();
        System.out.println("================================================");
        System.out.println("              TOURIST DESTINATIONS");
        System.out.println("================================================");

        for (int i = 0; i < destinations.length; i++) {

            System.out.println();
            System.out.println((i + 1) + ". " + destinations[i]);

            System.out.println(
                "   " + descriptions[i]
            );
        }
    }

    // =========================================================
    // 2. KMP DESTINATION SEARCH
    // =========================================================

    static void kmpDestinationSearch() {

        System.out.println();
        System.out.println("================================================");
        System.out.println("             KMP DESTINATION SEARCH");
        System.out.println("================================================");

        System.out.print("Enter destination keyword: ");

        String pattern = sc.nextLine();

        boolean found = false;

        for (int i = 0; i < destinations.length; i++) {

            boolean result = kmpSearch(
                destinations[i].toLowerCase(),
                pattern.toLowerCase()
            );

            if (result) {

                System.out.println();
                System.out.println("Destination Found!");
                System.out.println("Destination: " + destinations[i]);

                System.out.println(
                    "Details: " + descriptions[i]
                );

                found = true;
            }
        }

        if (!found) {

            System.out.println();
            System.out.println(
                "No destination found using KMP."
            );
        }
    }

    // =========================================================
    // KMP ALGORITHM
    // =========================================================

    static boolean kmpSearch(String text, String pattern) {

        if (pattern.length() == 0) {

            return true;
        }

        int[] lps = buildLPS(pattern);

        int i = 0;

        int j = 0;

        while (i < text.length()) {

            if (text.charAt(i) == pattern.charAt(j)) {

                i++;

                j++;

                if (j == pattern.length()) {

                    return true;
                }

            } else {

                if (j != 0) {

                    j = lps[j - 1];

                } else {

                    i++;
                }
            }
        }

        return false;
    }

    // =========================================================
    // BUILD LPS ARRAY
    // =========================================================

    static int[] buildLPS(String pattern) {

        int[] lps = new int[pattern.length()];

        int length = 0;

        int i = 1;

        while (i < pattern.length()) {

            if (pattern.charAt(i)
                    == pattern.charAt(length)) {

                length++;

                lps[i] = length;

                i++;

            } else {

                if (length != 0) {

                    length = lps[length - 1];

                } else {

                    lps[i] = 0;

                    i++;
                }
            }
        }

        return lps;
    }

    // =========================================================
    // 3. RABIN-KARP PACKAGE SEARCH
    // =========================================================

    static void rabinKarpPackageSearch() {

        System.out.println();
        System.out.println("================================================");
        System.out.println("          RABIN-KARP PACKAGE SEARCH");
        System.out.println("================================================");

        System.out.print("Enter package keyword: ");

        String pattern = sc.nextLine();

        boolean found = false;

        for (int i = 0; i < packageNames.length; i++) {

            boolean result = rabinKarp(
                packageNames[i].toLowerCase(),
                pattern.toLowerCase()
            );

            if (result) {

                System.out.println();
                System.out.println("Package Found!");

                System.out.println(
                    "Package: " + packageNames[i]
                );

                System.out.println(
                    "Price: Rs." + packagePrices[i]
                );

                System.out.println(
                    "Duration: " + packageDays[i] + " days"
                );

                found = true;
            }
        }

        if (!found) {

            System.out.println();
            System.out.println(
                "No package found using Rabin-Karp."
            );
        }
    }

    // =========================================================
    // RABIN-KARP ALGORITHM
    // =========================================================

    static boolean rabinKarp(String text, String pattern) {

        int n = text.length();

        int m = pattern.length();

        if (m == 0) {

            return true;
        }

        if (m > n) {

            return false;
        }

        int prime = 101;

        int base = 256;

        long patternHash = 0;

        long textHash = 0;

        long highestPower = 1;

        // Calculate highest power
        for (int i = 0; i < m - 1; i++) {

            highestPower =
                (highestPower * base) % prime;
        }

        // Calculate initial hashes
        for (int i = 0; i < m; i++) {

            patternHash =
                (base * patternHash
                + pattern.charAt(i)) % prime;

            textHash =
                (base * textHash
                + text.charAt(i)) % prime;
        }

        // Sliding window
        for (int i = 0; i <= n - m; i++) {

            if (patternHash == textHash) {

                boolean match = true;

                for (int j = 0; j < m; j++) {

                    if (text.charAt(i + j)
                            != pattern.charAt(j)) {

                        match = false;

                        break;
                    }
                }

                if (match) {

                    return true;
                }
            }

            if (i < n - m) {

                textHash =
                    (
                        base * (
                            textHash
                            - text.charAt(i)
                            * highestPower
                        )
                        + text.charAt(i + m)
                    ) % prime;

                if (textHash < 0) {

                    textHash += prime;
                }
            }
        }

        return false;
    }

    // =========================================================
    // 4. EDIT DISTANCE
    // =========================================================

    static int editDistance(String a, String b) {

        int n = a.length();

        int m = b.length();

        int[][] dp = new int[n + 1][m + 1];

        // First column
        for (int i = 0; i <= n; i++) {

            dp[i][0] = i;
        }

        // First row
        for (int j = 0; j <= m; j++) {

            dp[0][j] = j;
        }

        // Fill DP table
        for (int i = 1; i <= n; i++) {

            for (int j = 1; j <= m; j++) {

                if (a.charAt(i - 1)
                        == b.charAt(j - 1)) {

                    dp[i][j] =
                        dp[i - 1][j - 1];

                } else {

                    int insert =
                        dp[i][j - 1];

                    int delete =
                        dp[i - 1][j];

                    int replace =
                        dp[i - 1][j - 1];

                    dp[i][j] =
                        1 + Math.min(
                            insert,
                            Math.min(delete, replace)
                        );
                }
            }
        }

        return dp[n][m];
    }

    // =========================================================
    // 5. FUZZY MATCHING
    // =========================================================

    static void fuzzySearch() {

        System.out.println();
        System.out.println("================================================");
        System.out.println("             FUZZY DESTINATION SEARCH");
        System.out.println("================================================");

        System.out.print("Enter destination name: ");

        String input = sc.nextLine().toLowerCase();

        int bestDistance = Integer.MAX_VALUE;

        String bestDestination = "";

        // Compare input with all destinations
        for (String destination : destinations) {

            int distance =
                editDistance(
                    input,
                    destination.toLowerCase()
                );

            System.out.println(
                destination
                + " -> Edit Distance = "
                + distance
            );

            if (distance < bestDistance) {

                bestDistance = distance;

                bestDestination = destination;
            }
        }

        System.out.println();
        System.out.println("--------------------------------");

        System.out.println("Best Fuzzy Match:");

        System.out.println(bestDestination);

        System.out.println(
            "Edit Distance: " + bestDistance
        );

        if (bestDistance <= 3) {

            System.out.println();

            System.out.println(
                "Did you mean: "
                + bestDestination
                + "?"
            );

        } else {

            System.out.println();

            System.out.println(
                "No close destination found."
            );
        }
    }

    // =========================================================
    // 6. VIEW PACKAGES
    // =========================================================

    static void viewPackages() {

        System.out.println();
        System.out.println("================================================");
        System.out.println("                 TOUR PACKAGES");
        System.out.println("================================================");

        for (int i = 0; i < packageNames.length; i++) {

            System.out.println();

            System.out.println(
                (i + 1) + ". " + packageNames[i]
            );

            System.out.println(
                "   Price: Rs."
                + packagePrices[i]
            );

            System.out.println(
                "   Duration: "
                + packageDays[i]
                + " days"
            );
        }
    }

    // =========================================================
    // 7. SORT PACKAGES BY PRICE
    // Selection Sort
    // =========================================================

    static void sortPackages() {

        System.out.println();
        System.out.println("================================================");
        System.out.println("        PACKAGES SORTED BY PRICE");
        System.out.println("================================================");

        String[] names = packageNames.clone();

        double[] prices = packagePrices.clone();

        int[] days = packageDays.clone();

        // Selection Sort
        for (int i = 0;
             i < prices.length - 1;
             i++) {

            int minIndex = i;

            for (int j = i + 1;
                 j < prices.length;
                 j++) {

                if (prices[j] < prices[minIndex]) {

                    minIndex = j;
                }
            }

            // Swap price
            double tempPrice = prices[i];

            prices[i] = prices[minIndex];

            prices[minIndex] = tempPrice;

            // Swap package name
            String tempName = names[i];

            names[i] = names[minIndex];

            names[minIndex] = tempName;

            // Swap duration
            int tempDay = days[i];

            days[i] = days[minIndex];

            days[minIndex] = tempDay;
        }

        // Display sorted packages
        for (int i = 0; i < names.length; i++) {

            System.out.println(
                (i + 1)
                + ". "
                + names[i]
                + " - Rs."
                + prices[i]
                + " - "
                + days[i]
                + " days"
            );
        }
    }

    // =========================================================
    // 8. BOOK PACKAGE
    // =========================================================

    static void bookPackage() {

        viewPackages();

        System.out.print(
            "\nEnter package number to book: "
        );

        int packageNumber = readInt();

        if (packageNumber < 1
                || packageNumber > packageNames.length) {

            System.out.println(
                "Invalid package number."
            );

            return;
        }

        int index = packageNumber - 1;

        System.out.print(
            "Enter number of travellers: "
        );

        int travellers = readInt();

        if (travellers <= 0) {

            System.out.println(
                "Invalid number of travellers."
            );

            return;
        }

        double baseCost =
            packagePrices[index] * travellers;

        double tax =
            baseCost * 0.05;

        double total =
            baseCost + tax;

        // Display Bill
        System.out.println();
        System.out.println("================================================");
        System.out.println("                  TOUR BILL");
        System.out.println("================================================");

        System.out.println(
            "Package      : "
            + packageNames[index]
        );

        System.out.println(
            "Travellers   : "
            + travellers
        );

        System.out.println(
            "Duration     : "
            + packageDays[index]
            + " days"
        );

        System.out.println(
            "Base Cost    : Rs."
            + baseCost
        );

        System.out.println(
            "GST (5%)     : Rs."
            + tax
        );

        System.out.println("--------------------------------");

        System.out.println(
            "TOTAL        : Rs."
            + total
        );

        System.out.println("================================================");

        // Store booking
        if (bookingCount < bookingHistory.length) {

            bookingHistory[bookingCount] =
                packageNames[index]
                + " | Travellers: "
                + travellers
                + " | Total: Rs."
                + total;

            bookingCount++;
        }

        System.out.println();
        System.out.println("Booking Successful!");
    }

    // =========================================================
    // 9. ADD TO WISHLIST
    // =========================================================

    static void addWishlist() {

        viewDestinations();

        System.out.print(
            "\nEnter destination number to add "
            + "to wishlist: "
        );

        int choice = readInt();

        if (choice < 1
                || choice > destinations.length) {

            System.out.println(
                "Invalid destination."
            );

            return;
        }

        if (wishlistCount < wishlist.length) {

            wishlist[wishlistCount] =
                destinations[choice - 1];

            wishlistCount++;

            System.out.println();

            System.out.println(
                destinations[choice - 1]
                + " added to wishlist!"
            );

        } else {

            System.out.println(
                "Wishlist is full."
            );
        }
    }

    // =========================================================
    // 10. VIEW WISHLIST
    // =========================================================

    static void viewWishlist() {

        System.out.println();
        System.out.println("================================================");
        System.out.println("                  MY WISHLIST");
        System.out.println("================================================");

        if (wishlistCount == 0) {

            System.out.println(
                "Wishlist is empty."
            );

            return;
        }

        for (int i = 0;
             i < wishlistCount;
             i++) {

            System.out.println(
                (i + 1)
                + ". "
                + wishlist[i]
            );
        }
    }

    // =========================================================
    // 11. VIEW BOOKING HISTORY
    // =========================================================

    static void viewBookingHistory() {

        System.out.println();
        System.out.println("================================================");
        System.out.println("                BOOKING HISTORY");
        System.out.println("================================================");

        if (bookingCount == 0) {

            System.out.println(
                "No bookings available."
            );

            return;
        }

        for (int i = 0;
             i < bookingCount;
             i++) {

            System.out.println(
                (i + 1)
                + ". "
                + bookingHistory[i]
            );
        }
    }

    // =========================================================
    // SAFE INTEGER INPUT
    // =========================================================

    static int readInt() {

        while (true) {

            try {

                return Integer.parseInt(
                    sc.nextLine()
                );

            } catch (Exception e) {

                System.out.print(
                    "Please enter a valid number: "
                );
            }
        }
    }
}