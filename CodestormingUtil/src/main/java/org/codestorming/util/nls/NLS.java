/*
 * Copyright (c) 2012-2016 Codestorming.org
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Codestorming - initial implementation and API
 */
package org.codestorming.util.nls;

import org.codestorming.util.PropertiesLoader;

import java.net.URL;
import java.util.Locale;

/**
 * A {@code NLS} implementation is a set of {@code public static} fields of type
 * {@link String}.
 * <p/>
 * A properties file must be in the same jar file than the NLS class, the fields will be
 * initialized from that file.<br/>
 * There may be multiple properties files, as long as their name contain the locale they
 * represent.
 * <p/>
 * Examples of properties files:
 * <ul>
 * <li>i18n.properties</li>
 * <li>i18n_en.properties</li>
 * <li>i18n_fr.properties</li>
 * </ul>
 * Example of a NLS class:
 *
 * <pre>
 * public class ExampleNLS extends NLS {
 * 	public static String PROP_1;
 * 	public static String PROP_2;
 *
 * 	static {
 * 		initialize(ExampleNLS.class, getBundleUrl(ExampleNLS.class, &quot;i18n&quot;));
 * 	}
 * }
 * </pre>
 *
 * In this example, the properties files must be at the root of the JAR. If you whish to
 * put them in a package you must specify the package name (e.g.
 * <em><code>com.example.nls.i18n</code></em> if the properties files are in the package
 * {@code com.example.nls}).
 *
 * @author Thaedrik <thaedrik@gmail.com>
 * @since 1.1
 */
public abstract class NLS extends PropertiesLoader {

	/**
	 * Returns the URL of the properties file for the default system locale, or
	 * {@code null} if no file were found.
	 *
	 * @param nlsClass The {@link NLS} class containing the properties to initialize.
	 * @param bundleName The qualified name of the properties file without the extension
	 *        (e.g. {@code org.codestorming.messages}).
	 * @return the URL of the properties file for the default system locale, or
	 *         {@code null} if no file were found.
	 */
	protected static URL getBundleUrl(Class<? extends NLS> nlsClass, String bundleName) {
		String[] locales = getLocales(bundleName);
		URL bundleURL = null;
		for (int i = 0, n = locales.length; i < n && bundleURL == null; i++) {
			bundleURL = nlsClass.getClassLoader().getResource(locales[i]);
		}
		return bundleURL;
	}

	private static String[] getLocales(String bundleName) {
		final String ext = ".properties";
		String[] locales = new String[3];
		Locale locale = Locale.getDefault();
		String bundle = bundleName.replaceAll("\\.", "/");
		String country = locale.getCountry();
		locales[0] = bundle + '_' + locale.getLanguage() + (country.length() > 0 ? '_' + country : "") + ext;
		locales[1] = bundle + '_' + locale.getLanguage() + ext;
		locales[2] = bundle + ext;
		return locales;
	}
}
