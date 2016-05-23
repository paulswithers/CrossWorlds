package org.openntf.xworlds.appservers.webapp;

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

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openntf.xworlds.appservers.lifecycle.XWorldsManagedThread;
import org.openntf.xworlds.appservers.webapp.config.XWorldsApplicationConfigurator;

/**
 * @author Daniele Vistalli
 * @since 1.0.0
 * 
 *        Default Servlet Filter implementation fior XWorlds This is added to
 *        the application in the web.xml or via the j2eeenabler web fragment, to
 *        route all HTTP requests
 */

public class XWorldsRequestsFilter implements Filter {

	// static final XLogger log =
	// XLoggerFactory.getXLogger(XWorldsRequestsFilter.class);

	/**
	 * Default constructor.
	 */
	public XWorldsRequestsFilter() {
	}

	/**
	 * @see Filter#destroy()
	 */
	@Override
	public void destroy() {
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		try {

			XWorldsManagedThread.setupAsDominoThread((HttpServletRequest) request);
			XWorldsApplicationConfigurator configurator = (XWorldsApplicationConfigurator) request.getServletContext()
					.getAttribute(XWorldsApplicationConfigurator.APPCONTEXT_ATTRS_CWAPPCONFIG);
			if (configurator != null) {
				configurator.setupRequest((HttpServletRequest) request, (HttpServletResponse) response);
			}

			chain.doFilter(request, response);

		} finally {
			XWorldsManagedThread.shutdownDominoThread();
		}

	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	@Override
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
