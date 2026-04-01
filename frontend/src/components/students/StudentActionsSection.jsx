import { Button, FormControl, Grid, InputLabel, MenuItem, Paper, Select, Stack, TextField, Typography } from '@mui/material';

function StudentActionsSection({ studentAction, setStudentAction, students, departments, courses, selectedStudent, onRunAction }) {
  return (
    <Paper elevation={0} sx={{ p: { xs: 2, md: 3 }, borderRadius: 4, border: '1px solid rgba(15, 23, 42, 0.08)' }}>
      <Typography variant="h5" fontWeight={800} gutterBottom>Student Actions</Typography>
      {/*<Typography color="text.secondary" sx={{ mb: 3 }}>Use backend relation endpoints without leaving the students page.</Typography>*/}

      <Grid container spacing={3}>
        <Grid size={{ xs: 12, lg: 4 }}>
          <Stack spacing={2}>
            <FormControl fullWidth>
              <InputLabel>Student</InputLabel>
              <Select label="Student" value={studentAction.studentId} onChange={(event) => setStudentAction((current) => ({ ...current, studentId: event.target.value }))}>
                <MenuItem value=""><em>Select student</em></MenuItem>
                {students.map((student) => (
                  <MenuItem key={student.studId} value={student.studId}>
                    {student.email}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
            <Paper elevation={0} sx={{ p: 2, borderRadius: 3, bgcolor: 'rgba(29,78,216,0.05)' }}>
              <Typography variant="subtitle2" fontWeight={700}>Selected Student</Typography>
              <Typography sx={{ mt: 1 }} fontWeight={700}>{selectedStudent ? `${selectedStudent.firstName} ${selectedStudent.lastName}` : 'No student selected'}</Typography>
              <Typography variant="body2" color="text.secondary">{selectedStudent?.email || 'Choose a student to run relation actions.'}</Typography>
            </Paper>
          </Stack>
        </Grid>

        <Grid size={{ xs: 12, lg: 8 }}>
          <Grid container spacing={2}>
            <Grid size={{ xs: 12, md: 6 }}>
              <ActionCard title="Assign Department">
                <Select displayEmpty value={studentAction.deptId} onChange={(event) => setStudentAction((current) => ({ ...current, deptId: event.target.value }))}>
                  <MenuItem value=""><em>Select department</em></MenuItem>
                  {departments.map((department) => <MenuItem key={department.deptId} value={department.deptId}>{department.deptCode} - {department.deptName}</MenuItem>)}
                </Select>
                <Button variant="contained" onClick={() => onRunAction('assignDepartment')}>Update department</Button>
              </ActionCard>
            </Grid>

            <Grid size={{ xs: 12, md: 6 }}>
              <ActionCard title="Course Enrollment">
                <Select displayEmpty value={studentAction.courseId} onChange={(event) => setStudentAction((current) => ({ ...current, courseId: event.target.value }))}>
                  <MenuItem value=""><em>Select course</em></MenuItem>
                  {courses.map((course) => <MenuItem key={course.courseId} value={course.courseId}>{course.courseName}</MenuItem>)}
                </Select>
                <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1.5}>
                  <Button fullWidth variant="contained" onClick={() => onRunAction('enrollCourse')}>Enroll</Button>
                  <Button fullWidth variant="outlined" onClick={() => onRunAction('unenrollCourse')}>Unenroll</Button>
                </Stack>
              </ActionCard>
            </Grid>

            <Grid size={{ xs: 12, md: 6 }}>
              <ActionCard title="Graduation Date">
                <TextField type="date" value={studentAction.graduationDate} onChange={(event) => setStudentAction((current) => ({ ...current, graduationDate: event.target.value }))} InputLabelProps={{ shrink: true }} />
                <Button variant="contained" onClick={() => onRunAction('graduationDate')}>Save graduation date</Button>
              </ActionCard>
            </Grid>
          </Grid>
        </Grid>
      </Grid>
    </Paper>
  );
}

function ActionCard({ title, children, danger = false }) {
  return (
    <Paper elevation={0} sx={{ p: 2.5, borderRadius: 3, border: '1px solid rgba(15, 23, 42, 0.08)', bgcolor: danger ? 'rgba(220, 38, 38, 0.04)' : 'transparent', height: '100%' }}>
      <Stack spacing={2}>
        <Typography variant="subtitle1" fontWeight={700}>{title}</Typography>
        {children}
      </Stack>
    </Paper>
  );
}

export default StudentActionsSection;
