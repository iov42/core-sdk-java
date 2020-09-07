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
