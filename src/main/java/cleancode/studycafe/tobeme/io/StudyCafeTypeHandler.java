package cleancode.studycafe.tobeme.io;

import cleancode.studycafe.tobeme.define.StudyCafePassType;
import cleancode.studycafe.tobeme.model.StudyCafeLockerPass;
import cleancode.studycafe.tobeme.model.StudyCafePass;

import java.util.List;

public class StudyCafeTypeHandler {

    private static final InputHandler inputHandler = new InputHandler();
    private static final OutputHandler outputHandler = new OutputHandler();

    public void run(StudyCafePassType type) {
        StudyCafeFileHandler studyCafeFileHandler = new StudyCafeFileHandler();
        List<StudyCafePass> studyCafePasses = studyCafeFileHandler.readStudyCafePassesBy(type);
        outputHandler.showPassListForSelection(studyCafePasses);
        StudyCafePass selectedPass = inputHandler.getSelectPass(studyCafePasses);

        StudyCafeLockerPass lockerPass = null;
        if (type.isLockerAvailable()) {
            lockerPass = runForLocker(studyCafeFileHandler, selectedPass);
        }

        outputHandler.showPassOrderSummary(selectedPass, lockerPass);
    }

    private StudyCafeLockerPass runForLocker(StudyCafeFileHandler studyCafeFileHandler, StudyCafePass selectedPass) {
        StudyCafeLockerPass lockerPass = studyCafeFileHandler.readLockerPassesBy(selectedPass);
        boolean lockerSelection = false;
        if (lockerPass != null) {
            outputHandler.askLockerPass(lockerPass);
            lockerSelection = inputHandler.getLockerSelection();
        }

        if (lockerSelection) {
            return lockerPass;
        }

        return null;
    }
}
