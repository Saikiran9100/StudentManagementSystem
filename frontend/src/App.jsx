import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom';
import TenantProvider from './components/providers/TenantProvider.jsx';
import AppLayout from './components/layout/AppLayout.jsx';
import OverviewRoute from './components/overview/OverviewRoute.jsx';
import StudentsRoute from './components/students/StudentsRoute.jsx';
import DepartmentsRoute from './components/departments/DepartmentsRoute.jsx';
import CoursesRoute from './components/courses/CoursesRoute.jsx';
import BankAccountsRoute from './components/bankAccounts/BankAccountsRoute.jsx';
import IdCardsRoute from './components/idCards/IdCardsRoute.jsx';
import CurrenciesRoute from './components/currencies/CurrenciesRoute.jsx';

function App() {
  return (
    <BrowserRouter>
      <TenantProvider>
        <Routes>
          <Route element={<AppLayout />}>
            <Route path="/" element={<OverviewRoute />} />
            <Route path="/students" element={<StudentsRoute />} />
            <Route path="/departments" element={<DepartmentsRoute />} />
            <Route path="/courses" element={<CoursesRoute />} />
            <Route path="/bank-accounts" element={<BankAccountsRoute />} />
            <Route path="/id-cards" element={<IdCardsRoute />} />
            <Route path="/currencies" element={<CurrenciesRoute />} />
            <Route path="*" element={<Navigate to="/" replace />} />
          </Route>
        </Routes>
      </TenantProvider>
    </BrowserRouter>
  );
}

export default App;
