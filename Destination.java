public class Destination {

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

        "Charminar, Golconda Fort and Hussain Sagar",

        "Beaches, nightlife and water sports",

        "Backwaters, beaches and hill stations",

        "Red Fort, India Gate and Qutub Minar",

        "Gateway of India and Marine Drive",

        "Gardens, technology parks and beautiful places",

        "Forts, palaces and royal heritage",

        "Taj Mahal and historical monuments",

        "Snow, mountains and adventure activities",

        "Tea gardens and beautiful hill stations"
    };

    public static void showDestinations() {

        System.out.println("\n==========================================");
        System.out.println("           TOURIST DESTINATIONS");
        System.out.println("==========================================");

        for (int i = 0; i < destinations.length; i++) {

            System.out.println("\n" + (i + 1)
                    + ". " + destinations[i]);

            System.out.println(
                    "   " + descriptions[i]
            );
        }
    }
}