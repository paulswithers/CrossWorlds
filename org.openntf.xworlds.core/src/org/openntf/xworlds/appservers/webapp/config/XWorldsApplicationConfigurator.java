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
 *        Interface for managing interaction with the current ServletContext
 *
 */
public interface XWorldsApplicationConfigurator {

	/**
	 * This constant is the name of the ServletContext attribute holding the
	 * application configuration.
	 */
	public static final String CONTEXTPARAM_CWAPPCONFIG_CLASS = "org.openntf.crossworlds.appconfigurator.class";
	/**
	 * The notes full name for the identity to be used when a "SIGNER" session
	 * is required.
	 */
	public static final String CONTEXTPARAM_CWAPPSIGNER_IDENTITY = "org.openntf.crossworlds.appsignername";
	/**
	 * The notes full name for the identity to be used when a "SIGNER" session
	 * is required.
	 */
	public static final String CONTEXTPARAM_CWDEFAULTDEVELOPER_IDENTITY = "org.openntf.crossworlds.devtimename";
	/**
	 * This constant is the name of the ServletContext attribute holding the
	 * application configuration.
	 */
	public static final String APPCONTEXT_ATTRS_CWAPPCONFIG = "org.openntf.crossworlds.appconfig";

	/**
	 * Configures the configuration for the current application, based on server
	 * settings and application settings (managed via the
	 * javax.servlet.ServletContext
	 * 
	 * @param context
	 *            current ServletContext
	 */
	public void configure(ServletContext context);

	/**
	 * Registers the implementation of this class to the current ServletContext,
	 * using {@linkplain #APPCONTEXT_ATTRS_CWAPPCONFIG}
	 */
	public XWorldsApplicationConfiguration build();

	/**
	 * This initialises the ODA Factory sessions based on authentication and
	 * sets Domino full name
	 * 
	 * @param request
	 *            current HttpServletRequest
	 * @param response
	 *            current HttpServletResponse
	 */
	public void setupRequest(HttpServletRequest request, HttpServletResponse response);

}
