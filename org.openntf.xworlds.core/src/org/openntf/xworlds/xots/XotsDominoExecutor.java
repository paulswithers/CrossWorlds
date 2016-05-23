package org.openntf.xworlds.xots;

/*

<!--
Copyright 2016 Daniele Vistalli, Paul Withers
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and limitations under the License
-->

*/

import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openntf.domino.thread.DominoExecutor;
import org.openntf.domino.thread.IWrappedCallable;
import org.openntf.domino.thread.IWrappedRunnable;

/**
 * @author Paul Withers
 * @since 1.0.0
 * 
 *        Xots thread executor. ThreadPoolExecutor for Domino runnables. It sets
 *        up a shutdown hook for proper termination. There should be maximum one
 *        instance of DominoExecutor, otherwise concurrency won't work. Based on
 *        {@linkplain org.openntf.domino.xsp.xots.XotsDominoExecutor}
 *
 */
public class XotsDominoExecutor extends DominoExecutor {
	private static final Logger log_ = Logger.getLogger(XotsDominoExecutor.class.getName());

	/**
	 * @author Paul Withers
	 * @since 1.0.0
	 * 
	 *        Based on
	 *        {@linkplain org.openntf.domino.xsp.xots.XotsDominoExecutor.XotsWrappedCallable}
	 *        but we have no NSFComponentModule context
	 * 
	 */
	public static class XotsWrappedCallable<V> extends XotsWrappedTask implements IWrappedCallable<V> {

		/**
		 * Constructor, wrapping the Callable
		 * 
		 * @param wrappedObject
		 *            Callable to be wrapped
		 */
		public XotsWrappedCallable(final Callable<V> wrappedObject) {
			setWrappedTask(wrappedObject);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.concurrent.Callable#call()
		 */
		@SuppressWarnings("unchecked")
		@Override
		public V call() throws Exception {
			return (V) callOrRun();
		}
	}

	/**
	 * @author Paul Withers
	 * @since 1.0.0
	 * 
	 *        Based on
	 *        {@linkplain org.openntf.domino.xsp.xots.XotsDominoExecutor.XotsWrappedRunnable}
	 *        but we have no NSFComponentModule context
	 * 
	 */
	public static class XotsWrappedRunnable extends XotsWrappedTask implements IWrappedRunnable {

		/**
		 * Constructor, wrapping the Runnable
		 * 
		 * @param wrappedObject
		 *            Runnable to be wrapped
		 */
		public XotsWrappedRunnable(final Runnable wrappedObject) {
			setWrappedTask(wrappedObject);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			try {
				callOrRun();
			} catch (Exception e) {
				log_.log(Level.SEVERE, "Could not execute " + "/" + getWrappedTask().getClass(), e);
			}
		}
	}

	/**
	 * Loads the Xots executor threads, with a specific number of threads
	 * available
	 * 
	 * @param corePoolSize
	 *            int number of threads available (by default 10)
	 */
	public XotsDominoExecutor(final int corePoolSize) {
		super(corePoolSize, "Xots");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.thread.DominoExecutor#wrap(java.util.concurrent.
	 * Callable)
	 */
	@Override
	protected <V> IWrappedCallable<V> wrap(final Callable<V> inner) {
		if (inner instanceof IWrappedCallable)
			return (IWrappedCallable<V>) inner;

		if (inner instanceof XotsWrappedCallable<?>) {
			return new XotsWrappedCallable<V>(inner);
		}

		return new XotsWrappedCallable<V>(inner);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.domino.thread.DominoExecutor#wrap(java.lang.Runnable)
	 */
	@Override
	protected IWrappedRunnable wrap(final Runnable inner) {
		if (inner instanceof IWrappedRunnable)
			return (IWrappedRunnable) inner;

		if (inner instanceof XotsWrappedRunnable) {
			return new XotsWrappedRunnable(inner);
		}

		return new XotsWrappedRunnable(inner);
	}

}