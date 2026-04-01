import SchoolRoundedIcon from '@mui/icons-material/SchoolRounded';
import ApartmentRoundedIcon from '@mui/icons-material/ApartmentRounded';
import MenuBookRoundedIcon from '@mui/icons-material/MenuBookRounded';
import AccountBalanceRoundedIcon from '@mui/icons-material/AccountBalanceRounded';
import BadgeRoundedIcon from '@mui/icons-material/BadgeRounded';
import CurrencyRupeeRoundedIcon from '@mui/icons-material/CurrencyRupeeRounded';
import DashboardRoundedIcon from '@mui/icons-material/DashboardRounded';

export const navigationItems = [
  { id: 'overview', path: '/', label: 'Overview', icon: DashboardRoundedIcon },
  { id: 'students', path: '/students', label: 'Students', icon: SchoolRoundedIcon },
  { id: 'departments', path: '/departments', label: 'Departments', icon: ApartmentRoundedIcon },
  { id: 'courses', path: '/courses', label: 'Courses', icon: MenuBookRoundedIcon },
  { id: 'bankAccounts', path: '/bank-accounts', label: 'Bank Accounts', icon: AccountBalanceRoundedIcon },
  { id: 'idCards', path: '/id-cards', label: 'ID Cards', icon: BadgeRoundedIcon },
  { id: 'currencies', path: '/currencies', label: 'Currencies', icon: CurrencyRupeeRoundedIcon },
];

export function toInteger(value) {
  return value === '' ? null : Number.parseInt(value, 10);
}

export function toFloat(value) {
  return value === '' ? null : Number.parseFloat(value);
}
