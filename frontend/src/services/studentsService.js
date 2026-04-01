export const studentsService = {
  getTenantList: (client) => client.get('/student/tenants'),
  getPage: (client, params) => client.get('/student/getpages', params),
  getById: (client, id) => client.get(`/student/get/${id}`),
  create: (client, payload) => client.post('/student/add', payload),
  update: (client, id, payload) => client.put(`/student/update/${id}`, payload),
  remove: (client, id) => client.delete(`/student/delete/${id}`),
  assignDepartment: (client, studentId, deptId) => client.put(`/student/updatedept/${studentId}`, null, { deptId }),
  enrollCourse: (client, studentId, courseId) => client.put(`/student/update/${studentId}/enroll`, null, { courseId }),
  unenrollCourse: (client, studentId, courseId) => client.put(`/student/update/${studentId}/unenroll`, null, { courseId }),
  updateGraduationDate: (client, studentId, graduationDate) => client.patch(`/student/${studentId}/graduation-date`, null, { graduationDate }),
};
