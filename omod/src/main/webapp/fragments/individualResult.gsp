<%
    ui.decorateWith("kenyaui", "panel", [heading: "Details"])

    ui.includeCss("kenyareg", "bootstrap.css")
    ui.includeCss("kenyareg", "kenyareg.css")

    ui.includeJavascript("kenyareg", "bootstrap.js")
%>

<script type="application/javascript">
    jq(function () {
        jq.getJSON('${ui.actionLink("kenyareg", "basicSearch", "search")}', function (result) {
            jq('#results').html(result);
        });
    });
</script>

<div class="panel panel-default">
    <div class="panel-heading">Unique Identifiers</div>
    <div class="panel-body unique-identifiers">
    </div>
</div>

<div class="panel panel-default">
    <div class="panel-heading">Personal Information</div>
    <div class="panel-body">
        <div class="row">
            <div class="col-md-4">
                First Name: <span class="first-name"></span>
            </div>
            <div class="col-md-4">
                Middle Name: <span class="middle-name"></span>
            </div>
            <div class="col-md-4">
                Last Name: <span class="last-name"></span>
            </div>
        </div>
        <div class="row">
            <div class="col-md-4">
                Sex: <span class="sex">Male</span>
            </div>
            <div class="col-md-4">
                Birth Date: <span class="birth-date"></span>
            </div>
        </div>
        <div>Marital Status: <span class="marital-status"></span></div>
        <div>Alive: <span class="alive-status"></span></div>
    </div>
</div>

    <div class="panel panel-default">
        <div class="panel-heading">Parent Information</div>
        <div class="panel-body">
            <div class="row">
                <div class="col-md-4">
                    Father's First Name: <span class="father-first-name"></span>
                </div>
                <div class="col-md-4">
                    Father's Middle Name: <span class="father-middle-name"></span>
                </div>
                <div class="col-md-4">
                    Father's Last Name: <span class="father-last-name"></span>
                </div>
            </div>
            <div class="row">
                <div class="col-md-4">
                    Mother's First Name: <span class="mother-first-name"></span>
                </div>
                <div class="col-md-4">
                    Mother's Middle Name: <span class="mother-middle-name"></span>
                </div>
                <div class="col-md-4">
                    Mother's Last Name: <span class="mother-last-name"></span>
                </div>
            </div>
        </div>
    </div>

    <button id="accept-button" class="button">Accept</button>
</div>