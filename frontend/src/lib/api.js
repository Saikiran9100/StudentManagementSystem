export function createApiClient(apiBase, tenantId) {
  async function request(path, options = {}) {
    const { method = 'GET', body, params, isFormData = false } = options;
    const requestUrl = new URL(`${apiBase}${path}`, window.location.origin);

    if (params) {
      Object.entries(params).forEach(([key, value]) => {
        if (value !== '' && value !== undefined && value !== null) {
          requestUrl.searchParams.set(key, value);
        }
      });
    }

    const headers = { 'X-Tenant-ID': tenantId || 'default' };
    if (!isFormData) {
      headers['Content-Type'] = 'application/json';
    }

    const response = await fetch(requestUrl.toString(), {
      method,
      headers,
      body: isFormData ? body : body ? JSON.stringify(body) : undefined,
    });

    let payload = null;
    try {
      payload = await response.json();
    } catch {
      payload = null;
    }

    if (!response.ok || (payload && payload.success === false)) {
      throw new Error(payload?.message || `Request failed with status ${response.status}`);
    }

    return payload?.data ?? payload;
  }

  return {
    get: (path, params) => request(path, { method: 'GET', params }),
    post: (path, body, params, isFormData = false) => request(path, { method: 'POST', body, params, isFormData }),
    put: (path, body, params) => request(path, { method: 'PUT', body, params }),
    patch: (path, body, params) => request(path, { method: 'PATCH', body, params }),
    delete: (path) => request(path, { method: 'DELETE' }),
  };
}
