# iov42 Core Java SDK

This repository contains the iov42 core SDK for Java, that can be used to interact with the iov42 platform.

 Below you can find instructions on how to install and use this library. For additional information visit our
 Platform API specification [here](https://tech.iov42.com/platform/api/).

## Installation and setup

The prerequisites for installing the SDK are `git` and JDK (version 8 or higher).

Clone the repository with the example code, install all dependencies, and build package:
```shell
$ git clone https://github.com/iov42/core-sdk-java.git
$ cd core-sdk-java
$ ./gradlew assemble
```

## Running automated tests

You can run the test cases by using the gradle tool:
```console
$ ./gradlew test
```

## Using the SDK in Java applications

The following code snippets show how to use the SDK.

### Importing library, instantiating the platform client and creating an Identity.

See how the `PlatformClient` can be instantiated and used to submit requests to the iov42 platform (in the example 
to create a new identity).

```java
import com.iov42.solutions.core.sdk.*;
import com.iov42.solutions.core.sdk.model.*;
import com.iov42.solutions.core.sdk.model.requests.command.*;
import com.iov42.solutions.core.sdk.model.responses.RequestInfoResponse;
import com.iov42.solutions.core.sdk.utils.PlatformUtils;

import java.util.*;
import java.security.KeyPair;
```
```java
PlatformClient client = new PlatformClient("<Insert Platform URL here>");

String identityId = UUID.randomUUID().toString();
KeyPair keyPair = PlatformUtils.generateKeyPair(ProtocolType.SHA256WithRSA);
SignatoryInfo signatoryInfo = new SignatoryInfo(identityId, ProtocolType.SHA256WithRSA, keyPair);
        
CreateIdentityRequest request = new CreateIdentityRequest(identityId, signatoryInfo.toPublicCredentials());
RequestInfoResponse response = client.send(request, signatoryInfo).join();
```

### Creating an Asset Type

Create an asset with the `PlatformClient` owned by a specific Identity. There are two main types of asset on the platform - unique and quantifiable.

To create a new unique asset type:
```java
SignatoryInfo myIdentityInfo = new SignatoryInfo(myIdentityId, myProtocolType, myKeyPair);

String assetTypeId = "unique-asset-type-id";

CreateAssetTypeRequest uniqueAssetTypeRequest = CreateAssetTypeRequest.unique(assetTypeId);
RequestInfoResponse response = client.send(uniqueAssetTypeRequest, myIdentityInfo).join();
```

To create a new quantifiable asset type (the scale is the number of decimal places to support):
```java
SignatoryInfo myIdentityInfo = new SignatoryInfo(myIdentityId, myProtocolType, myKeyPair);

String assetTypeId = "quantifiable-asset-type-id";
// scale represents the number of decimal places the quantifiable asset supports
int scale = 2;

CreateAssetTypeRequest quantifiableAssetTypeRequest = CreateAssetTypeRequest.quantifiable(assetTypeId, scale);
RequestInfoResponse response = client.send(quantifiableAssetTypeRequest, myIdentityInfo).join();
```

### Creating an Asset

Once you created an asset type, you can create the actual asset.

To create a new unique asset:
```java
SignatoryInfo myIdentityInfo = new SignatoryInfo(myIdentityId, myProtocolType, myKeyPair);

String uniqueAssetTypeId = "unique-asset-type-id";
String assetId = "asset-id";

CreateAssetRequest createAssetRequest = new CreateAssetRequest(uniqueAssetTypeId, assetId);
RequestInfoResponse response = client.send(createAssetRequest, myIdentityInfo).join();
```

To create quantifiable assets:

The amount of a quantifiable asset is held in something equivalent to an account. 
When the account is created you can also pass an initial balance if required (a specific quantity is minted).
```java
SignatoryInfo myIdentityInfo = new SignatoryInfo(myIdentityId, myProtocolType, myKeyPair);

String quantifiableAssetTypeId = "quantifiable-asset-type-id";
// this instance of the quantifiable asset represents an account 
String accountId = "account-id";
// remember that the scale has to be considered when putting an amount
// in this example an amount of 10.00 is added assuming a scale of 2
BigInteger amount = BigInteger.valueOf(1000); 
        
CreateAssetRequest createAssetRequest = CreateAssetRequest(quantifiableAssetTypeId, accountId, amount);
RequestInfoResponse response = client.send(createAssetRequest, myIdentityInfo).join();
```

### Transfers

When you have assets, you can transfer them between identities.

To transfer the ownership of a unique asset:
```java
SignatoryInfo myIdentityInfo = new SignatoryInfo(myIdentityId, myProtocolType, myKeyPair);

String uniqueAssetTypeId = "unique-asset-type-id";
String assetId = "asset-id";
String receiverId = "receiver-identity-id";

TransfersRequest transferRequest = TransfersRequest.of(
        new TransferOwnership(uniqueAssetTypeId, assetId, myIdentityInfo.getIdentityId(), receiverId));

RequestInfoResponse response = client.send(transferRequest, myIdentityInfo).join();
```

To transfer quantity from one account to another:

For this transfer two quantifiable asset accounts are required one that holds an amount of the asset and another one that should receive the amount.
```java
SignatoryInfo myIdentityInfo = new SignatoryInfo(myIdentityId, myProtocolType, myKeyPair);

String quantifiableAssetTypeId = "quantifiable-asset-type-id";
String senderAccountId = "account-id";
// the receiver account id has to be an account that does already exist of the same asset type then the one to be transferred
String receiverAccountId = "receiver-account-id";
BigInteger amount = BigInteger.valueOf(500);

TransfersRequest transferRequest =
        TransfersRequest.of(new TransferQuantity(quantifiableAssetTypeId, senderAccountId, receiverAccountId, amount));
RequestInfoResponse response = client.send(transferRequest, myIdentityInfo).join();
```

### Claims

Identities, assets and asset types can be "extended" by adding claims to them. A claim is just the hash of some text. 
The text could be structured or unstructured. The platform takes the hash and the text and checks that the hash is really the hash for the text and then discards the text from that point on. 
It only stores and deals with the hash after that.

To add claims to an identity:
```java
String identityId = myIdentityInfo.getIdentityId();
Claim claims = Claims.of("claim");

CreateIdentityClaimsRequest createIdentityClaimsRequest = new CreateIdentityClaimsRequest(identityId, claims);
RequestInfoResponse response = client.send(createIdentityClaimsRequest, myIdentityInfo).join();
```

To add claims to an asset type:
```java
String assetTypeId = "some-asset-type-id";
Claim claims = Claims.of("claim");

CreateAssetTypeClaimsRequest createAssetTypeClaimsRequest = new CreateAssetTypeClaimsRequest(assetTypeId, claims);
RequestInfoResponse response = client.send(createAssetTypeClaimsRequest, myIdentityInfo).join();
```

To add claims to an asset (or an account):
```java
String assetTypeId = "some-asset-type-id";
String assetId = "some-asset-id";
Claims claims = Claims.of("claim");

CreateAssetClaimsRequest createAssetClaimsRequest = new CreateAssetClaimsRequest(assetTypeId, assetId, claims);
RequestInfoResponse response = client.send(createAssetClaimsRequest, myIdentityInfo).join();
```

### Endorsements

The claims that were created can be endorsed by identities - either the same identity or, more likely, other identities. 
If another identity is involved then both parties need to sign the endorsement being created. 
This means there is likely to be an out of band communication to get to the point where the endorsement can be submitted.

To create an endorsement for a claim on an identity
```java
Claims claims = Claims.of("claim");
CreateIdentityEndorsementsRequest createIdentityEndorsementsRequest =
    new CreateIdentityEndorsementsRequest(identityId, endorserId, new Endorsements(endorserInfo, identityId, claims));

// this request requires two authorisation signatures (from the owner and the endorser) in case the claim was not created beforehand
AuthorisedRequest authRequest = AuthorisedRequest.from(createIdentityEndorsementsRequest)
        .authorise(identityInfo)
        .authorise(endorserInfo);

RequestInfoResponse response = client.send(authRequest, identity).join();
```

### Querying the iov42 platform

Once you have an identity on the iov42 platform you can issue query requests (i.e., query requests have to be authenticated with an identity).
In the example below you see an identity that queries the public credentials of another identity.

```java
PlatformClient client = new PlatformClient("<Insert Platform URL here>");

SignatoryInfo myIdentityInfo = new SignatoryInfo(myIdentityId, myProtocolType, myKeyPair);

GetIdentityRequest request = new GetIdentityRequest(otherIdentityId);
PublicCredentials publicCredentials = client.queryAs(myIdentityInfo).getIdentityPublicKey(request);
```

### Advanced: Creating an endorsement request with two authorisation signatures

In this scenario an endorser wants to endorse a claim that was not yet created. There is a variant of the endorsement
creation  where a claim will be created and endorsed in the same request. However, for this to be valid both the owner 
(in our case an identity), and the  endorser need to sign the request. This can be achieved by using an `AuthorisedRequest`.

```java
PlatformClient client = new PlatformClient("<Insert Platform URL here>");

String identityId = UUID.randomUUID().toString();
KeyPair keyPair = PlatformUtils.generateKeyPair(ProtocolType.SHA256WithRSA);
SignatoryInfo identityInfo = new SignatoryInfo(identityId, ProtocolType.SHA256WithRSA, keyPair);

String endorserId = UUID.randomUUID().toString();
KeyPair endorserKeyPair = PlatformUtils.generateKeyPair(ProtocolType.SHA256WithRSA);
SignatoryInfo endorserInfo = new SignatoryInfo(endorserId, ProtocolType.SHA256WithRSA, endorserKeyPair);

Claims claims = Claims.of("claim");
Endorsements endorsements = new Endorsements(endorserInfo, identityId, claims);
CreateIdentityEndorsementsRequest request =
        new CreateIdentityEndorsementsRequest(identityId, endorserId, endorsements);

AuthorisedRequest authorisedRequest = AuthorisedRequest.from(request)
        .authorise(identityInfo)
        .authorise(endorserInfo);

RequestInfoResponse response = client.send(authorisedRequest, endorserInfo).join();
```

### Advanced: Using a custom HttpBackend implementation
The `HttpBackend` represents an interface for accessing a remote server via HTTP.
By default `PlatformClient` uses the `java.util.ServiceLoader` to locate instances of `HttpBackend`.

If you want to use a specific implementation of an `HttpBackend` use the alternative constructor of `PlatformClient`.
Please note that an implementation of `HttpBackend` must properly handle redirects 
(including respecting the `Retry-After` header).
```java
PlatformClient client = new PlatformClient("<Insert Platform URL here>", new MyHttpBackend());
```


For more examples and use-cases please check the [integration-testing](integration-testing) module.

## License

See [LICENSE.txt](LICENSE.txt)
