# Safe Serve

**A JavaFX-based Disaster Management and Relief Coordination System**

Safe Serve is a desktop application designed to support disaster
response and relief coordination. It brings citizens, volunteers, NGOs,
and administrators together through role-based workflows for reporting
incidents, requesting assistance, and coordinating relief activities.

![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk)
![JavaFX](https://img.shields.io/badge/UI-JavaFX-blue)
![Firebase](https://img.shields.io/badge/Backend-Firebase-ffca28?logo=firebase)
![Maven](https://img.shields.io/badge/Build-Maven-C71A36?logo=apachemaven)

------------------------------------------------------------------------

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Firebase Configuration](#firebase-configuration)
- [Security Notes](#security-notes)
- [Project Scope](#project-scope)
- [Author](#author)

## Overview

Disasters require timely communication and coordination between affected
people and relief providers. Safe Serve aims to provide a common desktop
platform for these activities.

The application contains separate interfaces for four user roles:

- **Citizens** – report disasters and request help.
- **Volunteers** – participate in relief tasks and track their
  contributions.
- **NGOs** – access information relevant to relief coordination.
- **Government / Administrators** – review reports, manage resources and
  users, and publish alerts.

The application uses JavaFX for its desktop interface and Firebase
services for authentication and cloud data operations.

> **Disclaimer:** Safe Serve is an academic project/prototype, not a
> live emergency-response service. It must not be relied upon for
> emergency dispatch or life-critical decisions.

## Features

### Citizen Portal

- Submit disaster reports with incident details, location, description,
  and an optional image.
- Request assistance such as medical help, food and water, shelter, or
  rescue.
- View submitted requests.
- Access emergency information and safety resources.
- View safe-zone information and the disaster map interface.
- Access the donation interface.

### Volunteer Portal

- Access a dedicated volunteer dashboard.
- View assigned relief tasks and update task status.
- Record hours contributed to relief activities.
- View volunteer profile information.

### NGO Portal

- Access an NGO dashboard.
- View information relevant to reports and volunteers.
- Participate in the relief coordination workflow.

### Government / Administrator Portal

- Access an administrative dashboard.
- Review disaster reports and assistance requests.
- Manage volunteer and NGO information.
- Verify NGO entries.
- Create and publish priority-based alerts.
- Access the analytics interface.

### Shared Capabilities

- User authentication through Firebase Authentication.
- Cloud data operations using Cloud Firestore.
- File and image storage integration.
- News feed integration through NewsAPI.
- JavaFX screens for dashboards, forms, maps, and resource information.

*Feature availability depends on the current implementation and
configuration. Some screens may use sample or locally defined
information.*

## Technology Stack

| Component              | Technology                              |
|------------------------|-----------------------------------------|
| Programming language   | Java                                    |
| Desktop application UI | JavaFX                                  |
| Build tool             | Maven                                   |
| Authentication         | Firebase Authentication                 |
| Database               | Cloud Firestore                         |
| File storage           | Firebase Storage / Google Cloud Storage |
| JSON handling          | `org.json`                              |
| HTTP communication     | OkHttp and Java networking APIs         |
| News integration       | NewsAPI                                 |

## Project Structure

``` text
Safe-Serve/
└── superx_project/
    ├── pom.xml
    └── src/
        └── main/
            ├── java/
            │   └── com/thinkspark/
            │       ├── Controller/
            │       ├── Model/
            │       ├── configuration/
            │       ├── dao/
            │       └── view/
            └── resources/
                ├── Assets/
                ├── logo.png
                └── map.html
```

### Code Organization

| Directory        | Responsibility                                           |
|------------------|----------------------------------------------------------|
| `Controller/`    | Login, registration, and account-related controllers     |
| `Model/`         | Application models and Firebase-related configuration    |
| `configuration/` | Application configuration                                |
| `dao/`           | Data-access classes for Firebase and application records |
| `view/`          | JavaFX screens and dashboards                            |
| `resources/`     | Images, assets, and other application resources          |

## Getting Started

### Prerequisites

Before running the project, make sure you have:

- JDK 17 or a compatible JDK.
- Maven installed.
- JavaFX SDK compatible with your JDK and operating system.
- A Firebase project configured for the services used by the
  application.
- A NewsAPI key if you want to use the news feed.

### 1. Clone the Repository

``` bash
git clone https://github.com/atharvaguthe/Safe-Serve.git
cd Safe-Serve
```

### 2. Open the Maven Project

Open the `superx_project` directory in your Java IDE, or navigate into
it:

``` bash
cd superx_project
```

Make sure Maven has downloaded the required dependencies.

### 3. Configure Firebase

Create a Firebase project and configure the services used by the
application:

1.  Enable the required Firebase Authentication provider(s).
2.  Create a Cloud Firestore database.
3.  Configure Firebase Storage if you need file uploads.
4.  Configure the credentials and permissions required by the
    application.

The current configuration expects a service-account JSON file at:

``` text
src/main/resources/safe-serve-private.json
```

Do not commit this file or distribute it publicly. See [Security
Notes](#security-notes).

### 4. Run the Application

The application entry point is:

``` java
com.thinkspark.Main
```

Configure JavaFX in your IDE or Maven launch setup, then run the main
class after completing the Firebase configuration.

**Build note:** Verify the JavaFX dependencies and launch configuration
in `pom.xml` for your environment. A compatible JavaFX SDK may need to
be configured separately.

## Firebase Configuration

Safe Serve uses Firebase services for application authentication and
data operations.

Depending on the feature being used, the Firebase project needs:

- Firebase Authentication
- Cloud Firestore
- Firebase Storage

Make sure the Firebase project configuration, database rules, and
storage permissions are set up before testing the application.

## Security Notes

Before sharing or deploying the project:

- Never commit service-account JSON files, private keys, passwords, or
  tokens.
- Rotate credentials if they have ever been committed to a public
  repository.
- Move API keys from source code into external configuration.
- Apply least-privilege permissions to Firebase and Google Cloud
  resources.
- Enforce authorization through Firestore and Storage security rules,
  not only through the desktop UI.
- Validate uploaded files and restrict access to private user data.
- Do not use sample dashboard values or static safe-zone information as
  verified live emergency data.

## Project Scope

Safe Serve demonstrates a role-based desktop workflow for disaster
reporting and relief coordination. It brings together JavaFX user
interfaces and Firebase-backed data-access functionality.

It is an academic prototype. A production emergency-management platform
would additionally require verified real-time data, robust
authorization, operational monitoring, reliable deployment
infrastructure, and integration with responsible emergency-response
organizations.

## Author

**Atharva Guthe**

- GitHub: [@atharvaguthe](https://github.com/atharvaguthe)
- Repository: [Safe-Serve](https://github.com/atharvaguthe/Safe-Serve)

------------------------------------------------------------------------

*Built as an academic project exploring how software can support
disaster response and community relief coordination.*
