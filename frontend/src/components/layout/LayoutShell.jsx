import {
  Alert,
  AppBar,
  Avatar,
  Backdrop,
  Box,
  Drawer,
  FormControl,
  IconButton,
  InputLabel,
  LinearProgress,
  List,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  ListSubheader,
  MenuItem,
  Select,
  Stack,
  Toolbar,
  Typography,
} from '@mui/material';
import MenuRoundedIcon from '@mui/icons-material/MenuRounded';
import AutoAwesomeRoundedIcon from '@mui/icons-material/AutoAwesomeRounded';
import AddRoundedIcon from '@mui/icons-material/AddRounded';

const drawerWidth = 280;

function LayoutShell({
  children,
  activeSection,
  navigationItems,
  mobileOpen,
  setMobileOpen,
  tenantId,
  tenantOptions,
  loading,
  submitting,
  error,
  onTenantChange,
  onTenantAddRequest,
  onSectionChange,
}) {
  const drawerContent = (
    <Box sx={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
      <Box sx={{ p: 3, background: 'linear-gradient(160deg, #0f172a 0%, #1d4ed8 70%, #38bdf8 100%)', color: 'common.white' }}>
        <Stack direction="row" spacing={2} alignItems="center">
          <Avatar sx={{ bgcolor: 'rgba(255,255,255,0.16)', width: 52, height: 52, boxShadow: '0 10px 30px rgba(0,0,0,0.18)' }}>
            <AutoAwesomeRoundedIcon />
          </Avatar>
          <Box>
            <Typography variant="h6" fontWeight={800}>Campus Control</Typography>
            <Typography variant="body2" sx={{ opacity: 0.84 }}>Modern student management console</Typography>
          </Box>
        </Stack>
      </Box>

      <List sx={{ px: 2, py: 2.5, flexGrow: 1 }}>
        {navigationItems.map((item) => {
          const Icon = item.icon;
          return (
            <ListItemButton
              key={item.id}
              selected={activeSection === item.id}
              onClick={() => {
                onSectionChange(item.path);
                setMobileOpen(false);
              }}
              sx={{
                mb: 1,
                borderRadius: 3.5,
                border: '1px solid transparent',
                '&.Mui-selected': {
                  bgcolor: 'rgba(29, 78, 216, 0.96)',
                  color: 'common.white',
                  borderColor: 'rgba(255,255,255,0.12)',
                  boxShadow: '0 12px 30px rgba(29, 78, 216, 0.24)',
                  '& .MuiListItemIcon-root': { color: 'common.white' },
                },
              }}
            >
              <ListItemIcon sx={{ minWidth: 40 }}><Icon /></ListItemIcon>
              <ListItemText primary={item.label} />
            </ListItemButton>
          );
        })}
      </List>
    </Box>
  );

  return (
    <Box sx={{ minHeight: '100vh', bgcolor: '#eff4fb', backgroundImage: 'radial-gradient(circle at top left, rgba(56,189,248,0.16), transparent 24%), radial-gradient(circle at 80% 20%, rgba(29,78,216,0.14), transparent 24%), linear-gradient(180deg, #f8fbff 0%, #eef4fb 100%)' }}>
      <AppBar position="fixed" color="inherit" elevation={0} sx={{ width: { md: `calc(100% - ${drawerWidth}px)` }, ml: { md: `${drawerWidth}px` }, borderBottom: '1px solid rgba(15, 23, 42, 0.06)', bgcolor: 'rgba(255,255,255,0.74)', backdropFilter: 'blur(18px)' }}>
        <Toolbar sx={{ gap: 2 }}>
          <IconButton sx={{ display: { md: 'none' } }} onClick={() => setMobileOpen(true)}>
            <MenuRoundedIcon />
          </IconButton>
          <Box sx={{ flexGrow: 1 }}>
            <Typography variant="h5" fontWeight={800}>{navigationItems.find((item) => item.id === activeSection)?.label || 'Overview'}</Typography>
            <Typography variant="body2" color="text.secondary">Student Management System</Typography>
          </Box>
          <FormControl size="small" sx={{ minWidth: 200 }}>
            <InputLabel>Tenant</InputLabel>
            <Select
              label="Tenant"
              value={tenantId}
              onChange={(event) => {
                if (event.target.value === '__add_tenant__') {
                  onTenantAddRequest();
                  return;
                }
                onTenantChange(event.target.value);
              }}
              sx={{ bgcolor: 'rgba(255,255,255,0.88)', borderRadius: 2 }}
            >
              {tenantOptions.map((tenant) => (
                <MenuItem key={tenant} value={tenant}>{tenant}</MenuItem>
              ))}
              <ListSubheader sx={{ lineHeight: 1.8 }}>Actions</ListSubheader>
              <MenuItem value="__add_tenant__">
                <Stack direction="row" spacing={1} alignItems="center">
                  <AddRoundedIcon fontSize="small" />
                  <span>Add tenant</span>
                </Stack>
              </MenuItem>
            </Select>
          </FormControl>
        </Toolbar>
        {(loading || submitting) && <LinearProgress />}
      </AppBar>

      <Box component="nav" sx={{ width: { md: drawerWidth }, flexShrink: { md: 0 } }}>
        <Drawer variant="temporary" open={mobileOpen} onClose={() => setMobileOpen(false)} ModalProps={{ keepMounted: true }} sx={{ display: { xs: 'block', md: 'none' }, '& .MuiDrawer-paper': { width: drawerWidth, borderRight: 'none' } }}>
          {drawerContent}
        </Drawer>
        <Drawer variant="permanent" open sx={{ display: { xs: 'none', md: 'block' }, '& .MuiDrawer-paper': { width: drawerWidth, borderRight: '1px solid rgba(15, 23, 42, 0.06)', bgcolor: 'rgba(255,255,255,0.72)', backdropFilter: 'blur(18px)' } }}>
          {drawerContent}
        </Drawer>
      </Box>

      <Box component="main" sx={{ flexGrow: 1, ml: { md: `${drawerWidth}px` }, pt: 12, px: { xs: 2, md: 4 }, pb: 4 }}>
        {error ? <Alert sx={{ mb: 3, borderRadius: 3 }} severity="error">{error}</Alert> : null}
        {children}
      </Box>

      <Backdrop open={submitting} sx={{ color: '#fff', zIndex: (theme) => theme.zIndex.drawer + 2 }}>
        <Typography variant="h6">Submitting request...</Typography>
      </Backdrop>
    </Box>
  );
}

export default LayoutShell;
