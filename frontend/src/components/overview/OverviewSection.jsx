import { Box, Card, CardContent, Grid, Stack, Typography } from '@mui/material';
import SchoolRoundedIcon from '@mui/icons-material/SchoolRounded';
import ApartmentRoundedIcon from '@mui/icons-material/ApartmentRounded';
import MenuBookRoundedIcon from '@mui/icons-material/MenuBookRounded';
import CurrencyRupeeRoundedIcon from '@mui/icons-material/CurrencyRupeeRounded';
import ArrowOutwardRoundedIcon from '@mui/icons-material/ArrowOutwardRounded';

function OverviewSection({ data, onNavigate }) {
  const cards = [
    { id: 'students', label: 'Students', value: data.students.length, icon: <SchoolRoundedIcon fontSize="large" />, note: 'Student directory and academic records' },
    { id: 'departments', label: 'Departments', value: data.departments.length, icon: <ApartmentRoundedIcon fontSize="large" />, note: 'Department-wise structure and capacity' },
    { id: 'courses', label: 'Courses', value: data.courses.length, icon: <MenuBookRoundedIcon fontSize="large" />, note: 'Course catalog and enrollment flow' },
    { id: 'currencies', label: 'Currencies', value: data.currencies.length, icon: <CurrencyRupeeRoundedIcon fontSize="large" />, note: 'Conversion setup used across modules' },
  ];

  return (
    <Box sx={{ maxWidth: 860, mx: 'auto' }}>
      <Grid container spacing={3} justifyContent="center">
        {cards.map((card) => (
          <Grid key={card.id} size={{ xs: 12, md: 6 }}>
            <Card
              elevation={0}
              sx={{
                borderRadius: 4,
                border: '1px solid rgba(15, 23, 42, 0.08)',
                cursor: 'pointer',
                bgcolor: 'rgba(255,255,255,0.88)',
                boxShadow: '0 18px 36px rgba(15,23,42,0.05)',
                transition: 'transform 0.2s ease, box-shadow 0.2s ease',
                '&:hover': {
                  transform: 'translateY(-4px)',
                  boxShadow: '0 22px 44px rgba(15,23,42,0.09)',
                },
              }}
              onClick={() => onNavigate(card.id)}
            >
              <CardContent sx={{ p: 2.75 }}>
                <Stack spacing={2.2}>
                  <Stack direction="row" justifyContent="space-between" alignItems="center">
                    <Box
                      sx={{
                        width: 58,
                        height: 58,
                        borderRadius: 3.5,
                        display: 'grid',
                        placeItems: 'center',
                        color: 'primary.dark',
                        background: 'linear-gradient(135deg, rgba(191,219,254,0.95) 0%, rgba(125,211,252,0.9) 100%)',
                        border: '1px solid rgba(29,78,216,0.1)',
                        boxShadow: 'inset 0 1px 0 rgba(255,255,255,0.7), 0 10px 20px rgba(29,78,216,0.12)',
                      }}
                    >
                      {card.icon}
                    </Box>
                    <ArrowOutwardRoundedIcon color="primary" />
                  </Stack>
                  <Box>
                    <Typography variant="body2" color="text.secondary">{card.label}</Typography>
                    <Typography variant="h3" fontWeight={900} sx={{ mt: 0.4, fontSize: { xs: '2.1rem', md: '2.4rem' } }}>{card.value}</Typography>
                    <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>{card.note}</Typography>
                  </Box>
                </Stack>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
    </Box>
  );
}

export default OverviewSection;
