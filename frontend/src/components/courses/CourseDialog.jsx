import { FormControl, Grid, InputLabel, MenuItem, Select, TextField } from '@mui/material';
import BaseDialog from '../shared/BaseDialog.jsx';

function CourseDialog({ open, mode, values, currencies, onChange, onClose, onSave, submitting }) {
  return (
    <BaseDialog open={open} title={`${mode === 'create' ? 'Add' : 'Edit'} Course`} onClose={onClose} onSave={onSave} submitting={submitting} submitLabel={mode === 'create' ? 'Create' : 'Save changes'}>
      <Grid container spacing={2} sx={{ pt: 1 }}>
        <Grid size={{ xs: 12 }}><TextField label="Course name" fullWidth value={values.courseName} onChange={(e) => onChange('courseName', e.target.value)} /></Grid>
        <Grid size={{ xs: 12, sm: 6 }}><TextField label="Course fee" type="number" fullWidth value={values.courseFee} onChange={(e) => onChange('courseFee', e.target.value)} /></Grid>
        <Grid size={{ xs: 12, sm: 6 }}><TextField label="Max capacity" type="number" fullWidth value={values.maxCapacity} onChange={(e) => onChange('maxCapacity', e.target.value)} /></Grid>
        <Grid size={{ xs: 12, sm: 6 }}><TextField label="Deadline" type="date" fullWidth InputLabelProps={{ shrink: true }} value={values.deadLine} onChange={(e) => onChange('deadLine', e.target.value)} /></Grid>
        <Grid size={{ xs: 12, sm: 6 }}>
          <FormControl fullWidth>
            <InputLabel>Currency</InputLabel>
            <Select label="Currency" value={values.currencyCode} onChange={(e) => onChange('currencyCode', e.target.value)}>
              {currencies.map((currency) => <MenuItem key={currency.id} value={currency.currencyCode}>{currency.currencyCode}</MenuItem>)}
            </Select>
          </FormControl>
        </Grid>
      </Grid>
    </BaseDialog>
  );
}

export default CourseDialog;
