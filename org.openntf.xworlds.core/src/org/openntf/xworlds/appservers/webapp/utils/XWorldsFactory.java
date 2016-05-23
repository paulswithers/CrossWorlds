package org.openntf.xworlds.appservers.webapp.utils;

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

import org.openntf.domino.Session;
import org.openntf.domino.utils.Factory;

import com.google.common.annotations.Beta;

/**
 * @author Daniele Vistalli
 * @since 1.0.0
 * 
 *        This functionality has not yet been implemented. Use
 *        {@link Factory#getSession(org.openntf.domino.utils.Factory.SessionType)}
 *        to access Domino sessions
 *
 */
@Beta
public class XWorldsFactory {

	/**
	 * Call this to get a session for the user currently executing in the
	 * thread.
	 * 
	 * - If the user is Anonymous you get an anonymous session - If the user is
	 * Authenticated by WebSphere security stack you get a session for the
	 * loggedin user for it's security name - If the current thread is not
	 * running under a "user" security context but is a "system" thread then a
	 * SessionType.NATIVE session is returned.
	 * 
	 * @return a Domino session with the identity of the current executing user
	 *         for the thread.
	 */
	public static Session getCallerSession() {

		return null;
	}

	/**
	 * @return
	 */
	public static Session getDefaultSession() {

		return null;
	}

	public static String getCallerFullName() {

		return null;
	}

}
