import PaginatedEntityPage from '../shared/PaginatedEntityPage.jsx';

const columns = [
  { key: 'courseId', label: 'ID' },
  { key: 'courseName', label: 'Course' },
  { key: 'courseFee', label: 'Fee' },
  { key: 'deadLine', label: 'Deadline' },
];

const filters = [
  { key: 'courseName', label: 'Course name' },
  { key: 'deadLine', label: 'Deadline', type: 'date' },
];

function CoursesPage(props) {
  return (
    <PaginatedEntityPage
      title="Courses"
      subtitle="Tenant-aware course pages with backend filtering."
      actionLabel="Add course"
      columns={columns}
      filters={filters}
      {...props}
    />
  );
}

export default CoursesPage;
