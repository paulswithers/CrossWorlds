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

import javax.servlet.ServletContext;

import org.openntf.domino.xots.XotsContext;
import org.openntf.xworlds.core.ServletOpenLogItem;

/**
 * @author Paul Withers, Daniele Vistalli
 * @since 1.0.0
 * 
 *        Xots-related utility methods
 *
 */
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
