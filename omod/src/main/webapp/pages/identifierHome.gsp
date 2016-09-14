<%
    ui.decorateWith("kenyaemr", "standardPage", [layout: "sidebar"])

    def menuItems = [
            [label: "Back", iconProvider: "kenyaui", icon: "buttons/back.png", href: ui.pageLink("kenyareg", "registryHome")]
    ]
%>

<div class="ke-page-sidebar">
    ${ui.includeFragment("kenyaui", "widget/panelMenu", [heading: "Back", items: menuItems])}

    ${ui.includeFragment("kenyareg", "identifierSearch")}
</div>

${ui.includeFragment("kenyareg", "results")}