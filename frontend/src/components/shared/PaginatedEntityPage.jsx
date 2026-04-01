import {
  Box,
  Button,
  FormControl,
  Grid,
  IconButton,
  InputLabel,
  MenuItem,
  Paper,
  Select,
  Stack,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TablePagination,
  TableRow,
  TextField,
  Tooltip,
  Typography,
} from '@mui/material';
import AddRoundedIcon from '@mui/icons-material/AddRounded';
import EditRoundedIcon from '@mui/icons-material/EditRounded';
import DeleteRoundedIcon from '@mui/icons-material/DeleteRounded';
import FilterAltRoundedIcon from '@mui/icons-material/FilterAltRounded';
import RestartAltRoundedIcon from '@mui/icons-material/RestartAltRounded';

function PaginatedEntityPage({
  title,
  subtitle,
  actionLabel,
  columns,
  filters = [],
  rows,
  onCreate,
  onEdit,
  onDelete,
  pagination,
  filterDrafts,
  onFilterChange,
  onApplyFilters,
  onResetFilters,
  onPageChange,
  onPageSizeChange,
}) {
  const isPaged = Boolean(pagination);

  return (
    <Paper elevation={0} sx={{ p: { xs: 2, md: 3 }, borderRadius: 4, border: '1px solid rgba(15, 23, 42, 0.08)' }}>
      <Stack direction={{ xs: 'column', md: 'row' }} spacing={2} justifyContent="space-between" alignItems={{ md: 'center' }} sx={{ mb: 3 }}>
        <Box>
          <Typography variant="h5" fontWeight={800}>{title}</Typography>
          <Typography color="text.secondary">{subtitle}</Typography>
        </Box>
        <Button variant="contained" size="large" startIcon={<AddRoundedIcon />} onClick={onCreate}>{actionLabel}</Button>
      </Stack>

      {isPaged && filters.length > 0 && (
        <Paper elevation={0} sx={{ p: 2, mb: 3, borderRadius: 3, bgcolor: 'rgba(29, 78, 216, 0.03)', border: '1px solid rgba(29, 78, 216, 0.1)' }}>
          <Grid container spacing={2}>
            {filters.map((filter) => (
              <Grid key={filter.key} size={{ xs: 12, md: 6, xl: 3 }}>
                {filter.type === 'select' ? (
                  <FormControl fullWidth>
                    <InputLabel>{filter.label}</InputLabel>
                    <Select
                      label={filter.label}
                      value={filterDrafts[filter.key] ?? ''}
                      onChange={(event) => onFilterChange(filter.key, event.target.value)}
                    >
                      {filter.options.map((option) => (
                        <MenuItem key={option.value} value={option.value}>{option.label}</MenuItem>
                      ))}
                    </Select>
                  </FormControl>
                ) : (
                  <TextField
                    fullWidth
                    type={filter.type || 'text'}
                    label={filter.label}
                    value={filterDrafts[filter.key] ?? ''}
                    onChange={(event) => onFilterChange(filter.key, event.target.value)}
                    InputLabelProps={filter.type === 'date' ? { shrink: true } : undefined}
                  />
                )}
              </Grid>
            ))}
          </Grid>
          <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1.5} sx={{ mt: 2 }}>
            <Button variant="contained" startIcon={<FilterAltRoundedIcon />} onClick={onApplyFilters}>Apply filters</Button>
            <Button variant="outlined" startIcon={<RestartAltRoundedIcon />} onClick={onResetFilters}>Reset</Button>
          </Stack>
        </Paper>
      )}

      <TableContainer sx={{ borderRadius: 3, border: '1px solid rgba(15, 23, 42, 0.06)' }}>
        <Table>
          <TableHead>
            <TableRow sx={{ bgcolor: 'rgba(15, 23, 42, 0.03)' }}>
              {columns.map((column) => <TableCell key={column.key} sx={{ fontWeight: 700 }}>{column.label}</TableCell>)}
              <TableCell sx={{ fontWeight: 700 }}>Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {rows.map((row) => (
              <TableRow key={row.studId || row.deptId || row.courseId || row.bankId || row.id}>
                {columns.map((column) => <TableCell key={column.key}>{column.render ? column.render(row) : row[column.key]}</TableCell>)}
                <TableCell>
                  <Stack direction="row" spacing={1}>
                    <Tooltip title="Edit">
                      <IconButton color="primary" onClick={() => onEdit(row)}>
                        <EditRoundedIcon />
                      </IconButton>
                    </Tooltip>
                    {onDelete ? (
                      <Tooltip title="Delete">
                        <IconButton color="error" onClick={() => onDelete(row)}>
                          <DeleteRoundedIcon />
                        </IconButton>
                      </Tooltip>
                    ) : null}
                  </Stack>
                </TableCell>
              </TableRow>
            ))}
            {rows.length === 0 && (
              <TableRow>
                <TableCell colSpan={columns.length + 1}>
                  <Typography color="text.secondary" sx={{ py: 3, textAlign: 'center' }}>No records found.</Typography>
                </TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </TableContainer>

      {isPaged && (
        <TablePagination
          component="div"
          count={pagination.totalElements}
          page={Math.max(0, pagination.page - 1)}
          onPageChange={(_, newPage) => onPageChange(newPage + 1)}
          rowsPerPage={pagination.pageSize}
          onRowsPerPageChange={(event) => onPageSizeChange(Number(event.target.value))}
          rowsPerPageOptions={[5, 10, 20]}
        />
      )}
    </Paper>
  );
}

export default PaginatedEntityPage;
