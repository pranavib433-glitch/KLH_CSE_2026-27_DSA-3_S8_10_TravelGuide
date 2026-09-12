import java.util.Scanner;

public class Wishlist {

    static Scanner sc = new Scanner(System.in);

    static String[] list =
            new String[50];

    static int count = 0;

    public static void add() {

        Destination.showDestinations();

        System.out.print(
                "\nEnter destination number: "
        );

        int choice =
                Integer.parseInt(
                        sc.nextLine()
                );

        if (choice < 1 ||
                choice > Destination.destinations.length) {

            System.out.println(
                    "Invalid destination."
            );

            return;
        }

        list[count] =
                Destination.destinations[
                        choice - 1
                ];

        count++;

        System.out.println(
                "\n"
                + Destination.destinations[
                        choice - 1
                ]
                + " added to Wishlist!"
        );
    }

    public static void show() {

        System.out.println(
                "\n=========================================="
        );

        System.out.println(
                "                 WISHLIST"
        );

        System.out.println(
                "=========================================="
        );

        if (count == 0) {

            System.out.println(
                    "Wishlist is empty."
            );

            return;
        }

        for (int i = 0;
                i < count;
                i++) {

            System.out.println(
                    (i + 1)
                    + ". "
                    + list[i]
            );
        }
    }
}