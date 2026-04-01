import { useEffect, useState } from 'react';
import BankAccountsPage from './BankAccountsPage.jsx';
import BankAccountDialog from './BankAccountDialog.jsx';
import FeedbackSnackbar from '../shared/FeedbackSnackbar.jsx';
import { useTenant } from '../providers/TenantProvider.jsx';
import { usePagedResource } from '../../hooks/usePagedResource.js';
import { bankAccountsService } from '../../services/bankAccountsService.js';
import { studentsService } from '../../services/studentsService.js';
import { currenciesService } from '../../services/currenciesService.js';
import { toFloat } from '../../lib/config.js';

const initialBankAccountValues = {
  bankName: '',
  accountNo: '',
  balance: '',
  branch: '',
  studId: '',
  currencyCode: '',
};

function mapBankAccountToFormValues(bankAccount) {
  return {
    id: bankAccount.bankId || '',
    bankName: bankAccount.bankName || '',
    accountNo: bankAccount.accountNo || '',
    balance: bankAccount.balance ?? '',
    branch: bankAccount.branch || '',
    studId: bankAccount.student?.studId || '',
    currencyCode: bankAccount.currency?.currencyCode || bankAccount.currencyCode || '',
  };
}

function BankAccountsRoute() {
  const { client, tenantId } = useTenant();
  const [dialog, setDialog] = useState({ open: false, mode: 'create', values: initialBankAccountValues });
  const [students, setStudents] = useState([]);
  const [currencies, setCurrencies] = useState([]);
  const [submitting, setSubmitting] = useState(false);
  const [snackbar, setSnackbar] = useState({ open: false, severity: 'success', message: '' });

  const paged = usePagedResource({
    fetchPage: ({ page, pageSize, filters }) => bankAccountsService.getPage(client, { pageNo: page, pageSize, ...filters }),
    initialFilters: { bankName: '', branch: '' },
    reloadKey: tenantId,
  });

  useEffect(() => {
    loadSupportData();
  }, [client, tenantId]);

  async function loadSupportData() {
    try {
      const [studentRows, currencyRows] = await Promise.all([
        studentsService.getTenantList(client),
        currenciesService.getAll(client),
      ]);
      setStudents(studentRows || []);
      setCurrencies(currencyRows || []);
    } catch {
      setStudents([]);
      setCurrencies([]);
    }
  }

  function showSnackbar(message, severity = 'success') {
    setSnackbar({ open: true, severity, message });
  }

  function closeDialog() {
    setDialog({ open: false, mode: 'create', values: initialBankAccountValues });
  }

  function openCreate() {
    setDialog({ open: true, mode: 'create', values: initialBankAccountValues });
  }

  async function openEdit(row) {
    try {
      const detail = await bankAccountsService.getById(client, row.bankId);
      setDialog({ open: true, mode: 'edit', values: mapBankAccountToFormValues(detail) });
    } catch (error) {
      showSnackbar(error.message, 'error');
    }
  }

  async function saveDialog() {
    setSubmitting(true);
    try {
      const payload = {
        bankName: dialog.values.bankName,
        accountNo: dialog.values.accountNo,
        balance: toFloat(dialog.values.balance),
        branch: dialog.values.branch,
      };

      if (dialog.mode === 'create') {
        await bankAccountsService.create(client, payload, dialog.values.studId, dialog.values.currencyCode);
      } else {
        await bankAccountsService.update(client, dialog.values.id, payload, dialog.values.currencyCode);
      }

      closeDialog();
      await Promise.all([paged.loadPage({ page: 1 }), loadSupportData()]);
      showSnackbar(`Bank account ${dialog.mode === 'create' ? 'created' : 'updated'} successfully`);
    } catch (error) {
      showSnackbar(error.message, 'error');
    } finally {
      setSubmitting(false);
    }
  }

  async function removeBankAccount(row) {
    if (!window.confirm('Delete this bank account?')) {
      return;
    }

    try {
      await bankAccountsService.remove(client, row.bankId);
      await paged.loadPage();
      showSnackbar('Bank account deleted successfully');
    } catch (error) {
      showSnackbar(error.message, 'error');
    }
  }

  return (
    <>
      <BankAccountsPage
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
        onDelete={removeBankAccount}
      />
      <BankAccountDialog
        open={dialog.open}
        mode={dialog.mode}
        values={dialog.values}
        currencies={currencies}
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

export default BankAccountsRoute;
