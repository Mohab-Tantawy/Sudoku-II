package com.company.Backend;

import java.util.Scanner;

public class ConsoleUI {
    private Application app;
    private Scanner scanner;
    private boolean running;

    public ConsoleUI() {
        this.app = new Application();
        this.scanner = new Scanner(System.in);
        this.running = true;
    }

    public void start() {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║        SUDOKU GAME CONSOLE UI        ║");
        System.out.println("╚══════════════════════════════════════╝");

        app.initialize();

        while (running) {
            printMenu();
            int choice = getIntInput("Choice: ", 1, 10);

            switch (choice) {
                case 1: showBoard(); break;
                case 2: makeMove(); break;
                case 3: app.undoMove(); break;
                case 4: app.redoMove(); break;
                case 5: app.validateCurrentGame(); break;
                case 6: app.solveGame(); break;
                case 7: app.checkSolveButtonStatus(); break;
                case 8: saveGame(); break;
                case 9: app.showBoard(); break;
                case 10: exit(); break;
            }
        }
        scanner.close();
    }

    private void printMenu() {
        System.out.println("\n╔════════════════ MENU ════════════════╗");
        System.out.println("║ 1. Show Board                        ║");
        System.out.println("║ 2. Make Move                         ║");
        System.out.println("║ 3. Undo                              ║");
        System.out.println("║ 4. Redo                              ║");
        System.out.println("║ 5. Validate Board                    ║");
        System.out.println("║ 6. Solve (if 5 empty cells)          ║");
        System.out.println("║ 7. Check Solve Button Status         ║");
        System.out.println("║ 8. Save Game to Difficulty Folder    ║");
        System.out.println("║ 9. Show Game Info                    ║");
        System.out.println("║ 10. Exit                             ║");
        System.out.println("╚══════════════════════════════════════╝");
    }

    private void showBoard() {
        app.showBoard();
    }

    private void makeMove() {
        System.out.println("\n=== Make a Move ===");
        int x = getIntInput("Row (0-8): ", 0, 8);
        int y = getIntInput("Column (0-8): ", 0, 8);
        int value = getIntInput("Value (1-9): ", 1, 9);

        app.makeMove(x, y, value);
    }

    private void saveGame() {
        System.out.println("\n=== Save Game ===");
        System.out.println("1. Save to Easy folder");
        System.out.println("2. Save to Medium folder");
        System.out.println("3. Save to Hard folder");
        int choice = getIntInput("Choice: ", 1, 3);

        // In real implementation, this would save with different difficulty
        app.saveGameToDifficulty();
    }

    private void exit() {
        System.out.println("\nExiting Sudoku Game...");
        System.out.println("Goodbye!");
        running = false;
    }

    private int getIntInput(String prompt, int min, int max) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine());
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println("❌ Please enter a number between " + min + " and " + max);
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input. Please enter a number.");
            }
        }
    }

    public static void main(String[] args) {
        ConsoleUI ui = new ConsoleUI();
        ui.start();
    }
}