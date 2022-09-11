import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlannerTest {

    String json1 = "{\n" +
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

    String json2 = "{\n" +
            "working_hours: {\n" +
            "start: \"10:00\",\n" +
            "end: \"18:30\"\n" +
            "},\n" +
            "planned_meeting: [\n" +
            "{\n" +
            "start: \"10:00\",\n" +
            "end: \"11:30\"\n" +
            "},\n" +
            "{\n" +
            "start: \"12:30\",\n" +
            "end: \"14:30\"\n" +
            "},\n" +
            "{\n" +
            "start: \"14:30\",\n" +
            "end: \"15:00\"\n" +
            "},\n" +
            "{\n" +
            "start: \"16:00\",\n" +
            "end: \"17:00\"\n" +
            "}\n" +
            "]\n" +
            "}";

    Calendar calendar1 = new Calendar(json1);
    Calendar calendar2 = new Calendar(json2);

    @Test
    void planTest(){
        List<Pair<LocalTime, LocalTime>> expectedResult = Arrays.asList(
                Pair.of(LocalTime.of(11,30), LocalTime.of(12, 0)),
                Pair.of(LocalTime.of(15,0), LocalTime.of(16, 0)),
                Pair.of(LocalTime.of(18,0), LocalTime.of(18, 30))
        );

        assertEquals(expectedResult, Planner.plan(calendar1, calendar2, Duration.of(30, ChronoUnit.MINUTES)));
    }

}