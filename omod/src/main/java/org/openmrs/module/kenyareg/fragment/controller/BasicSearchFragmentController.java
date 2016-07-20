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
import org.go2itech.oecui.data.Server;
import org.openmrs.module.kenyareg.api.RegistryService;
import org.openmrs.module.kenyareg.form.BasicSearchForm;
import org.openmrs.module.kenyareg.helper.SearchHelper;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.BindParams;
import org.openmrs.ui.framework.annotation.MethodParam;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.session.Session;
import org.springframework.web.bind.annotation.RequestParam;

public class BasicSearchFragmentController {

    private void setDisplayAttributes(int server, Session session) {
        switch (server) {
            case Server.MPI_LPI:
                session.setAttribute("lpiDisplayed", Boolean.FALSE);
                session.setAttribute("mpiDisplayed", Boolean.FALSE);
                break;
            case Server.MPI:
                session.setAttribute("mpiDisplayed", Boolean.FALSE);
                break;
            case Server.LPI:
                session.setAttribute("lpiDisplayed", Boolean.FALSE);
                break;
        }
    }

    public void controller(Session session) {
        session.setAttribute("lpiResult", null);
        session.setAttribute("mpiResult", null);

        session.setAttribute("lpiDisplayed", null);
        session.setAttribute("mpiDisplayed", null);

        session.setAttribute("lastResort", null);
    }

    public RequestResultPair search(@MethodParam("newBasicSearchForm") @BindParams BasicSearchForm form,
                                    @SpringBean("registryService") RegistryService registryService,
                                    @SpringBean("searchHelper") SearchHelper searchHelper,
                                    Session session,
                                    UiUtils ui) {
        return searchHelper.search(registryService, session, form, ui);
    }

    public Integer accept(@RequestParam(value = "uuid", required = true) String uuid,
                          @SpringBean("registryService") RegistryService registryService,
                          @SpringBean("searchHelper") SearchHelper searchHelper,
                          Session session) {
        return searchHelper.accept(registryService, session, uuid);
    }

    public BasicSearchForm newBasicSearchForm() {
        return new BasicSearchForm();
    }
}
