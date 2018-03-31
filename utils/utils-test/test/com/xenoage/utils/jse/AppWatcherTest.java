package com.xenoage.utils.jse;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.UUID;

import org.junit.After;
import org.junit.Test;

import com.xenoage.utils.jse.AppWatcher;

/**
 * Test cases for the AppWatcher class.
 * 
 * @author Andreas Wenger
 */
public class AppWatcherTest {

	private String appID = "test";


	/**
	 * Test 1. Call alive and dead methods, no time-out.
	 */
	@Test public void test1() {
		cleanUp();
		AppWatcher watcher = AppWatcher.getInstance();
		watcher.setTimeObsolete(5);
		//create two instances
		UUID instance1 = UUID.randomUUID();
		watcher.alive(appID, instance1);
		UUID instance2 = UUID.randomUUID();
		watcher.alive(appID, instance2);
		//app must be alive
		assertTrue(watcher.isAppAlive(appID));
		//close instance2. app must still be alive.
		watcher.dead(appID, instance2);
		assertTrue(watcher.isAppAlive(appID));
		//close instance1. app must be dead now.
		watcher.dead(appID, instance1);
		assertFalse(watcher.isAppAlive(appID));
	}

	/**
	 * Test 2. Call alive method and test time-out.
	 */
	@Test public void test2() {
		cleanUp();
		AppWatcher watcher = AppWatcher.getInstance();
		watcher.setTimeObsolete(2);
		//create one instances
		UUID instance1 = UUID.randomUUID();
		watcher.alive(appID, instance1);
		//app must be alive
		assertTrue(watcher.isAppAlive(appID));
		//wait 2.1 seconds. app must be dead.
		try {
			Thread.sleep(2100);
			assertFalse(watcher.isAppAlive(appID));
		} catch (InterruptedException ex) {
		}
	}

	/**
	 * Clean up the files.
	 */
	@After public void cleanUp() {
		new File("data/running/" + appID).delete();
	}

}
