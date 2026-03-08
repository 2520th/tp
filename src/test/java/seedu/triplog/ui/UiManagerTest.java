package seedu.triplog.ui;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class UiManagerTest {

    @Test
    public void alertDialogPaneFieldId_hasCorrectValue() {
        assertEquals("alertDialogPane", UiManager.ALERT_DIALOG_PANE_FIELD_ID);
    }

    @Test
    public void constructor_withNullLogic_doesNotThrow() {
        assertDoesNotThrow(() -> new UiManager(null));
    }
}
