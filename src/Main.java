import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        HelpService helpService = new HelpService();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Velkommen til TwoFeet - din udflytnings-hjælper ");

        boolean running = true;
        while (running) {
            printMainMenu();
            System.out.print("Vælg et nummer: ");
            String input = scanner.nextLine();

            switch (input) {
                case "1" -> showCategory(helpService, HelpCategory.LAUNDRY, scanner);
                case "2" -> showCategory(helpService, HelpCategory.ELECTRONICS, scanner);
                case "3" -> showCategory(helpService, HelpCategory.MOVE_IN, scanner);
                case "4" -> showCategory(helpService, HelpCategory.STARTERPACK, scanner);
                case "5" -> showCategory(helpService, HelpCategory.CLEANING, scanner);
                case "6" -> freeTextQuestion(helpService, scanner);
                case "0" -> {
                    System.out.println("Farvel og held og lykke med hjemmet! 😊");
                    running = false;
                }
                default -> System.out.println("Ugyldigt valg, prøv igen.\n");
            }
        }
    }

    private static void printMainMenu() {
        System.out.println("\n--- HOVEDMENU ---");
        System.out.println("1) Tøjvask");
        System.out.println("2) Elektronik (køleskab/fryser)");
        System.out.println("3) Inden indflytning");
        System.out.println("4) Udflytnings-starterpack");
        System.out.println("5) Rengøringsrutine");
        System.out.println("6) Stil dit eget spørgsmål (fritekst)");
        System.out.println("0) Afslut");
    }

    private static void showCategory(HelpService helpService,
                                     HelpCategory category,
                                     Scanner scanner) {
        List<HelpTopic> topics = helpService.getTopicsByCategory(category);
        if (topics.isEmpty()) {
            System.out.println("Der er ingen emner i denne kategori endnu.");
            return;
        }

        System.out.println("\nEmner i kategorien: " + category);
        for (int i = 0; i < topics.size(); i++) {
            System.out.println((i + 1) + ") " + topics.get(i).getTitle());
        }
        System.out.println("0) Tilbage til hovedmenu");

        System.out.print("Vælg et emne: ");
        String input = scanner.nextLine();

        if ("0".equals(input)) {
            return;
        }

        try {
            int choice = Integer.parseInt(input);
            if (choice < 1 || choice > topics.size()) {
                System.out.println("Ugyldigt valg.\n");
                return;
            }
            HelpTopic chosen = topics.get(choice - 1);
            System.out.println("\n--- " + chosen.getTitle() + " ---");
            System.out.println(chosen.getAnswerText());
            System.out.println();
        } catch (NumberFormatException e) {
            System.out.println("Du skal skrive et tal.\n");
        }
    }

    private static void freeTextQuestion(HelpService helpService, Scanner scanner) {
        System.out.println("\nSkriv dit spørgsmål (eller 'tilbage' for at gå til menuen):");
        System.out.print("> ");
        String q = scanner.nextLine();

        if (q.equalsIgnoreCase("tilbage")) {
            return;
        }

        String answer = helpService.ask(q);
        System.out.println("\n" + answer + "\n");
    }
}
