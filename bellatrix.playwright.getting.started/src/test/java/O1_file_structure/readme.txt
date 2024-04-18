    Find detailed information about what each empty project contains or should contain if you wish to create it manually.

    1. Contains the following information of project and configuration information to build the project such dependencies,
    build directory, source directory, test source directory, plugin, e.t. For example:

    <?xml version="1.0" encoding="UTF-8"?>
    <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
        <modelVersion>4.0.0</modelVersion>

        <parent>
            <artifactId>bellatrix</artifactId>
            <groupId>solutions.bellatrix</groupId>
            <version>1.0-SNAPSHOT</version>
        </parent>

        <artifactId>bellatrix.web.tests</artifactId>

        <dependencies>
           <dependency>
                <groupId>solutions.bellatrix</groupId>
                <artifactId>bellatrix.web</artifactId>
                <version>1.0</version>
            </dependency>

            <dependency>
                <groupId>io.rest-assured</groupId>
                <artifactId>rest-assured</artifactId>
                <version>5.0.1</version>
                <scope>test</scope>
            </dependency>

            <dependency>
                <groupId>org.hamcrest</groupId>
                <artifactId>hamcrest-all</artifactId>
                <version>1.3</version>
                <scope>test</scope>
            </dependency>

            <dependency>
                <groupId>com.github.javafaker</groupId>
                <artifactId>javafaker</artifactId>
                <version>1.0.2</version>
            </dependency>
        </dependencies>

    </project>

    2. editorconfig You can read more about it here - https://www.jetbrains.com/help/idea/editorconfig.html

    In short: EditorConfig is use for defining coding styles and a collection of text editor plugins that enable editors
    to read the file format and adhere to defined styles. EditorConfig files are easily readable and they work nicely
    with version control systems.
    As with .editorconfig, you can change the predefined rules to fit your company's standards.

    3. Test framework settings files

    There are two files testFrameworkSettings. They are JSON files. Depending on your test build configuration the
    different files are used. For example, if you run your tests in dev the testFrameworkSettings.dev.json file is used.

    Note: If you want to make changes you need to do it in each file separately.

    There is a separate more detailed section in the guide describing how to use the configuration files.