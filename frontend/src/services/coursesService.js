export const coursesService = {
  getTenantList: (client) => client.get('/course/tenants'),
  getPage: (client, params) => client.get('/course/getpages', params),
  getById: (client, id) => client.get(`/course/get/${id}`),
  create: (client, payload, currencyCode) => client.post('/course/add', payload, { currencyCode }),
  update: (client, id, payload, currencyCode) => client.put(`/course/update/${id}`, payload, { currencyCode }),
  remove: (client, id) => client.delete(`/course/delete/${id}`),
};
