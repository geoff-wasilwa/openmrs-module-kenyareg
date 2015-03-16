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
import ke.go.moh.oec.PersonRequest;
import ke.go.moh.oec.lib.Mediator;
import org.go2itech.oecui.api.RequestDispatcher;
import org.go2itech.oecui.data.RequestResult;
import org.go2itech.oecui.data.RequestResultPair;
import org.springframework.stereotype.Service;

/**
 * Created by gitahi on 16/03/15.
 */
@Service("registryService")
public class RegistryServiceImpl implements RegistryService {

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
}
