# Specific Java version
FROM openjdk:21-slim

# Set the working directory
WORKDIR /app

# Copy all necessary class files and dependencies
COPY out/production/CITS5501_project/ /app

# Set the classpath to include the current directory
ENV CLASSPATH=/app

# Run the REPL class
CMD ["java", "com.tong.cits5501.parser.REPL"]