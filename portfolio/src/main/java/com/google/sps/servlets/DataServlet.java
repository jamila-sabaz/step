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

import com.google.appengine.api.datastore.FetchOptions;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.sps.data.Comment;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/** Servlet that encapsulates some data from training exercises. */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  
  // Hard-coded value to limit number of comments on the page.
  private int limit = 5;
  private int count = 0;
  // private Object lock = new Object();
  /* Do Get function to fetch and list the todo list items onto the home page*/
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException 
  {
    Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    // Private class which is supposed to act like a Comment class.
    List<Comment> comments = new ArrayList<>();
    for (Entity entity : results.asIterable(FetchOptions.Builder.withLimit(Integer.parseInt(getParameter(request, "limit", "")))) ) {
      long id = entity.getKey().getId();
      String title = (String) entity.getProperty("title");
      long timestamp = (long) entity.getProperty("timestamp");
      // Private class which is supposed to act like a Comment class but was changed to be a constructor.
      Comment comment = new Comment(id, title, timestamp);
      
      comments.add(comment);
      
    }
    Gson gson = new Gson();

    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(comments));

  }


  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    // Get the input from the form.
    String fullName = getParameter(request, "name-input", "");
    String text = getParameter(request, "text-input", "");
    // Break the text into individual words.
    String[] names = fullName.split("\\s*,\\s*");
    String[] words = text.split("\\s*,\\s*");

    // Respond with the result.
    response.setContentType("text/html;");
    response.getWriter().print("A message from: ");
    response.getWriter().println(Arrays.toString(names));
    response.getWriter().println(Arrays.toString(words));

    // Here starts the New Comment part.
    String title = request.getParameter("title");
    long timestamp = System.currentTimeMillis();

    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("title", title);
    commentEntity.setProperty("timestamp", timestamp);
    
    // Create a server connection to get data and put new comments to teh database.
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity);

    // After the procedure go back to the home page.
    response.sendRedirect("/index.html");

  }
  
  /**
   * @return the request parameter, or the default value if the parameter
   *         was not specified by the client
   */
  
  private String getParameter(HttpServletRequest request, String name, String defaultValue) 
  {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }

  
}
