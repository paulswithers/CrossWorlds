package org.openntf.xworlds.xots;

import javax.servlet.ServletContext;

import org.openntf.domino.xots.XotsContext;
import org.openntf.xworlds.core.ServletOpenLogItem;

public enum XotsUtil {
	;

	/**
	 * Handle an exception, passing a XotsContext
	 * 
	 * As a recommendation of best practice, Java developers should use Java
	 * logging. XPages developers should get familiar with Java logging.
	 * 
	 * @param t
	 *            Throwable
	 * @param xotsContext
	 *            XotsContext from which to get Log database and current
	 *            database path
	 */
	@Deprecated
	public static void handleException(final Throwable t, final XotsContext xotsContext) {
		getOpenLogItem(xotsContext).logError(t);
	}

	/**
	 * Gets the OpenLogItem for more complex logging, passing a XotsContext
	 * 
	 * As a recommendation of best practice, Java developers should use Java
	 * logging. XPages developers should get familiar with Java logging.
	 * 
	 * @param xotsContext
	 *            XotsContext from which to get Log database and current
	 *            database path
	 */
	@Deprecated
	public static ServletOpenLogItem getOpenLogItem(final XotsContext xotsContext) {
		return new ServletOpenLogItem(xotsContext);
	}

	/**
	 * Handle an exception, passing a ServletContext
	 * 
	 * As a recommendation of best practice, Java developers should use Java
	 * logging. XPages developers should get familiar with Java logging.
	 * 
	 * @param t
	 *            Throwable
	 * @param servletContext
	 *            ServletContext from which to get Log database and current
	 *            database path
	 */
	@Deprecated
	public static void handleException(final Throwable t, final ServletContext servletContext) {
		getOpenLogItem(servletContext).logError(t);
	}

	/**
	 * Gets the OpenLogItem for more complex logging, passing a ServletContext
	 * 
	 * As a recommendation of best practice, Java developers should use Java
	 * logging. XPages developers should get familiar with Java logging.
	 * 
	 * @param servletContext
	 *            ServletContext from which to get Log database and current
	 *            database path
	 */
	@Deprecated
	public static ServletOpenLogItem getOpenLogItem(final ServletContext servletContext) {
		return new ServletOpenLogItem(servletContext);
	}

	public static XotsContext initialiseXotsContextFromServlet(ServletContext ctx) {
		XotsContext context = new XotsContext();
		String fullContextPath = ctx.getContextPath();
		context.setContextApiPath(fullContextPath.substring(1));
		if (null == ctx.getInitParameter("xsp.openlog.filepath")) {
			context.setOpenLogApiPath("openlog.nsf");
		} else {
			context.setOpenLogApiPath(ctx.getInitParameter("xsp.openlog.filepath"));
		}
		return context;
	}

}
