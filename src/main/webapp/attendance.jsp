<%-- 
    Document   : attendance
    Created on : 02-Feb-2026, 5:15:27 pm
    Author     : vaishnavi.dhole
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<title>Labour Attendance</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<style>
body { font-family: Arial; padding: 10px; }
label { font-weight: bold; }
select, button { width: 100%; padding: 10px; margin-top: 5px; }
button { background: green; color: white; font-size: 16px; }
</style>
</head>

<body>

<h3>📋 Labour Attendance</h3>

<form method="post" action="attendance" enctype="multipart/form-data">

<label>Worker</label>
<select name="workerId" required>
    <option value="">-- Select Worker --</option>
    <!-- Populate from DB -->
    <option value="1">Ramesh</option>
    <option value="2">Suresh</option>
</select>

<label>Work Type</label>
<select name="workTypeId" required>
    <option value="">-- Select Work --</option>
    <option value="1">Mason</option>
    <option value="2">Helper</option>
</select>

<label>Day Type</label>
<select name="dayType" required>
    <option value="FULL">Full Day</option>
    <option value="HALF">Half Day</option>
</select>

<label>📸 Worker Photo</label>
<input type="file" name="photo" accept="image/*" capture="camera" required>

<input type="hidden" name="latitude" id="latitude">
<input type="hidden" name="longitude" id="longitude">
<input type="hidden" name="accuracy" id="accuracy">

<button type="submit">✅ Submit Attendance</button>
</form>
<div style="text-align:right">
    <a href="<%=request.getContextPath()%>/logout">Logout</a>
</div>

<script>
if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(
        function(pos) {
            document.getElementById("latitude").value = pos.coords.latitude;
            document.getElementById("longitude").value = pos.coords.longitude;
            document.getElementById("accuracy").value = pos.coords.accuracy;
        },
        function() {
            alert("GPS is mandatory. Please enable location.");
        },
        { enableHighAccuracy: true }
    );
} else {
    alert("GPS not supported");
}
</script>

</body>
</html>
