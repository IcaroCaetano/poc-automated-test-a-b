# poc_automated_test_a_b

POC for automated A/B testing using Spring Boot (Java 23), PostgreSQL, and Swagger/OpenAPI. The service compares two algorithmic variants for the classic Dungeon Game problem and records results for later analysis.

## ✨ What this project does

- Exposes a REST API to compute the minimum initial health needed to rescue the princess in a dungeon grid.

- Runs the computation using A/B testing:

  - Variant A → Bottom-up DP

  - Variant B → Top-down DP (memoization)

- Chooses the variant either explicitly (query param) or randomly according to a configurable split.

- Saves each run to PostgreSQL with execution time (µs) for analysis.

- Provides a summary endpoint and Swagger UI.

## 🧠 How A/B testing works here (explicit)

Where it happens: the A/B decision and execution happen inside ExperimentService.run(...).

Step-by-step:

1. Two algorithmic variants

  - Variant A → calls **dungeonService.minHealthBottomUp(int[][] dungeon)** (bottom-up DP).

  - Variant B → calls dungeonService.minHealthTopDown(int[][] dungeon) (top-down recursion with memo).

2. Choosing the variant

  - If the client sends ?variant=A or ?variant=B in the request, that variant is forced and will be executed.

  - If no variant is provided, the service randomly assigns a variant according to the configured split (ab.split.a / env AB_SPLIT_A) using the logic:

```
Variant selected = (forced != null) ? forced : (rng.nextInt(100) < splitA ? Variant.A : Variant.B);
```

3. Execute and measure

  - The chosen algorithm runs and computes the minimum initial health.

  - Execution time is measured using Stopwatch in microseconds.

4. Persist result

  - The application saves an ExperimentRun record containing: variant, rows, cols, result, micros, clientIp.

5. Summary / analysis

  - Use the /api/v1/experiments/summary endpoint to retrieve aggregated results: total runs, runs per variant, and average execution time.

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
