package org.openntf.xworlds.appservers.webapp.security;

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

import javax.servlet.http.HttpServletRequest;

import org.openntf.xworlds.appservers.webapp.config.DefaultXWorldsApplicationConfig;

/**
 * @author Daniele Vistalli
 * @since 1.0.0
 * 
 *        XWorlds SecurityManager, for managing the current Domino name used for
 *        the session
 *
 */
public class SecurityManager {

	public static final String ANON_FULLNAME = "Anonymous";

	/**
	 * Sets the "xworlds.request.username" property in the HttpSession to a name
	 * or "Anonymous"
	 * 
	 * @param request
	 *            current HttpServletRequest
	 * @param fullName
	 *            String name to set as the current username or
	 *            null/ANON_FULLNAME
	 */
	public static void setDominoFullName(HttpServletRequest request, String fullName) {

		if (fullName == null || fullName.equals(ANON_FULLNAME)) {
			request.getSession().removeAttribute("xworlds.request.username");
			DefaultXWorldsApplicationConfig.setDominoFullName(ANON_FULLNAME);
		} else {
			request.getSession().setAttribute("xworlds.request.username", fullName);
			DefaultXWorldsApplicationConfig.setDominoFullName(fullName);
		}

	}

	/**
	 * Gets the full Notes Name for the current user from the HttpSession or
	 * "Anonymous". The name is stored in the HttpSession's
	 * "xworlds.request.username" property.
	 * 
	 * @param request
	 *            current HttpServletRequest
	 * @return String current user's Domino name
	 */
	public static String getDominoFullName(HttpServletRequest request) {
		if (request.getSession(false) != null
				&& request.getSession(false).getAttribute("xworlds.request.username") != null) {
			return (String) request.getSession().getAttribute("xworlds.request.username");
		}
		return ANON_FULLNAME;
	}
}
