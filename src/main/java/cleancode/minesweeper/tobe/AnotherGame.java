package cleancode.minesweeper.tobe;

import cleancode.minesweeper.tobe.game.GameRunnable;

// initialize 작업이 필요없는 게임
public class AnotherGame implements GameRunnable {

    // @Override
    // public void initialize() {
    //     // ... 필요없는데.. ? 빈 형태로 놔두자 -> ISP 위반
    // }

    @Override
    public void run() {
        // 게임 진행
    }
}
