<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="it.sauronsoftware.cron4j.TaskExecutor"%>
<%
// Retrieves the current timestamp
long now = System.currentTimeMillis();
// Retrieves the executors.
TaskExecutor[] executors = (TaskExecutor[]) request.getAttribute("executors");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>cron4web</title>
</head>
<body>
<h1>Ongoing tasks</h1>
<% if (executors.length > 0) { %>

  <table border="1" cellspacing="2" cellpadding="3">
    <tr>
      <th>Task</th>
      <th>Status</th>
      <th>Message</th>
      <th>Level</th>
      <th>Actions</th>
    </tr>
  <% for (int i = 0; i < executors.length; i++) { %>
    <%
    boolean canBePaused = executors[i].canBePaused();
    boolean canBeStopped = executors[i].canBeStopped();
    boolean isPaused = canBePaused && executors[i].isPaused();
    boolean isStopped = canBeStopped && executors[i].isStopped();
    boolean isAlive = executors[i].isAlive();
    String status;
    if (isPaused) {
    	status = "PAUSED";
    } else if (isStopped) {
    	status = "STOPPED";
    } else if (isAlive) {
    	status = "RUNNING";
    } else if (executors[i].getStartTime() < 0) {
    	status = "STARTING";
    } else {
    	status = "COMPLETED";
    }
    String id = executors[i].getGuid();
    %>
  	<tr>
  	  <td><%= executors[i].getTask().toString() %></td>
  	  <td><%= status %></td>
  	  <td>
  	    <% if (executors[i].supportsStatusTracking()) { %>
  	      <%= executors[i].getStatusMessage() %>
  	    <% } else { %>
  	      N/A
  	    <% } %>
  	  </td>
  	  <td>
  	    <% if (executors[i].supportsCompletenessTracking()) { %>
  	      <%= (int) Math.round(executors[i].getCompleteness() * 100) %> &#37;
  	    <% } else { %>
  	      N/A
  	    <% } %>
  	  </td>
  	  <td>
  	    <% if (canBePaused) { %>
  	      <% if (isPaused) { %>
  	        <a href="?action=resume&id=<%= id %>">RESUME</a>
  	      <% } else { %>
  	        <a href="?action=pause&id=<%= id %>">PAUSE</a>
  	      <% } %>
  	    <% } %>
  	    <% if (canBeStopped && !isStopped) { %>
  	        <a href="?action=stop&id=<%= id %>">STOP</a>
  	    <% } %>
  	  </td>
  	</tr>
  <% } %>
  </table>

<% } else { %>

  <p>No task is running...</p>

<% } %>

<p>Timestamp: <%= now %></p>
<p><a href="?ts=<%= now %>" onclick="location.reload(); return false;">Refresh page</a></p>
</body>
</html>