public class Sorting {

    public static void sortByPrice() {

        String[] names =
                PackageInfo.packageNames.clone();

        double[] prices =
                PackageInfo.prices.clone();

        int[] days =
                PackageInfo.days.clone();

        // Selection Sort
        for (int i = 0;
                i < prices.length - 1;
                i++) {

            int minIndex = i;

            for (int j = i + 1;
                    j < prices.length;
                    j++) {

                if (prices[j]
                        < prices[minIndex]) {

                    minIndex = j;
                }
            }

            // Swap price
            double tempPrice =
                    prices[i];

            prices[i] =
                    prices[minIndex];

            prices[minIndex] =
                    tempPrice;

            // Swap name
            String tempName =
                    names[i];

            names[i] =
                    names[minIndex];

            names[minIndex] =
                    tempName;

            // Swap days
            int tempDay =
                    days[i];

            days[i] =
                    days[minIndex];

            days[minIndex] =
                    tempDay;
        }

        System.out.println(
                "\n=========================================="
        );

        System.out.println(
                "       PACKAGES SORTED BY PRICE"
        );

        System.out.println(
                "=========================================="
        );

        for (int i = 0;
                i < names.length;
                i++) {

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
}