import java.util.Scanner;

public class KMPSearch {

    static Scanner sc = new Scanner(System.in);

    public static void searchDestination() {

        System.out.println("\n==========================================");
        System.out.println("          KMP DESTINATION SEARCH");
        System.out.println("==========================================");

        System.out.print("Enter destination keyword: ");

        String pattern = sc.nextLine().toLowerCase();

        boolean found = false;

        for (int i = 0;
                i < Destination.destinations.length;
                i++) {

            String text =
                    Destination.destinations[i].toLowerCase();

            if (kmp(text, pattern)) {

                System.out.println("\nDestination Found!");
                System.out.println(
                        "Destination: "
                        + Destination.destinations[i]
                );

                System.out.println(
                        "Details: "
                        + Destination.descriptions[i]
                );

                found = true;
            }
        }

        if (!found) {

            System.out.println(
                    "\nNo destination found."
            );
        }
    }

    // KMP Algorithm
    public static boolean kmp(
            String text,
            String pattern) {

        if (pattern.length() == 0) {
            return true;
        }

        int[] lps = createLPS(pattern);

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

    // Create LPS array
    public static int[] createLPS(String pattern) {

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
}