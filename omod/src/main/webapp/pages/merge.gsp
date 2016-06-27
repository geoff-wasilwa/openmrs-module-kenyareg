<%
    ui.decorateWith("kenyaemr", "standardPage", [layout: "sidebar"])

    def menuItems = [
            [label: "Back", iconProvider: "kenyaui", icon: "buttons/back.png", href: ui.pageLink("kenyareg", "registryHome")]
    ]
%>

<div class="ke-page-sidebar">
    ${ui.includeFragment("kenyaui", "widget/panelMenu", [heading: "Back", items: menuItems])}

    ${ui.includeFragment("kenyareg", "basicSearch")}
</div>

<div class="ke-page-content">
    <td>${ui.includeFragment("kenyareg", "status")}</td>
    <table>
        <tr>
            <td>
                ${ui.includeFragment("kenyareg", "multipleResults")}
                ${ui.includeFragment("kenyareg", "individualResult")}
            </td>
            <td>
                ${ui.includeFragment("kenyareg", "multipleMpiResults")}
                ${ui.includeFragment("kenyareg", "individualMpiResult")}
            </td>
        </tr>
    </table>
    </div>
</div>