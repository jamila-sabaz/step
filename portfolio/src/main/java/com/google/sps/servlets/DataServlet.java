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

/** Servlet that encapsulates some data from training exercises. */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

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
     
      Comment comment = new Comment(id, title, timestamp);
      comments.add(comment);
      
    }
    Gson gson = new Gson();

    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(comments));
  }

  /* Do Post function responsible for creating new tasks. */ 
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException 
  {
      // Method to create and send New Comment to the server.
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
    * @param request - Defines an object to provide client request information to a servlet.
    * @param name - Name of the requested element (can be name of teh text-box in teh form).
    * @param defaultValue - The value, related to the element, that was requested (could be true/false).
    * @return - The request parameter, or the default value if the parameter
   *         was not specified by the client.
    */
   /* Private function accessing the properties of the Comment-objects. */
  private String getParameter(HttpServletRequest request, String name, String defaultValue) 
  {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }
}
