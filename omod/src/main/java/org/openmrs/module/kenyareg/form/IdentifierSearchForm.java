package org.openmrs.module.kenyareg.form;

import ke.go.moh.oec.Person;
import ke.go.moh.oec.PersonIdentifier;
import org.openmrs.module.kenyaui.form.ValidatingCommandObject;
import org.springframework.validation.Errors;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gitahi on 20/07/16.
 */
public class IdentifierSearchForm extends ValidatingCommandObject implements SearchForm {
    private int server;
    private int identifierTypeId;
    private String personIdentifier;

    public IdentifierSearchForm() {
    }

    @Override
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

    @Override
    public Person getPerson() {
        Person person = new Person();
        PersonIdentifier pi = new PersonIdentifier();
        List<PersonIdentifier> identifiers = new ArrayList<PersonIdentifier>();
        pi.setIdentifier(personIdentifier);
        switch (identifierTypeId) {
            case 1: {
                pi.setIdentifierType(PersonIdentifier.Type.kisumuHdssId);
                break;
            }
            case 2: {
                pi.setIdentifierType(PersonIdentifier.Type.cccUniqueId);
                break;
            }
            case 3: {
                pi.setIdentifierType(PersonIdentifier.Type.masterPatientRegistryId);
                break;
            }
            case 4: {
                pi.setIdentifierType(PersonIdentifier.Type.cccLocalId);
                break;
            }
            case 50: {
                pi.setIdentifierType(PersonIdentifier.Type.nupi);
                break;
            }
            case 10: {
                pi.setIdentifierType(PersonIdentifier.Type.telNo);
                break;
            }
            case 8: {
                pi.setIdentifierType(PersonIdentifier.Type.nationalId);
                break;
            }
            case 11: {
                pi.setIdentifierType(PersonIdentifier.Type.parentTelNo);
                break;
            }
            default: {
                pi.setIdentifierType(PersonIdentifier.Type.patientRegistryId);
                break;
            }
        }
        identifiers.add(pi);
        person.setPersonIdentifierList(identifiers);
        return person;
    }
}
