package org.openmrs.module.kenyareg.helper;

import ke.go.moh.oec.Person;
import org.go2itech.oecui.data.RequestResultPair;
import org.go2itech.oecui.data.Server;
import org.openmrs.module.kenyareg.api.RegistryService;
import org.openmrs.module.kenyareg.form.SearchForm;
import org.openmrs.module.kenyaui.form.ValidatingCommandObject;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.session.Session;
import org.springframework.stereotype.Component;

/**
 * Created by gitahi on 20/07/16.
 */
@Component
public class SearchHelper {

    public void initialize(Session session) {
        session.setAttribute("lpiResult", null);
        session.setAttribute("mpiResult", null);

        session.setAttribute("lpiDisplayed", null);
        session.setAttribute("mpiDisplayed", null);

        session.setAttribute("lastResort", null);
    }

    public RequestResultPair search(
            RegistryService registryService,
            Session session,
            SearchForm form,
            UiUtils ui) {
        ui.validate(form, (ValidatingCommandObject) form, null);
        Person query = form.getPerson();
        RequestResultPair resultPair = registryService.findPerson(form.getServer(), query);

        if (form.getServer() == Server.LPI) {
            session.setAttribute("lpiResult", resultPair.getLpiResult());
        }
        if (form.getServer() == Server.MPI) {
            session.setAttribute("mpiResult", resultPair.getMpiResult());
        }
        if (form.getServer() == Server.MPI_LPI) {
            session.setAttribute("lpiResult", resultPair.getLpiResult());
            session.setAttribute("mpiResult", resultPair.getMpiResult());
        }
        setDisplayAttributes(form.getServer(), session);
        return resultPair;
    }

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

}
