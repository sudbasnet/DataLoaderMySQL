/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author sbasnet
 */
@Entity
@Table(name = "import_log")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ImportLog.findAll", query = "SELECT i FROM ImportLog i")
    , @NamedQuery(name = "ImportLog.findById", query = "SELECT i FROM ImportLog i WHERE i.id = :id")
    , @NamedQuery(name = "ImportLog.findByDownloadUrl", query = "SELECT i FROM ImportLog i WHERE i.downloadUrl = :downloadUrl")
    , @NamedQuery(name = "ImportLog.findByImportDate", query = "SELECT i FROM ImportLog i WHERE i.importDate = :importDate")
    , @NamedQuery(name = "ImportLog.findByFileDate", query = "SELECT i FROM ImportLog i WHERE i.fileDate = :fileDate")
    , @NamedQuery(name = "ImportLog.findByFileName", query = "SELECT i FROM ImportLog i WHERE i.fileName = :fileName")
    , @NamedQuery(name = "ImportLog.findByDownloadedRows", query = "SELECT i FROM ImportLog i WHERE i.downloadedRows = :downloadedRows")
    , @NamedQuery(name = "ImportLog.findByExtractRows", query = "SELECT i FROM ImportLog i WHERE i.extractRows = :extractRows")
    , @NamedQuery(name = "ImportLog.findByAggregatedRows", query = "SELECT i FROM ImportLog i WHERE i.aggregatedRows = :aggregatedRows")
    , @NamedQuery(name = "ImportLog.findByMessage", query = "SELECT i FROM ImportLog i WHERE i.message = :message")})
public class ImportLog implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "download_url")
    private String downloadUrl;
    @Column(name = "import_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date importDate;
    @Column(name = "file_date")
    private String fileDate;
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "downloaded_rows")
    private Integer downloadedRows;
    @Column(name = "extract_rows")
    private Integer extractRows;
    @Column(name = "aggregated_rows")
    private Integer aggregatedRows;
    @Column(name = "message")
    private String message;

    public ImportLog() {
    }

    public ImportLog(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public Date getImportDate() {
        return importDate;
    }

    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }

    public String getFileDate() {
        return fileDate;
    }

    public void setFileDate(String fileDate) {
        this.fileDate = fileDate;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getDownloadedRows() {
        return downloadedRows;
    }

    public void setDownloadedRows(Integer downloadedRows) {
        this.downloadedRows = downloadedRows;
    }

    public Integer getExtractRows() {
        return extractRows;
    }

    public void setExtractRows(Integer extractRows) {
        this.extractRows = extractRows;
    }

    public Integer getAggregatedRows() {
        return aggregatedRows;
    }

    public void setAggregatedRows(Integer aggregatedRows) {
        this.aggregatedRows = aggregatedRows;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ImportLog)) {
            return false;
        }
        ImportLog other = (ImportLog) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.ImportLog[ id=" + id + " ]";
    }
    
}
