<%
    ui.decorateWith("kenyaemr", "standardPage", [ layout: "sidebar" ])

    def menuItems = [
            [ label: "Fingerprint", iconProvider: "kenyareg", icon: "buttons/fingerprint.png", href: ui.pageLink("kenyareg", "registryHome") ],
            [ label: "Identifier", iconProvider: "kenyareg", icon: "buttons/identifier.png", href: ui.pageLink("kenyareg", "registryHome") ],
            [ label: "Basic", iconProvider: "kenyareg", icon: "buttons/basic.png", href: ui.pageLink("kenyareg", "basicSearch") ],
            [ label: "Advanced", iconProvider: "kenyareg", icon: "buttons/advanced.png", href: ui.pageLink("kenyareg", "registryHome") ]
    ]
%>

<div class="ke-page-sidebar">
    ${ ui.includeFragment("kenyaui", "widget/panelMenu", [ heading: "Find Patient", items: menuItems ]) }
</div>