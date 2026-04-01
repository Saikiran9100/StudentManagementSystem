import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import OverviewSection from './OverviewSection.jsx';
import { useTenant } from '../providers/TenantProvider.jsx';
import { loadOverviewData } from '../../services/overviewService.js';

function OverviewRoute() {
  const navigate = useNavigate();
  const { client, tenantId } = useTenant();
  const [data, setData] = useState({ students: [], departments: [], courses: [], bankAccounts: [], idCards: [], currencies: [] });

  useEffect(() => {
    loadOverviewData(client).then(setData).catch(() => {});
  }, [client, tenantId]);

  return <OverviewSection data={data} onNavigate={(section) => navigate(section === 'overview' ? '/' : `/${section === 'bankAccounts' ? 'bank-accounts' : section === 'idCards' ? 'id-cards' : section}`)} />;
}

export default OverviewRoute;
