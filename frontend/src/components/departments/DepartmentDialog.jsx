import { Grid, TextField } from '@mui/material';
import BaseDialog from '../shared/BaseDialog.jsx';

function DepartmentDialog({ open, mode, values, onChange, onClose, onSave, submitting }) {
  return (
    <BaseDialog open={open} title={`${mode === 'create' ? 'Add' : 'Edit'} Department`} onClose={onClose} onSave={onSave} submitting={submitting} submitLabel={mode === 'create' ? 'Create' : 'Save changes'}>
      <Grid container spacing={2} sx={{ pt: 1 }}>
        <Grid size={{ xs: 12 }}><TextField label="Department name" fullWidth value={values.deptName} onChange={(e) => onChange('deptName', e.target.value)} /></Grid>
        <Grid size={{ xs: 12, sm: 6 }}><TextField label="Department code" fullWidth value={values.deptCode} onChange={(e) => onChange('deptCode', e.target.value)} /></Grid>
        <Grid size={{ xs: 12, sm: 6 }}><TextField label="Capacity" type="number" fullWidth value={values.capacity} onChange={(e) => onChange('capacity', e.target.value)} /></Grid>
      </Grid>
    </BaseDialog>
  );
}

export default DepartmentDialog;
