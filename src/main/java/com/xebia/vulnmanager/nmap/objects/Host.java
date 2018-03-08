package com.xebia.vulnmanager.nmap.objects;

import java.io.Serializable;

/**
 * HostDetails Serializable.
 */
public class Host implements Serializable {
    private StateDetails stateDetails;
    private AddressDetails addressDetails;
    private HostNamesDetails hostNamesDetails;
    private HostPorts hostPorts;
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
}
