# Safe Serve

```{=html}
<p align="center">
```
`<strong>`{=html}A JavaFX-based disaster management and relief
coordination application`</strong>`{=html} `<br />`{=html} Bringing
citizens, volunteers, NGOs, and administrators together to coordinate
disaster response.
```{=html}
</p>
```
```{=html}
<p align="center">
```
`<img src="superx_project/src/main/resources/logo.png" alt="Safe Serve logo" width="150" />`{=html}
```{=html}
</p>
```
```{=html}
<p align="center">
```
`<img src="https://img.shields.io/badge/Java-17-orange?logo=openjdk" alt="Java 17" />`{=html}
`<img src="https://img.shields.io/badge/JavaFX-Desktop%20UI-2f6f9f" alt="JavaFX" />`{=html}
`<img src="https://img.shields.io/badge/Backend-Firebase-ffca28?logo=firebase" alt="Firebase" />`{=html}
`<img src="https://img.shields.io/badge/Build-Maven-C71A36?logo=apachemaven" alt="Maven" />`{=html}
```{=html}
</p>
```
## Overview

**Safe Serve** is a desktop application designed to support disaster
response and relief operations. It provides a shared platform where
people can report incidents, request assistance, coordinate volunteers,
and communicate important updates.

The application has separate workflows for different user roles,
including **Citizens, Volunteers, NGOs, and Government Administrators**.
Its JavaFX interface connects to Firebase services for authentication
and data management.

> Safe Serve is an academic project/prototype. It is not an emergency
> dispatch service. In a real emergency, contact the relevant local
> emergency services rather than relying on this application.

## Features

### Citizen

-   Create disaster reports with incident type, location, description,
    and an optional image.
-   Submit requests for assistance, including medical help, food and
    water, shelter, and rescue.
-   View submitted requests.
-   Browse emergency information, safety resources, and relief-resource
    information.
-   View safe-zone information and a disaster map.
-   Access the donation interface.

### Volunteer

-   Access a dedicated volunteer dashboard.
-   View assigned relief tasks and update task status.
-   Log hours contributed to relief activities.
-   View volunteer profile information.

### NGO

-   Access an NGO dashboard.
-   View relevant reports and volunteer information.
-   Participate in the relief coordination workflow.

### Government / Administrator

-   Access an administrative dashboard.
-   Review disaster reports and requests.
-   Manage volunteers and partner NGOs.
-   Verify NGO entries.
-   Create and broadcast priority-based alerts.
-   View the analytics interface.

### Shared application capabilities

-   Firebase Authentication for account registration and sign-in.
-   Cloud Firestore integration for application records.
-   Firebase Cloud Storage integration for uploaded files.
-   News feed retrieval through NewsAPI.
-   JavaFX desktop screens for dashboards, forms, maps, and resource
    information.

*Some screens currently contain sample or locally defined information.
Features such as payments, live operational data, and real-world
emergency dispatch should not be assumed to be fully integrated.*

## Technology Stack

  Area                  Technology
  --------------------- -----------------------------------------------------
  Language              Java
  Desktop UI            JavaFX
  Build configuration   Maven
  Authentication        Firebase Authentication (Identity Toolkit REST API)
  Database              Cloud Firestore
  File storage          Google Cloud Storage / Firebase Storage
  JSON handling         `org.json`
  HTTP client           OkHttp and Java networking APIs
  News                  NewsAPI

## Project Structure

``` text
Safe-Serve/
├── README.md
└── superx_project/
    ├── pom.xml
    └── src/
        └── main/
            ├── java/
            │   └── com/thinkspark/
            │       ├── Controller/    # Login, registration and account controllers
            │       ├── Model/         # Domain models and Firebase configuration
            │       ├── configuration/ # Application configuration
            │       ├── dao/           # Firestore and storage data-access classes
            │       └── view/          # JavaFX screens and dashboards
            └── resources/
                ├── Assets/
                ├── logo.png
                └── map.html
```

### Architectural notes

The code is organized around three main responsibilities:

-   **Views (`view/`)** -- JavaFX screens and user interactions.
-   **Controllers (`Controller/`)** -- authentication and
    account-related workflows.
-   **Models and data access (`Model/`, `dao/`)** -- application data
    structures and communication with Firebase services.

The DAO layer includes classes for disaster reports, alerts, tasks,
volunteers, NGOs, news, and file storage.

## Getting Started

### Prerequisites

-   JDK 17 or a compatible JDK.
-   Maven.
-   JavaFX SDK compatible with your JDK and operating system.
-   A Firebase project with the required services configured.
-   A NewsAPI key if you want the news feed to work.

### 1. Clone the repository

``` bash
git clone https://github.com/atharvaguthe/Safe-Serve.git
cd Safe-Serve
```

### 2. Configure Firebase

1.  Create or select a Firebase project.
2.  Enable the Firebase Authentication providers used by the
    application.
3.  Create a Cloud Firestore database.
4.  Configure Firebase Storage if you intend to use file uploads.
5.  Create a service-account key with only the permissions the
    application needs.
6.  Configure the application to locate the service-account credentials.

The current Firebase configuration code expects a service-account JSON
file at:

``` text
superx_project/src/main/resources/safe-serve-private.json
```

**Do not commit this file.** It contains private credentials. Keep it
outside version control, add it to `.gitignore`, and preferably update
the application to load credentials from a protected external path or
environment-based configuration. Never distribute a service-account key
inside a desktop application or a public repository.

The Firebase Authentication API key and NewsAPI key are also currently
present in source code. Move configurable keys out of source control and
apply the appropriate restrictions/quotas before sharing or deploying
the application.

### 3. Configure JavaFX and Maven

The project uses JavaFX classes, but the current `pom.xml` does not
declare the JavaFX dependencies or a JavaFX Maven run configuration.
Configure the JavaFX SDK and update the Maven build for your platform
before building.

For a local JavaFX SDK installation, the application is launched
through:

``` java
com.thinkspark.Main
```

The entry point calls `Application.launch(StartupScreen.class, args)`.

### 4. Run the application

Once JavaFX, Maven dependencies, and Firebase credentials are
configured, run the `com.thinkspark.Main` class from your IDE or your
configured Java launch command.

## Configuration and Security Notes

Before using this project beyond a local demonstration:

-   Keep Firebase service-account JSON files, private keys, passwords,
    and tokens out of Git.
-   Rotate any credential that has ever been committed or shared
    publicly.
-   Move API keys from source code into external configuration.
-   Apply least-privilege IAM permissions and secure Firestore and
    Storage rules.
-   Do not treat a client-side role selector as authorization; enforce
    permissions in Firebase security rules and trusted backend code.
-   Review file-upload validation, access controls, and signed-URL
    expiration.
-   Replace sample dashboard values and static safe-zone/resource
    information with verified, maintained data.
-   Add error handling and user-facing feedback for network and Firebase
    failures.

## Current Scope

Safe Serve demonstrates a role-based desktop workflow for disaster
reporting and relief coordination. The repository includes JavaFX
interfaces and Firebase data-access code. However, not every screen
represents a production-ready or live service: some content is sample
data, and the repository does not establish a verified
emergency-response deployment.

## Contributing

Contributions and suggestions are welcome.

1.  Fork the repository.
2.  Create a feature branch.
3.  Make your changes and test them locally.
4.  Open a pull request with a clear description of the change.

## Author

**Atharva Guthe**

-   GitHub: [@atharvaguthe](https://github.com/atharvaguthe)
-   Project: [Safe-Serve](https://github.com/atharvaguthe/Safe-Serve)

------------------------------------------------------------------------

```{=html}
<p align="center">
```
Built to explore how software can support disaster response and
community relief coordination.
```{=html}
</p>
```
