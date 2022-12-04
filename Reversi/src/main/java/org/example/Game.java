package org.example;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.Scanner;


/**
 * Класс игры
 * Отвечает за саму игру (ходы, смена хода, вывод хода)
 */
public final class Game {

    /**
     * Игровое поле
     */
    private final GameField field;

    /**
     * Игрок делающий ход
     */
    private Player currentPlayer;

    private final Character forFirstPlayer;
    private final Character forSecondPlayer;

    /**
     * Игрой ожидающий ход
     */
    private Player nextPlayer;

    /**
     * Конструктор игры
     * @param firstPlayer первый игрок
     * @param secondPlayer второй игрок
     * @param boardSize размер доски для игры
     */
    public Game(Player firstPlayer, Player secondPlayer, Integer boardSize, boolean isChangedChip) {
        field = new GameField(boardSize);
        currentPlayer = firstPlayer;
        nextPlayer = secondPlayer;

        if (isChangedChip) {
            var temp = nextPlayer.order;
            nextPlayer.order = currentPlayer.order;
            currentPlayer.order = temp;

            var temp1 = nextPlayer;
            nextPlayer = currentPlayer;
            currentPlayer = temp1;

            var temp2 = currentPlayer.color;
            currentPlayer.color = nextPlayer.color;
            nextPlayer.color = temp2;

            forFirstPlayer = currentPlayer.getChip();
            forSecondPlayer = nextPlayer.getChip();
        } else {
            forFirstPlayer = currentPlayer.getChip();
            forSecondPlayer = nextPlayer.getChip();
        }
    }

    /**
     * Запуск игры
     */
    public void startGame() {
        var hintsForCurrPlayer = getPossibleMoves(currentPlayer);
        while (!checkForEndGame()) {
            hintsForCurrPlayer = getPossibleMoves(currentPlayer);
            printPlayers();
            field.printField(hintsForCurrPlayer, forFirstPlayer, forSecondPlayer);
            System.out.println("Ход делает " + currentPlayer.color + currentPlayer.getName() + Colors.ANSI_RESET);
            if (printPossibleMoves(hintsForCurrPlayer)) {
                var currMove = currentPlayer.makeMove(false);
                while (!isMoveValid(currMove, hintsForCurrPlayer)) {
                    currMove = currentPlayer.makeMove(true);
                }
                field.playerMadeMove(currentPlayer, currMove);
                var scores = getScoreForPlayers(currentPlayer, nextPlayer);
                currentPlayer.setScore(scores.first());
                nextPlayer.setScore(scores.second());
            }
            swapPlayers();
            System.out.println("\n\n");
        }
        printPlayers();
        var scores = getScoreForPlayers(currentPlayer, nextPlayer);
        currentPlayer.setScore(scores.first());
        nextPlayer.setScore(scores.second());
        if (currentPlayer.getScore() > nextPlayer.getScore()) {
            currentPlayer.incrementWin();

            System.out.println("The winner is " + currentPlayer.color + currentPlayer.getName() + Colors.ANSI_RESET);
        } else {
            nextPlayer.incrementWin();
            System.out.println("The winner is " + nextPlayer.color + nextPlayer.getName() + Colors.ANSI_RESET);
        }
        field.printField(null, forFirstPlayer, forSecondPlayer);
    }

    /**
     * Проверка на конец игры
     * @return True - если игра закончена, False - если нет
     */
    private boolean checkForEndGame() {
        var countSpaces = 0;
        var board = GameField.getGameBoard();
        for (int i = 0; i < field.getBoardSize(); i++) {
            for (int j = 0; j < field.getBoardSize(); j++) {
                if (board[i][j] == 0) {
                    countSpaces++;
                }
            }
        }
        if (countSpaces == 0) {
            return true;
        }

        return getPossibleMoves(currentPlayer).isEmpty() && getPossibleMoves(nextPlayer).isEmpty();
    }

    /**
     * Проверка на верность хода
     *
     * @param move сделанный ход
     * @return true, если ход правильный, false иначе
     */
    private boolean isMoveValid(Pair<Integer, Integer> move, Set<Pair<Integer, Integer>> hints) {
        if (move == null) {
            return false;
        }
        for (var hint : hints) {
            if (Objects.equals(hint, move)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Получение возможных ходоы для игрока
     *
     * @param player игрок, которому нужно просмотреть возможне ходы
     * @return вектор пар - координат возможных ходов
     */
    public static Set<Pair<Integer, Integer>> getPossibleMoves(Player player) {
        var hints = new HashSet<Pair<Integer, Integer>>();
        var board = GameField.getGameBoard();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == 0) {
                    // вверх ^
                    int x = i - 1;
                    int countOtherPlayer = 0;
                    while (x >= 0) {
                        if (board[x][j] == 0) {
                            break;
                        } else if (Objects.equals(board[x][j], player.order) && countOtherPlayer == 0) {
                            break;
                        } else if (Objects.equals(board[x][j], player.order) && countOtherPlayer != 0) {
                            hints.add(new Pair<>(i, j));
                            break;
                        } else {
                            countOtherPlayer++;
                            x--;
                        }
                    }
                    // Вниз _
                    x = i + 1;
                    countOtherPlayer = 0;
                    while (x < board.length) {
                        if (board[x][j] == 0) {
                            break;
                        } else if (Objects.equals(board[x][j], player.order) && countOtherPlayer == 0) {
                            break;
                        } else if (Objects.equals(board[x][j], player.order) && countOtherPlayer != 0) {
                            hints.add(new Pair<>(i, j));
                            break;
                        } else {
                            countOtherPlayer++;
                            x++;
                        }
                    }
                    // Влео <-
                    x = j - 1;
                    countOtherPlayer = 0;
                    while (x >= 0) {
                        if (board[i][x] == 0) {
                            break;
                        } else if (Objects.equals(board[i][x], player.order) && countOtherPlayer == 0) {
                            break;
                        } else if (Objects.equals(board[i][x], player.order) && countOtherPlayer != 0) {
                            hints.add(new Pair<>(i, j));
                            break;
                        } else {
                            countOtherPlayer++;
                            x--;
                        }
                    }

                    // вправо –>
                    x = j + 1;
                    countOtherPlayer = 0;
                    while (x < board.length) {
                        if (board[i][x] == 0) {
                            break;
                        } else if (Objects.equals(board[i][x], player.order) && countOtherPlayer == 0) {
                            break;
                        } else if (Objects.equals(board[i][x], player.order) && countOtherPlayer != 0) {
                            hints.add(new Pair<>(i, j));
                            break;
                        } else {
                            countOtherPlayer++;
                            x++;
                        }
                    }
                    // По диаганали вправо вниз \
                    x = i + 1;
                    int y = j + 1;
                    countOtherPlayer = 0;
                    while (x < board.length && y < board.length) {
                        if (board[x][y] == 0) {
                            break;
                        } else if (Objects.equals(board[x][y], player.order) && countOtherPlayer == 0) {
                            break;
                        } else if (Objects.equals(board[x][y], player.order) && countOtherPlayer != 0) {
                            hints.add(new Pair<>(i, j));
                            break;
                        } else {
                            countOtherPlayer++;
                            x++;
                            y++;
                        }
                    }
                    // подиагонали влево вверх \
                    x = i - 1;
                    y = j - 1;
                    countOtherPlayer = 0;
                    while (x >= 0 && y >= 0) {
                        if (board[x][y] == 0) {
                            break;
                        } else if (Objects.equals(board[x][y], player.order) && countOtherPlayer == 0) {
                            break;
                        } else if (Objects.equals(board[x][y], player.order) && countOtherPlayer != 0) {
                            hints.add(new Pair<>(i, j));
                            break;
                        } else {
                            countOtherPlayer++;
                            x--;
                            y--;
                        }
                    }
                    // по диагонали влево вниз /
                    x = i + 1;
                    y = j - 1;
                    countOtherPlayer = 0;
                    while (x < board.length && y > 0) {
                        if (board[x][y] == 0) {
                            break;
                        } else if (Objects.equals(board[x][y], player.order) && countOtherPlayer == 0) {
                            break;
                        } else if (Objects.equals(board[x][y], player.order) && countOtherPlayer != 0) {
                            hints.add(new Pair<>(i, j));
                            break;
                        } else {
                            countOtherPlayer++;
                            x++;
                            y--;
                        }
                    }
                    // по диагонали вправо вверх /
                    x = i - 1;
                    y = j + 1;
                    countOtherPlayer = 0;
                    while (x >= 0 && y < board.length) {
                        if (board[x][y] == 0) {
                            break;
                        } else if (Objects.equals(board[x][y], player.order) && countOtherPlayer == 0) {
                            break;
                        } else if (Objects.equals(board[x][y], player.order) && countOtherPlayer != 0) {
                            hints.add(new Pair<>(i, j));
                            break;
                        } else {
                            countOtherPlayer++;
                            x--;
                            y++;
                        }
                    }
                }
            }
        }
        return hints;
    }

    /**
     * Подсчет очков игроков
     *
     * @param firstPlayer  первый игрок
     * @param secondPlayer второй игрок
     * @return (очки первого игрока ; очки второго игрока)
     */
    private Pair<Integer, Integer> getScoreForPlayers(Player firstPlayer, Player secondPlayer) {
        var board = GameField.getGameBoard();
        var first = 0;
        var second = 0;
        for (int i = 0; i < field.getBoardSize(); i++) {
            for (int j = 0; j < field.getBoardSize(); j++) {
                if (Objects.equals(board[i][j], firstPlayer.order)) {
                    first++;
                } else if (Objects.equals(board[i][j], secondPlayer.order)) {
                    second++;
                }
            }
        }

        return new Pair<Integer, Integer>(first, second);
    }

    /**
     * Смена хода
     */
    private void swapPlayers() {
        var temp = currentPlayer;
        currentPlayer = nextPlayer;
        nextPlayer = temp;
    }

    /**
     * Вывод возможных ходов для текущего игрока
     *
     * @param hints Возможные ходы для игрока
     */
    private boolean printPossibleMoves(Set<Pair<Integer, Integer>> hints) {
        if (checkForEndGame()) {
            return false;
        }
        if (hints.isEmpty()) {
            System.out.println("Нет возможных ходов(");
            return false;
        }
        System.out.print("Возможные ходы: ");
        for (var pair : hints) {
            System.out.print("(" + (pair.first() + 1) + "; " + (pair.second() + 1) + ") ");
        }
        System.out.println();
        return true;
    }

    /**
     * Вывод счета игроков
     */
    private void printPlayers() {
        if (currentPlayer.order == 1) {
            System.out.println("Scores - \t" + currentPlayer.color + currentPlayer.getPlayer() + Colors.ANSI_RESET + " | " + nextPlayer.color + nextPlayer.getPlayer() + Colors.ANSI_RESET);
        } else {
            System.out.println("Scores - \t" + nextPlayer.color + nextPlayer.getPlayer() + Colors.ANSI_RESET + " | " + currentPlayer.color + currentPlayer.getPlayer() + Colors.ANSI_RESET);
        }
    }

    /**
     * Вывод всего побед
     */
    public void printTotalPlayers() {
        if (currentPlayer.order == 1) {
            System.out.println("Total wins - \t" + currentPlayer.color + currentPlayer.getTotal() + Colors.ANSI_RESET + " | " + nextPlayer.color + nextPlayer.getTotal() + Colors.ANSI_RESET);
        } else {
            System.out.println("Total wins - \t" + nextPlayer.color + nextPlayer.getTotal() + Colors.ANSI_RESET + " | " + currentPlayer.color + currentPlayer.getTotal() + Colors.ANSI_RESET);
        }
    }

    /**
     * Вывод наилучшего счета
     */
    public void printBestScore() {
        if (currentPlayer.order == 1) {
            System.out.println("Best score - \t" + currentPlayer.color + currentPlayer.getMaxScore() + Colors.ANSI_RESET + " | " + nextPlayer.color + nextPlayer.getMaxScore() + Colors.ANSI_RESET);
        } else {
            System.out.println("Best score - \t" + nextPlayer.color + nextPlayer.getMaxScore() + Colors.ANSI_RESET + " | " + currentPlayer.color + currentPlayer.getMaxScore() + Colors.ANSI_RESET);
        }
    }
}
