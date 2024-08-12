package cleancode.minesweeper.tobe.io;

import java.util.Scanner;

// 입력을 담당하는 책임
public class ConsoleInputHandler {

    public static final Scanner SCANNER = new Scanner(System.in);

    public String getUserInput() {
        return SCANNER.nextLine();
    }
}
