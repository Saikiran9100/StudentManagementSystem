import { useEffect, useState } from 'react';
import CurrenciesPage from './CurrenciesPage.jsx';
import CurrencyDialog from './CurrencyDialog.jsx';
import FeedbackSnackbar from '../shared/FeedbackSnackbar.jsx';
import { useTenant } from '../providers/TenantProvider.jsx';
import { currenciesService } from '../../services/currenciesService.js';
import { toFloat } from '../../lib/config.js';

const initialCurrencyValues = {
  currencyCode: '',
  valueInr: '',
};

function mapCurrencyToFormValues(currency) {
  return {
    id: currency.id || '',
    currencyCode: currency.currencyCode || '',
    valueInr: currency.valueInr ?? '',
  };
}

function CurrenciesRoute() {
  const { client } = useTenant();
  const [rows, setRows] = useState([]);
  const [dialog, setDialog] = useState({ open: false, mode: 'create', values: initialCurrencyValues });
  const [submitting, setSubmitting] = useState(false);
  const [snackbar, setSnackbar] = useState({ open: false, severity: 'success', message: '' });

  useEffect(() => {
    loadCurrencies();
  }, [client]);

  async function loadCurrencies() {
    try {
      const result = await currenciesService.getAll(client);
      setRows(result || []);
    } catch (error) {
      showSnackbar(error.message, 'error');
      setRows([]);
    }
  }

  function showSnackbar(message, severity = 'success') {
    setSnackbar({ open: true, severity, message });
  }

  function closeDialog() {
    setDialog({ open: false, mode: 'create', values: initialCurrencyValues });
  }

  function openCreate() {
    setDialog({ open: true, mode: 'create', values: initialCurrencyValues });
  }

  async function openEdit(row) {
    try {
      const detail = await currenciesService.getById(client, row.id);
      setDialog({ open: true, mode: 'edit', values: mapCurrencyToFormValues(detail) });
    } catch (error) {
      showSnackbar(error.message, 'error');
    }
  }

  async function saveDialog() {
    setSubmitting(true);
    try {
      const payload = {
        currencyCode: dialog.values.currencyCode,
        valueInr: toFloat(dialog.values.valueInr),
      };

      if (dialog.mode === 'create') {
        await currenciesService.create(client, payload);
      } else {
        await currenciesService.update(client, dialog.values.id, payload);
      }

      closeDialog();
      await loadCurrencies();
      showSnackbar(`Currency ${dialog.mode === 'create' ? 'created' : 'updated'} successfully`);
    } catch (error) {
      showSnackbar(error.message, 'error');
    } finally {
      setSubmitting(false);
    }
  }

  async function removeCurrency(row) {
    if (!window.confirm('Delete this currency?')) {
      return;
    }

    try {
      await currenciesService.remove(client, row.id);
      await loadCurrencies();
      showSnackbar('Currency deleted successfully');
    } catch (error) {
      showSnackbar(error.message, 'error');
    }
  }

  return (
    <>
      <CurrenciesPage rows={rows} onCreate={openCreate} onEdit={openEdit} onDelete={removeCurrency} />
      <CurrencyDialog
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

export default CurrenciesRoute;
