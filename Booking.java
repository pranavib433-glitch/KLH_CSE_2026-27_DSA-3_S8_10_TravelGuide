import java.util.Scanner;

public class Booking {

    static Scanner sc = new Scanner(System.in);

    static String[] history =
            new String[100];

    static int count = 0;

    public static void book() {

        PackageInfo.showPackages();

        System.out.print(
                "\nEnter package number: "
        );

        int choice =
                Integer.parseInt(
                        sc.nextLine()
                );

        if (choice < 1 ||
                choice > PackageInfo.packageNames.length) {

            System.out.println(
                    "Invalid package."
            );

            return;
        }

        System.out.print(
                "Enter number of travellers: "
        );

        int travellers =
                Integer.parseInt(
                        sc.nextLine()
                );

        if (travellers <= 0) {

            System.out.println(
                    "Invalid number of travellers."
            );

            return;
        }

        int index = choice - 1;

        double baseCost =
                PackageInfo.prices[index]
                * travellers;

        double gst =
                baseCost * 0.05;

        double total =
                baseCost + gst;

        System.out.println(
                "\n=========================================="
        );

        System.out.println(
                "                 TOUR BILL"
        );

        System.out.println(
                "=========================================="
        );

        System.out.println(
                "Package     : "
                + PackageInfo.packageNames[index]
        );

        System.out.println(
                "Travellers  : "
                + travellers
        );

        System.out.println(
                "Duration    : "
                + PackageInfo.days[index]
                + " days"
        );

        System.out.println(
                "Base Cost   : Rs."
                + baseCost
        );

        System.out.println(
                "GST (5%)    : Rs."
                + gst
        );

        System.out.println(
                "------------------------------------------"
        );

        System.out.println(
                "TOTAL       : Rs."
                + total
        );

        System.out.println(
                "=========================================="
        );

        // Store booking
        history[count] =
                PackageInfo.packageNames[index]
                + " | Travellers: "
                + travellers
                + " | Total: Rs."
                + total;

        count++;

        System.out.println(
                "\nBooking Successful!"
        );
    }

    public static void showHistory() {

        System.out.println(
                "\n=========================================="
        );

        System.out.println(
                "             BOOKING HISTORY"
        );

        System.out.println(
                "=========================================="
        );

        if (count == 0) {

            System.out.println(
                    "No bookings available."
            );

            return;
        }

        for (int i = 0;
                i < count;
                i++) {

            System.out.println(
                    (i + 1)
                    + ". "
                    + history[i]
            );
        }
    }
}