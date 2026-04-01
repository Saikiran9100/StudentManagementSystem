import { Box } from '@mui/material';
import PaginatedEntityPage from '../shared/PaginatedEntityPage.jsx';
import StudentActionsSection from './StudentActionsSection.jsx';

const studentColumns = [
  { key: 'studId', label: 'ID' },
  { key: 'name', label: 'Student', render: (item) => `${item.firstName} ${item.lastName}` },
  { key: 'email', label: 'Email' },
  { key: 'age', label: 'Age' },
  { key: 'cGpa', label: 'CGPA' },
];

const studentFilters = [
  { key: 'email', label: 'Email' },
  { key: 'cGpa', label: 'Min CGPA', type: 'number' },
];

function StudentsPage({
  rows,
  pagination,
  filterDrafts,
  onFilterChange,
  onApplyFilters,
  onResetFilters,
  onPageChange,
  onPageSizeChange,
  onCreate,
  onEdit,
  onDelete,
  studentAction,
  setStudentAction,
  students,
  departments,
  courses,
  selectedStudent,
  onRunAction,
}) {
  return (
    <Box sx={{ display: 'grid', gap: 3 }}>
      <PaginatedEntityPage
        title="Students"
        subtitle="Tenant-based student listing with server-side filtering and pagination."
        actionLabel="Add student"
        columns={studentColumns}
        filters={studentFilters}
        rows={rows}
        pagination={pagination}
        filterDrafts={filterDrafts}
        onFilterChange={onFilterChange}
        onApplyFilters={onApplyFilters}
        onResetFilters={onResetFilters}
        onPageChange={onPageChange}
        onPageSizeChange={onPageSizeChange}
        onCreate={onCreate}
        onEdit={onEdit}
        onDelete={onDelete}
      />
      <StudentActionsSection
        studentAction={studentAction}
        setStudentAction={setStudentAction}
        students={students}
        departments={departments}
        courses={courses}
        selectedStudent={selectedStudent}
        onRunAction={onRunAction}
      />
    </Box>
  );
}

export default StudentsPage;
