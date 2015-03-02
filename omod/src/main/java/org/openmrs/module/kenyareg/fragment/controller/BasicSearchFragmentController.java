package org.openmrs.module.kenyareg.fragment.controller;

import ke.go.moh.oec.PersonRequest;
import ke.go.moh.oec.lib.Mediator;
import org.go2itech.oecui.api.RequestDispatcher;
import org.go2itech.oecui.data.RequestResult;
import org.go2itech.oecui.data.RequestResultPair;
import org.go2itech.oecui.data.Server;
import org.openmrs.module.kenyaui.form.ValidatingCommandObject;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.BindParams;
import org.openmrs.ui.framework.annotation.MethodParam;
import org.springframework.validation.Errors;

/**
 * Created by gitahi on 23/01/15.
 */
public class BasicSearchFragmentController {

	public void controller() {
	}

	public RequestResultPair search(@MethodParam("newBasicSearchForm") @BindParams BasicSearchForm form, UiUtils ui) {
		ui.validate(form, form, null);
		return form.search(Server.MPI_LPI);
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

		public RequestResultPair search(int server) {
			ke.go.moh.oec.Person person = new ke.go.moh.oec.Person();
			person.setFirstName(firstName);
			person.setLastName(surname);

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
	}
}
