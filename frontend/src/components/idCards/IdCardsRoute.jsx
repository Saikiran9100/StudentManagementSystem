import { useEffect, useState } from 'react';
import IdCardsPage from './IdCardsPage.jsx';
import IdCardDialog from './IdCardDialog.jsx';
import FeedbackSnackbar from '../shared/FeedbackSnackbar.jsx';
import { useTenant } from '../providers/TenantProvider.jsx';
import { usePagedResource } from '../../hooks/usePagedResource.js';
import { idCardsService } from '../../services/idCardsService.js';
import { studentsService } from '../../services/studentsService.js';

const initialIdCardValues = {
  cardNumber: '',
  standard: '',
  section: '',
  issueDate: '',
  expiryDate: '',
  address: '',
  studId: '',
};

function mapIdCardToFormValues(idCard) {
  return {
    id: idCard.id || '',
    cardNumber: idCard.cardNumber || '',
    standard: idCard.standard || '',
    section: idCard.section || '',
    issueDate: idCard.issueDate || '',
    expiryDate: idCard.expiryDate || '',
    address: idCard.address || '',
    studId: idCard.student?.studId || '',
  };
}

function IdCardsRoute() {
  const { client, tenantId } = useTenant();
  const [dialog, setDialog] = useState({ open: false, mode: 'create', values: initialIdCardValues });
  const [students, setStudents] = useState([]);
  const [submitting, setSubmitting] = useState(false);
  const [snackbar, setSnackbar] = useState({ open: false, severity: 'success', message: '' });

  const paged = usePagedResource({
    fetchPage: ({ page, pageSize, filters }) => idCardsService.getPage(client, { pageNo: page, pageSize, ...filters }),
    initialFilters: { cardNumber: '', active: '' },
    reloadKey: tenantId,
  });

  useEffect(() => {
    loadStudents();
  }, [client, tenantId]);

  async function loadStudents() {
    try {
      const result = await studentsService.getTenantList(client);
      setStudents(result || []);
    } catch {
      setStudents([]);
    }
  }

  function showSnackbar(message, severity = 'success') {
    setSnackbar({ open: true, severity, message });
  }

  function closeDialog() {
    setDialog({ open: false, mode: 'create', values: initialIdCardValues });
  }

  function openCreate() {
    setDialog({ open: true, mode: 'create', values: initialIdCardValues });
  }

  async function openEdit(row) {
    try {
      const detail = await idCardsService.getById(client, row.id);
      setDialog({ open: true, mode: 'edit', values: mapIdCardToFormValues(detail) });
    } catch (error) {
      showSnackbar(error.message, 'error');
    }
  }

  async function saveDialog() {
    setSubmitting(true);
    try {
      const payload = {
        cardNumber: dialog.values.cardNumber,
        standard: dialog.values.standard,
        section: dialog.values.section,
        issueDate: dialog.values.issueDate,
        expiryDate: dialog.values.expiryDate,
        address: dialog.values.address,
      };

      if (dialog.mode === 'create') {
        await idCardsService.create(client, payload, dialog.values.studId);
      } else {
        await idCardsService.update(client, dialog.values.id, payload);
      }

      closeDialog();
      await Promise.all([paged.loadPage({ page: 1 }), loadStudents()]);
      showSnackbar(`ID card ${dialog.mode === 'create' ? 'created' : 'updated'} successfully`);
    } catch (error) {
      showSnackbar(error.message, 'error');
    } finally {
      setSubmitting(false);
    }
  }

  async function removeIdCard(row) {
    if (!window.confirm('Delete this ID card?')) {
      return;
    }

    try {
      await idCardsService.remove(client, row.id);
      await paged.loadPage();
      showSnackbar('ID card deleted successfully');
    } catch (error) {
      showSnackbar(error.message, 'error');
    }
  }

  return (
    <>
      <IdCardsPage
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
        onDelete={removeIdCard}
      />
      <IdCardDialog
        open={dialog.open}
        mode={dialog.mode}
        values={dialog.values}
        students={students}
        onChange={(key, value) => setDialog((current) => ({ ...current, values: { ...current.values, [key]: value } }))}
        onClose={closeDialog}
        onSave={saveDialog}
        submitting={submitting}
      />
      <FeedbackSnackbar snackbar={snackbar} onClose={() => setSnackbar((current) => ({ ...current, open: false }))} />
    </>
  );
}

export default IdCardsRoute;
