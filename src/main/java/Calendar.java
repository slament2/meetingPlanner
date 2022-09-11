import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import lombok.Data;
import org.apache.commons.lang3.tuple.Pair;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class Calendar {

    private Pair<LocalTime, LocalTime> workingHours;
    private List<Pair<LocalTime, LocalTime>> plannedMeetings = new ArrayList<>();

    public Calendar (String json){
        JsonObject calendarJson =  (JsonObject) JsonParser.parseString(json);
        JsonObject workingHours = (JsonObject) calendarJson.get("working_hours");

        this.workingHours = jsonMeetingToPair(workingHours);

        JsonArray plannedMeetings = (JsonArray) calendarJson.get("planned_meeting");

        plannedMeetings.forEach(meeting -> this.plannedMeetings.add(jsonMeetingToPair((JsonObject) meeting)));
    }


    private Pair<LocalTime, LocalTime> jsonMeetingToPair(JsonObject jsonMeeting) {

        JsonPrimitive startTime = (JsonPrimitive) jsonMeeting.get("start");
        LocalTime localStartTime = LocalTime.parse(startTime.toString().replace("\"", ""));

        JsonPrimitive endTime = (JsonPrimitive) jsonMeeting.get("end");
        LocalTime localEndTime = LocalTime.parse(endTime.toString().replace("\"", ""));

        return Pair.of(localStartTime, localEndTime);
    }


}
