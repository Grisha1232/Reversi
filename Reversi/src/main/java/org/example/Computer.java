package org.example;

import java.util.*;


public class Computer extends Player {

    /**
     * Для рандомизации строки имени
     */
    private static String[] randomNames = {"Computer", "MacOS", "Windows 11", "High intelligence"};
    private static final Random rand = new Random();

    /**
     * Сложность бота
     * 1 - Легкий
     * 2 - Сложный
     */
    private Integer level;

    /**
     * Противник компьютера
     */
    private Integer opponent;

    public void setOpponent(Integer opponent) {
        this.opponent = opponent;
    }

    private Computer(String name) {
        super(name);
    }

    /**
     * Сделать ход
     *
     * @param again повторный ход? (после не правильного ввода)
     * @return пару целочисленных значений - координаты клетки куда сходил бот
     */
    @Override
    public Pair<Integer, Integer> makeMove(boolean again) {
        var moves = getPossibleMoves(GameField.getGameBoard());
        var move = getBestOfPossibleMove(moves, level);
        System.out.print(color + getName() + Colors.ANSI_RESET + " делает ход: ");
        System.out.println("(" + (move.first() + 1) + "; " + (move.second() + 1) + ")");
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            System.out.println();
        }
        return move;
    }

    public Computer(int levelBot, Integer opponent) {
        this("randomNames[rand.nextInt(randomNames.length)]");
        if (randomNames.length > 2) {
            int index = rand.nextInt(0, randomNames.length);
            setName(randomNames[index]);
            var temp = randomNames.clone();
            randomNames = new String[temp.length - 1];
            for (int i = 0; i < randomNames.length; i++) {
                if (i < index) {
                    randomNames[i] = temp[i];
                } else {
                    randomNames[i] = temp[i + 1];
                }
            }
        }
        this.level = levelBot;
        this.opponent = opponent;
    }

    /**
     * Получить все возможные ходы для бота
     *
     * @param board доска, на которой играют
     * @return не повторяющиеся возможные ходы
     */
    private Set<Pair<Integer, Integer>> getPossibleMoves(int[][] board) {
        var hints = new HashSet<Pair<Integer, Integer>>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == 0) {
                    // вверх ^
                    int x = i - 1;
                    int countOtherPlayer = 0;
                    while (x >= 0) {
                        if (board[x][j] == 0) {
                            break;
                        } else if (board[x][j] == order && countOtherPlayer == 0) {
                            break;
                        } else if (board[x][j] == order && countOtherPlayer != 0) {
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
                        } else if (board[x][j] == order && countOtherPlayer == 0) {
                            break;
                        } else if (board[x][j] == order && countOtherPlayer != 0) {
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
                        } else if (board[i][x] == order && countOtherPlayer == 0) {
                            break;
                        } else if (board[i][x] == order && countOtherPlayer != 0) {
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
                        } else if (board[i][x] == order && countOtherPlayer == 0) {
                            break;
                        } else if (board[i][x] == order && countOtherPlayer != 0) {
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
                        } else if (board[x][y] == order && countOtherPlayer == 0) {
                            break;
                        } else if (board[x][y] == order && countOtherPlayer != 0) {
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
                        } else if (board[x][y] == order && countOtherPlayer == 0) {
                            break;
                        } else if (board[x][y] == order && countOtherPlayer != 0) {
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
                        } else if (board[x][y] == order && countOtherPlayer == 0) {
                            break;
                        } else if (board[x][y] == order && countOtherPlayer != 0) {
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
                        } else if (board[x][y] == order && countOtherPlayer == 0) {
                            break;
                        } else if (board[x][y] == order && countOtherPlayer != 0) {
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
     * Получить лучший возможный ход в зависимости от сложности бота
     *
     * @param moves Возможные ходы
     * @param level Сложность бота
     * @return Лучший ход
     */
    private Pair<Integer, Integer> getBestOfPossibleMove(Set<Pair<Integer, Integer>> moves, Integer level) {
        var board = GameField.getGameBoard();
        Integer[][] boardClone = new Integer[board.length][board.length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                boardClone[i][j] = board[i][j];
            }
        }
        Pair<Integer, Integer> result = moves.iterator().next();
        var m = moves.iterator();
        for (int i = 0; i < rand.nextInt(0, moves.size()); i++) {
            result = m.next();
        }
        double max = -1000.0;
        if (level == 1) {
            for (var move : moves) {
                var value = valueFunc(boardClone, move, false);
                if (value > max) {
                    max = value;
                    result = move;
                }
            }
        } else {
            var probablyOpponent = new Computer(1, order);
            probablyOpponent.order = opponent;
            for (var move : moves) {
                var copyBoard = GameField.getGameBoard();
                Integer[][] copyBoardClone = new Integer[copyBoard.length][copyBoard.length];
                for (int i = 0; i < board.length; i++) {
                    for (int j = 0; j < board.length; j++) {
                         copyBoardClone[i][j] = copyBoard[i][j];
                    }
                }
                var value = valueFunc(copyBoardClone, move, true);
                double maxForOpp = 0;
                for (var moveOpp : probablyOpponent.getPossibleMoves(copyBoard)) {
                    var valueOpp = valueFunc(copyBoardClone, moveOpp, false);
                    if (valueOpp > maxForOpp) {
                        maxForOpp = valueOpp;
                    }
                }
                if (value - maxForOpp > max) {
                    max = value - maxForOpp;
                    result = move;
                }
            }
        }
        return result;

    }


    /**
     * Оценочная функция
     *
     * @param board    доска, на которой играют
     * @param move     ход, который делают
     * @param withFill определяет заполнять ли доску или нет (для просчета следующего хода противника)?
     * @return Оценочное число
     */
    private double valueFunc(Integer[][] board, Pair<Integer, Integer> move, boolean withFill) {
        double result = 0.0;
        var alreadyCount = new HashSet<Pair<Integer, Integer>>();

        proceedCount(board, alreadyCount, move, withFill);

        if ((move.first() == 0 && move.second() == 0) || (move.first() == 0 && move.second() == board.length - 1) || (move.first() == board.length && move.second() == 0) || (move.first() == board.length - 1 && move.second() == board.length - 1)) {
            result += 0.8;
        } else if (move.first() == 0 || move.second() == 0 || move.first() == board.length - 1 || move.second() == board.length) {
            result += 0.4;
        }

        for (var chip : alreadyCount) {
            if (chip.first() == 0 || chip.second() == 0 || chip.first() == board.length - 1 || chip.second() == board.length - 1) {
                result += 2;
            } else {
                result += 1;
            }
        }

        return result;
    }

    /**
     * Функция выполняющие все нижнии функции (для подсчета взятых фишек после хода)
     *
     * @param board     доска, на которой играют
     * @param cells     клетки, на которые уже сходили
     * @param move      ход, который делается
     * @param fillBoard определяет заполнять ли доску или нет (для просчета следующего хода противника)?
     */
    private void proceedCount(Integer[][] board, Set<Pair<Integer, Integer>> cells, Pair<Integer, Integer> move, boolean fillBoard) {
        countUp(board, cells, move, fillBoard);
        countDown(board, cells, move, fillBoard);

        countLeft(board, cells, move, fillBoard);
        countRight(board, cells, move, fillBoard);

        countRightDown(board, cells, move, fillBoard);
        countLeftUp(board, cells, move, fillBoard);

        countRightUp(board, cells, move, fillBoard);
        countLeftDown(board, cells, move, fillBoard);
    }

    private void countUp(Integer[][] board, Set<Pair<Integer, Integer>> cells, Pair<Integer, Integer> move, boolean fillBoard) {
        boolean thatGood = false;
        int countBetween = 0;
        for (int i = move.first() - 1; i >= 0; i--) {
            if (board[i][move.second()] == 0) {
                return;
            } else if (Objects.equals(board[i][move.second()], order) && countBetween != 0) {
                thatGood = true;
                break;
            } else {
                countBetween++;
            }
        }
        if (thatGood) {
            for (int i = move.first() - 1; i >= 0; i--) {
                if (Objects.equals(board[i][move.second()], opponent)) {
                    cells.add(new Pair<>(i, move.second()));
                    if (fillBoard) {
                        board[i][move.second()] = order;
                    }
                } else {
                    if (fillBoard) {
                        board[move.first()][move.second()] = order;
                    }
                    return;
                }
            }
        }
    }

    private void countDown(Integer[][] board, Set<Pair<Integer, Integer>> cells, Pair<Integer, Integer> move, boolean fillBoard) {
        boolean thatGood = false;
        int countBetween = 0;
        for (int i = move.first() + 1; i < board.length; i++) {
            if (board[i][move.second()] == 0) {
                return;
            } else if (Objects.equals(board[i][move.second()], order) && countBetween != 0) {
                thatGood = true;
                break;
            } else {
                countBetween++;
            }
        }
        if (thatGood) {
            for (int i = move.first() + 1; i < board.length; i++) {
                if (Objects.equals(board[i][move.second()], opponent)) {
                    cells.add(new Pair<>(i, move.second()));
                    if (fillBoard) {
                        board[i][move.second()] = order;
                    }
                } else {
                    if (fillBoard) {
                        board[move.first()][move.second()] = order;
                    }
                    return;
                }
            }
        }
    }

    private void countLeft(Integer[][] board, Set<Pair<Integer, Integer>> cells, Pair<Integer, Integer> move, boolean fillBoard) {
        boolean thatGood = false;
        int countBetween = 0;
        for (int j = move.second() - 1; j >= 0; j--) {
            if (board[move.first()][j] == 0) {
                return;
            } else if (Objects.equals(board[move.first()][j], order) && countBetween != 0) {
                thatGood = true;
                break;
            } else {
                countBetween++;
            }
        }
        if (thatGood) {
            for (int j = move.second() - 1; j >= 0; j--) {
                if (Objects.equals(board[move.first()][j], opponent)) {
                    cells.add(new Pair<>(move.first(), j));
                    if (fillBoard) {
                        board[move.first()][j] = order;
                    }
                } else {
                    if (fillBoard) {
                        board[move.first()][move.second()] = order;
                    }
                    return;
                }
            }
        }
    }

    private void countRight(Integer[][] board, Set<Pair<Integer, Integer>> cells, Pair<Integer, Integer> move, boolean fillBoard) {
        boolean thatGood = false;
        int countBetween = 0;
        for (int j = move.second() + 1; j < board.length; j++) {
            if (board[move.first()][j] == 0) {
                return;
            } else if (Objects.equals(board[move.first()][j], order) && countBetween != 0) {
                thatGood = true;
                break;
            } else {
                countBetween++;
            }
        }
        if (thatGood) {
            for (int j = move.second() + 1; j < board.length; j++) {
                if (Objects.equals(board[move.first()][j], opponent)) {
                    cells.add(new Pair<>(move.first(), j));
                    if (fillBoard) {
                        board[move.first()][j] = order;
                    }
                } else {
                    if (fillBoard) {
                        board[move.first()][move.second()] = order;
                    }
                    return;
                }
            }
        }
    }


    private void countRightDown(Integer[][] board, Set<Pair<Integer, Integer>> cells, Pair<Integer, Integer> move, boolean fillBoard) {
        boolean thatGood = false;
        int countBetween = 0;
        for (int i = move.first() + 1, j = move.second() + 1; i < board.length && j < board.length; i++, j++) {
            if (board[i][j] == 0) {
                return;
            } else if (Objects.equals(board[i][j], order) && countBetween != 0) {
                thatGood = true;
                break;
            } else {
                countBetween++;
            }
        }
        if (thatGood) {
            for (int i = move.first() + 1, j = move.second() + 1; i < board.length && j < board.length; i++, j++) {
                if (Objects.equals(board[i][j], opponent)) {
                    cells.add(new Pair<>(i, j));
                    if (fillBoard) {
                        board[i][j] = order;
                    }
                } else {
                    if (fillBoard) {
                        board[move.first()][move.second()] = order;
                    }
                    return;
                }
            }
        }
    }

    private void countLeftUp(Integer[][] board, Set<Pair<Integer, Integer>> cells, Pair<Integer, Integer> move, boolean fillBoard) {
        boolean thatGood = false;
        int countBetween = 0;
        for (int i = move.first() - 1, j = move.second() - 1; i >= 0 && j >= 0; i--, j--) {
            if (board[i][j] == 0) {
                return;
            } else if (Objects.equals(board[i][j], order) && countBetween != 0) {
                thatGood = true;
                break;
            } else {
                countBetween++;
            }
        }
        if (thatGood) {
            for (int i = move.first() - 1, j = move.second() - 1; i >= 0 && j >= 0; i--, j--) {
                if (Objects.equals(board[i][j], opponent)) {
                    cells.add(new Pair<>(i, j));
                    if (fillBoard) {
                        board[i][j] = order;
                    }
                } else {
                    if (fillBoard) {
                        board[move.first()][move.second()] = order;
                    }
                    return;
                }
            }
        }
    }

    private void countRightUp(Integer[][] board, Set<Pair<Integer, Integer>> cells, Pair<Integer, Integer> move, boolean fillBoard) {
        boolean thatGood = false;
        int countBetween = 0;
        for (int i = move.first() - 1, j = move.second() + 1; i >= 0 && j < board.length; i--, j++) {
            if (board[i][j] == 0) {
                return;
            } else if (Objects.equals(board[i][j], order) && countBetween != 0) {
                thatGood = true;
                break;
            } else {
                countBetween++;
            }
        }
        if (thatGood) {
            for (int i = move.first() - 1, j = move.second() + 1; i >= 0 && j < board.length; i--, j++) {
                if (Objects.equals(board[i][j], opponent)) {
                    cells.add(new Pair<>(i, j));
                    if (fillBoard) {
                        board[i][j] = order;
                    }
                } else {
                    if (fillBoard) {
                        board[move.first()][move.second()] = order;
                    }
                    return;
                }
            }
        }
    }

    private void countLeftDown(Integer[][] board, Set<Pair<Integer, Integer>> cells, Pair<Integer, Integer> move, boolean fillBoard) {
        boolean thatGood = false;
        int countBetween = 0;
        for (int i = move.first() + 1, j = move.second() - 1; i < board.length && j >= 0; i++, j--) {
            if (board[i][j] == 0) {
                return;
            } else if (Objects.equals(board[i][j], order) && countBetween != 0) {
                thatGood = true;
                break;
            } else {
                countBetween++;
            }
        }
        if (thatGood) {
            for (int i = move.first() + 1, j = move.second() - 1; i < board.length && j >= 0; i++, j--) {
                if (Objects.equals(board[i][j], opponent)) {
                    cells.add(new Pair<>(i, j));
                    if (fillBoard) {
                        board[i][j] = order;
                    }
                } else {
                    if (fillBoard) {
                        board[move.first()][move.second()] = order;
                    }
                    return;
                }
            }
        }
    }


    @Override
    public String getPlayer() {
        return (getName() + " (level bot = " + (level == 1 ? "Easy" : "Hard") + ")" + ": " + getScore());
    }
}
