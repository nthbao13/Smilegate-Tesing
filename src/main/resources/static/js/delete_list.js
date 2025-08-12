const LS_KEY = 'selectedGames';

function loadSelected() {
  try {
    const raw = localStorage.getItem(LS_KEY);
    return raw ? JSON.parse(raw) : [];
  } catch (e) {
    console.error('Failed to parse selectedGames from localStorage', e);
    return [];
  }
}

function saveSelected(arr) {
  localStorage.setItem(LS_KEY, JSON.stringify(arr));
}

function getSelectedIdsSet() {
  return new Set(loadSelected().map(item => String(item.id)));
}

function addSelected(item) {
  const arr = loadSelected();
  if (!arr.find(x => String(x.id) === String(item.id))) {
    arr.push(item);
    saveSelected(arr);
  }
}

function removeSelectedById(id) {
  let arr = loadSelected();
  arr = arr.filter(x => String(x.id) !== String(id));
  saveSelected(arr);
}

function clearSelectedIds(ids = null) {
  if (!ids) {
    // clear all
    localStorage.removeItem(LS_KEY);
  } else {
    // remove listed ids
    let arr = loadSelected();
    const set = new Set(ids.map(String));
    arr = arr.filter(x => !set.has(String(x.id)));
    saveSelected(arr);
  }
}

function updateSmallFloatingModal() {
  const arr = loadSelected();
  const small = document.getElementById('deleteSmallModal');
  if (!small) return;
  if (arr.length > 0) {
    small.style.display = 'flex';
    const countEl = document.getElementById('deleteSmallCount');
    countEl.textContent = arr.length;
  } else {
    small.style.display = 'none';
  }
}

function renderLargeModalList() {
  const arr = loadSelected();

  const largeCountEl = document.getElementById('deleteLargeCount');
    if (largeCountEl) {
      largeCountEl.textContent = arr.length;
    }

  const tbody = document.getElementById('deleteLargeListBody');
  if (!tbody) return;
  tbody.innerHTML = '';
  if (arr.length === 0) {
    tbody.innerHTML = `<tr><td colspan="4" class="text-center py-3">No selected items</td></tr>`;
    return;
  }
  arr.forEach(item => {
    const tr = document.createElement('tr');

    const tdCheck = document.createElement('td');
    tdCheck.innerHTML = `<input type="checkbox" class="form-check-input large-modal-checkbox" data-id="${escapeHtml(String(item.id))}" checked>`;
    tr.appendChild(tdCheck);

    const tdName = document.createElement('td');
    tdName.textContent = item.name || '';
    tr.appendChild(tdName);

    const tdCode = document.createElement('td');
    tdCode.textContent = item.code || '';
    tr.appendChild(tdCode);

    const tdCategory = document.createElement('td');
    tdCategory.textContent = item.category || '';
    tr.appendChild(tdCategory);

    tbody.appendChild(tr);
  });
}

function escapeHtml(str) {
  return str.replace(/[&<>"']/g, (m) => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[m]));
}

function syncCheckboxesFromStorage() {
  const ids = getSelectedIdsSet();
  const checkboxes = document.querySelectorAll('.game-checkbox');
  checkboxes.forEach(cb => {
    const id = String(cb.value);
    if (ids.has(id)) {
      cb.checked = true;
    } else {
      cb.checked = false;
    }
  });

  updateMasterCheckboxState();
  updateDeleteButtonState();
  updateSmallFloatingModal();
}

function updateMasterCheckboxState() {
  const master = document.getElementById('masterCheckbox');
  const checkboxes = Array.from(document.querySelectorAll('.game-checkbox'));
  if (!master || checkboxes.length === 0) return;

  const checkedCount = checkboxes.filter(cb => cb.checked).length;
  if (checkedCount === 0) {
    master.checked = false;
    master.indeterminate = false;
  } else if (checkedCount === checkboxes.length) {
    master.checked = true;
    master.indeterminate = false;
  } else {
    master.checked = false;
    master.indeterminate = true;
  }
}

function updateDeleteButtonState() {
  const btn = document.getElementById('deleteSelected');
  if (!btn) return;
  const count = loadSelected().length;
  btn.disabled = count === 0;
}

function attachCheckboxListeners() {
  const checkboxes = document.querySelectorAll('.game-checkbox');

  checkboxes.forEach(cb => {
    cb.addEventListener('change', function (e) {
      const id = String(this.value);
      const name = this.getAttribute('data-name') || this.dataset.name || (this.closest('tr')?.querySelector('strong')?.textContent || '');
      const code = this.getAttribute('data-code') || this.dataset.code || (this.closest('tr')?.querySelector('code')?.textContent || '');
      const category = this.getAttribute('data-category') || this.dataset.category || (this.closest('tr')?.querySelector('.category-badge')?.textContent || '');

      if (this.checked) {
        addSelected({ id, name, code, category });
      } else {
        removeSelectedById(id);
      }

      updateMasterCheckboxState();
      updateDeleteButtonState();
      updateSmallFloatingModal();
    });
  });

  const master = document.getElementById('masterCheckbox');
  if (master) {
    master.addEventListener('change', function () {
      const checkboxes = document.querySelectorAll('.game-checkbox');
      checkboxes.forEach(cb => {
        cb.checked = master.checked;
        cb.dispatchEvent(new Event('change'));
      });
    });
  }

  const selectAllBtn = document.getElementById('selectAll');
  if (selectAllBtn) {
    selectAllBtn.addEventListener('click', function (e) {
      e.preventDefault();
      const checkboxes = document.querySelectorAll('.game-checkbox');
      checkboxes.forEach(cb => {
        if (!cb.checked) {
          cb.checked = true;
          cb.dispatchEvent(new Event('change'));
        }
      });
    });
  }

  const deleteSelectedBtn = document.getElementById('deleteSelected');
  if (deleteSelectedBtn) {
    deleteSelectedBtn.addEventListener('click', function (e) {
      e.preventDefault();
      const modalEl = document.getElementById('deleteLargeModal');
      if (modalEl) {
        renderLargeModalList();
        const bsModal = new bootstrap.Modal(modalEl);
        bsModal.show();
      } else {
        if (confirm(`Delete ${loadSelected().length} selected games?`)) {
          sendBulkDeleteRequest();
        }
      }
    });
  }

  const small = document.getElementById('deleteSmallModal');
  if (small) {
    small.addEventListener('click', function () {
      const modalEl = document.getElementById('deleteLargeModal');
      if (modalEl) {
        renderLargeModalList();
        const bsModal = new bootstrap.Modal(modalEl);
        bsModal.show();
      }
    });
  }
}

function attachLargeModalHandlers() {
  document.addEventListener('change', function (e) {
    if (e.target && e.target.classList.contains('large-modal-checkbox')) {
      const id = e.target.getAttribute('data-id');
      if (!id) return;
      if (!e.target.checked) {
        removeSelectedById(id);
        const pageCb = document.querySelector(`.game-checkbox[value="${CSS.escape(id)}"]`);
        if (pageCb) pageCb.checked = false;
      } else {
        const pageCb = document.querySelector(`.game-checkbox[value="${CSS.escape(id)}"]`);
        const name = pageCb?.getAttribute('data-name') || pageCb?.closest('tr')?.querySelector('strong')?.textContent || '';
        const code = pageCb?.getAttribute('data-code') || pageCb?.closest('tr')?.querySelector('code')?.textContent || '';
        const category = pageCb?.getAttribute('data-category') || pageCb?.closest('tr')?.querySelector('.category-badge')?.textContent || '';
        addSelected({ id, name, code, category });
        if (pageCb) pageCb.checked = true;
      }
      renderLargeModalList();
      updateSmallFloatingModal();
      updateDeleteButtonState();
      updateMasterCheckboxState();
    }
  });


  const confirmBtn = document.getElementById('confirmBulkDeleteBtn');
  if (confirmBtn) {
    confirmBtn.addEventListener('click', function () {
      const arr = loadSelected();
      if (arr.length === 0) {
        alert('No items selected.');
        return;
      }
      if (!confirm(`Are you sure you want to delete ${arr.length} selected games? This action cannot be undone.`)) {
        return;
      }

      const ids = arr.map(x => x.id);
      sendBulkDeleteRequest(ids)
        .then(() => {
          clearSelectedIds(ids);
          updateSmallFloatingModal();
          updateDeleteButtonState();
          const modalEl = document.getElementById('deleteLargeModal');
          const bsModal = bootstrap.Modal.getInstance(modalEl);
          if (bsModal) bsModal.hide();
          window.location.reload();
        })
        .catch(err => {
          console.error('Bulk delete failed', err);
          alert('Delete failed. See console for details.');
        });
    });
  }
}

function sendBulkDeleteRequest(ids = null) {
  const arr = loadSelected();
  const targetIds = ids || arr.map(x => x.id);
  if (!targetIds || targetIds.length === 0) {
    return Promise.reject(new Error('No ids to delete'));
  }

  const endpoint = '/api/games/bulk-delete';

  const headers = {
    'Content-Type': 'application/json'
  };

  return fetch(endpoint, {
    method: 'POST',
    credentials: 'same-origin',
    headers,
    body: JSON.stringify(targetIds)
  }).then(resp => {
    if (!resp.ok) {
      return resp.text().then(t => { throw new Error(t || resp.statusText); });
    }
    return resp.json?.() ?? resp.text();
  });
}

// on load
document.addEventListener('DOMContentLoaded', function () {
  attachCheckboxListeners();
  attachLargeModalHandlers();
  syncCheckboxesFromStorage();
});
