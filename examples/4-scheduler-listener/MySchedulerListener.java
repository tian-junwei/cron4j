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
import it.sauronsoftware.cron4j.SchedulerListener;
import it.sauronsoftware.cron4j.TaskExecutor;

/**
 * A very simple SchedulerListener, sending messages to the console.
 */
public class MySchedulerListener implements SchedulerListener {

	public void taskLaunching(TaskExecutor executor) {
		System.out.println("Task launched!");
	}

	public void taskSucceeded(TaskExecutor executor) {
		System.out.println("Task completed!");
	}

	public void taskFailed(TaskExecutor executor, Throwable exception) {
		System.out.println("Task failed due to an exception!");
		exception.printStackTrace();
	}

}
