package com.kamilpomietlo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Battleship {

    private final char[][] gameGridPlayer1 = new char[10][10];
    private final char[][] foggedGameGridPlayer1 = new char[10][10];
    private final char[][] gameGridPlayer2 = new char[10][10];
    private final char[][] foggedGameGridPlayer2 = new char[10][10];
    private final List<Ship> ships = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);
    private int hitCountPlayer1 = 0;
    private int hitCountPlayer2 = 0;

    public void start() {
        fillInitialGameGrid(gameGridPlayer1, foggedGameGridPlayer1);
        fillInitialGameGrid(gameGridPlayer2, foggedGameGridPlayer2);
        createShips();

        System.out.println("Player 1, place your ships on the game field\n");
        printGameGrid(gameGridPlayer1);
        startPlacingShips(gameGridPlayer1);
        System.out.print("\n\nPress Enter and pass the move to another player\n...");
        scanner.nextLine();

        System.out.println("Player 2, place your ships on the game field\n");
        printGameGrid(gameGridPlayer2);
        startPlacingShips(gameGridPlayer2);
        System.out.print("\n\nPress Enter and pass the move to another player\n...");
        scanner.nextLine();

        System.out.println("The game starts!\n");
        startShootingRound();
    }

    private void startPlacingShips(char[][] gameGrid) {
        for (Ship ship : ships) {
            String input;
            System.out.println("\n\nEnter the coordinates of the " + ship.getName()
                    + " (" + ship.getLength() + " cells):");

            while (true) {
                System.out.println();
                System.out.print("> ");
                input = scanner.nextLine().trim().toUpperCase();
                System.out.println();

                if (placeShip(gameGrid, input, ship)) {
                    printGameGrid(gameGrid);
                    break;
                }
            }
        }
    }

    private boolean placeShip(char[][] gameGrid, String input, Ship ship) {
        String[] coordinates = input.split(" ");
        int firstCoordRow = coordinates[0].charAt(0) - 'A';
        int secondCoordRow = coordinates[1].charAt(0) - 'A';
        int firstCoordColumn = Integer.parseInt(coordinates[0].substring(1));
        int secondCoordColumn = Integer.parseInt(coordinates[1].substring(1));

        if (firstCoordRow != secondCoordRow && firstCoordColumn != secondCoordColumn) {
            System.out.println("Error! Wrong ship location! Try again:");
        } else if (firstCoordRow == secondCoordRow) {
            if (Math.abs(firstCoordColumn - secondCoordColumn) + 1 == ship.getLength()) {
                if (isPlaceFreeForHorizontalShip(gameGrid, firstCoordRow, firstCoordColumn, secondCoordColumn)) {
                    placeShipHorizontally(gameGrid, firstCoordRow, firstCoordColumn, secondCoordColumn);
                    return true;
                }
            } else {
                System.out.println("Error! Wrong length of the " + ship.getName() + "! Try again:");
            }
        } else {
            if (Math.abs(firstCoordRow - secondCoordRow) + 1 == ship.getLength()) {
                if (isPlaceFreeForVerticalShip(gameGrid, firstCoordColumn, firstCoordRow, secondCoordRow)) {
                    placeShipVertically(gameGrid, firstCoordColumn, firstCoordRow, secondCoordRow);
                    return true;
                }
            } else {
                System.out.println("Error! Wrong length of the " + ship.getName() + "! Try again:");
            }
        }
        return false;
    }

    private void placeShipHorizontally(char[][] gameGrid, int row, int firstCoord, int lastCoord) {
        if (firstCoord < lastCoord) {
            for (int i = firstCoord; i <= lastCoord; i++) {
                gameGrid[row][i - 1] = 'O';
            }
        } else {
            for (int i = firstCoord; i >= lastCoord; i--) {
                gameGrid[row][i - 1] = 'O';
            }
        }
    }

    private void placeShipVertically(char[][] gameGrid, int column, int firstCoord, int lastCoord) {
        if (firstCoord < lastCoord) {
            for (int i = firstCoord; i <= lastCoord; i++) {
                gameGrid[i][column - 1] = 'O';
            }
        } else {
            for (int i = firstCoord; i >= lastCoord; i--) {
                gameGrid[i][column - 1] = 'O';
            }
        }
    }

    private boolean isPlaceFreeForHorizontalShip(char[][] gameGrid, int row, int firstCoord, int lastCoord) {
        if (firstCoord < lastCoord) {
            for (int i = row - 1; i <= row + 1; i++) {
                for (int j = firstCoord - 2; j <= lastCoord; j++) {
                    try {
                        if (gameGrid[i][j] != '~') {
                            System.out.println("Error! You placed it too close to another one. Try again:");
                            return false;
                        }
                    } catch (ArrayIndexOutOfBoundsException ignored) {
                    }
                }
            }
        } else {
            for (int i = row - 1; i <= row + 1; i++) {
                for (int j = firstCoord; j >= lastCoord - 2; j--) {
                    try {
                        if (gameGrid[i][j] != '~') {
                            System.out.println("Error! You placed it too close to another one. Try again:");
                            return false;
                        }
                    } catch (ArrayIndexOutOfBoundsException ignored) {
                    }
                }
            }
        }
        return true;
    }

    private boolean isPlaceFreeForVerticalShip(char[][] gameGrid, int column, int firstCoord, int lastCoord) {
        if (firstCoord < lastCoord) {
            for (int i = firstCoord - 1; i <= lastCoord + 1; i++) {
                for (int j = column - 2; j <= column; j++) {
                    try {
                        if (gameGrid[i][j] != '~') {
                            System.out.println("Error! You placed it too close to another one. Try again:");
                            return false;
                        }
                    } catch (ArrayIndexOutOfBoundsException ignored) {
                    }
                }
            }
        } else {
            for (int i = firstCoord + 1; i >= lastCoord - 1; i--) {
                for (int j = column - 2; j <= column; j++) {
                    try {
                        if (gameGrid[i][j] != '~') {
                            System.out.println("Error! You placed it too close to another one. Try again:");
                            return false;
                        }
                    } catch (ArrayIndexOutOfBoundsException ignored) {
                    }
                }
            }
        }
        return true;
    }

    private void startShootingRound() {
        printGameGrid(foggedGameGridPlayer2);
        System.out.println("\n---------------------");
        printGameGrid(gameGridPlayer1);
        System.out.println("\n\nPlayer 1, it's your turn:\n");
        hitCountPlayer1 = shoot(gameGridPlayer2, foggedGameGridPlayer2, hitCountPlayer1);

        if (hitCountPlayer1 >= 17) {
            System.out.println("\nYou sank the last ship. Player 1 won. Congratulations!");
        } else {
            System.out.println("Press Enter and pass the move to another player\n...");
            scanner.nextLine();
            scanner.nextLine();
            printGameGrid(foggedGameGridPlayer1);
            System.out.println("\n---------------------");
            printGameGrid(gameGridPlayer2);
            System.out.println("\n\nPlayer 2, it's your turn:\n");
            hitCountPlayer2 = shoot(gameGridPlayer1, foggedGameGridPlayer1, hitCountPlayer2);

            if (hitCountPlayer2 >= 17) {
                System.out.println("\nYou sank the last ship. Player 2 won. Congratulations!");
            } else {
                System.out.println("Press Enter and pass the move to another player\n...");
                scanner.nextLine();
                scanner.nextLine();
                startShootingRound();
            }
        }
    }

    private int shoot(char[][] gameGrid, char[][] foggedGameGrid, int hitCount) {
        System.out.print("> ");
        String target = scanner.next().trim().toUpperCase();
        System.out.println();
        int row = target.charAt(0) - 'A';
        int column = Integer.parseInt(target.substring(1));

        try {
            if (gameGrid[row][column - 1] == 'O') {
                gameGrid[row][column - 1] = 'X';
                foggedGameGrid[row][column - 1] = 'X';
                hitCount++;
                System.out.println("You hit a ship!\n");
            } else if (gameGrid[row][column - 1] == '~') {
                gameGrid[row][column - 1] = 'M';
                foggedGameGrid[row][column - 1] = 'M';
                System.out.println("You missed!\n");
            } else {
                System.out.println("You hit a ship!\n");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            shoot(gameGrid, foggedGameGrid, hitCount);
        }

        return hitCount;
    }

    private void fillInitialGameGrid(char[][] gameGrid, char[][] foggedGameGrid) {
        for (char[] chars : gameGrid) {
            Arrays.fill(chars, '~');
        }
        for (char[] chars : foggedGameGrid) {
            Arrays.fill(chars, '~');
        }
    }

    private void createShips() {
        ships.add(new Ship("Aircraft Carrier", 5));
        ships.add(new Ship("Battleship", 4));
        ships.add(new Ship("Submarine", 3));
        ships.add(new Ship("Cruiser", 3));
        ships.add(new Ship("Destroyer", 2));
    }

    private void printGameGrid(char[][] gameGrid) {
        char rowIndex = 'A';

        System.out.print(" ");
        for (int i = 1; i <= 10; i++) {
            System.out.print(" " + i);
        }
        for (char[] chars : gameGrid) {
            System.out.println();
            System.out.print(rowIndex);
            for (int j = 0; j < gameGrid.length; j++) {
                System.out.print(" " + chars[j]);
            }
            rowIndex++;
        }
    }
}
