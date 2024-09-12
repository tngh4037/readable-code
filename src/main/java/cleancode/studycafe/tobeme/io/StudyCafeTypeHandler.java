package cleancode.studycafe.tobeme.io;

import cleancode.studycafe.tobeme.define.StudyCafePassType;
import cleancode.studycafe.tobeme.model.StudyCafeLockerPass;
import cleancode.studycafe.tobeme.model.StudyCafePass;

import java.util.List;

public class StudyCafeTypeHandler {

    private static final InputHandler inputHandler = new InputHandler();
    private static final OutputHandler outputHandler = new OutputHandler();

    public void run(StudyCafePassType studyCafePassType) {
        StudyCafeFileHandler studyCafeFileHandler = new StudyCafeFileHandler();
        List<StudyCafePass> studyCafePasses = studyCafeFileHandler.readStudyCafePassesBy(studyCafePassType);
        outputHandler.showPassListForSelection(studyCafePasses);
        StudyCafePass selectedPass = inputHandler.getSelectPass(studyCafePasses);

        StudyCafeLockerPass lockerPass = getLockerPass(studyCafePassType, selectedPass);
        outputHandler.showPassOrderSummary(selectedPass, lockerPass);
    }

    private StudyCafeLockerPass getLockerPass(StudyCafePassType studyCafePassType, StudyCafePass studyCafePass) {
        if (!studyCafePassType.isLockerAvailable()) {
            return null;
        }

        StudyCafeFileHandler studyCafeFileHandler = new StudyCafeFileHandler();
        StudyCafeLockerPass lockerPass = studyCafeFileHandler.readLockerPassesBy(studyCafePass);
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
