package seedu.triplog.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.triplog.model.Model.PREDICATE_SHOW_ALL_TRIPS;

import java.util.Comparator;

import seedu.triplog.model.Model;
import seedu.triplog.model.trip.Trip;

/**
 * Lists all trips in the trip log to the user, sorted by start date ascending.
 * Trips with no start date are shown last.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_SUCCESS = "Listed all trips sorted by start date";

    public static final Comparator<Trip> SORT_BY_START_DATE = Comparator.comparing(
            trip -> trip.getStartDate() == null ? null : trip.getStartDate().value,
            Comparator.nullsLast(Comparator.naturalOrder())
    );

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredTripList(PREDICATE_SHOW_ALL_TRIPS);
        model.updateSortedTripList(SORT_BY_START_DATE);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
