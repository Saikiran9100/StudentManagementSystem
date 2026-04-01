import { useCallback, useEffect, useRef, useState } from 'react';

export function usePagedResource({ fetchPage, initialFilters, reloadKey = 'initial' }) {
  const [rows, setRows] = useState([]);
  const [pagination, setPagination] = useState({
    page: 1,
    pageSize: 5,
    totalElements: 0,
    totalPages: 0,
  });
  const [filters, setFilters] = useState(initialFilters);
  const [filterDrafts, setFilterDrafts] = useState(initialFilters);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const paginationRef = useRef(pagination);
  const filtersRef = useRef(filters);

  useEffect(() => {
    paginationRef.current = pagination;
  }, [pagination]);

  useEffect(() => {
    filtersRef.current = filters;
  }, [filters]);

  const loadPage = useCallback(async (overrides = {}) => {
    const page = overrides.page ?? paginationRef.current.page;
    const pageSize = overrides.pageSize ?? paginationRef.current.pageSize;
    const activeFilters = overrides.filters ?? filtersRef.current;

    setLoading(true);
    setError('');
    try {
      const response = await fetchPage({ page, pageSize, filters: activeFilters });
      setRows(response?.content || []);
      setPagination({
        page,
        pageSize,
        totalElements: response?.totalElements || 0,
        totalPages: response?.totalPages || 0,
      });
      setFilters(activeFilters);
    } catch (requestError) {
      setError(requestError.message);
      throw requestError;
    } finally {
      setLoading(false);
    }
  }, [fetchPage]);

  useEffect(() => {
    loadPage().catch(() => {});
  }, [reloadKey]);

  function updateFilterDraft(key, value) {
    setFilterDrafts((current) => ({ ...current, [key]: value }));
  }

  function resetFilterDrafts() {
    setFilterDrafts(initialFilters);
    return loadPage({ page: 1, filters: {} });
  }

  function applyFilterDrafts() {
    const normalized = Object.fromEntries(
      Object.entries(filterDrafts).filter(([, value]) => value !== ''),
    );
    return loadPage({ page: 1, filters: normalized });
  }

  return {
    rows,
    pagination,
    filters,
    filterDrafts,
    loading,
    error,
    loadPage,
    updateFilterDraft,
    applyFilterDrafts,
    resetFilterDrafts,
  };
}
