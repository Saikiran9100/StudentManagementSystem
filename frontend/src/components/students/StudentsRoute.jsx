import { useEffect, useMemo, useState } from 'react';
import StudentsPage from './StudentsPage.jsx';
import StudentDialog from './StudentDialog.jsx';
import FeedbackSnackbar from '../shared/FeedbackSnackbar.jsx';
import { useTenant } from '../providers/TenantProvider.jsx';
import { usePagedResource } from '../../hooks/usePagedResource.js';
import { studentsService } from '../../services/studentsService.js';
import { departmentsService } from '../../services/departmentsService.js';
import { coursesService } from '../../services/coursesService.js';
import { toFloat, toInteger } from '../../lib/config.js';

const initialStudentValues = {
  firstName: '',
  lastName: '',
  email: '',
  age: '',
  admissionDate: '',
  cGpa: '',
};

function mapStudentToFormValues(student) {
  return {
    id: student.studId || '',
    firstName: student.firstName || '',
    lastName: student.lastName || '',
    email: student.email || '',
    age: student.age ?? '',
    admissionDate: student.admissionDate || '',
    cGpa: student.cGpa ?? '',
  };
}

function StudentsRoute() {
  const { client, tenantId } = useTenant();
  const [dialog, setDialog] = useState({ open: false, mode: 'create', values: initialStudentValues });
  const [supportData, setSupportData] = useState({ students: [], departments: [], courses: [] });
  const [studentAction, setStudentAction] = useState({ studentId: '', deptId: '', courseId: '', graduationDate: '' });
  const [submitting, setSubmitting] = useState(false);
  const [snackbar, setSnackbar] = useState({ open: false, severity: 'success', message: '' });

  const paged = usePagedResource({
    fetchPage: ({ page, pageSize, filters }) => studentsService.getPage(client, { pageNo: page, pageSize, ...filters }),
    initialFilters: { email: '', cGpa: '' },
    reloadKey: tenantId,
  });

  useEffect(() => {
    loadSupportData();
    setStudentAction({ studentId: '', deptId: '', courseId: '', graduationDate: '' });
  }, [client, tenantId]);

  async function loadSupportData() {
    try {
      const [students, departments, courses] = await Promise.all([
        studentsService.getTenantList(client),
        departmentsService.getTenantList(client),
        coursesService.getTenantList(client),
      ]);
      setSupportData({ students: students || [], departments: departments || [], courses: courses || [] });
    } catch {
      setSupportData({ students: [], departments: [], courses: [] });
    }
  }

  const selectedStudent = useMemo(
    () => supportData.students.find((item) => `${item.studId}` === `${studentAction.studentId}`),
    [supportData.students, studentAction.studentId],
  );

  function showSnackbar(message, severity = 'success') {
    setSnackbar({ open: true, severity, message });
  }

  function openCreate() {
    setDialog({ open: true, mode: 'create', values: initialStudentValues });
  }

  async function openEdit(row) {
    try {
      const detail = await studentsService.getById(client, row.studId);
      setDialog({ open: true, mode: 'edit', values: mapStudentToFormValues(detail) });
    } catch (error) {
      showSnackbar(error.message, 'error');
    }
  }

  async function saveDialog() {
    setSubmitting(true);
    try {
      const payload = {
        firstName: dialog.values.firstName,
        lastName: dialog.values.lastName,
        email: dialog.values.email,
        age: toInteger(dialog.values.age),
        admissionDate: dialog.values.admissionDate,
        cGpa: toFloat(dialog.values.cGpa),
      };
      if (dialog.mode === 'create') {
        await studentsService.create(client, payload);
      } else {
        await studentsService.update(client, dialog.values.id, payload);
      }
      setDialog({ open: false, mode: 'create', values: initialStudentValues });
      await Promise.all([paged.loadPage({ page: 1 }), loadSupportData()]);
      showSnackbar(`Student ${dialog.mode === 'create' ? 'created' : 'updated'} successfully`);
    } catch (error) {
      showSnackbar(error.message, 'error');
    } finally {
      setSubmitting(false);
    }
  }

  async function removeStudent(row) {
    if (!window.confirm('Delete this student?')) {
      return;
    }
    try {
      await studentsService.remove(client, row.studId);
      await Promise.all([paged.loadPage(), loadSupportData()]);
      showSnackbar('Student deleted successfully');
    } catch (error) {
      showSnackbar(error.message, 'error');
    }
  }

  async function runStudentAction(action) {
    if (!studentAction.studentId) {
      showSnackbar('Select a student first', 'error');
      return;
    }
    setSubmitting(true);
    try {
      if (action === 'assignDepartment') await studentsService.assignDepartment(client, studentAction.studentId, studentAction.deptId);
      if (action === 'enrollCourse') await studentsService.enrollCourse(client, studentAction.studentId, studentAction.courseId);
      if (action === 'unenrollCourse') await studentsService.unenrollCourse(client, studentAction.studentId, studentAction.courseId);
      if (action === 'graduationDate') await studentsService.updateGraduationDate(client, studentAction.studentId, studentAction.graduationDate);
      await Promise.all([paged.loadPage(), loadSupportData()]);
      showSnackbar('Student action completed successfully');
    } catch (error) {
      showSnackbar(error.message, 'error');
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <>
      <StudentsPage
        rows={paged.rows}
        pagination={paged.pagination}
        filterDrafts={paged.filterDrafts}
        onFilterChange={paged.updateFilterDraft}
        onApplyFilters={() => paged.applyFilterDrafts().catch((error) => showSnackbar(error.message, 'error'))}
        onResetFilters={() => paged.resetFilterDrafts().catch((error) => showSnackbar(error.message, 'error'))}
        onPageChange={(page) => paged.loadPage({ page }).catch((error) => showSnackbar(error.message, 'error'))}
        onPageSizeChange={(pageSize) => paged.loadPage({ page: 1, pageSize }).catch((error) => showSnackbar(error.message, 'error'))}
        onCreate={openCreate}
        onEdit={openEdit}
        onDelete={removeStudent}
        studentAction={studentAction}
        setStudentAction={setStudentAction}
        students={supportData.students}
        departments={supportData.departments}
        courses={supportData.courses}
        selectedStudent={selectedStudent}
        onRunAction={runStudentAction}
      />
      <StudentDialog open={dialog.open} mode={dialog.mode} values={dialog.values} onChange={(key, value) => setDialog((current) => ({ ...current, values: { ...current.values, [key]: value } }))} onClose={() => setDialog({ open: false, mode: 'create', values: initialStudentValues })} onSave={saveDialog} submitting={submitting} />
      <FeedbackSnackbar snackbar={snackbar} onClose={() => setSnackbar((current) => ({ ...current, open: false }))} />
    </>
  );
}

export default StudentsRoute;
