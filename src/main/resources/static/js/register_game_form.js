// Get data from server (passed via window.serverData)
const availableLanguages = window.serverData?.languages || [
  { languageId: "EN", languageName: "English" },
  { languageId: "KO", languageName: "Korean" },
  { languageId: "JA", languageName: "Japanese" },
];

// Existing data for edit mode
const existingGameData = window.serverData?.inputGameRequest || null;
const isEditMode = window.serverData?.mode === 'edit';

let languageEntries = [];
let languageCounter = 0;

// Initialize the form
document.addEventListener("DOMContentLoaded", function () {
  populateLanguageModal();

  // Initialize form based on mode
  if (isEditMode && existingGameData) {
    initializeEditMode();
  } else {
    initializeRegisterMode();
  }

  updateAddLanguageButton();

  // Auto-hide toasts
  const toasts = document.querySelectorAll(".toast");
  toasts.forEach((toast) => {
    setTimeout(() => {
      toast.classList.remove("show");
    }, 5000);
  });
});

function initializeEditMode() {
  console.log('Initializing edit mode with data:', existingGameData);

  // Populate existing language entries if available
  if (existingGameData && existingGameData.gameNameRequests && existingGameData.gameNameRequests.length > 0) {
    existingGameData.gameNameRequests.forEach((gameNameRequest, index) => {
      if (gameNameRequest && gameNameRequest.languageId && gameNameRequest.name) {
        const languageName = availableLanguages.find(
          lang => lang.languageId === gameNameRequest.languageId
        )?.languageName || gameNameRequest.languageId;

        const newEntry = {
          id: languageCounter++,
          languageId: gameNameRequest.languageId,
          languageName: languageName,
          gameName: gameNameRequest.name,
          isDefault: gameNameRequest.isDefault || false
        };

        languageEntries.push(newEntry);
      }
    });

    // Ensure at least one entry is default if none is set
    if (languageEntries.length > 0 && !languageEntries.some(entry => entry.isDefault)) {
      languageEntries[0].isDefault = true;
    }

    renderLanguageEntries();
  }
}

function initializeRegisterMode() {
  // Start with empty form for registration
  languageEntries = [];
  languageCounter = 0;
}

function populateLanguageModal() {
  const modalLanguageSelect = document.getElementById("modalLanguage");
  modalLanguageSelect.innerHTML = '<option value="">Select a language</option>';

  availableLanguages.forEach((lang) => {
    const option = document.createElement("option");
    option.value = lang.languageId;
    option.textContent = lang.languageName;
    modalLanguageSelect.appendChild(option);
  });
}

function showAddLanguageModal() {
  updateModalLanguageOptions();
  document.getElementById("modalGameName").value = "";
  document.getElementById("modalIsDefault").checked =
    languageEntries.length === 0;

  const modal = new bootstrap.Modal(
    document.getElementById("addLanguageModal")
  );
  modal.show();
}

function updateModalLanguageOptions() {
  const modalLanguageSelect = document.getElementById("modalLanguage");
  const usedLanguages = languageEntries.map((entry) => entry.languageId);

  modalLanguageSelect.innerHTML = '<option value="">Select a language</option>';

  availableLanguages.forEach((lang) => {
    if (!usedLanguages.includes(lang.languageId)) {
      const option = document.createElement("option");
      option.value = lang.languageId;
      option.textContent = lang.languageName;
      modalLanguageSelect.appendChild(option);
    }
  });
}

function addLanguageEntry() {
  const languageId = document.getElementById("modalLanguage").value;
  const gameName = document.getElementById("modalGameName").value.trim();
  const isDefault = document.getElementById("modalIsDefault").checked;

  // Validation
  if (!languageId) {
    alert("Please select a language");
    return;
  }
  if (!gameName) {
    alert("Please enter a game name");
    return;
  }

  // If setting as default, remove default from other entries
  if (isDefault) {
    languageEntries.forEach((entry) => {
      entry.isDefault = false;
    });
  }

  const languageName = availableLanguages.find(
    (lang) => lang.languageId === languageId
  ).languageName;

  const newEntry = {
    id: languageCounter++,
    languageId: languageId,
    languageName: languageName,
    gameName: gameName,
    isDefault: isDefault,
  };

  languageEntries.push(newEntry);
  renderLanguageEntries();
  updateAddLanguageButton();

  // Close modal
  const modal = bootstrap.Modal.getInstance(
    document.getElementById("addLanguageModal")
  );
  modal.hide();
}

function removeLanguageEntry(entryId) {
  const entryIndex = languageEntries.findIndex((entry) => entry.id === entryId);
  if (entryIndex === -1) return;

  const wasDefault = languageEntries[entryIndex].isDefault;
  languageEntries.splice(entryIndex, 1);

  // If removed entry was default, make first entry default
  if (wasDefault && languageEntries.length > 0) {
    languageEntries[0].isDefault = true;
  }

  renderLanguageEntries();
  updateAddLanguageButton();
}

function setAsDefault(entryId) {
  languageEntries.forEach((entry) => {
    entry.isDefault = entry.id === entryId;
  });
  renderLanguageEntries();
}

function renderLanguageEntries() {
  const container = document.getElementById("languageEntries");
  container.innerHTML = "";

  languageEntries.forEach((entry, index) => {
    const entryDiv = document.createElement("div");
    entryDiv.className = `language-entry ${
      entry.isDefault ? "default-language" : ""
    }`;

    // Only add 'new' class for newly added entries, not for edit mode initialization
    if (index === languageEntries.length - 1 && !isEditMode) {
      entryDiv.classList.add("new");
    }

    entryDiv.innerHTML = `
        <div class="row align-items-center">
          <div class="col-md-4">
            <label class="form-label fw-bold">
              <i class="fas fa-language me-2"></i>${entry.languageName}
            </label>
            <input type="hidden" name="gameNameRequests[${index}].languageId" value="${entry.languageId}">
            <input type="hidden" name="gameNameRequests[${index}].isDefault" value="${entry.isDefault}" class="isDefault-input">
          </div>
          <div class="col-md-8">
            <input type="text"
                   class="form-control"
                   name="gameNameRequests[${index}].name"
                   value="${entry.gameName.replace(/"/g, '&quot;')}"
                   onchange="updateLanguageEntryName(${entry.id}, this.value)"
                   placeholder="Game name in ${entry.languageName}"
                   required>
          </div>
        </div>

        <div class="mt-2 d-flex gap-2">
          ${
            !entry.isDefault
              ? `
            <button type="button" class="btn btn-sm btn-outline-success" onclick="setAsDefault(${entry.id})">
              <i class="fas fa-star me-1"></i>Set as Default
            </button>
          `
              : `
            <span class="badge bg-success">
              <i class="fas fa-star me-1"></i>Default Language
            </span>
          `
          }
        </div>

        ${
          languageEntries.length > 1
            ? `
          <button type="button" class="remove-language btn btn-sm btn-danger" onclick="removeLanguageEntry(${entry.id})">
            <i class="fas fa-times"></i>
          </button>
        `
            : ""
        }
      `;

    container.appendChild(entryDiv);
  });

  // Update all isDefault inputs after rendering
  updateAllIsDefaultInputs();
}

// Update all isDefault input values
function updateAllIsDefaultInputs() {
  const isDefaultInputs = document.querySelectorAll('.isDefault-input');
  isDefaultInputs.forEach((input, index) => {
    const entry = languageEntries[index];
    if (entry) {
      input.value = entry.isDefault;
    }
  });
}

function updateLanguageEntryName(entryId, newName) {
  const entry = languageEntries.find((e) => e.id === entryId);
  if (entry) {
    entry.gameName = newName;
  }
}

function updateAddLanguageButton() {
  const addButton = document.querySelector(".add-language-btn");
  const usedLanguagesCount = languageEntries.length;
  const availableLanguagesCount = availableLanguages.length;

  if (usedLanguagesCount >= availableLanguagesCount) {
    addButton.style.display = "none";
  } else {
    addButton.style.display = "block";
  }
}

function resetForm() {
  const confirmMessage = isEditMode
    ? "Are you sure you want to reset the form? All changes will be lost and the form will be restored to original values."
    : "Are you sure you want to reset the form? All entered data will be lost.";

  if (confirm(confirmMessage)) {
    if (isEditMode) {
      // Reset to original data for edit mode
      document.getElementById("gameRegistrationForm").reset();
      languageEntries = [];
      languageCounter = 0;
      initializeEditMode(); // Re-initialize with original data
    } else {
      // Clear everything for register mode
      document.getElementById("gameRegistrationForm").reset();
      languageEntries = [];
      languageCounter = 0;
      renderLanguageEntries();
    }
    updateAddLanguageButton();
    clearValidationErrors();
  }
}

function clearValidationErrors() {
  document.querySelectorAll(".is-invalid").forEach((el) => {
    el.classList.remove("is-invalid");
  });
  document.querySelectorAll(".invalid-feedback").forEach((el) => {
    el.textContent = "";
  });
}

// Form validation
document
  .getElementById("gameRegistrationForm")
  .addEventListener("submit", function (e) {
    let isValid = true;
    clearValidationErrors();

    updateAllIsDefaultInputs();

    // Validate game code
    const gameCode = document.getElementById("gameCode").value.trim();
    if (!gameCode || gameCode.length < 5) {
      document.getElementById("gameCode").classList.add("is-invalid");
      document.getElementById("gameCodeError").textContent =
        "Game code must be at least 5 characters long";
      isValid = false;
    }

    // Validate category
    const category = document.getElementById("category").value;
    if (!category) {
      document.getElementById("category").classList.add("is-invalid");
      document.getElementById("categoryError").textContent =
        "Please select a category";
      isValid = false;
    }

    // Validate language entries
    if (languageEntries.length === 0) {
      alert("Please add at least one language entry");
      isValid = false;
    }

    // Check if at least one default language is set
    const hasDefault = languageEntries.some((entry) => entry.isDefault);
    if (languageEntries.length > 0 && !hasDefault) {
      alert("Please set one language as default");
      isValid = false;
    }

    // Debug logging
    if (console && console.log) {
      document.querySelectorAll('.isDefault-input').forEach((input, idx) => {
        console.log(`Input ${idx} isDefault value:`, input.value);
      });
    }

    if (!isValid) {
      e.preventDefault();
      return false;
    }
  });