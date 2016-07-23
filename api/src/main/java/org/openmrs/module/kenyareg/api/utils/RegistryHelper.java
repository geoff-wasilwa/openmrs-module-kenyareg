package org.openmrs.module.kenyareg.api.utils;

import ke.go.moh.oec.PersonIdentifier;
import ke.go.moh.oec.PersonRequest;
import ke.go.moh.oec.lib.Mediator;
import org.go2itech.oecui.api.RequestDispatcher;
import org.openmrs.module.kenyareg.api.PersonWrapper;

/**
 * @author Stanslaus Odhiambo
 * Created on 7/22/2016.
 */
public class RegistryHelper {
    private OpenmrsPersonToOecPersonConverter converter=new OpenmrsPersonToOecPersonConverter();


    public void createPerson(int targetServer, PersonWrapper personWrapper) {
        RequestDispatcher.dispatch(createPersonRequest(personWrapper), RequestDispatcher.MessageType.CREATE, targetServer);
    }

    public void modifyPerson(int targetServer, PersonWrapper personWrapper) {
        RequestDispatcher.dispatch(createPersonRequest(personWrapper), RequestDispatcher.MessageType.MODIFY, targetServer);
    }
    private PersonRequest createPersonRequest(PersonWrapper personWrapper) {
        PersonRequest personRequest = new PersonRequest();
        personRequest.setPerson(personWrapper.unwrap());
        personRequest.setRequestReference(Mediator.generateMessageId());
        return personRequest;
    }

    /**
     * This method examines the String passed to determine the kind of clinic id
     * it is. If it can recognize the pattern, it uses the String passed to create
     * a fully qualified clinic id of that type. If it cannot deduce the clinic type
     * represented by the passed String, it returns null.
     *
     * @param clinicId
     *
     * @return
     */
    public static PersonIdentifier createPersonIdentifier(String clinicId) {
        PersonIdentifier personIdentifier = new PersonIdentifier();

//        TODO Code to validate the Clinic ID
        personIdentifier.setIdentifier(clinicId);
        personIdentifier.setIdentifierType(PersonIdentifier.Type.cccLocalId);
        return personIdentifier;
    }
}
