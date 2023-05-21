package budget;

import budget.Purchases.Clothes;
import budget.Purchases.Entertainment;
import budget.Purchases.Food;
import budget.Purchases.Other;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Budget {

    double balance = 0.0;
    int purchaseSizeList = 0;
    List<Food> foodList = new ArrayList<>();
    List<Clothes> clothesList = new ArrayList<>();
    List<Entertainment> entertainmentList = new ArrayList<>();
    List<Other> otherList = new ArrayList<>();

    double totalSumOfFood = 0;
    double totalSumOfClothes = 0;
    double totalSumOfEntertainment = 0;
    double totalSumOfOther = 0;
    double totalSum = totalSumOfClothes + totalSumOfFood + totalSumOfOther + totalSumOfEntertainment;


    void chooseAction(Scanner scanner) {
        System.out.println("Choose your action");
        System.out.println("1) Add income");
        System.out.println("2) Add purchase");
        System.out.println("3) Show list of purchases");
        System.out.println("4) Balance");
        System.out.println("5) Save");
        System.out.println("6) Load");
        System.out.println("7) Analyze (Sort)");
        System.out.println("0) Exit");


        int choose = scanner.nextInt();
        scanner.nextLine();
        switch (choose) {
            case 1 -> addIncome(scanner);
            case 2 -> addPurchase(scanner);
            case 3 -> showListOfPurchases(scanner);
            case 4 -> showBalance();
            case 5 -> saveToFile();
            case 6 -> readFile();
            case 7 -> analyze(scanner);
            case 0 -> exitProgram();
        }
        System.out.println();
    }

    private void analyze(Scanner scanner) {
        while (true) {
            System.out.println("\nHow do you want to sort?");
            System.out.println("1) Sort all purchases");
            System.out.println("2) Sort by type");
            System.out.println("3) Sort certain type");
            System.out.println("4) Back");
            int choose = scanner.nextInt();


            switch (choose) {
                case 1 -> {
                    if (purchaseSizeList == 0) {
                        System.out.println("\nThe purchase list is empty!");
                    } else {
                        Map<String, Double> map = new HashMap<>();

                        for (Food food : foodList
                        ) {
                            map.put(food.getName(), food.getPrice());
                        }
                        for (Clothes clothes : clothesList
                        ) {
                            map.put(clothes.getName(), clothes.getPrice());
                        }
                        for (Entertainment entertainment : entertainmentList
                        ) {
                            map.put(entertainment.getName(), entertainment.getPrice());
                        }
                        for (Other other : otherList
                        ) {
                            map.put(other.getName(), other.getPrice());
                        }


                        System.out.println("\nAll:");

                        map.entrySet().stream()
                                .sorted(Map.Entry.comparingByKey(Comparator.reverseOrder()))
                                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())
                                ).forEach((k) -> {
                                    System.out.printf(k.getKey());
                                    System.out.printf(" $%.2f%n", k.getValue());
                                });

                        System.out.printf("Total: $%.2f%n", totalSum);

                    }
                }
                case 2 -> {
                    System.out.println("\nTypes:");

                    if (purchaseSizeList == 0) {
                        System.out.println("Food - $0");
                        System.out.println("Entertainment - $0");
                        System.out.println("Clothes - $0");
                        System.out.println("Other - $0");
                        System.out.println("Total sum: $0");
                    } else {
                        Map<String, Double> map = new HashMap<>();
                        map.put("Food", totalSumOfFood);
                        map.put("Entertainment", totalSumOfEntertainment);
                        map.put("Clothes", totalSumOfClothes);
                        map.put("Other", totalSumOfOther);

                        map.entrySet().stream()

                                .sorted(Map.Entry.comparingByKey(Comparator.reverseOrder()))
                                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())
                                ).forEach((k) -> {
                                    System.out.printf(k.getKey());
                                    System.out.printf(" - $%.2f%n", k.getValue());
                                });

                        System.out.printf("Total: $%.2f%n", totalSum);
                    }
                }
                case 3 -> {
                    System.out.println("\nChoose the type of purchase");
                    System.out.println("1) Food");
                    System.out.println("2) Clothes");
                    System.out.println("3) Entertainment");
                    System.out.println("4) Other");

                    int chooser = scanner.nextInt();

                    switch (chooser) {
                        case 1 -> {
                            if (foodList.isEmpty()) {
                                System.out.println("\nThe purchase list is empty!");
                            } else {
                                System.out.println("\nFood");
                                foodList.stream().sorted(Comparator.comparingDouble(Food::getPrice).reversed()).forEach(System.out::println);
                                System.out.printf("Total: $%.2f%n", totalSumOfFood);
                            }
                        }
                        case 2 -> {
                            if (clothesList.isEmpty()) {
                                System.out.println("\nThe purchase list is empty!");
                            } else {
                                System.out.println("\nClothes");
                                clothesList.stream().sorted(Comparator.comparingDouble(Clothes::getPrice).reversed()).forEach(System.out::println);
                                System.out.printf("Total: $%.2f%n", totalSumOfClothes);
                            }
                        }
                        case 3 -> {
                            if (entertainmentList.isEmpty()) {
                                System.out.println("\nThe purchase list is empty!");
                            } else {
                                System.out.println("\nEntertainment");
                                entertainmentList.stream().sorted(Comparator.comparingDouble(Entertainment::getPrice).reversed()).forEach(System.out::println);
                                System.out.printf("Total: $%.2f%n", totalSumOfEntertainment);
                            }
                        }
                        case 4 -> {
                            if (otherList.isEmpty()) {
                                System.out.println("\nThe purchase list is empty!");
                            } else {
                                System.out.println("\nOther");
                                otherList.stream().sorted(Comparator.comparingDouble(Other::getPrice).reversed()).forEach(System.out::println);
                                System.out.printf("Total: $%.2f%n", totalSumOfOther);
                            }
                        }
                    }
                }
                case 4 -> {
                    return;
                }
            }
        }
    }

    private void readFile() {

        File file = new File("/home/tompaw/IdeaProjects/Budget Manager/Budget Manager/task/src/budget/purchases.txt");
//        File file = new File("purchases.txt");

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext()) {

                String line = scanner.nextLine();

                if (line.contains("Balance:")) {
                    int dollarIndex = line.indexOf("$");
                    balance = Double.parseDouble(line.substring(dollarIndex + 1, line.length()));
                    continue;
                }

                int indexOfSpace = line.indexOf(" ");
                int dollarIndex = 0;

                String patternStr = "\\$\\d+\\.\\d+";
                Pattern pattern = Pattern.compile(patternStr);
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    dollarIndex = matcher.start();
                }

                String category = line.substring(0, indexOfSpace - 1);
                String name = line.substring(indexOfSpace + 1, dollarIndex - 1);
                double price = Double.parseDouble(line.substring(dollarIndex + 1));

                switch (category) {
                    case "Food" -> {
                        Food food = new Food(name, price);
                        foodList.add(food);
                        totalSumOfFood += price;
                        purchaseSizeList++;
                    }
                    case "Clothes" -> {
                        Clothes clothes = new Clothes(name, price);
                        clothesList.add(clothes);
                        totalSumOfClothes += price;
                        purchaseSizeList++;
                    }
                    case "Entertainment" -> {
                        Entertainment entertainment = new Entertainment(name, price);
                        entertainmentList.add(entertainment);
                        totalSumOfEntertainment += price;
                        purchaseSizeList++;
                    }
                    case "Other" -> {
                        Other other = new Other(name, price);
                        otherList.add(other);
                        totalSumOfOther += price;
                        purchaseSizeList++;
                    }
                }
            }
            System.out.println("\nPurchases were loaded!");
        } catch (FileNotFoundException e) {
            System.out.println("No file found: " + file.getPath());
        }
    }

    private void saveToFile() {
        File file = new File("/home/tompaw/IdeaProjects/Budget Manager/Budget Manager/task/src/budget/purchases.txt");
//        File file = new File("purchases.txt");

        try (PrintWriter writer = new PrintWriter(file)) {

            writer.println("Balance: $" + balance);

            for (Food f : foodList
            ) {
                writer.println("Food: " + f.getName() + " $" + f.getPrice());
            }
            for (Clothes c : clothesList
            ) {
                writer.println("Clothes: " + c.getName() + " $" + c.getPrice());
            }
            for (Entertainment e : entertainmentList
            ) {
                writer.println("Entertainment: " + e.getName() + " $" + e.getPrice());
            }
            for (Other o : otherList
            ) {
                writer.println("Other: " + o.getName() + " $" + o.getPrice());
            }

            System.out.println("\nPurchases were saved!");
        } catch (IOException e) {
            System.out.printf("An exception occurred %s", e.getMessage());
        }

    }

    void addIncome(Scanner scanner) {
        System.out.println("\nEnter income:");
        double income = scanner.nextDouble();
        System.out.println("Income was added!");
        this.balance += income;
    }

    void addPurchase(Scanner scanner) {

        while (true) {
            System.out.println("\nChoose the type of purchase");
            System.out.println("1) Food");
            System.out.println("2) Clothes");
            System.out.println("3) Entertainment");
            System.out.println("4) Other");
            System.out.println("5) Back");
            int choose = scanner.nextInt();
            scanner.nextLine();

            switch (choose) {
                case 1 -> {
                    Map<String, Double> map = addTypeOfPurchase(scanner);
                    Map.Entry<String, Double> entry = map.entrySet().stream().findFirst().get();
                    String key = entry.getKey();
                    Double value = entry.getValue();
                    Food food = new Food(key, value);
                    foodList.add(food);
                    totalSumOfFood += food.getPrice();
                    purchaseSizeList++;
                }
                case 2 -> {
                    Map<String, Double> map = addTypeOfPurchase(scanner);
                    Map.Entry<String, Double> entry = map.entrySet().stream().findFirst().get();
                    String key = entry.getKey();
                    Double value = entry.getValue();
                    Clothes clothes = new Clothes(key, value);
                    clothesList.add(clothes);
                    totalSumOfClothes += clothes.getPrice();
                    purchaseSizeList++;
                }
                case 3 -> {
                    Map<String, Double> map = addTypeOfPurchase(scanner);
                    Map.Entry<String, Double> entry = map.entrySet().stream().findFirst().get();
                    String key = entry.getKey();
                    Double value = entry.getValue();
                    Entertainment entertainment = new Entertainment(key, value);
                    entertainmentList.add(entertainment);
                    totalSumOfEntertainment += entertainment.getPrice();
                    purchaseSizeList++;
                }
                case 4 -> {
                    Map<String, Double> map = addTypeOfPurchase(scanner);
                    Map.Entry<String, Double> entry = map.entrySet().stream().findFirst().get();
                    String key = entry.getKey();
                    Double value = entry.getValue();
                    Other other = new Other(key, value);
                    otherList.add(other);
                    totalSumOfOther += other.getPrice();
                    purchaseSizeList++;
                }
                case 5 -> {
                    return;
                }
            }
        }
    }

    Map<String, Double> addTypeOfPurchase(Scanner scanner) {
        Map<String, Double> map = new HashMap<>();

        System.out.println("\nEnter purchase name:");
        String nameOfPurchase = scanner.nextLine();
        System.out.println("Enter its price:");
        double priceOfPurchase = scanner.nextDouble();
        System.out.println("Purchase was added!");

        map.put(nameOfPurchase, priceOfPurchase);

        balance -= priceOfPurchase;

        return map;
    }

    private void showListOfPurchases(Scanner scanner) {

        if (purchaseSizeList == 0) {
            System.out.println("\nThe purchase list is empty!");
        }

        while (true) {
            System.out.println("\nChoose the type of purchase");
            System.out.println("1) Food");
            System.out.println("2) Clothes");
            System.out.println("3) Entertainment");
            System.out.println("4) Other");
            System.out.println("5) All");
            System.out.println("6) Back");

            int choose = scanner.nextInt();

            switch (choose) {
                case 1 -> {
                    System.out.println("\nFood:");
                    if (foodList.size() == 0) {
                        System.out.println("The purchase list is empty!");

                    } else {
                        foodList.forEach(System.out::println);
                    }
                    double totalSum = foodList.stream().mapToDouble(Food::getPrice).sum();
                    System.out.printf("Total sum: $%.2f%n", totalSum);
                }
                case 2 -> {
                    System.out.println("\nClothes:");
                    if (clothesList.size() == 0) {
                        System.out.println("The purchase list is empty!");

                    } else {
                        clothesList.forEach(System.out::println);
                    }
                    double totalSum = clothesList.stream().mapToDouble(Clothes::getPrice).sum();
                    System.out.printf("Total sum: $%.2f%n", totalSum);
                }
                case 3 -> {
                    System.out.println("\nEntertainment:");
                    if (entertainmentList.size() == 0) {
                        System.out.println("The purchase list is empty!");

                    } else {
                        entertainmentList.forEach(System.out::println);

                    }
                    double totalSum = entertainmentList.stream().mapToDouble(Entertainment::getPrice).sum();
                    System.out.printf("Total sum: $%.2f%n", totalSum);
                }
                case 4 -> {
                    System.out.println("\nOther:");
                    if (otherList.size() == 0) {
                        System.out.println("The purchase list is empty!");

                    } else {
                        otherList.forEach(System.out::println);
                    }
                    double totalSum = otherList.stream().mapToDouble(Other::getPrice).sum();
                    System.out.printf("Total sum: $%.2f%n", totalSum);
                }
                case 5 -> {
                    System.out.println("\nAll");
                    if (purchaseSizeList == 0) {
                        System.out.println("The purchase list is empty!");
                    } else {
                        foodList.forEach(System.out::println);
                        double foodSum = foodList.stream().mapToDouble(Food::getPrice).sum();

                        clothesList.forEach(System.out::println);
                        double clothesSum = clothesList.stream().mapToDouble(Clothes::getPrice).sum();

                        entertainmentList.forEach(System.out::println);
                        double enterSum = entertainmentList.stream().mapToDouble(Entertainment::getPrice).sum();
                        otherList.forEach(System.out::println);
                        double otherSum = otherList.stream().mapToDouble(Other::getPrice).sum();
                        double totalSum = foodSum + clothesSum + enterSum + otherSum;
                        System.out.printf("Total sum: $%.2f%n", totalSum);
                    }
                }
                case 6 -> {
                    return;
                }
            }
        }
    }

    private void showBalance() {
        System.out.println("\nBalance: $" + balance);
    }

    private void exitProgram() {
        System.out.println("\nBye!");
        System.exit(0);
    }
}
