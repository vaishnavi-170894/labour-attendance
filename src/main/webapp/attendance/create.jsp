<%-- 
    Document   : create
    Created on : 03-Feb-2026
    Author     : vaishnavi.dhole
--%>

<%@ page import="java.util.List" %>
<%@ page import="com.attendance.dto.User" %>
<%@ page import="com.attendance.dto.Worker" %>
<%@ page import="com.attendance.dto.WorkType" %>

<%
User user = (User) session.getAttribute("LOGIN");
if (user == null || !"SUPERVISOR".equals(user.getRole())) {
    response.sendRedirect(request.getContextPath() + "/index.jsp");
    return;
}

List<Worker> workerList = (List<Worker>) request.getAttribute("workerList");
List<WorkType> workTypeList = (List<WorkType>) request.getAttribute("workTypeList");
%>

<!DOCTYPE html>
<html>
<head>
    <title>Mark Attendance</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/app.css">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

</head>

<body>
<div class="card">
<h2>Mark Attendance</h2>

<form method="post"
      action="<%=request.getContextPath()%>/attendance?action=create"
      enctype="multipart/form-data">

    <!-- ================= WORKER DROPDOWN ================= -->
    <label>Worker Name</label><br>

 <input type="text"
       id="workerSearch"
       placeholder="Type to search worker"
       onkeyup="filterWorkers()"
       autocomplete="off">

   <select name="workerId" required>
        <option value="">-- Select Worker --</option>
        <% if (workerList  != null) {
            for (Worker w : workerList ) { %>
                <option value="<%= w.getWorkerId()%>">
                    <%= w.getWorkerName()%>
                </option>
        <%  }
        } %>
    </select>

    <br><br>

    <!-- ================= WORK TYPE DROPDOWN ================= -->
    <label>Work Type</label><br>

    <select name="workTypeId" required>
        <option value="">-- Select Work Type --</option>
        <% if (workTypeList != null) {
            for (WorkType wt : workTypeList) { %>
                <option value="<%= wt.getWorkTypeId() %>">
                    <%= wt.getWorkTypeName() %>
                </option>
        <%  }
        } %>
    </select>

    <br><br>

    <!-- ================= DAY TYPE ================= -->
    <label>Day Type</label><br>
    <select name="dayType">
        <option value="FULL">Full Day</option>
        <option value="HALF">Half Day</option>
    </select>

    <br><br>

    <!-- ================= PHOTO ================= -->
    <label>Worker Photo</label><br>
    <input type="file" name="photo" accept="image/*" capture="camera" required>

    <br><br>

    <!-- ================= GPS (HIDDEN) ================= -->
    <input type="hidden" id="lat" name="latitude">
    <input type="hidden" id="lng" name="longitude">
    <input type="hidden" id="acc" name="accuracy">

    <button type="submit">Submit Attendance</button>
</form>
</div>
<script>
navigator.geolocation.getCurrentPosition(function(p){
    document.getElementById("lat").value = p.coords.latitude;
    document.getElementById("lng").value = p.coords.longitude;
    document.getElementById("acc").value = p.coords.accuracy;
});
function filterWorkers() {
    let input = document.getElementById("workerSearch").value.toLowerCase();
    let select = document.getElementById("workerSelect");
    let options = select.options;

    for (let i = 0; i < options.length; i++) {
        let text = options[i].text.toLowerCase();
        options[i].style.display =
            text.includes(input) ? "" : "none";
    }
}
</script>

<br>
<a href="<%=request.getContextPath()%>/attendance">Back</a>

</body>
</html>
