package org.openntf.xworlds.core;

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

import org.openntf.domino.utils.Factory;
import org.openntf.xworlds.appservers.lifecycle.XWorldsManager;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * @author Daniele Vistalli
 * @since 1.0.0
 * 
 *        Activator for the XWorlds feature. Also registers XWorlds commands
 *
 */
public class Activator implements BundleActivator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.
	 * BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		Factory.println("XWorlds", "XWorlds Server bundle starting v. " + context.getBundle().getVersion());

		Command xworldsAdminCmd = new Command();
		xworldsAdminCmd.setContext(context);

		context.registerService("java.lang.Object", xworldsAdminCmd, Command.PROPERTIES);

		XWorldsManager.getInstance().Startup();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		Factory.println("XWorlds", "XWorlds Server bundle stopping");

		XWorldsManager.getInstance().Shutdown();

	}
}