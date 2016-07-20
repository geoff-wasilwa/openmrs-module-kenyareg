<%
    ui.decorateWith("kenyaui", "panel", [heading: "Search By Identifier"])

    ui.includeJavascript("kenyareg", "kenyareg.js")

    def fields =
            [
                    [
                            [
                                    formFieldName: "personIdentifier",
                                    label        : "Enter Person Identifier",
                                    class        : java.lang.String,
                                    id           : "personIdentifier"
                            ]
                    ],
                    [
                            [
                                    hiddenInputName: "server",
                                    value          : 1
                            ]
                    ]
            ]

    def formFieldName = "identifierTypeId";
    def options = [

            [
                    value: "1",
                    label: "NUPI"
            ],
            [
                    value: "2",
                    label: "CCC UPN"
            ],
            [
                    value: "3",
                    label: "MPI"
            ],
            [
                    value: "4",
                    label: "CCC LPN"
            ]


    ]

%>

<form id="search-form" method="post" action="${ui.actionLink("kenyareg", "identifierSearch", "search")}">
    <div class="ke-form-globalerrors" style="display: none"></div>
    <fieldset>
        ${ui.includeFragment("kenyaui", "widget/radioButtons", [formFieldName: formFieldName, options: options, selected: "1", separator: "&nbsp;"])}
        <% fields.each { %>
        ${ui.includeFragment("kenyaui", "widget/rowOfFields", [fields: it])}
        <% } %>

    </fieldset>

    <div class="ke-form-footer">
        <button class="search" type="submit"><img src="${ui.resourceLink("kenyaui", "images/glyphs/ok.png")}"/> Search
        </button>
    </div>
</form>