/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */
package com.github.kubernetes.java.client.exceptions;

import javax.ws.rs.ClientErrorException;

public class KubernetesClientException extends Exception {

    private static final long serialVersionUID = -7521673271244696906L;

    public KubernetesClientException(String message, Exception exception) {
        super(message, exception);
    }

    public KubernetesClientException(Exception exception) {
        super(buildMessage(exception), exception);
    }

    public KubernetesClientException(String msg) {
        super(msg);
    }

    private static Status getResponse(Throwable exception) {
        if (exception instanceof ClientErrorException) {
            ClientErrorException error = (ClientErrorException) exception;
            return error.getResponse().readEntity(Status.class);
        }
        return null;
    }

    private static String buildMessage(Exception exception) {
        Status response = getResponse(exception);
        return response != null ? response.getMessage() : null;
    }

    public Status getResponse() {
        return getResponse(getCause());
    }
}
