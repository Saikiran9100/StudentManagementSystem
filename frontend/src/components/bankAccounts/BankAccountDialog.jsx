import { FormControl, Grid, InputLabel, MenuItem, Select, TextField } from '@mui/material';
import BaseDialog from '../shared/BaseDialog.jsx';

function BankAccountDialog({ open, mode, values, currencies, students, onChange, onClose, onSave, submitting }) {
  return (
    <BaseDialog open={open} title={`${mode === 'create' ? 'Add' : 'Edit'} Bank Account`} onClose={onClose} onSave={onSave} submitting={submitting} submitLabel={mode === 'create' ? 'Create' : 'Save changes'}>
      <Grid container spacing={2} sx={{ pt: 1 }}>
        <Grid size={{ xs: 12 }}><TextField label="Bank name" fullWidth value={values.bankName} onChange={(e) => onChange('bankName', e.target.value)} /></Grid>
        <Grid size={{ xs: 12, sm: 6 }}><TextField label="Account number" fullWidth value={values.accountNo} onChange={(e) => onChange('accountNo', e.target.value)} /></Grid>
        <Grid size={{ xs: 12, sm: 6 }}><TextField label="Balance" type="number" fullWidth value={values.balance} onChange={(e) => onChange('balance', e.target.value)} /></Grid>
        <Grid size={{ xs: 12, sm: 6 }}><TextField label="Branch" fullWidth value={values.branch} onChange={(e) => onChange('branch', e.target.value)} /></Grid>
        <Grid size={{ xs: 12, sm: 6 }}>
          <FormControl fullWidth>
            <InputLabel>Currency</InputLabel>
            <Select label="Currency" value={values.currencyCode} onChange={(e) => onChange('currencyCode', e.target.value)}>
              {currencies.map((currency) => <MenuItem key={currency.id} value={currency.currencyCode}>{currency.currencyCode}</MenuItem>)}
            </Select>
          </FormControl>
        </Grid>
        {!values.id && (
          <Grid size={{ xs: 12 }}>
            <FormControl fullWidth>
              <InputLabel>Student</InputLabel>
              <Select label="Student" value={values.studId} onChange={(e) => onChange('studId', e.target.value)}>
                {students.map((student) => <MenuItem key={student.studId} value={student.studId}>{student.email}</MenuItem>)}
              </Select>
            </FormControl>
          </Grid>
        )}
      </Grid>
    </BaseDialog>
  );
}

export default BankAccountDialog;
