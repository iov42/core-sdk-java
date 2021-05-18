package com.iov42.solutions.core.sdk.model;

public class HealthChecks {

    private BuildInfo buildInfo;

    private WriteStatus broker;

    private ReadWriteStatus requestStore;

    private ReadWriteStatus assetStore;

    private ReadWriteStatus claimStore;

    private ReadWriteStatus endorsementStore;

    private ReadWriteStatus permissionStore;

    private ReadWriteStatus proofStore;

    private ReadWriteStatus transactionStore;

    private ReadWriteStatus transactionEntryForReaderStore;

    private HsmStatus hsm;

    public BuildInfo getBuildInfo() {
        return buildInfo;
    }

    public void setBuildInfo(BuildInfo buildInfo) {
        this.buildInfo = buildInfo;
    }

    public WriteStatus getBroker() {
        return broker;
    }

    public ReadWriteStatus getRequestStore() {
        return requestStore;
    }

    public ReadWriteStatus getAssetStore() {
        return assetStore;
    }

    public ReadWriteStatus getClaimStore() {
        return claimStore;
    }

    public ReadWriteStatus getEndorsementStore() {
        return endorsementStore;
    }

    public ReadWriteStatus getPermissionStore() {
        return permissionStore;
    }

    public ReadWriteStatus getProofStore() {
        return proofStore;
    }

    public ReadWriteStatus getTransactionStore() {
        return transactionStore;
    }

    public ReadWriteStatus getTransactionEntryForReaderStore() {
        return transactionEntryForReaderStore;
    }

    public HsmStatus getHsm() {
        return hsm;
    }

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

    public static class WriteStatus {
        private Boolean canWrite;

        public Boolean getCanWrite() {
            return canWrite;
        }
    }

    public static class ReadWriteStatus extends WriteStatus {
        private Boolean canRead;

        public Boolean getCanRead() {
            return canRead;
        }
    }

    public static class HsmStatus {
        private Boolean hasKeys;

        public Boolean getHasKeys() {
            return hasKeys;
        }
    }
}
