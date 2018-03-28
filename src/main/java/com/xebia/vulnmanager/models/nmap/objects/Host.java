package com.xebia.vulnmanager.models.nmap.objects;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.CascadeType;
import javax.persistence.GenerationType;
import java.io.Serializable;

/**
 * HostDetails Serializable.
 */
@Table(name = "Host")
@Entity
public class Host implements Serializable {
    private static final String HOST_PARENT_STRING_LITERAL = "hostParent";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "nMapReport_id", nullable = false) // Column that will be used to keep track of the parent
    @JsonBackReference // A back reference to keep json from infinite looping
    private NMapReport nMapReportParent; // NMapReport for host to know

    @OneToOne(mappedBy = HOST_PARENT_STRING_LITERAL, cascade = CascadeType.ALL)
    private StateDetails stateDetails;

    @OneToOne(mappedBy = HOST_PARENT_STRING_LITERAL, cascade = CascadeType.ALL)
    private AddressDetails addressDetails;

    @OneToOne(mappedBy = HOST_PARENT_STRING_LITERAL, cascade = CascadeType.ALL)
    private HostNamesDetails hostNamesDetails;

    @OneToOne(mappedBy = HOST_PARENT_STRING_LITERAL, cascade = CascadeType.ALL)
    private HostPorts hostPorts;

    @OneToOne(mappedBy = HOST_PARENT_STRING_LITERAL, cascade = CascadeType.ALL)
    private TimingData timingData;

    public Host(final StateDetails stateDetails, final AddressDetails addressDetails, final HostNamesDetails hostNamesDetails,
                final HostPorts hostPorts, final TimingData timingData) {
        this.stateDetails = stateDetails;
        this.addressDetails = addressDetails;
        this.hostNamesDetails = hostNamesDetails;
        this.hostPorts = hostPorts;
        this.timingData = timingData;
    }

    public StateDetails getStateDetails() {
        return stateDetails;
    }

    public AddressDetails getAddressDetails() {
        return addressDetails;
    }

    public HostNamesDetails getHostNamesDetails() {
        return hostNamesDetails;
    }

    public HostPorts getHostPorts() {
        return hostPorts;
    }

    public TimingData getTimingData() {
        return timingData;
    }

    public long getId() {
        return id;
    }

    public NMapReport getParentReport() {
        return nMapReportParent;
    }

    public void setParentNMapReport(NMapReport nMapReport) {
        this.nMapReportParent = nMapReport;
    }
}
