export const bankAccountsService = {
  getTenantList: (client) => client.get('/bank/tenants'),
  getPage: (client, params) => client.get('/bank/getpages', params),
  getById: (client, id) => client.get(`/bank/get/${id}`),
  create: (client, payload, studId, currencyCode) => client.post('/bank/add', payload, { studId, currencyCode }),
  update: (client, id, payload, currencyCode) => client.put(`/bank/update/${id}`, payload, { currencyCode }),
  remove: (client, id) => client.delete(`/bank/delete/${id}`),
};
