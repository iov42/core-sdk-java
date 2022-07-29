package com.iov42.solutions.core.sdk.model;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashMap;
import java.util.Map;

public class HealthChecks {

    private BuildInfo buildInfo;

    @JsonAnySetter
    private final Map<String, Object> details = new HashMap<>();

    public boolean isHealthy() {
        for (Object module : details.values()) {
            if (module instanceof Map<?, ?>) {
                Map<?, ?> properties = (Map<?, ?>) module;
                for (Object state : properties.values()) {
                    if (state instanceof Boolean && Boolean.FALSE.equals(state)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public BuildInfo getBuildInfo() {
        return buildInfo;
    }

    public void setBuildInfo(BuildInfo buildInfo) {
        this.buildInfo = buildInfo;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BuildInfo {
        private String name;
        private String version;
        private String scalaVersion;
        private String sbtVersion;

        public String getName() {
            return name;
        }

        public String getVersion() {
            return version;
        }

        public String getScalaVersion() {
            return scalaVersion;
        }

        public String getSbtVersion() {
            return sbtVersion;
        }
    }
}
