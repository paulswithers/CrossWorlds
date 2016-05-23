package org.openntf.xworlds.appservers.webapp.config;

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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Daniele Vistalli
 * @since 1.0.0
 * 
 *        Abstract implementation of {@link XWorldsApplicationConfigurator}.
 *        Only provides ServletContext property
 *
 */
public abstract class BaseXWorldsApplicationConfigurator implements XWorldsApplicationConfigurator {

	private static final String ERROR_DONT_USE_DIRECTLY = "This base application configurator shouldn't be used directly, verify the configuration.";
	private ServletContext appContext = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.xworlds.appservers.webapp.config.
	 * XWorldsApplicationConfigurator#configure(javax.servlet.ServletContext)
	 */
	@Override
	public void configure(ServletContext context) {
		throw new IllegalStateException(ERROR_DONT_USE_DIRECTLY);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.xworlds.appservers.webapp.config.
	 * XWorldsApplicationConfigurator#setupRequest(javax.servlet.http.
	 * HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void setupRequest(HttpServletRequest request, HttpServletResponse response) {
		throw new IllegalStateException(ERROR_DONT_USE_DIRECTLY);
	}

	/**
	 * Getter for current ServletContext
	 * 
	 * @return current ServletContext
	 */
	public ServletContext getAppContext() {
		return appContext;
	}

	/**
	 * Setter for ServletContext
	 * 
	 * @param appContext
	 *            current ServletContext
	 */
	public void setAppContext(ServletContext appContext) {
		this.appContext = appContext;
	}

}
