import { useMemo, useState } from 'react';
import { Outlet, useLocation, useNavigate } from 'react-router-dom';
import LayoutShell from './LayoutShell.jsx';
import TenantDialog from './TenantDialog.jsx';
import { navigationItems } from '../../lib/config.js';
import { useTenant } from '../providers/TenantProvider.jsx';
import FeedbackSnackbar from '../shared/FeedbackSnackbar.jsx';

const initialSnackbar = { open: false, severity: 'success', message: '' };

function AppLayout() {
  const [mobileOpen, setMobileOpen] = useState(false);
  const [tenantDialogOpen, setTenantDialogOpen] = useState(false);
  const [newTenantId, setNewTenantId] = useState('');
  const [tenantDialogError, setTenantDialogError] = useState('');
  const [tenantSubmitting, setTenantSubmitting] = useState(false);
  const [snackbar, setSnackbar] = useState(initialSnackbar);
  const navigate = useNavigate();
  const location = useLocation();
  const { tenantId, tenantOptions, setTenantId, tenantLoading, createTenant } = useTenant();

  const activeSection = useMemo(
    () => navigationItems.find((item) => item.path === location.pathname)?.id || 'overview',
    [location.pathname],
  );

  function closeTenantDialog(force = false) {
    if (tenantSubmitting && !force) {
      return;
    }
    setTenantDialogOpen(false);
    setNewTenantId('');
    setTenantDialogError('');
  }

  async function handleTenantCreate() {
    const normalizedTenantId = newTenantId.trim().toUpperCase();

    if (!normalizedTenantId) {
      setTenantDialogError('Tenant ID is required.');
      return;
    }

    if (!/^[A-Z0-9_-]{2,30}$/.test(normalizedTenantId)) {
      setTenantDialogError('Use 2-30 letters, numbers, underscores, or hyphens.');
      return;
    }

    try {
      setTenantSubmitting(true);
      setTenantDialogError('');
      const createdTenantId = await createTenant(normalizedTenantId);
      closeTenantDialog(true);
      setSnackbar({
        open: true,
        severity: 'success',
        message: `Tenant ${createdTenantId} created successfully.`,
      });
    } catch (error) {
      setTenantDialogError(error.message || 'Unable to create tenant.');
    } finally {
      setTenantSubmitting(false);
    }
  }

  return (
    <>
      <LayoutShell
        activeSection={activeSection}
        navigationItems={navigationItems}
        mobileOpen={mobileOpen}
        setMobileOpen={setMobileOpen}
        tenantId={tenantId}
        tenantOptions={tenantOptions}
        loading={tenantLoading}
        submitting={tenantSubmitting}
        error=""
        onTenantChange={setTenantId}
        onTenantAddRequest={() => setTenantDialogOpen(true)}
        onSectionChange={(path) => navigate(path)}
      >
        <Outlet />
      </LayoutShell>
      <TenantDialog
        open={tenantDialogOpen}
        value={newTenantId}
        error={tenantDialogError}
        onChange={(value) => {
          setNewTenantId(value);
          if (tenantDialogError) {
            setTenantDialogError('');
          }
        }}
        onClose={closeTenantDialog}
        onSave={handleTenantCreate}
        submitting={tenantSubmitting}
      />
      <FeedbackSnackbar
        snackbar={snackbar}
        onClose={() => setSnackbar((current) => ({ ...current, open: false }))}
      />
    </>
  );
}

export default AppLayout;
