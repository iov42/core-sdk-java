package com.iov42.solutions.core.sdk.model.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.iov42.solutions.core.sdk.model.ProtocolType;
import com.iov42.solutions.core.sdk.model.PublicCredentials;

/**
 * Structure represents information of a proof.
 * @see <a href="https://api.iov42.com/#tag/proofs/paths/~1proofs~1{requestId}/get">iov42 platform API specification: proofs</a> for more information.
 */
public class ProofInfoResponse {

    public static class IdentifierInfo {

        @JsonProperty("_type")
        private String type;
        private String id;

        public String getId() {
            return id;
        }

        public String getType() {
            return type;
        }
    }

    public static class SealInfo {
        private String signature;
        private ProtocolType protocolId;
        private String identityId;

        public String getSignature() {
            return signature;
        }

        public ProtocolType getProtocolId() {
            return protocolId;
        }

        public String getIdentityId() {
            return identityId;
        }
    }

    public static class LinkInfo {
        private IdentifierInfo target;
        private SealInfo[] seals;

        public IdentifierInfo getTarget() {
            return target;
        }

        public SealInfo[] getSeals() {
            return seals;
        }
    }

    public static class NodeInfo {
        private IdentifierInfo id;
        private String payload;

        private LinkInfo[] links;

        public IdentifierInfo getId() {
            return id;
        }

        public String getPayload() {
            return payload;
        }

        public LinkInfo[] getLinks() {
            return links;
        }
    }

    public static class ProofInfo {
        private NodeInfo[] nodes;

        public NodeInfo[] getNodes() {
            return nodes;
        }
    }

    public static class SignatoryInfo {
        private String identity;

        private PublicCredentials credentials;

        public String getIdentity() {
            return identity;
        }

        public PublicCredentials getCredentials() {
            return credentials;
        }
    }

    public static class FingerprintInfo {

        private String requestId;

        private SealInfo seal;

        public String getRequestId() {
            return requestId;
        }

        public SealInfo getSeal() {
            return seal;
        }
    }

    private String requestId;

    private ProofInfo proof;

    private SignatoryInfo[] signatories;

    private FingerprintInfo[] parentFingerprints;

    public String getRequestId() {
        return requestId;
    }

    public ProofInfo getProof() {
        return proof;
    }

    public SignatoryInfo[] getSignatories() {
        return signatories;
    }

    public FingerprintInfo[] getParentFingerprints() {
        return parentFingerprints;
    }
}
