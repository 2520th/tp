package seedu.triplog.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.triplog.model.ReadOnlyTripLog;
import seedu.triplog.model.TripLog;
import seedu.triplog.model.tag.Tag;
import seedu.triplog.model.trip.Address;
import seedu.triplog.model.trip.Email;
import seedu.triplog.model.trip.Name;
import seedu.triplog.model.trip.Phone;
import seedu.triplog.model.trip.Trip;
import seedu.triplog.model.trip.TripDate;


/**
 * Contains utility methods for populating {@code TripLog} with sample data.
 */
public class SampleDataUtil {
    public static Trip[] getSamplePersons() {
        return new Trip[] {
            new Trip(new Name("Hotel California"), new Phone("87438807"), new Email("alexyeoh@example.com"),
                    new Address("1670 Ocean Ave, Santa Monica"),
                    getTagSet("Hotel"), new TripDate("2026-03-01"),
                    new TripDate("2026-03-05")),
            new Trip(new Name("ZhangJiaJie Forest"), new Phone("99272758"), new Email("berniceyu@example.com"),
                    new Address("Wulingyuan District, Zhangjiajie, Hunan, China, 427403"),
                    getTagSet("scenery", "China"), new TripDate("2026-03-01"),
                    new TripDate("2026-03-05")),
            new Trip(new Name("Singapore Zoo"), new Phone("93210283"), new Email("charlotte@example.com"),
                    new Address("80 Mandai Lake Rd, 729826"),
                    getTagSet("zoo"), new TripDate("2026-03-01"),
                    new TripDate("2026-03-05")),
            new Trip(new Name("Serangoon Gardens"), new Phone("91031282"), new Email("lidavid@example.com"),
                    new Address("Blk 436 Serangoon Gardens Street 26, #16-43"),
                    getTagSet("garden"), new TripDate("2026-03-01"),
                    new TripDate("2026-03-05")),
            new Trip(new Name("Gardens By The Bay"), new Phone("92492021"), new Email("irfan@example.com"),
                    new Address("Marina Gardens Drive Supertree Grove, Singapore 018953"),
                    getTagSet("garden"), new TripDate("2026-03-01"),
                    new TripDate("2026-03-05")),
            new Trip(new Name("National Museum of Singapore"), new Phone("92624417"), new Email("royb@example.com"),
                    new Address("93 Stamford Rd, Singapore 178897"),
                    getTagSet("colleagues"), new TripDate("2026-03-01"),
                    new TripDate("2026-03-05"))
        };
    }

    public static ReadOnlyTripLog getSampleTripLog() {
        TripLog sampleAb = new TripLog();
        for (Trip sampleTrip : getSamplePersons()) {
            sampleAb.addTrip(sampleTrip);
        }
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

}
