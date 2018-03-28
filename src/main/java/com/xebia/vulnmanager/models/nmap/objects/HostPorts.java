package com.xebia.vulnmanager.models.nmap.objects;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.ManyToOne;
import javax.persistence.GenerationType;
import javax.persistence.CascadeType;
import java.io.Serializable;
import java.util.List;

@Table(name = "HostPorts")
@Entity
public class HostPorts implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToOne
    @JoinColumn(name = "host_id", nullable = false) // Column that will be used to keep track of the parent
    @JsonBackReference // A backrefrence to keep json from infinite looping
    private Host hostParent;

    @OneToMany(mappedBy = "hostPortParent", cascade = CascadeType.ALL)
    private List<Port> ports;
    @OneToMany(mappedBy = "hostPortParent", cascade = CascadeType.ALL)
    private List<ExtraPort> extraPorts;

    public HostPorts(final List<Port> ports, final List<ExtraPort> extraPorts) {
        this.ports = ports;
        this.extraPorts = extraPorts;
    }

    @JacksonXmlElementWrapper(localName = "portList")
    @JacksonXmlProperty(localName = "port")
    public List<Port> getPorts() {
        return ports;
    }

    @JacksonXmlElementWrapper(localName = "extraPortsList")
    @JacksonXmlProperty(localName = "extraPort")
    public List<ExtraPort> getExtraPorts() {
        return extraPorts;
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


    @Table(name = "HostPortsPort")
    @Entity
    public static class Port implements Serializable {
        @ManyToOne
        @JoinColumn(name = "host_port_id", nullable = false) // Column that will be used to keep track of the parent
        @JsonBackReference // A backrefrence to keep json from infinite looping
        private HostPorts hostPortParent;

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        private Long id;

        private String protocol;
        private String portId;

        @OneToOne(mappedBy = "portParent", cascade = CascadeType.ALL)
        private StateDetails stateDetails;
        @OneToOne(mappedBy = "portParent", cascade = CascadeType.ALL)
        private ServiceDetails serviceDetails;

        public Port(final String protocol, final String portId, final StateDetails stateDetails, final ServiceDetails serviceDetails) {
            this.protocol = protocol;
            this.portId = portId;
            this.stateDetails = stateDetails;
            this.serviceDetails = serviceDetails;
        }

        public String getProtocol() {
            return protocol;
        }

        public String getPortId() {
            return portId;
        }

        public StateDetails getStateDetails() {
            return stateDetails;
        }

        public ServiceDetails getServiceDetails() {
            return serviceDetails;
        }

        public Long getId() {
            return id;
        }

        public void setParentHostPorts(HostPorts hostPortParent) {
            this.hostPortParent = hostPortParent;
        }

        public HostPorts getParentHostPorts() {
            return hostPortParent;
        }
    }


    @Table(name = "HostPortsExtraPorts")
    @Entity
    public static class ExtraPort implements Serializable {
        @ManyToOne
        @JoinColumn(name = "host_port_id", nullable = false) // Column that will be used to keep track of the parent
        @JsonBackReference // A backrefrence to keep json from infinite looping
        private HostPorts hostPortParent;

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        private Long id;

        private String state;
        private String count;
        @OneToOne(mappedBy = "extraPortsParent", cascade = CascadeType.ALL)
        private ExtraReason extraReason;

        public ExtraPort(final String state, final String count, final ExtraReason extraReason) {
            this.state = state;
            this.count = count;
            this.extraReason = extraReason;
        }

        public String getState() {
            return state;
        }

        public String getCount() {
            return count;
        }

        public ExtraReason getExtraReason() {
            return extraReason;
        }

        public Long getId() {
            return id;
        }

        public void setParentHostPorts(HostPorts hostPorts) {
            this.hostPortParent = hostPorts;
        }

        public HostPorts getParentHostPorts() {
            return hostPortParent;
        }
    }
}
