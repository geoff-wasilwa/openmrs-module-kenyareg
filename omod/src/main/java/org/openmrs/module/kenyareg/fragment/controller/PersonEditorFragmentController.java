package org.openmrs.module.kenyareg.fragment.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.go2itech.oecui.data.RequestResult;
import org.openmrs.module.kenyareg.api.PersonMergeService;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.fragment.FragmentConfiguration;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.openmrs.ui.framework.session.Session;

import ke.go.moh.oec.Person;

public class PersonEditorFragmentController {

	public void controller(
			@SpringBean("personMergeService") PersonMergeService mergeService,
			FragmentConfiguration config,
			FragmentModel model,
			Session session
		) {
		Person fromLpi = new Person();
		if (config.containsKey("lpiUid")) {
			String uuid = config.get("lpiUid").toString();
			@SuppressWarnings("unchecked")
			List<Person> lpiPersonList = (List<Person>) session.getAttribute("lpiResult", RequestResult.class).getData();
			for (Person person : lpiPersonList) {
				if (person.getPersonGuid().equals(uuid)) {
					fromLpi = person;
					break;
				}
			}
		}

		Person fromMpi = new Person();
		if (config.containsKey("mpiUid")) {
			String uuid = config.get("mpiUid").toString();
			@SuppressWarnings("unchecked")
			List<Person> mpiPersonList = (List<Person>) session.getAttribute("lpiResult", RequestResult.class).getData();
			for (Person person : mpiPersonList) {
				if (person.getPersonGuid().equals(uuid)) {
					fromMpi = person;
					break;
				}
			}
		}
		Map<String, Object> mergedProperties = new HashMap<String, Object>();
		Map<String, Map<String, Object>> conflicts = new HashMap<String, Map<String,Object>>();
		String[] properties = new String[]
			{
				"lastName", "firstName", "middleName", "otherName",
				"clanName", "sex", "birthdate", "mothersFirstName",
				"mothersMiddleName", "mothersLastName", "fathersFirstName",
				"fathersMiddleName", "fathersLastName",
				"villageName", "maritalStatus"
			};

		mergeService.mergeLpiMpiPersonProperties(mergedProperties, conflicts, fromLpi, fromMpi, properties);

		model.addAttribute("mergedProperties", mergedProperties);
		model.addAttribute("conflictedProperties", conflicts);

	}

	public void update() {

	}

	public void pollPersonIndex(Session session) {

	}

}
