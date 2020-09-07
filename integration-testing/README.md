# iov42 Core Java SDK integration tests

This module contains integration tests for the iov42 core SDK for Java.

## General

In here you can find some integration tests that also show the basic usage of the iov42 core SDK (as well as concepts of the platform itself).
The platform URL that points to the iov42 platform instance used by the tests can be configured in the 
[integration-test.properties](src/test/resources/integration-test.properties) file.
 

**Attention**: You will notice that instead of a valid URL it currently says "*&lt;Insert Platform URL here&gt;*". 
If you want to test the SDK/platform yourself please contact [iov42](https://iov42.com/contact/) to request a valid platform URL.

## Use Case Demo

Included in the integration tests is a [demo integration test](src/test/java/com/iov42/solutions/core/sdk/demo/DemoTest.java)
that shows a simplified platform use case.