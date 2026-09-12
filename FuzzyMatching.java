import java.util.Scanner;

public class FuzzyMatching {

    static Scanner sc = new Scanner(System.in);

    public static void search() {

        System.out.println("\n==========================================");
        System.out.println("          FUZZY DESTINATION SEARCH");
        System.out.println("==========================================");

        System.out.print(
                "Enter destination name: "
        );

        String input =
                sc.nextLine().toLowerCase();

        int bestDistance =
                Integer.MAX_VALUE;

        String bestDestination = "";

        for (String destination :
                Destination.destinations) {

            int distance =
                    EditDistance.calculate(
                            input,
                            destination.toLowerCase()
                    );

            System.out.println(
                    destination
                    + " -> Distance = "
                    + distance
            );

            if (distance < bestDistance) {

                bestDistance = distance;

                bestDestination = destination;
            }
        }

        System.out.println(
                "\n------------------------------------------"
        );

        System.out.println(
                "Best Match: "
                + bestDestination
        );

        System.out.println(
                "Edit Distance: "
                + bestDistance
        );

        if (bestDistance <= 3) {

            System.out.println(
                    "\nDid you mean: "
                    + bestDestination
                    + "?"
            );

        } else {

            System.out.println(
                    "\nNo close destination found."
            );
        }
    }
}