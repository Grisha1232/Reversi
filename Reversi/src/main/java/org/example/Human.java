package org.example;

import java.util.Scanner;


public final class Human extends Player{

    public Human(String name) {
        super(name);
    }

    @Override
    public Pair<Integer, Integer> makeMove(boolean again) {
        var in = new Scanner(System.in);
        if (!again) {
            System.out.print("Введите ваш ход: ");
            Pair<Integer, Integer> currMove;
            try {
                currMove = new Pair<>(in.nextInt() - 1, in.nextInt() - 1);
            } catch (Exception e) {
                return null;
            }
            return currMove;
        } else {
            System.out.print("Ход невалидный, повторите ввод: ");
            Pair<Integer, Integer> currMove;
            try {
                currMove = new Pair<>(in.nextInt() - 1, in.nextInt() - 1);
            } catch (Exception e) {
                return null;
            }
            return currMove;
        }
    }

}
