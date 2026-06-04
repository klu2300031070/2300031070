const LOG_API_URL = "http://4.224.186.213/evaluation-service/logs";

const ACCESS_TOKEN=import.meta.env.REACT_APP_API_KEY;
export async function Log(level, pkg, message) {
  try {
    const response = await fetch(LOG_API_URL, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${ACCESS_TOKEN}`,
      },
      body: JSON.stringify({
        stack: "frontend",
        level,
        package: pkg,
        message,
      }),
    });

    console.log("Status:", response.status)
     const data= await response.json();
    console.log("Response Data:",data);

    return data;
  } catch (error) {
    console.error("Log submission failed:",error);
    return {
      error: error.message,
    };
  }
}