package com.xebia.vulnmanager.NMapReport.Objects;

import java.io.Serializable;

/**
 * HostDetails Serializable.
 *
 */
public class HostDetails implements Serializable {
    private StatusDetails statusDetails;
    private AddressDetails addressDetails;

    public StatusDetails getStatusDetails() {
        return statusDetails;
    }

    public void setStatusDetails(StatusDetails statusDetails) {
        this.statusDetails = statusDetails;
    }

    public AddressDetails getAddressDetails() {
        return addressDetails;
    }

    public void setAddressDetails(AddressDetails addressDetails) {
        this.addressDetails = addressDetails;
    }
}
