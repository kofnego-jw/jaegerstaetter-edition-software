# Software-Requirement-Speficifaction for Jägerstätter-Edition Web Applications

## Abstract

This document contains the necessary and optional features that the server application of the Jägerstätter Edition. It
is written in English in order to ease the final documentation. Since this is an internal document, parts of it may be
kept in German.

Please note that for some parts (e.g.: the detailed definition of the Object Models), the document will refer to the
source code. This document should capture the "major" features for project orientation. For developing applications that
can communicate with the server application please also refer to the source codes.

## Overview

In this chapter, an overview of the application will be given.

### Software Architecture

The software should be maintained as three different web applications that work together.

* The application `registry` is used to edit registry data.
* The application `preview` is used to preview and publish new data.
* The application `edition` is used to publish data and allow users to access data.

All applications should be three-layered. The following layers can be identified:

* Database-Layer: The database layer consists of an EXist-DB instance. All "data" are stored there. However, we will not
  use EXist application structure that is used for e.g. TEIPublisher.
* Webserver-Backend: The middleware consists of a [Spring-Boot](https://spring.io/projects/spring-boot) application.
* SPA-Frontend: The UI frontend is a Single-Page-Application (SPA) using [Angular Framework](https://angular.io/) for
  the application development.

The software is built using [Apache Maven](https://maven.apache.org/) as build-tool and should create the following:

* A JAR-file using Spring-Boot. This file can be executed using "java -jar XXXX.jar" command. A webserver (embedded
  Tomcat Server) should start up and listens to a certain port. The frontend part of the application should already be
  embedded into this file.
    * NB: This file will not start an EXist database. The connection to the database should be set through an
      application.properties file.
* A Docker-Compose suite that should be able to start EXistDB and all three applications

### Workflows

Two workflows should be described here: Workflow of maintaining and deploying the application and workflow of data
publishing.

#### Maintaining and Deploying Applications

The source code of the application is maintained at the GitLab repository of the University Innsbruck. For publication
purpose, we should also consider using a second repository like github.

The applications will be deployed as a docker-compose suite. The deploying and updating processes should be like this:

1. Build the application using mvn package in the `docker` directory
2. Copy the dist folder to the desired place
3. Call `docker-compose up -d` or `docker-compose restart` to start the application or restart the application.

The settings in `docker-compose.yml` should make sure that data is not lost during restart.

#### Data Publishing

The built application should not have any data available. The data should be stored in the EXist database. Editors
should provide data by storing them in a certain schema space (see below).

The workflow should look like this:

1. After creating and updating the xml data, the editors should copy the xml files -- while preserving the directory
   structure -- to a certain place in the EXist-DB using WebDAV.
2. The editors should renew the preview in the test-preview application. The preview will first clone data from the
   edition schema and then ingest data from the ingest schema. Afterwards, a summary of the update (how many files are
   updated) should be presented to the editors.
3. If the editors are content with the preview, they can publish the data. Doing this will copy (and create new
   versions) data to the edition-schema. Afterwards, the edition will deliver new data. Old data will still be available
   as old versions.

### Features implemented for the edition application

Following features should be implemented in the edition application:

1. Delivering viewing abilities for XML/TEI data provided by the editors: The correspondence and notebooks of Franz
   Jägerstätter are edited as XML/TEI data. These data should be presented as HTML view (in diplomatic and normalized
   versions) and XML. Additionally, facsimiles of the original documents will be provided.
2. Delivering viewing abilities for XML/TEI data of the edition, e.g. Editorial Report.
3. Content listing and filtering: Users should be able to list the correspondence and notebooks and filter them
   according to some predefined criteria.
4. Searching functionalities: Users should be able to perform fulltext search on provided data
5. Registry: Indices and registries should be provided to ease the navigation through the data
6. Experimental: A Timeline-Map

### Organisation of the Modules

Aside of the Exist-Database that runs "on its own" (not embedded), the application is developed using different Maven
Modules.

* The base POM contains base settings for all sub modules.
* `document-model` Module contains the base abstraction for dealing with XML repositories.
* `existdb-connector` Module implements document-model and provides a standardized way to communicate with EXistDB
* `configuration` Module contains all configuration for the application (e.g. ExistDB connection url)
* `xmlservice` Module contains the XML services
* `index` Module for indexing functions.
* `xslt-extend` Module contains extension functions for XSLT stylesheets.
* `xslt-stylesheets` Module contains classes for calling XSLT stylesheets.
* `exist-edition-connector` Module contains classes for connecting ExistDB
* `exist-registry-connector` Module contains classes for connecting Registry entries in ExistDB
* `registry-model` Module stores the basic model for registry entries.
* `vocabulary-search` Module contains routines for querying GND, Geonames and WikiData
* `frontend-model` Module contains the models used for communicating with the frontend
* `registry-webapp` Module contains the modules necessary for the web application.
* `search` Module contains search functionalities
* `biography` Module contains functionalities fopr biographies
* `bible-helper` Module contains bible helper functions
* `bible-registry` Module contains bible registry functions
* `photodocument` Module contains functionalities for converting photo documents
* `edition-admin` Module contains functions for administration edition data

Further modules will be documented as the development goes on.

## Database

For Jägestätter Edition we will use an EXistDB as our main database. We might consider using MySQL as cache for
transformed data. At the moment of writing, we do not consider this as necessary, though.

All data should be kept in the "schema" `ffji`. (Franz und Franziska Jägerstätter Institut)

Five distinct spaces should be used: `common`, `edition`, `preview`, `ingest` and `test`.

NB: There is no "schema space" in ExistDB. As an XML store, EXistDB stores data in a data structure similar to file
systems. (We use "folder" and "file" the datasets.)What is meant therefore is the first folder that is available for
users. Basically the folder that is a direct child of `/db/ffji/`.

### Schema-Space `common`

The common space should be used for storing common files shared across different applications. The following data are
stored here:

* `/common/xslt/`: XSLT-Stylesheets, only if we want to amend as quickly as possible.
* `/common/facsimiles/`: Facsimiles for the edition.
* `/common/photodocument/`: Data for photographies and documents

### Schema-Space `edition`

The schema space edition is reserved for publicly available edition data. Suggestion for subfolders:

* `/edition/report/`: This folder is reserved for start page and commantary section of the edition:
    * `.../start.xml`: The start page content in XML/TEI
    * `.../digitale_edition/01_projekt.xml`: The content for "Digitale Edition / Projekt Jägerstätter digital"
    * `.../digitale_edition/02_editionsrichtlinien.xml`: The content for "Digitale Edition / Editionsrichtlinien"
    * And so on. Please note that the XML files within `commentary/` folder will all be served. Using their title as
      menu item, taken their filename in alphabetic order.
* `/edition/data/`: This folder is used for XML data for letters etc. Please do not edit data here.
* `/edition/registry/`: This folder is reserved for registry entries.
* `/edition/biography/`: This folder is reserved for biography entries.

### Schema-Space `preview`

The schema space `preview` has the same structure as edition. However, the data is served through the test site of the
edition.

### Schema-Spoace `ingest`

The schema space `ingest` is used for ingesting new data (or new versions of data). It has the same structure
as `edition`. However, as soon as data is changed here, the data will be propagated to preview. Upon satisfaction, the
preview version will be propagated to edition.

### Schema-Space `test`

This schema space is reserved for unit and integration tests. This folder will be regularly erased. Please do not store
any data here.

## Backend: Connection to Data-Storage

The backend should be able to do the following:

* Fetch data from the database
* Store data into the database

### Retrieving data

The data stored in the database should be fetched. The following data are stored in the database and should be retrieved
when needed:

* TEI/XML data: The transcriptions and the commentaries of the primary sources.
* Image data: Facsimiles of the primary sources -- when available -- are stored as PNG or JPEG images in the database.
* Data on registry entries: The registry entries are also stored as XML file in the database
* Introductory notes, biographies. These data are also stored in the database.

Frontend files (e.g. HTML- and JS-files) are not stored in the database. Instead, they are embedded in the JAR files
that are built in the build process.

### Storing data

The backend should be able to update the data. See the workflow of data publishing for more information.

There will be no method for file deletion. If a file should be deleted, this should be done directly through WebDAV or
WebUI of EXistDB.

## Backend-Endpoints

The backend should provide different endpoints for different purposes:

* Endpoints for the presentation of the edition: These endpoints should all be REST-like. The response should be
  delivered -- as much as possible -- as JSON.
    * In the source code of the frontend, all REST endpoints for the presentation of the edition are enlisted in the
      ApiService.
* Endpoints for OAI-MPH: To be able to deliver the data in "bulk" mode for the archiving system of KU-Linz, the
  application should implement the OAI-PMH.

### Endpoints for the presentation of the edition

Please also refer to the controller source code of the projects.

### Endpoints for Search functions

Please refer to the search section of this document.

## Searching and Filtering functions

While the detailed possibilities for searching and filtering will be worked out in due time, the following design
principles for searching and filtering functionalities should be kept as good as possible:

* As the result set of a search -- even when a search returns all documents available -- can be handled in a modern web
  browser, paging and ordering of results should be performed in the frontend.
* All searching and filtering algorithms should be performed in the backend.

### Query possibilities

The backend should be able to handle following queries:

* Search for all
* Search within all fields
* Search in specific fields
* Filter by date range
* Filter by other categories

Special forms are needed for the search and filter functionalities. More on this topic in the according section in this
document.

### Software Contract, Backend Side

The software contract for the backend is written in the module `fronend-model`. In this module, all messages, DTOs and
endpoints are defined. Please note that the for each REST

## Frontend

The frontend is designed by KENON e. U. and is coded by the project team.

The frontend is a user interface that is able to interact with the backend of the application and by doing so present
both data and functionalities of the edition.

### Principles of Development

This chapter is used for documenting how the frontend for Jägerstätter Edition should be developed.

Frontend should be "dependent" on backend, i.e. frontend should implement the functionalities provided in
the `frontend-model` module. Furthermore, frontend should not generate data, i.e. frontend should only generate "
derived" functionalities (e.g. create url from a given id), but should do this as sparsely as possible.

The frontend will be written using general conventions in Angular. We will use TypeScript.

#### Frameworks

**Angular**

For development, we will stick to Angular v.13.3.11 to ensure compatibility across dev. machines. After the development
we should consider upgrade to the newer versions.

We will use the standard browser compatibility settings.

**Bootstrap**

We will use Bootstrap v5.1.3. However, we will not use the JS provided by Bootstrap.

Bootstrap should be used only for CSS.

**NGX Bootstrap**

The components provided by bootstrap should be taken from NGX Bootstrap, v.8.0.0.

**NGX Highlight.js**

The code view (XML codes) should be viewed through Highlight.JS (using the github style). We will use ngx-highlightjs (
v4.1.4) for this job.

**Font Awesome and Fort Awesome**

Icons should be taken from Font Awesome or Fort Awesome.

## Frontend-Backend-Interface
