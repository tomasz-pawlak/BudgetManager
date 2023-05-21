package budget;


import java.util.*;

public class Main {


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Budget budget = new Budget();

        while (true) {
            budget.chooseAction(scanner);
        }

    }
}
