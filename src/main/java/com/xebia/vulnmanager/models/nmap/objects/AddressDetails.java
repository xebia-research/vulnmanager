package com.xebia.vulnmanager.models.nmap.objects;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;
import javax.persistence.GenerationType;
import java.io.Serializable;

/**
 * AddressDetails Serializable.
 */
@Table(name = "AddressDetails")
@Entity
public class AddressDetails implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToOne
    @JoinColumn(name = "host_id", nullable = false) // Column that will be used to keep track of the parent
    @JsonBackReference // A back reference to keep json from infinite looping
    private Host hostParent;

    private String address;
    private String addressType;

    protected AddressDetails() {
        // JPA constructor
    }

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

    public Long getId() {
        return id;
    }
    @JsonBackReference // A backrefrence to keep json from infinite looping
    public Host getHostParent() {
        return hostParent;
    }

    public void setHostParent(Host hostParent) {
        this.hostParent = hostParent;
    }
}
