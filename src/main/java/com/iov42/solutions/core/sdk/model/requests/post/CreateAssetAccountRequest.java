package com.iov42.solutions.core.sdk.model.requests.post;

public class CreateAssetAccountRequest extends BaseCreateAssetRequest {

    /**
     * TODO: Refactor after migrate to Jakcson:
     *
     * @JsonInclude(JsonInclude.Include.NON_NULL)
     * @JsonFormat(shape = JsonFormat.Shape.STRING)
     * private BigInteger quantity;
     */
    private final String quantity;


    public CreateAssetAccountRequest(String requestId, String assetId, String assetTypeId, String quantity) {
        super(requestId, assetId, assetTypeId);
        this.quantity = quantity;
    }

    public String getQuantity() {
        return quantity;
    }
}
