import { useEffect, useState, useCallback } from "react";
import "./App.css";

const API_URL = "http://localhost:8080";

function App() {

  // =====================================
  // ROBOT STATES
  // =====================================

  const [robots, setRobots] = useState([]);
  const [loading, setLoading] = useState(true);


  // =====================================
  // DASHBOARD STATISTICS
  // =====================================

  const [dashboardStats, setDashboardStats] = useState({
    totalRobots: 0,
    healthyRobots: 0,
    criticalRobots: 0,
    warningRobots: 0,

    totalTasks: 0,
    completedTasks: 0,
    pendingTasks: 0,
    inProgressTasks: 0,

    totalAlerts: 0,
    criticalAlerts: 0,
    highAlerts: 0,
  });


  // =====================================
  // SERVER STATUS
  // =====================================

  const [serverStatus, setServerStatus] = useState("CHECKING");


  // =====================================
  // ROBOT FORM STATES
  // =====================================

  const [robotCode, setRobotCode] = useState("");
  const [name, setName] = useState("");
  const [model, setModel] = useState("");
  const [status, setStatus] = useState("ACTIVE");
  const [batteryLevel, setBatteryLevel] = useState("");
  const [location, setLocation] = useState("");
  const [healthStatus, setHealthStatus] = useState("HEALTHY");


  // =====================================
  // SEARCH & FILTER STATES
  // =====================================

  const [searchName, setSearchName] = useState("");
  const [searchCode, setSearchCode] = useState("");
  const [filterStatus, setFilterStatus] = useState("");
  const [filterHealthStatus, setFilterHealthStatus] = useState("");


  // =====================================
  // EDIT ROBOT STATE
  // =====================================

  const [editingRobot, setEditingRobot] = useState(null);


  // =====================================
  // ALERT STATES
  // =====================================

  const [alerts, setAlerts] = useState([]);
  const [alertLoading, setAlertLoading] = useState(false);
  const [showAlerts, setShowAlerts] = useState(false);


  // =====================================
  // TASK STATES
  // =====================================

  const [tasks, setTasks] = useState([]);
  const [taskLoading, setTaskLoading] = useState(false);

  const [taskRobotId, setTaskRobotId] = useState("");
  const [taskName, setTaskName] = useState("");
  const [taskDescription, setTaskDescription] = useState("");


  // =====================================
  // FETCH DASHBOARD STATISTICS
  // =====================================

  const fetchDashboardStats = useCallback(async () => {

    try {

      const response = await fetch(`${API_URL}/dashboard`);

      if (!response.ok) {
        throw new Error("Failed to fetch dashboard statistics");
      }

      const data = await response.json();

      setDashboardStats(data);
      setServerStatus("ONLINE");

    } catch (error) {

      console.error("Dashboard Stats Error:", error);
      setServerStatus("OFFLINE");

    }

  }, []);


  // =====================================
  // FETCH ALL ROBOTS
  // =====================================

  const fetchRobots = useCallback(async () => {

    try {

      setLoading(true);

      const response = await fetch(`${API_URL}/robots`);

      if (!response.ok) {
        throw new Error("Failed to fetch robots");
      }

      const data = await response.json();

      setRobots(
        Array.isArray(data)
          ? data
          : []
      );

      setServerStatus("ONLINE");

    } catch (error) {

      console.error("Robot Fetch Error:", error);

      setRobots([]);
      setServerStatus("OFFLINE");

    } finally {

      setLoading(false);

    }

  }, []);


  // =====================================
  // FETCH ALERTS
  // =====================================

  const fetchAlerts = useCallback(async () => {

    try {

      setAlertLoading(true);

      const response = await fetch(`${API_URL}/alerts`);

      if (!response.ok) {
        throw new Error("Failed to fetch alerts");
      }

      const data = await response.json();

      setAlerts(
        Array.isArray(data)
          ? data
          : []
      );

    } catch (error) {

      console.error("Alert Fetch Error:", error);
      setAlerts([]);

    } finally {

      setAlertLoading(false);

    }

  }, []);


  // =====================================
  // FETCH TASKS
  // =====================================

  const fetchTasks = useCallback(async () => {

    try {

      setTaskLoading(true);

      const response = await fetch(`${API_URL}/tasks`);

      if (!response.ok) {
        throw new Error("Failed to fetch tasks");
      }

      const data = await response.json();

      setTasks(
        Array.isArray(data)
          ? data
          : []
      );

    } catch (error) {

      console.error("Task Fetch Error:", error);
      setTasks([]);

    } finally {

      setTaskLoading(false);

    }

  }, []);


  // =====================================
  // LOAD ALL DASHBOARD DATA
  // =====================================

  const loadDashboardData = useCallback(async () => {

    await Promise.all([
      fetchRobots(),
      fetchDashboardStats(),
      fetchTasks(),
    ]);

  }, [
    fetchRobots,
    fetchDashboardStats,
    fetchTasks,
  ]);


  // =====================================
  // INITIAL LOAD + AUTO REFRESH
  // =====================================

  useEffect(() => {

    loadDashboardData();

    const interval = setInterval(() => {

      loadDashboardData();

      if (showAlerts) {
        fetchAlerts();
      }

    }, 5000);

    return () => {
      clearInterval(interval);
    };

  }, [
    loadDashboardData,
    fetchAlerts,
    showAlerts,
  ]);


  // =====================================
  // TOGGLE ALERTS
  // =====================================

  const toggleAlerts = async () => {

    if (!showAlerts) {
      await fetchAlerts();
    }

    setShowAlerts(
      previousValue => !previousValue
    );

  };


  // =====================================
  // CLEAR ROBOT FORM
  // =====================================

  const clearForm = () => {

    setRobotCode("");
    setName("");
    setModel("");
    setStatus("ACTIVE");
    setBatteryLevel("");
    setLocation("");
    setHealthStatus("HEALTHY");
    setEditingRobot(null);

  };


  // =====================================
  // CLEAR TASK FORM
  // =====================================

  const clearTaskForm = () => {

    setTaskRobotId("");
    setTaskName("");
    setTaskDescription("");

  };


  // =====================================
  // SEARCH ROBOT BY NAME
  // =====================================

  const searchRobotByName = async () => {

    if (searchName.trim() === "") {

      await fetchRobots();
      return;

    }

    try {

      setLoading(true);

      const response = await fetch(
        `${API_URL}/robots/search?name=${encodeURIComponent(
          searchName.trim()
        )}`
      );

      if (!response.ok) {
        throw new Error("Robot not found");
      }

      const data = await response.json();

      setRobots(
        Array.isArray(data)
          ? data
          : [data]
      );

    } catch (error) {

      console.error("Name Search Error:", error);

      setRobots([]);

      alert("Robot not found!");

    } finally {

      setLoading(false);

    }

  };


  // =====================================
  // SEARCH ROBOT BY CODE
  // =====================================

  const searchRobotByCode = async () => {

    if (searchCode.trim() === "") {

      alert("Please enter Robot Code");
      return;

    }

    try {

      setLoading(true);

      const response = await fetch(
        `${API_URL}/robots/search/code?robotCode=${encodeURIComponent(
          searchCode.trim()
        )}`
      );

      if (!response.ok) {
        throw new Error("Robot not found");
      }

      const data = await response.json();

      setRobots(
        Array.isArray(data)
          ? data
          : [data]
      );

    } catch (error) {

      console.error("Code Search Error:", error);

      setRobots([]);

      alert("Robot not found!");

    } finally {

      setLoading(false);

    }

  };


  // =====================================
  // FILTER BY ROBOT STATUS
  // =====================================

  const searchByStatus = async () => {

    if (filterStatus === "") {

      await fetchRobots();
      return;

    }

    try {

      setLoading(true);

      const response = await fetch(
        `${API_URL}/robots/status?status=${encodeURIComponent(
          filterStatus
        )}`
      );

      if (!response.ok) {
        throw new Error("No robots found");
      }

      const data = await response.json();

      setRobots(
        Array.isArray(data)
          ? data
          : []
      );

    } catch (error) {

      console.error("Status Filter Error:", error);
      setRobots([]);

    } finally {

      setLoading(false);

    }

  };


  // =====================================
  // FILTER BY HEALTH STATUS
  // =====================================

  const searchByHealthStatus = async () => {

    if (filterHealthStatus === "") {

      await fetchRobots();
      return;

    }

    try {

      setLoading(true);

      const response = await fetch(
        `${API_URL}/robots/health?healthStatus=${encodeURIComponent(
          filterHealthStatus
        )}`
      );

      if (!response.ok) {
        throw new Error("No robots found");
      }

      const data = await response.json();

      setRobots(
        Array.isArray(data)
          ? data
          : []
      );

    } catch (error) {

      console.error("Health Filter Error:", error);
      setRobots([]);

    } finally {

      setLoading(false);

    }

  };


  // =====================================
  // CLEAR SEARCH
  // =====================================

  const clearSearch = async () => {

    setSearchName("");
    setSearchCode("");
    setFilterStatus("");
    setFilterHealthStatus("");

    await fetchRobots();

  };


  // =====================================
  // ADD OR UPDATE ROBOT
  // =====================================

  const addRobot = async (e) => {

    e.preventDefault();

    if (
      robotCode.trim() === "" ||
      name.trim() === "" ||
      model.trim() === "" ||
      batteryLevel === "" ||
      location.trim() === ""
    ) {

      alert("Please fill all fields!");
      return;

    }

    if (
      Number(batteryLevel) < 0 ||
      Number(batteryLevel) > 100
    ) {

      alert("Battery level must be between 0 and 100");
      return;

    }

    const robotData = {

      robotCode: robotCode.trim(),
      name: name.trim(),
      model: model.trim(),
      status,
      batteryLevel: Number(batteryLevel),
      location: location.trim(),
      healthStatus,

    };

    try {

      // =============================
      // UPDATE ROBOT
      // =============================

      if (editingRobot) {

        const response = await fetch(
          `${API_URL}/robots/${editingRobot.id}`,
          {
            method: "PUT",
            headers: {
              "Content-Type": "application/json",
            },
            body: JSON.stringify(robotData),
          }
        );

        if (!response.ok) {
          throw new Error("Failed to update robot");
        }

        await loadDashboardData();
        await fetchAlerts();

        clearForm();

        alert("Robot updated successfully!");

        return;

      }


      // =============================
      // ADD NEW ROBOT
      // =============================

      const response = await fetch(
        `${API_URL}/robots`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(robotData),
        }
      );

      if (!response.ok) {
        throw new Error("Failed to add robot");
      }

      await loadDashboardData();
      await fetchAlerts();

      clearForm();

      alert("Robot added successfully!");

    } catch (error) {

      console.error("Robot Error:", error);

      alert(
        error.message ||
        "Something went wrong!"
      );

    }

  };


  // =====================================
  // EDIT ROBOT
  // =====================================

  const editRobot = (robot) => {

    setEditingRobot(robot);

    setRobotCode(robot.robotCode || "");
    setName(robot.name || "");
    setModel(robot.model || "");
    setStatus(robot.status || "ACTIVE");
    setBatteryLevel(robot.batteryLevel ?? "");
    setLocation(robot.location || "");
    setHealthStatus(robot.healthStatus || "HEALTHY");

    window.scrollTo({
      top: 0,
      behavior: "smooth",
    });

  };


  // =====================================
  // CANCEL EDIT
  // =====================================

  const cancelEdit = () => {
    clearForm();
  };


  // =====================================
  // DELETE ROBOT
  // =====================================

  const deleteRobot = async (id) => {

    const confirmDelete = window.confirm(
      "Are you sure you want to delete this robot?"
    );

    if (!confirmDelete) {
      return;
    }

    try {

      const response = await fetch(
        `${API_URL}/robots/${id}`,
        {
          method: "DELETE",
        }
      );

      if (!response.ok) {
        throw new Error("Failed to delete robot");
      }

      await loadDashboardData();
      await fetchAlerts();

      if (
        editingRobot &&
        editingRobot.id === id
      ) {
        clearForm();
      }

      alert("Robot deleted successfully!");

    } catch (error) {

      console.error(
        "Delete Robot Error:",
        error
      );

      alert(
        error.message ||
        "Failed to delete robot"
      );

    }

  };


  // =====================================
  // CREATE / ASSIGN TASK
  // =====================================

  const assignTask = async (e) => {

    e.preventDefault();

    if (taskRobotId === "") {

      alert("Please select a robot");
      return;

    }

    if (taskName.trim() === "") {

      alert("Please enter task name");
      return;

    }

    if (taskDescription.trim() === "") {

      alert("Please enter task description");
      return;

    }

    const taskData = {

      robotId: Number(taskRobotId),

      taskName: taskName.trim(),

      description: taskDescription.trim(),

      taskStatus: "PENDING",

    };

    try {

      const response = await fetch(
        `${API_URL}/tasks/assign`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(taskData),
        }
      );

      if (!response.ok) {

        let errorMessage =
          "Failed to assign task";

        try {

          const errorData =
            await response.text();

          if (errorData) {
            errorMessage = errorData;
          }

        } catch (error) {
          console.error(error);
        }

        throw new Error(errorMessage);

      }

      await loadDashboardData();

      clearTaskForm();

      alert("Task assigned successfully!");

    } catch (error) {

      console.error(
        "Assign Task Error:",
        error
      );

      alert(
        error.message ||
        "Failed to assign task"
      );

    }

  };


  // =====================================
  // UPDATE TASK STATUS
  // =====================================

  const updateTaskStatus = async (
    id,
    newStatus
  ) => {

    try {

      const response = await fetch(
        `${API_URL}/tasks/${id}/status?status=${encodeURIComponent(
          newStatus
        )}`,
        {
          method: "PUT",
        }
      );

      if (!response.ok) {
        throw new Error(
          "Failed to update task status"
        );
      }

      await loadDashboardData();

    } catch (error) {

      console.error(
        "Task Status Error:",
        error
      );

      alert(
        error.message ||
        "Failed to update task status"
      );

    }

  };


  // =====================================
  // COMPLETE TASK
  // =====================================

  const completeTask = async (id) => {

    try {

      const response = await fetch(
        `${API_URL}/tasks/${id}/complete`,
        {
          method: "PUT",
        }
      );

      if (!response.ok) {
        throw new Error(
          "Failed to complete task"
        );
      }

      await loadDashboardData();

      alert("Task completed successfully!");

    } catch (error) {

      console.error(
        "Complete Task Error:",
        error
      );

      alert(
        error.message ||
        "Failed to complete task"
      );

    }

  };


  // =====================================
  // DELETE TASK
  // =====================================

  const deleteTask = async (id) => {

    const confirmDelete = window.confirm(
      "Are you sure you want to delete this task?"
    );

    if (!confirmDelete) {
      return;
    }

    try {

      const response = await fetch(
        `${API_URL}/tasks/${id}`,
        {
          method: "DELETE",
        }
      );

      if (!response.ok) {
        throw new Error(
          "Failed to delete task"
        );
      }

      await loadDashboardData();

      alert("Task deleted successfully!");

    } catch (error) {

      console.error(
        "Delete Task Error:",
        error
      );

      alert(
        error.message ||
        "Failed to delete task"
      );

    }

  };


  // =====================================
  // MANUAL REFRESH
  // =====================================

  const handleRefresh = async () => {

    await loadDashboardData();

    if (showAlerts) {
      await fetchAlerts();
    }

  };


  // =====================================
  // UI
  // =====================================

  return (

    <div className="app">


      {/* ================= HEADER ================= */}

      <div className="header-section">

        <div>

          <h1>
            🤖 RoboSentinel Dashboard
          </h1>

          <div
            className={`server-status ${serverStatus.toLowerCase()}`}
          >

            <span className="status-dot"></span>

            Server:{" "}
            {serverStatus}

          </div>

        </div>


        <div className="header-actions">

          <button
            className="refresh-button"
            onClick={handleRefresh}
          >
            🔄 Refresh
          </button>


          <button
            className="alert-button"
            onClick={toggleAlerts}
          >

            🚨 Alerts

            {dashboardStats.totalAlerts > 0 && (

              <span className="alert-count">
                {dashboardStats.totalAlerts}
              </span>

            )}

          </button>

        </div>

      </div>


      {/* ================= ROBOT STATISTICS ================= */}

      <h2 className="section-title">
        🤖 Robot Statistics
      </h2>

      <div className="stats-container">

        <div className="card">
          <h3>Total Robots</h3>
          <p>{dashboardStats.totalRobots}</p>
        </div>

        <div className="card">
          <h3>Healthy</h3>
          <p>{dashboardStats.healthyRobots}</p>
        </div>

        <div className="card">
          <h3>Warning</h3>
          <p>{dashboardStats.warningRobots}</p>
        </div>

        <div className="card">
          <h3>Critical</h3>
          <p>{dashboardStats.criticalRobots}</p>
        </div>

      </div>


      {/* ================= TASK STATISTICS ================= */}

      <h2 className="section-title">
        📋 Task Statistics
      </h2>

      <div className="stats-container">

        <div className="card">
          <h3>Total Tasks</h3>
          <p>{dashboardStats.totalTasks}</p>
        </div>

        <div className="card">
          <h3>Completed</h3>
          <p>{dashboardStats.completedTasks}</p>
        </div>

        <div className="card">
          <h3>Pending</h3>
          <p>{dashboardStats.pendingTasks}</p>
        </div>

        <div className="card">
          <h3>In Progress</h3>
          <p>{dashboardStats.inProgressTasks}</p>
        </div>

      </div>


      {/* ================= ALERT STATISTICS ================= */}

      <h2 className="section-title">
        🚨 Alert Statistics
      </h2>

      <div className="stats-container">

        <div className="card">
          <h3>Total Alerts</h3>
          <p>{dashboardStats.totalAlerts}</p>
        </div>

        <div className="card">
          <h3>Critical Alerts</h3>
          <p>{dashboardStats.criticalAlerts}</p>
        </div>

        <div className="card">
          <h3>High Alerts</h3>
          <p>{dashboardStats.highAlerts}</p>
        </div>

      </div>


      {/* ================= ALERT LIST ================= */}

      {showAlerts && (

        <div className="alerts-container">

          <h2>
            🚨 Robot Alerts
          </h2>

          {alertLoading ? (

            <p>
              Loading alerts...
            </p>

          ) : alerts.length === 0 ? (

            <p>
              No alerts found 🎉
            </p>

          ) : (

            <div className="table-container">

              <table>

                <thead>

                  <tr>
                    <th>Alert ID</th>
                    <th>Robot ID</th>
                    <th>Type</th>
                    <th>Message</th>
                    <th>Severity</th>
                  </tr>

                </thead>

                <tbody>

                  {alerts.map(
                    (alertItem) => (

                      <tr
                        key={alertItem.id}
                      >

                        <td>
                          {alertItem.id}
                        </td>

                        <td>
                          {alertItem.robotId ?? "N/A"}
                        </td>

                        <td>
                          {alertItem.alertType ?? "N/A"}
                        </td>

                        <td>
                          {alertItem.message ?? "No message"}
                        </td>

                        <td>

                          <span
                            className={`severity ${
                              alertItem.severity?.toLowerCase() || ""
                            }`}
                          >
                            {alertItem.severity ?? "N/A"}
                          </span>

                        </td>

                      </tr>

                    )
                  )}

                </tbody>

              </table>

            </div>

          )}

        </div>

      )}


      {/* ================= ADD / UPDATE ROBOT ================= */}

      <div className="form-container">

        <h2>

          {editingRobot
            ? "✏️ Update Robot"
            : "➕ Add New Robot"
          }

        </h2>

        <form onSubmit={addRobot}>

          <input
            type="text"
            placeholder="Robot Code"
            value={robotCode}
            onChange={(e) =>
              setRobotCode(e.target.value)
            }
            required
          />

          <input
            type="text"
            placeholder="Robot Name"
            value={name}
            onChange={(e) =>
              setName(e.target.value)
            }
            required
          />

          <input
            type="text"
            placeholder="Robot Model"
            value={model}
            onChange={(e) =>
              setModel(e.target.value)
            }
            required
          />

          <select
            value={status}
            onChange={(e) =>
              setStatus(e.target.value)
            }
          >

            <option value="ACTIVE">
              ACTIVE
            </option>

            <option value="INACTIVE">
              INACTIVE
            </option>

            <option value="IDLE">
              IDLE
            </option>

            <option value="BUSY">
              BUSY
            </option>

          </select>

          <input
            type="number"
            placeholder="Battery Level (0-100)"
            value={batteryLevel}
            onChange={(e) =>
              setBatteryLevel(e.target.value)
            }
            min="0"
            max="100"
            required
          />

          <input
            type="text"
            placeholder="Location"
            value={location}
            onChange={(e) =>
              setLocation(e.target.value)
            }
            required
          />

          <select
            value={healthStatus}
            onChange={(e) =>
              setHealthStatus(e.target.value)
            }
          >

            <option value="HEALTHY">
              HEALTHY
            </option>

            <option value="WARNING">
              WARNING
            </option>

            <option value="CRITICAL">
              CRITICAL
            </option>

          </select>

          <button type="submit">

            {editingRobot
              ? "Update Robot"
              : "Add Robot"
            }

          </button>

          {editingRobot && (

            <button
              type="button"
              className="cancel-btn"
              onClick={cancelEdit}
            >
              Cancel
            </button>

          )}

        </form>

      </div>


      {/* ================= TASK MANAGEMENT ================= */}

      <div className="form-container">

        <h2>
          📋 Task Management
        </h2>

        <form onSubmit={assignTask}>

          <select
            value={taskRobotId}
            onChange={(e) =>
              setTaskRobotId(e.target.value)
            }
            required
          >

            <option value="">
              Select Healthy Robot
            </option>

            {robots
              .filter(
                (robot) =>
                  robot.healthStatus === "HEALTHY"
              )
              .map((robot) => (

                <option
                  key={robot.id}
                  value={robot.id}
                >

                  {robot.robotCode} - {robot.name}

                </option>

              ))}

          </select>


          <input
            type="text"
            placeholder="Task Name"
            value={taskName}
            onChange={(e) =>
              setTaskName(e.target.value)
            }
            required
          />


          <input
            type="text"
            placeholder="Task Description"
            value={taskDescription}
            onChange={(e) =>
              setTaskDescription(e.target.value)
            }
            required
          />


          <button type="submit">
            Assign Task
          </button>


          <button
            type="button"
            className="clear-btn"
            onClick={clearTaskForm}
          >
            Clear Task
          </button>

        </form>

      </div>


      {/* ================= TASK LIST ================= */}

      <h2 className="section-title">
        📋 Task List
      </h2>


      {taskLoading ? (

        <p className="loading-text">
          Loading tasks...
        </p>

      ) : tasks.length === 0 ? (

        <div className="table-container">

          <table>

            <tbody>

              <tr>

                <td>
                  No tasks found.
                </td>

              </tr>

            </tbody>

          </table>

        </div>

      ) : (

        <div className="table-container">

          <table>

            <thead>

              <tr>

                <th>
                  ID
                </th>

                <th>
                  Robot ID
                </th>

                <th>
                  Task Name
                </th>

                <th>
                  Description
                </th>

                <th>
                  Status
                </th>

                <th>
                  Action
                </th>

              </tr>

            </thead>


            <tbody>

              {tasks.map(
                (task) => (

                  <tr
                    key={task.id}
                  >

                    <td>
                      {task.id}
                    </td>

                    <td>
                      {task.robotId}
                    </td>

                    <td>
                      {task.taskName}
                    </td>

                    <td>
                      {task.description}
                    </td>

                    <td>

                      <span
                        className={`status ${
                          task.taskStatus?.toLowerCase() || ""
                        }`}
                      >
                        {task.taskStatus}
                      </span>

                    </td>


                    <td>

                      {task.taskStatus === "PENDING" && (

                        <button
                          type="button"
                          className="edit-btn"
                          onClick={() =>
                            updateTaskStatus(
                              task.id,
                              "IN_PROGRESS"
                            )
                          }
                        >
                          Start
                        </button>

                      )}


                      {task.taskStatus === "IN_PROGRESS" && (

                        <button
                          type="button"
                          className="edit-btn"
                          onClick={() =>
                            completeTask(task.id)
                          }
                        >
                          Complete
                        </button>

                      )}


                      {task.taskStatus === "COMPLETED" && (

                        <span>
                          ✅ Done
                        </span>

                      )}


                      <button
                        type="button"
                        className="delete-btn"
                        onClick={() =>
                          deleteTask(task.id)
                        }
                      >
                        Delete
                      </button>

                    </td>

                  </tr>

                )
              )}

            </tbody>

          </table>

        </div>

      )}


      {/* ================= SEARCH & FILTER ================= */}

      <div className="search-container">

        <h2>
          🔍 Search & Filter Robots
        </h2>

        <div className="search-row">

          <input
            type="text"
            placeholder="Search by Robot Name"
            value={searchName}
            onChange={(e) =>
              setSearchName(e.target.value)
            }
          />

          <button
            type="button"
            onClick={searchRobotByName}
          >
            Search Name
          </button>


          <input
            type="text"
            placeholder="Search by Robot Code"
            value={searchCode}
            onChange={(e) =>
              setSearchCode(e.target.value)
            }
          />

          <button
            type="button"
            onClick={searchRobotByCode}
          >
            Search Code
          </button>

        </div>


        <div className="search-row">

          <select
            value={filterStatus}
            onChange={(e) =>
              setFilterStatus(e.target.value)
            }
          >

            <option value="">
              Select Status
            </option>

            <option value="ACTIVE">
              ACTIVE
            </option>

            <option value="INACTIVE">
              INACTIVE
            </option>

            <option value="IDLE">
              IDLE
            </option>

            <option value="BUSY">
              BUSY
            </option>

          </select>


          <button
            type="button"
            onClick={searchByStatus}
          >
            Filter Status
          </button>


          <select
            value={filterHealthStatus}
            onChange={(e) =>
              setFilterHealthStatus(e.target.value)
            }
          >

            <option value="">
              Select Health Status
            </option>

            <option value="HEALTHY">
              HEALTHY
            </option>

            <option value="WARNING">
              WARNING
            </option>

            <option value="CRITICAL">
              CRITICAL
            </option>

          </select>


          <button
            type="button"
            onClick={searchByHealthStatus}
          >
            Filter Health
          </button>


          <button
            type="button"
            className="clear-btn"
            onClick={clearSearch}
          >
            Clear All
          </button>

        </div>

      </div>


      {/* ================= ROBOT LIST ================= */}

      <h2 className="section-title">
        📋 Robot List
      </h2>


      {loading ? (

        <p className="loading-text">
          Loading robots...
        </p>

      ) : (

        <div className="table-container">

          <table>

            <thead>

              <tr>

                <th>ID</th>
                <th>Robot Code</th>
                <th>Name</th>
                <th>Model</th>
                <th>Status</th>
                <th>Battery</th>
                <th>Location</th>
                <th>Health</th>
                <th>Action</th>

              </tr>

            </thead>


            <tbody>

              {robots.length === 0 ? (

                <tr>

                  <td colSpan="9">
                    No robots found.
                  </td>

                </tr>

              ) : (

                robots.map(
                  (robot) => (

                    <tr
                      key={robot.id}
                    >

                      <td>
                        {robot.id}
                      </td>

                      <td>
                        {robot.robotCode}
                      </td>

                      <td>
                        {robot.name}
                      </td>

                      <td>
                        {robot.model}
                      </td>


                      {/* STATUS */}

                      <td>

                        <span
                          className={`status ${
                            robot.status?.toLowerCase() || ""
                          }`}
                        >
                          {robot.status}
                        </span>

                      </td>


                      {/* BATTERY */}

                      <td>

                        <span
                          className={`battery ${
                            robot.batteryLevel < 20
                              ? "critical-battery"
                              : robot.batteryLevel <= 50
                              ? "warning-battery"
                              : "healthy-battery"
                          }`}
                        >
                          🔋 {robot.batteryLevel}%
                        </span>

                      </td>


                      {/* LOCATION */}

                      <td>
                        {robot.location}
                      </td>


                      {/* HEALTH */}

                      <td>

                        <span
                          className={`health ${
                            robot.healthStatus?.toLowerCase() || ""
                          }`}
                        >
                          {robot.healthStatus || "N/A"}
                        </span>

                      </td>


                      {/* ACTION */}

                      <td>

                        <button
                          type="button"
                          className="edit-btn"
                          onClick={() =>
                            editRobot(robot)
                          }
                        >
                          Edit
                        </button>


                        <button
                          type="button"
                          className="delete-btn"
                          onClick={() =>
                            deleteRobot(robot.id)
                          }
                        >
                          Delete
                        </button>

                      </td>

                    </tr>

                  )
                )

              )}

            </tbody>

          </table>

        </div>

      )}

    </div>

  );

}

export default App;