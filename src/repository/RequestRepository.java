package repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import model.Request;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Repository
@Transactional
public class RequestRepository {
	@PersistenceContext
	private EntityManager em;

	public void saveRequest(Request request) {
		em.persist(request);
		em.flush();
	}

	public Request updateRequest(Request request, String existingRequestNumber) {
		Request existingRequest = findRequest(existingRequestNumber);
		existingRequest.copy(request);
		em.merge(existingRequest);
		em.flush();
		return existingRequest;
	}

	public Request findRequest(String requestNumber) {
		Request request = null;
		if (requestNumber != null && requestNumber.length() > 0) {
			String queryString = "SELECT p FROM Request p WHERE p.requestNumber = :requestNumber and p.isDeleted= :isDeleted";
			TypedQuery<Request> query = em.createQuery(queryString,
					Request.class);
			query.setParameter("isDeleted", Boolean.FALSE);
			List<Request> requests = query.setParameter("requestNumber",
					requestNumber).getResultList();
			if (requests != null && requests.size() > 0) {
				request = requests.get(0);
			}
		}
		return request;
	}

	public Request findRequest(Long requestId) {
		Request request = null;
		if (requestId != null) {
			String queryString = "SELECT p FROM Request p WHERE p.requestId = :requestId and p.isDeleted= :isDeleted";
			TypedQuery<Request> query = em.createQuery(queryString,
					Request.class);
			query.setParameter("isDeleted", Boolean.FALSE);
			List<Request> requests = query.setParameter("requestId", requestId)
					.getResultList();
			if (requests != null && requests.size() > 0) {
				request = requests.get(0);
			}
		}
		return request;
	}

	public ArrayList<Request> getAllRequests() {
		String queryString = "SELECT p FROM Request p where p.isDeleted = :isDeleted order by p.dateRequested";
		TypedQuery<Request> query = em.createQuery(queryString, Request.class);
		query.setParameter("isDeleted", Boolean.FALSE);
		return new ArrayList<Request>(query.getResultList());
	}

	public void deleteAllRequests() {
		Query query = em.createQuery("DELETE FROM Request p");
		query.executeUpdate();
	}

	public List<Request> getRequests(Date fromDateRequested,
			Date toDateRequested) {
		TypedQuery<Request> query = em
				.createQuery(
						"SELECT p FROM Request p WHERE  p.dateRequested >= :fromDate and p.dateRequested<= :toDate and p.isDeleted = :isDeleted",
						Request.class);
		query.setParameter("fromDate", fromDateRequested);
		query.setParameter("toDate", toDateRequested);
		query.setParameter("isDeleted", Boolean.FALSE);
		List<Request> requests = query.getResultList();
		if (CollectionUtils.isEmpty(requests)) {
			return new ArrayList<Request>();
		}
		return requests;
	}

	public void delete(String existingRequestNumber) {
		Request existingRequest = findRequest(existingRequestNumber);
		existingRequest.setIsDeleted(Boolean.TRUE);
		em.merge(existingRequest);
		em.flush();
	}

	public ArrayList<Request> getAllUnfulfilledRequests() {
		String queryString = "SELECT p FROM Request p where p.status = 'pending' or p.status='partiallyFulfilled' and p.isDeleted = :isDeleted order by p.dateRequested";
		TypedQuery<Request> query = em.createQuery(queryString, Request.class);
		query.setParameter("isDeleted", Boolean.FALSE);
		return new ArrayList<Request>(query.getResultList());
	}
}
