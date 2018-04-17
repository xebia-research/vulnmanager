package com.xebia.vulnmanager.models.zap.objects;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "RiskInstance")
@Entity
public class RiskInstance implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "alert_item_id", nullable = false) // Column that will be used to keep track of the parent
    @JsonBackReference // A backrefrence to keep json from infinite looping
    private ZapAlertItem zapAlertItem;

    private String uri;
    private String method;
    private String param;

    public RiskInstance() {
        // Empty constructor
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZapAlertItem getZapAlertItem() {
        return zapAlertItem;
    }

    public void setZapAlertItem(ZapAlertItem zapAlertItem) {
        this.zapAlertItem = zapAlertItem;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }
}
