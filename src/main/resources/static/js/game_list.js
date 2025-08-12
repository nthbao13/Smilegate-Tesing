// URL utility functions for clean URLs
  function buildCleanUrl(page) {
      const baseUrl = '/api/games';
      const params = new URLSearchParams();

      // Only add page if not page 1
      if (page && page > 1) {
          params.set('page', page);
      }

      // Only add keyword if it has value
      const keyword = document.getElementById('keyword').value.trim();
      if (keyword) {
          params.set('keyword', keyword);
      }

      // Only add category if it has value
      const category = document.getElementById('category').value;
      if (category) {
          params.set('category', category);
      }

      const queryString = params.toString();
      return queryString ? `${baseUrl}?${queryString}` : baseUrl;
  }

  function navigateToPage(page) {
      window.location.href = buildCleanUrl(page);
  }

  // Clean form submission
  document.getElementById('searchForm').addEventListener('submit', function(e) {
      e.preventDefault();

      const keyword = document.getElementById('keyword').value.trim();
      const category = document.getElementById('category').value;

      // If no search criteria, just redirect to base URL
      if (!keyword && !category) {
          window.location.href = '/api/games';
          return;
      }

      // Build clean URL for search
      const params = new URLSearchParams();
      if (keyword) params.set('keyword', keyword);
      if (category) params.set('category', category);

      window.location.href = `/api/games?${params.toString()}`;
  });

  // Toast auto-hide functionality
  document.addEventListener('DOMContentLoaded', function() {
      const toasts = document.querySelectorAll('.toast');
      toasts.forEach(toast => {
          setTimeout(() => {
              toast.classList.remove('show');
          }, 5000);
      });
  });

  // Checkbox functionality
  const masterCheckbox = document.getElementById('masterCheckbox');
  const gameCheckboxes = document.querySelectorAll('.game-checkbox');
  const deleteSelectedBtn = document.getElementById('deleteSelected');
  const selectAllBtn = document.getElementById('selectAll');

  function updateDeleteButton() {
      const checkedBoxes = document.querySelectorAll('.game-checkbox:checked');
      deleteSelectedBtn.disabled = checkedBoxes.length === 0;

      if (checkedBoxes.length === gameCheckboxes.length && gameCheckboxes.length > 0) {
          masterCheckbox.indeterminate = false;
          masterCheckbox.checked = true;
      } else if (checkedBoxes.length > 0) {
          masterCheckbox.indeterminate = true;
          masterCheckbox.checked = false;
      } else {
          masterCheckbox.indeterminate = false;
          masterCheckbox.checked = false;
      }
  }

  masterCheckbox.addEventListener('change', function() {
      gameCheckboxes.forEach(checkbox => {
          checkbox.checked = this.checked;
      });
      updateDeleteButton();
  });

  gameCheckboxes.forEach(checkbox => {
      checkbox.addEventListener('change', updateDeleteButton);
  });

  selectAllBtn.addEventListener('click', function() {
      gameCheckboxes.forEach(checkbox => {
          checkbox.checked = true;
      });
      updateDeleteButton();
  });

  deleteSelectedBtn.addEventListener('click', function() {
      const checkedBoxes = document.querySelectorAll('.game-checkbox:checked');
      if (checkedBoxes.length > 0) {
          if (confirm(`Are you sure you want to delete ${checkedBoxes.length} selected game(s)?`)) {
              const gameIds = Array.from(checkedBoxes).map(cb => cb.value);
              // Implement bulk delete API call here
              console.log('Deleting games:', gameIds);
          }
      }
  });

  function editGame(gameId) {
      window.location.href = `/api/games/${gameId}/edit`;
  }

  // Auto-clear empty search inputs on page load
  document.addEventListener('DOMContentLoaded', function() {
      const urlParams = new URLSearchParams(window.location.search);

      // Clean up URL if it has empty parameters
      let shouldRedirect = false;
      const cleanParams = new URLSearchParams();

      urlParams.forEach((value, key) => {
          if (value && value.trim()) {
              cleanParams.set(key, value);
          } else {
              shouldRedirect = true;
          }
      });

      // Redirect to clean URL if needed
      if (shouldRedirect) {
          const cleanUrl = cleanParams.toString() ?
              `/api/games?${cleanParams.toString()}` : '/api/games';
          window.history.replaceState({}, '', cleanUrl);
      }
  });