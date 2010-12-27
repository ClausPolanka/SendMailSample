

This is an example of how to use gwtupload library.

It has both ant a maven files so you should be able to run:

mvn clean         # delete temporary stuff
mvn gwt:run       # run development mode
mvn gwt:compile   # compile to javascript
mvn package       # generate a .war package ready to deploy

ant devmode       # To run development mode
ant               # To compile your project for deployment
ant war           # To compile and also bundle into a .war file


Note: the project includes necessary libraries to use ant. 
      If you preffer maven, you can delete build.xml and
      src/main/webapp/WEB-INF/*.jar

-Manuel Carrasco Moñino

