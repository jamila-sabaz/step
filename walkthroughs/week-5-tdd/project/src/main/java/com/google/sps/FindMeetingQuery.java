// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public final class FindMeetingQuery {

  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {

    // Convert collection of events into an arraylist.
    ArrayList<Event> eventList = new ArrayList<>(events);
    // Initialise result variable. 
    Collection<TimeRange> possibleTimes = new ArrayList<>();

    // Initialise variables for no events and no attendees.
    Collection<Event> NO_EVENTS = Collections.emptySet();
    Collection<String> NO_ATTENDEES = Collections.emptySet();
    
    // Special case: duration is longer than a day. No option returned.
    if (request.getDuration() >  TimeRange.WHOLE_DAY.duration() ) {
      return possibleTimes; // Empty set.
    }
    // Special case: no attendees. Return the whole day.
    if (request.getAttendees() == NO_ATTENDEES ) {
      possibleTimes.add(TimeRange.WHOLE_DAY);
      return possibleTimes;
    }
    // Special case: no events on the day. Return the whole day.
    if ( (events == NO_EVENTS)) {
      possibleTimes.add(TimeRange.WHOLE_DAY);
      return possibleTimes;
    }
    // Special case: if the only events attendee is not the one requesting. ignore them.
    // Initialise counter to check how many events don't have requested attendees.
    int count = 0 ;
    for ( int i=0; i< events.size(); i++){
      // If the requested attendees list doesn't contain event attendees increment count.
      if ( request.getAttendees() .containsAll( (eventList.get(i).getAttendees() )) == false ) {
        count ++; 
      }
    }
    // Check if counted number is same as number of events.
    if (count == events.size()){
      possibleTimes.add(TimeRange.WHOLE_DAY);
      return possibleTimes;
    }
    
    // Variable for the merged events for nested and overlapping cases.
    List<TimeRange> merged = new ArrayList<TimeRange>();
    // Previous event variable.
    TimeRange previous = eventList.get(0).getWhen();
    
    // Iterate through events to find overlaps to merge them.
    for(int i=1;i<events.size();i++){
      TimeRange current = eventList.get(i).getWhen();

      if(previous.end() >= current.start()){
        // New updated event interval covering both events.
        TimeRange newTimeOfEvent =  TimeRange.fromStartEnd ( Math.min(previous.start(), current.start() ), Math.max( previous.end(), current.end() ),  false );
        previous = newTimeOfEvent;
      } else {
        merged.add(previous);
        previous = current;
      }
    }
    merged.add(previous);  

    // Now find intervals between the meetings.
    List<TimeRange> gaps = new ArrayList<>();
    // Avoid same name of the variables.
    TimeRange previous2 = merged.get(0);

    TimeRange current2 = TimeRange.fromStartEnd(TimeRange.START_OF_DAY, TimeRange.START_OF_DAY, false);

    // Compare the first event with the start of the day.
    if (previous2.start()!=current2.start()){
      // If there is space between start of the day and the first meeting add the interval.
      gaps.add(TimeRange.fromStartEnd( current2.start(), previous2.start(),false));
    }

    for(int i=1; i<merged.size(); i++){
      // Assign current event to the second event (index 1).
      current2 = merged.get(i);
      // Add interval.
      TimeRange gapInterval =  TimeRange.fromStartEnd(  previous2.end(),current2.start(),false);
      
      gaps.add(gapInterval);
      previous2 = current2;
    }
    // Check if there is a gap between last event adn the end of the day.
    if (previous2.end()!= TimeRange.END_OF_DAY) {
      gaps.add(TimeRange.fromStartEnd( previous2.end(),TimeRange.END_OF_DAY, true));
    }
    // Go through the gaps and check if the requested duration fits in them.
    for (int i=0; i<gaps.size();i++ ){
      if (gaps.get(i).duration() >= request.getDuration() ){
        // If so, add to the resulting set.
        possibleTimes.add(gaps.get(i));
      }
    }  
    return possibleTimes;
  }
}
