import { useEffect, useState } from "react";
import { Log } from "./Log";

function App() {
  const [logResponse, setLogResponse] = useState(null);

  useEffect(() => {
  const createLog = async () => {
    const response = await Log(
      "info",
      "component",
      "Application started successfully"
    );

    console.log("App received:", response);

    setLogResponse(response);
  };

  createLog();
}, []);

  return (
    <div>
      <h1>LOGGING MIDDLEWARE</h1>

      {logResponse && (
        <div>
          <p>Log ID: {logResponse.logID}</p>
          <p>Message: {logResponse.message}</p>
        </div>
      )}
    </div>
  );
}

export default App;