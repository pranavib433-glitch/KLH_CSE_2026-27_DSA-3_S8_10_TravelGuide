import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * Console-based Travel Guide.
 *
 * Features: destination search (exact / partial / spelling suggestion),
 * packages and demo bookings, budget calculator, favorites,
 * text search (KMP + Rabin-Karp), description comparison (suffix array + LCP),
 * shortest route (Dijkstra) and multi-destination planning (bitmask DP).
 *
 * Distances and prices are illustrative demo data.
 */
public class TravelGuide {

    // =====================================================================
    //  MODEL
    // =====================================================================

    static final class Destination {
        final int id;
        final String name;
        final String speciality;
        final List<String> places;
        final String bestMonths;
        final String weather;
        final String precaution;
        final String thingsToCarry;
        final String transport;
        boolean favorite;

        Destination(int id, String name, String speciality, String places,
                    String bestMonths, String weather, String precaution,
                    String thingsToCarry, String transport) {
            this.id = id;
            this.name = name;
            this.speciality = speciality;
            this.places = Arrays.asList(places.split(",\\s*"));
            this.bestMonths = bestMonths;
            this.weather = weather;
            this.precaution = precaution;
            this.thingsToCarry = thingsToCarry;
            this.transport = transport;
        }

        /** Text used by the word-search feature. */
        String searchableText() {
            return (name + " " + speciality + " " + String.join(", ", places)).toLowerCase();
        }
    }

    enum TravelPackage {
        BUDGET("Budget Package", 2, 4999),
        STANDARD("Standard Package", 4, 8999),
        PREMIUM("Premium Package", 6, 14999);

        final String label;
        final int days;
        final double pricePerPerson;

        TravelPackage(String label, int days, double pricePerPerson) {
            this.label = label;
            this.days = days;
            this.pricePerPerson = pricePerPerson;
        }

        int nights() {
            return days - 1;
        }
    }

    static final class Booking {
        final String traveler;
        final Destination destination;
        final String date;
        final TravelPackage pack;
        final int travelers;

        Booking(String traveler, Destination destination, String date,
                TravelPackage pack, int travelers) {
            this.traveler = traveler;
            this.destination = destination;
            this.date = date;
            this.pack = pack;
            this.travelers = travelers;
        }

        double total() {
            return pack.pricePerPerson * travelers;
        }

        @Override
        public String toString() {
            return String.format("%s | %s | %s | %s | Travelers: %d | Total: Rs. %.2f",
                    traveler, destination.name, date, pack.label, travelers, total());
        }
    }

    // =====================================================================
    //  DATA
    // =====================================================================

    static final List<Destination> DESTINATIONS = List.of(
        new Destination(0, "Hyderabad",
            "Charminar, Hyderabadi Biryani and Golconda Fort",
            "Charminar, Golconda Fort, Hussain Sagar, Salar Jung Museum",
            "October to February", "Pleasant winters and hot summers",
            "Avoid extreme summer heat and stay hydrated.",
            "ID proof, water bottle, sunglasses, power bank, comfortable shoes",
            "Metro, Bus, Cab, Auto"),
        new Destination(1, "Goa",
            "Beaches, water sports and nightlife",
            "Baga Beach, Calangute Beach, Fort Aguada, Dudhsagar Falls",
            "November to February", "Warm weather with dry winter months",
            "Follow beach safety rules and avoid restricted swimming areas.",
            "ID proof, sunscreen, sunglasses, beachwear, power bank",
            "Bus, Cab, Bike Rental, Taxi"),
        new Destination(2, "Manali",
            "Mountains, snow and adventure activities",
            "Solang Valley, Rohtang Pass, Hidimba Temple, Mall Road",
            "March to June and December to February",
            "Cold mountain climate; winters can be very cold",
            "Carry warm clothes and check mountain weather conditions.",
            "Warm clothes, ID proof, medicines, gloves, comfortable shoes",
            "Bus, Cab, Taxi"),
        new Destination(3, "Bangalore",
            "IT hub, gardens and modern city life",
            "Lalbagh, Cubbon Park, Bangalore Palace, Vidhana Soudha",
            "October to February", "Pleasant and moderate climate",
            "Protect belongings in crowded places and take care in traffic.",
            "ID proof, umbrella, power bank, comfortable shoes",
            "Metro, Bus, Cab, Auto"),
        new Destination(4, "Chennai",
            "Temples, beaches and South Indian culture",
            "Marina Beach, Kapaleeshwarar Temple, Fort St. George, Mahabalipuram",
            "November to February", "Warm and humid; cooler during winter",
            "Carry water and sunscreen in hot weather.",
            "Water bottle, sunscreen, ID proof, comfortable clothes",
            "Metro, Bus, Cab, Auto"),
        new Destination(5, "Mumbai",
            "Gateway of India, Bollywood and Marine Drive",
            "Gateway of India, Marine Drive, Elephanta Caves, Juhu Beach",
            "November to February", "Warm and humid with pleasant winter months",
            "Keep valuables secure in crowded places.",
            "ID proof, power bank, comfortable shoes, small backpack",
            "Local Train, Metro, Bus, Cab"),
        new Destination(6, "Delhi",
            "Historical monuments, forts and street food",
            "India Gate, Red Fort, Qutub Minar, Lotus Temple",
            "October to March", "Hot summers and cool winters",
            "Protect yourself from heat and keep belongings safe.",
            "Water bottle, sunglasses, ID proof, sunscreen",
            "Metro, Bus, Cab, Auto"),
        new Destination(7, "Jaipur",
            "Royal palaces, forts and Rajasthani culture",
            "Amber Fort, City Palace, Hawa Mahal, Jantar Mantar",
            "October to March", "Dry summers and pleasant winters",
            "Wear comfortable footwear while visiting forts.",
            "Water bottle, comfortable shoes, sunglasses, ID proof",
            "Metro, Bus, Cab, Auto"),
        new Destination(8, "Ooty",
            "Tea gardens, hills and cool climate",
            "Ooty Lake, Botanical Garden, Doddabetta Peak, Tea Gardens",
            "October to June", "Cool and pleasant for most of the year",
            "Carry warm clothes and be careful on hill roads.",
            "Warm clothes, umbrella, comfortable shoes, camera",
            "Bus, Cab, Taxi"),
        new Destination(9, "Kerala",
            "Backwaters, beaches and greenery",
            "Munnar, Alleppey, Kochi, Wayanad",
            "September to March", "Tropical, humid and generally pleasant",
            "Carry rain protection and take care near water.",
            "Umbrella, raincoat, comfortable shoes, mosquito repellent",
            "Bus, Cab, Auto, Taxi")
    );

    static final List<Booking> BOOKINGS = new ArrayList<>();

    // =====================================================================
    //  CONSOLE INPUT
    // =====================================================================

    static final class Input {
        private static final Scanner SC = new Scanner(System.in);

        static String text(String prompt) {
            while (true) {
                System.out.print(prompt);
                String value = SC.nextLine().trim();
                if (!value.isEmpty()) return value;
                System.out.println("This field cannot be empty.");
            }
        }

        static int integer(String prompt) {
            while (true) {
                System.out.print(prompt);
                try {
                    return Integer.parseInt(SC.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println("Enter a valid whole number.");
                }
            }
        }

        static int integer(String prompt, int min, int max) {
            while (true) {
                int value = integer(prompt);
                if (value >= min && value <= max) return value;
                System.out.printf("Enter a number from %d to %d.%n", min, max);
            }
        }

        static double amount(String prompt) {
            while (true) {
                System.out.print(prompt);
                try {
                    double value = Double.parseDouble(SC.nextLine().trim());
                    if (Double.isFinite(value) && value >= 0) return value;
                    System.out.println("Enter a non-negative amount.");
                } catch (NumberFormatException e) {
                    System.out.println("Enter a valid amount.");
                }
            }
        }

        static boolean confirm(String prompt) {
            return text(prompt + " (Y/N): ").equalsIgnoreCase("Y");
        }

        static void close() {
            SC.close();
        }
    }

    // =====================================================================
    //  STRING ALGORITHMS
    // =====================================================================

    static final class StringAlgorithms {

        /** Levenshtein distance (dynamic programming). */
        static int editDistance(String a, String b) {
            int[][] dp = new int[a.length() + 1][b.length() + 1];
            for (int i = 0; i <= a.length(); i++) dp[i][0] = i;
            for (int j = 0; j <= b.length(); j++) dp[0][j] = j;

            for (int i = 1; i <= a.length(); i++) {
                for (int j = 1; j <= b.length(); j++) {
                    dp[i][j] = a.charAt(i - 1) == b.charAt(j - 1)
                        ? dp[i - 1][j - 1]
                        : 1 + Math.min(dp[i - 1][j - 1],
                                Math.min(dp[i - 1][j], dp[i][j - 1]));
                }
            }
            return dp[a.length()][b.length()];
        }

        /** Knuth-Morris-Pratt: index of first match, or -1. */
        static int kmpSearch(String text, String pattern) {
            if (pattern.isEmpty()) return 0;

            int[] lps = buildLps(pattern);
            int i = 0, j = 0;

            while (i < text.length()) {
                if (text.charAt(i) == pattern.charAt(j)) {
                    i++;
                    j++;
                    if (j == pattern.length()) return i - j;
                } else if (j > 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
            return -1;
        }

        private static int[] buildLps(String pattern) {
            int[] lps = new int[pattern.length()];
            int length = 0;
            int i = 1;

            while (i < pattern.length()) {
                if (pattern.charAt(i) == pattern.charAt(length)) {
                    lps[i++] = ++length;
                } else if (length > 0) {
                    length = lps[length - 1];
                } else {
                    lps[i++] = 0;
                }
            }
            return lps;
        }

        /** Rabin-Karp rolling hash: index of first match, or -1. */
        static int rabinKarpSearch(String text, String pattern) {
            if (pattern.isEmpty()) return 0;
            int m = pattern.length();
            if (m > text.length()) return -1;

            final long mod = 1_000_000_007L;
            final long base = 257;
            long patternHash = 0, windowHash = 0, highestPower = 1;

            for (int i = 0; i < m - 1; i++) highestPower = highestPower * base % mod;

            for (int i = 0; i < m; i++) {
                patternHash = (patternHash * base + pattern.charAt(i)) % mod;
                windowHash = (windowHash * base + text.charAt(i)) % mod;
            }

            for (int i = 0; i <= text.length() - m; i++) {
                if (patternHash == windowHash && text.regionMatches(i, pattern, 0, m))
                    return i;

                if (i < text.length() - m) {
                    windowHash = (windowHash - text.charAt(i) * highestPower % mod + mod) % mod;
                    windowHash = (windowHash * base + text.charAt(i + m)) % mod;
                }
            }
            return -1;
        }

        /** Longest common substring using a suffix array and LCP array. */
        static String longestCommonSubstring(String a, String b) {
            String combined = a + "{" + b; // '{' sorts after lowercase letters
            int separator = a.length();

            int[] sa = buildSuffixArray(combined);
            int[] lcp = buildLcpArray(combined, sa);

            int bestLength = 0, bestPosition = -1;

            for (int i = 1; i < sa.length; i++) {
                boolean prevInA = sa[i - 1] < separator;
                boolean currInA = sa[i] < separator;

                if (prevInA != currInA && lcp[i] > bestLength) {
                    bestLength = lcp[i];
                    bestPosition = Math.min(sa[i - 1], sa[i]);
                }
            }
            return bestLength == 0 ? "" : combined.substring(bestPosition, bestPosition + bestLength);
        }

        // Simple suffix sorting; fine for short descriptions.
        private static int[] buildSuffixArray(String text) {
            Integer[] suffixes = new Integer[text.length()];
            for (int i = 0; i < suffixes.length; i++) suffixes[i] = i;

            Arrays.sort(suffixes, Comparator.comparing(text::substring));

            int[] sa = new int[suffixes.length];
            for (int i = 0; i < sa.length; i++) sa[i] = suffixes[i];
            return sa;
        }

        // Kasai's algorithm.
        private static int[] buildLcpArray(String text, int[] sa) {
            int n = text.length();
            int[] rank = new int[n];
            int[] lcp = new int[n];
            for (int i = 0; i < n; i++) rank[sa[i]] = i;

            int common = 0;
            for (int i = 0; i < n; i++) {
                if (rank[i] == 0) {
                    common = 0;
                    continue;
                }
                int j = sa[rank[i] - 1];
                while (i + common < n && j + common < n
                        && text.charAt(i + common) == text.charAt(j + common)) {
                    common++;
                }
                lcp[rank[i]] = common;
                if (common > 0) common--;
            }
            return lcp;
        }
    }

    // =====================================================================
    //  ROUTE GRAPH
    // =====================================================================

    static final class RouteGraph {
        static final int INF = 1_000_000_000;
        private final int[][] weight;

        RouteGraph(int n) {
            weight = new int[n][n];
            for (int[] row : weight) Arrays.fill(row, INF);
            for (int i = 0; i < n; i++) weight[i][i] = 0;
        }

        void addRoute(int a, int b, int km) {
            weight[a][b] = km;
            weight[b][a] = km;
        }

        /** Dijkstra: returns {distance[], parent[]}. */
        int[][] dijkstra(int start) {
            int n = weight.length;
            int[] dist = new int[n];
            int[] parent = new int[n];
            Arrays.fill(dist, INF);
            Arrays.fill(parent, -1);
            dist[start] = 0;

            PriorityQueue<int[]> queue = new PriorityQueue<>(Comparator.comparingInt(x -> x[1]));
            queue.add(new int[]{start, 0});

            while (!queue.isEmpty()) {
                int[] top = queue.poll();
                int node = top[0];
                if (top[1] > dist[node]) continue; // stale entry

                for (int next = 0; next < n; next++) {
                    if (weight[node][next] >= INF) continue;
                    int candidate = dist[node] + weight[node][next];
                    if (candidate < dist[next]) {
                        dist[next] = candidate;
                        parent[next] = node;
                        queue.add(new int[]{next, candidate});
                    }
                }
            }
            return new int[][]{dist, parent};
        }

        /** Floyd-Warshall all-pairs shortest paths. */
        int[][] allPairs() {
            int n = weight.length;
            int[][] dist = new int[n][];
            for (int i = 0; i < n; i++) dist[i] = weight[i].clone();

            for (int k = 0; k < n; k++)
                for (int i = 0; i < n; i++)
                    for (int j = 0; j < n; j++)
                        if (dist[i][k] < INF && dist[k][j] < INF
                                && dist[i][k] + dist[k][j] < dist[i][j])
                            dist[i][j] = dist[i][k] + dist[k][j];
            return dist;
        }
    }

    static final RouteGraph GRAPH = buildGraph();

    private static RouteGraph buildGraph() {
        RouteGraph g = new RouteGraph(DESTINATIONS.size());
        // Illustrative weights only.
        g.addRoute(0, 3, 570);
        g.addRoute(0, 4, 630);
        g.addRoute(0, 5, 710);
        g.addRoute(1, 5, 590);
        g.addRoute(1, 3, 560);
        g.addRoute(2, 8, 250);
        g.addRoute(3, 4, 350);
        g.addRoute(3, 8, 270);
        g.addRoute(3, 9, 550);
        g.addRoute(4, 5, 1330);
        g.addRoute(4, 9, 600);
        g.addRoute(5, 6, 1400);
        g.addRoute(6, 7, 280);
        g.addRoute(7, 2, 650);
        return g;
    }

    // =====================================================================
    //  MAIN MENU
    // =====================================================================

    public static void main(String[] args) {
        System.out.println("======================================");
        System.out.println("        WELCOME TO TRAVEL GUIDE");
        System.out.println("======================================");
        login();

        boolean running = true;
        while (running) {
            printMainMenu();
            switch (Input.integer("Enter your choice: ")) {
                case 1:  searchDestination();       break;
                case 2:  listDestinations();        break;
                case 3:  showFavorites();           break;
                case 4:  budgetCalculator(pickDestination()); break;
                case 5:  showBookings();            break;
                case 6:  showEmergency();           break;
                case 7:  searchDescriptions();      break;
                case 8:  compareDescriptions();     break;
                case 9:  planMultiDestinationTrip(); break;
                case 10:
                    System.out.println("Thank you for using Travel Guide!");
                    running = false;
                    break;
                default: System.out.println("Invalid choice.");
            }
        }
        Input.close();
    }

    private static void printMainMenu() {
        System.out.println("\n============== MAIN MENU ==============");
        System.out.println(" 1. Search Destination");
        System.out.println(" 2. View All Destinations");
        System.out.println(" 3. View Favorites");
        System.out.println(" 4. Travel Budget Calculator");
        System.out.println(" 5. View My Bookings");
        System.out.println(" 6. Safety and Emergency Information");
        System.out.println(" 7. Search Words in Destination Details");
        System.out.println(" 8. Compare Destination Descriptions");
        System.out.println(" 9. Multi-Destination Trip Planner");
        System.out.println("10. Exit");
    }

    private static void login() {
        System.out.println("\n--------------- LOGIN ---------------");
        String username = Input.text("Enter username: ");
        Input.text("Enter password: ");
        System.out.println("Welcome, " + username + "!");
        System.out.println("(Demo login only; credentials are not verified.)");
    }

    // =====================================================================
    //  DESTINATION LISTING AND SELECTION
    // =====================================================================

    private static List<Destination> sortedDestinations() {
        List<Destination> sorted = new ArrayList<>(DESTINATIONS);
        sorted.sort(Comparator.comparing(d -> d.name, String.CASE_INSENSITIVE_ORDER));
        return sorted;
    }

    private static List<Destination> listDestinations() {
        List<Destination> sorted = sortedDestinations();
        System.out.println("\n========== DESTINATIONS ==========");
        for (int i = 0; i < sorted.size(); i++)
            System.out.println((i + 1) + ". " + sorted.get(i).name);
        return sorted;
    }

    /** Shows the list and returns the chosen destination, or null if cancelled. */
    private static Destination pickDestination() {
        List<Destination> sorted = listDestinations();
        int choice = Input.integer("Choose destination number (0 to cancel): ", 0, sorted.size());
        return choice == 0 ? null : sorted.get(choice - 1);
    }

    private static void showFavorites() {
        System.out.println("\n========== FAVORITES ==========");
        boolean found = false;
        for (Destination d : sortedDestinations()) {
            if (d.favorite) {
                System.out.println("- " + d.name);
                found = true;
            }
        }
        if (!found) System.out.println("No favorites yet. Add one from destination details.");
    }

    // =====================================================================
    //  SEARCH
    // =====================================================================

    private static Destination findExact(String input) {
        for (Destination d : DESTINATIONS)
            if (d.name.equalsIgnoreCase(input.trim())) return d;
        return null;
    }

    private static Destination findPartial(String input) {
        String query = input.trim().toLowerCase();
        for (Destination d : DESTINATIONS)
            if (d.name.toLowerCase().contains(query)) return d;
        return null;
    }

    private static Destination findClosest(String input) {
        String query = input.trim().toLowerCase();
        Destination best = null;
        int bestDistance = Integer.MAX_VALUE;

        for (Destination d : DESTINATIONS) {
            int distance = StringAlgorithms.editDistance(query, d.name.toLowerCase());
            if (distance < bestDistance) {
                bestDistance = distance;
                best = d;
            }
        }
        return bestDistance <= 3 ? best : null;
    }

    private static void searchDestination() {
        String input = Input.text("Enter destination name: ");

        Destination match = findExact(input);
        if (match == null) match = findPartial(input);
        if (match != null) {
            showDestination(match);
            return;
        }

        System.out.println("Destination not found.");
        Destination suggestion = findClosest(input);
        if (suggestion == null) {
            System.out.println("No close spelling match found.");
        } else if (Input.confirm("Did you mean " + suggestion.name + "? Open it")) {
            showDestination(suggestion);
        }
    }

    private static void searchDescriptions() {
        String pattern = Input.text("Enter a word or phrase to search: ").toLowerCase();
        boolean found = false;

        for (Destination d : DESTINATIONS) {
            String text = d.searchableText();
            int position = StringAlgorithms.kmpSearch(text, pattern);
            if (position < 0) continue;

            found = true;
            boolean verified = StringAlgorithms.rabinKarpSearch(text, pattern) >= 0;
            System.out.println("\nMatch in " + d.name);
            System.out.println("  Description    : " + text);
            System.out.println("  Match position : " + position);
            System.out.println("  Cross-check    : " + (verified ? "confirmed (Rabin-Karp)" : "not confirmed"));
        }
        if (!found) System.out.println("No matching descriptions found.");
    }

    private static void compareDescriptions() {
        Destination first = pickDestination();
        if (first == null) return;
        Destination second = pickDestination();
        if (second == null) return;

        String common = StringAlgorithms.longestCommonSubstring(
                first.speciality.toLowerCase(), second.speciality.toLowerCase());

        System.out.println("\nComparing " + first.name + " and " + second.name);
        if (common.isEmpty()) {
            System.out.println("No common substring found.");
        } else {
            System.out.println("Longest common substring: \"" + common + "\"");
            System.out.println("Length: " + common.length());
        }
    }

    // =====================================================================
    //  DESTINATION DETAILS
    // =====================================================================

    private static void showDestination(Destination d) {
        boolean open = true;
        while (open) {
            System.out.println("\n========== " + d.name.toUpperCase() + " ==========");
            System.out.println(" 1. Speciality");
            System.out.println(" 2. Famous Places");
            System.out.println(" 3. Best Time to Visit");
            System.out.println(" 4. Weather");
            System.out.println(" 5. Travel Precautions");
            System.out.println(" 6. Things to Carry");
            System.out.println(" 7. Transportation");
            System.out.println(" 8. Day-wise Itinerary");
            System.out.println(" 9. Travel Packages and Booking");
            System.out.println("10. Calculate Budget");
            System.out.println("11. Add/Remove Favorite");
            System.out.println("12. Emergency Information");
            System.out.println("13. Shortest Route to Another Destination");
            System.out.println("14. Back to Main Menu");

            switch (Input.integer("Enter your choice: ")) {
                case 1:  System.out.println(d.speciality); break;
                case 2:  System.out.println(String.join(", ", d.places)); break;
                case 3:  System.out.println(d.bestMonths); break;
                case 4:
                    System.out.println(d.weather);
                    System.out.println("Check a current forecast before travelling.");
                    break;
                case 5:  System.out.println(d.precaution); break;
                case 6:  System.out.println(d.thingsToCarry); break;
                case 7:  System.out.println(d.transport); break;
                case 8:  showItinerary(d); break;
                case 9:  showPackages(d); break;
                case 10: budgetCalculator(d); break;
                case 11:
                    d.favorite = !d.favorite;
                    System.out.println(d.name + (d.favorite
                        ? " added to favorites." : " removed from favorites."));
                    break;
                case 12: showEmergency(); break;
                case 13: {
                    Destination target = pickDestination();
                    if (target != null) showShortestRoute(d, target);
                    break;
                }
                case 14: open = false; break;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    private static void showItinerary(Destination d) {
        System.out.println("\nSAMPLE ITINERARY FOR " + d.name.toUpperCase());

        for (int day = 0; day < 3; day++) {
            System.out.println("\nDay " + (day + 1));
            int start = day * 2;
            int end = Math.min(start + 2, d.places.size());

            for (int i = start; i < end; i++)
                System.out.println("- Visit " + d.places.get(i));

            System.out.println("- Enjoy local food and leisure.");
        }
        System.out.println("\nSample itinerary; check opening hours and travel times.");
    }

    // =====================================================================
    //  PACKAGES, BOOKINGS, BUDGET
    // =====================================================================

    private static void showPackages(Destination d) {
        System.out.println("\nTRAVEL PACKAGES FOR " + d.name.toUpperCase());

        TravelPackage[] packs = TravelPackage.values();
        for (int i = 0; i < packs.length; i++) {
            System.out.printf("%d. %-17s %d days / %d nights   Rs. %,.2f per person%n",
                    i + 1, packs[i].label, packs[i].days, packs[i].nights(),
                    packs[i].pricePerPerson);
        }
        System.out.println("Prices are illustrative.");

        int choice = Input.integer("Select package (1-3), or 0 to cancel: ", 0, packs.length);
        if (choice == 0) return;

        TravelPackage selected = packs[choice - 1];
        System.out.println("Selected: " + selected.label);
        if (Input.confirm("Book this package?")) makeBooking(d, selected);
    }

    private static void makeBooking(Destination d, TravelPackage pack) {
        String name = Input.text("Traveler name: ");
        String date = Input.text("Travel date (DD-MM-YYYY): ");
        int travelers = Input.integer("Number of travelers: ", 1, 100);

        Booking booking = new Booking(name, d, date, pack, travelers);

        System.out.println("\n========== BOOKING SUMMARY ==========");
        System.out.println("Traveler    : " + name);
        System.out.println("Destination : " + d.name);
        System.out.println("Travel date : " + date);
        System.out.println("Package     : " + pack.label + " (" + pack.days + " days)");
        System.out.println("Travelers   : " + travelers);
        System.out.printf("Total price : Rs. %,.2f%n", booking.total());

        if (Input.confirm("Confirm demo booking?")) {
            BOOKINGS.add(booking);
            System.out.println("Demo booking confirmed for this session.");
        } else {
            System.out.println("Booking not confirmed.");
        }
    }

    private static void showBookings() {
        System.out.println("\n========== MY BOOKINGS ==========");
        if (BOOKINGS.isEmpty()) {
            System.out.println("No bookings in this session.");
            return;
        }
        for (int i = 0; i < BOOKINGS.size(); i++)
            System.out.println((i + 1) + ". " + BOOKINGS.get(i));
    }

    private static void budgetCalculator(Destination d) {
        if (d == null) return;

        int travelers = Input.integer("Number of travelers: ", 1, 1000);
        int days = Input.integer("Number of days: ", 1, 365);

        double hotel = Input.amount("Hotel cost per day for the group: ");
        double food = Input.amount("Food cost per day for the group: ");
        double transport = Input.amount("Transport cost per day for the group: ");
        double sightseeing = Input.amount("Sightseeing cost per day for the group: ");

        double total = (hotel + food + transport + sightseeing) * days;

        System.out.println("\nBUDGET SUMMARY - " + d.name);
        System.out.printf("Hotel       : Rs. %,.2f%n", hotel * days);
        System.out.printf("Food        : Rs. %,.2f%n", food * days);
        System.out.printf("Transport   : Rs. %,.2f%n", transport * days);
        System.out.printf("Sightseeing : Rs. %,.2f%n", sightseeing * days);
        System.out.printf("Group total : Rs. %,.2f%n", total);
        System.out.printf("Per person  : Rs. %,.2f%n", total / travelers);
    }

    // =====================================================================
    //  ROUTES AND TRIP PLANNING
    // =====================================================================

    private static void showShortestRoute(Destination from, Destination to) {
        int[][] result = GRAPH.dijkstra(from.id);
        int[] dist = result[0];
        int[] parent = result[1];

        if (dist[to.id] >= RouteGraph.INF) {
            System.out.println("No route exists in the sample graph.");
            return;
        }

        List<String> path = new ArrayList<>();
        for (int at = to.id; at != -1; at = parent[at])
            path.add(0, DESTINATIONS.get(at).name);

        System.out.println("\nSHORTEST DEMO ROUTE");
        System.out.println(String.join(" -> ", path));
        System.out.println("Sample distance: " + dist[to.id] + " km");
        System.out.println("Distances are illustrative, not real road data.");
    }

    /** Travelling-salesman style planner using bitmask DP (open path). */
    private static void planMultiDestinationTrip() {
        System.out.println("\nMULTI-DESTINATION TRIP PLANNER");
        System.out.println("Select 2 to 8 distinct destinations.");
        List<Destination> sorted = listDestinations();

        int count = Input.integer("How many destinations? ", 2, 8);
        Destination[] chosen = new Destination[count];
        boolean[] used = new boolean[sorted.size()];

        for (int i = 0; i < count; i++) {
            while (true) {
                int choice = Input.integer("Destination " + (i + 1) + " number: ", 1, sorted.size());
                if (used[choice - 1]) {
                    System.out.println("Choose a different destination.");
                    continue;
                }
                used[choice - 1] = true;
                chosen[i] = sorted.get(choice - 1);
                break;
            }
        }

        int[][] dist = GRAPH.allPairs();
        final long unreachable = Long.MAX_VALUE / 4;
        int states = 1 << count;
        long[][] dp = new long[states][count];
        int[][] parent = new int[states][count];
        for (int mask = 0; mask < states; mask++) {
            Arrays.fill(dp[mask], unreachable);
            Arrays.fill(parent[mask], -1);
        }

        dp[1][0] = 0; // the first selected destination is the start

        for (int mask = 1; mask < states; mask++) {
            for (int last = 0; last < count; last++) {
                if (dp[mask][last] >= unreachable) continue;

                for (int next = 0; next < count; next++) {
                    if ((mask & (1 << next)) != 0) continue;

                    int edge = dist[chosen[last].id][chosen[next].id];
                    if (edge >= RouteGraph.INF) continue;

                    int newMask = mask | (1 << next);
                    long candidate = dp[mask][last] + edge;
                    if (candidate < dp[newMask][next]) {
                        dp[newMask][next] = candidate;
                        parent[newMask][next] = last;
                    }
                }
            }
        }

        int fullMask = states - 1;
        int last = -1;
        long best = unreachable;
        for (int i = 0; i < count; i++) {
            if (dp[fullMask][i] < best) {
                best = dp[fullMask][i];
                last = i;
            }
        }

        if (last == -1) {
            System.out.println("No valid trip route exists for this selection.");
            return;
        }

        List<String> order = new ArrayList<>();
        int mask = fullMask;
        for (int current = last; current != -1; ) {
            order.add(0, chosen[current].name);
            int previous = parent[mask][current];
            mask ^= 1 << current;
            current = previous;
        }

        System.out.println("\nPLANNED TRIP");
        System.out.println(String.join(" -> ", order));
        System.out.println("Estimated sample distance: " + best + " km");
        System.out.println("Starts at your first pick and visits each destination once.");
        System.out.println("Distances are illustrative, not real navigation data.");
    }

    // =====================================================================
    //  SAFETY
    // =====================================================================

    private static void showEmergency() {
        System.out.println("\n========== SAFETY INFORMATION ==========");
        System.out.println("Emergency response : 112");
        System.out.println("Ambulance          : 108 (availability may vary)");
        System.out.println("Fire service       : 101 (availability may vary)");
        System.out.println("\nTravel precautions:");
        System.out.println("- Share your itinerary with a trusted person.");
        System.out.println("- Keep identification and emergency contacts accessible.");
        System.out.println("- Check official weather and transport updates.");
        System.out.println("- Follow local safety instructions.");
        System.out.println("Verify local emergency services before travelling.");
    }
}