import { TextField } from '@mui/material';
import BaseDialog from '../shared/BaseDialog.jsx';

function TenantDialog({ open, value, error, onChange, onClose, onSave, submitting }) {
  return (
    <BaseDialog
      open={open}
      title="Add Tenant"
      onClose={onClose}
      onSave={onSave}
      submitting={submitting}
      submitLabel="Create tenant"
    >
      <TextField
        autoFocus
        fullWidth
        label="Tenant ID"
        value={value}
        onChange={(event) => onChange(event.target.value)}
        placeholder="Enter tenant ID"
        helperText={error || 'Use letters, numbers, underscores, or hyphens.'}
        error={Boolean(error)}
        sx={{ mt: 1 }}
      />
    </BaseDialog>
  );
}

export default TenantDialog;
