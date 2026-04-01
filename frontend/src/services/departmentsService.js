export const departmentsService = {
  getTenantList: (client) => client.get('/department/tenants'),
  getPage: (client, params) => client.get('/department/getpages', {
    ...params,
    pageNo: Math.max(0, (params?.pageNo ?? 1) - 1),
  }),
  getById: (client, id) => client.get(`/department/get/${id}`),
  create: (client, payload) => client.post('/department/add', payload),
  update: (client, id, payload) => client.put(`/department/update/${id}`, payload),
  remove: (client, id) => client.delete(`/department/delete/${id}`),
};
