import PaginatedEntityPage from '../shared/PaginatedEntityPage.jsx';

const columns = [
  { key: 'deptId', label: 'ID' },
  { key: 'deptName', label: 'Department' },
  { key: 'deptCode', label: 'Code' },
];

const filters = [
  { key: 'deptName', label: 'Department name' },
  { key: 'deptCode', label: 'Department code' },
];

function DepartmentsPage(props) {
  return (
    <PaginatedEntityPage
      title="Departments"
      subtitle="Search departments inside the active tenant only."
      actionLabel="Add department"
      columns={columns}
      filters={filters}
      {...props}
    />
  );
}

export default DepartmentsPage;
