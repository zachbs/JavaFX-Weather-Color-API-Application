JavaFX Weather & Color API Application

This project is a JavaFX desktop application built for a CS1302 (Software Development) course at the University of Georgia. The application fetches data from multiple external APIs to display information to the user and dynamically updates the UI.

Features

Fetches and displays weather data from an external API.

Uses the weather condition (e.g., "Rain", "Clear") from the weather API to generate a corresponding hex color value.

Calls a second API (php-noise.com) with this hex value to dynamically generate and display a matching background image.

Parses JSON responses from APIs using the Gson library.

Demonstrates robust error handling for failed API requests.

Uses Java's modern HttpClient for asynchronous, non-blocking network requests.

How to Run

This is a Maven project. You must have Java 17+ and Apache Maven installed.

Clone the repository:

git clone [your-new-github-repo-url]
cd [your-project-folder]



Compile the project:

mvn clean compile



Run the application:

mvn exec:java



Technologies Used

Java 17

JavaFX (for the graphical user interface)

Apache Maven (for project build and dependency management)

Gson (for parsing JSON data)

Java HttpClient (for making API requests)