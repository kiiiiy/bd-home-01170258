fetch(`${window.API_BASE}/api/health`)
  .then(r => r.text())
  .then(t => document.getElementById("out").innerText = t)
  .catch(e => document.getElementById("out").innerText = String(e));
