package org.openmrs.module.kenyareg.fragment.controller;

import ke.go.moh.oec.Person;
import ke.go.moh.oec.PersonIdentifier;
import org.go2itech.oecui.data.RequestResultPair;
import org.go2itech.oecui.data.Server;
import org.openmrs.module.kenyareg.api.RegistryService;
import org.openmrs.module.kenyaui.form.ValidatingCommandObject;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.BindParams;
import org.openmrs.ui.framework.annotation.MethodParam;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.session.Session;
import org.springframework.validation.Errors;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Stanslaus Odhiambo
 *         Created on 7/14/2016.
 */
public class IdentifierSearchFragmentController {

    public void controller(Session session) {
        session.setAttribute("lpiResult", null);
        session.setAttribute("mpiResult", null);

        session.setAttribute("lpiDisplayed", null);
        session.setAttribute("mpiDisplayed", null);

        session.setAttribute("lastResort", null);
    }

    public RequestResultPair search(@MethodParam("newIdentifierSearchForm") @BindParams IdentifierSearchForm form,
                                    @SpringBean("registryService") RegistryService registryService,
                                    Session session, UiUtils ui) {
        ui.validate(form, form, null);

        //setDisplayAttributes(form.getServer(), session);

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

        return resultPair;
    }

    public IdentifierSearchForm newIdentifierSearchForm() {
        return new IdentifierSearchForm();
    }

    public class IdentifierSearchForm extends ValidatingCommandObject {
        private int server;
        private int identifierTypeId;
        private String personIdentifier;

        public IdentifierSearchForm() {
        }

        public int getServer() {
            return server;
        }

        public void setServer(int server) {
            this.server = server;
        }

        public int getIdentifierTypeId() {
            return identifierTypeId;
        }

        public void setIdentifierTypeId(int identifierTypeId) {
            this.identifierTypeId = identifierTypeId;
        }

        public String getPersonIdentifier() {
            return personIdentifier;
        }

        public void setPersonIdentifier(String personIdentifier) {
            this.personIdentifier = personIdentifier;
        }

        @Override
        public void validate(Object o, Errors errors) {
            require(errors, "personIdentifier");
        }
        public Person getPerson() {
            Person person = new Person();
            PersonIdentifier pi=new PersonIdentifier();
            List<PersonIdentifier> identifiers = new ArrayList<PersonIdentifier>();
            pi.setIdentifier(personIdentifier);
            switch (identifierTypeId){
                case 1:{
                    pi.setIdentifierType(PersonIdentifier.Type.kisumuHdssId);
                    break;
                }
                case 2:{
                    pi.setIdentifierType(PersonIdentifier.Type.cccUniqueId);
                    break;
                }
                case 3:{
                    pi.setIdentifierType(PersonIdentifier.Type.masterPatientRegistryId);
                    break;
                }
                case 4:{
                    pi.setIdentifierType(PersonIdentifier.Type.cccLocalId);
                    break;
                }
                default:{
                    pi.setIdentifierType(PersonIdentifier.Type.patientRegistryId);
                    break;
                }
            }
            identifiers.add(pi);
            person.setPersonIdentifierList(identifiers);
            return person;
        }
    }



}
