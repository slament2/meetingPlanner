
import org.apache.commons.lang3.tuple.Pair;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Planner {

    public static List<Pair<LocalTime, LocalTime>> plan(Calendar calendar1, Calendar calendar2, Duration duration) {

        if (Duration.between(calendar1.getWorkingHours().getLeft(), calendar1.getWorkingHours().getRight())
                .compareTo(Duration.between(calendar2.getWorkingHours().getLeft(), calendar2.getWorkingHours().getRight())) < 0
        ) {
            Calendar tmpCalendar = calendar1;
            calendar1 = calendar2;
            calendar2 = tmpCalendar;
        }

        List<Pair<LocalTime, LocalTime>> calendar1MergedMeetings = mergeAdjacentMeetings(calendar1);
        List<Pair<LocalTime, LocalTime>> calendar2MergedMeetings = mergeAdjacentMeetings(calendar2);

        LocalTime latestStartTime = calendar1.getWorkingHours().getLeft();

        List<Pair<LocalTime, LocalTime>> calendar1PossibleRanges = new ArrayList<>();

        for (Pair<LocalTime, LocalTime> meeting : calendar1MergedMeetings) {

            if(Duration.between(latestStartTime, meeting.getLeft()).compareTo(duration) >= 0
                    && latestStartTime.isAfter(calendar2.getWorkingHours().getLeft())
                    && meeting.getLeft().isBefore(calendar2.getWorkingHours().getRight())
            )
                calendar1PossibleRanges.add(Pair.of(latestStartTime, meeting.getLeft()));

            latestStartTime = meeting.getRight();
        }

        if(Duration.between(latestStartTime, calendar1.getWorkingHours().getRight()).compareTo(duration) >= 0)
            calendar1PossibleRanges.add(Pair.of(latestStartTime, calendar1.getWorkingHours().getRight()));

        List<Pair<LocalTime, LocalTime>> resultList = new ArrayList<>(calendar1PossibleRanges);
        List<Pair<LocalTime, LocalTime>> meetingsToBeRemoved = new ArrayList<>();

        for(Pair<LocalTime, LocalTime> resultListMeeting: calendar1PossibleRanges) {
            for (Pair<LocalTime, LocalTime> calendar2Meeting: calendar2MergedMeetings){
                if(
                        resultListMeeting.getLeft().isBefore(calendar2Meeting.getLeft())
                        && resultListMeeting.getRight().isAfter(calendar2Meeting.getLeft())
                        && (resultListMeeting.getRight().isBefore(calendar2Meeting.getRight()) || resultListMeeting.getRight().equals(calendar2Meeting.getRight()))
                        && Duration.between(resultListMeeting.getLeft(), calendar2Meeting.getLeft()).compareTo(duration) >= 0
                ) {
                    meetingsToBeRemoved.add(resultListMeeting);
                    resultList.add(Pair.of(resultListMeeting.getLeft(), calendar2Meeting.getLeft()));

                } else if ((resultListMeeting.getLeft().isAfter(calendar2Meeting.getLeft()) || (resultListMeeting.getLeft().equals(calendar2Meeting.getLeft())))
                        && resultListMeeting.getRight().isAfter(calendar2Meeting.getRight())
                        && resultListMeeting.getLeft().isBefore(calendar2Meeting.getRight())
                        && Duration.between(calendar2Meeting.getRight(), resultListMeeting.getRight()).compareTo(duration) >= 0

                ){
                    meetingsToBeRemoved.add(resultListMeeting);
                    resultList.add(Pair.of(calendar2Meeting.getRight(), resultListMeeting.getRight()));

                } else if (resultListMeeting.getLeft().isBefore(calendar2Meeting.getLeft())
                        && resultListMeeting.getRight().isAfter(calendar2Meeting.getRight())
                ) {
                    if(Duration.between(resultListMeeting.getLeft(), calendar2Meeting.getLeft()).compareTo(duration) >= 0){
                        meetingsToBeRemoved.add(resultListMeeting);
                        resultList.add(Pair.of(resultListMeeting.getLeft(), calendar2Meeting.getLeft()));
                    }
                    if(Duration.between(resultListMeeting.getRight(), calendar2Meeting.getRight()).compareTo(duration) >= 0){
                        meetingsToBeRemoved.add(resultListMeeting);
                        resultList.add(Pair.of(resultListMeeting.getRight(), calendar2Meeting.getRight()));
                    }
                } else if (resultListMeeting.getLeft().isAfter(calendar2Meeting.getLeft()) && resultListMeeting.getRight().isBefore(calendar2Meeting.getRight()))
                    meetingsToBeRemoved.add(resultListMeeting);
            }

            if(resultListMeeting.getLeft().isBefore(calendar2.getWorkingHours().getLeft())
                && Duration.between(calendar2.getWorkingHours().getLeft(), resultListMeeting.getRight()).compareTo(duration) >= 0
            ) {
                meetingsToBeRemoved.add(resultListMeeting);
                resultList.add(Pair.of(calendar2.getWorkingHours().getLeft(), resultListMeeting.getRight()));
            }

            if(resultListMeeting.getRight().isAfter(calendar2.getWorkingHours().getRight())
                    && Duration.between(resultListMeeting.getLeft(), calendar2.getWorkingHours().getRight()).compareTo(duration) >= 0
            ) {
                meetingsToBeRemoved.add(resultListMeeting);
                resultList.add(Pair.of(resultListMeeting.getLeft(), calendar2.getWorkingHours().getRight()));
            }

        }

        meetingsToBeRemoved.forEach(resultList::remove);

        resultList.sort(Comparator.comparing(Pair::getLeft));

        return resultList;
    }

    private static List<Pair<LocalTime, LocalTime>> mergeAdjacentMeetings(Calendar calendar){
        List<Pair<LocalTime, LocalTime>> plannedMeetingsMerged = new ArrayList<>(calendar.getPlannedMeetings());

        for (int i = 0; i < calendar.getPlannedMeetings().size()-1; i++) {
            Pair<LocalTime, LocalTime> meeting1 = calendar.getPlannedMeetings().get(i);
            Pair<LocalTime, LocalTime> meeting2 = calendar.getPlannedMeetings().get(i+1);

            if(meeting1.getRight().equals(meeting2.getLeft())){
                plannedMeetingsMerged.add(Pair.of(meeting1.getLeft(), meeting2.getRight()));
                plannedMeetingsMerged.remove(meeting1);
                plannedMeetingsMerged.remove(meeting2);
            }

        }

        return plannedMeetingsMerged;
    }

}
