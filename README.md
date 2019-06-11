# CloudSight Android SDK
[![](https://jitpack.io/v/cloudsight/cloudsight-android-sdk.svg)](https://jitpack.io/#cloudsight/cloudsight-android-sdk)

CloudSight API SDK for Android.

The CloudSight API SDK for Android includes options for:

 - Recognizing images from a file on device
 - Recognizing images from a remote URL
 - Setting the localization for an image

This SDK is designed to work with at least Android SDK 16.

## Table of Contents

  * [Getting Started](#getting-started)
  * [Installation](#installation)
    * [Gradle](#gradle)
  * [Usage](#usage)
    * [Authentication](#authentication)
    * [Sending Images](#sending-images)
  * [Example / Demo Application](#example---demo-application)
  * [License](#license)

## Getting Started

You will need an `api-key` (and `secret`, if using OAuth1 authentication). You can obtain an `api-key` with a free trial account by [contacting the CloudSight Sales Team](https://cloudsight.ai/contact). We love for developers to try CloudSight, so we encourage you to contact us for a free trial of CloudSight!

## Installation

To use the CloudSight Android SDK, add the compile dependency with the latest version of the CloudSight SDK.
You will need to add the `android.permission.INTERNET` and `android.permission.READ_EXTERNAL_STORAGE` permissions in the Manifest of your Android project.

### Gradle
To get a Git project into your build:

#### Step 1. Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
#### Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.cloudsight:cloudsight-android-sdk:Tag'
	}

## Usage

### Authentication
Create a client instance using simple API key-based authentication:

```java
CloudSightClient client = new CloudSightClient().init("api-key");

```

Or, if you using OAuth1 Authentication:

```java
CloudSightClient client = new CloudSightClient().init("api-key", "secret");
```

Also, you may set localization for your responses from `CloudSightClient`:

```java
client.setLocale("en-US");

```

### Sending Images

Sending an image file from your device:

```java
File file = new File();
client.getImageInformation(file, new CloudSightCallback() {

            @Override
            public void imageUploaded(CloudSightResponse response) {
          		Log.d("CloudSight ", "Recognition started");
        	  }

            @Override
            public void imageRecognized(CloudSightResponse response) {
          		Log.d("CloudSight ", "Recognition finished");
            }

            @Override
            public void imageRecognitionFailed(String reason) {
          		Log.d("CloudSight ", "Recognition failed by reason");
            }

            @Override
            public void onFailure(Throwable throwable) {
          		Log.d("CloudSight ", "Recognition failed");
            }
        });
```

Or, you can send the image using a remote URL:

```java
String url = "http://www.example.com/image.png"
client.getImageInformation(url, new CloudSightCallback() {

            @Override
            public void imageUploaded(CloudSightResponse response) {
          		Log.d("CloudSight ", "Recognition started");
        	  }

            @Override
            public void imageRecognized(CloudSightResponse response) {
          		Log.d("CloudSight ", "Recognition finished");
            }

            @Override
            public void imageRecognitionFailed(String reason) {
          		Log.d("CloudSight ", "Recognition failed by reason");
            }

            @Override
            public void onFailure(Throwable throwable) {
          		Log.d("CloudSight ", "Recognition failed");
            }
        });
```

## Example / Demo Application

We have included a working Android example of this SDK to demonstrate how it works and can be implemented. You can run this example by opening the `app` directory in Android Studio.

## License

The CloudSight Android SDK is released under the MIT license.
