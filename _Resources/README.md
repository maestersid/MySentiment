# App Center Developer Hackathon

## Requirements:
1. Developer machine with native tooling
2. Source Code- [aka.ms/appcenterhackday](http://aka.ms/appcenterhackday) (fork this repo and clone to your own machine- inside the repo you will find folders for both iOS and Android apps)
3. (if iOS) Apple Developer Account
4. VS Code (For Azure Functions work)
5. Azure Subscription
6. Optional- Headphones for videos/focused work

## Introduction

Let’s imagine that you work for Contoso, Inc. and you have been tasked to develop an app that interacts with your companies’ new website. This is a companion app to bring a native mobile experience to the functionality offered by the website already. You're collaborating with 3 other developers to build this iOS companion app and the team has decided to use Visual Studio App Center to automate the iOS application lifecycle.

The dev team has decided to use Visual Studio App Center automate the iOS application lifecycle. You’ve read that App Center can connect to your repository in minutes, build in the cloud, test on thousands of devices, distribute to beta testers and app stores, and monitor real-world usage with crash and analytics data.

For the next two days you’re going to put App Center to the test to see if it delivers on those promises.

## Familiarization & Side by Side
*Est Time to Complete: 15 min*

**Scenario:** A developer on your team recently heard about App Center at a mobile development conference. After sharing some of the main benefits of the product, your team has decided to see if App Center could streamline your development pipeline into one tool instead of many.

**Challenge:** Sign-in to App Center using your HockeyApp credentials. Once logged into App Center, familiarize yourself with the new interface.

## Challenge 1- CI-CD
*Est. Time to Complete: 90 min*

### 1.1 Setting up Your CI/CD Pipeline (60 min)

**Scenario:** You have your code on your local machine, but as you collaborate with your team, you want to ensure nobody is breaking the build as they all commit their updates. You want an autonomous solution to build this all for you, without hardware to manage and configure. Additionally, you want to have these new builds verified and automatically distributed to an internal alpha test group. 

**Challenge:** Connect App Center to your GitHub repository, configure a continuous build whenever code is submitted that includes a launch test and automatic distribution to a beta test group. Include yourself in the beta test group. Verify everything builds and is distributed. 

### 1.2 Distribution Management (30 min)

**Scenario:** Now that you have your general build pipeline established, your QA team needs a build delivered to them on a regular basis. Additionally, you want to start sharing your app to external groups, so you will need an additional public test group as well

**Challenge:** Setup two distribution groups. The first being an internal beta distribution group that includes yourself and along with the [#] tester emails below. Secondly, create a separate public distribution group. Distribute the existing build from challenge 1.1 to both groups and lastly install the release on your device.

Emails to invite
1. Your own email address
2. Dayton.register@gmail.com 
3. Lincoln.exchanges@gmail.com 
4. Jaina.mayberry@outlook.com 
5. Tom.Swifty@yahoo.com 

## Challenge 2- Test
*Est. Time to Complete: 2.5 hours*

### 2.1 Building your Test Suite (60 min)

**Scenario:** You have added new functionality to your application, and you need to verify it works. Part of your sign-off process is to integrate new automation scripts into your code that can be run on a regular basis to test for any regressions in the future

**Challenge:** Add new tests to the existing automation suite and test the functionality of the application on your device.

### 2.2 Uploading and Running your Tests (60 min)

**Scenario:** Once you have written functionality, it can be difficult to ensure your app works on the devices your users are using.

**Challenge:** Create a targeted set of devices in App Center, then upload your app and tests and run them on the device set you created.

### 2.3 CI Revisited (30 min)

**Scenario:** Add Test to your Build process

**Challenge:** Integrate your testing as part of your CI/CD process, where tests will launch after Build is completed. (HINT: App Center lacks the ability to toggle a setting for integration of Build->Test but this can be done in other ways)

## Challenge 3- Push
*Est Time to Complete: 45 min*

**Scenario:** You have noticed some general usage trends with your application, and want to send a targeted push notification that includes a thank you to your active users, as well as a promo for an upcoming event. You need to create some segmented messaging based on your audiences.

**Challenge:** Set up push notifications for your application, create a push campaign and send the notification. Send a push, send an event when acknowledged.

## Challenge 4- Diagnostics
*Est. Time to Complete: 90 min*

### 4.1 Crash Reporting and Bug Tracking (60 min)

**Scenario:** Your app is tested, verified and successfully published, but now you are getting reports of crashes from the users. You need better metrics and insight on when these crashes take place, and as much detail about them as possible. Additionally you want to have it integrated into your own bug tracking process to ensure proper workflow and signoff when issues are resolved.

**Challenge:** Add crash reporting to your application and connect it to the GitHub issue tracker (in your forked repo). Trigger a couple of crashes within your app, ensure the workflow has been established.

### Challenge 4.2 Crash Triage

**Scenario:** Your app has been live for a few weeks now, and you have some crash reports. Your job is to figure out best next steps to improve the quality of your app.

**Challenge:** Determine which crashes you will fix first and why. Describe your algorithm for prioritizing crashes. Make sure you investigate all the diagnostic information available to you in the crash logs. 

## Challenge 5- Analytics & Azure App Insights
*Est Time to Complete: 90 min*

### Challenge 5.1 Event Tracking

**Scenario:** With your recently launched app, you want to understand how your app is being used, and if functionality is begin discovered. Additionally you would like greater insight on device usage trends and general session information

**Challenge:** Enable analytics and add manual event tracking to your application, and track the results after a few use cases. Add custom properties to one of the events.

### Challenge 5.2- App Insights

**Scenario:** You have had your application running for a while now, and have been gathering a great deal of analytics and data. Your teams product lead would like some additional reports on how the application is doing and you are tasked with proving some visual representations of the data.

**Challenge:** Connect your app to Azure Application Insights, set up a data export, then within App Insights create a chart from some of the exported data. Take bucketed events and put it into a graph that can be measured. (Hint: You may need to run the app after setting up export and generate new events to get it to show up in App Insights)

---

## Bonus 1- Tools Integration
*Est Time to Complete: 30 min*

**Scenario:** My team likes to be notified when new crashes come in, and has some of their own tools as part of the developer process. You would like to integrate App Center into these workflows you already have in place.

**Challenge:** Use webhooks to set up a notification to a third party service you use (Slack, Teams, etc)

## Bonus 2- Azure Functions
*Est Time to Complete: 60 min*

**Scenario:** After establishing a delivery pipeline and pool of quality testers you feel confident in you can move on to a more automated approach. With all the great analytics and crash data at your fingertips, rather than relying on manual analysis you would like to automatically interpret this data and redistribute a release to a larger public group of testers.

**Challenge:**  Setup and debug the sample Azure Function project locally to validate you are able to achieve the desired automation. Follow it up by configuring this Function to run automatically in the cloud every minute by directly hooking up your repository to Azure.

## Bonus 3- Code Push
*Est Time to Complete: 45 min*

**Scenario:** My team is working in React Native and needs to publish an update quickly. We need a way to get updates to users faster.

**Challenge:** Write an update to a react native application, distribute it then use Code Push to push updates live.

## App Center Documentation
Begin with the [Getting Started](https://docs.microsoft.com/en-us/mobile-center/quickstarts/ios/getting-started) tutorial. After you've completed that, you can do the rest in the order below, or choose a specific tutorial to follow.

| Tutorial | Description |
|:-|:-|
| [Getting Started](https://docs.microsoft.com/en-us/appcenter/quickstarts/ios/getting-started) | Set up the app |
| [Build](https://docs.microsoft.com/en-us/appcenter/quickstarts/ios/build) | Build the app |
| [Test](https://docs.microsoft.com/en-us/appcenter/quickstarts/ios/test) | Run automated UI tests on real devices |
| [Distribute](https://docs.microsoft.com/en-us/appcenter/quickstarts/ios/distribute)| Distribute application to a group of users |
| [Crashes](https://docs.microsoft.com/en-us/appcenter/quickstarts/ios/crashes) | Monitor application crashes |
| [Analytics](https://docs.microsoft.com/en-us/appcenter/quickstarts/ios/analytics) | View user analytics |
| [Push](https://docs.microsoft.com/en-us/appcenter/quickstarts/ios/push) | Send push notifications to your app users |
