package controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import model.Collection;
import model.Location;
import model.User;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import repository.CollectionRepository;
import repository.DisplayNamesRepository;
import repository.LocationRepository;
import repository.ReportConfigRepository;
import utils.ChartUtil;
import utils.ControllerUtil;
import viewmodel.CollectionViewModel;
import viewmodel.DailyAggregate;
import viewmodel.MonthlyAggregate;
import viewmodel.YearlyAggregate;

@Controller
public class CollectionReportsController {
	@Autowired
	private CollectionRepository collectionRepository;

	@Autowired
	private LocationRepository locationRepository;

	@Autowired
	private ReportConfigRepository reportConfigRepository;

	@Autowired
	private DisplayNamesRepository displayNamesRepository;

	@RequestMapping("/collectionReport")
	public ModelAndView getCollectionReportsPage(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("collectionReport");
		Map<String, Object> model = new HashMap<String, Object>();

		ControllerUtil.addReportsDisplayNamesToModel(model,
				displayNamesRepository);
		modelAndView.addObject("model", model);

		return modelAndView;
	}

	@RequestMapping("/getCollectionReport")
	public ModelAndView getCollectionReport(
			@RequestParam Map<String, String> params, HttpServletRequest request) {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		Date fromDate = getDate(params.get("collectionFromDate"));
		Date toDate = getDate(params.get("collectionToDate"));

		ModelAndView modelAndView = new ModelAndView("collectionReport");

		Map<String, Object> model = new HashMap<String, Object>();

		List<Collection> collections = collectionRepository.getCollections(
				fromDate, toDate);
		if (collections != null && collections.size() > 0) {
			ArrayList<Collection> filteredCollections = applyCollectionFilters(
					collections, params);

			Collections.sort(filteredCollections, new Comparator<Collection>() {
				public int compare(Collection collection, Collection collection1) {
					return collection.getDateCollected().compareTo(
							collection1.getDateCollected());
				}
			});

			if (params.get("collectionAggregateType").equals("")) {
				model.put(
						"collections",
						getCollectionViewModels(filteredCollections,
								locationRepository.getAllCollectionSites(),
								locationRepository.getAllCenters()));
				// if (numberOfBloodGroupsSelected(params) <= 1) {
				model.put("hasCollectionReport", true);
				ControllerUtil.addCollectionReportConfigFieldsToModel(model,
						reportConfigRepository);
				// } else {
				model.put("hasCollectionReportGraph", true);
				String chartFilename = "bloodGroupChart" + user.getUsername();
				model.put("bloodGroupChartName", chartFilename);
				createGraphForBloodGroups(params, collections, chartFilename);
				// }
			} else if (params.get("collectionAggregateType").equals("daily")) {
				DailyAggregate dailyAggregates = DailyAggregate
						.createWithCollections(filteredCollections);
				model.put("hasDailyCollectionReport", true);
				LinkedHashMap<String, String> aggregates = dailyAggregates
						.getAggregates();
				model.put("dailyCollectionAggregates", aggregates);
				String chartFilename = "dailyCollectionsChart"
						+ user.getUsername();
				model.put("hasDailyCollectionReportGraph", true);
				model.put("dailyCollectionsChartName", chartFilename);
				createDailyGraphForBloodGroups(dailyAggregates, chartFilename);
			} else if (params.get("collectionAggregateType").equals("monthly")) {
				MonthlyAggregate monthlyAggregate = MonthlyAggregate
						.createWithCollections(filteredCollections);
				model.put("hasMonthlyCollectionReport", true);
				LinkedHashMap<String, String> aggregates = monthlyAggregate
						.getAggregates();
				model.put("monthlyCollectionAggregates", aggregates);
				String chartFilename = "monthlyCollectionsChart"
						+ user.getUsername();
				model.put("hasMonthlyCollectionReportGraph", true);
				model.put("monthlyCollectionsChartName", chartFilename);
				createMonthlyGraphForBloodGroups(monthlyAggregate,
						chartFilename);

			} else if (params.get("collectionAggregateType").equals("yearly")) {
				YearlyAggregate yearlyAggregate = YearlyAggregate
						.createWithCollections(filteredCollections);
				model.put("hasYearlyCollectionReport", true);
				LinkedHashMap<String, String> aggregates = yearlyAggregate
						.getAggregates();
				model.put("yearlyCollectionAggregates", aggregates);
				String chartFilename = "yearlyCollectionsChart"
						+ user.getUsername();
				model.put("hasYearlyCollectionReportGraph", true);
				model.put("yearlyCollectionsChartName", chartFilename);
				createYearlyGraphForBloodGroups(yearlyAggregate, chartFilename);
			}
		} else {
			model.put("noCollectionsFound", true);
		}
		model.put("hasCollectionDetails", true);
		model.put("fromDate", params.get("collectionFromDate"));
		model.put("toDate", params.get("collectionToDate"));
		model.put("aPositive", params.get("aPositive"));
		model.put("aNegative", params.get("aNegative"));
		model.put("bPositive", params.get("bPositive"));
		model.put("bNegative", params.get("bNegative"));
		model.put("abPositive", params.get("abPositive"));
		model.put("abNegative", params.get("abNegative"));
		model.put("oPositive", params.get("oPositive"));
		model.put("oNegative", params.get("oNegative"));
		model.put("allBloodTypes", params.get("allBloodTypes"));
		model.put("collectionAggregateType",
				params.get("collectionAggregateType"));

		ControllerUtil.addCollectionDisplayNamesToModel(model,
				displayNamesRepository);
		ControllerUtil.addReportsDisplayNamesToModel(model,
				displayNamesRepository);

		modelAndView.addObject("model", model);

		return modelAndView;
	}

	private void createDailyGraphForBloodGroups(DailyAggregate dailyAggregates,
			String chartFilename) {
		ChartUtil.createDailyChart(dailyAggregates, chartFilename,
				"Collections");
	}

	private void createMonthlyGraphForBloodGroups(
			MonthlyAggregate monthlyAggregates, String chartFilename) {
		ChartUtil.createMonthlyChart(monthlyAggregates, chartFilename,
				"Collections");
	}

	private void createYearlyGraphForBloodGroups(
			YearlyAggregate yearlyAggregates, String chartFilename) {
		ChartUtil.createYearlyChart(yearlyAggregates, chartFilename,
				"Collections");
	}

	private void createGraphForBloodGroups(Map<String, String> params,
			List<Collection> collections, String chartFilename) {
		final DefaultCategoryDataset dataset = getDatasetForBarGraph(params,
				collections);
		ChartUtil.createBarGraph(chartFilename, dataset, "Blood Group",
				"Collections");
	}

	private DefaultCategoryDataset getDatasetForBarGraph(
			Map<String, String> params, List<Collection> collections) {
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		if ("true".equals(params.get("allBloodTypes"))) {
			dataset.addValue(
					filterCollectionForBloodType(collections, "A", "positive")
							.size(), "", "A+");
			dataset.addValue(
					filterCollectionForBloodType(collections, "A", "negative")
							.size(), "", "A-");
			dataset.addValue(
					filterCollectionForBloodType(collections, "B", "positive")
							.size(), "", "B+");
			dataset.addValue(
					filterCollectionForBloodType(collections, "B", "negative")
							.size(), "", "B-");
			dataset.addValue(
					filterCollectionForBloodType(collections, "AB", "positive")
							.size(), "", "AB+");
			dataset.addValue(
					filterCollectionForBloodType(collections, "AB", "negative")
							.size(), "", "AB-");
			dataset.addValue(
					filterCollectionForBloodType(collections, "O", "positive")
							.size(), "", "O+");
			dataset.addValue(
					filterCollectionForBloodType(collections, "O", "negative")
							.size(), "", "O-");
			return dataset;
		}
		if ("true".equals(params.get("aPositive"))) {
			dataset.addValue(
					filterCollectionForBloodType(collections, "A", "positive")
							.size(), "", "A+");
		}
		if ("true".equals(params.get("aNegative"))) {
			dataset.addValue(
					filterCollectionForBloodType(collections, "A", "negative")
							.size(), "", "A-");
		}
		if ("true".equals(params.get("bPositive"))) {
			dataset.addValue(
					filterCollectionForBloodType(collections, "B", "positive")
							.size(), "", "B+");
		}
		if ("true".equals(params.get("bNegative"))) {
			dataset.addValue(
					filterCollectionForBloodType(collections, "B", "negative")
							.size(), "", "B-");
		}
		if ("true".equals(params.get("abPositive"))) {
			dataset.addValue(
					filterCollectionForBloodType(collections, "AB", "positive")
							.size(), "", "AB+");
		}
		if ("true".equals(params.get("abNegative"))) {
			dataset.addValue(
					filterCollectionForBloodType(collections, "AB", "negative")
							.size(), "", "AB-");
		}
		if ("true".equals(params.get("oPositive"))) {
			dataset.addValue(
					filterCollectionForBloodType(collections, "O", "positive")
							.size(), "", "O+");
		}
		if ("true".equals(params.get("oNegative"))) {
			dataset.addValue(
					filterCollectionForBloodType(collections, "O", "negative")
							.size(), "", "O-");
		}
		return dataset;
	}

	private int numberOfBloodGroupsSelected(Map<String, String> params) {
		if ("true".equals(params.get("allBloodTypes"))) {
			return 8;
		}
		int count = 0;
		if ("true".equals(params.get("aPositive"))) {
			count++;
		}
		if ("true".equals(params.get("aNegative"))) {
			count++;

		}
		if ("true".equals(params.get("bPositive"))) {
			count++;

		}
		if ("true".equals(params.get("bNegative"))) {
			count++;

		}
		if ("true".equals(params.get("abPositive"))) {
			count++;

		}
		if ("true".equals(params.get("abNegative"))) {
			count++;

		}
		if ("true".equals(params.get("oPositive"))) {
			count++;

		}
		if ("true".equals(params.get("oNegative"))) {
			count++;

		}
		return count;
	}

	private ArrayList<Collection> applyCollectionFilters(
			List<Collection> collections, Map<String, String> params) {
		ArrayList<Collection> filteredCollections = new ArrayList<Collection>();
		if ("true".equals(params.get("allBloodTypes"))) {
			return new ArrayList<Collection>(collections);
		}
		if ("true".equals(params.get("aPositive"))) {
			List<Collection> selectedCollections = filterCollectionForBloodType(
					collections, "A", "positive");
			filteredCollections.addAll(selectedCollections);
		}
		if ("true".equals(params.get("aNegative"))) {
			List<Collection> selectedCollections = filterCollectionForBloodType(
					collections, "A", "negative");
			filteredCollections.addAll(selectedCollections);

		}
		if ("true".equals(params.get("bPositive"))) {
			List<Collection> selectedCollections = filterCollectionForBloodType(
					collections, "B", "positive");
			filteredCollections.addAll(selectedCollections);

		}
		if ("true".equals(params.get("bNegative"))) {
			List<Collection> selectedCollections = filterCollectionForBloodType(
					collections, "B", "negative");
			filteredCollections.addAll(selectedCollections);

		}
		if ("true".equals(params.get("abPositive"))) {
			List<Collection> selectedCollections = filterCollectionForBloodType(
					collections, "AB", "positive");
			filteredCollections.addAll(selectedCollections);

		}
		if ("true".equals(params.get("abNegative"))) {
			List<Collection> selectedCollections = filterCollectionForBloodType(
					collections, "AB", "negative");
			filteredCollections.addAll(selectedCollections);

		}
		if ("true".equals(params.get("oPositive"))) {
			List<Collection> selectedCollections = filterCollectionForBloodType(
					collections, "O", "positive");
			filteredCollections.addAll(selectedCollections);

		}
		if ("true".equals(params.get("oNegative"))) {
			List<Collection> selectedCollections = filterCollectionForBloodType(
					collections, "O", "negative");
			filteredCollections.addAll(selectedCollections);

		}

		return filteredCollections;
	}

	private List<Collection> filterCollectionForBloodType(
			List<Collection> collections, final String bloodGroup,
			final String rhd) {
		ArrayList<Collection> selectedCollections = (ArrayList<Collection>) CollectionUtils
				.select(collections, new Predicate() {
					public boolean evaluate(Object o) {
						Collection collection = (Collection) o;
						if (collection.getAbo() != null
								&& collection.getAbo().equals(bloodGroup)
								&& collection.getRhd() != null
								&& collection.getRhd().equals(rhd)) {
							return true;
						}
						return false;
					}
				});
		return selectedCollections;
	}

	private List<CollectionViewModel> getCollectionViewModels(
			List<Collection> collections, List<Location> allCollectionSites,
			List<Location> allCenters) {
		ArrayList<CollectionViewModel> collectionViewModels = new ArrayList<CollectionViewModel>();
		Collections.sort(collections, new Comparator<Collection>() {
			public int compare(Collection collection, Collection collection1) {
				return collection.getDateCollected().compareTo(
						collection1.getDateCollected());
			}
		});
		for (Collection collection : collections) {
			collectionViewModels.add(new CollectionViewModel(collection,
					allCollectionSites, allCenters));
		}
		return collectionViewModels;
	}

	private Date getDate(String dateParam) {
		DateFormat formatter;
		formatter = new SimpleDateFormat("MM/dd/yyyy");
		Date date = null;
		try {
			String dateEntered = dateParam;
			if (dateEntered.length() > 0) {
				date = (Date) formatter.parse(dateEntered);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
}
