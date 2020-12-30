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

package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.repackaged.com.google.gson.Gson;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import com.google.sps.data.SentScore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/sentiment")
public class SentimentAnalysisServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String message = request.getParameter("message");

    Document doc =
        Document.newBuilder().setContent(message).setType(Document.Type.PLAIN_TEXT).build();
    LanguageServiceClient languageService = LanguageServiceClient.create();
    Sentiment sentiment = languageService.analyzeSentiment(doc).getDocumentSentiment();
    long score = (long) sentiment.getScore();
    languageService.close();

    // Output the sentiment score as HTML.
    // A real project would probably store the score alongside the content.
    // response.setContentType("text/html;");
    // response.getWriter().println("<h1>Sentiment Analysis</h1>");
    // response.getWriter().println("<p>You entered: " + message + "</p>");
    // response.getWriter().println("<p>Sentiment analysis score: " + score + "</p>");
    // response.getWriter().println("<p><a href=\"/\">Back</a></p>");

      Entity sentScoreEntity = new Entity("SentScore");
      sentScoreEntity.setProperty("message", message);
      sentScoreEntity.setProperty("score", score);
      
      // Create a server connection to get data and put new sentScores to teh database.
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      datastore.put(sentScoreEntity);

      // After the procedure go back to the home page.
      response.sendRedirect("/index.html");
  }

  /* Do Get function to fetch and list the todo list items onto the home page*/
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query("SentScore").addSort("score", SortDirection.DESCENDING);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);
    
    // Private class which is supposed to act like a SentScore class.
    List<SentScore> SentScores = new ArrayList<>();
    for (Entity entity : results.asIterable()) {
      String message = (String) entity.getProperty("message");
      long score = (long) entity.getProperty("score");

      SentScore SentScore = new SentScore( message, score);
      SentScores.add(SentScore);
    }
    Gson gson = new Gson();

    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(SentScores));
  }
     /**
    * @param request - Defines an object to provide client request information to a servlet.
    * @param name - Name of the requested element (can be name of teh text-box in teh form).
    * @param defaultValue - The value, related to the element, that was requested (could be true/false).
    * @return - The request parameter, or the default value if the parameter
   *         was not specified by the client.
    */
   /* Private function accessing the properties of the Comment-objects. */
  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }
}
