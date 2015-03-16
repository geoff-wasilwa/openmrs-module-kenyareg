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

package org.openmrs.module.kenyareg.fragment.controller;

import ke.go.moh.oec.Person;
import org.go2itech.oecui.data.RequestResult;
import org.go2itech.oecui.data.RequestResultPair;
import org.go2itech.oecui.data.Server;
import org.openmrs.Patient;
import org.openmrs.api.PatientService;
import org.openmrs.module.kenyareg.api.RegistryService;
import org.openmrs.module.kenyaui.form.ValidatingCommandObject;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.BindParams;
import org.openmrs.ui.framework.annotation.MethodParam;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.session.Session;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestParam;

public class BasicSearchFragmentController {

	public void controller(Session session) {
		session.setAttribute("lpiResult", null);
		session.setAttribute("mpiResult", null);
	}

	public RequestResultPair search(@MethodParam("newBasicSearchForm") @BindParams BasicSearchForm form,
	                                @SpringBean("registryService") RegistryService registryService,
	                                Session session, UiUtils ui) {
		ui.validate(form, form, null);

		Person query = form.getPerson();
        RequestResultPair resultPair = registryService.findPerson(Server.MPI_LPI, query);

		session.setAttribute("lpiResult", resultPair.getLpiResult());
		session.setAttribute("mpiResult", resultPair.getMpiResult());

		RequestResult lpi = session.getAttribute("lpiResult", RequestResult.class);

		return resultPair;
	}


	public Integer accept(@RequestParam(value = "uuid", required = true) String uuid,
	                      @SpringBean("patientService") PatientService patientService) {
		Patient patient = patientService.getPatientByUuid(uuid);
		if (patient == null) {
			return null;
		}
		/**
		 * This method should receive the match being accepted. It should then use the
		 * NUPI in there to:
		 *
		 * 1. Create or update a patient record
		 * 2. Redirect to the patient dashboard for that record
		 */
		return patient.getId();
	}

	public BasicSearchForm newBasicSearchForm() {
		return new BasicSearchForm();
	}

	public class BasicSearchForm extends ValidatingCommandObject {

		private String surname;
		private String firstName;

		public BasicSearchForm() {
		}

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getSurname() {
			return surname;
		}

		public void setSurname(String surname) {
			this.surname = surname;
		}

		@Override
		public void validate(Object o, Errors errors) {

		}

		public Person getPerson() {
			Person person = new Person();
			person.setFirstName(firstName);
			person.setLastName(surname);
			return person;
		}
	}
}
