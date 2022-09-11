import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CalendarTest {

    private String jsonCalendar = "{\n" +
            "working_hours: {\n" +
            "start: \"09:00\",\n" +
            "end: \"19:55\"\n" +
            "},\n" +
            "planned_meeting: [\n" +
            "{\n" +
            "start: \"09:00\",\n" +
            "end: \"10:30\"\n" +
            "},\n" +
            "{\n" +
            "start: \"12:00\",\n" +
            "end: \"13:00\"\n" +
            "},\n" +
            "{\n" +
            "start: \"16:00\",\n" +
            "end: \"18:00\"\n" +
            "}\n" +
            "]\n" +
            "}";

    private Calendar calendar = new Calendar(jsonCalendar);

    @Test
    public void workingHoursTest(){

        Pair<LocalTime, LocalTime> expectedResult = Pair.of(LocalTime.of(9,0), LocalTime.of(19, 55));

        assertEquals(calendar.getWorkingHours(), expectedResult);
    }

    @Test
    public void plannedMeetingsTest() {
        List<Pair<LocalTime, LocalTime>> expectedResult = Arrays.asList(
                Pair.of(LocalTime.of(9,0), LocalTime.of(10, 30)),
                Pair.of(LocalTime.of(12,0), LocalTime.of(13, 0)),
                Pair.of(LocalTime.of(16,0), LocalTime.of(18, 0))
                );

        assertEquals(expectedResult, calendar.getPlannedMeetings());
    }

}