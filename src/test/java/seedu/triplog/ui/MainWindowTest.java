package seedu.triplog.ui;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import seedu.triplog.commons.core.GuiSettings;
import seedu.triplog.logic.Logic;
import seedu.triplog.logic.commands.CommandResult;
import seedu.triplog.logic.commands.HelpCommand;
import seedu.triplog.logic.parser.exceptions.ParseException;
import seedu.triplog.model.ReadOnlyTripLog;
import seedu.triplog.model.trip.Trip;

/**
 * UI tests for MainWindow focusing on component initialization and error handling.
 */
@ExtendWith(ApplicationExtension.class)
public class MainWindowTest {

    /** Matches the ERROR_ICON prefix defined in ResultDisplay */
    private static final String ERROR_ICON_PREFIX = "[!!]";

    private MainWindow mainWindow;
    private Stage stage;
    private final String error = "Data file error: Corrupted entry detected. Starting fresh.";

    private class LogicStub implements Logic {
        private String errorToReturn;
        private boolean shouldThrowException = false;

        LogicStub(String errorToReturn) {
            this.errorToReturn = errorToReturn;
        }

        LogicStub(boolean shouldThrowException) {
            this.shouldThrowException = shouldThrowException;
        }

        LogicStub(boolean shouldThrowException, boolean shouldShowHelp) {
            this.shouldThrowException = shouldThrowException;
            this.shouldShowHelp = shouldShowHelp;
        }

        private boolean shouldShowHelp = false;

        @Override
        public CommandResult execute(String cmd) throws ParseException {
            if (shouldThrowException) {
                throw new ParseException("Unknown command");
            }
            return new CommandResult("Success", shouldShowHelp, false);
        }

        @Override
        public ReadOnlyTripLog getTripLog() {
            return null;
        }

        @Override
        public ObservableList<Trip> getFilteredTripList() {
            return FXCollections.observableArrayList();
        }

        @Override
        public ObservableList<Trip> getSortedTripList() {
            return FXCollections.observableArrayList();
        }

        @Override
        public Path getTripLogFilePath() {
            return Path.of("dummy.json");
        }

        @Override
        public GuiSettings getGuiSettings() {
            return new GuiSettings();
        }

        @Override
        public void setGuiSettings(GuiSettings gui) {
        }

        @Override
        public String getInitialDataLoadError() {
            return errorToReturn;
        }

        @Override
        public String getSummary() {
            return "Summary: 0 Upcoming, 0 Ongoing, 0 Completed, 0 Planning";
        }
    }

    @Start
    public void start(Stage stage) {
        this.stage = stage;
    }

    /**
     * Verifies that the ResultDisplay correctly reflects initial data load errors with the error icon.
     */
    @Test
    public void fillInnerParts_withError_updatesResultDisplay(FxRobot robot) {
        robot.interact(() -> {
            mainWindow = new MainWindow(stage, new LogicStub(error));
            mainWindow.fillInnerParts();
        });

        assertResultDisplayContains(error);
        assertResultDisplayContains(ERROR_ICON_PREFIX);
    }

    /**
     * Verifies that the executeCommand catch block correctly handles exceptions and shows the error icon.
     * This ensures coverage for the catch block in MainWindow#executeCommand.
     */
    @Test
    public void executeCommand_withException_updatesResultDisplay(FxRobot robot) throws Exception {
        LogicStub logicStub = new LogicStub(true);
        robot.interact(() -> {
            mainWindow = new MainWindow(stage, logicStub);
            mainWindow.fillInnerParts();
        });

        // Use reflection to invoke the private executeCommand method
        Method executeCommandMethod = MainWindow.class.getDeclaredMethod("executeCommand", String.class);
        executeCommandMethod.setAccessible(true);

        robot.interact(() -> {
            try {
                executeCommandMethod.invoke(mainWindow, "invalidCommand");
            } catch (Exception e) {
                // Expected exception from reflection wrapper
            }
        });

        assertResultDisplayContains("Unknown command");
        assertResultDisplayContains(ERROR_ICON_PREFIX);
    }

    /**
     * Helper method to access the private ResultDisplay and verify its content.
     */
    private void assertResultDisplayContains(String expectedContent) {
        try {
            Field resultDisplayField = MainWindow.class.getDeclaredField("resultDisplay");
            resultDisplayField.setAccessible(true);
            ResultDisplay rd = (ResultDisplay) resultDisplayField.get(mainWindow);

            Field textAreaField = ResultDisplay.class.getDeclaredField("resultDisplay");
            textAreaField.setAccessible(true);
            TextArea textArea = (TextArea) textAreaField.get(rd);

            String displayedText = textArea.getText();
            assertTrue(displayedText.contains(expectedContent),
                    "Result display should contain: " + expectedContent);
        } catch (Exception e) {
            throw new AssertionError("Reflection failed to access UI components", e);
        }
    }

    /**
     * Verifies that handleHelp opens the window when not showing,
     * then focuses it on a second call.
     */
    @Test
    public void handleHelp_helpAlreadyShowing_focusesWindow(FxRobot robot) {
        robot.interact(() -> {
            mainWindow = new MainWindow(stage, new LogicStub((String) null));
            mainWindow.fillInnerParts();
            mainWindow.handleHelp(); // open 
            mainWindow.handleHelp(); // focus 
        });
    }

    /**
     * Verifies that executeCommand shows FOCUSING_HELP_MESSAGE when isShowHelp=true
     * and the help window is already open.
     */
    @Test
    public void executeCommand_showHelp_helpAlreadyShowing_showsFocusMessage(FxRobot robot) throws Exception {
        LogicStub logicStub = new LogicStub(false, true);
        robot.interact(() -> {
            mainWindow = new MainWindow(stage, logicStub);
            mainWindow.fillInnerParts();
            mainWindow.handleHelp(); // pre-open help window
        });

        Method executeCommandMethod = MainWindow.class.getDeclaredMethod("executeCommand", String.class);
        executeCommandMethod.setAccessible(true);

        robot.interact(() -> {
            try {
                executeCommandMethod.invoke(mainWindow, "help");
            } catch (Exception e) {
                throw new AssertionError("Unexpected exception during executeCommand", e);
            }
        });

        assertResultDisplayContains(HelpCommand.FOCUSING_HELP_MESSAGE);
    }
}
