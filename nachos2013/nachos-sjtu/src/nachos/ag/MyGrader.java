package nachos.ag;

import nachos.machine.Machine;
import nachos.threads.KThread;

public class MyGrader extends BasicTestGrader {
	void run() {
		hehe = 10;
		for (int i = 0; i < 10; i++)
			(new KThread(new PingTest(i))).fork();
		while (hehe > 0) {
			System.err.println("!!!");
			KThread.yield();
		}
		done();
	}

	private class PingTest implements Runnable {
		PingTest(int which) {
			this.which = which;
		}

		public void run() {
			for (int i = 0; i < 100; i++) {
				System.err.println(i + " " + which + " "
						+ Machine.interrupt().enabled());
				System.err.flush();
			}
			hehe--;
		}

		private int which;
	}

	static int hehe = 0;
}
