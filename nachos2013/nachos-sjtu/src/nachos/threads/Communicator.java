package nachos.threads;

import nachos.machine.Machine;

/**
 * A <i>communicator</i> allows threads to synchronously exchange 32-bit
 * messages. Multiple threads can be waiting to <i>speak</i>, and multiple
 * threads can be waiting to <i>listen</i>. But there should never be a time
 * when both a speaker and a listener are waiting, because the two threads can
 * be paired off at this point.
 */
public class Communicator {
	/**
	 * Allocate a new communicator.
	 */
	public Communicator() {
		lock = new Lock();
		speaker = new ConditionQueue(lock);
		listener = new ConditionQueue(lock);
		transfer = new ConditionQueue(lock);
		transferWord = 0;
		transferFlag = false;
	}

	/**
	 * Wait for a thread to listen through this communicator, and then transfer
	 * <i>word</i> to the listener.
	 * 
	 * <p>
	 * Does not return until this thread is paired up with a listening thread.
	 * Exactly one listener should receive <i>word</i>.
	 * 
	 * @param word
	 *            the integer to transfer.
	 */
	public void speak(int word) {
		lock.acquire();
		if (transferFlag || listener.empty())
			speaker.sleep();
		transferFlag = true;
		transferWord = word;
		listener.wake();
		transfer.sleep();
		transferFlag = false;
		if (!speaker.empty() && !listener.empty()) {
			transferFlag = true;
			speaker.wake();
		}
		lock.release();
	}

	/**
	 * Wait for a thread to speak through this communicator, and then return the
	 * <i>word</i> that thread passed to <tt>speak()</tt>.
	 * 
	 * @return the integer transferred.
	 */
	public int listen() {
		lock.acquire();
		if (!transferFlag && !speaker.empty()) {
			transferFlag = true;
			speaker.wake();
		}
		listener.sleep();
		int ret = transferWord;
		transfer.wake();
		lock.release();
		return (ret);
	}

	boolean transferFlag;
	int transferWord;
	Lock lock;
	ConditionQueue speaker, listener, transfer;

	class ConditionQueue {
		Condition2 cond;
		int cnt;

		ConditionQueue(Lock lock) {
			cnt = 0;
			cond = new Condition2(lock);
		}

		boolean empty() {
			return (cnt == 0);
		}

		void sleep() {
			cnt++;
			cond.sleep();
		}

		void wake() {
			cnt--;
			cond.wake();
		}
	}
}
