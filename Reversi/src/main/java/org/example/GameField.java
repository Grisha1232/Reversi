package org.example;

import java.util.Set;


/**
 * Класс игрового поля
 */
final class GameField {
    /**
     * Размер игрового поля
     */
    private static Integer boardSize;

    /**
     * Игровое поле
     * 0 - пусто
     * 1 - первый игрок
     * 2 - второй игрок
     */
    private static Integer[][] board;

    public GameField(Integer size) {
        boardSize = size;
        board = new Integer[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = 0;
                if (i == boardSize / 2 - 1) {
                    if (j == boardSize / 2 - 1) {
                        board[i][j] = 1;
                    } else if (j == boardSize / 2) {
                        board[i][j] = 2;
                    }
                }
                if (i == boardSize / 2) {
                    if (j == boardSize / 2 - 1) {
                        board[i][j] = 2;
                    } else if (j == boardSize / 2) {
                        board[i][j] = 1;
                    }
                }
            }
        }
    }

    /**
     * Вывод игрового поля
     * @param hints Подсказки ходов
     */
    void printField(Set<Pair<Integer, Integer>> hints, Character forFirst, Character forSecond) {
        setHintsOnBoard(hints);
        for (int i = 0; i < 2 * boardSize + 2; i++) {
            if (i == 0) {
                System.out.print("\t");
                for (int j = 0; j < boardSize; j++) {
                    System.out.print("  " + (j + 1) + " ");
                }
                System.out.println();
                continue;
            }
            if (i % 2 == 1) {
                System.out.print("\t");
                for (int j = 0; j < boardSize; j++) {
                    System.out.print(" –––");
                }
            } else {
                System.out.print(" " + (i / 2) + "\t");
                System.out.print("|");
                for (int j = 0; j < boardSize; j++) {
                    System.out.print(" ");
                    if (board[i / 2 - 1][j] == 1) {
                        System.out.print(Colors.ANSI_YELLOW + forFirst.toString() + Colors.ANSI_RESET);
                    } else if (board[i / 2 - 1][j] == 2) {
                        System.out.print(Colors.ANSI_CYAN + forSecond.toString() + Colors.ANSI_RESET);
                    } else if (board[i / 2 - 1][j] == 3) {
                        System.out.print(Colors.ANSI_PURPLE + "?" + Colors.ANSI_RESET);
                    } else {
                        System.out.print(" ");
                    }
                    System.out.print(" |");
                }
            }
            System.out.println();
        }
        setBoardWithoutHints();
    }

    /**
     * Ход игрока
     * @param player игрок, который делает ход
     * @param move ход сделанный игроком
     */
    public void playerMadeMove(Player player, Pair<Integer, Integer> move) {
        setBoardWithoutHints();
        // Вверх
        if (checkUpFromMove(player, move)) {
            for (int i = move.first() - 1; i >= 0; i--) {
                if (board[i][move.second()].compareTo(player.order) == 0) {
                    break;
                }
                board[i][move.second()] = player.order;
            }
        }

        // Вниз
        if (checkDownFromMove(player, move)) {
            for (int i = move.first() + 1; i < boardSize; i++) {
                if (board[i][move.second()].compareTo(player.order) == 0) {
                    break;
                }
                board[i][move.second()] = player.order;
            }
        }

        // Влево
        if (checkLeftFromMove(player, move)) {
            for (int j = move.second() - 1; j >= 0; j--) {
                if (board[move.first()][j].compareTo(player.order) == 0) {
                    break;
                }
                board[move.first()][j] = player.order;
            }
        }

        // Вправо
        if (checkRightFromMove(player, move)) {
            for (int j = move.second() + 1; j < boardSize; j++) {
                if (board[move.first()][j].compareTo(player.order) == 0) {
                    break;
                }
                board[move.first()][j] = player.order;
            }
        }


        // Вниз по диагонале вправо
        if (checkRightDownFromMove(player, move)) {
            for (int i = move.first() + 1, j = move.second() + 1; i < boardSize && j < boardSize; i++, j++) {
                if (board[i][j].compareTo(player.order) == 0) {
                    break;
                }
                board[i][j] = player.order;
            }
        }

        // Вверх по диагонале влево
        if (checkLeftUpFromMove(player, move)) {
            for (int i = move.first() - 1, j = move.second() - 1; i >= 0 && j >= 0; i--, j--) {
                if (board[i][j].compareTo(player.order) == 0) {
                    break;
                }
                board[i][j] = player.order;
            }
        }

        // Вверх по диагонале вправо
        if (checkRightUpFromMove(player, move)) {
            for (int i = move.first() - 1, j = move.second() + 1; i >= 0 && j < boardSize; i--, j++) {
                if (board[i][j].compareTo(player.order) == 0) {
                    break;
                }
                board[i][j] = player.order;
            }
        }

        // Вниз по диагонале влево
        if (checkLeftDownFromMove(player, move)) {
            for (int i = move.first() + 1, j = move.second() - 1; i < boardSize && j >= 0; i++, j--) {
                if (board[i][j].compareTo(player.order) == 0) {
                    break;
                }
                board[i][j] = player.order;
            }
        }

        board[move.first()][move.second()] = player.order;
    }

    /**
     * Обнуляет подсказки выставленные на поле
     */
    private static void setBoardWithoutHints() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] == 3) {
                    board[i][j] = 0;
                }
            }
        }
    }

    /**
     * Выставляет подсказки игроку
     * @param hints подсказки игроку
     */
    private void setHintsOnBoard(Set<Pair<Integer, Integer>> hints) {
        if (hints == null) {
            return;
        }
        for (var hint : hints) {
            board[hint.first()][hint.second()] = 3;
        }
    }

    /**
     * Проверяет валидность хода (проходит вверх от хода)
     * @param player игрок делающий ход
     * @param move возможный ход
     * @return правильный ли ход true - если правильный, false - иначе
     */
    private boolean checkUpFromMove(Player player, Pair<Integer, Integer> move) {
        for (int i = move.first() - 1; i >= 0; i--) {
            if (board[i][move.second()].compareTo(0) == 0) {
                return false;
            } else if (board[i][move.second()].compareTo(player.order) == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Проверяет валидность хода (проходит вниз от хода)
     * @param player игрок делающий ход
     * @param move возможный ход
     * @return правильный ли ход true - если правильный, false - иначе
     */
    private boolean checkDownFromMove(Player player, Pair<Integer, Integer> move) {
        for (int i = move.first() + 1; i < boardSize; i++) {
            if (board[i][move.second()].compareTo(0) == 0) {
                return false;
            } else if (board[i][move.second()].compareTo(player.order) == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Проверяет валидность хода (проходит влево от хода)
     * @param player игрок делающий ход
     * @param move возможный ход
     * @return правильный ли ход true - если правильный, false - иначе
     */
    private boolean checkLeftFromMove(Player player, Pair<Integer, Integer> move) {
        for (int i = move.second() - 1; i >= 0; i--) {
            if (board[move.first()][i].compareTo(0) == 0) {
                return false;
            } else if (board[move.first()][i].compareTo(player.order) == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Проверяет валидность хода (проходит вправо от хода)
     * @param player игрок делающий ход
     * @param move возможный ход
     * @return правильный ли ход true - если правильный, false - иначе
     */
    private boolean checkRightFromMove(Player player, Pair<Integer, Integer> move) {
        for (int i = move.second() + 1; i < boardSize; i++) {
            if (board[move.first()][i].compareTo(0) == 0) {
                return false;
            } else if (board[move.first()][i].compareTo(player.order) == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Проверяет валидность хода (проходит вправо по диагонале вниз от хода)
     * @param player игрок делающий ход
     * @param move возможный ход
     * @return правильный ли ход true - если правильный, false - иначе
     */
    private boolean checkRightDownFromMove(Player player, Pair<Integer, Integer> move) {
        for (int i = move.first() + 1, j = move.second() + 1; i < boardSize && j < boardSize; i++, j++) {
            if (board[i][j].compareTo(0) == 0) {
                return false;
            } else if (board[i][j].compareTo(player.order) == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Проверяет валидность хода (проходит влево по диагонале вверх от хода)
     * @param player игрок делающий ход
     * @param move возможный ход
     * @return правильный ли ход true - если правильный, false - иначе
     */
    private boolean checkLeftUpFromMove(Player player, Pair<Integer, Integer> move) {
        for (int i = move.first() - 1, j = move.second() - 1; i >= 0 && j >= 0; i--, j--) {
            if (board[i][j].compareTo(0) == 0) {
                return false;
            } else if (board[i][j].compareTo(player.order) == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Проверяет валидность хода (проходит вправо по диагонале вверх от хода)
     * @param player игрок делающий ход
     * @param move возможный ход
     * @return правильный ли ход true - если правильный, false - иначе
     */
    private boolean checkRightUpFromMove(Player player, Pair<Integer, Integer> move) {
        for (int i = move.first() - 1, j = move.second() + 1; i >= 0 && j < boardSize; i--, j++) {
            if (board[i][j].compareTo(0) == 0) {
                return false;
            } else if (board[i][j].compareTo(player.order) == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Проверяет валидность хода (проходит влево по диагонале вниз от хода)
     * @param player игрок делающий ход
     * @param move возможный ход
     * @return правильный ли ход true - если правильный, false - иначе
     */
    private boolean checkLeftDownFromMove(Player player, Pair<Integer, Integer> move) {
        for (int i = move.first() + 1, j = move.second() - 1; i < boardSize && j >= 0; i++, j--) {
            if (board[i][j].compareTo(0) == 0) {
                return false;
            } else if (board[i][j].compareTo(player.order) == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Получение копии игрового поля
     * @return игровое поле
     */
    public static int[][] getGameBoard() {
        setBoardWithoutHints();
        int[][] boardClone = new int[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                boardClone[i][j] = board[i][j];
            }
        }
        return boardClone;
    }

    /**
     * Получение размера доски
     * @return размер доски
     */
    public Integer getBoardSize() {
        return boardSize;
    }
}
