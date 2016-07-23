package org.openmrs.module.kenyareg.api.utils;

import ke.go.moh.oec.PersonIdentifier;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.Person;
import org.openmrs.PersonName;

import java.util.Set;

/**
 * @author Stanslaus Odhiambo
 *         Created on 7/23/2016.
 */
public class OpenmrsPersonToOecPersonConverter {

    public OpenmrsPersonToOecPersonConverter() {
    }

    public static ke.go.moh.oec.Person convert(Person openmrsPerson) {
        ke.go.moh.oec.Person oecPerson=new ke.go.moh.oec.Person();
        oecPerson.setPersonGuid(openmrsPerson.getUuid());
        oecPerson.setSex(openmrsPerson.getGender().equals("M")? ke.go.moh.oec.Person.Sex.M: ke.go.moh.oec.Person.Sex.F);
        oecPerson.setBirthdate(openmrsPerson.getBirthdate());
        oecPerson.setFirstName(openmrsPerson.getGivenName());
        oecPerson.setDeathdate(openmrsPerson.getDeathDate());
        PersonName personName = openmrsPerson.getPersonName();
        oecPerson.setFirstName(personName.getGivenName());
        oecPerson.setMiddleName(personName.getMiddleName());
        oecPerson.setLastName(personName.getFamilyName());

        Patient openmrsPatient= (Patient) openmrsPerson;
        Set<PatientIdentifier> identifiers = openmrsPatient.getIdentifiers();
        oecPerson = mergePatientIdentifiers(oecPerson, identifiers);
        return oecPerson;
    }

    private static ke.go.moh.oec.Person mergePatientIdentifiers(ke.go.moh.oec.Person person,  Set<PatientIdentifier> identifiers) {
        assert person != null;
        assert identifiers != null;

        for (PatientIdentifier identifier: identifiers) {
            if (matchesIdentifierInOec(identifier.getIdentifierType().toString())) {
                updateOecIdentifier(person, identifier.getIdentifier(), identifier.getIdentifierType().toString());
            }
        }
        return person;
    }

    private static boolean matchesIdentifierInOec(String identifierName) {
        PersonIdentifier.Type[] availableOecIdenitfierTypes = PersonIdentifier.Type.values();

        for (PersonIdentifier.Type identifierType : availableOecIdenitfierTypes) {
            if (identifierType.name().equalsIgnoreCase(identifierName)) {
                return true;
            }
        }
        return false;
    }

    private static void updateOecIdentifier(ke.go.moh.oec.Person person, String identifier, String identifierType) {
        PersonIdentifier.Type patientIdentifierType = PersonIdentifier.Type.valueOf(identifierType);
        PersonIdentifier personIdentifier=new PersonIdentifier();
        personIdentifier.setIdentifierType(patientIdentifierType);
        personIdentifier.setIdentifier(identifier);

        boolean identifierSet = false;
        for (PersonIdentifier existingIdentifier : person.getPersonIdentifierList()) {
            if (existingIdentifier.getIdentifierType().name().equalsIgnoreCase(identifierType)) {
                existingIdentifier.setIdentifier(identifier);
                identifierSet = true;
                break;
            }
        }
        if (!identifierSet) {
            person.getPersonIdentifierList().add(personIdentifier);
        }
    }

}
