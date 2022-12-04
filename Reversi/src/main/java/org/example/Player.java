package org.example;

/**
 * Класс игрока.
 */
public abstract class Player {
    /**
     * Счетчик игроков
     */
    static private Integer countPlayer = 0;
    /**
     * Имя игрока
     */
    private String name;
    /**
     * Счет игрока
     */
    private Integer score;

    private Integer maxScore = 0;

    /**
     * Общее кол-во побед
     */
    private Integer totalWins;

    /**
     * Цвет используемы для вывода;
     */
    public Colors color;

    /**
     * Игральная фишка игрока
     */
    private Character chip;

    /**
     * Порядковый номер игрока
     */
    public Integer order;

    public Player(String name) {
        this.name = name;
        score = 2;
        order = ++countPlayer;
        order = order % 2 + 1;
        if (order == 1) {
            chip = '@';
            color = Colors.ANSI_YELLOW;
        } else {
            chip = '$';
            color = Colors.ANSI_CYAN;
        }
        totalWins = 0;
    }

    public abstract Pair<Integer, Integer> makeMove(boolean again);

    /**
     * Получить строку вида: "имя игрока: очки игрока"
     * @return Строка информация об игроке
     */
    public String getPlayer() {
        return (name + ": " + score);
    }

    /**
     * Получить строку вида "имя игрока: всего побед"
     * @return строка информация об игроке
     */
    public String getTotal() {
        return (name + ": " + totalWins);
    }

    /**
     * Получить счет игрока
     * @return счет игрока
     */
    public Integer getScore() {
        return score;
    }

    /**
     * Сеттер для кол-во очков
     * @param value очки
     */
    public void setScore(int value) {
        if (score > maxScore) {
            maxScore = score;
        }
        score = value;
    }

    public String getMaxScore() {
        return (name + ": " + maxScore);
    }

    /**
     * Увеличивает общее кол-во побед на 1
     */
    public void incrementWin() {
        totalWins++;
    }

    /**
     * Получить имя иргрока
     * @return имя игрока
     */
    public String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    /**
     * Сеттер для фишек
     * @param c символь фишки
     */
    public void setChip(Character c) {
        chip = c;
    }

    public Character getChip() {
        return chip;
    }
}
