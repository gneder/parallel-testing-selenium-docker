# Parallel Cross-Browser Testing with Selenium and Docker

Selenium + TestNG + Maven test project demonstrating parallel, cross-browser
test execution (Chrome, Firefox, Edge) using a Dockerized Selenium Grid.

The test scenario logs into [saucedemo.com](https://www.saucedemo.com/), a
public demo e-commerce site, and verifies the login flow for both a valid
user and a locked-out user.

## Running locally (headless, no Docker needed)

```
mvn test
```

This runs `testng.xml`, a single-browser suite against a local headless
Chrome instance (via Selenium's built-in Selenium Manager) — this is what
CI runs, so it works with zero setup.

## Running the full cross-browser suite against a Selenium Grid

```
docker-compose up -d
mvn test -DsuiteXmlFile=testng-grid.xml -DgridUrl=http://localhost:4444/wd/hub
docker-compose down
```

This runs `testng-grid.xml`, which executes the same test in parallel across
Chrome, Firefox and Edge nodes on the local grid started by
`docker-compose.yml`.

## Tech stack

- Java 17
- Selenium WebDriver 4
- TestNG
- Maven
- Docker / Selenium Grid
- GitHub Actions CI
