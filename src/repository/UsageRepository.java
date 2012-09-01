package repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import model.ProductUsage;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class UsageRepository {
	@PersistenceContext
	private EntityManager em;

	public void saveUsage(ProductUsage productUsage) {
		em.persist(productUsage);
		em.flush();
	}

	public ProductUsage findProductUsage(String productNumber) {
		ProductUsage productUsage = null;
		if (productNumber != null && productNumber.length() > 0) {
			String queryString = "SELECT p FROM ProductUsage p WHERE p.productNumber = :productNumber and p.isDeleted= :isDeleted";
			TypedQuery<ProductUsage> query = em.createQuery(queryString,
					ProductUsage.class);
			query.setParameter("isDeleted", Boolean.FALSE);
			List<ProductUsage> productUsages = query.setParameter(
					"productNumber", productNumber).getResultList();
			if (productUsages != null && productUsages.size() > 0) {
				productUsage = productUsages.get(0);
			}
		}
		return productUsage;
	}

	public ProductUsage updateUsage(ProductUsage productUsage) {
		ProductUsage existingProductUsage = findProductUsage(productUsage
				.getProductNumber());
		existingProductUsage.copy(productUsage);
		em.merge(existingProductUsage);
		em.flush();
		return existingProductUsage;
	}

	public void deleteAllUsages() {
		Query query = em.createQuery("DELETE FROM ProductUsage u");
		query.executeUpdate();
	}
}