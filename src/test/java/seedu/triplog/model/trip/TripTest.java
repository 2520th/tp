package seedu.triplog.model.trip;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.triplog.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.triplog.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.triplog.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.triplog.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.triplog.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.triplog.model.trip.Trip.MAX_NAME_LENGTH;
import static seedu.triplog.testutil.Assert.assertThrows;
import static seedu.triplog.testutil.TypicalTrips.ALICE;
import static seedu.triplog.testutil.TypicalTrips.BOB;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.triplog.testutil.TripBuilder;

public class TripTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Trip trip = new TripBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> trip.getTags().remove(0));
    }

    @Test
    public void isSameTrip() {
        // same object -> returns true
        assertTrue(ALICE.isSameTrip(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSameTrip(null));

        // same name, same dates, other attributes different -> returns true
        Trip editedAlice = new TripBuilder(ALICE).withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB)
                .withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND).build();
        assertTrue(ALICE.isSameTrip(editedAlice));

        // different name, all other attributes same -> returns false
        editedAlice = new TripBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.isSameTrip(editedAlice));

        // name differs in case, all other attributes same -> returns false
        Trip editedBob = new TripBuilder(BOB).withName(VALID_NAME_BOB.toLowerCase()).build();
        assertFalse(BOB.isSameTrip(editedBob));

        // name has trailing spaces, all other attributes same -> returns false
        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        editedBob = new TripBuilder(BOB).withName(nameWithTrailingSpaces).build();
        assertFalse(BOB.isSameTrip(editedBob));

        // same name, non-overlapping dates -> returns false
        Trip trip1 = new TripBuilder().withName("Trip").withStart("2026-01-01").withEnd("2026-01-10").build();
        Trip trip2 = new TripBuilder().withName("Trip").withStart("2026-02-01").withEnd("2026-02-10").build();
        assertFalse(trip1.isSameTrip(trip2));

        // same name, overlapping dates -> returns true
        trip2 = new TripBuilder().withName("Trip").withStart("2026-01-05").withEnd("2026-01-15").build();
        assertTrue(trip1.isSameTrip(trip2));

        // same name, one range contains the other -> returns true
        trip2 = new TripBuilder().withName("Trip").withStart("2026-01-03").withEnd("2026-01-07").build();
        assertTrue(trip1.isSameTrip(trip2));

        // same name, touching at boundary (end1 == start2) -> returns true
        trip2 = new TripBuilder().withName("Trip").withStart("2026-01-10").withEnd("2026-01-20").build();
        assertTrue(trip1.isSameTrip(trip2));

        // same name, identical dates -> returns true
        trip2 = new TripBuilder().withName("Trip").withStart("2026-01-01").withEnd("2026-01-10").build();
        assertTrue(trip1.isSameTrip(trip2));

        // different name, overlapping dates -> returns false
        trip2 = new TripBuilder().withName("Other Trip").withStart("2026-01-05").withEnd("2026-01-15").build();
        assertFalse(trip1.isSameTrip(trip2));
    }

    @Test
    public void isSameTrip_bothOnlyStartDatesSame_returnsTrue() {
        Trip trip1 = new TripBuilder().withName("Trip").withStart("2026-03-01").withEnd(null).build();
        Trip trip2 = new TripBuilder().withName("Trip").withStart("2026-03-01").withEnd(null).build();
        assertTrue(trip1.isSameTrip(trip2));
    }

    @Test
    public void isSameTrip_bothOnlyStartDatesDifferent_returnsFalse() {
        Trip trip1 = new TripBuilder().withName("Trip").withStart("2026-03-01").withEnd(null).build();
        Trip trip2 = new TripBuilder().withName("Trip").withStart("2026-04-01").withEnd(null).build();
        assertFalse(trip1.isSameTrip(trip2));
    }

    @Test
    public void isSameTrip_bothOnlyEndDatesSame_returnsTrue() {
        Trip trip1 = new TripBuilder().withName("Trip").withStart(null).withEnd("2026-03-10").build();
        Trip trip2 = new TripBuilder().withName("Trip").withStart(null).withEnd("2026-03-10").build();
        assertTrue(trip1.isSameTrip(trip2));
    }

    @Test
    public void isSameTrip_bothOnlyEndDatesDifferent_returnsFalse() {
        Trip trip1 = new TripBuilder().withName("Trip").withStart(null).withEnd("2026-03-10").build();
        Trip trip2 = new TripBuilder().withName("Trip").withStart(null).withEnd("2026-04-10").build();
        assertFalse(trip1.isSameTrip(trip2));
    }

    @Test
    public void isSameTrip_startOnlyWithinFullRange_returnsTrue() {
        Trip trip1 = new TripBuilder().withName("Trip").withStart("2026-01-01").withEnd("2026-01-10").build();
        Trip trip2 = new TripBuilder().withName("Trip").withStart("2026-01-05").withEnd(null).build();
        assertTrue(trip1.isSameTrip(trip2));
    }

    @Test
    public void isSameTrip_startOnlyOutsideFullRange_returnsFalse() {
        Trip trip1 = new TripBuilder().withName("Trip").withStart("2026-01-01").withEnd("2026-01-10").build();
        Trip trip2 = new TripBuilder().withName("Trip").withStart("2026-02-01").withEnd(null).build();
        assertFalse(trip1.isSameTrip(trip2));
    }

    @Test
    public void isSameTrip_endOnlyWithinFullRange_returnsTrue() {
        Trip trip1 = new TripBuilder().withName("Trip").withStart("2026-01-01").withEnd("2026-01-10").build();
        Trip trip2 = new TripBuilder().withName("Trip").withStart(null).withEnd("2026-01-05").build();
        assertTrue(trip1.isSameTrip(trip2));
    }

    @Test
    public void isSameTrip_endOnlyOutsideFullRange_returnsFalse() {
        Trip trip1 = new TripBuilder().withName("Trip").withStart("2026-01-01").withEnd("2026-01-10").build();
        Trip trip2 = new TripBuilder().withName("Trip").withStart(null).withEnd("2026-02-01").build();
        assertFalse(trip1.isSameTrip(trip2));
    }

    @Test
    public void isSameTrip_startOnlyAgainstOtherFullRange_returnsTrue() {
        Trip trip1 = new TripBuilder().withName("Trip").withStart("2026-01-05").withEnd(null).build();
        Trip trip2 = new TripBuilder().withName("Trip").withStart("2026-01-01").withEnd("2026-01-10").build();
        assertTrue(trip1.isSameTrip(trip2));
    }

    @Test
    public void isSameTrip_startOnlyOutsideOtherFullRange_returnsFalse() {
        Trip trip1 = new TripBuilder().withName("Trip").withStart("2026-02-01").withEnd(null).build();
        Trip trip2 = new TripBuilder().withName("Trip").withStart("2026-01-01").withEnd("2026-01-10").build();
        assertFalse(trip1.isSameTrip(trip2));
    }

    @Test
    public void isSameTrip_endOnlyAgainstOtherFullRange_returnsTrue() {
        Trip trip1 = new TripBuilder().withName("Trip").withStart(null).withEnd("2026-01-05").build();
        Trip trip2 = new TripBuilder().withName("Trip").withStart("2026-01-01").withEnd("2026-01-10").build();
        assertTrue(trip1.isSameTrip(trip2));
    }

    @Test
    public void isSameTrip_endOnlyOutsideOtherFullRange_returnsFalse() {
        Trip trip1 = new TripBuilder().withName("Trip").withStart(null).withEnd("2026-02-01").build();
        Trip trip2 = new TripBuilder().withName("Trip").withStart("2026-01-01").withEnd("2026-01-10").build();
        assertFalse(trip1.isSameTrip(trip2));
    }

    @Test
    public void isSameTrip_bothNullDates_returnsFalse() {
        Trip trip1 = new TripBuilder().withName("Trip").withStart(null).withEnd(null).build();
        Trip trip2 = new TripBuilder().withName("Trip").withStart(null).withEnd(null).build();
        assertFalse(trip1.isSameTrip(trip2));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Trip aliceCopy = new TripBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different trip -> returns false
        assertFalse(ALICE.equals(BOB));

        // different name -> returns false
        Trip editedAlice = new TripBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new TripBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different email -> returns false
        editedAlice = new TripBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different address -> returns false
        editedAlice = new TripBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different tags -> returns false
        editedAlice = new TripBuilder(ALICE).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(ALICE.equals(editedAlice));
    }

    @Test
    public void hashCode_test() {
        Trip aliceCopy = new TripBuilder(ALICE).build();
        assertEquals(ALICE.hashCode(), aliceCopy.hashCode());
        assertNotEquals(ALICE.hashCode(), BOB.hashCode());
    }

    @Test
    public void toStringMethod() {
        String expected = Trip.class.getCanonicalName() + "{name=" + ALICE.getName() + ", phone=" + ALICE.getPhone()
                + ", email=" + ALICE.getEmail() + ", address=" + ALICE.getAddress()
                + ", startDate=" + ALICE.getStartDate() + ", endDate=" + ALICE.getEndDate()
                + ", tags=" + ALICE.getTags() + "}";
        assertEquals(expected, ALICE.toString());
    }

    @Test
    public void getDurationInDays() {
        Name name = new Name("Test");
        // Valid range
        Trip validTrip = new Trip(name, null, null, null, Collections.emptySet(),
                new TripDate("2026-01-01"), new TripDate("2026-01-11"));
        assertEquals(10, validTrip.getDurationInDays());

        // Null start
        Trip nullStart = new Trip(name, null, null, null, Collections.emptySet(),
                null, new TripDate("2026-01-11"));
        assertEquals(-1, nullStart.getDurationInDays());

        // Null end
        Trip nullEnd = new Trip(name, null, null, null, Collections.emptySet(),
                new TripDate("2026-01-01"), null);
        assertEquals(-1, nullEnd.getDurationInDays());
    }

    @Test
    public void constructor_startDateAfterEndDate_throwsIllegalArgumentException() {
        Name name = new Name("Test Trip");
        Phone phone = null;
        Email email = null;
        Address address = null;

        TripDate startDate = new TripDate("2026-12-31");
        TripDate endDate = new TripDate("2026-01-01"); // earlier than startDate

        assertThrows(IllegalArgumentException.class, () ->
                new Trip(name, phone, email, address, Collections.emptySet(), startDate, endDate)
        );
    }

    @Test
    public void constructor_startDateEqualsEndDate_noException() {
        Name name = new Name("Test Trip");
        TripDate startDate = new TripDate("2026-01-01");
        TripDate endDate = new TripDate("2026-01-01");

        new Trip(name, null, null, null, Collections.emptySet(), startDate, endDate);
        // No exception expected
    }

    @Test
    public void constructor_startDateBeforeEndDate_noException() {
        Name name = new Name("Test Trip");
        TripDate startDate = new TripDate("2026-01-01");
        TripDate endDate = new TripDate("2026-12-31");

        new Trip(name, null, null, null, Collections.emptySet(), startDate, endDate);
        // No exception expected
    }

    @Test
    public void constructor_nullStartDate_noException() {
        Name name = new Name("Test Trip");
        TripDate endDate = new TripDate("2026-01-01");

        new Trip(name, null, null, null, Collections.emptySet(), null, endDate);
    }

    @Test
    public void constructor_nullEndDate_noException() {
        Name name = new Name("Test Trip");
        TripDate startDate = new TripDate("2026-01-01");

        new Trip(name, null, null, null, Collections.emptySet(), startDate, null);
    }

    @Test
    public void constructor_bothDatesNull_noException() {
        Name name = new Name("Test Trip");

        new Trip(name, null, null, null, Collections.emptySet(), null, null);
    }

    @Test
    public void constructor_nameTooLong_throwsIllegalArgumentException() {
        String longName = "A".repeat(MAX_NAME_LENGTH + 1);
        assertThrows(IllegalArgumentException.class, () ->
                new Trip(new Name(longName), null, null, null, Collections.emptySet(), null, null)
        );
    }

    @Test
    public void constructor_validTrip_success() {
        TripDate start = new TripDate("2025-01-01");
        TripDate end = new TripDate("2025-12-31");
        Trip trip = new Trip(new Name("Valid Trip"), null, null, null, Collections.emptySet(), start, end);
        assertEquals("Valid Trip", trip.getName().toString());
    }

    @Test
    public void chronologicalComparator_differentDatesAndNulls_sortedChronologically() {
        // Case 1: Both dates present, different dates
        Trip janTrip = new Trip(new Name("A"), null, null, null, Collections.emptySet(),
                new TripDate("2026-01-01"), null);
        Trip febTrip = new Trip(new Name("B"), null, null, null, Collections.emptySet(),
                new TripDate("2026-02-01"), null);
        assertTrue(Trip.CHRONOLOGICAL_COMPARATOR.compare(janTrip, febTrip) < 0);

        // Case 2: One date null, one present (null should be last)
        Trip nullTrip = new Trip(new Name("C"), null, null, null, Collections.emptySet(), null, null);
        assertTrue(Trip.CHRONOLOGICAL_COMPARATOR.compare(janTrip, nullTrip) < 0);
        assertTrue(Trip.CHRONOLOGICAL_COMPARATOR.compare(nullTrip, janTrip) > 0);

        // Case 3: Both dates null, alphabetical tie-break
        Trip nullTripAlphaA = new Trip(new Name("Alpha"), null, null, null, Collections.emptySet(), null, null);
        Trip nullTripBetaB = new Trip(new Name("Beta"), null, null, null, Collections.emptySet(), null, null);
        assertTrue(Trip.CHRONOLOGICAL_COMPARATOR.compare(nullTripAlphaA, nullTripBetaB) < 0);

        // Case 4: Same dates, alphabetical tie-break
        Trip sameDateA = new Trip(new Name("A"), null, null, null, Collections.emptySet(),
                new TripDate("2026-01-01"), null);
        Trip sameDateB = new Trip(new Name("B"), null, null, null, Collections.emptySet(),
                new TripDate("2026-01-01"), null);
        assertTrue(Trip.CHRONOLOGICAL_COMPARATOR.compare(sameDateA, sameDateB) < 0);
    }
}
