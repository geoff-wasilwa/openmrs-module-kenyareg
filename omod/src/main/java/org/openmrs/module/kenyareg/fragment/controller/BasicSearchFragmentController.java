package org.openmrs.module.kenyareg.fragment.controller;

import org.openmrs.Person;
import org.openmrs.module.kenyaui.KenyaUiUtils;
import org.openmrs.module.kenyaui.form.ValidatingCommandObject;
import org.openmrs.ui.framework.SimpleObject;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.BindParams;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.annotation.MethodParam;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpSession;

/**
 * Created by gitahi on 23/01/15.
 */
public class BasicSearchFragmentController {

	public void controller(@FragmentParam(value = "person", required = false) Person person,
	                       FragmentModel model) {
	}

	public SimpleObject search(@MethodParam("newBasicSearchForm") @BindParams BasicSearchForm form,
	                           UiUtils ui,
	                           HttpSession session,
	                           @SpringBean KenyaUiUtils kenyaUi) {
		ui.validate(form, form, null);
		return null;
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
	}
}
