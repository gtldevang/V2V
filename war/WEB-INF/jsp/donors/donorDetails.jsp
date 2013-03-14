<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<div class="formInTabPane printableArea">
	<br />
	<div id="${donorSummaryBarcodeId}"></div>
	<c:if test="${donorFields.donorNumber.hidden != true }">
		<div>
			<label>${donorFields.donorNumber.displayName}</label>
			<label>${donor.donorNumber}</label>
		</div>
	</c:if>
	<c:if test="${donorFields.firstName.hidden != true }">
		<div>
			<label>${donorFields.firstName.displayName}</label>
			<label>${donor.firstName}</label>
		</div>
	</c:if>
	<c:if test="${donorFields.middleName.hidden != true }">
		<div>
			<label>${donorFields.middleName.displayName}</label>
			<label>${donor.middleName}</label>
		</div>
	</c:if>
	<c:if test="${donorFields.lastName.hidden != true }">
		<div>
			<label>${donorFields.lastName.displayName}</label>
			<label>${donor.lastName}</label>
		</div>
	</c:if>
	<c:if test="${donorFields.birthDate.hidden != true }">
		<div>
			<label>${donorFields.birthDate.displayName}</label>
			<label>${donor.birthDate}</label>
		</div>
	</c:if>
	<c:if test="${donorFields.gender.hidden != true }">
		<div>
			<label>${donorFields.gender.displayName}</label>
			<label>${donor.gender}</label>
		</div>
	</c:if>
	<c:if test="${donorFields.bloodGroup.hidden != true }">
		<div>
			<label>${donorFields.bloodGroup.displayName}</label>
			<label>${donor.bloodGroup}</label>
		</div>
	</c:if>
	<c:if test="${donorFields.address.hidden != true }">
		<div>
			<label>${donorFields.address.displayName}</label>
			<label>${donor.address}</label>
		</div>
	</c:if>
	<c:if test="${donorFields.city.hidden != true }">
		<div>
			<label>${donorFields.city.displayName}</label>
			<label>${donor.city}</label>
		</div>
	</c:if>
	<c:if test="${donorFields.province.hidden != true }">
		<div>
			<label>${donorFields.province.displayName}</label>
			<label>${donor.province}</label>
		</div>
	</c:if>
	<c:if test="${donorFields.district.hidden != true }">
		<div>
			<label>${donorFields.district.displayName}</label>
			<label>${donor.district}</label>
		</div>
	</c:if>
	<c:if test="${donorFields.state.hidden != true }">
		<div>
			<label>${donorFields.state.displayName}</label>
			<label>${donor.state}</label>
		</div>
	</c:if>
	<c:if test="${donorFields.country.hidden != true }">
		<div>
			<label>${donorFields.country.displayName}</label>
			<label>${donor.country}</label>
		</div>
	</c:if>
	<c:if test="${donorFields.zipcode.hidden != true }">
		<div>
			<label>${donorFields.zipcode.displayName}</label>
			<label>${donor.zipcode}</label>
		</div>
	</c:if>
	<c:if test="${donorFields.notes.hidden != true }">
		<div>
			<label>${donorFields.notes.displayName}</label>
			<label>${donor.notes}</label>
		</div>
	</c:if>
	<br />
	<div>
		<label>${donorFields.lastUpdatedTime.displayName}</label>
		<label style="width: auto;">${donor.lastUpdated}</label>
	</div>
	<div>
		<label>${donorFields.lastUpdatedBy.displayName}</label>
		<label style="width: auto;">${donor.lastUpdatedBy}</label>
	</div>
		<hr />
	</div>