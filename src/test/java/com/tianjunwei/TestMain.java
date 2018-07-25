package com.tianjunwei;

import it.sauronsoftware.cron4j.Scheduler;

public class TestMain {

	public static void main(String[] args) {
		
		// Creates a Scheduler instance.
		Scheduler s = new Scheduler();
		// Schedule a once-a-minute task.
		
		s.schedule("*/30 * * * * *", new Runnable() {
					int i = 0;
					public void run() {
						i++;
						System.out.println("Another minute ticked away..."+System.currentTimeMillis()/1000);
					}
				});
		// Starts the scheduler.
		s.start();
		// Will run for ten minutes.
		try {
			Thread.sleep(1000L * 60L * 10L);
		} catch (InterruptedException e) {
			;
		}
		// Stops the scheduler.
		s.stop();

	}

}
