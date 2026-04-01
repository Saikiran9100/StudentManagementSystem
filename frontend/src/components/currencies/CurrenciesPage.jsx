import PaginatedEntityPage from '../shared/PaginatedEntityPage.jsx';

const columns = [
  { key: 'id', label: 'ID' },
  { key: 'currencyCode', label: 'Code' },
  { key: 'valueInr', label: 'Value in INR' },
];

function CurrenciesPage(props) {
  return (
    <PaginatedEntityPage
      title="Currencies"
      subtitle="Manage conversion codes used when creating courses and bank accounts."
      actionLabel="Add currency"
      columns={columns}
      rows={props.rows}
      onCreate={props.onCreate}
      onEdit={props.onEdit}
      onDelete={props.onDelete}
    />
  );
}

export default CurrenciesPage;
