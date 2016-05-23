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

import java.util.Dictionary;
import java.util.Hashtable;

import org.openntf.domino.utils.Factory;
import org.openntf.xworlds.appservers.lifecycle.XWorldsManager;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

/**
 * @author Daniele Vistalli
 * @since 1.0.0
 * 
 *        Defines the OSGi commands made available for XWorlds from OSGi
 *        console. Commands available are:
 *        <ul>
 *        <li>xworlds report</li>
 *        <li>xworlds counters</li>
 *        <li>xworlds install</li>
 *        </ul>
 *
 */
public class Command {

	static final Dictionary<String, Object> PROPERTIES = new Hashtable<String, Object>();

	static {
		// define the command scope as "jndi:"
		PROPERTIES.put("osgi.command.scope", "xworlds");
		// define the command names (which map to methods)
		PROPERTIES.put("osgi.command.function", "report counters install".split(" "));
	}

	private BundleContext context;

	/**
	 * Dumps out the XWorlds Manager report to the console
	 */
	public void report() {
		System.out.println("Reporting about the status of the XWorlds Server");
		System.out.println();

		XWorldsManager xwm = XWorldsManager.getInstance();

		System.out.println(xwm.getXWorldsReportAsString());

	}

	/**
	 * Enables or disables the ODA counters per thread. Default ODA counters as
	 * of 2.5.0 are:
	 * <ul>
	 * <li>Lotus objects accessed</li>
	 * <li>recycle errors</li>
	 * <li>automatic recycles processed</li>
	 * <li>manual recycles processed</li>
	 * </ul>
	 * Current counters can be dumped out by calling
	 * {@linkplain Factory#dumpCounters(boolean)}
	 * 
	 * @param enable
	 *            boolean whether or not to enable the counters
	 * @param perThread
	 *            boolean whether or not counters are enabled globally or on a
	 *            thread-by-thread basis
	 */
	public void counters(boolean enable, boolean perThread) {
		System.out.println("Setting ODA counters");
		System.out.println("Enabled: " + enable);
		System.out.println("Count per thread: " + perThread);
		Factory.enableCounters(enable, perThread);
	}

	/**
	 * Allows a bundle to be manually installed. This does not appear to be used
	 * currently
	 * 
	 * @param packageToInstall
	 *            String name of bundle to install
	 */
	public void install(String packageToInstall) {
		System.out.println("Installing: " + packageToInstall);
		try {
			context.installBundle(packageToInstall);
		} catch (BundleException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the bundle context, required for installing packages
	 * 
	 * @param context
	 *            BundleContext in which the commands
	 */
	public void setContext(BundleContext context) {
		this.context = context;
	}

}