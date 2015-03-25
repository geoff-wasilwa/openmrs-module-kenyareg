/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.openmrs.module.kenyareg.api;

import ke.go.moh.oec.Person;
import ke.go.moh.oec.PersonIdentifier;
import ke.go.moh.oec.PersonRequest;
import ke.go.moh.oec.lib.Mediator;
import org.go2itech.oecui.api.RequestDispatcher;
import org.go2itech.oecui.data.RequestResult;
import org.go2itech.oecui.data.RequestResultPair;
import org.openmrs.Patient;
import org.openmrs.PersonName;
import org.openmrs.api.PatientService;
import org.openmrs.api.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("registryService")
public class RegistryServiceImpl implements RegistryService {

	@Autowired(required = true)
	@Qualifier("patientService")
	PatientService patientService;

	@Autowired(required = true)
	@Qualifier("personService")
	PersonService personService;

	@Override
	public RequestResultPair findPerson(int server, Person person) {
		PersonRequest request = new PersonRequest();
		request.setPerson(person);
		request.setRequestReference(Mediator.generateMessageId());

		RequestResult mpiResult = new RequestResult();
		RequestResult lpiResult = new RequestResult();

		RequestDispatcher.dispatch(request, mpiResult, lpiResult,
				RequestDispatcher.MessageType.FIND, server);

		RequestResultPair resultPair = new RequestResultPair(lpiResult, mpiResult);
		return resultPair;
	}

	@Override
	public Patient acceptPerson(Person fromMpi) {
		org.openmrs.Person fromOmrs = personService.getPersonByUuid(fromMpi.getPersonGuid());
		org.openmrs.Person merged = mergePerson(fromOmrs, fromMpi);
		Patient patient;
		if (merged.isPatient()) {
			patient = (Patient) merged;
		} else {
			patient = new Patient(merged);
		}
		mergePersonIdentifiers(fromOmrs, fromMpi);
		return patientService.savePatient(patient);
	}

	private org.openmrs.Person mergePerson(org.openmrs.Person fromOmrs, Person fromMpi) {
		if (fromOmrs == null) {
			fromOmrs = new org.openmrs.Person();
			fromOmrs.setUuid(fromMpi.getPersonGuid());
		}
		fromOmrs.setGender(fromMpi.getSex() == null ? "M" : fromMpi.getSex().toString());
		fromOmrs.setBirthdate(fromMpi.getBirthdate());
		fromOmrs.setDeathDate(fromMpi.getDeathdate());

		PersonName personName = fromOmrs.getPersonName();
		if (personName == null) {
			personName = new PersonName();
			personName.setPerson(fromOmrs);
		}
		personName.setGivenName(fromMpi.getFirstName());
		personName.setMiddleName(fromMpi.getMiddleName());
		personName.setFamilyName(fromMpi.getLastName());
		fromOmrs.addName(personName);

		return fromOmrs;
	}

	private org.openmrs.Person mergePersonIdentifiers(org.openmrs.Person fromOmrs, Person fromMpi) {
		List<PersonIdentifier> mpiIds = fromMpi.getPersonIdentifierList();
		if (mpiIds != null) {
			for (PersonIdentifier mpiId : mpiIds) {
				if (mpiId.getIdentifierType().toString().equalsIgnoreCase("")) {
					//assign patient ids here
				}
			}
		}
		return fromOmrs;
	}
}
