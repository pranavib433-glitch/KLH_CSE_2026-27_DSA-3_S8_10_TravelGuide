public class PackageInfo {

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

    static double[] prices = {

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

    static int[] days = {

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

    public static void showPackages() {

        System.out.println("\n==========================================");
        System.out.println("              TOUR PACKAGES");
        System.out.println("==========================================");

        for (int i = 0; i < packageNames.length; i++) {

            System.out.println(
                    "\n" + (i + 1) + ". "
                    + packageNames[i]
            );

            System.out.println(
                    "   Price: Rs." + prices[i]
            );

            System.out.println(
                    "   Duration: " + days[i] + " days"
            );
        }
    }
}