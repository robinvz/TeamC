<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  User: Mathias
  Date: 8-2-13
  Time: 10:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
      <form:form method="POST" modelAttribute="user">
          <fieldset>
              <legend>Edit Profile</legend>
              <table>
                  <tr>
                      <td><form:label path="email">Email</form:label></td>
                      <td><form:input path="email"/></td>
                  </tr>
                  <tr>
                      <td><form:label path="password">Password</form:label></td>
                      <td><form:input path="password"/></td>
                  </tr>
              </table>
          </fieldset>
          <button id="search">Search</button>
      </form:form>
</body>
</html>