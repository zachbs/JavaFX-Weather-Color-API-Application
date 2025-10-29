# JavaFX Weather & Color API Application  
A JavaFX application that dynamically generates a color background image based on live weather data.

## Live Weather-Based Dynamic UI  
This is a JavaFX desktop application built for a **CS1302 (Software Development)** course at the **University of Georgia**.  
The application demonstrates real-time API consumption and dynamic UI updates by chaining the output of one API (live weather data) into another (color and image generation).

The primary goal of this project was to practice **JavaFX**, **asynchronous API handling**, and **data-driven UI rendering** using modern Java tools.

---

## Tech Stack  
* **Java 17**  
* **JavaFX** (for the graphical user interface)  
* **Apache Maven** (for project build and dependency management)  
* **Gson** (for parsing JSON data)  
* **Java HttpClient** (for asynchronous, non-blocking API requests)

---

## Features  
* Fetches and displays **live weather data** from an external weather API.  
* Converts the weather condition (e.g., `"Rain"`, `"Clear"`, `"Snow"`) into a **hex color code**.  
* Sends the color value to the **php-noise.com API** to dynamically generate and display a **matching background image**.  
* Handles **asynchronous network requests** using Java’s modern `HttpClient`.  
* Parses JSON responses using **Gson**.  
* Implements **robust error handling** for failed API requests or invalid responses.  

---

## Current Status: Fully Functional Prototype  
This application is fully functional and demonstrates all core features: live weather retrieval, chained API logic, and dynamic visual updates.  
It serves as a practical demonstration of software design patterns, modular JavaFX development, and networked application structure.

---

## How to Run  

### 1. Clone the Repository  

This is a Maven project. You must have Java 17+ and Apache Maven installed.

```bash
git clone https://github.com/zachbs/JavaFX-Weather-Color-API-Application.git
```




### 2. Compile the project:

```bash
mvn clean compile
```


### 3. Run the application:

```bash
mvn exec:java
```

## From an IDE

Alternatively, you can import the project as a Maven project into your favorite Java IDE (like VS Code or IntelliJ). The IDE will automatically detect the pom.xml and download the required libraries.

You can then run the application by running the main method in the cs1302.api.ApiDriver class.


Java HttpClient (for making API requests)
