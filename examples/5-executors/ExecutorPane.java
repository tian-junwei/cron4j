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
import it.sauronsoftware.cron4j.TaskExecutor;
import it.sauronsoftware.cron4j.TaskExecutorListener;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 * This panel shows the progress of a {@link TaskExecutor} and let the user gain
 * control over it.
 * 
 */
public class ExecutorPane extends JPanel implements TaskExecutorListener {

	private static final long serialVersionUID = 1L;

	/**
	 * The task executor.
	 */
	private TaskExecutor executor;

	/**
	 * A progress bar for task completeness tracking.
	 */
	private JProgressBar progressBar = new JProgressBar(1, 1000);

	/**
	 * A label reporting the task current status.
	 */
	private JLabel statusLabel = new JLabel("N/A");

	/**
	 * Pause button.
	 */
	private JButton pauseButton = new JButton("Pause");

	/**
	 * Resume button.
	 */
	private JButton resumeButton = new JButton("Resume");

	/**
	 * Stop button.
	 */
	private JButton stopButton = new JButton("Stop");

	/**
	 * Constructor.
	 * 
	 * @param executor
	 *            The task executor.
	 */
	public ExecutorPane(final TaskExecutor executor) {
		this.executor = executor;
		executor.addTaskExecutorListener(this);
		// Components.
		if (!executor.supportsCompletenessTracking()) {
			progressBar.setIndeterminate(true);
		}
		pauseButton.setEnabled(executor.canBePaused());
		resumeButton.setEnabled(false);
		stopButton.setEnabled(executor.canBeStopped());
		pauseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				executor.pause();
			}
		});
		resumeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				executor.resume();
			}
		});
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				executor.stop();
			}
		});
		// GUI.
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(3, 3, 3, 3);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.WEST;
		c.weightx = 1;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 0;
		add(progressBar, c);
		c.weightx = 0;
		c.gridx = 1;
		add(pauseButton, c);
		c.gridx = 2;
		add(resumeButton, c);
		c.gridx = 3;
		add(stopButton, c);
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1;
		c.gridwidth = 4;
		add(statusLabel, c);
	}

	/**
	 * Returns the task executor.
	 * 
	 * @return The task executor.
	 */
	public TaskExecutor getTaskExecutor() {
		return executor;
	}

	public void completenessValueChanged(TaskExecutor executor,
			double completenessValue) {
		// Updates the progress bar.
		int n = (int) Math.round(1000 * completenessValue);
		progressBar.setValue(n);
	}

	public void statusMessageChanged(TaskExecutor executor, String statusMessage) {
		// Updates the status label.
		statusLabel.setText(statusMessage);
	}

	public void executionTerminated(TaskExecutor executor, Throwable exception) {
		// Updates GUI components.
		if (!executor.supportsCompletenessTracking()) {
			progressBar.setIndeterminate(false);
		}
		if (exception == null) {
			statusLabel.setText("COMPLETED");
		} else {
			statusLabel.setText("FAILED");
		}
	}

	public void executionPausing(TaskExecutor executor) {
		pauseButton.setEnabled(false);
		resumeButton.setEnabled(executor.canBePaused());
	}

	public void executionResuming(TaskExecutor executor) {
		pauseButton.setEnabled(executor.canBePaused());
		resumeButton.setEnabled(false);
	}

	public void executionStopping(TaskExecutor executor) {
		pauseButton.setEnabled(false);
		resumeButton.setEnabled(false);
		stopButton.setEnabled(false);
	}

}
