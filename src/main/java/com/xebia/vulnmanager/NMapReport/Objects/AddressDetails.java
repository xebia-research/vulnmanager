package com.xebia.vulnmanager.NMapReport.Objects;

import java.io.Serializable;

/**
 * AddressDetails Serializable.
 */

public class AddressDetails implements Serializable {
    private String address;
    private String addressType;

    public AddressDetails(String address, String addressType) {
        this.address = address;
        this.addressType = addressType;
    }

    public String getAddress() {
        return address;
    }

    public String getAddressType() {
        return addressType;
    }
}
