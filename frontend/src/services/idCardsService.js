export const idCardsService = {
  getTenantList: (client) => client.get('/idcard/tenants'),
  getPage: (client, params) => client.get('/idcard/getpages', params),
  getById: (client, id) => client.get(`/idcard/get/${id}`),
  create: (client, payload, studId) => client.post('/idcard/add', payload, { studId }),
  update: (client, id, payload) => client.put(`/idcard/update/${id}`, payload),
  remove: (client, id) => client.delete(`/idcard/delete/${id}`),
};
