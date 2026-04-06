# Ong Jin Hui - Project Portfolio for TripLog

## Project Overview
**TripLog** is a CLI-centric desktop application designed for travelers who prefer managing their itineraries via a terminal interface. It allows users to track destinations and trip schedules, providing a streamlined, keyboard-first alternative to traditional GUI-based travel planners.

### Summary of Contributions

* **Code Contribution**: [Link to Repo Analyzer](https://nus-cs2103-ay2526-s2.github.io/tp-dashboard/?search=&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2026-02-20T00%3A00%3A00&filteredFileName=&tabOpen=true&tabType=authorship&tabAuthor=poise3&tabRepo=AY2526S2-CS2103-F13-2%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false)
* **Feature Enhanced: Flexible and Expanded Add Command**
    * **What it does**: Refactored the `add` command to support using optional fields for `phone`, `address` and `email`. It also introduced two new optional fields `startDate` and `endDate`.
    * **Justification**: A trip may not necessarily have all the information. It also made the command a lot more flexible and less rigid to use.
    * **Highlights**: Optional fields are now saved as `null` in storage. The UI is also refactored to hide optional fields if they are empty.

* **Feature Enhanced: Refactored duplicate checks**
    * **What it does**: Trips with the same `name` and overlapping `dates` are now considered duplicates (duplicate trip can have a `date` part of another trip's date range `sd` - `ed`, or have either the same `sd` or `ed`).
    * **Justification**: Multiple trips to the same destination may exist, but in different time periods.
    * **Highlights**: Implemented new `hasTripExcluding` method in Model for duplicate checks in the `EditCommand` class. New helper method `datesOverlap` in Trip model helps verify overlapping dates for duplicates.
  
* **Documentation Contributions**:
    * **User Guide**: Authored the dupplicate checking logic under "Addding a trip" and "Editing a trip".
    * **Developer Guide**: Added the implementation details for the Add command, including a sequence diagram illustrating the interaction between the `Logic` and `Model` components during a add operation.

* **Quality Assurance & Testing**:
    * Reported **5 alpha-bugs** during the internal testing phase, ranging from high-severity data corruption risks to UI/UX inconsistencies.
    * Identified a critical "Silent Failure" bug where editing a resulting duplicate trip is allowed, corrupting the data and crashing the application on next launch.

---

## Contributions to the User Guide

<box type="info" seamless>

**Duplicate detection:** A trip is considered a duplicate if it has the same name and
overlapping dates as an existing trip. Trips with the same name but non-overlapping date
ranges are allowed. For example, you can have two trips named "Tokyo", one from
2026-01-01 to 2026-01-10 and another from 2026-03-01 to 2026-03-10.
</box>

<box type="info" seamless>

**Duplicate detection:** Editing a trip will be rejected if it violates the duplicate logic as mentioned
in `Adding a trip` (same name and overlapping dates as another existing trip).
</box>

---

## Contributions to the Developer Guide

### Trip Creation: Add Command
The `add` command creates a new trip in the trip log. Only the destination name (n/NAME) is mandatory. All other fields (phone, email, address, start date, end date, tags) are optional and default to null if omitted. When both `startDate` and `endDate` are provided, the Trip model validates that the start date is not after the end date.

The parsing is handled by `AddCommandParser`, which tokenizes the input and checks each optional field via `Optional<String>` before constructing the corresponding `AddCommand`.

On execution, `AddCommand` checks for duplicates via `Model#hasTrip(Trip)`. Two trips are considered duplicates if they share the same name (case-insensitive) and have overlapping date ranges, as determined by the private `Trip#datesOverlap()` method. If trip is non-duplicate, it is added to the model.

The following sequence diagram shows how the `add` command is parsed and executed:

<puml src="diagrams/AddSequenceDiagram.puml" alt="Add Command Sequence Diagram" />

### Adding a trip

1. Adding a trip with only the required field
  1. Test case: `add n/Tokyo`
     Expected: Trip with name "Tokyo" is added. All other fields show as blank/absent.

2. Adding a trip with all fields
  1. Test case: `add n/Osaka p/91234567 e/trip@mail.com a/Dotonbori sd/2026-06-01 ed/2026-06-10 t/food t/travel`
     Expected: Trip is added with all fields populated and both tags shown.

3. Adding a trip with optional fields omitted
  1. Test case: `add n/Kyoto sd/2026-07-01 ed/2026-07-10`
     Expected: Trip is added. Phone, email, and address are absent.

4. Invalid date order
  1. Test case: `add n/Seoul sd/2026-12-31 ed/2026-01-01`
     Expected: Error message indicating start date cannot be after end date. No trip is added.

5. Duplicate trip (same name, overlapping dates)
  1. Prerequisites: A trip named "Tokyo" with dates 2026-06-01 to 2026-06-10 already exists.
  2. Test case: `add n/Tokyo sd/2026-06-05 ed/2026-06-15`
     Expected: Error message indicating duplicate trip. No trip is added.

6. Same name, non-overlapping dates (allowed)
  1. Prerequisites: A trip named "Tokyo" with dates 2026-06-01 to 2026-06-10 already exists.
  2. Test case: `add n/Tokyo sd/2026-09-01 ed/2026-09-10`
     Expected: Trip is added successfully. Both "Tokyo" trips coexist.

7. Missing name
  1. Test case: `add sd/2026-06-01 ed/2026-06-10`
     Expected: Error message indicating invalid command format. No trip is added.

---

## Contributions to Team Tasks

* **Bug Triaging**: Actively participated in reviewing and triaging issues reported during the Alpha phase.
* **PR Reviews**: Reviewed peer PRs to ensure that their implementation is bug-free and that code is up to standard.

---

## Review of Alpha Bugs Reported
| ID   | Title | Severity |
|------|---|----------|
| #165 | Trips are allowed to be created with unlimited 'name' length | Medium   |
| #164 | Filtering context is lost on executing 'add' or 'edit' command | Medium   |
| #163 | Trips with the same name but non-overlapping dates are not allowed | Medium   |
| #162 | Edit command always creates new Trip even if edited field is same | Low      |
| #161 | 'Help' button on GUI fails to align with 'File' button | Low      |
