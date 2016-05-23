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

import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.openntf.domino.utils.Factory;
import org.openntf.xworlds.appservers.lifecycle.XWorldsManager;
import org.openntf.xworlds.appservers.webapp.config.DefaultXWorldsApplicationConfig;
import org.openntf.xworlds.appservers.webapp.config.XWorldsApplicationConfigurator;

/**
 * @author Daniele Vistalli
 * @since 1.0.0
 * 
 *        Application Lifecycle Listener implementation class
 *        XWorldsApplicationListener This is added to the application in the
 *        web.xml or via the j2eeenabler web fragment, to register the
 *        application as using XWorlds in the XWorldsManager.
 *
 */
public class XWorldsApplicationListener implements ServletContextListener {

	static final java.util.logging.Logger log = Logger.getLogger(XWorldsApplicationListener.class.getName());

	lotus.domino.NotesThread NotesController = null;

	/**
	 * Default constructor.
	 */
	public XWorldsApplicationListener() {
		Factory.println("XWorlds:AppListener", "Starting Application Listener");
	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent appEvent) {

		XWorldsManager xwm = XWorldsManager.getInstance();

		// Configure XWorlds for this application

		String appConfiguratorClass = null;
		XWorldsApplicationConfigurator configurator = null;
		if ((appConfiguratorClass = appEvent.getServletContext()
				.getInitParameter(XWorldsApplicationConfigurator.CONTEXTPARAM_CWAPPCONFIG_CLASS)) != null) {
			try {
				configurator = (XWorldsApplicationConfigurator) Class.forName(appConfiguratorClass).newInstance();
			} catch (IllegalAccessException e) {
				throw new IllegalStateException("Cannot configure the XWorlds application", e);
			} catch (InstantiationException e) {
				throw new IllegalStateException("Cannot configure the XWorlds application", e);
			} catch (ClassNotFoundException e) {
				throw new IllegalStateException("Cannot configure the XWorlds application", e);
			}
		} else {
			configurator = new DefaultXWorldsApplicationConfig();
		}

		configurator.configure(appEvent.getServletContext());
		configurator.build();

		xwm.addApplication(appEvent.getServletContext().getServletContextName(),
				appEvent.getServletContext().getContextPath(), appEvent.getServletContext().getMajorVersion(),
				appEvent.getServletContext().getMinorVersion());

	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent appEvent) {

		XWorldsManager xwm = XWorldsManager.getInstance();

		xwm.removeApplication(appEvent.getServletContext().getServletContextName(),
				appEvent.getServletContext().getContextPath(), appEvent.getServletContext().getMajorVersion(),
				appEvent.getServletContext().getMinorVersion());

	}

}
