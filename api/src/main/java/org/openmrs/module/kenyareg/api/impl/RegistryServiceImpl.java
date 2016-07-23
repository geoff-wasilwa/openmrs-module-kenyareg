/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 * <p/>
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 * <p/>
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.openmrs.module.kenyareg.api.impl;

import java.util.Collections;
import java.util.List;

import org.go2itech.oecui.api.RequestDispatcher;
import org.go2itech.oecui.data.RequestResult;
import org.go2itech.oecui.data.RequestResultPair;
import org.go2itech.oecui.data.Server;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.api.PatientService;
import org.openmrs.api.PersonService;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.idgen.service.IdentifierSourceService;
import org.openmrs.module.kenyaemr.api.KenyaEmrService;
import org.openmrs.module.kenyaemr.metadata.CommonMetadata;
import org.openmrs.module.kenyareg.api.PersonMergeService;
import org.openmrs.module.kenyareg.api.RegistryService;
import org.openmrs.module.kenyareg.api.utils.OpenmrsPersonToOecPersonConverter;
import org.openmrs.module.kenyareg.api.PersonWrapper;
import org.openmrs.module.kenyareg.api.utils.RegistryHelper;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ke.go.moh.oec.Person;
import ke.go.moh.oec.PersonIdentifier;
import ke.go.moh.oec.PersonRequest;
import ke.go.moh.oec.lib.Mediator;

@Service("registryService")
public class RegistryServiceImpl extends BaseOpenmrsService implements RegistryService {

    @Autowired(required = true)
    @Qualifier("patientService")
    PatientService patientService;

    @Autowired(required = true)
    @Qualifier("personService")
    PersonService personService;

    @Autowired(required = true)
    @Qualifier("personMergeService")
    PersonMergeService mergeService;

    @Autowired
    private KenyaEmrService emrService;

    private RegistryHelper registryHelper;

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
    public Patient acceptPerson(Person fromMpi, boolean lpiMatched, boolean mpiMatched) {
        registryHelper = new RegistryHelper();
        org.openmrs.Person fromOmrs = personService.getPersonByUuid(fromMpi.getPersonGuid());
        org.openmrs.Person person = mergePerson(fromOmrs, fromMpi);
        Patient patient = (Patient) person;
        Person convertedPatient = OpenmrsPersonToOecPersonConverter.convert(patient);
        PersonWrapper lpiUpdatePersonWrapper = new PersonWrapper(convertedPatient);
        PersonWrapper mpiUpdatePersonWrapper = new PersonWrapper(convertedPatient);
        if (!mpiMatched && !lpiMatched) {
            registryHelper.createPerson(Server.MPI, mpiUpdatePersonWrapper);
            registryHelper.createPerson(Server.LPI, lpiUpdatePersonWrapper);
        } else {
            if (!mpiMatched && lpiMatched) {
                registryHelper.createPerson(Server.MPI, mpiUpdatePersonWrapper);
                registryHelper.modifyPerson(Server.LPI,lpiUpdatePersonWrapper);
            } else if (mpiMatched && !lpiMatched) {
                lpiUpdatePersonWrapper.setMPIIdentifier(mpiUpdatePersonWrapper.getPersonGuid());
                registryHelper.modifyPerson(Server.MPI, mpiUpdatePersonWrapper);
                registryHelper.createPerson(Server.LPI, lpiUpdatePersonWrapper);
            } else {
                //link mpiMatch with lpiMatch if they have not been linked yet
                if (lpiUpdatePersonWrapper.getMPIIdentifier().isEmpty()) {
                    lpiUpdatePersonWrapper.setMPIIdentifier(mpiUpdatePersonWrapper.getPersonGuid());
                }
                registryHelper.modifyPerson(Server.MPI, mpiUpdatePersonWrapper);
                registryHelper.modifyPerson(Server.LPI, lpiUpdatePersonWrapper);
            }
        }

        return patientService.savePatient(patient);
    }

    private org.openmrs.Person mergePerson(org.openmrs.Person fromOmrs, Person fromMpi) {
        if (fromOmrs == null) {
            fromOmrs = new org.openmrs.Person();
            fromOmrs.setUuid(fromMpi.getPersonGuid());
        }
        org.openmrs.Person person = mergeService.mergePerson(fromOmrs, fromMpi);
        Patient patient;
        if (fromOmrs.isPatient()) {
            patient = (Patient) person;
        } else {
            patient = new Patient(person);
        }
        List<PersonIdentifier> personIdentifiers = fromMpi.getPersonIdentifierList();
        if (personIdentifiers == null) {
            personIdentifiers = Collections.emptyList();
        }
        mergeService.mergePatientIdentifiers(patient, personIdentifiers, emrService.getDefaultLocation());
        PatientIdentifierType openmrsIdType = MetadataUtils.existing(PatientIdentifierType.class, CommonMetadata._PatientIdentifierType.OPENMRS_ID);
        PatientIdentifier openmrsId = patient.getPatientIdentifier(openmrsIdType);

        if (openmrsId == null) {
            //no openmrs id....set it
            String generated = Context.getService(IdentifierSourceService.class).generateIdentifier(openmrsIdType, "Registration");
            openmrsId = new PatientIdentifier(generated, openmrsIdType, emrService.getDefaultLocation());
            if (patient.getPatientIdentifier() != null) {
                if (!patient.getPatientIdentifier().isPreferred()) {
                    openmrsId.setPreferred(true);
                }
            }
        } else {
            //openmrs id exists, check if there is a preferred
            if (patient.getPatientIdentifier() != null) {
                if (!patient.getPatientIdentifier().isPreferred()) {
                    //not preferred in this case so set the openmrs one
                    patient.getPatientIdentifier(openmrsIdType).setPreferred(true);
                }
            }
        }
        patient.addIdentifier(openmrsId);
        return patient;
    }
}
