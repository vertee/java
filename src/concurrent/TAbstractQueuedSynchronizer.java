package concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/** 
 *
 * @author chenxun
 * @date 2016年12月22日 下午3:54:32 
 *
 */
public class TAbstractQueuedSynchronizer implements Lock {
	
	
	@SuppressWarnings("serial")
	private  class Sync extends AbstractQueuedSynchronizer{
		public Sync(int limit){
			super.setState(limit);
		};
		
		public int tryAcquireShared(int reduceCount) {

			for (;;) {
				int current = getState();
				int newCount = current - reduceCount;
				if (newCount < 0 || compareAndSetState(current, newCount)) {
					return newCount;
				}
			}
		}

		public boolean tryReleaseShared(int returnCount) {

			for (;;) {
				int current = getState();
				int newCount = current + returnCount;
				if (compareAndSetState(current, newCount)) {
					return true;
				}
			}
		}
	}
	
	private Sync sync = new Sync(2);
	
	@Override
	public void lock() {
		sync.acquireShared(1);//acquire()
	}
	
	@Override
	public void unlock() {
		sync.releaseShared(1);//release()
	}
	
	@Override
	public void lockInterruptibly() throws InterruptedException {
		sync.acquireSharedInterruptibly(1);
	}
	@Override
	public boolean tryLock() {
		return sync.tryAcquireShared(1) >= 0;
	}
	@Override
	public boolean tryLock(long time, TimeUnit unit)
			throws InterruptedException {
		return sync.tryAcquireSharedNanos(1, unit.toNanos(time));
	}
	
	@Override
	public Condition newCondition() {
		// TODO Auto-generated method stub
		return null;
	}
   
	public static void main(String[] args) throws InterruptedException {
		final TAbstractQueuedSynchronizer lock = new TAbstractQueuedSynchronizer();
		ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(11);
		for (int i = 0; i < 10; i++) {
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
						lock.lock();
						System.out.format("%s====%n",Thread.currentThread().getName());
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
						}finally{
						lock.unlock();
						}
				}
			};
			newFixedThreadPool.submit(runnable);
		}
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(2000);
						System.out.format("%n");
					} catch (InterruptedException e) {
					}
				}
			}
		};
		newFixedThreadPool.execute(runnable);
		
		
		
		Thread.sleep(12000);
		System.exit(0);
	}
   
}
