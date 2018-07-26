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
import it.sauronsoftware.cron4j.Scheduler;
import it.sauronsoftware.cron4j.SchedulerListener;
import it.sauronsoftware.cron4j.TaskExecutor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * A GUI providing controls over the scheduler and its executors.
 */
public class ControlFrame extends JFrame implements SchedulerListener {

	private static final long serialVersionUID = 1L;

	/**
	 * The scheduler.
	 */
	private Scheduler scheduler = new Scheduler();

	/**
	 * A list of alive {@link ExecutorPane} objects.
	 */
	private ArrayList executorPanes = new ArrayList();

	/**
	 * This container shows any ongoing {@link ExecutorPane} object.
	 */
	private JPanel ongoingExecutorPanes = new JPanel();

	/**
	 * Constructor.
	 */
	public ControlFrame() {
		// Add the listener.
		scheduler.addSchedulerListener(this);
		// Schedules the task.
		scheduler.schedule("* * * * *", new MyTask());
		// Builds the GUI.
		setTitle("cron4j example");
		setContentPane(buildContentPane());
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	/**
	 * Builds the frame main panel.
	 * 
	 * @return The frame mail panel.
	 */
	private JPanel buildContentPane() {
		JPanel pane = new JPanel();
		pane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		pane.setLayout(new BorderLayout(5, 5));
		pane.add(buildSchedulerPanel(), BorderLayout.NORTH);
		pane.add(buildExecutorsPanel(), BorderLayout.CENTER);
		return pane;
	}

	/**
	 * Builds the panel which contains the scheduler control commands.
	 * 
	 * @return The scheduler control commands panel.
	 */
	private JPanel buildSchedulerPanel() {
		final JButton start = new JButton("Start");
		final JButton stop = new JButton("Stop");
		start.setEnabled(true);
		stop.setEnabled(false);
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scheduler.start();
				start.setEnabled(false);
				stop.setEnabled(true);
			}
		});
		stop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scheduler.stop();
				start.setEnabled(true);
				stop.setEnabled(false);
			}
		});
		JPanel pane = new JPanel();
		pane.setBorder(BorderFactory.createTitledBorder("Scheduler"));
		pane.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
		pane.add(start);
		pane.add(stop);
		return pane;
	}

	/**
	 * Builds the panel with the executors.
	 * 
	 * @return The panel with the executors.
	 */
	private JPanel buildExecutorsPanel() {
		BoxLayout l = new BoxLayout(ongoingExecutorPanes, BoxLayout.Y_AXIS);
		ongoingExecutorPanes.setLayout(l);
		JScrollPane scrollPane = new JScrollPane(ongoingExecutorPanes);
		scrollPane.setPreferredSize(new Dimension(500, 400));
		JPanel pane = new JPanel();
		pane.setBorder(BorderFactory.createTitledBorder("Ongoing"));
		pane.setLayout(new BorderLayout(5, 5));
		pane.add(scrollPane, BorderLayout.CENTER);
		return pane;
	}

	public void taskLaunching(TaskExecutor executor) {
		ExecutorPane pane = new ExecutorPane(executor);
		synchronized (executorPanes) {
			executorPanes.add(pane);
			Dimension pref = pane.getPreferredSize();
			Dimension max = new Dimension(Short.MAX_VALUE, pref.height);
			pane.setMaximumSize(max);
			ongoingExecutorPanes.add(pane);
			ongoingExecutorPanes.repaint();
		}
	}

	public void taskSucceeded(TaskExecutor executor) {
		removeExecutorPane(executor, 2000);
	}

	public void taskFailed(TaskExecutor executor, Throwable exception) {
		removeExecutorPane(executor, 2000);
	}

	private void removeExecutorPane(TaskExecutor executor, final long delay) {
		ExecutorPane pane = null;
		synchronized (executorPanes) {
			for (Iterator i = executorPanes.iterator(); i.hasNext();) {
				ExecutorPane ep = (ExecutorPane) i.next();
				if (ep.getTaskExecutor() == executor) {
					executorPanes.remove(ep);
					pane = ep;
					break;
				}
			}
		}
		if (pane != null) {
			final ExecutorPane ep = pane;
			Runnable r = new Runnable() {
				public void run() {
					try {
						Thread.sleep(delay);
					} catch (InterruptedException e) {
						;
					}
					ongoingExecutorPanes.remove(ep);
					ongoingExecutorPanes.validate();
				}
			};
			Thread t = new Thread(r);
			t.setDaemon(true);
			t.start();
		}
	}

}
