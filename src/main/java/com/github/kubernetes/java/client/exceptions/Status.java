package com.github.kubernetes.java.client.exceptions;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.google.common.base.MoreObjects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Status {
    private String kind, status, message, reason;
    private int code;
    private StatusDetails details;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public StatusDetails getDetails() {
        return details;
    }

    public void setDetails(StatusDetails details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("kind", kind).add("status", status).add("message", message)
                .add("reason", reason).add("code", code).add("details", details).toString();
    }

}
