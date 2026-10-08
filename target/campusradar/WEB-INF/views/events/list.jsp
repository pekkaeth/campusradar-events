<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/WEB-INF/common/header.jspf" %>
<div class="container mt-4">
  <div class="d-flex justify-content-between align-items-center mb-3">
    <h3 class="mb-0">🎉 Campus Events</h3>
    <c:if test="${sessionScope.user.role == 'ORGANIZER' || sessionScope.user.role == 'ADMIN'}">
      <a class="btn btn-primary" href="${pageContext.request.contextPath}/events/create">+ Create event</a>
    </c:if>
  </div>

  <c:if test="${not empty param.msg}"><div class="alert alert-info"><c:out value="${param.msg}"/></div></c:if>
  <c:if test="${not empty error}"><div class="alert alert-danger"><c:out value="${error}"/></div></c:if>

  <div class="row g-3">
    <c:forEach items="${events}" var="e">
      <div class="col-md-6 col-lg-4">
        <div class="card shadow-sm h-100">
          <div class="card-body d-flex flex-column">
            <div class="d-flex justify-content-between">
              <h5 class="card-title"><c:out value="${e.title}"/></h5>
              <c:choose>
                <c:when test="${e.status == 'ONGOING'}"><span class="badge bg-danger align-self-start">LIVE NOW</span></c:when>
                <c:when test="${e.status == 'UPCOMING'}"><span class="badge bg-primary align-self-start">Upcoming</span></c:when>
                <c:otherwise><span class="badge bg-secondary align-self-start">Ended</span></c:otherwise>
              </c:choose>
            </div>
            <span class="badge bg-light text-dark border align-self-start mb-2"><c:out value="${e.category}"/></span>
            <p class="card-text text-muted small"><c:out value="${e.description}"/></p>
            <p class="small mb-1">📍 <c:out value="${e.venueNote}"/></p>
            <p class="small mb-1">🕒 <fmt:formatDate value="${e.startTime}" pattern="dd MMM, HH:mm"/> – <fmt:formatDate value="${e.endTime}" pattern="HH:mm"/></p>
            <p class="small mb-3">By <strong><c:out value="${e.organizerName}"/></strong> · ${e.goingCount}/${e.capacity} going</p>
            <div class="mt-auto">
              <c:choose>
                <c:when test="${e.status == 'ENDED'}"><span class="text-muted small">This event has ended.</span></c:when>
                <c:when test="${e.going}">
                  <form method="post" action="${pageContext.request.contextPath}/events/cancel">
                    <input type="hidden" name="eventId" value="${e.id}">
                    <button class="btn btn-outline-danger btn-sm">Cancel RSVP</button>
                  </form>
                </c:when>
                <c:otherwise>
                  <form method="post" action="${pageContext.request.contextPath}/events/rsvp">
                    <input type="hidden" name="eventId" value="${e.id}">
                    <button class="btn btn-success btn-sm">RSVP</button>
                  </form>
                </c:otherwise>
              </c:choose>
            </div>
          </div>
        </div>
      </div>
    </c:forEach>
    <c:if test="${empty events}"><p class="text-muted">No events yet.</p></c:if>
  </div>
</div>
<%@ include file="/WEB-INF/common/footer.jspf" %>