package com.iov42.solutions.core.sdk.model;

public class HealthChecks {

    private AssetStore assetStore;

    private Broker broker;

    private BuildInfo buildInfo;

    private ClaimStore claimStore;

    private EndorsementStore endorsementStore;

    private Hsm hsm;

    private ProofStore proofStore;

    private RequestStore requestStore;

    public AssetStore getAssetStore() {
        return assetStore;
    }

    public void setAssetStore(AssetStore assetStore) {
        this.assetStore = assetStore;
    }

    public Broker getBroker() {
        return broker;
    }

    public void setBroker(Broker broker) {
        this.broker = broker;
    }

    public BuildInfo getBuildInfo() {
        return buildInfo;
    }

    public void setBuildInfo(BuildInfo buildInfo) {
        this.buildInfo = buildInfo;
    }

    public ClaimStore getClaimStore() {
        return claimStore;
    }

    public void setClaimStore(ClaimStore claimStore) {
        this.claimStore = claimStore;
    }

    public EndorsementStore getEndorsementStore() {
        return endorsementStore;
    }

    public void setEndorsementStore(EndorsementStore endorsementStore) {
        this.endorsementStore = endorsementStore;
    }

    public Hsm getHsm() {
        return hsm;
    }

    public void setHsm(Hsm hsm) {
        this.hsm = hsm;
    }

    public ProofStore getProofStore() {
        return proofStore;
    }

    public void setProofStore(ProofStore proofStore) {
        this.proofStore = proofStore;
    }

    public RequestStore getRequestStore() {
        return requestStore;
    }

    public void setRequestStore(RequestStore requestStore) {
        this.requestStore = requestStore;
    }

    @Override
    public String toString() {
        return "HealthChecks{" +
                "assetStore=" + assetStore +
                ", broker=" + broker +
                ", buildInfo=" + buildInfo +
                ", claimStore=" + claimStore +
                ", endorsementStore=" + endorsementStore +
                ", hsm=" + hsm +
                ", proofStore=" + proofStore +
                ", requestStore=" + requestStore +
                '}';
    }

    public static class AssetStore {

        private Boolean canRead;

        private Boolean canWrite;

        public Boolean getCanRead() {
            return canRead;
        }

        public void setCanRead(Boolean canRead) {
            this.canRead = canRead;
        }

        public Boolean getCanWrite() {
            return canWrite;
        }

        public void setCanWrite(Boolean canWrite) {
            this.canWrite = canWrite;
        }

        @Override
        public String toString() {
            return "AssetStore{" +
                    "canRead=" + canRead +
                    ", canWrite=" + canWrite +
                    '}';
        }
    }

    public static class Broker {

        private Boolean canWrite;

        public Boolean getCanWrite() {
            return canWrite;
        }

        public void setCanWrite(Boolean canWrite) {
            this.canWrite = canWrite;
        }

        @Override
        public String toString() {
            return "Broker{" +
                    "canWrite=" + canWrite +
                    '}';
        }
    }

    public static class BuildInfo {

        private String name;

        private String sbtVersion;

        private String scalaVersion;

        private String version;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSbtVersion() {
            return sbtVersion;
        }

        public void setSbtVersion(String sbtVersion) {
            this.sbtVersion = sbtVersion;
        }

        public String getScalaVersion() {
            return scalaVersion;
        }

        public void setScalaVersion(String scalaVersion) {
            this.scalaVersion = scalaVersion;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        @Override
        public String toString() {
            return "BuildInfo{" +
                    "name='" + name + '\'' +
                    ", version='" + version + '\'' +
                    ", scalaVersion='" + scalaVersion + '\'' +
                    ", sbtVersion='" + sbtVersion + '\'' +
                    '}';
        }
    }

    public static class ClaimStore {

        private Boolean canRead;

        private Boolean canWrite;

        public Boolean getCanRead() {
            return canRead;
        }

        public void setCanRead(Boolean canRead) {
            this.canRead = canRead;
        }

        public Boolean getCanWrite() {
            return canWrite;
        }

        public void setCanWrite(Boolean canWrite) {
            this.canWrite = canWrite;
        }

        @Override
        public String toString() {
            return "ClaimStore{" +
                    "canRead=" + canRead +
                    ", canWrite=" + canWrite +
                    '}';
        }
    }

    public static class EndorsementStore {

        private Boolean canRead;

        private Boolean canWrite;

        public Boolean getCanRead() {
            return canRead;
        }

        public void setCanRead(Boolean canRead) {
            this.canRead = canRead;
        }

        public Boolean getCanWrite() {
            return canWrite;
        }

        public void setCanWrite(Boolean canWrite) {
            this.canWrite = canWrite;
        }

        @Override
        public String toString() {
            return "EndorsementStore{" +
                    "canRead=" + canRead +
                    ", canWrite=" + canWrite +
                    '}';
        }
    }

    public static class Hsm {

        private Boolean hasKeys;

        public Boolean getHasKeys() {
            return hasKeys;
        }

        public void setHasKeys(Boolean hasKeys) {
            this.hasKeys = hasKeys;
        }

        @Override
        public String toString() {
            return "Hsm{" +
                    "hasKeys=" + hasKeys +
                    '}';
        }
    }

    public static class ProofStore {

        private Boolean canRead;

        private Boolean canWrite;

        public Boolean getCanRead() {
            return canRead;
        }

        public void setCanRead(Boolean canRead) {
            this.canRead = canRead;
        }

        public Boolean getCanWrite() {
            return canWrite;
        }

        public void setCanWrite(Boolean canWrite) {
            this.canWrite = canWrite;
        }

        @Override
        public String toString() {
            return "ProofStore{" +
                    "canRead=" + canRead +
                    ", canWrite=" + canWrite +
                    '}';
        }
    }

    public static class RequestStore {

        private Boolean canRead;

        private Boolean canWrite;

        public Boolean getCanRead() {
            return canRead;
        }

        public void setCanRead(Boolean canRead) {
            this.canRead = canRead;
        }

        public Boolean getCanWrite() {
            return canWrite;
        }

        public void setCanWrite(Boolean canWrite) {
            this.canWrite = canWrite;
        }

        @Override
        public String toString() {
            return "RequestStore{" +
                    "canRead=" + canRead +
                    ", canWrite=" + canWrite +
                    '}';
        }
    }
}
