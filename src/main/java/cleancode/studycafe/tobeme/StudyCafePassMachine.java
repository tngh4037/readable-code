package cleancode.studycafe.tobeme;

import cleancode.studycafe.tobeme.exception.AppException;
import cleancode.studycafe.tobeme.io.InputHandler;
import cleancode.studycafe.tobeme.io.OutputHandler;
import cleancode.studycafe.tobeme.define.StudyCafePassType;
import cleancode.studycafe.tobeme.io.StudyCafeTypeHandler;

public class StudyCafePassMachine {

    private final InputHandler inputHandler = new InputHandler();
    private final OutputHandler outputHandler = new OutputHandler();
    private final StudyCafeTypeHandler studyCafeTypeHandler = new StudyCafeTypeHandler();

    public void run() {
        try {
            outputHandler.showWelcomeMessage();
            outputHandler.showAnnouncement();
            outputHandler.askPassTypeSelection();

            StudyCafePassType studyCafePassType = inputHandler.getPassTypeSelectingUserAction();
            studyCafeTypeHandler.run(studyCafePassType);
        } catch (AppException e) {
            outputHandler.showSimpleMessage(e.getMessage());
        } catch (Exception e) {
            outputHandler.showSimpleMessage("알 수 없는 오류가 발생했습니다.");
        }
    }

}
