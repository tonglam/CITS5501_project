# Specific Java version
FROM openjdk:11.0.12-jre-slim

# Set the working directory
WORKDIR /app

# Copy all necessary class files and dependencies
COPY target/classes /app

# Set the classpath to include the current directory
ENV CLASSPATH=/app

# Run the REPL class
CMD ["java", "com.tong.cits5501.parser.REPL"]