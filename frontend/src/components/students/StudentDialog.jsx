import { Grid, TextField } from '@mui/material';
import BaseDialog from '../shared/BaseDialog.jsx';

function StudentDialog({ open, mode, values, onChange, onClose, onSave, submitting }) {
  return (
    <BaseDialog open={open} title={`${mode === 'create' ? 'Add' : 'Edit'} Student`} onClose={onClose} onSave={onSave} submitting={submitting} submitLabel={mode === 'create' ? 'Create' : 'Save changes'}>
      <Grid container spacing={2} sx={{ pt: 1 }}>
        <Grid size={{ xs: 12, sm: 6 }}><TextField label="First name" fullWidth value={values.firstName} onChange={(e) => onChange('firstName', e.target.value)} /></Grid>
        <Grid size={{ xs: 12, sm: 6 }}><TextField label="Last name" fullWidth value={values.lastName} onChange={(e) => onChange('lastName', e.target.value)} /></Grid>
        <Grid size={{ xs: 12 }}><TextField label="Email" fullWidth value={values.email} onChange={(e) => onChange('email', e.target.value)} /></Grid>
        <Grid size={{ xs: 12, sm: 6 }}><TextField label="Age" type="number" fullWidth value={values.age} onChange={(e) => onChange('age', e.target.value)} /></Grid>
        <Grid size={{ xs: 12, sm: 6 }}><TextField label="CGPA" type="number" fullWidth value={values.cGpa} onChange={(e) => onChange('cGpa', e.target.value)} /></Grid>
        <Grid size={{ xs: 12 }}><TextField label="Admission date" type="date" fullWidth InputLabelProps={{ shrink: true }} value={values.admissionDate} onChange={(e) => onChange('admissionDate', e.target.value)} /></Grid>
      </Grid>
    </BaseDialog>
  );
}

export default StudentDialog;
