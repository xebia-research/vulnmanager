package com.xebia.vulnmanager.nmap.objects;

import java.io.Serializable;
import java.util.List;

public class HostPorts implements Serializable {
    private List<Port> ports;
    private List<ExtraPort> extraPorts;

    public HostPorts(final List<Port> ports, final List<ExtraPort> extraPorts) {
        this.ports = ports;
        this.extraPorts = extraPorts;
    }

    public List<Port> getPorts() {
        return ports;
    }

    public List<ExtraPort> getExtraPorts() {
        return extraPorts;
    }

    public static class Port implements Serializable {
        private String protocol;
        private String portId;
        private StateDetails stateDetails;
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
    }

    public static class ExtraPort implements Serializable {
        private String state;
        private String count;
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
    }
}
