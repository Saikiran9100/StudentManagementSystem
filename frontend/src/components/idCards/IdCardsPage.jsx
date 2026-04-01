import PaginatedEntityPage from '../shared/PaginatedEntityPage.jsx';

const columns = [
  { key: 'id', label: 'ID' },
  { key: 'cardNumber', label: 'Card No' },
  { key: 'section', label: 'Section' },
  { key: 'address', label: 'Address' },
  { key: 'active', label: 'Status', render: (item) => (item.active ? 'Active' : 'Inactive') },
];

const filters = [
  { key: 'cardNumber', label: 'Card number' },
  {
    key: 'active',
    label: 'Status',
    type: 'select',
    options: [
      { value: '', label: 'All' },
      { value: 'true', label: 'Active' },
      { value: 'false', label: 'Inactive' },
    ],
  },
];

function IdCardsPage(props) {
  return (
    <PaginatedEntityPage
      title="ID Cards"
      subtitle="Tenant-only ID card search with pagination."
      actionLabel="Add ID card"
      columns={columns}
      filters={filters}
      {...props}
    />
  );
}

export default IdCardsPage;
