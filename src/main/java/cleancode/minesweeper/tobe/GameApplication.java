package cleancode.minesweeper.tobe;

// 게임을 실행하는 책임
public class GameApplication {

    public static void main(String[] args) {
        Minesweeper minesweeper = new Minesweeper();
        minesweeper.run();
    }
}
