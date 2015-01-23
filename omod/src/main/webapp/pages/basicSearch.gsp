<%
    ui.decorateWith("kenyaemr", "standardPage", [ layout: "sidebar" ])

    def menuItems = [
            [ label: "Back", iconProvider: "kenyaui", icon: "buttons/back.png", href: ui.pageLink("kenyareg", "registryHome") ]
    ]
%>

<div class="ke-page-sidebar">
    ${ ui.includeFragment("kenyaui", "widget/panelMenu", [ heading: "Basic Search", items: menuItems ]) }
</div>

<div class="ke-page-content">
    ${ ui.includeFragment("kenyareg", "basicSearch", [heading: "Basic Search Here" ]) }
</div>