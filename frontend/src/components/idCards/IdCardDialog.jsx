import { FormControl, Grid, InputLabel, MenuItem, Select, TextField } from '@mui/material';
import BaseDialog from '../shared/BaseDialog.jsx';

function IdCardDialog({ open, mode, values, students, onChange, onClose, onSave, submitting }) {
  return (
    <BaseDialog open={open} title={`${mode === 'create' ? 'Add' : 'Edit'} ID Card`} onClose={onClose} onSave={onSave} submitting={submitting} submitLabel={mode === 'create' ? 'Create' : 'Save changes'}>
      <Grid container spacing={2} sx={{ pt: 1 }}>
        <Grid size={{ xs: 12, sm: 6 }}><TextField label="Card number" fullWidth value={values.cardNumber} onChange={(e) => onChange('cardNumber', e.target.value)} /></Grid>
        <Grid size={{ xs: 12, sm: 3 }}><TextField label="Standard" fullWidth value={values.standard} onChange={(e) => onChange('standard', e.target.value)} /></Grid>
        <Grid size={{ xs: 12, sm: 3 }}><TextField label="Section" fullWidth value={values.section} onChange={(e) => onChange('section', e.target.value)} /></Grid>
        <Grid size={{ xs: 12, sm: 6 }}><TextField label="Issue date" type="date" fullWidth InputLabelProps={{ shrink: true }} value={values.issueDate} onChange={(e) => onChange('issueDate', e.target.value)} /></Grid>
        <Grid size={{ xs: 12, sm: 6 }}><TextField label="Expiry date" type="date" fullWidth InputLabelProps={{ shrink: true }} value={values.expiryDate} onChange={(e) => onChange('expiryDate', e.target.value)} /></Grid>
        <Grid size={{ xs: 12 }}><TextField label="Address" fullWidth multiline minRows={3} value={values.address} onChange={(e) => onChange('address', e.target.value)} /></Grid>
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

export default IdCardDialog;
