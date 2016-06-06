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

import org.openntf.domino.xots.AbstractXotsCallable;

/**
 * @author Paul Withers
 * @since 1.0.0
 * 
 *        Extension of AbstractXotsCallable, used for logging to OpenLog. If you
 *        don't want to log to OpenLog, just extend Callable
 *
 */
public abstract class AbstractOpenLogXotsCallable extends AbstractXotsCallable {

	public AbstractOpenLogXotsCallable(ServletContext ctx) {
		setContext(XotsUtil.initialiseXotsContextFromServlet(ctx));
	}

}
