export const currenciesService = {
  getAll: (client) => client.get('/currency/get'),
  getById: (client, id) => client.get(`/currency/get/${id}`),
  create: (client, payload) => client.post('/currency/add', payload),
  update: (client, id, payload) => client.put(`/currency/update/${id}`, payload),
  remove: (client, id) => client.delete(`/currency/delete/${id}`),
};
