package concurrent.lock.comporeblock;

import java.util.concurrent.locks.LockSupport;

/**
 * @author:chenxun
 * @0年月日 上午::
 * @Theme:
 * @Reference:http://www.cnblogs.com/skywang12345/p/3505784.html
 * @Descript:使用LockSupport工具类的方法 直接控制线程阻塞与非阻塞
 */
public class TestLockSupport {
	private static Thread mainThread;

	public static void main(String[] args) {

		ThreadA ta = new ThreadA("ta");
		// 获取主线程
		mainThread = Thread.currentThread();

		System.out.println(Thread.currentThread().getName() + " start ta");
		ta.start();

		System.out.println(Thread.currentThread().getName() + " block");
		// 主线程阻塞
		LockSupport.park(mainThread);

		System.out.println(Thread.currentThread().getName() + " continue");
	}

	static class ThreadA extends Thread {

		public ThreadA(String name) {
			super(name);
		}

		public void run() {
			System.out.println(Thread.currentThread().getName()+ " wakup others");
			// 唤醒“主线程”
			LockSupport.unpark(mainThread);
		}
	}
}