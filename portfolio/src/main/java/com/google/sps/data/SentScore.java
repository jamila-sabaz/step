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

package com.google.sps.data;
/**
 * Class representing the comment object with its properties.
 */
public final class SentScore {
  /* Private variables to be used in the constructor to pass the values/input to properties. */
  private final String message;
  private final long score;
  /**
   * @param message - Content of the comment written as text.
   * @param score - A digital record of the time of occurrence of a particular event (when the comment was posted on the server.)
   */
  public SentScore( String message, long score) {
    
    this.message = message;
    this.score = score;
  }
}
