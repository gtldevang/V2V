package viewmodel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import model.Collection;
import model.Product;
import model.TestResult;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.joda.time.DateTime;

public class MonthlyAggregate {
	private TreeMap<Date, Integer> aggregates;

	private MonthlyAggregate() {
	}

	public static MonthlyAggregate createWithCollections(
			ArrayList<Collection> collections) {
		MonthlyAggregate monthlyAggregate = new MonthlyAggregate();
		monthlyAggregate.aggregates = new TreeMap<Date, Integer>(
				new Comparator<Date>() {
					public int compare(Date date, Date date1) {
						return date.compareTo(date1);
					}
				});
		List<Date> dateList = (List<Date>) CollectionUtils.collect(collections,
				new Transformer() {
					public Object transform(Object o) {
						Collection collection = (Collection) o;
						DateTime dateTime = new DateTime(collection
								.getDateCollected());
						DateTime firstDayOfMonth = dateTime.minusDays(dateTime
								.getDayOfMonth() - 1);
						return firstDayOfMonth.toDate();
					}
				});
		HashSet<Date> uniqueDates = new HashSet<Date>(dateList);
		for (Date uniqueDate : uniqueDates) {
			final Date collectionDate = uniqueDate;
			List<Collection> dailyCollections = (List<Collection>) CollectionUtils
					.select(collections, new Predicate() {
						public boolean evaluate(Object o) {
							Collection collection = (Collection) o;
							DateTime dateTime = new DateTime(collection
									.getDateCollected());
							DateTime firstDayOfMonth = dateTime
									.minusDays(dateTime.getDayOfMonth() - 1);
							if (firstDayOfMonth.toDate().equals(collectionDate)) {
								return true;
							}
							return false;
						}
					});
			monthlyAggregate.aggregates
					.put(uniqueDate, dailyCollections.size());
		}
		return monthlyAggregate;
	}

	public static MonthlyAggregate createWithProducts(
			ArrayList<Product> filteredProducts) {
		MonthlyAggregate monthlyAggregate = new MonthlyAggregate();
		monthlyAggregate.aggregates = new TreeMap<Date, Integer>(
				new Comparator<Date>() {
					public int compare(Date date, Date date1) {
						return date.compareTo(date1);
					}
				});
		List<Date> dateList = (List<Date>) CollectionUtils.collect(
				filteredProducts, new Transformer() {
					public Object transform(Object o) {
						Product product = (Product) o;
						DateTime dateTime = new DateTime(product
								.getDateCollected());
						DateTime firstDayOfMonth = dateTime.minusDays(dateTime
								.getDayOfMonth() - 1);
						return firstDayOfMonth.toDate();
					}
				});
		HashSet<Date> uniqueDates = new HashSet<Date>(dateList);
		for (Date uniqueDate : uniqueDates) {
			final Date collectionDate = uniqueDate;
			List<Product> dailyProducts = (List<Product>) CollectionUtils
					.select(filteredProducts, new Predicate() {
						public boolean evaluate(Object o) {
							Product product = (Product) o;
							DateTime dateTime = new DateTime(product
									.getDateCollected());
							DateTime firstDayOfMonth = dateTime
									.minusDays(dateTime.getDayOfMonth() - 1);
							if (firstDayOfMonth.toDate().equals(collectionDate)) {
								return true;
							}
							return false;
						}
					});
			monthlyAggregate.aggregates.put(uniqueDate, dailyProducts.size());
		}
		return monthlyAggregate;
	}

	public static MonthlyAggregate createWithTestResults(
			ArrayList<TestResult> filteredTestResults) {
		MonthlyAggregate monthlyAggregate = new MonthlyAggregate();
		monthlyAggregate.aggregates = new TreeMap<Date, Integer>(
				new Comparator<Date>() {
					public int compare(Date date, Date date1) {
						return date.compareTo(date1);
					}
				});
		List<Date> dateList = (List<Date>) CollectionUtils.collect(
				filteredTestResults, new Transformer() {
					public Object transform(Object o) {
						TestResult testResult = (TestResult) o;
						DateTime dateTime = new DateTime(testResult
								.getDateCollected());
						DateTime firstDayOfMonth = dateTime.minusDays(dateTime
								.getDayOfMonth() - 1);
						return firstDayOfMonth.toDate();
					}
				});
		HashSet<Date> uniqueDates = new HashSet<Date>(dateList);
		for (Date uniqueDate : uniqueDates) {
			final Date collectionDate = uniqueDate;
			List<TestResult> dailyProducts = (List<TestResult>) CollectionUtils
					.select(filteredTestResults, new Predicate() {
						public boolean evaluate(Object o) {
							TestResult testResult = (TestResult) o;
							DateTime dateTime = new DateTime(testResult
									.getDateCollected());
							DateTime firstDayOfMonth = dateTime
									.minusDays(dateTime.getDayOfMonth() - 1);
							if (firstDayOfMonth.toDate().equals(collectionDate)) {
								return true;
							}
							return false;
						}
					});
			monthlyAggregate.aggregates.put(uniqueDate, dailyProducts.size());
		}
		return monthlyAggregate;
	}

	public LinkedHashMap<String, String> getAggregates() {
		LinkedHashMap<String, String> viewAggregates = new LinkedHashMap<String, String>();
		DateFormat formatter = new SimpleDateFormat("MM/yyyy");
		Set<Date> dates = aggregates.keySet();
		ArrayList<Date> sortedDates = new ArrayList<Date>(dates);
		Collections.sort(sortedDates);
		for (Date date : sortedDates) {
			viewAggregates.put(formatter.format(date), aggregates.get(date)
					.toString());
		}
		return viewAggregates;
	}

	public LinkedHashMap<Date, Integer> getOrderedAggregates() {
		LinkedHashMap<Date, Integer> viewAggregates = new LinkedHashMap<Date, Integer>();
		Set<Date> dates = aggregates.keySet();
		ArrayList<Date> sortedDates = new ArrayList<Date>(dates);
		Collections.sort(sortedDates);
		for (Date date : sortedDates) {
			viewAggregates.put(date, aggregates.get(date));
		}
		return viewAggregates;
	}

	public LinkedHashMap<Date, Integer> getFilledMonthlyAggregate(
			LinkedHashMap<Date, Integer> orderedAggregates) {
		LinkedHashMap<Date, Integer> filledOrderedAggregates = new LinkedHashMap<Date, Integer>();
		TreeMap<Date, Integer> treeMap = new TreeMap(orderedAggregates);

		DateTime startDate = new DateTime(Collections.min(orderedAggregates
				.keySet()));

		DateTime endDate = new DateTime(Collections.max(orderedAggregates
				.keySet()));

		DateTime currentDate = startDate;

		while (currentDate.getMillis() != endDate.getMillis()) {
			Date date = currentDate.toDate();
			if (treeMap.get(date) == null) {
				filledOrderedAggregates.put(date, 0);
			} else {
				filledOrderedAggregates.put(date, treeMap.get(date));
			}
			currentDate = currentDate.plusMonths(1);
		}
		return filledOrderedAggregates;
	}
}