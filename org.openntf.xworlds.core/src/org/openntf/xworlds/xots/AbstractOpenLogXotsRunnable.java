package org.openntf.xworlds.xots;

import javax.servlet.ServletContext;

import org.openntf.domino.xots.AbstractXotsRunnable;

/**
 * @author Paul Withers
 * @since 1.0.0
 * 
 *        Extension of AbstractXotsCallable, used for logging to OpenLog. If you
 *        don't want to log to OpenLog, just extend Callable
 *
 */
public abstract class AbstractOpenLogXotsRunnable extends AbstractXotsRunnable {

	public AbstractOpenLogXotsRunnable(ServletContext ctx) {
		setContext(XotsUtil.initialiseXotsContextFromServlet(ctx));
	}

}
