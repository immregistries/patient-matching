package org.immregistries.pm;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.WebPage;

/**
 * Default page supplied by Wicket framework. Not currently in use. 
 * @author Nathan Bunker
 *
 */
public class HomePage extends WebPage {
	private static final long serialVersionUID = 1L;

    public HomePage(final PageParameters parameters) {
		add(new Label("version", SoftwareVersion.VERSION));
        // TODO Add your page's components here
    }
}
