package com.xebia.vulnmanager.models.nmap.objects;

import java.io.Serializable;

/**
 * AddressDetails Serializable.
 */

public class AddressDetails implements Serializable {
    private String address;
    private String addressType;

    public AddressDetails(final String address, final String addressType) {
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
