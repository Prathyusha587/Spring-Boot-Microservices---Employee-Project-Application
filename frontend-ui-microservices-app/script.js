// ================= LOGIN =================
async function handleLogin(event) {
  event.preventDefault();

  const username = document.getElementById("username").value;
  const password = document.getElementById("password").value;

  const payload = {
    employeeEmail: username,
    password: password,
  };

  try {
    const response = await fetch("http://localhost:8765/auth/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(payload),
    });

    const data = await response.json();

    if (response.ok && data.token) {
      localStorage.setItem("token", data.token);

      alert("Login successful");

      setTimeout(() => {
        window.location.href = "dashboard.html";
      }, 300);
    } else {
      alert(data.message || "Login failed");
    }
  } catch (error) {
    console.error("Login Error:", error);
    alert("Something went wrong during login");
  }
}

// ================= FETCH EMPLOYEES =================
async function fetchEmployeeData() {
  try {
    const token = localStorage.getItem("token");

    if (!token) {
      alert("Session expired. Please login again.");
      window.location.href = "login.html";
      return;
    }

    const response = await fetch(
      "http://localhost:8765/employee-microservices/api/employee/allemployees?page=0&size=10",
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: "Bearer " + token,
        },
      },
    );

    const data = await response.json();
    renderEmployeeData(data);
  } catch (error) {
    console.error("Error fetching employee data:", error);
  }
}

// ================= RENDER TABLE =================
function renderEmployeeData(data) {
  const tableBody = document.getElementById("employeeTableBody");
  if (!tableBody) return;

  tableBody.innerHTML = "";

  const list = Array.isArray(data) ? data : data?.content;

  if (!list || list.length === 0) {
    tableBody.innerHTML = `<tr><td colspan="6">No employee data found</td></tr>`;
    return;
  }

  list.forEach((employee) => {
    const row = document.createElement("tr");

    const id = employee.id ?? "";
    const name = employee.employeeName ?? "";
    const email = employee.employeeEmail ?? "";
    const location = employee.employeeBaseLocation ?? "";
    const projectCode = employee.projectCode ?? "";

    row.innerHTML = `
      <td>${id}</td>
      <td>${name}</td>
      <td>${email}</td>
      <td>${location}</td>
      <td>${employee.projectName ?? "-"}</td>
      <td>
        <button class="edit-btn"
          data-id="${id}"
          data-name="${name}"
          data-email="${email}"
          data-location="${location}"
          data-project="${projectCode}">
          ✏️
        </button>

        <button class="delete-btn" data-id="${id}">🗑️</button>
      </td>
    `;

    tableBody.appendChild(row);
  });

  attachEditHandlers();
}

// ================= ATTACH EDIT HANDLERS =================
function attachEditHandlers() {
  document.querySelectorAll(".edit-btn").forEach((btn) => {
    btn.addEventListener("click", () => {
      openEditModal(
        btn.dataset.id,
        btn.dataset.name,
        btn.dataset.email,
        btn.dataset.location,
        btn.dataset.project,
      );
    });
  });
}

// ================= PROJECT DROPDOWN =================
async function loadProjects(selectedProjectCode = null) {
  try {
    const token = localStorage.getItem("token");

    const response = await fetch(
      "http://localhost:8765/project-microservices/api/project/allprojects",
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: "Bearer " + token,
        },
      },
    );

    const result = await response.json();

    let projects = [];

    if (Array.isArray(result)) {
      projects = result;
    } else if (Array.isArray(result.content)) {
      projects = result.content;
    } else if (Array.isArray(result.data)) {
      projects = result.data;
    }

    const dropdown = document.getElementById("editProject");
    if (!dropdown) return;

    dropdown.innerHTML = "";

    const defaultOption = document.createElement("option");
    defaultOption.value = "";
    defaultOption.textContent = "Select Project";
    dropdown.appendChild(defaultOption);

    projects.forEach((project) => {
      const option = document.createElement("option");
      option.value = project.projectCode;
      option.textContent = project.projectName;
      dropdown.appendChild(option);
    });

    if (selectedProjectCode) {
      dropdown.value = selectedProjectCode;
    }
  } catch (error) {
    console.error("Error loading projects:", error);
  }
}

// ================= EDIT MODAL =================
function openEditModal(id, name, email, location, projectCode) {
  document.getElementById("editModal").style.display = "block";

  document.getElementById("modalTitle").textContent = "Edit Employee";

  document.getElementById("editId").value = id;
  document.getElementById("editName").value = name;
  document.getElementById("editEmail").value = email;
  document.getElementById("editLocation").value = location;

  document.getElementById("saveEditBtn").textContent = "Save";

  loadProjects(projectCode);
}

// ================= ADD EMPLOYEE MODAL =================
function openAddEmployeeModal() {
  document.getElementById("editModal").style.display = "block";

  document.getElementById("modalTitle").textContent = "Add Employee";

  document.getElementById("editId").value = "";
  document.getElementById("editName").value = "";
  document.getElementById("editEmail").value = "";
  document.getElementById("editLocation").value = "";

  document.getElementById("saveEditBtn").textContent = "Add";

  loadProjects(null);
}

// ================= CLOSE MODAL =================
document.addEventListener("DOMContentLoaded", () => {
  const closeBtn = document.getElementById("closeModalBtn");

  if (closeBtn) {
    closeBtn.addEventListener("click", () => {
      document.getElementById("editModal").style.display = "none";
    });
  }

  if (document.getElementById("employeeTableBody")) {
    fetchEmployeeData();
  }
});

// ================= SAVE (ADD + EDIT) =================
document
  .getElementById("saveEditBtn")
  ?.addEventListener("click", async function () {
    const id = document.getElementById("editId").value;
    const token = localStorage.getItem("token");

    const projectValue = document.getElementById("editProject").value;

    const payload = {
      employeeName: document.getElementById("editName").value,
      employeeEmail: document.getElementById("editEmail").value,
      employeeBaseLocation: document.getElementById("editLocation").value,
      employeeAssignedProject: projectValue ? Number(projectValue) : null,
    };

    try {
      let url = "";
      let method = "";

      if (!id) {
        url = "http://localhost:8765/employee-microservices/api/employee";
        method = "POST";
      } else {
        url = `http://localhost:8765/employee-microservices/api/employee/${id}`;
        method = "PUT";
      }

      const response = await fetch(url, {
        method,
        headers: {
          "Content-Type": "application/json",
          Authorization: "Bearer " + token,
        },
        body: JSON.stringify(payload),
      });

      if (response.ok) {
        alert(
          id
            ? "Employee updated successfully ✅"
            : "Employee added successfully ✅",
        );

        document.getElementById("editModal").style.display = "none";

        fetchEmployeeData();

        document.getElementById("saveEditBtn").textContent = "Save";
      } else {
        const errorText = await response.text();
        console.error(errorText);
        alert("Operation failed ❌");
      }
    } catch (error) {
      console.error("Error:", error);
    }
  });

// ================= DELETE EMPLOYEE =================
const deleteEmployee = async (id) => {
  const token = localStorage.getItem("token");

  const confirmDelete = confirm("Do you want to delete this employee?");
  if (!confirmDelete) return;

  try {
    const response = await fetch(
      `http://localhost:8765/employee-microservices/api/employee/${id}`,
      {
        method: "DELETE",
        headers: {
          Authorization: "Bearer " + token,
        },
      },
    );

    if (response.ok) {
      alert("Employee deleted successfully ✅");
      fetchEmployeeData();
    } else {
      alert("Failed to delete employee ❌");
    }
  } catch (error) {
    console.error("Error:", error);
  }
};

// ================= DELETE CLICK HANDLER =================
document
  .getElementById("employeeTableBody")
  ?.addEventListener("click", (event) => {
    if (event.target.classList.contains("delete-btn")) {
      const id = event.target.dataset.id;
      if (id) deleteEmployee(id);
    }
  });
