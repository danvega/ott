# Spring Security One-Time Token Authentication

Enhance your application's security with passwordless authentication using Spring Security's One-Time Token (OTT) feature. This project demonstrates how to implement magic link authentication, allowing users to securely log in through email-delivered tokens.

## Overview

This Spring Boot application showcases a modern approach to authentication by implementing magic links through Spring Security's OTT functionality. When users attempt to log in, they receive a secure one-time token via email that grants them temporary access to the application.

## Project Requirements

- Java 17 or higher
- Spring Boot 3.x
- Maven or Gradle build tool
- SendGrid account for email delivery

## Dependencies

The project relies on the following key dependencies:

- Spring Boot Starter Web
- Spring Boot Starter Security
- Spring Boot Starter Test
- SendGrid Java Library
- JTE (Java Template Engine)

## Getting Started

1. Set up your SendGrid API key:
   ```properties
   sendgrid.api-key=${SENDGRID_API_KEY}
   ```

2. Configure email settings in `EmailService.java`:
   ```java
   Email from = new Email("your-sender@email.com");
   ```

3. Update user configuration in `SecurityConfig.java` as needed:
   ```java
   var user = User.withUsername("youruser")
       .password("{noop}password")
       .build();
   ```

## How It Works

### Authentication Flow

1. User requests access to a protected resource
2. System generates a one-time token
3. Token is sent via email using SendGrid
4. User clicks the magic link containing the token
5. Spring Security validates the token and grants access

### Key Components

#### Security Configuration

The `SecurityConfig` class sets up the security filter chain and configures authentication:

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/ott/sent").permitAll()
                    .requestMatchers("/login/ott").permitAll()
                    .anyRequest().authenticated()
            )
            .formLogin(Customizer.withDefaults())
            .oneTimeTokenLogin(Customizer.withDefaults())
            .build();
}
```

#### OTT Success Handler

The `OttSuccessHandler` manages token generation and email delivery:

```java
@Override
public void handle(HttpServletRequest request, HttpServletResponse response, 
                  OneTimeToken oneTimeToken) throws IOException, ServletException {
    UriComponentsBuilder builder = UriComponentsBuilder
            .fromHttpUrl(UrlUtils.buildFullRequestUrl(request))
            .replacePath(request.getContextPath())
            .replaceQuery(null)
            .fragment(null)
            .path("/login/ott")
            .queryParam("token", oneTimeToken.getTokenValue());

    String magicLink = builder.toUriString();
    // Send magic link via email
}
```

## Frontend Templates

The application includes two JTE templates:

- `index.jte`: Main application page (protected)
- `sent.jte`: Confirmation page for token delivery

## Testing

Run the included tests using your preferred build tool:

```bash
./mvnw test   # Maven
./gradlew test # Gradle
```

## Security Considerations

- Tokens are single-use and time-limited
- All endpoints except `/ott/sent` and `/login/ott` require authentication
- Passwords are stored using Spring Security's password encoding
- Email delivery is handled securely through SendGrid's API