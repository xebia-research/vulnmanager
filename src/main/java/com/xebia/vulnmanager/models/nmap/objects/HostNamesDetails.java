package com.xebia.vulnmanager.models.nmap.objects;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.GeneratedValue;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.ManyToOne;
import javax.persistence.CascadeType;
import javax.persistence.GenerationType;

import java.io.Serializable;
import java.util.List;

/**
 * AddressDetails Serializable.
 */
@Table(name = "HostNamesDetails")
@Entity
public class HostNamesDetails implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToOne
    @JoinColumn(name = "host_id", nullable = false) // Column that will be used to keep track of the parent
    @JsonBackReference // A back reference to keep json from infinite looping
    private Host hostParent;

    @OneToMany(mappedBy = "hostNamesDetailsParent", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<HostNameDetails> hostNameDetails;

    public HostNamesDetails(final List<HostNameDetails> hostNameDetails) {
        this.hostNameDetails = hostNameDetails;
    }

    @JacksonXmlElementWrapper(localName = "hostNameList")
    @JacksonXmlProperty(localName = "hostName")
    public List<HostNameDetails> getHostNameDetails() {
        return hostNameDetails;
    }

    public Long getId() {
        return id;
    }

    public Host getHostParent() {
        return hostParent;
    }

    public void setHostParent(Host hostParent) {
        this.hostParent = hostParent;
    }


    @Table(name = "HostNameDetails")
    @Entity
    public static class HostNameDetails implements Serializable {
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "HostNamesDetails_id", nullable = false)
        // Column that will be used to keep track of the parent
        @JsonBackReference // A back reference to keep json from infinite looping
        private HostNamesDetails hostNamesDetailsParent;

        private String hostName;
        private String hostType;

        public HostNameDetails(final String hostName, final String hostType) {
            this.hostName = hostName;
            this.hostType = hostType;
        }

        public String getHostName() {
            return hostName;
        }

        public String getHostType() {
            return hostType;
        }

        public Long getId() {
            return id;
        }

        public HostNamesDetails getHostNamesDetailsParent() {
            return hostNamesDetailsParent;
        }

        public void setHostNamesDetailsParent(HostNamesDetails hostNamesDetails) {
            this.hostNamesDetailsParent = hostNamesDetails;
        }
    }
}
