import java.util.Scanner;

public class Login {

    static Scanner sc = new Scanner(System.in);

    public static boolean login() {

        String correctUsername = "pranavi";
        String correctPassword = "1234";

        int attempts = 3;

        while (attempts > 0) {

            System.out.println("\n------------- LOGIN -------------");

            System.out.print("Enter Username: ");
            String username = sc.nextLine();

            System.out.print("Enter Password: ");
            String password = sc.nextLine();

            if (username.equals(correctUsername)
                    && password.equals(correctPassword)) {

                System.out.println("\nLogin Successful!");
                System.out.println("Welcome, " + username);

                return true;
            }

            attempts--;

            System.out.println("\nInvalid Username or Password.");
            System.out.println("Attempts remaining: " + attempts);
        }

        return false;
    }
}