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
  
  private Collection<TimeRange> possibleTimes ;

  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    //throw new UnsupportedOperationException("TODO: Implement this method.");
    // If no attndees
    if ((request.getAttendees() == null ) && (events == null) ){
      possibleTimes.add(TimeRange.WHOLE_DAY);
      return possibleTimes;
    }
    //no conflicts
    else if ((request.getAttendees() != null) && (events == null)) {
      possibleTimes.add(TimeRange.WHOLE_DAY);
      return possibleTimes;
    }
    //if no events
    // else if (events == null){
    //   possibleTimes.add(TimeRange.WHOLE_DAY);
    //   return possibleTimes;
    // }
    
    int min_duration = (int) request.getDuration();
    // Initialize min_time to current time
    int curr_time = TimeRange.START_OF_DAY;

    // Sort array by start time
    //Collections.sort(events);

    // Loop through all intervals
    for(Event event: events){
      int st_tm = event.getWhen().start();
      int end_tm = event.getWhen().end();

      if(curr_time < st_tm ){  //if current time is < start time of the interval
        if(st_tm - curr_time > min_duration){  //if the time between the 2 is greater than min_duration
          possibleTimes.add(TimeRange.fromStartEnd(curr_time, st_tm, true));
        }
        if(st_tm - curr_time == min_duration){  //if the time between the 2 is greater than min_duration
          possibleTimes.add(TimeRange.fromStartEnd(curr_time, st_tm, false));
        }
        curr_time = end_tm; // Update end time to be current_time 
      } 
      else {
        curr_time = Math.max(end_tm, curr_time); // For overlapping intervals, make sure you tax the max between current time and end time
      }
    }

    if (possibleTimes==null){
      return Arrays.asList();
    }
    else
      return possibleTimes;

    
  }
}
