document.getElementById("form_registration").addEventListener("submit", function () {
    document.getElementById('alert_wait').innerHTML='<div th:if="*{loading}" class="loader_container" >\n' +
        '        <div id="loader" class="loader" ></div>\n' +
        '    </div>'
});