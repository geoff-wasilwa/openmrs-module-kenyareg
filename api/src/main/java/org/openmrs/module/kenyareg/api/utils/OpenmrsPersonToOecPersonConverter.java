package org.openmrs.module.kenyareg.api.utils;

import ke.go.moh.oec.PersonIdentifier;
import org.apache.commons.lang3.ArrayUtils;
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
        ke.go.moh.oec.Person oecPerson = new ke.go.moh.oec.Person();
        oecPerson.setPersonGuid(openmrsPerson.getUuid());
        oecPerson.setSex(openmrsPerson.getGender().equals("M") ? ke.go.moh.oec.Person.Sex.M : ke.go.moh.oec.Person.Sex.F);
        oecPerson.setBirthdate(openmrsPerson.getBirthdate());
        oecPerson.setFirstName(openmrsPerson.getGivenName());
        oecPerson.setDeathdate(openmrsPerson.getDeathDate());
        PersonName personName = openmrsPerson.getPersonName();
        oecPerson.setFirstName(personName.getGivenName());
        oecPerson.setMiddleName(personName.getMiddleName());
        oecPerson.setLastName(personName.getFamilyName());

        Patient openmrsPatient = (Patient) openmrsPerson;
        Set<PatientIdentifier> identifiers = openmrsPatient.getIdentifiers();
        oecPerson = mergePatientIdentifiers(oecPerson, identifiers);
        return oecPerson;
    }

    private static ke.go.moh.oec.Person mergePatientIdentifiers(ke.go.moh.oec.Person person, Set<PatientIdentifier> identifiers) {
        assert person != null;
        assert identifiers != null;
        for (PatientIdentifier identifier : identifiers) {
            String identifierType = identifier.getIdentifierType().getName();
            if (matchesIdentifierInOec(identifierType)) {
                String matchedOecIdentifierType = getMatchedOecIdentifierType(identifierType);
                updateOecIdentifier(person, identifier.getIdentifier(), matchedOecIdentifierType);
            }
        }
        return person;
    }

    private static boolean matchesIdentifierInOec(String identifierType) {
        PersonIdentifier.Type[] availableOecIdenitfierTypes = PersonIdentifier.Type.values();
        String matchedOecIdentifierType = getMatchedOecIdentifierType(identifierType);
        return ArrayUtils.contains(availableOecIdenitfierTypes, matchedOecIdentifierType);
    }


    private static String getMatchedOecIdentifierType(String identifierType){
        String matched=null;
        for (OpenmrsTypeConverter s : OpenmrsTypeConverter.values()) {
            if (s.getTypeString().equalsIgnoreCase(identifierType)) {
                /*
                An identifier type has been found in openmrs that matches a possible oec identifier type
                This is only true as long as Identifier Strings do not change as they have been hard coded in @link OpenmrsTypeConverter
                 */
                System.out.printf("Found a match at : %s == %s for identifier type : %s \n", s.getTypeString(), identifierType,s.name());
                matched = s.name();
            }
        }
        return matched;

    }

    private static void updateOecIdentifier(ke.go.moh.oec.Person person, String identifier, String matchedOecIdentifierType) {
        PersonIdentifier.Type patientIdentifierType = PersonIdentifier.Type.valueOf(matchedOecIdentifierType);
        PersonIdentifier personIdentifier = new PersonIdentifier();
        personIdentifier.setIdentifierType(patientIdentifierType);
        personIdentifier.setIdentifier(identifier);
        boolean identifierSet = false;
        for (PersonIdentifier existingIdentifier : person.getPersonIdentifierList()) {
            if (existingIdentifier.getIdentifierType().name().equalsIgnoreCase(matchedOecIdentifierType)) {
                existingIdentifier.setIdentifier(identifier);
                identifierSet = true;
                break;
            }
        }
        if (!identifierSet) {
            person.getPersonIdentifierList().add(personIdentifier);
        }
    }

    public enum OpenmrsTypeConverter {
        openMRSIdentificationNumber("OpenMRS Identification Number"),
        oldIdentificationNumber("Old Identification Number"),
        openMRSID("OpenMRS ID"),
        nationalID("National ID"),
        heiIdNumber("HEI ID Number"),
        districtRegistrationNumber("District Registration Number"),
        patientRegistryId("Patient Redistry ID"),
        masterPatientRegistryId("Master Patient Registry ID"),
        cccUniqueId("Unique Patient Number"),
        cccLocalId("Patient Clinic Number"),
        kisumuHdssId("Kisumu HDSS ID");

        private final String typeString;

        public String getTypeString() {
            return typeString;
        }

        private OpenmrsTypeConverter(String typeString) {
            this.typeString = typeString;
        }
    }

}
