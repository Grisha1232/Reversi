package org.example;

import java.util.HashSet;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        var in = new Scanner(System.in);
        System.out.println("Если хотите играть с другом на одном компьютере, введите - 1");
        System.out.println("Если хотите играть с компьютером, введите - 2");
        System.out.println("Если хотите посмотреть как сыграют два компьютера, введите все что удгодно");
        var enter = in.nextLine();
        Player firstPlayer = null, secondPlayer = null;
        String continueGame;
        if (Objects.equals(enter, "1")) {
            System.out.print("Введите никнейм первого игрока: ");
            var name = in.nextLine();
            firstPlayer = new Human(name);
            System.out.print("Введите никнейм первого игрока: ");
            name = in.nextLine();
            secondPlayer = new Human(name);
        } else if (Objects.equals(enter, "2")){
            System.out.print("Введите ваш никнейм: ");
            var name = in.nextLine();
            firstPlayer = new Human(name);
            System.out.println("Выберите сложность бота: ");
            System.out.println("Новичок - 1");
            System.out.println("Профессионал - 2");
            System.out.print("Введите сложность (1 / 2): ");
            var level = in.nextInt();
            secondPlayer = new Computer(level, firstPlayer.order);

        } else {
            firstPlayer = new Computer(1, 2);
            secondPlayer = new Computer(2, firstPlayer.order);
            Computer p = (Computer) firstPlayer;
            p.setOpponent(secondPlayer.order);
            p = (Computer) secondPlayer;
            p.setOpponent(firstPlayer.order);
        }
        boolean swapedChips = false;
        do {

            firstPlayer.setScore(2);
            secondPlayer.setScore(2);
            Game game = new Game(firstPlayer, secondPlayer, 8, swapedChips);
            game.startGame();
            game.printTotalPlayers();
            game.printBestScore();
            System.out.print("Вы хотите продолжить играть? (y / n): ");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            continueGame = in.nextLine();
            while (!Objects.equals(continueGame, "y") && !Objects.equals(continueGame, "n")) {
                System.out.print("Не правильный ввод повторите (y / n): ");
                continueGame = in.nextLine();
            }
            swapedChips = !swapedChips;
        } while (Objects.equals(continueGame, "y"));
    }
}