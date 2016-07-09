jq(function () {

	/**
	 * Pre-fill basic search form with test data.
	 */
	jq('#surname').val("Kamau");
	jq('#firstName').val("Rose");

	/**
	 * The search result after sending a query to the MPI, LPI or both.
	 */
	var requestResult = null;

	/**
	 * The matching person selected by the user from the LPI, if any.
	 */
	var lpiMatch = null;

	/**
	 * The matching person selected by the user from the MPI, if any.
	 */
	var mpiMatch = null;

	/**
	 * Sets up the basic search form to be submitted via AJAX.
	 */
	kenyaui.setupAjaxPost('basic-search-form', {
		onSuccess: function (data) {
			requestResult = data;
			if (!data.lpiResult.successful && !data.mpiResult.successful) {
				jq('#status').html("Neither the LPI nor the MPI could be contacted. Retry?");
			} else {
				if (!data.lpiResult.successful && data.mpiResult.successful) {
					jq('#status').html("The LPI could not be contacted. Retry?");
					showResultBySource('mpi');
					showDetails(0, 'mpi')
				} else if (data.lpiResult.successful && !data.mpiResult.successful) {
					jq('#status').html("The MPI could not be contacted. Retry?");
					showResultBySource('lpi');
					showDetails(0, 'lpi');
				} else {
					jq('#status').html("Both the LPI and the MPI were contacted. render results.");
					showResults();
					showDetails(0, 'lpi');
					showDetails(0, 'mpi');
				}
			}
		}
	});

	jq('#lpi-results-table tbody').on('click', 'tr', function (e) {
		showDetails(this.rowIndex - 1, 'lpi');
	});

	jq('#mpi-results-table tbody').on('click', 'tr', function (e) {
		showDetails(this.rowIndex - 1, 'mpi');
	});

	function showResults() {
		showResultBySource('lpi');
		showResultBySource('mpi');
	}

	function showResultBySource(source) {
		var list;
		var id;
		if (source == 'lpi') {
			list = requestResult.lpiResult;
			id = '#lpi-results-table';
		} else if (source == 'mpi') {
			list = requestResult.mpiResult;
			id = '#mpi-results-table';
		}
		jq(id + ' > tbody').html("");
		if (list.data.length == 0) {
			showEmpty(source);
		} else {
			for (var i = 0; i < list.data.length; i++) {
				showPerson(i, source);
			}
		}
	}

	function showEmpty(source) {
		var id;
		if (source == 'lpi') {
			id = '#lpi-results-table';
		} else if (source == 'mpi') {
			id = '#mpi-results-table';
		}
		jq(id).append('<tr>' +
		'<td colspan="6">Nothing to show</td>' +
		'</tr>');
	}

	function showPerson(i, source) {
		var person;
		var id;
		if (source == 'lpi') {
			person = requestResult.lpiResult.data[i];
			id = '#lpi-results-table';
		} else if (source == 'mpi') {
			person = requestResult.mpiResult.data[i];
			id = '#mpi-results-table';
		}
		jq(id).append('<tr>' +
		'<td>' + replaceNull(person.matchScore) + '</td>' +
		'<td>' + replaceNull(person.firstName) + '</td>' +
		'<td>' + replaceNull(person.middleName) + '</td>' +
		'<td>' + replaceNull(person.lastName) + '</td>' +
		'<td>' + replaceNull(person.sex) + '</td>' +
		'<td>' + replaceNull(formatDate(person.birthdate)) + '</td>' +
		'</tr>');
	}

	function replaceNull(value) {
		return value ? value : "";
	}

	function showDetails(i, source) {
		var selected;
		if (source == 'lpi') {
			lpiMatch = requestResult.lpiResult.data[i];
			selected = lpiMatch;
		} else if (source == 'mpi') {
			mpiMatch = requestResult.mpiResult.data[i];
			selected = mpiMatch;
		}
		showMatchScore(selected, source)
		showIdentifierDetails(selected, source);
		showBasicDetails(selected, source);
		showStatusDetails(selected, source);
		showParentDetails(selected, source);
	}

	function showMatchScore(person, source) {
		jq('#' + source + '-score').html(replaceNull(person.matchScore));
	}

	function showIdentifierDetails(person, source) {
		var id = '#' + source + '-id-table';
		jq(id + ' > tbody').html("");
		var personIdList = person.personIdentifierList;
		if (personIdList) {
			for (var j = 0; j < personIdList.length; j++) {
				var personId = personIdList[j];
				jq(id).append('<tr>' +
				'<td class="field-label">' + replaceNull(formatIdentifierType(personId.identifierType)) + '</td>' +
				'<td>' + replaceNull(personId.identifier) + '</td>' +
				'</tr>');
			}
		} else {
			jq(id).append('<tr><td></td></tr>');
		}
	}

	function showBasicDetails(person, source) {
		jq('#' + source + '-first-name').html(replaceNull(person.firstName));
		jq('#' + source + '-middle-name').html(replaceNull(person.middleName));
		jq('#' + source + '-last-name').html(replaceNull(person.lastName));
		jq('#' + source + '-birth-date').html(replaceNull(formatDate(person.birthdate)));
		jq('#' + source + '-sex').html(replaceNull(person.sex));
	}

	function showStatusDetails(person, source) {
		jq('#' + source + '-alive-status').html(replaceNull(person.aliveStatus));
		jq('#' + source + 'marital-status').html(replaceNull(formatMaritalStatus(person.maritalStatus)));
		jq('#' + source + 'last-visit-date').html(replaceNull(person.lastRegularVisit));
	}

	function showParentDetails(person, source) {
		jq('#' + source + 'father-first-name').html(replaceNull(person.fathersFirstName));
		jq('#' + source + 'father-middle-name').html(replaceNull(person.fathersMiddleName));
		jq('#' + source + 'father-last-name').html(replaceNull(person.fathersLastName));
		jq('#' + source + 'mother-first-name').html(replaceNull(person.mothersFirstName));
		jq('#' + source + 'mother-middle-name').html(replaceNull(person.mothersMiddleName));
		jq('#' + source + 'mother-last-name').html(replaceNull(person.mothersLastName));
	}

	function formatMaritalStatus(status) {
		var formatted = '';
		if (status) {
			switch (status) {
				case 'single':
					formatted = 'Single';
					break;
				case 'marriedPolygamous':
					formatted = 'Married Polygamous';
					break;
				case 'marriedMonogamous':
					formatted = 'Married Monogamous';
					break;
				case 'divorced':
					formatted = 'Divorced';
					break;
				case 'widowed':
					formatted = 'Widowed';
					break;
				case 'cohabitating':
					formatted = 'Cohabitating';
					break;
				default:
					formatted = '';
			}
		}
		return formatted;
	}

	function formatIdentifierType(type) {
		var formatted = '';
		if (type) {
			switch (type) {
				case 'patientRegistryId':
					formatted = 'Patient Registry ID';
					break;
				case 'masterPatientRegistryId':
					formatted = 'Master Patient Registry ID';
					break;
				case 'cccUniqueId':
					formatted = 'CCC Unique ID';
					break;
				case 'cccLocalId':
					formatted = 'CCC Local ID';
					break;
				case 'kisumuHdssId':
					formatted = 'Kisumu HDSS ID';
					break;
				default:
					formatted = '';
			}
		}
		return formatted;
	}

	function formatDate(date) {
		var formatted = '';
		var format = 'dd-MM-yyyy';
		if (date) {
			formatted = new Date(date).toDateString();
		}
		return formatted;
	}

	//selectedMatch was renamed/split to lpiMatch and mpiMatch
	function extractNupi(person) {
		var nupi = null;
		if (selectedMatch.personIdentifierList) {
			jq.each(selectedMatch.personIdentifierList, function (i, id) {
				if (id.identifierType === 'kisumuHdssId') {
					nupi = id.identifier;
				}
			});
		}
		if (!nupi) {
			nupi = selectedMatch.personGuid;
		}
		return nupi;
	}

	jq('#accept-button').click(function () {
		ui.navigate('kenyareg', 'merge', {lpiUid: lpiMatch.personGuid, mpiUid: mpiMatch.personGuid});
	})

	/**
	 * This is the original implementation. It directly extracts the NUPI uses it to retrieve the patient from
	 * KenyaEMR
	 */

	//jq('#accept-button').click(function () {
	//	var nupi = extractNupi(selectedMatch);
	//	jq.getJSON('/' + OPENMRS_CONTEXT_PATH + '/kenyareg/basicSearch/accept.action', {uuid: nupi})
	//		.success(function (data) {
	//			var patId = data;
	//			if (patId) {
	//				ui.navigate('kenyaemr', 'registration/registrationViewPatient', {patientId: patId});
	//			} else {
	//				alert('Patient not found in EMR.');
	//			}
	//		})
	//		.error(function (xhr, status, err) {
	//			alert(err);
	//		})
	//})
});