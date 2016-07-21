<%
    ui.decorateWith("kenyaui", "panel", [heading: "Edit Person Details"])

    ui.includeCss("kenyareg", "kenyareg.css")

    def initializeFields = { fieldCollection ->
        fieldCollection.each { field ->
            def property = mergedProperties.find { key, value ->
                key == field.id
            }
            if (property) {
                field.initialValue = property.value
            }
        }
    }

    def removeFieldsInConflict = { fieldCollection ->
        def mergedFields = fieldCollection.findAll { field ->
            def isInConflict = conflictedProperties.find { key, value ->
                return field.id == key
            }
            return !isInConflict
        }
        return mergedFields
    }

    def nameFieldDefinitions = [
        [ formFieldName: "lastName", label : "Surname", class : java.lang.String, id : "lastName" ],
        [ formFieldName: "firstName", label : "First Name", class : java.lang.String, id : "firstName" ],
        [ formFieldName: "middleName", label : "Middle Name", class : java.lang.String, id : "middleName" ],
        [ formFieldName: "otherName", label : "Other Name(s)", class : java.lang.String, id : "otherName" ],
    ]
    
    def otherDemoFieldDefinitions = [
        [ formFieldName: "birthdate", label : "Birth Date", class : java.util.Date, id : "birthdate" ],
        [ formFieldName: "sex", label : "Sex", class : java.lang.String, id : "sex",
            config : [
                options : [
                    [ value: 'M', label: 'Male' ],
                    [ value: 'F', label: 'Female' ]
                ]
            ]
        ],
        [ formFieldName: "maritalStatus", label : "Marital Status", class : java.lang.String, id : "maritalStatus",
            config : [
                options  : [
                    [ value: 'single', label: 'Single' ],
                    [ value: 'marriedPolygamous', label: 'Married Polygamous' ],
                    [ value: 'marriedMonogamous', label: 'Married Monogamous' ],
                    [ value: 'divorced', label: 'Divorced' ],
                    [ value: 'widowed', label: 'Widowed' ],
                    [ value: 'cohabitating', label: 'Cohabitating' ]
                ]
            ]
        ],
    ]

    def fatherFieldDefinitions = [
        [ formFieldName: "fathersFirstName", label : "Father's First Name", class : java.lang.String, id : "fathersFirstName" ],
        [ formFieldName: "fathersMiddleName", label : "Father's Middle Name", class : java.lang.String, id : "fathersMiddleName" ],
        [ formFieldName: "fathersLastName", label : "Father's Last Name", class : java.lang.String, id : "fathersLastName" ]
    ]

    def motherFieldDefinitions = [
        [ formFieldName: "mothersFirstName", label : "Mother's First Name", class : java.lang.String, id : "mothersFirstName" ],
        [ formFieldName: "mothersMiddleName", label : "Mother's Middle Name", class : java.lang.String, id : "mothersMiddleName" ],
        [ formFieldName: "mothersLastName", label : "Mother's Last Name", class : java.lang.String, id : "mothersLastName" ]
    ]

    initializeFields(nameFieldDefinitions)
    initializeFields(otherDemoFieldDefinitions)
    initializeFields(fatherFieldDefinitions)
    initializeFields(motherFieldDefinitions)
    
    def mergedNameFields = removeFieldsInConflict(nameFieldDefinitions);
    def mergedOtherDemoFields = removeFieldsInConflict(otherDemoFieldDefinitions);
    def mergedFatherFields = removeFieldsInConflict(fatherFieldDefinitions);
    def mergedMotherFields = removeFieldsInConflict(motherFieldDefinitions);

    nameFields = [ mergedNameFields ]
    otherDemoFields = mergedOtherDemoFields.collect { [it] }
    parentFields = [ mergedFatherFields, mergedMotherFields ]

    def conflictingFields = []
    conflictedProperties.each { property, lpiMpiValue ->
        def conflictedPair = []
        def fieldDefinitions = nameFieldDefinitions + otherDemoFieldDefinitions + fatherFieldDefinitions + motherFieldDefinitions
        def definition = fieldDefinitions.find { it.id == property }
        if (definition) {
            lpiMpiValue.each { source, value ->
                def lpiMpi = [:]
                definition.each { defKey, defValue ->
                    lpiMpi.put(defKey, defValue)
                }
                lpiMpi.initialValue = value
                def resolveInputName = lpiMpi.formFieldName
                lpiMpi.formFieldName = "conflict-${source}-${lpiMpi.formFieldName}"
                lpiMpi.label += " (${source.toUpperCase()})"
                def resolveInput =
                  "<input name=\'resolve-${resolveInputName}\' data-field-name=\'${lpiMpi.formFieldName}\' type=\'radio\' class=\'resolve\'>"
                conflictedPair.push(lpiMpi)
                conflictedPair.push(resolveInput.toString())
            }
            conflictingFields.push(conflictedPair)
        }
    }
%>

<form id="person-editor-form" method="post" action="${ui.actionLink("kenyareg", "personEditor", "update")}">
    <div class="ke-panel-content">
        <div class="ke-form-globalerrors" style="display: none"></div>
        <fieldset>
            <legend>Demographics</legend>
            <% nameFields.each { %>
                ${ui.includeFragment("kenyaui", "widget/rowOfFields", [fields: it])}
            <% } %>
            <% otherDemoFields.each { %>
                ${ui.includeFragment("kenyaui", "widget/rowOfFields", [fields: it])}
            <% } %>
        </fieldset>
        <fieldset>
            <legend>Parent Information</legend>
            <% parentFields.each { %>
                ${ui.includeFragment("kenyaui", "widget/rowOfFields", [fields: it])}
            <% } %>
        </fieldset>
        <% if (!conflictingFields.empty) { %>
            <fieldset>
                <legend>Conflicts</legend>
                <% conflictingFields.each { %>
                    ${ui.includeFragment("kenyaui", "widget/rowOfFields", [fields: it])}
                <% } %>
            </fieldset>
        <% } %>
    </div>
    <div class="ke-panel-footer">
        <button id="save-button" class="button">Save</button>
        <button id="cancel-button" class="button">Cancel</button>
    </div>
</form>
<script>
  jQuery(function(){
    jQuery(".resolve").on("change", function(){
      var fieldName = jQuery(this).data("fieldName");
      var resolvedFieldName = fieldName.substring(fieldName.lastIndexOf("-") + 1);
      console.log(jQuery("input[name=" + resolvedFieldName + "],select[name=" + resolvedFieldName + "]"));
      if (jQuery(this).is(":checked") && fieldName.indexOf("lpi") > 0) {
        var mpiFieldName = fieldName.replace(/lpi/i, "mpi");
        jQuery("input[name=" + resolvedFieldName + "],select[name=" + resolvedFieldName + "]")
          .attr("name", mpiFieldName);
        jQuery("input[name=" + fieldName + "],select[name=" + fieldName + "]")
          .attr("name", resolvedFieldName);
      }
      else if (jQuery(this).is(":checked") && fieldName.indexOf("mpi") > 0) {
        var lpiFieldName = fieldName.replace(/mpi/i, "lpi");
        jQuery("input[name=" + resolvedFieldName + "],select[name=" + resolvedFieldName + "]")
          .attr("name", lpiFieldName);
        jQuery("input[name=" + fieldName + "],select[name=" + fieldName + "]")
          .attr("name", resolvedFieldName);
      }
    });

    kenyaui.setupAjaxPost('person-editor-form', {
        onSuccess: function (data) {
            kenyaui.notifySuccess("Person details saved!");
            window.location = ui.pageLink("kenyareg", "basicHome");
        }
    });
  });
</script>