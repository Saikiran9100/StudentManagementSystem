import { createContext, useContext, useEffect, useMemo, useState } from 'react';
import { createApiClient } from '../../lib/api.js';

const TenantContext = createContext(null);

function TenantProvider({ children }) {
  const [tenantId, setTenantId] = useState(() => localStorage.getItem('tenantId') || 'default');
  const [tenantOptions, setTenantOptions] = useState(['default']);
  const [tenantLoading, setTenantLoading] = useState(true);

  const client = useMemo(() => createApiClient('/api', tenantId), [tenantId]);

  useEffect(() => {
    localStorage.setItem('tenantId', tenantId);
  }, [tenantId]);

  useEffect(() => {
    loadTenants();
  }, []);

  async function loadTenants(preferredTenantId) {
    setTenantLoading(true);
    try {
      const bootstrapClient = createApiClient('/api', preferredTenantId || tenantId || 'default');
      const tenants = await bootstrapClient.get('/tenant/list');
      const options = tenants?.length ? tenants : ['default'];
      setTenantOptions(options);
      if (preferredTenantId && options.includes(preferredTenantId)) {
        setTenantId(preferredTenantId);
      } else if (!options.includes(tenantId)) {
        setTenantId(options[0]);
      }
    } finally {
      setTenantLoading(false);
    }
  }

  async function createTenant(nextTenantId) {
    const bootstrapClient = createApiClient('/api', tenantId || 'default');
    const createdTenantId = await bootstrapClient.post('/tenant/add', { tenantId: nextTenantId });
    await loadTenants(createdTenantId);
    return createdTenantId;
  }

  const value = {
    tenantId,
    setTenantId,
    tenantOptions,
    tenantLoading,
    createTenant,
    reloadTenants: loadTenants,
    client,
  };

  return <TenantContext.Provider value={value}>{children}</TenantContext.Provider>;
}

export function useTenant() {
  const context = useContext(TenantContext);
  if (!context) {
    throw new Error('useTenant must be used within TenantProvider');
  }
  return context;
}

export default TenantProvider;
