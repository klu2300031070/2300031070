import { useEffect, useState } from "react";

export default function Vehicle_Scheduling() {
  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(true);

  const DEPOTS_API = "http://4.224.186.213/evaluation-service/depots";
  const VEHICLES_API = "http://4.224.186.213/evaluation-service/vehicles";

  const knapsack = (tasks, capacity) => {
    const n = tasks.length;

    const dp = Array.from({ length: n + 1 }, () =>
      Array(capacity + 1).fill(0)
    );

    for (let i = 1; i <= n; i++) {
      const duration = tasks[i - 1].Duration;
      const impact = tasks[i - 1].Impact;

      for (let w = 0; w <= capacity; w++) {
        if (duration <= w) {
          dp[i][w] = Math.max(
            dp[i - 1][w],
            dp[i - 1][w - duration] + impact
          );
        } else {
          dp[i][w] = dp[i - 1][w];
        }
      }
    }

    let w = capacity;
    const selectedTasks = [];

    for (let i = n; i > 0; i--) {
      if (dp[i][w] !== dp[i - 1][w]) {
        selectedTasks.push(tasks[i - 1]);
        w -= tasks[i - 1].Duration;
      }
    }

    return {
      totalImpact: dp[n][capacity],
      totalDuration: selectedTasks.reduce(
        (sum, task) => sum + task.Duration,
        0
      ),
      selectedTasks: selectedTasks.reverse(),
    };
  };

 useEffect(() => {
  const fetchData = async () => {
    try {
      const token =
        import.meta.env.REACT_APP_API_KEY;

      const headers = {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      };

      const [depotsRes, vehiclesRes] = await Promise.all([
        fetch(DEPOTS_API, { headers }),
        fetch(VEHICLES_API, { headers }),
      ]);

      if (!depotsRes.ok || !vehiclesRes.ok) {
        throw new Error("API request failed (check token or endpoint)");
      }

      const depotsData = await depotsRes.json();
      const vehiclesData = await vehiclesRes.json();

      const output = depotsData.depots.map((depot) => {
        const solution = knapsack(
          vehiclesData.vehicles,
          depot.MechanicHours
        );

        return {
          depotId: depot.ID,
          mechanicHours: depot.MechanicHours,
          totalImpact: solution.totalImpact,
          totalDuration: solution.totalDuration,
          selectedTasks: solution.selectedTasks.map(
            (task) => task.TaskID
          ),
        };
      });

      setResults(output);
    } catch (error) {
      console.error("Error fetching data:", error);
    } finally {
      setLoading(false);
    }
  };

  fetchData();
}, []);

  if (loading) return <h2>Loading...</h2>;

  return (
    <div style={{ padding: "20px" }}>
      <h1>Vehicle Maintenance Scheduler</h1>

      {results.map((result) => (
        <div
          key={result.depotId}
          style={{
            border: "1px solid #ccc",
            marginBottom: "20px",
            padding: "15px",
            borderRadius: "8px",
          }}
        >
          <h2>Depot {result.depotId}</h2>

          <p>
            <strong>Mechanic Hours:</strong>{" "}
            {result.mechanicHours}
          </p>

          <p>
            <strong>Total Duration Used:</strong>{" "}
            {result.totalDuration}
          </p>

          <p>
            <strong>Total Impact:</strong>{" "}
            {result.totalImpact}
          </p>

          <h4>Selected Tasks</h4>

          <ul>
            {result.selectedTasks.map((taskId) => (
              <li key={taskId}>{taskId}</li>
            ))}
          </ul>
        </div>
      ))}
    </div>
  );
}