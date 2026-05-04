<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="owners">
    <jsp:attribute name="customScript">
        <script src="/webjars/chart.js/4.5.0/dist/chart.umd.js"></script>
        <script>
            <c:if test="${weightRecords.size() >= 2}">
                // Prepare data for Chart.js
                const labels = [
                    <c:forEach var="weightRecord" items="${weightRecords}" varStatus="status">
                        '<c:out value="${weightRecord.measurementDate}"/>'<c:if test="${!status.last}">,</c:if>
                    </c:forEach>
                ];
                
                const data = [
                    <c:forEach var="weightRecord" items="${weightRecords}" varStatus="status">
                        ${weightRecord.weightKg}<c:if test="${!status.last}">,</c:if>
                    </c:forEach>
                ];
                
                const ctx = document.getElementById('weightChart').getContext('2d');
                const chart = new Chart(ctx, {
                    type: 'line',
                    data: {
                        labels: labels,
                        datasets: [{
                            label: 'Weight (kg)',
                            data: data,
                            borderColor: 'rgb(75, 192, 192)',
                            backgroundColor: 'rgba(75, 192, 192, 0.1)',
                            tension: 0.1
                        }]
                    },
                    options: {
                        responsive: true,
                        plugins: {
                            title: {
                                display: true,
                                text: 'Weight Progress Chart'
                            }
                        },
                        scales: {
                            y: {
                                beginAtZero: false,
                                title: {
                                    display: true,
                                    text: 'Weight (kg)'
                                }
                            },
                            x: {
                                title: {
                                    display: true,
                                    text: 'Measurement Date'
                                }
                            }
                        }
                    }
                });
            </c:if>
        </script>
    </jsp:attribute>
    <jsp:body>
        <h2>Weight History for <c:out value="${pet.name}"/></h2>

        <c:choose>
            <c:when test="${empty weightRecords}">
                <p>No weight history recorded yet.</p>
            </c:when>
            <c:otherwise>
                <c:if test="${weightRecords.size() >= 2}">
                    <div style="width: 80%; margin: 20px auto;">
                        <canvas id="weightChart"></canvas>
                    </div>
                </c:if>
                
                <c:if test="${weightRecords.size() == 1}">
                    <p>Add more records to see a trend</p>
                </c:if>

                <span id="weightRecords"><strong>Weight Records</strong></span>
                <table class="table table-striped" aria-describedby="weightRecords">
                    <thead>
                    <tr>
                        <th scope="col">Measurement Date</th>
                        <th scope="col">Weight (kg)</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="weightRecord" items="${weightRecords}">
                        <tr>
                            <td><petclinic:localDate date="${weightRecord.measurementDate}" pattern="yyyy/MM/dd"/></td>
                            <td><c:out value="${weightRecord.weightKg}"/></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>

        <p>
            <a href="<spring:url value="/owners/{ownerId}/pets/{petId}/weights/new" htmlEscape="true">
                <spring:param name="ownerId" value="${pet.owner.id}"/>
                <spring:param name="petId" value="${pet.id}"/>
            </spring:url>" class="btn btn-primary">Add Weight Record</a>
        </p>

    </jsp:body>

</petclinic:layout>
