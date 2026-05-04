<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>


<petclinic:layout pageName="owners">
    <jsp:attribute name="customScript">
        <link rel="stylesheet" href="/webjars/flatpickr/4.6.13/dist/flatpickr.min.css">
        <script src="/webjars/flatpickr/4.6.13/dist/flatpickr.js"></script>
        <script>
            flatpickr("#measurementDate", {});
        </script>
    </jsp:attribute>
    <jsp:body>
        <h2><c:if test="${weightRecord['new']}">New </c:if>Weight Record</h2>

        <span id="pet"><strong>Pet</strong></span>
        <table class="table table-striped" aria-describedby="pet">
            <thead>
            <tr>
                <th scope="col">Name</th>
                <th scope="col">Birth Date</th>
                <th scope="col">Type</th>
                <th scope="col">Owner</th>
            </tr>
            </thead>
            <tr>
                <td><c:out value="${weightRecord.pet.name}"/></td>
                <td><petclinic:localDate date="${weightRecord.pet.birthDate}" pattern="yyyy/MM/dd"/></td>
                <td><c:out value="${weightRecord.pet.type.name}"/></td>
                <td><c:out value="${weightRecord.pet.owner.firstName} ${weightRecord.pet.owner.lastName}"/></td>
            </tr>
        </table>

        <form:form modelAttribute="weightRecord" class="form-horizontal">
            <div class="form-group has-feedback">
                <petclinic:inputField label="Weight (kg)" name="weightKg"/>
                <petclinic:inputField label="Measurement Date" name="measurementDate"/>
            </div>

            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <input type="hidden" name="petId" value="${weightRecord.pet.id}"/>
                    <button class="btn btn-primary" type="submit">Add Weight Record</button>
                </div>
            </div>
        </form:form>

    </jsp:body>

</petclinic:layout>
