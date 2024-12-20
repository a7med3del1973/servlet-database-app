<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Student Tracker App</title>
    <style>

    </style>
    <link type="text/css" rel="stylesheet" href="css/style.css">
    <script>
        function confirmDelete(deleteUrl) {
            if (confirm("Are you sure to delete this student?")) {
                window.location.href = deleteUrl;
            }
        }
    </script>
</head>
<body>

<h2>Student Tracker</h2>
<%--put new button add student--%>
<input type="button" value="Add Student"
       onclick="window.location.href='add-student-form.jsp';return false;"
       class="add-student-button"/>

<table>
    <thead>
    <tr>
        <th>First Name</th>
        <th>Last Name</th>
        <th>Email</th>
        <th>Action</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="tempStudent" items="${STUDENT_LIST}">
        <%--set up the link for each student --%>
    <c:url var="templink" value="StudentControllerServlet">
        <c:param name="command" value="LOAD"/>
        <c:param name="studentId" value="${tempStudent.id}"/>
    </c:url>

        <%--    set up the link for delete --%>
    <c:url var="deletelink" value="StudentControllerServlet">
        <c:param name="command" value="DELETE"/>
        <c:param name="studentId" value="${tempStudent.id}"/>
    </c:url>

    <tr>
        <td>${tempStudent.firstName} </td>
        <td>${tempStudent.lastName} </td>
        <td>${tempStudent.email} </td>
        <td>
            <a href="${templink}">Update</a>
            <a href="#" onclick="confirmDelete('${deletelink}'); return false;">| Delete</a>
        </td>
    </tr>
    </c:forEach>
</table>

</body>
</html>