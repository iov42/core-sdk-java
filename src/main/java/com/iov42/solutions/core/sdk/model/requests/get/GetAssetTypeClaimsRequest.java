package com.iov42.solutions.core.sdk.model.requests.get;

public class GetAssetTypeClaimsRequest extends GetAssetTypeRequest {

    private Integer limit;

    private String next;

    public GetAssetTypeClaimsRequest(String nodeId, String assetTypeId) {
        super(nodeId, assetTypeId);
    }

    public GetAssetTypeClaimsRequest(String requestId, String nodeId, String assetTypeId) {
        super(requestId, nodeId, assetTypeId);
    }

    public GetAssetTypeClaimsRequest(String nodeId, String assetTypeId, Integer limit, String next) {
        super(nodeId, assetTypeId);
        this.limit = limit;
        this.next = next;
    }

    public GetAssetTypeClaimsRequest(String requestId, String nodeId, String assetTypeId, Integer limit, String next) {
        super(requestId, nodeId, assetTypeId);
        this.limit = limit;
        this.next = next;
    }

    public Integer getLimit() {
        return limit;
    }

    public String getNext() {
        return next;
    }

    @Override
    public String toString() {
        return "GetAssetTypeClaimsRequest{" +
                "limit=" + limit +
                ", next='" + next + '\'' +
                "} " + super.toString();
    }
}
