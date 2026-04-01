import { Button, Dialog, DialogActions, DialogContent, DialogTitle } from '@mui/material';

function BaseDialog({ open, title, children, onClose, onSave, submitting, submitLabel = 'Save changes' }) {
  return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
      <DialogTitle>{title}</DialogTitle>
      <DialogContent dividers>{children}</DialogContent>
      <DialogActions sx={{ p: 2 }}>
        <Button onClick={onClose} disabled={submitting}>Cancel</Button>
        <Button variant="contained" onClick={onSave} disabled={submitting}>{submitLabel}</Button>
      </DialogActions>
    </Dialog>
  );
}

export default BaseDialog;
