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

import java.lang.annotation.AnnotationTypeMismatchException;
import java.util.concurrent.Callable;

import org.openntf.domino.Session;
import org.openntf.domino.session.INamedSessionFactory;
import org.openntf.domino.session.ISessionFactory;
import org.openntf.domino.thread.AbstractWrappedTask;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.xots.Tasklet;
import org.openntf.xworlds.appservers.lifecycle.XWorldsManagedThread;

import com.ibm.domino.napi.NException;
import com.ibm.domino.napi.c.NotesUtil;
import com.ibm.domino.napi.c.xsp.XSPNative;

import lotus.domino.NotesException;
import lotus.domino.NotesThread;

/**
 * @author Paul Withers
 * @since 1.0.0
 * 
 *        Extension of {@linkplain org.openntf.domino.xsp.xots.XotsWrappedTask}
 *
 */
public class XotsWrappedTask extends AbstractWrappedTask {
	private Object wrappedTask;
	private String dominoFullName;
	private SessionType sessType;

	private class XotsNamedSessionFactory implements ISessionFactory, INamedSessionFactory {
		private static final long serialVersionUID = 1L;
		private boolean _isFullAccess = false;
		private String _dominoFullName;

		public XotsNamedSessionFactory(boolean fullAccess, String dominoFullName) {
			this._isFullAccess = fullAccess;
			this._dominoFullName = dominoFullName;
		}

		@Override
		public Session createSession() {

			return createSession(_dominoFullName);

		}

		@Override
		public Session createSession(String username) {
			try {

				final long userHandle = NotesUtil.createUserNameList(username);
				lotus.domino.Session rawSession = XSPNative.createXPageSessionExt(username, userHandle, false, true,
						_isFullAccess);
				Session sess = Factory.fromLotus(rawSession, Session.SCHEMA, null);
				sess.setNoRecycle(false);
				return sess;
			} catch (NException e) {
				throw new RuntimeException(e);
			} catch (NotesException e) {
				throw new RuntimeException(e);
			}
		}
	};

	/**
	 * Common code for the wrappers
	 * 
	 * @return
	 */
	@Override
	protected Object callOrRun() throws Exception {

		try {
			XWorldsManagedThread.setupAsDominoThread(null);
			try {
				Object wrappedTask = getWrappedTask();

				Factory.setSessionFactory(new XotsNamedSessionFactory(false, dominoFullName), sessType);
				sessionFactory = Factory.getSessionFactory(SessionType.CURRENT);

				if (wrappedTask instanceof Callable) {
					return ((Callable<?>) wrappedTask).call();
				} else {
					((Runnable) wrappedTask).run();
					return null;
				}
			} catch (Exception e) {
				DominoUtils.handleException(e);
				return null;
			}
		} finally {
			XWorldsManagedThread.shutdownDominoThread();
		}
	}

	/**
	 * Returns the wrapped task
	 * 
	 * @return
	 */
	@Override
	protected synchronized Object getWrappedTask() {
		return wrappedTask;
	}

	/**
	 * Determines the sessionType, scope etc under which the current runnable
	 * should run. Based on same method in {@linkplain AbstractWrappedTask}
	 * 
	 * @param task
	 *            Callable or Runnable from which to extract settings. Should
	 *            extend Callable (or AbstractOpenLogXotsCallable, to use
	 *            OpenLog) or Runnable (or AbstractOpenLogXotsRunnable, to use
	 *            OpenLog). Recommendation, though, is not to use OpenLog but
	 *            use a Logger such as Logback.
	 */
	@Override
	protected synchronized void setWrappedTask(final Object task) {
		wrappedTask = task;
		if (task == null)
			return;
		// some security checks...
		if (task instanceof NotesThread) {
			// RPr: I'm not sure if this should be allowed anyway...
			throw new IllegalArgumentException(
					"Cannot wrap the NotesThread " + task.getClass().getName() + " into a DominoRunner");
		}
		// if (task instanceof DominoFutureTask) {
		// // RPr: don't know if this is possible
		// throw new IllegalArgumentException("Cannot wrap the WrappedCallable "
		// + task.getClass().getName() + " into a DominoRunner");
		// }
		if (task instanceof AbstractWrappedTask) {
			// RPr: don't know if this is possible
			throw new IllegalArgumentException(
					"Cannot wrap the WrappedCallable " + task.getClass().getName() + " into a DominoRunner");
		}

		if (task instanceof Tasklet.Interface) {
			Tasklet.Interface dominoRunnable = (Tasklet.Interface) task;
			sessionFactory = dominoRunnable.getSessionFactory();
			scope = dominoRunnable.getScope();
			context = dominoRunnable.getContext();
			sourceThreadConfig = dominoRunnable.getThreadConfig();
		}
		Tasklet annot = task.getClass().getAnnotation(Tasklet.class);

		if (annot != null) {
			if (sessionFactory == null) {
				switch (annot.session()) {
				case CLONE:
					sessType = SessionType.CURRENT;
					dominoFullName = Factory.getSession(SessionType.CURRENT).getEffectiveUserName();
					break;
				case CLONE_FULL_ACCESS:
					sessType = SessionType.CURRENT_FULL_ACCESS;
					dominoFullName = Factory.getSession(SessionType.CURRENT).getEffectiveUserName();
					break;

				case FULL_ACCESS:
					sessType = SessionType.FULL_ACCESS;
					dominoFullName = Factory.getSession(SessionType.SIGNER).getEffectiveUserName();
					break;

				case NATIVE:
					sessType = SessionType.NATIVE;
					dominoFullName = Factory.getSession(SessionType.NATIVE).getEffectiveUserName();
					break;

				case NONE:
					sessionFactory = null;
					break;

				case SIGNER:
					sessType = SessionType.SIGNER;
					dominoFullName = Factory.getSession(SessionType.SIGNER).getEffectiveUserName();
					break;

				case SIGNER_FULL_ACCESS:
					sessType = SessionType.SIGNER_FULL_ACCESS;
					dominoFullName = Factory.getSession(SessionType.SIGNER).getEffectiveUserName();
					break;

				case TRUSTED:
					throw new IllegalStateException("SessionType.TRUSTED is not supported");

				default:
					break;
				}
			}

			if (context == null) {
				context = annot.context();
			}
			if (context == null) {
				context = Tasklet.Context.DEFAULT;
			} else {
				if (context.equals(Tasklet.Context.XSPSCOPED) || context.equals(Tasklet.Context.XSPFORCE)
						|| context.equals(Tasklet.Context.XSPBARE)) {
					throw new AnnotationTypeMismatchException(null, "XSP contexts are not allowed");
				}
			}

			if (scope == null) {
				scope = annot.scope();
			}
			if (scope == null) {
				scope = Tasklet.Scope.NONE;
			}
			if (sourceThreadConfig == null) {
				switch (annot.threadConfig()) {
				case CLONE:
					sourceThreadConfig = Factory.getThreadConfig();
					break;
				case PERMISSIVE:
					sourceThreadConfig = Factory.PERMISSIVE_THREAD_CONFIG;
					break;
				case STRICT:
					sourceThreadConfig = Factory.STRICT_THREAD_CONFIG;
					break;
				}
			}
		}
		if (sourceThreadConfig == null)
			sourceThreadConfig = Factory.getThreadConfig();
	}
}