# BELLATRIX Tailor-made Test Automation Framework
Customize and extend our cross-platform .NET framework to perfectly fit your needs. Start on top of hundreds of best practice features and integrations.

Contains the full source code of BELLATRIX Test Automation Framework and Templates for faster usage

BELLATRIX is not a single thing it contains multiple framework libraries, extensions and tools. The tool is built to be cross-platform, however some of the features can be used under Windows since they are written for Visual Studio.

Simple Installation
------------------
1. Download the BELLATRIX projects as a zip file from the Code green button in the right corner.
2. Unzip it. Open BELLATRIX-Java in IntelliJ
3. Open one of the preconfigured modules. It works for both JUnit and TestNG without additional configuration.

4. Run the sample tests.
5. You can try to write a simple test yourself.
6. For an in-depth revision of all framework features you can check the [**official documentation**](http://docs.java.bellatrix.solutions/web-automation/)

Running Tests through CLI
--------------------------
 To execute your tests via command line in Continues Integration (CI), you can use the native Maven test runner.
1. Navigate to the folder of your test project.
2. Open the CMD there.
3. Execute the following command:

```
mvn clean test
```
For applying filters and other more advanced configuration check the official documentation [**official documentation**](https://junit.org/junit5/docs/current/user-guide/#running-tests-console-launcher)

Both TestNG and JUnit are supported.

Supported Code Editors
----------------------
The recommended code editor for writing BELLATRIX tests is IntelliJ

NOTE: After the support for .NET Framework 5.0 and higher, Microsoft officially not support .NET Core development in older versions of Visual Studio 2015, 2017 and so on.

### Other Supported Editors: ###
- Visual Studio Code
- IntelliJ
- NetBeans
- Eclipse

SDKs and Frameworks Prerequisites
-------------------------------- 
We recommend to install the latest stable version of JDK.

For BELLATRIX desktop modules you need to download [**WinAppDriver**](https://github.com/Microsoft/WinAppDriver/releases). You need to make sure it is started before running any BELLATRIX desktop tests.

For BELLATRIX mobile modules you need to download and install [**Appium**](http://appium.io/). You need to make sure it is started before running any BELLATRIX mobile tests.
