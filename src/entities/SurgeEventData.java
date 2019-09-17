/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import org.joda.time.DateTime;

/**
 *
 * @author sudbasnet
 */
@Entity
@Table(name = "surge_event_data")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SurgeEventData.findAll", query = "SELECT s FROM SurgeEventData s")
    , @NamedQuery(name = "SurgeEventData.findByEventId", query = "SELECT s FROM SurgeEventData s WHERE s.eventId = :eventId")
    , @NamedQuery(name = "SurgeEventData.findByDay", query = "SELECT s FROM SurgeEventData s WHERE s.day = :day")
    , @NamedQuery(name = "SurgeEventData.findByMonth", query = "SELECT s FROM SurgeEventData s WHERE s.month = :month")
    , @NamedQuery(name = "SurgeEventData.findByYear", query = "SELECT s FROM SurgeEventData s WHERE s.year = :year")
    , @NamedQuery(name = "SurgeEventData.findByEventDate", query = "SELECT s FROM SurgeEventData s WHERE s.eventDate = :eventDate")
    , @NamedQuery(name = "SurgeEventData.findByCountryCode", query = "SELECT s FROM SurgeEventData s WHERE s.countryCode = :countryCode")
    , @NamedQuery(name = "SurgeEventData.findByGeoCity", query = "SELECT s FROM SurgeEventData s WHERE s.geoCity = :geoCity")
    , @NamedQuery(name = "SurgeEventData.findByGeoState", query = "SELECT s FROM SurgeEventData s WHERE s.geoState = :geoState")
    , @NamedQuery(name = "SurgeEventData.findByGeoCountry", query = "SELECT s FROM SurgeEventData s WHERE s.geoCountry = :geoCountry")
    , @NamedQuery(name = "SurgeEventData.findByEventCategory", query = "SELECT s FROM SurgeEventData s WHERE s.eventCategory = :eventCategory")
    , @NamedQuery(name = "SurgeEventData.findByLatitude", query = "SELECT s FROM SurgeEventData s WHERE s.latitude = :latitude")
    , @NamedQuery(name = "SurgeEventData.findByLongitude", query = "SELECT s FROM SurgeEventData s WHERE s.longitude = :longitude")
    , @NamedQuery(name = "SurgeEventData.findByEventCount", query = "SELECT s FROM SurgeEventData s WHERE s.eventCount = :eventCount")
    , @NamedQuery(name = "SurgeEventData.findByPopulationDensity", query = "SELECT s FROM SurgeEventData s WHERE s.populationDensity = :populationDensity")
    , @NamedQuery(name = "SurgeEventData.findByDataSource", query = "SELECT s FROM SurgeEventData s WHERE s.dataSource = :dataSource")
    , @NamedQuery(name = "SurgeEventData.findByDataLoaded", query = "SELECT s FROM SurgeEventData s WHERE s.dataLoaded = :dataLoaded")
    , @NamedQuery(name = "SurgeEventData.findByAdm02ID", query = "SELECT s FROM SurgeEventData s WHERE s.adm02ID = :adm02ID")
    , @NamedQuery(name = "SurgeEventData.findByAdm02State", query = "SELECT s FROM SurgeEventData s WHERE s.adm02State = :adm02State")
    , @NamedQuery(name = "SurgeEventData.findByAdm03ID", query = "SELECT s FROM SurgeEventData s WHERE s.adm03ID = :adm03ID")
    , @NamedQuery(name = "SurgeEventData.findByAdm03District", query = "SELECT s FROM SurgeEventData s WHERE s.adm03District = :adm03District")
    , @NamedQuery(name = "SurgeEventData.findByFilename", query = "SELECT s FROM SurgeEventData s WHERE s.filename = :filename")})
public class SurgeEventData implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "event_id")
    private Long eventId;
    @Column(name = "day")
    private Integer day;
    @Column(name = "month")
    private Integer month;
    @Column(name = "year")
    private Integer year;
    @Column(name = "event_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date eventDate;
    @Column(name = "country_code")
    private String countryCode;
    @Column(name = "geo_city")
    private String geoCity;
    @Column(name = "geo_state")
    private String geoState;
    @Column(name = "geo_country")
    private String geoCountry;
    @Lob
    @Column(name = "url")
    private String url;
    @Column(name = "event_category")
    private String eventCategory;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "latitude")
    private BigDecimal latitude;
    @Column(name = "longitude")
    private BigDecimal longitude;
    @Column(name = "event_count")
    private Integer eventCount;
    @Column(name = "population_density")
    private Double populationDensity;
    @Column(name = "data_source")
    private String dataSource;
    @Column(name = "data_loaded")
    @Temporal(TemporalType.DATE)
    private Date dataLoaded;
    @Column(name = "adm02_ID")
    private String adm02ID;
    @Column(name = "adm02_state")
    private String adm02State;
    @Column(name = "adm03_ID")
    private String adm03ID;
    @Column(name = "adm03_district")
    private String adm03District;
    @Column(name = "filename")
    private String filename;

    public SurgeEventData() {
    }

    public SurgeEventData(Long eventId) {
        this.eventId = eventId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getGeoCity() {
        return geoCity;
    }

    public void setGeoCity(String geoCity) {
        this.geoCity = geoCity;
    }

    public String getGeoState() {
        return geoState;
    }

    public void setGeoState(String geoState) {
        this.geoState = geoState;
    }

    public String getGeoCountry() {
        return geoCountry;
    }

    public void setGeoCountry(String geoCountry) {
        this.geoCountry = geoCountry;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEventCategory() {
        return eventCategory;
    }

    public void setEventCategory(String eventCategory) {
        this.eventCategory = eventCategory;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public Integer getEventCount() {
        return eventCount;
    }

    public void setEventCount(Integer eventCount) {
        this.eventCount = eventCount;
    }

    public Double getPopulationDensity() {
        return populationDensity;
    }

    public void setPopulationDensity(Double populationDensity) {
        this.populationDensity = populationDensity;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public Date getDataLoaded() {
        return dataLoaded;
    }

    public void setDataLoaded(Date dataLoaded) {
        this.dataLoaded = dataLoaded;
    }

    public String getAdm02ID() {
        return adm02ID;
    }

    public void setAdm02ID(String adm02ID) {
        this.adm02ID = adm02ID;
    }

    public String getAdm02State() {
        return adm02State;
    }

    public void setAdm02State(String adm02State) {
        this.adm02State = adm02State;
    }

    public String getAdm03ID() {
        return adm03ID;
    }

    public void setAdm03ID(String adm03ID) {
        this.adm03ID = adm03ID;
    }

    public String getAdm03District() {
        return adm03District;
    }

    public void setAdm03District(String adm03District) {
        this.adm03District = adm03District;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (eventId != null ? eventId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SurgeEventData)) {
            return false;
        }
        SurgeEventData other = (SurgeEventData) object;
        if ((this.eventId == null && other.eventId != null) || (this.eventId != null && !this.eventId.equals(other.eventId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.SurgeEventData[ eventId=" + eventId + " ]";
    }

    public void setDataLoaded(DateTime currTime) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
