<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="owners">
    <jsp:body>
        <h2>Weight History for <c:out value="${pet.name}"/></h2>

        <spring:url value="/owners/{ownerId}/pets/{petId}/weights/new" var="addWeightUrl">
            <spring:param name="ownerId" value="${pet.owner.id}"/>
            <spring:param name="petId" value="${pet.id}"/>
        </spring:url>
        <a href="${fn:escapeXml(addWeightUrl)}" class="btn btn-primary">Add Weight Record</a>

        <spring:url value="/owners/{ownerId}" var="ownerUrl">
            <spring:param name="ownerId" value="${pet.owner.id}"/>
        </spring:url>
        <a href="${fn:escapeXml(ownerUrl)}" class="btn btn-default">Back to Owner</a>

        <br/><br/>

        <c:choose>
            <c:when test="${empty weightRecords}">
                <p>No weight history recorded yet.</p>
            </c:when>
            <c:otherwise>
                <c:if test="${fn:length(weightRecords) == 1}">
                    <p><em>Add more records to see a trend.</em></p>
                </c:if>
                <c:if test="${fn:length(weightRecords) >= 2}">
                    <script src="${pageContext.request.contextPath}/webjars/chart.js/4.5.0/dist/chart.umd.js"></script>
                    <canvas id="weightChart" style="max-height:400px;"></canvas>
                    <script>
                        (function () {
                            var labels = [<c:forEach var="wr" items="${weightRecords}" varStatus="s">'<c:out value="${wr.measurementDate}"/>'<c:if test="${!s.last}">,</c:if></c:forEach>];
                            var data = [<c:forEach var="wr" items="${weightRecords}" varStatus="s"><c:out value="${wr.weightKg}"/><c:if test="${!s.last}">,</c:if></c:forEach>];
                            new Chart(document.getElementById('weightChart'), {
                                type: 'line',
                                data: {
                                    labels: labels,
                                    datasets: [{
                                        label: 'Weight (kg)',
                                        data: data,
                                        borderColor: 'rgba(75, 192, 192, 1)',
                                        backgroundColor: 'rgba(75, 192, 192, 0.2)',
                                        tension: 0.1,
                                        fill: true
                                    }]
                                },
                                options: {
                                    responsive: true,
                                    scales: {
                                        y: {
                                            title: { display: true, text: 'Weight (kg)' },
                                            beginAtZero: false
                                        },
                                        x: {
                                            title: { display: true, text: 'Date' }
                                        }
                                    }
                                }
                            });
                        })();
                    </script>
                    <br/>
                </c:if>

                <span id="weightHistory"><strong>Weight History</strong></span>
                <table class="table table-striped" aria-describedby="weightHistory">
                    <thead>
                    <tr>
                        <th scope="col">Measurement Date</th>
                        <th scope="col">Weight (kg)</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="wr" items="${weightRecords}">
                        <tr>
                            <td><petclinic:localDate date="${wr.measurementDate}" pattern="yyyy/MM/dd"/></td>
                            <td><c:out value="${wr.weightKg}"/></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>
    </jsp:body>
</petclinic:layout>
