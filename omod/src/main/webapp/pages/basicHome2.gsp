<%
    ui.decorateWith("kenyaemr", "standardPage", [layout: "sidebar"])

    def menuItems = [
            [label: "Back", iconProvider: "kenyaui", icon: "buttons/back.png", href: ui.pageLink("kenyareg", "basicHome")]
    ]
%>

<div class="ke-page-sidebar">
    ${ui.includeFragment("kenyaui", "widget/panelMenu", [heading: "Back", items: menuItems])}

    ${ui.includeFragment("kenyareg", "basicSearch")}
</div>

<div class="ke-page-content">
    ${ui.includeFragment("kenyareg", "status")}

    ${ui.includeFragment("kenyareg", "multipleResults")}

    ${ui.includeFragment("kenyareg", "individualResult")}

    <div id="resultsi">

    </div>
</div>