import java.util.Scanner;

public class RabinKarp {

    static Scanner sc = new Scanner(System.in);

    public static void searchPackage() {

        System.out.println("\n==========================================");
        System.out.println("        RABIN-KARP PACKAGE SEARCH");
        System.out.println("==========================================");

        System.out.print("Enter package keyword: ");

        String pattern = sc.nextLine().toLowerCase();

        boolean found = false;

        for (int i = 0;
                i < PackageInfo.packageNames.length;
                i++) {

            String text =
                    PackageInfo.packageNames[i].toLowerCase();

            if (rabinKarp(text, pattern)) {

                System.out.println("\nPackage Found!");

                System.out.println(
                        "Package: "
                        + PackageInfo.packageNames[i]
                );

                System.out.println(
                        "Price: Rs."
                        + PackageInfo.prices[i]
                );

                System.out.println(
                        "Duration: "
                        + PackageInfo.days[i]
                        + " days"
                );

                found = true;
            }
        }

        if (!found) {

            System.out.println(
                    "\nNo package found."
            );
        }
    }

    // Rabin-Karp Algorithm
    public static boolean rabinKarp(
            String text,
            String pattern) {

        int n = text.length();
        int m = pattern.length();

        if (m == 0) {
            return true;
        }

        if (m > n) {
            return false;
        }

        int base = 256;
        int prime = 101;

        long patternHash = 0;
        long textHash = 0;

        long highestPower = 1;

        for (int i = 0; i < m - 1; i++) {

            highestPower =
                    (highestPower * base) % prime;
        }

        for (int i = 0; i < m; i++) {

            patternHash =
                    (base * patternHash
                    + pattern.charAt(i))
                    % prime;

            textHash =
                    (base * textHash
                    + text.charAt(i))
                    % prime;
        }

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
                        (base *
                        (textHash
                        - text.charAt(i)
                        * highestPower)
                        + text.charAt(i + m))
                        % prime;

                if (textHash < 0) {
                    textHash += prime;
                }
            }
        }

        return false;
    }
}