const categorySelect = document.getElementById("categorySelect");
const newCategoryInput = document.getElementById("newCategoryInput");
categorySelect.addEventListener("change", () => {
  if (categorySelect.value === "other") {
    newCategoryInput.classList.remove("hidden");
    newCategoryInput.required = true;
    categorySelect.name = "categoryId_disabled";
  } else {
    newCategoryInput.classList.add("hidden");
    newCategoryInput.required = false;
    categorySelect.name = "categoryId";
  }
});

const languagesContainer = document.getElementById("languagesContainer");
const addLangBtn = document.getElementById("addLangBtn");

// Lấy list option ngôn ngữ gốc từ dòng đầu tiên để clone
const originalOptions = [
  ...languagesContainer.querySelector("select.languageSelect").options,
].map((opt) => ({ value: opt.value, text: opt.text }));

function updateLanguageOptions() {
  // Lấy danh sách ngôn ngữ đã chọn
  const selectedLanguages = Array.from(
    document.querySelectorAll("select.languageSelect")
  )
    .map((select) => select.value)
    .filter((v) => v !== "");

  // Với mỗi select, ẩn các option đã chọn ở các select khác
  document.querySelectorAll("select.languageSelect").forEach((select) => {
    const currentValue = select.value;
    // Xóa hết option trước
    select.innerHTML = "";
    originalOptions.forEach((opt) => {
      // Hiện option nếu chưa được chọn ở select khác hoặc là option của chính select này
      if (
        opt.value === "" ||
        opt.value === currentValue ||
        !selectedLanguages.includes(opt.value)
      ) {
        const option = document.createElement("option");
        option.value = opt.value;
        option.text = opt.text;
        if (opt.value === currentValue) option.selected = true;
        select.appendChild(option);
      }
    });
  });
}

addLangBtn.addEventListener("click", () => {
  const index = languagesContainer.children.length;
  const newRow = document.createElement("div");
  newRow.classList.add("lang-row");

  // Tạo select với các option (copy từ originalOptions)
  const select = document.createElement("select");
  select.name = `gameNames[${index}].languageId`;
  select.classList.add("languageSelect");
  select.required = true;

  originalOptions.forEach((opt) => {
    const option = document.createElement("option");
    option.value = opt.value;
    option.text = opt.text;
    select.appendChild(option);
  });

  // Input tên game
  const inputName = document.createElement("input");
  inputName.type = "text";
  inputName.name = `gameNames[${index}].name`;
  inputName.placeholder = "Enter game name";
  inputName.required = true;

  // Checkbox default
  const labelDefault = document.createElement("label");
  const checkboxDefault = document.createElement("input");
  checkboxDefault.type = "checkbox";
  checkboxDefault.name = `gameNames[${index}].isDefault`;
  labelDefault.appendChild(checkboxDefault);
  labelDefault.appendChild(document.createTextNode(" Default"));

  // Nút remove
  const removeBtn = document.createElement("button");
  removeBtn.type = "button";
  removeBtn.textContent = "Remove";
  removeBtn.classList.add("removeLangBtn");
  removeBtn.addEventListener("click", () => removeLangRow(removeBtn));

  // Thêm vào div
  newRow.appendChild(select);
  newRow.appendChild(inputName);
  newRow.appendChild(labelDefault);
  newRow.appendChild(removeBtn);

  languagesContainer.appendChild(newRow);

  updateLanguageOptions();
});

function removeLangRow(button) {
  button.parentElement.remove();
  updateLanguageOptions();
}

// Cập nhật options khi thay đổi ngôn ngữ
document.addEventListener("change", (e) => {
  if (e.target.classList.contains("languageSelect")) {
    updateLanguageOptions();
  }
});

updateLanguageOptions(); // khởi tạo lần đầu
