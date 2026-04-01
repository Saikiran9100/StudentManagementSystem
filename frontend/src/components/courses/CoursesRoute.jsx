import { useEffect, useState } from 'react';
import CoursesPage from './CoursesPage.jsx';
import CourseDialog from './CourseDialog.jsx';
import FeedbackSnackbar from '../shared/FeedbackSnackbar.jsx';
import { useTenant } from '../providers/TenantProvider.jsx';
import { usePagedResource } from '../../hooks/usePagedResource.js';
import { coursesService } from '../../services/coursesService.js';
import { currenciesService } from '../../services/currenciesService.js';
import { toFloat, toInteger } from '../../lib/config.js';

const initialCourseValues = {
  courseName: '',
  courseFee: '',
  deadLine: '',
  maxCapacity: '',
  currencyCode: '',
};

function mapCourseToFormValues(course) {
  return {
    id: course.courseId || '',
    courseName: course.courseName || '',
    courseFee: course.courseFee ?? '',
    deadLine: course.deadLine || '',
    maxCapacity: course.maxCapacity ?? '',
    currencyCode: course.currency?.currencyCode || course.currencyCode || '',
  };
}

function CoursesRoute() {
  const { client, tenantId } = useTenant();
  const [dialog, setDialog] = useState({ open: false, mode: 'create', values: initialCourseValues });
  const [currencies, setCurrencies] = useState([]);
  const [submitting, setSubmitting] = useState(false);
  const [snackbar, setSnackbar] = useState({ open: false, severity: 'success', message: '' });

  const paged = usePagedResource({
    fetchPage: ({ page, pageSize, filters }) => coursesService.getPage(client, { pageNo: page, pageSize, ...filters }),
    initialFilters: { courseName: '', deadLine: '' },
    reloadKey: tenantId,
  });

  useEffect(() => {
    loadCurrencies();
  }, [client, tenantId]);

  async function loadCurrencies() {
    try {
      const result = await currenciesService.getAll(client);
      setCurrencies(result || []);
    } catch {
      setCurrencies([]);
    }
  }

  function showSnackbar(message, severity = 'success') {
    setSnackbar({ open: true, severity, message });
  }

  function closeDialog() {
    setDialog({ open: false, mode: 'create', values: initialCourseValues });
  }

  function openCreate() {
    setDialog({ open: true, mode: 'create', values: initialCourseValues });
  }

  async function openEdit(row) {
    try {
      const detail = await coursesService.getById(client, row.courseId);
      setDialog({ open: true, mode: 'edit', values: mapCourseToFormValues(detail) });
    } catch (error) {
      showSnackbar(error.message, 'error');
    }
  }

  async function saveDialog() {
    setSubmitting(true);
    try {
      const payload = {
        courseName: dialog.values.courseName,
        courseFee: toFloat(dialog.values.courseFee),
        deadLine: dialog.values.deadLine,
        maxCapacity: toInteger(dialog.values.maxCapacity),
      };

      if (dialog.mode === 'create') {
        await coursesService.create(client, payload, dialog.values.currencyCode);
      } else {
        await coursesService.update(client, dialog.values.id, payload, dialog.values.currencyCode);
      }

      closeDialog();
      await Promise.all([paged.loadPage({ page: 1 }), loadCurrencies()]);
      showSnackbar(`Course ${dialog.mode === 'create' ? 'created' : 'updated'} successfully`);
    } catch (error) {
      showSnackbar(error.message, 'error');
    } finally {
      setSubmitting(false);
    }
  }

  async function removeCourse(row) {
    if (!window.confirm('Delete this course?')) {
      return;
    }

    try {
      await coursesService.remove(client, row.courseId);
      await paged.loadPage();
      showSnackbar('Course deleted successfully');
    } catch (error) {
      showSnackbar(error.message, 'error');
    }
  }

  return (
    <>
      <CoursesPage
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
        onDelete={removeCourse}
      />
      <CourseDialog
        open={dialog.open}
        mode={dialog.mode}
        values={dialog.values}
        currencies={currencies}
        onChange={(key, value) => setDialog((current) => ({ ...current, values: { ...current.values, [key]: value } }))}
        onClose={closeDialog}
        onSave={saveDialog}
        submitting={submitting}
      />
      <FeedbackSnackbar snackbar={snackbar} onClose={() => setSnackbar((current) => ({ ...current, open: false }))} />
    </>
  );
}

export default CoursesRoute;
