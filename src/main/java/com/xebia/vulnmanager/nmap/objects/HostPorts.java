package com.xebia.vulnmanager.nmap.objects;

import java.io.Serializable;
import java.util.List;

public class HostPorts implements Serializable {
    private List<Port> ports;
    private List<ExtraPort> extraPorts;

    public void setPorts(List<Port> ports) {
        this.ports = ports;
    }

    public void setExtraPorts(List<ExtraPort> extraPorts) {
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
        private StateDetails state;
        private ServiceDetails serviceDetails;

        public void setProtocol(String protocol) {
            this.protocol = protocol;
        }

        public void setPortId(String portId) {
            this.portId = portId;
        }

        public void setState(StateDetails state) {
            this.state = state;
        }

        public void setServiceDetails(ServiceDetails serviceDetails) {
            this.serviceDetails = serviceDetails;
        }

        public String getProtocol() {
            return protocol;
        }

        public String getPortId() {
            return portId;
        }

        public ServiceDetails getServiceDetails() {
            return serviceDetails;
        }

        public StateDetails getState() {
            return state;
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

        public void setState(String state) {
            this.state = state;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public void setExtraReason(ExtraReason extraReason) {
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
