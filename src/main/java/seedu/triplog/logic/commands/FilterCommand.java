package seedu.triplog.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.triplog.logic.parser.CliSyntax.PREFIX_START_DATE;
import static seedu.triplog.logic.parser.CliSyntax.PREFIX_END_DATE;

import seedu.triplog.commons.util.ToStringBuilder;
import seedu.triplog.logic.commands.exceptions.CommandException;
import seedu.triplog.model.Model;
import seedu.triplog.model.trip.TripDate;

/**
 * Filter trips by date range.
 * Criteria: query start date <= trip start date <= trip end date <= query end date
 */
public class FilterCommand extends Command {

    public static final String COMMAND_WORD = "filter";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Filter trips by date range. "
            + "Parameters: "
            + PREFIX_START_DATE + "START DATE "
            + PREFIX_END_DATE + "END DATE "
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_START_DATE + "2026-01-01 "
            + PREFIX_END_DATE + "2026-03-01 ";

    public static final String MESSAGE_SUCCESS = "Found the following trips:";
    public static final String MESSAGE_NO_TRIPS_FOUND = "No trips found with the given date range.";
    public static final String MESSAGE_ERR_START_AFTER_END = "Start date should not be after end date.";

    private final TripDate startDate, endDate;

    /**
     * Creates an FilterCommand to add the specified {@code Trip}
     */
    public FilterCommand(TripDate startDate, TripDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (startDate.value.isAfter(endDate.value)) {
            return new CommandResult(MESSAGE_ERR_START_AFTER_END);
        }
        model.updateFilteredTripList(trip -> trip.getStartDate().value.isAfter(startDate.value.minusDays(1))
                && trip.getEndDate().value.isBefore(endDate.value.plusDays(1)));

        return new CommandResult(model.getFilteredTripList().isEmpty() ? MESSAGE_NO_TRIPS_FOUND : MESSAGE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof FilterCommand)) {
            return false;
        }

        return ((FilterCommand)other).startDate.equals(startDate)
                && ((FilterCommand)other).endDate.equals(endDate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("startDate", startDate)
                .add("endDate", endDate)
                .toString();
    }
}
