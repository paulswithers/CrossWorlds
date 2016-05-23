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

/**
 * @author Daniele Vistalli
 * @since 1.0.0
 * 
 *        Interface defining methods to determine whether the application is
 *        running in developerMode and methods for retrieving default
 *        development user and application "signer"
 *
 */
public interface XWorldsApplicationConfiguration {

	/**
	 * @return the Notes full name for the user that will be treated as the
	 *         signer for this application. May return null to indicate no
	 *         signer has been specified. In that case the server/client
	 *         identity is used.
	 */
	public String getAppSignerFullName();

	/**
	 * CrossWorlds can run in developer mode allowing identity switching to
	 * simplify development & testing on a client based setup.
	 * 
	 * Developer mode is controlled by adding
	 * <code>xworlds.developermode=true</code> to bootstrap.properties of server
	 * 
	 * @return true if CrossWorlds is running in developer mode.
	 */
	public boolean isDeveloperMode();

	/**
	 * Gets the default developer full Notes name, as defined in the
	 * application's web.xml using
	 * <code>org.openntf.crossworlds.devtimename</code>
	 * 
	 * @return
	 */
	public String getDefaultDevelopmentUserName();

}
