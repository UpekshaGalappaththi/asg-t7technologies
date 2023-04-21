# KfoneStore

This project implementation bases upon a telecommunications company, Kfone, that provides telecom and digital services and products to its customers. The implement scenario is given below and the implementation was completed with the following technology stack.

- [Android](https://developer.android.com) - As the front-end development framework.
- [AWS API Gateway](https://aws.amazon.com/api-gateway/) - As the API Gateway Service that validates tokens.
- [Java Spring Boot](https://spring.io/) - As the back-end development framework.
- [Fly](https://fly.io/) - As a hosting platform for the back-end service.

## Table of Contents

- [Kfone Consumer Application Requirement Discussion ](#kfone-consumer-application-requirement-discussion)
  - [Requirement 1 - Window shopping ](#requirement-1-window-shopping)
  - [Requirement 2 - Integrating Asgardeo with the Application ](#requirement-2-integrating-asgardeo-with-the-application)
  - [Requirement 3 - User being able to purchase devices or services ](#requirement-3-user-being-able-to-purchase-devices-or-services)
  - [Requirement 4 - Progressive Profiling ](#requirement-4-progressive-profiling)
  - [Requirement 4 - Recovery](#requirement-4-recovery)
- [Architecture](#architecture)
- [Configuring the Project locally](#configuring-the-project-locally)
  - [Prerequisites to be installed](#prerequisites-to-be-installed)
  - [Configure Asgardeo](#configure-asgardeo)
  - [Build & Deploy](#build-deploy)

## Kfone Consumer Application Requirement Discussion

Kfone wants to release an e-portal where customers can browse and purchase devices and services online. At Kfone, you want to secure access to this application, allowing your customers to register and consume services. You may want your customers to be able to manage their accounts, log in to the app, and secure their accounts with MFA. For that, you will use Asgardeo as your Customer Identity & Access Management (CIAM) solution to offload your burden of managing users and handling logins, registrations, and verifying email details.

_Devices for sale:_

- Smartphones: Smartphones are a key product category for telecom device sellers, with a wide range of options available from popular brands such as Apple, Samsung, and Google.
- Tablets: Tablets are another popular product category that could be sold through a telecom device selling application, with options ranging from basic models to high-end devices designed for productivity and entertainment.
- Wearable devices: Wearable devices such as smartwatches and fitness trackers have become increasingly popular in recent years, and could be a valuable product category for a telecom device-selling application.

_Services for sale:_

- Wireless services: One of the core services offered by telecommunications companies is wireless services, including mobile phone plans and data plans. These plans could include features such as unlimited talk and text, data rollover, and family plans.
- Internet services: Another key service offered by telecommunications companies is internet services, including home broadband and mobile data plans.
- Cloud services: With more businesses moving their operations to the cloud, telecommunications companies can offer cloud-based services such as cloud storage, backup, and disaster recovery.

_Kfone loyalty programme_

Kfone loyalty program has three tiers: Silver, Gold, and Platinum. Customers earn points based on their monthly spending on the telecom provider's services or device purchaces. The number of points earned determines the customer's tier level. A new customers who haven’t attained any tier levels earn 0.5 point for every $1 spent.

Each tier level offers different rewards and benefits. Here are some examples:

- Silver Tier (150): Customers earn 1 point for every $1 spent. At this level, customers can redeem their points for discounts on their monthly bills or for small gifts such as branded accessories or free data for a limited time.
- Gold Tier (300): Customers earn 2 points for every $1 spent. At this level, customers can redeem their points for larger discounts on their monthly bills or for more valuable gifts such as a free month of service or a discounted device.
- Platinum Tier(500): Customers earn 3 points for every $1 spent. At this level, customers can redeem their points for the most valuable rewards, such as a free device or a vacation package. Platinum customers also have access to exclusive perks such as priority customer service and free upgrades.

### Requirement 1 - Window shopping

The Kfone store should display available mobile phones and services to web visitors without requiring them to log in. A sample set of devices are available as a [GitHub Gist](https://gist.github.com/ayshsandu/1768aa1f4a349dc77e086bfc40efedd2).

- Visitors will see sign-in and sign-up options, but purchasing without logging in should be possible.
- The necessary services should be implemented to enable the listing of available devices and services in the UI.

Note: Ideally in this kind of e-store, users should be able to purchase without login. During the scope of this project, you don’t have to implement it. The "Add to cart" option can be disabled or prompt users to log in before adding to cart.

### Requirement 2 - Integrating Asgardeo with the Application

During sign-up, users can use email/password or social media accounts to create their accounts. Existing users can log  
in with their username/password or social media accounts. Once logged in, users can buy items or purchase services.

- The login options used in this application were: Username/password, [Google](https://wso2.com/asgardeo/docs/guides/authentication/social-login/add-google-login/), [Passkey](https://wso2.com/asgardeo/docs/guides/authentication/passwordless-login/add-passwordless-login-with-fido/).

### Requirement 3 - User being able to purchase devices or services

- Display logged-in users' details as a greeting.
- Display the tier of the user.
  - For new users just display a message how many points needed to achieve silver membership.
- Items followed need to be stored per user and when the user logs in next time, it should be displayed.
- Users should be able to add items to their cart and purchase them.
- In the scope of this project, you don’t need to implement the actual payment part.
  - Mocking a purchase, store the order details for future use once the purchase is made.
  - When a purchase is done, calculate the points according to the user’s tier and update in the profile.

### Requirement 4 - Progressive Profiling

- If the email verification is not done, display a warning that the account is unverified.
  - When login in the next time, display a message that account is not verified.
- When following an item if the first name is not available ask the user for the name and save it in the user's profile.
- When buying an item get the home address and store it in the profile.

### Requirement 4 - Recovery

The application users should be able to recover their account when the account password is forgotten.

## Architecture

The overall architecture level request flow of this project is depicted in the diagram mentioned below.

## Configuring the Project locally

### Prerequisites to be installed

- [Java 17 or greater](https://www3.ntu.edu.sg/home/ehchua/programming/howto/jdk_howto.html)
- [Maven](https://maven.apache.org/install.html)
- [Git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git)
- [Android Studio](https://developer.android.com/studio)
- [Fly + Fly Account](https://fly.io/docs/)
- [Asgardeo Account](https://wso2.com/asgardeo/)

### Configure Asgardeo

- Login to the [Asgardeo Console](console.asgardeo.io/).
- Create a Management application to use for this application. You can find more information about how to configure it [Here](https://wso2.com/asgardeo/docs/guides/applications/register-standard-based-app/#register-an-application).
- Select the Public Client Option.
- Enter the Authorized redirect URL as 'com.t7.consumer://callback'.
- Once you are redirected to the application configuration page under Allowed grant types select ‘Code’
- Under 'PKCE' select 'Mandatory'
- Under 'Access Token' select 'JWT'
- Update the Application
- Furthermore since this application is using a Points based loyalty system and tiers, you will need to create two custom attributes and send them in the Token as well.
- Create the custom attributes 'points' and 'tier' in the Asgardeo console. You can configure it according to the documentation [Here](https://wso2.com/asgardeo/docs/guides/applications/register-standard-based-app/#register-an-application).
- Next you will need to create a custom scope called 'loyalty' where the custom attributes 'points' and 'tier' are included. You can learn how to configure a custom scope from [Here](https://wso2.com/asgardeo/docs/guides/users/attributes/manage-scopes/#create-custom-scopes).
- Finally add the custom scope to the application user attributes by selecting the scope in User Attributes tab in the application and updating.
- Now the application will request the loyalty scope as well and will receive the custom attribute values in the token.

### Build & Deploy

- Clone the project to a suitable directory using the following command.

```bash
git clone https://github.com/janakamarasena/asg-t7technologies.git
```

- Open the cloned repository and navigate to the root folder.
- The **T7Consumer** directory contains the android application while the **T7ConsumerBackend** directory contains the spring boot application.
- To build and deploy the back-end you can follow the steps mentioned below.
- Navigate to the **T7ConsumerBackend** directory, change the name of the organization in the `SCIM_ME_API_ENDPOINT` variable under the file `KfoneStoreConsumerConstants.java` from `t7technologies` to your organization name and execute the command `mvn install` to build the back-end.
- Afterwards you can follow the steps mentioned at [Fly](https://fly.io/docs/languages-and-frameworks/dockerfile/) to deploy and host the back-end. **Note that a Dockerfile is already available in this repository so you do not need to create one**. If you require, you can also run it locally using the command `mvn spring-boot:run`.
- To build and run the android application, you can follow the steps below.
  - Navigate to the **T7Consumer** directory, change the name of the Asgardeo organization from `t7technologies` to your organization name in all the occurrences inside the `build.gradle` file. Additionally, you should also change the Asgardeo client id and the API endpoints in the same file to match the back-end hosted URL (The path for devices endpoint is `/devices` and `/purchase` for purchase). Note that you can front the back-end using [AWS API Gateway](https://aws.amazon.com/api-gateway/) if you require so.
  - Afterwards, you can [run the Android application](https://developer.android.com/studio/run) as you normally would.
