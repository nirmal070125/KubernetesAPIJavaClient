package com.github.kubernetes.java.client.exceptions;

import com.google.common.base.MoreObjects;

public class StatusDetailsCause {
    private String reason, message, field;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("reason", reason).add("message", message).add("field", field)
                .toString();
    }
}
