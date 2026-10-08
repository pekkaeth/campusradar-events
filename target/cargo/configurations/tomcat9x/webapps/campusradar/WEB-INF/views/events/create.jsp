<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/WEB-INF/common/header.jspf" %>
<div class="container mt-4" style="max-width:600px;">
  <h3>Create an event</h3>
  <c:if test="${not empty error}"><div class="alert alert-danger"><c:out value="${error}"/></div></c:if>
  <form method="post" action="${pageContext.request.contextPath}/events/create" class="card card-body shadow-sm">
    <div class="mb-3"><label class="form-label">Title</label>
      <input type="text" name="title" class="form-control" minlength="3" maxlength="120" required></div>
    <div class="row">
      <div class="col-md-6 mb-3"><label class="form-label">Category</label>
        <select name="category" class="form-select">
          <option>Workshop</option><option>Hackathon</option><option>Cultural</option>
          <option>Sports</option><option>Seminar</option><option>Other</option>
        </select></div>
      <div class="col-md-6 mb-3"><label class="form-label">Capacity</label>
        <input type="number" name="capacity" class="form-control" min="1" value="100" required></div>
    </div>
    <div class="mb-3"><label class="form-label">Venue</label>
      <input type="text" name="venue" class="form-control" maxlength="120" placeholder="e.g. Main Auditorium"></div>
    <div class="row">
      <div class="col-md-6 mb-3"><label class="form-label">Starts</label>
        <input type="datetime-local" name="start" class="form-control" required></div>
      <div class="col-md-6 mb-3"><label class="form-label">Ends</label>
        <input type="datetime-local" name="end" class="form-control" required></div>
    </div>
    <div class="mb-3"><label class="form-label">Description</label>
      <textarea name="description" class="form-control" rows="3" maxlength="500"></textarea></div>
    <button class="btn btn-primary">Create event</button>
    <a class="btn btn-link" href="${pageContext.request.contextPath}/events/list">Cancel</a>
  </form>
</div>
<%@ include file="/WEB-INF/common/footer.jspf" %>