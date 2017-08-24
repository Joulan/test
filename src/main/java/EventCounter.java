import org.jetbrains.annotations.NotNull;

/**
 * Author: ilya.petrovskiy
 * Date: 24.08.2017.
 */
public interface EventCounter {

    long MINUTE_IN_MILLS = 60000;
    long HOUR_IN_MILLS = 60*MINUTE_IN_MILLS;
    long DAY_IN_MILLS = 24*HOUR_IN_MILLS;

    void registerEvent(@NotNull Object event);

    int numberOfEventsPerLastMinute();

    int numberOfEventsPerLastHour();

    int numberOfEventsPerLastDay();

}
