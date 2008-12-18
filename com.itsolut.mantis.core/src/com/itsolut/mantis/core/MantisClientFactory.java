/*******************************************************************************
 * Copyright (c) 2006 - 2006 Mylar eclipse.org project and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Mylar project committers - initial API and implementation
 *******************************************************************************/
/*******************************************************************************
 * Copyright (c) 2007 - 2007 IT Solutions, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Chris Hane - adapted Trac implementation for Mantis
 *******************************************************************************/

package com.itsolut.mantis.core;

import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;

import org.eclipse.mylyn.commons.net.AbstractWebLocation;

import com.itsolut.mantis.core.IMantisClient.Version;


/**
 * @author Steffen Pingel
 * @author Chris Hane
 */
public class MantisClientFactory {
		
	public static IMantisClient createClient(String location, Version version, String username, String password,
			String httpUsername, String httpPassword, AbstractWebLocation webLocation) throws MalformedURLException {		
		URL url = new URL(location);

		if (version == Version.MC_1_0a5) {
			return new MantisAxis1SOAPClient(url, version, username, password, httpUsername, httpPassword, webLocation);
		}

		throw new RuntimeException("Invalid repository version: " + version);
	}
}
