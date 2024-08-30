package cleancode.minesweeper.tobe;

import cleancode.minesweeper.tobe.config.GameConfig;
import cleancode.minesweeper.tobe.gamelevel.Beginner;
import cleancode.minesweeper.tobe.io.ConsoleInputHandler;
import cleancode.minesweeper.tobe.io.ConsoleOutputHandler;

// 게임을 실행하는 책임
public class GameApplication {

    public static void main(String[] args) {
        GameConfig gameConfig = new GameConfig(
                new Beginner(),
                new ConsoleInputHandler(),
                new ConsoleOutputHandler()
        );

        Minesweeper minesweeper = new Minesweeper(gameConfig);
        minesweeper.initialize();
        minesweeper.run();
    }
}

/**
 * DIP (Dependency Inversion Principle)
 * - 고수준 모듈과 저수준 모듈이 서로 직접적으로 의존하지 않고, 추상화에 의존해야 한다. ( 인터페이스, 추상클래스 등 )
 *
 * DI (Dependency Injection)
 * - 필요한 의존성을 내가 직접 생성하는 것이 아니라, 외부에서 주입받겠다는 의미.
 *
 * IoC (Inversion of Control)
 * - 프로그램의 흐름을 개발자가 아닌, 프레임워크가 담당하도록 하는 것.
 */
