import { studentsService } from './studentsService.js';
import { departmentsService } from './departmentsService.js';
import { coursesService } from './coursesService.js';
import { bankAccountsService } from './bankAccountsService.js';
import { idCardsService } from './idCardsService.js';
import { currenciesService } from './currenciesService.js';

export async function loadOverviewData(client) {
  const [students, departments, courses, bankAccounts, idCards, currencies] = await Promise.all([
    studentsService.getTenantList(client),
    departmentsService.getTenantList(client),
    coursesService.getTenantList(client),
    bankAccountsService.getTenantList(client),
    idCardsService.getTenantList(client),
    currenciesService.getAll(client),
  ]);

  return {
    students: students || [],
    departments: departments || [],
    courses: courses || [],
    bankAccounts: bankAccounts || [],
    idCards: idCards || [],
    currencies: currencies || [],
  };
}
