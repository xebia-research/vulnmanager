package com.xebia.vulnmanager.nmap.objects;

import java.io.Serializable;

/**
 * HostDetails Serializable.
 */
public class HostDetails implements Serializable {
    private StateDetails stateDetails;
    private AddressDetails addressDetails;
    private HostNamesDetails hostNamesDetails;
    private HostPorts hostPorts;
    private TimingData timingData;

    public StateDetails getStateDetails() {
        return stateDetails;
    }

    public void setStateDetails(StateDetails stateDetails) {
        this.stateDetails = stateDetails;
    }

    public AddressDetails getAddressDetails() {
        return addressDetails;
    }

    public void setAddressDetails(AddressDetails addressDetails) {
        this.addressDetails = addressDetails;
    }

    public HostNamesDetails getHostNamesDetails() {
        return hostNamesDetails;
    }

    public void setHostNamesDetails(HostNamesDetails hostNamesDetails) {
        this.hostNamesDetails = hostNamesDetails;
    }

    public HostPorts getHostPorts() {
        return hostPorts;
    }

    public void setHostPorts(HostPorts hostPorts) {
        this.hostPorts = hostPorts;
    }

    public TimingData getTimingData() {
        return timingData;
    }

    public void setTimingData(TimingData timingData) {
        this.timingData = timingData;
    }
}
