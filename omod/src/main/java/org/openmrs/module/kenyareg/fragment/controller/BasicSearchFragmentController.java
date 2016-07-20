/**
 * The contents of this file are subject to the OpenMRS Public License Version 1.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at http://license.openmrs.org
 * <p/>
 * Software distributed under the License is distributed on an "AS IS" basis, WITHOUT WARRANTY OF ANY KIND, either
 * express or implied. See the License for the specific language governing rights and limitations under the License.
 * <p/>
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.openmrs.module.kenyareg.fragment.controller;

import org.go2itech.oecui.data.RequestResultPair;
import org.openmrs.module.kenyareg.api.RegistryService;
import org.openmrs.module.kenyareg.form.BasicSearchForm;
import org.openmrs.module.kenyareg.helper.SearchHelper;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.BindParams;
import org.openmrs.ui.framework.annotation.MethodParam;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.session.Session;

public class BasicSearchFragmentController {

    public void controller( @SpringBean("searchHelper") SearchHelper searchHelper, Session session) {
        searchHelper.initialize(session);
    }

    public RequestResultPair search(@MethodParam("newBasicSearchForm") @BindParams BasicSearchForm form,
                                    @SpringBean("registryService") RegistryService registryService,
                                    @SpringBean("searchHelper") SearchHelper searchHelper,
                                    Session session,
                                    UiUtils ui) {
        return searchHelper.search(registryService, session, form, ui);
    }

    public BasicSearchForm newBasicSearchForm() {
        return new BasicSearchForm();
    }
}
