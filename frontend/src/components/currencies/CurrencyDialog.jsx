import { Grid, TextField } from '@mui/material';
import BaseDialog from '../shared/BaseDialog.jsx';

function CurrencyDialog({ open, mode, values, onChange, onClose, onSave, submitting }) {
  return (
    <BaseDialog open={open} title={`${mode === 'create' ? 'Add' : 'Edit'} Currency`} onClose={onClose} onSave={onSave} submitting={submitting} submitLabel={mode === 'create' ? 'Create' : 'Save changes'}>
      <Grid container spacing={2} sx={{ pt: 1 }}>
        <Grid size={{ xs: 12, sm: 6 }}><TextField label="Currency code" fullWidth value={values.currencyCode} onChange={(e) => onChange('currencyCode', e.target.value)} /></Grid>
        <Grid size={{ xs: 12, sm: 6 }}><TextField label="Value in INR" type="number" fullWidth value={values.valueInr} onChange={(e) => onChange('valueInr', e.target.value)} /></Grid>
      </Grid>
    </BaseDialog>
  );
}

export default CurrencyDialog;
