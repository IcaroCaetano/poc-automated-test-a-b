# A/B Test Project

This project demonstrates the implementation of A/B testing to evaluate different variants of a given feature and measure their impact on system performance and user behavior.

## Project Overview

The application randomly assigns requests to one of two variants:
- **Variant A**
- **Variant B**

Each variant represents a different path, algorithm, or logic in the system. By running both variants simultaneously with real requests, the project enables comparative analysis of performance, reliability, and outcomes.

## Key Features

- **A/B Variant Selection**: Requests are automatically split between Variant A and Variant B.
- **Data Collection**: Each request is tracked to determine the assigned variant and response outcome.
- **Performance Metrics**: Results are measured to evaluate which variant performs better under real-world conditions.

## Example Usage

### Request Body

```json
{
  "dungeon": [
    [0]
  ]
}
```

### Enum Definition

```java
/**
 * Represents the two possible variants for A/B testing.
 */
public enum Variant {
    /**
     * Variant A - baseline version of the test.
     */
    A,

    /**
     * Variant B - alternative version of the test.
     */
    B
}
```

## How A/B Testing Works in This Project

1. **Assignment**: Each incoming request is randomly assigned to Variant A or Variant B.
2. **Execution**: The assigned variant's logic is executed.
3. **Tracking**: The system logs which variant was used and its response.
4. **Analysis**: Collected data is analyzed to compare success rates, performance, or other KPIs.

## Benefits of A/B Testing

- Provides **data-driven decisions** instead of assumptions.
- Allows for **controlled experiments** with minimal risk.
- Improves **system performance and user satisfaction** by validating changes before full rollout.

## Getting Started

1. Clone this repository.
2. Build and run the application.
3. Send requests with the provided schema.
4. Analyze logs and results to compare Variant A and Variant B.

## License

This project is licensed under the MIT License.
