import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Author: ilya.petrovskiy
 * Date: 24.08.2017.
 */
public class EventCounterImplTest {

    private EventCounter eventCounter;

    @Before
    public void newEventsRegistration() throws InterruptedException {
        TestPrivateEventCounterImpl er = new TestPrivateEventCounterImpl();
        generateEvents(er, EventCounter.DAY_IN_MILLS, 100000, "event1");
        generateEvents(er, EventCounter.HOUR_IN_MILLS, 20000, "event2");
        generateEvents(er, EventCounter.MINUTE_IN_MILLS, 3000, "event3");
        er.setTimeShift(0L);
        this.eventCounter = er;
    }

    private void generateEvents(TestPrivateEventCounterImpl er, long shiftStartPoint, int numberOfEvents, Object event) {
        er.setTimeShift(shiftStartPoint);
        for (int i = 0; i < numberOfEvents; i++) {
            er.registerEvent(event);
            // reduce time shift = shift EventCounter current time -> (right)
            er.setTimeShift(er.getTimeShift() - 1);
        }
    }

    @Test
    public void test() throws InterruptedException {
//        long start = System.currentTimeMillis();
        assertEquals(3000, this.eventCounter.numberOfEventsPerLastMinute());
//        System.out.println(System.currentTimeMillis() - start);
//        start = System.currentTimeMillis();
        assertEquals(23000, this.eventCounter.numberOfEventsPerLastHour());
//        System.out.println(System.currentTimeMillis() - start);
//        start = System.currentTimeMillis();
        assertEquals(123000, this.eventCounter.numberOfEventsPerLastDay());
//        System.out.println(System.currentTimeMillis() - start);
    }


    // class for time register management purposes
    // we fix current time and fill
    private static class TestPrivateEventCounterImpl extends EventCounterImpl {

        // start point of test requests
        private long startPoint = System.currentTimeMillis();

        //time shift <- (left)
        private long timeShift = 0L;

        long getTimeShift() {
            return timeShift;
        }

        void setTimeShift(long timeShift) {
            this.timeShift = timeShift;
        }

        @Override
        long getEventTime() {
            return startPoint - timeShift;
        }

    }

}