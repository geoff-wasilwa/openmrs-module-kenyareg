package org.openmrs.module.kenyareg.fragment.controller;

import org.go2itech.oecui.data.RequestResultPair;
import org.openmrs.module.kenyareg.api.RegistryService;
import org.openmrs.module.kenyareg.form.IdentifierSearchForm;
import org.openmrs.module.kenyareg.helper.SearchHelper;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.BindParams;
import org.openmrs.ui.framework.annotation.MethodParam;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.session.Session;

/**
 * @author Stanslaus Odhiambo
 *         Created on 7/14/2016.
 */
public class IdentifierSearchFragmentController {

    public void controller( @SpringBean("searchHelper") SearchHelper searchHelper, Session session) {
        searchHelper.initialize(session);
    }

    public RequestResultPair search(@MethodParam("newIdentifierSearchForm") @BindParams IdentifierSearchForm form,
                                    @SpringBean("registryService") RegistryService registryService,
                                    @SpringBean("searchHelper") SearchHelper searchHelper,
                                    Session session,
                                    UiUtils ui) {
        return searchHelper.search(registryService, session, form, ui);
    }

    public IdentifierSearchForm newIdentifierSearchForm() {
        return new IdentifierSearchForm();
    }
}
