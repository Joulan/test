import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Author: ilya.petrovskiy
 * Date: 24.08.2017.
 */
class EventCounterImpl implements EventCounter {

    // if we use EventCounter without synchronized =>
//     @NotNull
//     private Queue<Long> timestamps = new ConcurrentLinkedQueue<>();
    // else
    @NotNull
    private final Queue<Long> timestamps = new LinkedList<>();

    @Override
    public void registerEvent(@NotNull Object event) {
        // given task does not describe any event handling (except for its counting)...
        // doSomething(event);
        // ...
        timestamps.add(getEventTime());

        // probably should be run in separate thread
        removeRedundancy();
    }

    @Override
    public int numberOfEventsPerLastMinute() {
        return numberOfEventsPer(MINUTE_IN_MILLS);
    }

    @Override
    public int numberOfEventsPerLastHour() {
        return numberOfEventsPer(HOUR_IN_MILLS);
    }

    @Override
    public int numberOfEventsPerLastDay() {
        return numberOfEventsPer(DAY_IN_MILLS);
    }

    long getEventTime() {
        return System.currentTimeMillis();
    }

    private int numberOfEventsPer(long timeshift) {
        // Search in all linked list - too long operation
        // but we now that all elements are arranged in ascending order =>
        return timestamps.size() - getFirstMoreThan(getEventTime() - timeshift);

        // but this can be done differently | stream api, java 8...but it's so slow :( :
//        return (int) timestamps.parallelStream()
//                .filter(t -> t >= getEventTime() - timeshift)
//                .count();
    }

    private int getFirstMoreThan(long that) {
        int counter = 0;
        for (Long timestamp : timestamps) {
            if (timestamp >= that) {
                return counter;
            }
            counter++;
        }
        return timestamps.size();
    }

    // this method should be used for remove timestamp list redundancy, because max period that we can get = last day
    private void removeRedundancy() {
        // redundant events
        final long minusDay = getEventTime() - DAY_IN_MILLS;
        Long head;
        while ((head = timestamps.peek()) != null && head <= minusDay) {
            timestamps.remove();
        }
    }

}
