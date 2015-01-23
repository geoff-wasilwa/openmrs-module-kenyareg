<%
    ui.decorateWith("kenyaui", "panel", [heading: "Basic Search"])

    def fields =
            [
                    [
                            [
                                    formFieldName: "surname",
                                    label        : "Surname",
                                    class        : java.lang.String
                            ]
                    ],
                    [
                            [
                                    formFieldName: "firstName",
                                    label        : "First Name",
                                    class        : java.lang.String
                            ]
                    ]
            ]
%>

<form id="basic-search-form" method="post" action="${ ui.actionLink("kenyareg", "basicSearch", "search") }">
        <div class="ke-form-globalerrors" style="display: none"></div>

        <fieldset>
                <legend>Criteria</legend>
                <% fields.each { %>
                ${ ui.includeFragment("kenyaui", "widget/rowOfFields", [ fields: it ]) }
                <% } %>
        </fieldset>

        <div class="ke-form-footer">
                <button type="submit"><img src="${ ui.resourceLink("kenyaui", "images/glyphs/ok.png") }" /> Search</button>
        </div>
</form>