import { useState } from 'react';
import DepartmentsPage from './DepartmentsPage.jsx';
import DepartmentDialog from './DepartmentDialog.jsx';
import FeedbackSnackbar from '../shared/FeedbackSnackbar.jsx';
import { useTenant } from '../providers/TenantProvider.jsx';
import { usePagedResource } from '../../hooks/usePagedResource.js';
import { departmentsService } from '../../services/departmentsService.js';
import { toInteger } from '../../lib/config.js';

const initialDepartmentValues = {
  deptName: '',
  deptCode: '',
  capacity: '',
};

function mapDepartmentToFormValues(department) {
  return {
    id: department.deptId || '',
    deptName: department.deptName || '',
    deptCode: department.deptCode || '',
    capacity: department.capacity ?? '',
  };
}

function DepartmentsRoute() {
  const { client, tenantId } = useTenant();
  const [dialog, setDialog] = useState({ open: false, mode: 'create', values: initialDepartmentValues });
  const [submitting, setSubmitting] = useState(false);
  const [snackbar, setSnackbar] = useState({ open: false, severity: 'success', message: '' });

  const paged = usePagedResource({
    fetchPage: ({ page, pageSize, filters }) => departmentsService.getPage(client, { pageNo: page, pageSize, ...filters }),
    initialFilters: { deptName: '', deptCode: '' },
    reloadKey: tenantId,
  });

  function showSnackbar(message, severity = 'success') {
    setSnackbar({ open: true, severity, message });
  }

  function closeDialog() {
    setDialog({ open: false, mode: 'create', values: initialDepartmentValues });
  }

  function openCreate() {
    setDialog({ open: true, mode: 'create', values: initialDepartmentValues });
  }

  async function openEdit(row) {
    try {
      const detail = await departmentsService.getById(client, row.deptId);
      setDialog({ open: true, mode: 'edit', values: mapDepartmentToFormValues(detail) });
    } catch (error) {
      showSnackbar(error.message, 'error');
    }
  }

  async function saveDialog() {
    setSubmitting(true);
    try {
      const payload = {
        deptName: dialog.values.deptName,
        deptCode: dialog.values.deptCode,
        capacity: toInteger(dialog.values.capacity),
      };

      if (dialog.mode === 'create') {
        await departmentsService.create(client, payload);
      } else {
        await departmentsService.update(client, dialog.values.id, payload);
      }

      closeDialog();
      await paged.loadPage({ page: 1 });
      showSnackbar(`Department ${dialog.mode === 'create' ? 'created' : 'updated'} successfully`);
    } catch (error) {
      showSnackbar(error.message, 'error');
    } finally {
      setSubmitting(false);
    }
  }

  async function removeDepartment(row) {
    if (!window.confirm('Delete this department?')) {
      return;
    }

    try {
      await departmentsService.remove(client, row.deptId);
      await paged.loadPage();
      showSnackbar('Department deleted successfully');
    } catch (error) {
      showSnackbar(error.message, 'error');
    }
  }

  return (
    <>
      <DepartmentsPage
        rows={paged.rows}
        pagination={paged.pagination}
        filterDrafts={paged.filterDrafts}
        onFilterChange={paged.updateFilterDraft}
        onApplyFilters={() => paged.applyFilterDrafts().catch((error) => showSnackbar(error.message, 'error'))}
        onResetFilters={() => paged.resetFilterDrafts().catch((error) => showSnackbar(error.message, 'error'))}
        onPageChange={(page) => paged.loadPage({ page }).catch((error) => showSnackbar(error.message, 'error'))}
        onPageSizeChange={(pageSize) => paged.loadPage({ page: 1, pageSize }).catch((error) => showSnackbar(error.message, 'error'))}
        onCreate={openCreate}
        onEdit={openEdit}
        onDelete={removeDepartment}
      />
      <DepartmentDialog
        open={dialog.open}
        mode={dialog.mode}
        values={dialog.values}
        onChange={(key, value) => setDialog((current) => ({ ...current, values: { ...current.values, [key]: value } }))}
        onClose={closeDialog}
        onSave={saveDialog}
        submitting={submitting}
      />
      <FeedbackSnackbar snackbar={snackbar} onClose={() => setSnackbar((current) => ({ ...current, open: false }))} />
    </>
  );
}

export default DepartmentsRoute;
