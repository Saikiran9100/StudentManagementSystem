import PaginatedEntityPage from '../shared/PaginatedEntityPage.jsx';

const columns = [
  { key: 'bankId', label: 'ID' },
  { key: 'bankName', label: 'Bank' },
  { key: 'accountNo', label: 'Account No' },
  { key: 'branch', label: 'Branch' },
  { key: 'balance', label: 'Balance' },
];

const filters = [
  { key: 'bankName', label: 'Bank name' },
  { key: 'branch', label: 'Branch' },
];

function BankAccountsPage(props) {
  return (
    <PaginatedEntityPage
      title="Bank Accounts"
      subtitle="Server-side bank account filters for the current tenant."
      actionLabel="Add bank account"
      columns={columns}
      filters={filters}
      {...props}
    />
  );
}

export default BankAccountsPage;
