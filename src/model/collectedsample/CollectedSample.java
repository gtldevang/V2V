package model.collectedsample;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import model.bloodbagtype.BloodBagType;
import model.bloodbagtype.BloodBagTypeExists;
import model.donationBatch.DonationBatch;
import model.donationtype.DonationType;
import model.donationtype.DonationTypeExists;
import model.donor.Donor;
import model.donor.DonorExists;
import model.location.Location;
import model.location.LocationExists;
import model.modificationtracker.ModificationTracker;
import model.modificationtracker.RowModificationTracker;
import model.product.Product;
import model.testresults.TestResult;
import model.testresults.TestedStatus;
import model.user.User;
import model.util.BloodAbo;
import model.util.BloodRhd;
import model.worksheet.CollectionsWorksheet;

import org.hibernate.annotations.Index;

@Entity
public class CollectedSample implements ModificationTracker, Comparable<CollectedSample> {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable=false)
  private Long id;

  @Column(length=30)
  @Index(name="collectedSample_collectionNumber_index")
  private String collectionNumber;

  @DonorExists
  @ManyToOne(fetch=FetchType.LAZY)
  private Donor donor;

  @OneToMany(mappedBy="collectedSample")
  private List<TestResult> testResults;

  @LocationExists
  @ManyToOne
  private Location collectionCenter;

  @LocationExists
  @ManyToOne
  private Location collectionSite;

  @Temporal(TemporalType.TIMESTAMP)
  @Index(name="collectedSample_collectedOn_index")
  private Date collectedOn;

  @DonationTypeExists
  @ManyToOne
  private DonationType donorType;

  @BloodBagTypeExists
  @ManyToOne
  private BloodBagType bloodBagType;

  @OneToMany(mappedBy="collectedSample")
  private List<Product> products;

  @Enumerated(EnumType.STRING)
  @Column(length=30)
  @Index(name="donor_bloodAbo_index")
  private BloodAbo bloodAbo;

  @Enumerated(EnumType.STRING)
  @Column(length=30)
  @Index(name="donor_bloodRhd_index")
  private BloodRhd bloodRhd;
  
  @ManyToMany
  private List<CollectionsWorksheet> worksheets;

  @Valid
  private RowModificationTracker modificationTracker;

  @Column(precision=6, scale=2)
  private BigDecimal haemoglobinCount;

  @Column(precision=6, scale=2)
  private BigDecimal bloodPressure;

  @Column(precision=6, scale=2)
  private BigDecimal donorWeight;

  @ManyToOne(optional=true)
  private User donationCreatedBy;

  @ManyToOne(optional=true)
  private DonationBatch donationBatch;

  @Lob
  private String notes;

  @Enumerated(EnumType.STRING)
  @Column(length=20)
  private TestedStatus testedStatus;

  private Boolean isDeleted;

  public CollectedSample() {
    modificationTracker = new RowModificationTracker();
  }

  public Long getId() {
    return id;
  }

  public String getCollectionNumber() {
    return collectionNumber;
  }

  public Donor getDonor() {
    return donor;
  }

  public List<TestResult> getTestResults() {
    return testResults;
  }

  public Location getCollectionCenter() {
    return collectionCenter;
  }

  public Location getCollectionSite() {
    return collectionSite;
  }

  public Date getCollectedOn() {
    return collectedOn;
  }

  public DonationType getDonorType() {
    return donorType;
  }

  public BloodBagType getBloodBagType() {
    return bloodBagType;
  }

  public String getNotes() {
    return notes;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setCollectionNumber(String collectionNumber) {
    this.collectionNumber = collectionNumber;
  }

  public void setDonor(Donor donor) {
    this.donor = donor;
  }

  public void setTestResults(List<TestResult> testResults) {
    this.testResults = testResults;
  }

  public void setCollectionCenter(Location collectionCenter) {
    this.collectionCenter = collectionCenter;
  }

  public void setCollectionSite(Location collectionSite) {
    this.collectionSite = collectionSite;
  }

  public void setCollectedOn(Date collectedOn) {
    this.collectedOn = collectedOn;
  }

  public void setDonorType(DonationType donorType) {
    this.donorType = donorType;
  }

  public void setBloodBagType(BloodBagType bloodBagType) {
    this.bloodBagType = bloodBagType;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  public void copy(CollectedSample collectedSample) {
    assert (this.getId().equals(collectedSample.getId()));
    this.collectionNumber = collectedSample.collectionNumber;
    this.donor = collectedSample.donor;
    this.donorType = collectedSample.donorType;
    this.bloodBagType = collectedSample.bloodBagType;
    this.collectedOn = collectedSample.collectedOn;
    this.collectionCenter = collectedSample.collectionCenter;
    this.collectionSite = collectedSample.collectionSite;
    this.notes = collectedSample.notes;
  }

  public List<Product> getProducts() {
    return products;
  }

  public void setProducts(List<Product> products) {
    this.products = products;
  }

  public Date getLastUpdated() {
    return modificationTracker.getLastUpdated();
  }

  public Date getCreatedDate() {
    return modificationTracker.getCreatedDate();
  }

  public User getCreatedBy() {
    return modificationTracker.getCreatedBy();
  }

  public User getLastUpdatedBy() {
    return modificationTracker.getLastUpdatedBy();
  }

  public void setLastUpdated(Date lastUpdated) {
    modificationTracker.setLastUpdated(lastUpdated);
  }

  public void setCreatedDate(Date createdDate) {
    modificationTracker.setCreatedDate(createdDate);
  }

  public void setCreatedBy(User createdBy) {
    modificationTracker.setCreatedBy(createdBy);
  }

  public void setLastUpdatedBy(User lastUpdatedBy) {
    modificationTracker.setLastUpdatedBy(lastUpdatedBy);
  }

  public String getDonorNumber() {
    if (donor != null)
      return donor.getDonorNumber();
    return "";
  }

  public List<CollectionsWorksheet> getWorksheets() {
    return worksheets;
  }

  public void setWorksheets(List<CollectionsWorksheet> worksheets) {
    this.worksheets = worksheets;
  }

  @Override
  public int compareTo(CollectedSample c) {
    Long diff = (this.id - c.id);
    if (diff < 0)
      return -1;
    if (diff > 0)
      return 1;
    return 0;
  }

  public TestedStatus getTestedStatus() {
    return testedStatus;
  }

  public void setTestedStatus(TestedStatus testedStatus) {
    this.testedStatus = testedStatus;
  }

  public BigDecimal getHaemoglobinCount() {
    return haemoglobinCount;
  }

  public void setHaemoglobinCount(BigDecimal haemoglobinCount) {
    this.haemoglobinCount = haemoglobinCount;
  }

  public BigDecimal getBloodPressure() {
    return bloodPressure;
  }

  public void setBloodPressure(BigDecimal bloodPressure) {
    this.bloodPressure = bloodPressure;
  }

  public BigDecimal getDonorWeight() {
    return donorWeight;
  }

  public void setDonorWeight(BigDecimal donorWeight) {
    this.donorWeight = donorWeight;
  }

  public DonationBatch getDonationBatch() {
    return donationBatch;
  }

  public void setDonationBatch(DonationBatch donationBatch) {
    this.donationBatch = donationBatch;
  }

  public User getDonationCreatedBy() {
    return donationCreatedBy;
  }

  public void setDonationCreatedBy(User donationCreatedBy) {
    this.donationCreatedBy = donationCreatedBy;
  }

  public BloodAbo getBloodAbo() {
    return bloodAbo;
  }

  public void setBloodAbo(BloodAbo bloodAbo) {
    this.bloodAbo = bloodAbo;
  }

  public BloodRhd getBloodRhd() {
    return bloodRhd;
  }

  public void setBloodRhd(BloodRhd bloodRhd) {
    this.bloodRhd = bloodRhd;
  }
}
