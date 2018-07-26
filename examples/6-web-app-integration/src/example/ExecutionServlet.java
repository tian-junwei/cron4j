/*
 * cron4j - A pure Java cron-like scheduler
 * 
 * Copyright (C) 2007-2010 Carlo Pelliccia (www.sauronsoftware.it)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version
 * 2.1, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License 2.1 for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License version 2.1 along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package example;

import it.sauronsoftware.cron4j.Scheduler;
import it.sauronsoftware.cron4j.TaskExecutor;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This servlet enables the user to view and control any ongoing task execution.
 * The HTML layout is generated calling the /WEB-INF/ongoing.jsp page.
 */
public class ExecutionServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// Retrieves the servlet context.
		ServletContext context = getServletContext();
		// Retrieves the scheduler.
		Scheduler scheduler = (Scheduler) context
				.getAttribute(Constants.SCHEDULER);
		// Retrieves the executors.
		TaskExecutor[] executors = scheduler.getExecutingTasks();
		// Registers the executors in the request.
		req.setAttribute("executors", executors);
		// Action requested?
		String action = req.getParameter("action");
		if ("pause".equals(action)) {
			String id = req.getParameter("id");
			TaskExecutor executor = find(executors, id);
			if (executor != null && executor.isAlive() && !executor.isStopped()
					&& executor.canBePaused() && !executor.isPaused()) {
				executor.pause();
			}
		} else if ("resume".equals(action)) {
			String id = req.getParameter("id");
			TaskExecutor executor = find(executors, id);
			if (executor != null && executor.isAlive() && !executor.isStopped()
					&& executor.canBePaused() && executor.isPaused()) {
				executor.resume();
			}
		} else if ("stop".equals(action)) {
			String id = req.getParameter("id");
			TaskExecutor executor = find(executors, id);
			if (executor != null && executor.isAlive()
					&& executor.canBeStopped() && !executor.isStopped()) {
				executor.stop();
			}
		}
		// Layout.
		String page = "/WEB-INF/ongoing.jsp";
		RequestDispatcher dispatcher = req.getRequestDispatcher(page);
		dispatcher.include(req, resp);
	}

	private TaskExecutor find(TaskExecutor[] executors, String id) {
		if (id == null) {
			return null;
		}
		for (int i = 0; i < executors.length; i++) {
			String aux = executors[i].getGuid();
			if (aux.equals(id)) {
				return executors[i];
			}
		}
		return null;
	}

}
